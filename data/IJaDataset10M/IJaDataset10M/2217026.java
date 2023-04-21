package edu.columbia.hypercontent.util.codec;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.io.OutputStream;
import com.sun.image.codec.jpeg.JPEGQTable;
import com.sun.image.codec.jpeg.JPEGDecodeParam;

/**
 * An ImageEncoder for the JPEG (JFIF) file format.
 *
 * The common cases of single band grayscale and three or four band RGB images
 * are handled so as to minimize the amount of information required of the
 * programmer. See the comments pertaining to the constructor and the
 * <code>writeToStream()</code> method for more detailed information.
 *
 */
public class JPEGImageEncoder extends ImageEncoderImpl {

    private JPEGEncodeParam jaiEP = null;

    public JPEGImageEncoder(OutputStream output, ImageEncodeParam param) {
        super(output, param);
        if (param != null) {
            jaiEP = (JPEGEncodeParam) param;
        }
    }

    static void modifyEncodeParam(JPEGEncodeParam jaiEP, com.sun.image.codec.jpeg.JPEGEncodeParam j2dEP, int nbands) {
        int val;
        int[] qTab;
        for (int i = 0; i < nbands; i++) {
            val = jaiEP.getHorizontalSubsampling(i);
            j2dEP.setHorizontalSubsampling(i, val);
            val = jaiEP.getVerticalSubsampling(i);
            j2dEP.setVerticalSubsampling(i, val);
            if (jaiEP.isQTableSet(i)) {
                qTab = jaiEP.getQTable(i);
                val = jaiEP.getQTableSlot(i);
                j2dEP.setQTableComponentMapping(i, val);
                j2dEP.setQTable(val, new JPEGQTable(qTab));
            }
        }
        if (jaiEP.isQualitySet()) {
            float fval = jaiEP.getQuality();
            j2dEP.setQuality(fval, true);
        }
        val = jaiEP.getRestartInterval();
        j2dEP.setRestartInterval(val);
        if (jaiEP.getWriteTablesOnly() == true) {
            j2dEP.setImageInfoValid(false);
            j2dEP.setTableInfoValid(true);
        }
        if (jaiEP.getWriteImageOnly() == true) {
            j2dEP.setTableInfoValid(false);
            j2dEP.setImageInfoValid(true);
        }
        if (jaiEP.getWriteJFIFHeader() == false) {
            j2dEP.setMarkerData(com.sun.image.codec.jpeg.JPEGDecodeParam.APP0_MARKER, null);
        }
    }

    /**
     * Encodes a RenderedImage and writes the output to the
     * OutputStream associated with this ImageEncoder.
     */
    public void encode(RenderedImage im) throws IOException {
        SampleModel sampleModel = im.getSampleModel();
        ColorModel colorModel = im.getColorModel();
        int numBands = colorModel.getNumColorComponents();
        int transType = sampleModel.getTransferType();
        if ((transType != DataBuffer.TYPE_BYTE) || ((numBands != 1) && (numBands != 3))) {
            throw new RuntimeException(JaiI18N.getString("JPEGImageEncoder0"));
        }
        int cspaceType = colorModel.getColorSpace().getType();
        if (cspaceType != ColorSpace.TYPE_GRAY && cspaceType != ColorSpace.TYPE_RGB) {
            throw new Error(JaiI18N.getString("JPEGImageEncoder1"));
        }
        WritableRaster wRas;
        BufferedImage bi;
        wRas = (WritableRaster) im.getData();
        if (wRas.getMinX() != 0 || wRas.getMinY() != 0) {
            wRas = wRas.createWritableTranslatedChild(0, 0);
        }
        com.sun.image.codec.jpeg.JPEGEncodeParam j2dEP = null;
        if (colorModel instanceof IndexColorModel) {
            IndexColorModel icm = (IndexColorModel) colorModel;
            bi = icm.convertToIntDiscrete(wRas, false);
            j2dEP = com.sun.image.codec.jpeg.JPEGCodec.getDefaultJPEGEncodeParam(bi);
        } else {
            bi = new BufferedImage(colorModel, wRas, false, null);
            j2dEP = com.sun.image.codec.jpeg.JPEGCodec.getDefaultJPEGEncodeParam(bi);
        }
        if (jaiEP != null) {
            modifyEncodeParam(jaiEP, j2dEP, numBands);
        }
        com.sun.image.codec.jpeg.JPEGImageEncoder encoder;
        encoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(output, j2dEP);
        try {
            encoder.encode(bi);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
