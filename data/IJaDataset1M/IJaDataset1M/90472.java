package com.jachsoft.imagelib;

public class ConvolutionKernel extends DataArray {

    int size;

    public ConvolutionKernel() {
    }

    public ConvolutionKernel(int size) {
        super(size, size);
        this.size = size;
    }

    public ConvolutionKernel(int size, double data[]) {
        super(size, size);
        this.size = size;
        int count = size * size;
        for (int i = 0; i < count; i++) {
            this.setValue(i, data[i]);
        }
    }

    public int getSize() {
        return size;
    }

    public ConvolutionKernel meanFilter() {
        ConvolutionKernel kernel = new ConvolutionKernel(size);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                kernel.setValue(x, y, (float) 1 / (size * size));
            }
        }
        return kernel;
    }

    public ConvolutionKernel gaussianFilter(float sd) {
        ConvolutionKernel kernel = new ConvolutionKernel(size);
        double value;
        double min = 0;
        double sum = 0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int center = size / 2;
                int i = x - center;
                int j = y - center;
                value = (Math.exp(-(((i * i) + (j * j)) / (2 * sd * sd)))) / (2 * Math.PI * (sd * sd));
                if (x == 0 && y == 0) {
                    min = value;
                }
                value = value / min;
                sum = sum + value;
                kernel.setValue(x, y, (float) value);
            }
        }
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                kernel.setValue(x, y, (float) (kernel.getValue(x, y) / sum));
            }
        }
        return kernel;
    }

    public ConvolutionKernel LoGFilter(float sd) {
        ConvolutionKernel kernel = new ConvolutionKernel(size);
        double value;
        double min = 0;
        double sum = 0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int center = size / 2;
                int i = x - center;
                int j = y - center;
                float tmp1 = ((i * i) + (j * j)) / (2 * sd * sd);
                value = (Math.exp(-1 * (tmp1)) / (2 * Math.PI * sd * sd * sd * sd));
                value = value * (1 - tmp1);
                if (x == 0 && y == 0) {
                    min = value;
                }
                value = value / min;
                sum = sum + value;
                kernel.setValue(x, y, (float) value);
            }
        }
        return kernel;
    }

    public ConvolutionKernel laplacianMask() {
        return new ConvolutionKernel(3, new double[] { 0, 1, 0, 1, -4, 1, 0, 1, 0 });
    }

    public int getOffset() {
        return size / 2;
    }
}
