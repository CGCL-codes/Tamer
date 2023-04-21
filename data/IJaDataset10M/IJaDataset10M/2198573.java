package org.seehawk.android.cameras;

import java.util.ArrayList;
import java.util.Hashtable;
import org.seehawk.android.data.Image;
import org.seehawk.core.data.Region;
import org.seehawk.core.tools.Calculator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.CameraDevice;
import android.hardware.CameraDevice.CaptureParams;

public class ImageCamera extends BaseCamera {

    private class Size {

        public int height;

        public int width;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }

        /** Returns if the input size matches. */
        public boolean equals(Size size) {
            return ((this.width == size.width) && (this.height == size.height));
        }

        /** Returns the hash code for this object. */
        public int hashCode() {
            return ((this.width * 1000) + this.height);
        }
    }

    private CaptureParams captureParams;

    private int currentFileIdx;

    private ArrayList<Hashtable<Size, Image>> images;

    private Size origSize;

    private Size currentSize;

    public ImageCamera(ArrayList<Image> images) {
        this.origSize = new Size(0, 0);
        this.currentSize = this.origSize;
        this.images = new ArrayList<Hashtable<Size, Image>>();
        for (Image image : images) {
            Hashtable<Size, Image> sizeHash = new Hashtable<Size, Image>();
            sizeHash.put(this.origSize, image);
            this.images.add(sizeHash);
        }
        this.currentFileIdx = -1;
    }

    /** Captures from the camera onto the canvas. */
    public boolean capture(Canvas canvas) {
        if (this.images.size() <= 0) {
            return false;
        }
        this.currentFileIdx += 1;
        if (this.currentFileIdx >= this.images.size()) {
            this.currentFileIdx = 0;
        }
        Hashtable<Size, Image> imageSizes = this.images.get(this.currentFileIdx);
        if (imageSizes == null) {
            return false;
        }
        Image image = imageSizes.get(this.currentSize);
        if (image == null) {
            return false;
        }
        canvas.drawBitmap(image.getBitmap(), new Rect(0, 0, this.currentSize.width, this.currentSize.height), new Rect(0, 0, canvas.getBitmapWidth(), canvas.getBitmapHeight()), new Paint());
        return true;
    }

    /** Closes the camera. */
    public void close() {
    }

    /** Returns if the camera is ready. */
    public boolean isReady() {
        return true;
    }

    /** Sets the camera capture params. */
    public void setCaptureParams(CameraDevice.CaptureParams captureParams) {
        this.captureParams = captureParams;
        this.currentSize = new Size(this.captureParams.outputWidth, this.captureParams.outputHeight);
        Region newRegion = new Region(this.currentSize.width, this.currentSize.height);
        for (Hashtable<Size, Image> imageSizes : this.images) {
            if (imageSizes.containsKey(this.currentSize)) {
                continue;
            }
            Image origImage = imageSizes.get(this.origSize);
            Image newImage = (Image) Calculator.alterImage(origImage, newRegion);
            imageSizes.put(this.currentSize, newImage);
        }
    }
}
