package org.ldp.jdasm.attribute.instruction.opcode;

import java.util.*;
import org.ldp.jdasm.*;
import org.ldp.jdasm.attribute.instruction.*;

/** The LOOKUPSWITCH java instruction. */
public class LOOKUPSWITCH extends Instruction {

    private HashMap<Integer, Instruction> match_offset_table = new HashMap<Integer, Instruction>();

    private Instruction defaultJump;

    private int offset_defaultJump;

    private Vector<Integer> offset_tableJump_key = new Vector<Integer>();

    private Vector<Integer> offset_tableJump_value = new Vector<Integer>();

    /** Default Constructor. */
    public LOOKUPSWITCH() {
        super(Opcode.LOOKUPSWITCH);
    }

    /** Returns the default jump offset stored with {@link LOOKUPSWITCH#setDefaultJumpOffset(int)}.
	 * you will never want to use this method since it only returns the integer passed at 
	 * {@link LOOKUPSWITCH#setDefaultJumpOffset(int)} and not the real default jumping offset
	 * automatically computed.
	 * @see LOOKUPSWITCH#setDefaultJumpOffset(int) */
    public int getDefaultJumpOffset() {
        return offset_defaultJump;
    }

    /** Store default jump offset: you will never want to use this method, since the default jump
	 * offset is automatically computed at build time. This value is mainly unused and returned with 
	 * a call of {@link LOOKUPSWITCH#getDefaultJumpOffset()}. It is used when reading
	 * this opcode from a java class file (the instruction linking is made after) */
    public void setDefaultJumpOffset(int offset_defaultJump) {
        this.offset_defaultJump = offset_defaultJump;
    }

    /** Returns the size of the inner offset table: you will never want to use this method.
	 * See {@link LOOKUPSWITCH#addTableJumpOffset(int,int)} for details.
	 * @see LOOKUPSWITCH#addTableJumpOffset(int,int) 
	 * @see LOOKUPSWITCH#getTableJumpOffsetKey(int) 
	 * @see LOOKUPSWITCH#getTableJumpOffsetValue(int) */
    public int getTableJumpOffsetSize() {
        return this.offset_tableJump_key.size();
    }

    /** Adds an offset into an inner table: you will never want to use this method, use
	 * {@link LOOKUPSWITCH#addMatch(int, Instruction)} instead. This value is mainly unused and can be
	 * retrieved with a call of {@link LOOKUPSWITCH#getTableJumpOffsetKey(int)} or 
	 * {@link LOOKUPSWITCH#getTableJumpOffsetValue(int)}. It is used
	 * when reading this opcode from a java class file (the instruction linking is made after)
	 * @see LOOKUPSWITCH#getTableJumpOffsetSize() 
	 * @see LOOKUPSWITCH#getTableJumpOffsetKey(int) 
	 * @see LOOKUPSWITCH#getTableJumpOffsetValue(int) */
    public void addTableJumpOffset(int key, int value) {
        this.offset_tableJump_key.add(key);
        this.offset_tableJump_value.add(value);
    }

    /** Returns the idx-th jumping offset key from the inner offset table: you will never want 
	 * to use this method.
	 * See {@link LOOKUPSWITCH#addTableJumpOffset(int,int)} for details.
	 * @see LOOKUPSWITCH#addTableJumpOffset(int,int) 
	 * @see LOOKUPSWITCH#getTableJumpOffsetSize() */
    public int getTableJumpOffsetKey(int idx) {
        return offset_tableJump_key.elementAt(idx).intValue();
    }

    /** Returns the idx-th jumping offset value from the inner offset table: you will never want 
	 * to use this method.
	 * See {@link LOOKUPSWITCH#addTableJumpOffset(int,int)} for details.
	 * @see LOOKUPSWITCH#addTableJumpOffset(int,int) 
	 * @see LOOKUPSWITCH#getTableJumpOffsetSize() */
    public int getTableJumpOffsetValue(int idx) {
        return offset_tableJump_value.elementAt(idx).intValue();
    }

    /** Add a match: calling this instruction with <code>key</code> pushed onto
	 * the stack will make the control jump to <code>jumpTo</code> instruction. */
    public void addMatch(int key, Instruction jumpTo) {
        this.match_offset_table.put(new Integer(key), jumpTo);
    }

    /** Get all the matches. */
    public HashMap<Integer, Instruction> getMatches() {
        return this.match_offset_table;
    }

    /** Set the default jump. */
    public void setDefaultJump(Instruction jumpTo) {
        this.defaultJump = jumpTo;
    }

    /** Get the default jump. */
    public Instruction getDefaultJump() {
        return this.defaultJump;
    }

    /** Overloaded: Returns the number of operands. */
    public int getOperandNumber() {
        if (this.offset_tableJump_key.size() > 0) return 2 + this.offset_tableJump_key.size() * 2;
        return 2 + match_offset_table.size() * 2;
    }

    /** Overloaded: Returns the size (in byte) of all the operands */
    public int getOperandSize() {
        return this.getPaddingBytes() + getOperandNumber() * 4;
    }

    /**
	 * Returns the change in byte of the stack after this call.<br>
	 * Every Instruction specify how it will modify the stack: a positive
	 * value it means that one or more value (whose sum in bytes is the returned value)
	 * will be pushed onto the stack at the end of the call; a negative
	 * value it means that such values are popped. */
    public int getStackUse() {
        return -1;
    }

    /** Informs about the type of the values that must be onto the 
	 * stack before the call. See STACK_ constants. */
    public int[] stackRequest() {
        int[] ret;
        ret = new int[] { Instruction.STACK_INT };
        return ret;
    }

    private int getPaddingBytes() {
        int myIdx = super.getInstructionOffset();
        return 3 - (myIdx % 4);
    }

    /** Overloaded: It builds the content of this class into a final byte array.
	 * Such array is passed as first argument, and the index from
	 * where start to insert bytes is the second argument.
	 * @param tofill the array to fill
	 * @param atindex the index from where start to fill
	 * @return the number of byte inserted
	 */
    public int build(byte[] tofill, int atindex) {
        int r = super.build(tofill, atindex);
        for (int i = 0; i < this.getPaddingBytes(); i++) tofill[atindex + (r++)] = 0;
        if (this.defaultJump == null) r += ParseFacilities.addIntToByteArray(this.getBuildLength(), tofill, atindex + r); else r += ParseFacilities.addIntToByteArray(this.defaultJump.getInstructionOffset() - super.getInstructionOffset(), tofill, atindex + r);
        r += ParseFacilities.addIntToByteArray(this.match_offset_table.size(), tofill, atindex + r);
        Vector<Integer> v = new Vector<Integer>(this.match_offset_table.keySet());
        Collections.sort(v);
        for (Enumeration<Integer> e = v.elements(); e.hasMoreElements(); ) {
            Integer key = e.nextElement();
            Instruction val = this.match_offset_table.get(key);
            r += ParseFacilities.addIntToByteArray(key.intValue(), tofill, atindex + r);
            r += ParseFacilities.addIntToByteArray(val.getInstructionOffset() - super.getInstructionOffset(), tofill, atindex + r);
        }
        this.offset_defaultJump = 0;
        this.offset_tableJump_key.clear();
        this.offset_tableJump_value.clear();
        return r;
    }
}
