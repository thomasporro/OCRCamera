package unipd.se18.ocrcamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;

/**
 * This class is used to calculate the brightness of an image in Bitmap format
 * @author Pietro Balzan
 */
public class BrightnessRecognition {
    Handler handler;
    private Context appContext;
    private final Bitmap image;  // the bitmap containing the image to analyze
    private static final String TAG = "BrightnessRecognition";


    /**
     * Constructor of a BrightnessRecognition object containing a Bitmap
     * @param bmp the bitmap of the image to calculate the brightness of
     * @modify image with the new bitmap
     * @author Pietro Balzan
     */

    public BrightnessRecognition(Bitmap bmp, Context c, Handler h){
        appContext= c;
        image = bmp;
        handler= h;
    }

    /**
     * This method calculates the brightness of the image and prompts a Toast to the user
     * @author Pietro Balzan - some changes by Francesco Pham
     * @param UpperBound the toast will be launched if brightness exceeds this value.
     * @param LowerBound the toast will be launched if brightness is lower than this value.
     * @param pixelSkip how many pixels to skip each pixel. Higher values result in better performance,
     *                     but a more rough estimate. When pixelSpacing = 1, the method actually
     *                     calculates the real average brightness, not an estimate.
     *                     Do not use values for pixelSpacing that are smaller than 1.
     */
    public void imgBrightness(int UpperBound, int LowerBound, int pixelSkip){
        int width = image.getWidth();
        int height = image.getHeight();
        int R, G, B, pixel;
        int totalPixels = 0;
        double totBrightness=0;

        for(int x = 0; x < width; x+=pixelSkip) {
            for (int y = 0; y < height; y+=pixelSkip) {
                // get pixel color
                pixel = image.getPixel(x, y);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                //  RGB/Luma conversion formula, dermines luminance of a pixel
                double brightness = (0.2126 * R + 0.7152 * G + 0.0722 * B);
                totBrightness+= brightness;
                totalPixels++;
            }
        }

        final String tooBright = "The picture might be too bright";
        final String tooDark = "The picture might be too dark";
        double media= totBrightness/totalPixels;

        if (media > UpperBound){
            // image is too bright
            Log.d(TAG, "too bright image");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(appContext, tooBright, Toast.LENGTH_LONG).show();
                }
            });
        }
        else if (media < LowerBound){
            //image is too dark
            Log.d(TAG, "too dark image");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(appContext, tooDark, Toast.LENGTH_LONG).show();
                }
            });
        }
        else Log.d(TAG, "good image"); // image is neither too bright nor too dark

    }
}