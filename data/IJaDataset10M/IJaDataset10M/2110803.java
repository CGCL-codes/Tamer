package org.isakiev.wic.demo.statistics;

public class CompressionResult {

    private int size;

    private double psnr;

    private long time;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getPsnr() {
        return psnr;
    }

    public void setPsnr(double psnr) {
        this.psnr = psnr;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Size = " + size + "; PSNR = " + psnr + "; time = " + time / 1000000 + "ms";
    }
}
