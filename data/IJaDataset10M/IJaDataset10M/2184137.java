package org.apache.harmony.awt.gl.image;

import com.google.code.appengine.awt.Point;
import com.google.code.appengine.awt.Rectangle;
import com.google.code.appengine.awt.image.DataBuffer;
import com.google.code.appengine.awt.image.Raster;
import com.google.code.appengine.awt.image.SampleModel;
import com.google.code.appengine.awt.image.WritableRaster;

public class OrdinaryWritableRaster extends WritableRaster {

    public OrdinaryWritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle aRegion, Point sampleModelTranslate, WritableRaster parent) {
        super(sampleModel, dataBuffer, aRegion, sampleModelTranslate, parent);
    }

    public OrdinaryWritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point origin) {
        super(sampleModel, dataBuffer, origin);
    }

    public OrdinaryWritableRaster(SampleModel sampleModel, Point origin) {
        super(sampleModel, origin);
    }

    @Override
    public void setDataElements(int x, int y, Object inData) {
        super.setDataElements(x, y, inData);
    }

    @Override
    public void setDataElements(int x, int y, int w, int h, Object inData) {
        super.setDataElements(x, y, w, h, inData);
    }

    @Override
    public WritableRaster createWritableChild(int parentX, int parentY, int w, int h, int childMinX, int childMinY, int[] bandList) {
        return super.createWritableChild(parentX, parentY, w, h, childMinX, childMinY, bandList);
    }

    @Override
    public WritableRaster createWritableTranslatedChild(int childMinX, int childMinY) {
        return super.createWritableTranslatedChild(childMinX, childMinY);
    }

    @Override
    public WritableRaster getWritableParent() {
        return super.getWritableParent();
    }

    @Override
    public void setRect(Raster srcRaster) {
        super.setRect(srcRaster);
    }

    @Override
    public void setRect(int dx, int dy, Raster srcRaster) {
        super.setRect(dx, dy, srcRaster);
    }

    @Override
    public void setDataElements(int x, int y, Raster inRaster) {
        super.setDataElements(x, y, inRaster);
    }

    @Override
    public void setPixel(int x, int y, int[] iArray) {
        super.setPixel(x, y, iArray);
    }

    @Override
    public void setPixel(int x, int y, float[] fArray) {
        super.setPixel(x, y, fArray);
    }

    @Override
    public void setPixel(int x, int y, double[] dArray) {
        super.setPixel(x, y, dArray);
    }

    @Override
    public void setPixels(int x, int y, int w, int h, int[] iArray) {
        super.setPixels(x, y, w, h, iArray);
    }

    @Override
    public void setPixels(int x, int y, int w, int h, float[] fArray) {
        super.setPixels(x, y, w, h, fArray);
    }

    @Override
    public void setPixels(int x, int y, int w, int h, double[] dArray) {
        super.setPixels(x, y, w, h, dArray);
    }

    @Override
    public void setSamples(int x, int y, int w, int h, int b, int[] iArray) {
        super.setSamples(x, y, w, h, b, iArray);
    }

    @Override
    public void setSamples(int x, int y, int w, int h, int b, float[] fArray) {
        super.setSamples(x, y, w, h, b, fArray);
    }

    @Override
    public void setSamples(int x, int y, int w, int h, int b, double[] dArray) {
        super.setSamples(x, y, w, h, b, dArray);
    }

    @Override
    public void setSample(int x, int y, int b, int s) {
        super.setSample(x, y, b, s);
    }

    @Override
    public void setSample(int x, int y, int b, float s) {
        super.setSample(x, y, b, s);
    }

    @Override
    public void setSample(int x, int y, int b, double s) {
        super.setSample(x, y, b, s);
    }
}
