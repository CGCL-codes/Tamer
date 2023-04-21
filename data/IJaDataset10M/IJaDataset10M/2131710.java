package jpcsp.HLE.kernel.types;

public class SceMpegAu extends pspAbstractMemoryMappedStructure {

    public long pts;

    public long dts;

    public int esBuffer;

    public int esSize;

    public SceMpegAu() {
        pts = -1;
        dts = -1;
        esBuffer = 0;
        esSize = 0;
    }

    protected long readTimeStamp() {
        int msb = read32();
        int lsb = read32();
        return (((long) msb) << 32) | (((long) lsb) & 0xFFFFFFFFL);
    }

    protected void writeTimeStamp(long ts) {
        int msb = (int) (ts >> 32);
        int lsb = (int) ts;
        write32(msb);
        write32(lsb);
    }

    @Override
    protected void read() {
        pts = readTimeStamp();
        dts = readTimeStamp();
        esBuffer = read32();
        esSize = read32();
    }

    @Override
    protected void write() {
        writeTimeStamp(pts);
        writeTimeStamp(dts);
        write32(esBuffer);
        write32(esSize);
    }

    @Override
    public int sizeof() {
        return 24;
    }

    @Override
    public String toString() {
        return String.format("pts=%d, dts=%d", pts, dts);
    }
}
