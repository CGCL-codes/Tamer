package org.apache.harmony.luni.platform;

public class MappedPlatformAddress extends PlatformAddress {

    MappedPlatformAddress(long address, long size) {
        super(address, size);
    }

    public final void mmapLoad() {
        memorySpy.rangeCheck(this, 0, (int) size * SIZEOF_JBYTE);
        osMemory.load(osaddr, size);
    }

    public final boolean mmapIsLoaded() {
        memorySpy.rangeCheck(this, 0, (int) size * SIZEOF_JBYTE);
        return osMemory.isLoaded(osaddr, size);
    }

    public final void mmapFlush() {
        memorySpy.rangeCheck(this, 0, (int) size * SIZEOF_JBYTE);
        osMemory.flush(osaddr, size);
    }

    public final void free() {
        if (memorySpy.free(this)) {
            osMemory.unmap(osaddr, size);
        }
    }

    public PlatformAddress duplicate() {
        return PlatformAddressFactory.mapOn(osaddr, size);
    }

    public final PlatformAddress offsetBytes(int offset) {
        return PlatformAddressFactory.mapOn(osaddr + offset, size - offset);
    }

    public final PlatformAddress offsetBytes(long offset) {
        return PlatformAddressFactory.mapOn(osaddr + offset, size - offset);
    }
}
