package net.sf.topspeed.down;

public class DownloadBlock {

    private Download download;

    private long start;

    private long length;

    public DownloadBlock() {
    }

    public DownloadBlock(Download download, long start, long length) {
        this.download = download;
        this.start = start;
        this.length = length;
    }

    public Download getDownload() {
        return download;
    }

    public void setDownload(Download download) {
        this.download = download;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return download + "[" + start + ", " + (start + length) + "]";
    }
}
