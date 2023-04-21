package com.bluebrim.image.impl.shared;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.Serializable;
import java.util.Hashtable;

/**
 * Serializable class for sending image data to clients.
 * This is a temporary solution.
 *
 * @author Markus Persson 1999-10-23
 */
public class CoImageTransporter implements Serializable, CoImageConstants {

    private static final Point ORIGO = new Point(0, 0);

    private static final int DATA_TYPE = DataBuffer.TYPE_INT;

    private static final int RED_MASK = 0x00ff0000;

    private static final int GREEN_MASK = 0x0000ff00;

    private static final int BLUE_MASK = 0x000000ff;

    private static final int[] BIT_MASKS = new int[] { RED_MASK, GREEN_MASK, BLUE_MASK };

    private static final ColorModel COLOR_MODEL = new DirectColorModel(24, RED_MASK, GREEN_MASK, BLUE_MASK);

    private int m_width;

    private int m_height;

    private int m_dataBufferSize;

    private int[][] m_bankData;

    public CoImageTransporter(BufferedImage image) {
        if (image == null) throw new IllegalArgumentException("Cannot transport null image. (Silly, I know.)");
        if (image.getType() != IMAGE_TYPE) throw new IllegalArgumentException("Cannot transport image other than TYPE_INT_RGB.");
        WritableRaster raster = image.getRaster();
        if (!COLOR_MODEL.isCompatibleRaster(raster)) throw new IllegalArgumentException("Cannot transport image with incompatible raster.");
        try {
            DataBufferInt dataBuffer = (DataBufferInt) raster.getDataBuffer();
            m_bankData = dataBuffer.getBankData();
            m_dataBufferSize = dataBuffer.getSize();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot transport image with wrong data buffer type.");
        }
        try {
            SinglePixelPackedSampleModel sampleModel = (SinglePixelPackedSampleModel) raster.getSampleModel();
            m_width = sampleModel.getWidth();
            m_height = sampleModel.getHeight();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Cannot transport image with wrong sample model type.");
        }
    }

    public BufferedImage createBufferedImage() {
        SampleModel sampleModel = new SinglePixelPackedSampleModel(DATA_TYPE, m_width, m_height, BIT_MASKS);
        DataBuffer dataBuffer = new DataBufferInt(m_bankData, m_dataBufferSize);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, ORIGO);
        BufferedImage bufferedImage = new BufferedImage(COLOR_MODEL, raster, false, new Hashtable());
        return bufferedImage;
    }
}
