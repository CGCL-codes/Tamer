package emulator.hardware.memory;

import emulator.hardware.HwByte;
import emulator.hardware.HwWord;

public interface UnmappedMemoryHandler {

    HwByte read(HwWord address) throws UnmappedMemoryException;
}
