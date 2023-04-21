package net.sourceforge.jspcemulator.client.emulator.memory;

import net.sourceforge.jspcemulator.client.emulator.processor.Processor;

/**
 * Represents a complete 32-bit address-space composed of a sequence of blocks
 * <code>BLOCK_SIZE</code> long.
 * @author Chris Dennis
 * @author Kevin O'Dwyer (for JsPCEmulator)
 */
public abstract class AddressSpace extends AbstractMemory {

    public static final int BLOCK_SIZE = 4 * 1024;

    public static final int BLOCK_MASK = BLOCK_SIZE - 1;

    public static final int INDEX_MASK = ~(BLOCK_MASK);

    public static final int INDEX_SHIFT = 12;

    public static final int INDEX_SIZE = 1 << (32 - INDEX_SHIFT);

    /**
     * Returns the size of the <code>AddressSpace</code> which is always 2<sup>32</sup>.
     * @return 2<sup>32</sup>
     */
    public final long getSize() {
        return 0x100000000l;
    }

    public boolean isAllocated() {
        return true;
    }

    /**
     * Get a <code>Memory</code> instance suitable for reading from this address.
     * @param offset address to be written to
     * @return block covering this address
     */
    protected abstract Memory getReadMemoryBlockAt(int offset);

    /**
     * Get a <code>Memory</code> instance suitable for writing to this address.
     * @param offset address to be written to
     * @return block covering this address
     */
    protected abstract Memory getWriteMemoryBlockAt(int offset) throws OutOfBoundsException;

    public abstract void clear();

    public byte getByte(int offset) throws OutOfBoundsException {
        return getReadMemoryBlockAt(offset).getByte(offset & BLOCK_MASK);
    }

    public void setByte(int offset, byte data) throws OutOfBoundsException {
        getWriteMemoryBlockAt(offset).setByte(offset & BLOCK_MASK, data);
    }

    public short getWord(int offset) {
        try {
            return getReadMemoryBlockAt(offset).getWord(offset & BLOCK_MASK);
        } catch (OutOfBoundsException e) {
            try {
                return super.getWord(offset);
            } catch (OutOfBoundsException e1) {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                return super.getWord(offset);
            } catch (OutOfBoundsException e1) {
                return 0;
            }
        }
    }

    public int getDoubleWord(int offset) {
        try {
            return getReadMemoryBlockAt(offset).getDoubleWord(offset & BLOCK_MASK);
        } catch (OutOfBoundsException e) {
            try {
                return super.getDoubleWord(offset);
            } catch (OutOfBoundsException e1) {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                return super.getDoubleWord(offset);
            } catch (OutOfBoundsException e1) {
                return 0;
            }
        }
    }

    public long getQuadWord(int offset) {
        try {
            return getReadMemoryBlockAt(offset).getQuadWord(offset & BLOCK_MASK);
        } catch (OutOfBoundsException e) {
            try {
                return super.getQuadWord(offset);
            } catch (OutOfBoundsException e1) {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                return super.getQuadWord(offset);
            } catch (OutOfBoundsException e1) {
                return 0;
            }
        }
    }

    public long getLowerDoubleQuadWord(int offset) {
        try {
            return getReadMemoryBlockAt(offset).getLowerDoubleQuadWord(offset & BLOCK_MASK);
        } catch (OutOfBoundsException e) {
            try {
                return super.getLowerDoubleQuadWord(offset);
            } catch (OutOfBoundsException e1) {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                return super.getLowerDoubleQuadWord(offset);
            } catch (OutOfBoundsException e1) {
                return 0;
            }
        }
    }

    public long getUpperDoubleQuadWord(int offset) {
        try {
            return getReadMemoryBlockAt(offset).getUpperDoubleQuadWord(offset & BLOCK_MASK);
        } catch (OutOfBoundsException e) {
            try {
                return super.getUpperDoubleQuadWord(offset);
            } catch (OutOfBoundsException e1) {
                return 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                return super.getUpperDoubleQuadWord(offset);
            } catch (OutOfBoundsException e1) {
                return 0;
            }
        }
    }

    public void setWord(int offset, short data) {
        try {
            getWriteMemoryBlockAt(offset).setWord(offset & BLOCK_MASK, data);
        } catch (OutOfBoundsException e) {
            try {
                super.setWord(offset, data);
            } catch (OutOfBoundsException e1) {
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                super.setWord(offset, data);
            } catch (OutOfBoundsException e1) {
            }
        }
    }

    public void setDoubleWord(int offset, int data) {
        try {
            getWriteMemoryBlockAt(offset).setDoubleWord(offset & BLOCK_MASK, data);
        } catch (OutOfBoundsException e) {
            try {
                super.setDoubleWord(offset, data);
            } catch (OutOfBoundsException e1) {
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                super.setDoubleWord(offset, data);
            } catch (OutOfBoundsException e1) {
            }
        }
    }

    public void setQuadWord(int offset, long data) {
        try {
            getWriteMemoryBlockAt(offset).setQuadWord(offset & BLOCK_MASK, data);
        } catch (OutOfBoundsException e) {
            try {
                super.setQuadWord(offset, data);
            } catch (OutOfBoundsException e1) {
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                super.setQuadWord(offset, data);
            } catch (OutOfBoundsException e1) {
            }
        }
    }

    public void setLowerDoubleQuadWord(int offset, long data) {
        try {
            getWriteMemoryBlockAt(offset).setLowerDoubleQuadWord(offset & BLOCK_MASK, data);
        } catch (OutOfBoundsException e) {
            try {
                super.setLowerDoubleQuadWord(offset, data);
            } catch (OutOfBoundsException e1) {
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                super.setLowerDoubleQuadWord(offset, data);
            } catch (OutOfBoundsException e1) {
            }
        }
    }

    public void setUpperDoubleQuadWord(int offset, long data) {
        try {
            getWriteMemoryBlockAt(offset).setUpperDoubleQuadWord(offset & BLOCK_MASK, data);
        } catch (OutOfBoundsException e) {
            try {
                super.setUpperDoubleQuadWord(offset, data);
            } catch (OutOfBoundsException e1) {
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            try {
                super.setUpperDoubleQuadWord(offset, data);
            } catch (OutOfBoundsException e1) {
            }
        }
    }

    public void copyArrayIntoContents(int address, byte[] buffer, int off, int len) {
        do {
            int partialLength = Math.min(BLOCK_SIZE - (address & BLOCK_MASK), len);
            Memory block = null;
            try {
                block = getWriteMemoryBlockAt(address);
            } catch (OutOfBoundsException e1) {
            }
            if (block instanceof PhysicalAddressSpace.UnconnectedMemoryBlock) if (this instanceof PhysicalAddressSpace) {
                block = new LazyCodeBlockMemory(BLOCK_SIZE, ((PhysicalAddressSpace) this).getCodeBlockManager());
                ((PhysicalAddressSpace) this).mapMemory(address, block);
            }
            try {
                block.copyArrayIntoContents(address & BLOCK_MASK, buffer, off, partialLength);
            } catch (OutOfBoundsException e) {
            }
            address += partialLength;
            off += partialLength;
            len -= partialLength;
        } while (len > 0);
    }

    public void copyContentsIntoArray(int address, byte[] buffer, int off, int len) {
        do {
            int partialLength = Math.min(BLOCK_SIZE - (address & BLOCK_MASK), len);
            try {
                getReadMemoryBlockAt(address).copyContentsIntoArray(address & BLOCK_MASK, buffer, off, partialLength);
            } catch (OutOfBoundsException e) {
            }
            address += partialLength;
            off += partialLength;
            len -= partialLength;
        } while (len > 0);
    }

    /**
     * Replace all references to <code>original</code> with references to
     * <code>replacement</code>.
     * @param original block to be replaced.
     * @param replacement block to be added.
     */
    protected abstract void replaceBlocks(Memory original, Memory replacement);

    public abstract int executeReal(Processor cpu, int address);

    public abstract int executeProtected(Processor cpu, int address);

    public abstract int executeVirtual8086(Processor cpu, int address);
}
