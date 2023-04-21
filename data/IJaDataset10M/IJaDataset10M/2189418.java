package net.sourceforge.jspcemulator.client.emulator.memory;

import java.util.Arrays;
import net.sourceforge.jspcemulator.client.emulator.memory.codeblock.*;
import net.sourceforge.jspcemulator.client.emulator.processor.Processor;

/**
 * <code>Memory</code> object with simple execute capabilities.  Uses a
 * {@link net.sourceforge.jspcemulator.client.emulator.memory.codeblock.CodeBlockManager} instance to generate
 * {@link net.sourceforge.jspcemulator.client.emulator.memory.codeblock.CodeBlock} objects which are then
 * stored in mirror arrays of the memory structure.
 * @author Chris Dennis
 * @author Rhys Newman
 * @author Ian Preston
 * @author Kevin O'Dwyer (for JsPCEmulator)
 */
public class LazyCodeBlockMemory extends AbstractMemory {

    private CodeBlockManager codeBlockManager;

    private static final BlankCodeBlock PLACEHOLDER = new BlankCodeBlock();

    private RealModeCodeBlock[] realCodeBuffer;

    private ProtectedModeCodeBlock[] protectedCodeBuffer;

    private Virtual8086ModeCodeBlock[] virtual8086CodeBuffer;

    private static final int ALLOCATION_THRESHOLD = 10;

    private final int size;

    private byte[] buffer = null;

    private int nullReadCount = 0;

    /**
     * Constructs an instance <code>size</code> bytes long.
     * @param size
     */
    public LazyCodeBlockMemory(int size, CodeBlockManager manager) {
        this.size = size;
        this.codeBlockManager = manager;
    }

    /**
     * Should probably be made private.
     */
    protected void constructCodeBlocksArray() {
        realCodeBuffer = new RealModeCodeBlock[(int) getSize()];
        protectedCodeBuffer = new ProtectedModeCodeBlock[(int) getSize()];
        virtual8086CodeBuffer = new Virtual8086ModeCodeBlock[(int) getSize()];
    }

    private void constructRealCodeBlocksArray() {
        realCodeBuffer = new RealModeCodeBlock[(int) getSize()];
    }

    private void constructVirtual8086CodeBlocksArray() {
        virtual8086CodeBuffer = new Virtual8086ModeCodeBlock[(int) getSize()];
    }

    private void constructProtectedCodeBlocksArray() {
        protectedCodeBuffer = new ProtectedModeCodeBlock[(int) getSize()];
    }

    public int executeProtected(Processor cpu, int offset) {
        int x86Count = 0;
        int ip = cpu.getInstructionPointer();
        offset = ip & AddressSpace.BLOCK_MASK;
        ProtectedModeCodeBlock block = getProtectedModeCodeBlockAt(offset);
        if (block == null) {
            block = codeBlockManager.getProtectedModeCodeBlockAt(this, offset, cpu.cs.getDefaultSizeFlag());
            setProtectedCodeBlockAt(offset, block);
        }
        try {
            x86Count += block.execute(cpu);
        } catch (BlankException e) {
            block = codeBlockManager.getProtectedModeCodeBlockAt(this, offset, cpu.cs.getDefaultSizeFlag());
            setProtectedCodeBlockAt(offset, block);
            try {
                x86Count += block.execute(cpu);
            } catch (BlankException be) {
            }
        }
        return x86Count;
    }

    public int executeReal(Processor cpu, int offset) {
        int x86Count = 0;
        int ip = cpu.getInstructionPointer();
        offset = ip & AddressSpace.BLOCK_MASK;
        RealModeCodeBlock block = getRealModeCodeBlockAt(offset);
        if (block == null) {
            block = codeBlockManager.getRealModeCodeBlockAt(this, offset);
            setRealCodeBlockAt(offset, block);
        }
        try {
            x86Count += block.execute(cpu);
        } catch (BlankException e) {
            block = codeBlockManager.getRealModeCodeBlockAt(this, offset);
            setRealCodeBlockAt(offset, block);
            try {
                x86Count += block.execute(cpu);
            } catch (BlankException be) {
            }
        }
        return x86Count;
    }

    public int executeVirtual8086(Processor cpu, int offset) {
        int x86Count = 0;
        int ip = cpu.getInstructionPointer();
        offset = ip & AddressSpace.BLOCK_MASK;
        Virtual8086ModeCodeBlock block = getVirtual8086ModeCodeBlockAt(offset);
        if (block == null) {
            block = codeBlockManager.getVirtual8086ModeCodeBlockAt(this, offset);
            setVirtual8086CodeBlockAt(offset, block);
        }
        try {
            x86Count += block.execute(cpu);
        } catch (BlankException e) {
            block = codeBlockManager.getVirtual8086ModeCodeBlockAt(this, offset);
            setVirtual8086CodeBlockAt(offset, block);
            try {
                x86Count += block.execute(cpu);
            } catch (BlankException be) {
            }
        }
        return x86Count;
    }

    private RealModeCodeBlock getRealModeCodeBlockAt(int offset) {
        if (realCodeBuffer != null) {
            return realCodeBuffer[offset];
        }
        constructRealCodeBlocksArray();
        return realCodeBuffer[offset];
    }

    private ProtectedModeCodeBlock getProtectedModeCodeBlockAt(int offset) {
        if (protectedCodeBuffer != null) {
            return protectedCodeBuffer[offset];
        }
        constructProtectedCodeBlocksArray();
        return protectedCodeBuffer[offset];
    }

    private Virtual8086ModeCodeBlock getVirtual8086ModeCodeBlockAt(int offset) {
        if (virtual8086CodeBuffer != null) {
            return virtual8086CodeBuffer[offset];
        }
        constructVirtual8086CodeBlocksArray();
        return virtual8086CodeBuffer[offset];
    }

    private void removeVirtual8086CodeBlockAt(int offset) {
        Virtual8086ModeCodeBlock b = virtual8086CodeBuffer[offset];
        if ((b == null) || (b == PLACEHOLDER)) {
            return;
        }
        virtual8086CodeBuffer[offset] = null;
        int len = b.getX86Length();
        for (int i = offset + 1; (i < offset + len) && (i < virtual8086CodeBuffer.length); i++) {
            if (virtual8086CodeBuffer[i] == PLACEHOLDER) {
                virtual8086CodeBuffer[i] = null;
            }
        }
        for (int i = Math.min(offset + len, virtual8086CodeBuffer.length) - 1; i >= 0; i--) {
            if (virtual8086CodeBuffer[i] == null) {
                if (i < offset) {
                    break;
                } else {
                    continue;
                }
            }
            if (virtual8086CodeBuffer[i] == PLACEHOLDER) {
                continue;
            }
            Virtual8086ModeCodeBlock bb = virtual8086CodeBuffer[i];
            len = bb.getX86Length();
            for (int j = i + 1; (j < i + len) && (j < virtual8086CodeBuffer.length); j++) {
                if (virtual8086CodeBuffer[j] == null) {
                    virtual8086CodeBuffer[j] = PLACEHOLDER;
                }
            }
        }
    }

    private void removeProtectedCodeBlockAt(int offset) {
        ProtectedModeCodeBlock b = protectedCodeBuffer[offset];
        if ((b == null) || (b == PLACEHOLDER)) {
            return;
        }
        protectedCodeBuffer[offset] = null;
        int len = b.getX86Length();
        for (int i = offset + 1; (i < offset + len) && (i < protectedCodeBuffer.length); i++) {
            if (protectedCodeBuffer[i] == PLACEHOLDER) {
                protectedCodeBuffer[i] = null;
            }
        }
        for (int i = Math.min(offset + len, protectedCodeBuffer.length) - 1; i >= 0; i--) {
            if (protectedCodeBuffer[i] == null) {
                if (i < offset) {
                    break;
                } else {
                    continue;
                }
            }
            if (protectedCodeBuffer[i] == PLACEHOLDER) {
                continue;
            }
            ProtectedModeCodeBlock bb = protectedCodeBuffer[i];
            len = bb.getX86Length();
            for (int j = i + 1; (j < i + len) && (j < protectedCodeBuffer.length); j++) {
                if (protectedCodeBuffer[j] == null) {
                    protectedCodeBuffer[j] = PLACEHOLDER;
                }
            }
        }
    }

    private void removeRealCodeBlockAt(int offset) {
        RealModeCodeBlock b = realCodeBuffer[offset];
        if ((b == null) || (b == PLACEHOLDER)) {
            return;
        }
        realCodeBuffer[offset] = null;
        int len = b.getX86Length();
        for (int i = offset + 1; (i < offset + len) && (i < realCodeBuffer.length); i++) {
            if (realCodeBuffer[i] == PLACEHOLDER) {
                realCodeBuffer[i] = null;
            }
        }
        for (int i = Math.min(offset + len, realCodeBuffer.length) - 1; i >= 0; i--) {
            if (realCodeBuffer[i] == null) {
                if (i < offset) {
                    break;
                } else {
                    continue;
                }
            }
            if (realCodeBuffer[i] == PLACEHOLDER) {
                continue;
            }
            RealModeCodeBlock bb = realCodeBuffer[i];
            len = bb.getX86Length();
            for (int j = i + 1; (j < i + len) && (j < realCodeBuffer.length); j++) {
                if (realCodeBuffer[j] == null) {
                    realCodeBuffer[j] = PLACEHOLDER;
                }
            }
        }
    }

    private void setVirtual8086CodeBlockAt(int offset, Virtual8086ModeCodeBlock block) {
        removeVirtual8086CodeBlockAt(offset);
        if (block == null) {
            return;
        }
        virtual8086CodeBuffer[offset] = block;
        int len = block.getX86Length();
        for (int i = offset + 1; (i < offset + len) && (i < virtual8086CodeBuffer.length); i++) {
            if (virtual8086CodeBuffer[i] == null) {
                virtual8086CodeBuffer[i] = PLACEHOLDER;
            }
        }
    }

    private void setProtectedCodeBlockAt(int offset, ProtectedModeCodeBlock block) {
        removeProtectedCodeBlockAt(offset);
        if (block == null) {
            return;
        }
        protectedCodeBuffer[offset] = block;
        int len = block.getX86Length();
        for (int i = offset + 1; (i < offset + len) && (i < protectedCodeBuffer.length); i++) {
            if (protectedCodeBuffer[i] == null) {
                protectedCodeBuffer[i] = PLACEHOLDER;
            }
        }
    }

    private void setRealCodeBlockAt(int offset, RealModeCodeBlock block) {
        removeRealCodeBlockAt(offset);
        if (block == null) {
            return;
        }
        realCodeBuffer[offset] = block;
        int len = block.getX86Length();
        for (int i = offset + 1; (i < offset + len) && (i < realCodeBuffer.length); i++) {
            if (realCodeBuffer[i] == null) {
                realCodeBuffer[i] = PLACEHOLDER;
            }
        }
    }

    private void regionAltered(int start, int end) throws OutOfBoundsException {
        if (realCodeBuffer != null) {
            for (int i = end; i >= 0; i--) {
                if (i >= realCodeBuffer.length) {
                    throw new OutOfBoundsException();
                }
                RealModeCodeBlock b = realCodeBuffer[i];
                if (b == null) {
                    if (i < start) {
                        break;
                    } else {
                        continue;
                    }
                }
                if (b == PLACEHOLDER) {
                    continue;
                }
                if (!b.handleMemoryRegionChange(start, end)) {
                    removeRealCodeBlockAt(i);
                }
            }
        }
        if (protectedCodeBuffer != null) {
            for (int i = end; i >= 0; i--) {
                if (i >= protectedCodeBuffer.length) {
                    throw new OutOfBoundsException();
                }
                ProtectedModeCodeBlock b = protectedCodeBuffer[i];
                if (b == null) {
                    if (i < start) {
                        break;
                    } else {
                        continue;
                    }
                }
                if (b == PLACEHOLDER) {
                    continue;
                }
                if (!b.handleMemoryRegionChange(start, end)) {
                    removeProtectedCodeBlockAt(i);
                }
            }
        }
        if (virtual8086CodeBuffer != null) {
            for (int i = end; i >= 0; i--) {
                if (i >= virtual8086CodeBuffer.length) {
                    throw new OutOfBoundsException();
                }
                Virtual8086ModeCodeBlock b = virtual8086CodeBuffer[i];
                if (b == null) {
                    if (i < start) {
                        break;
                    } else {
                        continue;
                    }
                }
                if (b == PLACEHOLDER) {
                    continue;
                }
                if (!b.handleMemoryRegionChange(start, end)) {
                    removeVirtual8086CodeBlockAt(i);
                }
            }
        }
    }

    public void clear() {
        realCodeBuffer = null;
        protectedCodeBuffer = null;
        virtual8086CodeBuffer = null;
        buffer = null;
    }

    public String toString() {
        return "LazyCodeBlockMemory[" + getSize() + "]";
    }

    private static class BlankCodeBlock implements RealModeCodeBlock, ProtectedModeCodeBlock, Virtual8086ModeCodeBlock {

        private static final BlankException executeException = new BlankException();

        public int getX86Length() {
            return 0;
        }

        public int getX86Count() {
            return 0;
        }

        public int execute(Processor cpu) throws BlankException {
            throw executeException;
        }

        public boolean handleMemoryRegionChange(int startAddress, int endAddress) {
            return false;
        }

        public String getDisplayString() {
            return "\n\n<<Blank Block>>\n\n";
        }

        public String toString() {
            return " -- Blank --\n";
        }
    }

    public ProtectedModeCodeBlock getProtectedBlock(int offset, boolean size) {
        if (protectedCodeBuffer == null) {
            allocateBuffer();
            protectedCodeBuffer = new ProtectedModeCodeBlock[(int) getSize()];
        }
        ProtectedModeCodeBlock block = protectedCodeBuffer[offset];
        if ((block != null) && (block != PLACEHOLDER)) {
            return block;
        }
        block = codeBlockManager.getProtectedModeCodeBlockAt(this, offset, size);
        setProtectedCodeBlockAt(offset, block);
        return block;
    }

    public Virtual8086ModeCodeBlock getVirtual8086Block(int offset) {
        if (virtual8086CodeBuffer == null) {
            allocateBuffer();
            virtual8086CodeBuffer = new Virtual8086ModeCodeBlock[(int) getSize()];
        }
        Virtual8086ModeCodeBlock block = virtual8086CodeBuffer[offset];
        if ((block != null) && (block != PLACEHOLDER)) {
            return block;
        }
        block = codeBlockManager.getVirtual8086ModeCodeBlockAt(this, offset);
        setVirtual8086CodeBlockAt(offset, block);
        return block;
    }

    public RealModeCodeBlock getRealBlock(int offset) {
        if (realCodeBuffer == null) {
            allocateBuffer();
            realCodeBuffer = new RealModeCodeBlock[(int) getSize()];
        }
        RealModeCodeBlock block = realCodeBuffer[offset];
        if ((block != null) && (block != PLACEHOLDER)) {
            return block;
        }
        block = codeBlockManager.getRealModeCodeBlockAt(this, offset);
        setRealCodeBlockAt(offset, block);
        return block;
    }

    private final void allocateBuffer() {
        if (buffer == null) {
            buffer = new byte[size];
        }
    }

    public void copyContentsIntoArray(int address, byte[] buf, int off, int len) {
        if (buffer != null) {
            System.arraycopy(buffer, address, buf, off, len);
        } else {
            if (++nullReadCount == ALLOCATION_THRESHOLD) {
                allocateBuffer();
                System.arraycopy(buffer, address, buf, off, len);
            } else {
                Arrays.fill(buf, off, off + len, (byte) 0);
            }
        }
    }

    public void loadInitialContents(int address, byte[] buf, int off, int len) {
        if (buffer != null) {
            System.arraycopy(buf, off, buffer, address, len);
        } else {
            allocateBuffer();
            System.arraycopy(buf, off, buffer, address, len);
        }
    }

    public void copyArrayIntoContents(int address, byte[] buf, int off, int len) throws OutOfBoundsException {
        if (buffer != null) {
            System.arraycopy(buf, off, buffer, address, len);
        } else {
            allocateBuffer();
            System.arraycopy(buf, off, buffer, address, len);
        }
        regionAltered(address, address + len - 1);
    }

    public long getSize() {
        return size;
    }

    public boolean isAllocated() {
        return (buffer != null);
    }

    public byte getByte(int offset) throws OutOfBoundsException {
        if (buffer != null) {
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            return buffer[offset];
        }
        if (++nullReadCount == ALLOCATION_THRESHOLD) {
            allocateBuffer();
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            return buffer[offset];
        } else {
            return 0;
        }
    }

    public void setByte(int offset, byte data) throws OutOfBoundsException {
        if (getByte(offset) == data) {
            return;
        }
        if (buffer != null) {
            buffer[offset] = data;
        } else {
            allocateBuffer();
            buffer[offset] = data;
        }
        regionAltered(offset, offset);
    }

    public short getWord(int offset) throws OutOfBoundsException {
        if (buffer != null) {
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            int result = 0xFF & buffer[offset];
            offset++;
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            result |= buffer[offset] << 8;
            return (short) result;
        }
        if (++nullReadCount == ALLOCATION_THRESHOLD) {
            allocateBuffer();
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            int result = 0xFF & buffer[offset];
            offset++;
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            result |= buffer[offset] << 8;
            return (short) result;
        } else {
            return 0;
        }
    }

    public int getDoubleWord(int offset) throws OutOfBoundsException {
        if (buffer != null) {
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            int result = 0xFF & buffer[offset];
            offset++;
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            result |= (0xFF & buffer[offset]) << 8;
            offset++;
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            result |= (0xFF & buffer[offset]) << 16;
            offset++;
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            result |= (buffer[offset]) << 24;
            return result;
        }
        if (++nullReadCount == ALLOCATION_THRESHOLD) {
            allocateBuffer();
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            int result = 0xFF & buffer[offset];
            offset++;
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            result |= (0xFF & buffer[offset]) << 8;
            offset++;
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            result |= (0xFF & buffer[offset]) << 16;
            offset++;
            if (offset >= buffer.length) {
                throw new OutOfBoundsException();
            }
            result |= (buffer[offset]) << 24;
            return result;
        } else {
            return 0;
        }
    }

    public void setWord(int offset, short data) throws OutOfBoundsException {
        if (getWord(offset) == data) {
            return;
        }
        if (buffer != null) {
            buffer[offset] = (byte) data;
            offset++;
            buffer[offset] = (byte) (data >> 8);
        } else {
            allocateBuffer();
            buffer[offset] = (byte) data;
            offset++;
            buffer[offset] = (byte) (data >> 8);
        }
        regionAltered(offset, offset + 1);
    }

    public void setDoubleWord(int offset, int data) throws OutOfBoundsException {
        if (getDoubleWord(offset) == data) {
            return;
        }
        if (buffer != null) {
            buffer[offset] = (byte) data;
            offset++;
            data >>= 8;
            buffer[offset] = (byte) (data);
            offset++;
            data >>= 8;
            buffer[offset] = (byte) (data);
            offset++;
            data >>= 8;
            buffer[offset] = (byte) (data);
        } else {
            allocateBuffer();
            buffer[offset] = (byte) data;
            offset++;
            data >>= 8;
            buffer[offset] = (byte) (data);
            offset++;
            data >>= 8;
            buffer[offset] = (byte) (data);
            offset++;
            data >>= 8;
            buffer[offset] = (byte) (data);
        }
        regionAltered(offset, offset + 3);
    }
}
