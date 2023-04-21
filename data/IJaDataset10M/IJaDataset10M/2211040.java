package net.sourceforge.jspcemulator.client.emulator.processor;

import net.sourceforge.jspcemulator.client.emulator.memory.AddressSpace;

/**
 *
 * @author Ian Preston
 */
public abstract class ProtectedModeExpandDownSegment extends Segment {

    public static final int TYPE_ACCESSED = 0x1;

    public static final int TYPE_CODE = 0x8;

    public static final int TYPE_DATA_WRITABLE = 0x2;

    public static final int TYPE_DATA_EXPAND_DOWN = 0x4;

    public static final int TYPE_CODE_READABLE = 0x2;

    public static final int TYPE_CODE_CONFORMING = 0x4;

    public static final int DESCRIPTOR_TYPE_CODE_DATA = 0x10;

    private final boolean defaultSize;

    private final boolean granularity;

    private final boolean present, system;

    private final int selector;

    private final int base;

    private final int minOffset, maxOffset;

    private final int dpl;

    private final long limit;

    private final int rawLimit;

    private final long descriptor;

    private int rpl;

    public ProtectedModeExpandDownSegment(AddressSpace memory, int selector, long descriptor) {
        super(memory);
        this.selector = selector;
        this.descriptor = descriptor;
        granularity = (descriptor & 0x80000000000000L) != 0;
        defaultSize = (descriptor & (1L << 54)) != 0;
        base = (int) ((0xffffffL & (descriptor >> 16)) | ((descriptor >> 32) & 0xffffffffff000000L));
        if (granularity) limit = ((descriptor << 12) & 0xffff000L) | ((descriptor >>> 20) & 0xf0000000L) | 0xfffL; else limit = (descriptor & 0xffffL) | ((descriptor >>> 32) & 0xf0000L);
        rawLimit = (int) ((descriptor & 0xffffL) | ((descriptor >>> 32) & 0xf0000L));
        if (defaultSize) {
            minOffset = (int) (base + limit - 1);
            maxOffset = 0xFFFFFFFF;
        } else {
            minOffset = (int) (base + limit - 1);
            maxOffset = 0xFFFF;
        }
        rpl = selector & 0x3;
        dpl = (int) ((descriptor >> 45) & 0x3);
        present = (descriptor & (1L << 47)) != 0;
        system = (descriptor & (1L << 44)) != 0;
    }

    public boolean isPresent() {
        return present;
    }

    public boolean isSystem() {
        return !system;
    }

    public int translateAddressRead(int offset) {
        checkAddress(offset);
        return base + offset;
    }

    public int translateAddressWrite(int offset) {
        checkAddress(offset);
        return base + offset;
    }

    public void checkAddress(int offset) {
        if (((offset < 0) && (maxOffset < 0)) | ((offset > 0) && (maxOffset > 0))) {
            if (offset >= maxOffset) {
                throw new ProcessorException(ProcessorException.Type.GENERAL_PROTECTION, 0, true);
            }
        } else if (offset > 0) {
            return;
        } else {
            throw new ProcessorException(ProcessorException.Type.GENERAL_PROTECTION, 0, true);
        }
    }

    public boolean getDefaultSizeFlag() {
        return defaultSize;
    }

    public int getLimit() {
        return (int) limit;
    }

    public int getRawLimit() {
        return rawLimit;
    }

    public int getBase() {
        return base;
    }

    public int getSelector() {
        return (selector & 0xFFFC) | rpl;
    }

    public int getRPL() {
        return rpl;
    }

    public int getDPL() {
        return dpl;
    }

    public void setRPL(int cpl) {
        rpl = cpl;
    }

    public boolean setSelector(int selector) {
        throw new IllegalStateException("Cannot set a selector for a descriptor table segment");
    }

    public void printState() {
        System.out.println("PM Expand down segment.");
        System.out.println("selector: " + Integer.toHexString(selector));
        System.out.println("base: " + Integer.toHexString(base));
        System.out.println("dpl: " + Integer.toHexString(dpl));
        System.out.println("rpl: " + Integer.toHexString(rpl));
        System.out.println("limit: " + Long.toHexString(limit));
        System.out.println("descriptor: " + Long.toHexString(descriptor));
    }

    static final class ReadWriteDataSegment extends ProtectedModeExpandDownSegment {

        public ReadWriteDataSegment(AddressSpace memory, int selector, long descriptor) {
            super(memory, selector, descriptor);
        }

        public int getType() {
            return DESCRIPTOR_TYPE_CODE_DATA | TYPE_DATA_WRITABLE;
        }
    }
}
