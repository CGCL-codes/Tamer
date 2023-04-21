package dioscuri.module.cpu32;

import dioscuri.module.clock.Clock;

/**
 * @author Bram Lohman
 * @author Bart Kiers
 */
public class EPROMMemory extends LazyCodeBlockMemory {

    @SuppressWarnings("unused")
    private Clock clock;

    /**
     * @param data
     * @param clk
     */
    public EPROMMemory(byte[] data, Clock clk) {
        this(data, 0, data.length, clk);
    }

    /**
     * @param data
     * @param offset
     * @param length
     * @param clk
     */
    public EPROMMemory(byte[] data, int offset, int length, Clock clk) {
        this(length, 0, data, offset, length, clk);
    }

    /**
     * @param size
     * @param base
     * @param data
     * @param offset
     * @param length
     * @param clk
     */
    public EPROMMemory(int size, int base, byte[] data, int offset, int length, Clock clk) {
        super(size, clk);
        super.copyContentsFrom(base, data, offset, Math.min(size - base, Math.min(length, data.length - offset)));
        this.clock = clk;
    }

    /**
     * @param offset
     * @param data
     */
    @Override
    public void setByte(int offset, byte data) {
        System.err.println("Tried to write to EPROM");
    }

    /**
     * @param offset
     * @param data
     */
    @Override
    public void setWord(int offset, short data) {
        System.err.println("Tried to write to EPROM");
    }

    /**
     * @param offset
     * @param data
     */
    @Override
    public void setDoubleWord(int offset, int data) {
        System.err.println("Tried to write to EPROM");
    }

    /**
     * @param address
     * @param buf
     * @param off
     * @param len
     */
    @Override
    public void copyContentsFrom(int address, byte[] buf, int off, int len) {
    }

    @Override
    public void clear() {
        constructCodeBlocksArray();
    }

    /**
     * @return -
     */
    public boolean isVolatile() {
        return false;
    }
}
