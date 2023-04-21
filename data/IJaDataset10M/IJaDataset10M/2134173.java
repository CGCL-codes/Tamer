package com.jachsoft.imagelib.algorithms;

import com.jachsoft.imagelib.ConvolutionKernel;
import com.jachsoft.imagelib.DataArray;
import com.jachsoft.imagelib.RGBImage;

public class LaplacianEdgeDetect extends ImageOperator {

    int threshold = 3;

    public LaplacianEdgeDetect() {
    }

    public void setParameters(int threshold) {
        this.threshold = threshold;
    }

    public LaplacianEdgeDetect(RGBImage image) {
        super(image);
    }

    public RGBImage apply() {
        int w = source.getWidth();
        int h = source.getHeight();
        RGBImage retval = new RGBImage(w, h);
        RGBImage gray = source.getGrayScaleImage();
        ConvolutionKernel kernel = new ConvolutionKernel().laplacianMask();
        int offset = kernel.getOffset();
        DataArray raw = gray.getDataArray(1);
        Convolution convolution = new Convolution(gray);
        convolution.setParameters(kernel);
        raw = convolution.convolve();
        for (int y = offset; y < (h - offset); y++) {
            for (int x = offset; x < (w - offset); x++) {
                int min = 9999999;
                int max = -min;
                int newval = 0;
                for (int i = (y - offset); i <= (y + offset); i++) {
                    for (int j = (x - offset); j <= (x + offset); j++) {
                        newval = (int) raw.getValue(j, i);
                        if (newval < min) min = newval;
                        if (newval > max) max = newval;
                    }
                }
                if (min < -threshold && max > threshold) {
                    retval.setRGB(x, y, 255, 255, 255);
                } else {
                    retval.setRGB(x, y, 0, 0, 0);
                }
            }
        }
        return retval;
    }
}
