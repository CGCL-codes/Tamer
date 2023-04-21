package org.designerator.media.image.histogram;

import org.designerator.common.data.HistogramData;
import org.designerator.image.algo.util.ImageUtils;

public class GrayHistogram {

    private byte[] pixels;

    private static int[] greyLUT;

    public GrayHistogram(byte[] pixel) {
        this.pixels = pixel;
        greyLUT = ImageUtils.fillLUT(0.333f);
    }

    public HistogramData getHistogramData() {
        if (pixels == null) {
            return null;
        }
        HistogramData hd = new HistogramData();
        int end = pixels.length;
        for (int i = 0; i < end; i += 4) {
            int g = pixels[i] & 0xff;
            int b = pixels[i + 1] & 0xff;
            int r = pixels[i + 2] & 0xff;
            int gray = greyLUT[g] + greyLUT[b] + greyLUT[r];
            ++hd.RGB[gray];
            ++hd.R[r];
            ++hd.G[g];
            ++hd.B[b];
        }
        return hd;
    }
}
