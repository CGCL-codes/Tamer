package net.sourceforge.jspcemulator.client.emulator.memory.codeblock;

import net.sourceforge.jspcemulator.client.emulator.memory.OutOfBoundsException;

/**
 * Supplies a stream of JPC instructions for compiling a codeblock.
 * @author Chris Dennis
 * @author Kevin O'Dwyer (for JsPCEmulator)
 */
public interface InstructionSource {

    /**
     * Advances to the next x86 instruction in the stream.
     * @return <code>true</code> if there are more instructions.
     */
    public boolean getNext() throws OutOfBoundsException;

    /**
     * Get the next microcode component of this x86 operation.
     * @return microcode value
     */
    public int getMicrocode();

    /**
     * Get the length of this x86 instruction in microcodes
     * @return length in microcodes
     */
    public int getLength();

    /**
     * Get the lenth of this x86 instruction in original x86 bytes.
     * @return length of this instruction in bytes.
     */
    public int getX86Length();

    /**
     * Reset to the start of the instruction source
     */
    public void reset();
}
