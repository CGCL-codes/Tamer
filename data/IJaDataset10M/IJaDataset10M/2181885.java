package com.jgraph.gaeawt.java.awt.image;

import java.util.Arrays;
import org.apache.harmony.awt.internal.nls.Messages;

public class SinglePixelPackedSampleModel extends SampleModel {

    private int bitMasks[];

    private int bitOffsets[];

    private int bitSizes[];

    private int scanlineStride;

    private int maxBitSize;

    public SinglePixelPackedSampleModel(int w, int h, int bitMasks[]) {
        this(w, h, w, bitMasks);
    }

    public SinglePixelPackedSampleModel(int w, int h, int scanlineStride, int bitMasks[]) {
        super(w, h, bitMasks.length);
        this.scanlineStride = scanlineStride;
        this.bitMasks = bitMasks.clone();
        this.bitOffsets = new int[this.numBands];
        this.bitSizes = new int[this.numBands];
        this.maxBitSize = 0;
        for (int i = 0; i < this.numBands; i++) {
            int offset = 0;
            int size = 0;
            int mask = bitMasks[i];
            if (mask != 0) {
                while ((mask & 1) == 0) {
                    mask >>>= 1;
                    offset++;
                }
                while ((mask & 1) == 1) {
                    mask >>>= 1;
                    size++;
                }
                if (mask != 0) {
                    throw new IllegalArgumentException(Messages.getString("awt.62", bitMasks[i]));
                }
            }
            this.bitOffsets[i] = offset;
            this.bitSizes[i] = size;
            if (this.maxBitSize < size) {
                this.maxBitSize = size;
            }
        }
    }

    @Override
    public int getDataElements(int x, int y, int[] data) {
        return data[y * scanlineStride + x];
    }

    @Override
    public void setDataElements(int x, int y, int value, int[] data) {
        data[y * scanlineStride + x] = value;
    }

    @Override
    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof SinglePixelPackedSampleModel)) {
            return false;
        }
        SinglePixelPackedSampleModel model = (SinglePixelPackedSampleModel) o;
        return this.width == model.width && this.height == model.height && this.numBands == model.numBands && Arrays.equals(this.bitMasks, model.bitMasks) && Arrays.equals(this.bitOffsets, model.bitOffsets) && Arrays.equals(this.bitSizes, model.bitSizes) && this.scanlineStride == model.scanlineStride;
    }

    @Override
    public SampleModel createSubsetSampleModel(int bands[]) {
        if (bands.length > this.numBands) {
            throw new RasterFormatException(Messages.getString("awt.64"));
        }
        int masks[] = new int[bands.length];
        for (int i = 0; i < bands.length; i++) {
            masks[i] = this.bitMasks[bands[i]];
        }
        return new SinglePixelPackedSampleModel(this.width, this.height, this.scanlineStride, masks);
    }

    @Override
    public SampleModel createCompatibleSampleModel(int w, int h) {
        return new SinglePixelPackedSampleModel(w, h, this.bitMasks);
    }

    @Override
    public int[] getPixel(int x, int y, int iArray[], int[] data) {
        if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
            throw new ArrayIndexOutOfBoundsException(Messages.getString("awt.63"));
        }
        int pixel[];
        if (iArray == null) {
            pixel = new int[this.numBands];
        } else {
            pixel = iArray;
        }
        for (int i = 0; i < this.numBands; i++) {
            pixel[i] = getSample(x, y, i, data);
        }
        return pixel;
    }

    @Override
    public void setPixel(int x, int y, int iArray[], int[] data) {
        if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
            throw new ArrayIndexOutOfBoundsException(Messages.getString("awt.63"));
        }
        for (int i = 0; i < this.numBands; i++) {
            setSample(x, y, i, iArray[i], data);
        }
    }

    @Override
    public int getSample(int x, int y, int b, int[] data) {
        if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
            throw new ArrayIndexOutOfBoundsException(Messages.getString("awt.63"));
        }
        int sample = data[y * scanlineStride + x];
        return ((sample & this.bitMasks[b]) >>> this.bitOffsets[b]);
    }

    @Override
    public int[] getPixels(int x, int y, int w, int h, int iArray[], int[] data) {
        if ((x < 0) || (y < 0) || ((long) x + (long) w > this.width) || ((long) y + (long) h > this.height)) {
            throw new ArrayIndexOutOfBoundsException(Messages.getString("awt.63"));
        }
        int pixels[];
        if (iArray == null) {
            pixels = new int[w * h * this.numBands];
        } else {
            pixels = iArray;
        }
        int idx = 0;
        for (int i = y; i < y + h; i++) {
            for (int j = x; j < x + w; j++) {
                for (int n = 0; n < this.numBands; n++) {
                    pixels[idx++] = getSample(j, i, n, data);
                }
            }
        }
        return pixels;
    }

    @Override
    public void setPixels(int x, int y, int w, int h, int iArray[], int[] data) {
        if ((x < 0) || (y < 0) || ((long) x + (long) w > this.width) || ((long) y + (long) h > this.height)) {
            throw new ArrayIndexOutOfBoundsException(Messages.getString("awt.63"));
        }
        int idx = 0;
        for (int i = y; i < y + h; i++) {
            for (int j = x; j < x + w; j++) {
                for (int n = 0; n < this.numBands; n++) {
                    setSample(j, i, n, iArray[idx++], data);
                }
            }
        }
    }

    @Override
    public void setSample(int x, int y, int b, int s, int[] data) {
        if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
            throw new ArrayIndexOutOfBoundsException(Messages.getString("awt.63"));
        }
        int tmp = data[y * scanlineStride + x];
        tmp &= ~this.bitMasks[b];
        tmp |= (s << this.bitOffsets[b]) & this.bitMasks[b];
        data[y * scanlineStride + x] = tmp;
    }

    @Override
    public int[] getSamples(int x, int y, int w, int h, int b, int iArray[], int[] data) {
        if ((x < 0) || (y < 0) || ((long) x + (long) w > this.width) || ((long) y + (long) h > this.height)) {
            throw new ArrayIndexOutOfBoundsException(Messages.getString("awt.63"));
        }
        int samples[];
        int idx = 0;
        if (iArray == null) {
            samples = new int[w * h];
        } else {
            samples = iArray;
        }
        for (int i = y; i < y + h; i++) {
            for (int j = x; j < x + w; j++) {
                samples[idx++] = getSample(j, i, b, data);
            }
        }
        return samples;
    }

    @Override
    public void setSamples(int x, int y, int w, int h, int b, int iArray[], int[] data) {
        if ((x < 0) || (y < 0) || ((long) x + (long) w > this.width) || ((long) y + (long) h > this.height)) {
            throw new ArrayIndexOutOfBoundsException(Messages.getString("awt.63"));
        }
        int idx = 0;
        for (int i = y; i < y + h; i++) {
            for (int j = x; j < x + w; j++) {
                setSample(x + j, y + i, b, iArray[idx++], data);
            }
        }
    }

    @Override
    public int[] createDataBuffer() {
        int size = (this.height - 1) * scanlineStride + width;
        return new int[size];
    }

    public int getOffset(int x, int y) {
        return (y * scanlineStride + x);
    }

    @Override
    public int getSampleSize(int band) {
        return bitSizes[band];
    }

    @Override
    public int[] getSampleSize() {
        return bitSizes.clone();
    }

    public int[] getBitOffsets() {
        return bitOffsets.clone();
    }

    public int[] getBitMasks() {
        return bitMasks.clone();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        int tmp = 0;
        hash = width;
        tmp = hash >>> 24;
        hash <<= 8;
        hash |= tmp;
        hash ^= height;
        tmp = hash >>> 24;
        hash <<= 8;
        hash |= tmp;
        hash ^= numBands;
        tmp = hash >>> 24;
        hash <<= 8;
        hash |= tmp;
        for (int element : bitMasks) {
            hash ^= element;
            tmp = hash >>> 24;
            hash <<= 8;
            hash |= tmp;
        }
        for (int element : bitOffsets) {
            hash ^= element;
            tmp = hash >>> 24;
            hash <<= 8;
            hash |= tmp;
        }
        for (int element : bitSizes) {
            hash ^= element;
            tmp = hash >>> 24;
            hash <<= 8;
            hash |= tmp;
        }
        hash ^= scanlineStride;
        return hash;
    }

    public int getScanlineStride() {
        return this.scanlineStride;
    }
}
