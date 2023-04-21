package org.ldp.jdasm.attribute.instruction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.ldp.jdasm.*;
import org.ldp.jdasm.attribute.CodeAttribute;
import org.ldp.jdasm.constantpool.*;
import org.ldp.jdasm.exception.ClassFileParseException;
import org.ldp.jdasm.exception.InstructionNotReadyException;

/**
 * The ExceptionElement keeps informations about a try-catch block. It has four values:
 * the beginning of the try block, its end, and the beginning of the catch block; you
 * can also specify the class of the exception, or let it empty to handle all exceptions.<br>
 * Note that even if the class file format specify that the end_pc must be the offset of
 * the first instruction outside the try-block, here we intend the end_pc be the last
 * instruction inside the try-block.
 */
public class ExceptionElement extends AConstantPoolUser implements IBuildable {

    private Instruction start_pc;

    private Instruction end_pc;

    private Instruction handler_pc;

    private short cpool_index = 0;

    private String catching_class = null;

    /** Constructs the ExceptionElement that will catch all Exception.
	 * @param start The first instruction inside the try block
	 * @param end The last instruction inside the try block
	 * @param handler The first instruction inside the catch block 
	 */
    public ExceptionElement(Instruction start, Instruction end, Instruction handler) {
        this.start_pc = start;
        this.end_pc = end;
        this.handler_pc = handler;
    }

    /** Constructs the ExceptionElement for a specific class exception.
	 * @param start The first instruction inside the try block
	 * @param end The last instruction inside the try block
	 * @param handler The first instruction inside the catch block
	 * @param catch_class The fully qualified class name 
	 */
    public ExceptionElement(Instruction start, Instruction end, Instruction handler, String catch_class) {
        this.start_pc = start;
        this.end_pc = end;
        this.handler_pc = handler;
        this.catching_class = catch_class;
    }

    /** Creates a ExceptionElement reading it from an input stream
	 * whose cursor must point to the beginning of the Exception element.
	 * It needs a fill-of-constant constant pool and a fill-of-code CodeAttribute. */
    public ExceptionElement(InputStream is, DConstantPool cpool, CodeAttribute code) throws IOException, ClassFileParseException {
        this.start_pc = code.getInstructionAtOffset(ParseFacilities.getShortFromInputStream(is));
        int end_pc_index = ParseFacilities.getShortFromInputStream(is);
        if (end_pc_index == code.getByteCodeSize()) this.end_pc = code.getLastInstruction(); else {
            Instruction outsideBlock = code.getInstructionAtOffset(end_pc_index);
            try {
                this.end_pc = code.getInstruction(outsideBlock.getInstructionIndex() - 1);
            } catch (InstructionNotReadyException e) {
                throw new ClassFileParseException("No instruction index built before the call to this constructor");
            }
        }
        this.handler_pc = code.getInstructionAtOffset(ParseFacilities.getShortFromInputStream(is));
        this.cpool_index = ParseFacilities.getShortFromInputStream(is);
        if (this.cpool_index != 0) this.catching_class = ((CONSTANT_Class_Info) cpool.getConstant(this.cpool_index)).getClassName();
        if (this.start_pc == null || this.handler_pc == null) throw new ClassFileParseException("Error while reading ExceptionElement in a CodeAttribute from input stream: the instruction byte offset is invalid");
    }

    /** Get the catching class name, or <code>null</code> if it is not set (all exception handled). */
    public String getCatchingClass() {
        return catching_class;
    }

    /** Set the catching class name (fully qualified name). Use <code>null</code> to handle all exceptions. */
    public void setCatchingClass(String catch_type) {
        this.catching_class = catch_type;
    }

    /** Get the last instruction inside the try block. */
    public Instruction getEnd() {
        return end_pc;
    }

    /** Set the last instruction inside the try block. */
    public void setEnd(Instruction end_pc) {
        this.end_pc = end_pc;
    }

    /** Get the handler instruction (the first inside the catch block). */
    public Instruction getHandler() {
        return handler_pc;
    }

    /** Set the handler instruction (the first inside the catch block). */
    public void setHandler(Instruction handler_pc) {
        this.handler_pc = handler_pc;
    }

    /** Get the start instruction (the first inside the try block). */
    public Instruction getStart() {
        return start_pc;
    }

    /** Set the start instruction (the first inside the try block). */
    public void setStart(Instruction start_pc) {
        this.start_pc = start_pc;
    }

    /** Returns true if the given <code>instruction</code> falls inside the try-block
	 * specified by this ExceptionElement. */
    public boolean instructionIsInTryBlock(Instruction instruction) {
        try {
            int idx = instruction.getInstructionIndex();
            int startIdx = this.start_pc.getInstructionIndex();
            int endIdx = this.end_pc.getInstructionIndex();
            return startIdx <= idx && idx <= endIdx;
        } catch (InstructionNotReadyException e) {
            return false;
        }
    }

    /** Returns the number of IConstantPoolUser instances of this class. */
    public int constantPoolUserChildSize() {
        return 0;
    }

    /** Returns the idx-th IConstantPoolUser instance of this class. */
    public AConstantPoolUser getConstantPoolUserChild(int idx) {
        return null;
    }

    /** Returns the number of values that this constant pool user
	 * wants to insert into the constant pool. */
    public int constantPoolValueSize() {
        return this.catching_class == null ? 0 : 1;
    }

    /** Returns the idx-th value that this constant pool user
	 * wants to insert into the constant pool. 
	 * @throws Exception if the value is not ready (since the control is done only at build time) */
    public Object getConstantValue(int idx) throws Exception {
        return this.getCatchingClass();
    }

    /** Returns the value type of the idx-th value that this constant 
	 * pool user wants to insert into the constant pool. The list
	 * of the types is declared in {@link DConstantPool}
	 * @throws Exception if the value is not ready (since the control is done only at build time) */
    public int getConstantValueType(int idx) throws Exception {
        return DConstantPool.CONSTANT_Class;
    }

    /** The constant pool tells the user that the value retrieved
	 * with {@link AConstantPoolUser#getConstantValue(int)} at index
	 * "idx" has received the "cpool_index" index in the  constant pool.
	 * @param idx the index of the value mapped with the ones returned by getConstantValue(int)
	 * @param cpool_index the index of the value in the constant pool
	 */
    public void setConstantValueIndex(int idx, short cpool_index) {
        this.cpool_index = cpool_index;
    }

    /** It builds the content of this class into a final byte array.
	 * Such array is passed as first argument, and the index from
	 * where start to insert bytes is the second argument.
	 * @param tofill the array to fill
	 * @param atindex the index from where start to fill
	 * @return the number of byte inserted
	 */
    public int build(byte[] tofill, int atindex) {
        int r = ParseFacilities.addShortToByteArray((short) this.start_pc.getInstructionOffset(), tofill, atindex);
        r += ParseFacilities.addShortToByteArray((short) (this.end_pc.getInstructionOffset() + this.end_pc.getBuildLength()), tofill, atindex + r);
        r += ParseFacilities.addShortToByteArray((short) this.handler_pc.getInstructionOffset(), tofill, atindex + r);
        if (this.catching_class != null) r += ParseFacilities.addShortToByteArray(this.cpool_index, tofill, atindex + r); else r += ParseFacilities.addShortToByteArray((short) 0, tofill, atindex + r);
        return r;
    }

    /** It builds the content of this class into an output stream. 
	 * This method is a faster version of {@link IBuildable#build(byte[], int)}
	 * since it doesn't need to call the {@link IBuildable#getBuildLength()} before. 
	 * @param output the stream to write onto
	 * @return the number of byte inserted
	 * @exception on error while writing onto the stream */
    public int build(OutputStream output) throws IOException {
        int r = ParseFacilities.addShortToOutputStream((short) this.start_pc.getInstructionOffset(), output);
        r += ParseFacilities.addShortToOutputStream((short) (this.end_pc.getInstructionOffset() + this.end_pc.getBuildLength()), output);
        r += ParseFacilities.addShortToOutputStream((short) this.handler_pc.getInstructionOffset(), output);
        if (this.catching_class != null) r += ParseFacilities.addShortToOutputStream(this.cpool_index, output); else r += ParseFacilities.addShortToOutputStream((short) 0, output);
        return r;
    }

    /** Returns the length of portion of byte array the IBuildable
	 * class is going to create. If this class has instances of other
	 * IBuildable classes, then it will return the whole sum of all
	 * the various getBuildLength().
	 */
    public int getBuildLength() {
        return 8;
    }

    /** Returns a printable info string */
    public String classFileInfo(int indent_level) {
        if (this.catching_class == null) return ParseFacilities.getIndent(indent_level) + " start_pc(" + this.start_pc + ") end_pc(" + end_pc + ") handler_pc(" + handler_pc + ") catching all classes"; else return ParseFacilities.getIndent(indent_level) + " start_pc(" + this.start_pc + ") end_pc(" + end_pc + ") handler_pc(" + handler_pc + ") catch(" + this.catching_class + ")(" + ParseFacilities.classInfoPrintIntLink(this.cpool_index) + ")";
    }
}
