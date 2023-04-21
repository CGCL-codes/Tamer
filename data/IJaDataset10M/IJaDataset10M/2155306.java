package org.argeproje.resim.proc.data;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;

public class ImageDA extends Data {

    protected boolean _writable = false;

    public ImageDA(PlanarImage img) {
        setDataType(Data.IMAGE);
        setData(img);
    }

    public ImageDA(ImageDA imgDA) {
        setDataType(Data.IMAGE);
        TiledImage im = new TiledImage(imgDA.getImage(), true);
        setData(im);
    }

    public PlanarImage getImage() {
        return (PlanarImage) getData();
    }

    public int getNBands() {
        return ((PlanarImage) getData()).getNumBands();
    }

    public int getNBins(int band) {
        int nBits = ((PlanarImage) getData()).getSampleModel().getSampleSize(band);
        int nBins = (int) Math.pow(2.0, (double) nBits);
        return nBins;
    }

    public void printProperties() {
        PlanarImage image = getImage();
        System.out.println("-----Image Properties-----");
        System.out.println("Width : " + image.getWidth());
        System.out.println("Height : " + image.getHeight());
        ColorModel colorModel = image.getColorModel();
        System.out.println("Number of color components : " + colorModel.getNumColorComponents());
        System.out.println("Has alpha ? : " + colorModel.hasAlpha());
        System.out.println("Number of components(#color components + 1 if any alpha) : " + colorModel.getNumComponents());
        System.out.println("Number of bits per pixel : " + colorModel.getPixelSize());
        ColorSpace colorSpace = colorModel.getColorSpace();
        int numComponents = colorSpace.getNumComponents();
        for (int i = 0; i < numComponents; i++) {
            System.out.println(i + ". color component name : " + colorSpace.getName(i));
        }
        SampleModel sampleModel = image.getSampleModel();
        String dataType = "";
        switch(sampleModel.getDataType()) {
            case DataBuffer.TYPE_BYTE:
                dataType = "BYTE";
                break;
            case DataBuffer.TYPE_DOUBLE:
                dataType = "DOUBLE";
                break;
            case DataBuffer.TYPE_FLOAT:
                dataType = "FLOAT";
                break;
            case DataBuffer.TYPE_INT:
                dataType = "INT";
                break;
            case DataBuffer.TYPE_SHORT:
                dataType = "SHORT";
                break;
            case DataBuffer.TYPE_USHORT:
                dataType = "USHORT";
                break;
            case DataBuffer.TYPE_UNDEFINED:
                dataType = "UNDEFINED";
                break;
            default:
                dataType = "UNKNOWN";
                break;
        }
        System.out.println("Data Type : " + dataType);
        int nBands = sampleModel.getNumBands();
        for (int i = 0; i < nBands; i++) {
            System.out.println("Number of bits of " + i + ". Band: " + sampleModel.getSampleSize(i));
        }
        System.out.println("Number of Bands : " + image.getNumBands());
        System.out.println("-----JAI Properties-----");
        String[] propNames = image.getPropertyNames();
        for (int i = 0; i < propNames.length; i++) {
            System.out.print(propNames[i] + " : ");
            System.out.println(image.getProperty(propNames[i]).toString());
        }
        System.out.println("End of Properties\n\n");
    }

    public int getWidth() {
        return getImage().getWidth();
    }

    public int getHeight() {
        return getImage().getHeight();
    }

    public void setWritable() {
        if (!_writable) {
            ParameterBlock pbConvert = new ParameterBlock();
            pbConvert.addSource(getImage());
            pbConvert.add(DataBuffer.TYPE_FLOAT);
            PlanarImage i = (PlanarImage) JAI.create("format", pbConvert);
            TiledImage im = new TiledImage(i, true);
            setData(im);
            _writable = true;
        }
    }

    public double[] getPixelsDouble() {
        int len = getNBands() * getWidth() * getHeight();
        double[] pixels = new double[len];
        Raster inputRaster = getImage().getData();
        inputRaster.getPixels(0, 0, getWidth(), getHeight(), pixels);
        return pixels;
    }

    public void setPixelsDouble(double[] pixels) {
        WritableRaster raster = getImage().getData().createCompatibleWritableRaster();
        raster.setPixels(0, 0, getWidth(), getHeight(), pixels);
        TiledImage ti = new TiledImage(getImage(), 1, 1);
        ti.setData(raster);
        setData(ti);
    }

    public float[][] getSegmentByFirstPixelPos(int band, int width, int height, int posX, int posY) {
        this.setWritable();
        TiledImage im = (TiledImage) this.getData();
        float[][] segmentData = new float[height][width];
        int x = posX;
        int y = posY;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                segmentData[i][j] = im.getSampleFloat(x, y, band);
                x++;
            }
            x = posX;
            y++;
        }
        return segmentData;
    }

    public void setSegmentByFirstPixelPos(int band, int posX, int posY, float[][] segmentData) {
        this.setWritable();
        TiledImage im = (TiledImage) this.getData();
        int x = posX;
        int y = posY;
        int height = segmentData.length;
        int width = segmentData[0].length;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                im.setSample(x, y, band, segmentData[i][j]);
                x++;
            }
            x = posX;
            y++;
        }
    }

    public float[][] getSegmentBySegmentPos(int band, int width, int height, int segmentX, int segmentY) {
        float[][] segmentData = new float[height][width];
        int x = segmentX * width;
        int y = segmentY * height;
        segmentData = getSegmentByFirstPixelPos(band, width, height, x, y);
        return segmentData;
    }
}
