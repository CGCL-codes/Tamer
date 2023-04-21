package cat.uvic.android;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
* @author ANNA
*
*/
public class ResultBitmap {

    Bitmap bitmap;

    Rect region;

    /**
* @return image bitmap
*/
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
* @param bitmap
*/
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
* @return processed region
*/
    public Rect getRegion() {
        return region;
    }

    /**
* @param region
*/
    public void setRegion(Rect region) {
        this.region = region;
    }

    /**
* @param bitmap
* @param region
*/
    public ResultBitmap(final Bitmap bitmap, final Rect region) {
        super();
        this.bitmap = bitmap;
        this.region = region;
    }

    /**
*
*/
    public ResultBitmap() {
    }
}
