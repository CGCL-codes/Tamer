package takatuka.classreader.dataObjs.attribute;

import java.util.*;
import org.apache.commons.lang.builder.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.logic.constants.*;
import takatuka.classreader.logic.exception.*;
import takatuka.classreader.logic.factory.*;
import takatuka.io.*;
import takatuka.optimizer.bytecode.branchSetter.dataObjs.attributes.BHInstruction;
import takatuka.optimizer.bytecode.changer.logic.comb.*;
import takatuka.classreader.logic.util.*;

/**
 * <p>Title: </p>
 * <p>Description:
 * It is based on section 4.7.3 of http://java.sun.com/docs/books/jvms/second_edition/html/ClassFile.doc.html#43817
 * Code_attribute {
u2 attribute_name_index; //super
u4 attribute_length; // super
u2 max_stack;
u2 max_locals;
u4 code_length;
u1 code[code_length];
u2 exception_table_length;
{    	u2 start_pc;
u2 end_pc;
u2  handler_pc;
u2  catch_type;
}	exception_table[exception_table_length];
u2 attributes_count;
attribute_info attributes[attributes_count];
}
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class CodeAtt extends AttributeInfo {

    private Un max_stack;

    private Un max_locals;

    private Un code_length;

    private Un code;

    private Vector instructions = new Vector();

    private Un exception_table_length;

    private Vector<ExceptionTableEntry> exception_table = new Vector();

    private Un attributes_count;

    private AttributeInfoController attributes;

    private static FactoryFacade factory = FactoryPlaceholder.getInstanceOf().getFactory();

    private static BytecodeProcessor bcProc = factory.createBytecodeProcessor();

    private static long totalLengthOfCode = 0;

    private static long totalInstructions = 0;

    private MethodInfo method = null;

    private static HashMap<Long, CodeAtt> instrIdToCodeAttMap = new HashMap<Long, CodeAtt>();

    public CodeAtt() {
    }

    public CodeAtt(Un u2_attrNameIndex, Un u4_attributeLength, Un u2_maxStack, Un u2_maxLocals, Un u4_codeLength) throws Exception {
        super(u2_attrNameIndex, u4_attributeLength);
        setMaxStack(u2_maxStack);
        setMaxLocals(u2_maxLocals);
        setCodeLength(u4_codeLength);
        totalLengthOfCode += (long) getCodeLength().intValueUnsigned();
    }

    public void setMethod(MethodInfo methodInfo) {
        this.method = methodInfo;
    }

    public MethodInfo getMethod() {
        return this.method;
    }

    public static long getTotalNumberOfInstructions() {
        return totalInstructions;
    }

    /**
     * Let of Java bytecode read from class files.
     * @return
     */
    public static long getCodeTotalLengthInput() {
        return totalLengthOfCode;
    }

    public static void setCodeTotalengthInput(long length) {
        totalLengthOfCode = length;
    }

    public Vector getInstructions() {
        return instructions;
    }

    private void removeOldInstrIdRecord() {
        Iterator<Instruction> it = instructions.iterator();
        while (it.hasNext()) {
            Instruction instr = it.next();
            long instrId = instr.getInstructionId();
            instrIdToCodeAttMap.remove(instrId);
        }
    }

    private void addNewInstrIdRecord() {
        Iterator<Instruction> it = instructions.iterator();
        while (it.hasNext()) {
            Instruction instr = it.next();
            long instrId = instr.getInstructionId();
            instrIdToCodeAttMap.put(instrId, this);
        }
    }

    /**
     * given an instruction id it returns its corresponding codeAtt
     * @param instrId
     * @return
     */
    public static CodeAtt getCodeAttForInstr(long instrId) {
        return instrIdToCodeAttMap.get(instrId);
    }

    public void setInstructions(Vector instructions) {
        removeOldInstrIdRecord();
        this.instructions = instructions;
        updateCodeLength();
        Instruction.setAllOffsets(instructions);
        addNewInstrIdRecord();
    }

    /**
     * update codeAttribute length based on instructions.
     * returns the new length.
     *
     * @return int
     */
    public int updateCodeLength() {
        int codeLength = 0;
        for (int loop = 0; loop < instructions.size(); loop++) {
            Instruction inst = (Instruction) instructions.elementAt(loop);
            codeLength += inst.length();
        }
        try {
            setCodeLength(factory.createUn(codeLength));
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return codeLength;
    }

    /**
     * Merge two code attributes.
     *
     * @param codeAtt CodeAtt
     * @throws Exception
     */
    public void merge(CodeAtt codeAtt) throws Exception {
        Vector instVectorLocal = codeAtt.getInstructions();
        int newMaxLocals = getMaxLocals().intValueUnsigned() + codeAtt.getMaxLocals().intValueUnsigned();
        int newMaxStack = getMaxStack().intValueUnsigned() + codeAtt.getMaxStack().intValueUnsigned();
        setMaxLocals(factory.createUn(newMaxLocals).trim(2));
        setMaxStack(factory.createUn(newMaxStack).trim(2));
        Vector currentInstructions = getInstructions();
        Instruction lastInstruction = (Instruction) currentInstructions.elementAt(currentInstructions.size() - 1);
        if (lastInstruction.getMnemonic().contains("RETURN")) {
            if (lastInstruction instanceof InstructionsCombined) {
                throw new Exception("Cannot handle combine instructions");
            }
            lastInstruction.setOpCode(JavaInstructionsOpcodes.NOP);
            lastInstruction.setMnemonic("NOP");
            BytecodeProcessor.removeFromFixUnUsedInstruction(JavaInstructionsOpcodes.NOP);
        }
        for (int loop = 0; loop < instVectorLocal.size(); loop++) {
            currentInstructions.addElement(instVectorLocal.elementAt(loop));
        }
        updateCodeLength();
    }

    public void setMaxStack(Un u2) throws UnSizeException {
        Un.validateUnSize(2, u2);
        this.max_stack = u2;
    }

    public Un getMaxStack() {
        return max_stack;
    }

    public void setMaxLocals(Un u2) throws UnSizeException {
        Un.validateUnSize(2, u2);
        this.max_locals = u2;
    }

    public Un getMaxLocals() {
        return max_locals;
    }

    public void incCodeLength() throws UnSizeException, Exception {
        int codeLength = this.code_length.intValueUnsigned();
        this.code_length = factory.createUn(codeLength + 1).trim(4);
    }

    private void setCodeLength(Un u4) throws UnSizeException, Exception {
        Un.validateUnSize(4, u4);
        this.code_length = u4;
    }

    public Un getCodeLength() {
        return code_length;
    }

    public void setCode(Un code) {
        this.code = code;
        try {
            instructions = bcProc.process(code.getData(), this);
            totalInstructions += instructions.size();
        } catch (Exception d) {
            Miscellaneous.printlnErr(code);
            d.printStackTrace();
            Miscellaneous.exit();
        }
    }

    public Un getCode() {
        return code;
    }

    public void setExceptionTableLength(Un u2) throws UnSizeException, Exception {
        Un.validateUnSize(2, u2);
        this.exception_table_length = u2;
    }

    public void setExceptionTableLength(int length) throws UnSizeException, Exception {
        this.exception_table_length = factory.createUn(length).trim(2);
    }

    public Un getExceptionTableLength() throws Exception {
        return exception_table_length;
    }

    public void setAttributeCount(Un u2) throws UnSizeException, Exception {
        Un.validateUnSize(2, u2);
        this.attributes_count = u2;
        if (u2 != null) {
            attributes = factory.createAttributeInfoController(u2.intValueUnsigned());
        } else {
            attributes = factory.createAttributeInfoController(0);
        }
    }

    public AttributeInfoController getAttributes() {
        return attributes;
    }

    public int getAttributeCount() throws Exception {
        return attributes_count.intValueUnsigned();
    }

    public void addAttribute(AttributeInfo attInfo) throws Exception {
        attributes.add(attInfo);
        if (attributes.getCurrentSize() > this.attributes_count.intValueUnsigned()) {
            throw new Exception("Invalid entry in codeAttribute's attributes");
        }
    }

    public void addExceptionTable(Un start_pc, Un end_pc, Un handler_pc, Un catch_type) throws UnSizeException, Exception {
        ExceptionTableEntry expTable = new ExceptionTableEntry(start_pc, end_pc, handler_pc, catch_type);
        exception_table.addElement(expTable);
        expTable.savePCInformation(this);
        if (exception_table.size() > getExceptionTableLength().intValueUnsigned()) {
            throw new Exception("Invalid data in the exception table");
        }
    }

    /**
     * add a new exceptionTable based on instruction Ids.
     *
     * @param startPCInstrId
     * @param endPCInstrId
     * @param handlerPCInsrId
     * @param catchType
     * @throws Exception
     */
    public void addExceptionTable(long startPCInstrId, long endPCInstrId, long handlerPCInsrId, int catchType) throws Exception {
        ExceptionTableEntry expTable = new ExceptionTableEntry(startPCInstrId, endPCInstrId, handlerPCInsrId, catchType);
        exception_table.addElement(expTable);
        int oldSize = getExceptionTableLength().intValueUnsigned();
        if (exception_table.size() > oldSize) {
            setExceptionTableLength(oldSize + 1);
        }
    }

    public void restorePCInfoInExceptionTables(BHInstruction inst) {
        try {
            int size = exception_table.size();
            for (int index = 0; index < size; index++) {
                exception_table.elementAt(index).restorePCInformation(inst);
            }
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
    }

    public void addAllExceptionTablesByCutting(Un bytes) throws UnSizeException, Exception {
        for (int loop = 0; loop < getExceptionTableLength().intValueUnsigned(); loop++) {
            addExceptionTable(Un.cutBytes(2, bytes), Un.cutBytes(2, bytes), Un.cutBytes(2, bytes), Un.cutBytes(2, bytes));
        }
    }

    public Vector<ExceptionTableEntry> getExceptions() {
        return exception_table;
    }

    public Un getStartPC(int index) {
        return ((ExceptionTableEntry) exception_table.elementAt(index)).getStartPC();
    }

    public Un getEndPC(int index) {
        return ((ExceptionTableEntry) exception_table.elementAt(index)).getEndPC();
    }

    public Un getHandlerPC(int index) {
        return ((ExceptionTableEntry) exception_table.elementAt(index)).getHandlerPC();
    }

    public Un getCatchType(int index) {
        return ((ExceptionTableEntry) exception_table.elementAt(index)).getCatchType();
    }

    public void setCatchType(int index, Un value) throws Exception {
        ((ExceptionTableEntry) exception_table.elementAt(index)).setCatchType(value);
    }

    @Override
    public String toString() {
        String ret = "";
        try {
            ret = ret + writeSelected(null);
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CodeAtt)) {
            return false;
        }
        CodeAtt att = (CodeAtt) obj;
        if (super.equals(att) && max_stack.equals(att.max_stack) && max_locals.equals(att.max_locals) && code_length.equals(att.code_length) && instructions.equals(att.instructions) && exception_table_length.equals(att.exception_table_length) && exception_table.equals(att.exception_table)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(max_stack).append(max_stack).append(code_length).append(code_length).append(instructions).append(exception_table_length).append(exception_table).append(super.hashCode()).toHashCode();
    }

    protected String writeInsructions(BufferedByteCountingOutputStream buff) throws Exception {
        String ret = "code={\n";
        for (int loop = 0; loop < instructions.size(); loop++) {
            ret += "\t\t" + ((Instruction) instructions.elementAt(loop)).writeSelected(buff);
            if (loop + 1 < instructions.size()) {
                ret = ret + "\n ";
            }
        }
        ret = ret + "}";
        return ret;
    }

    @Override
    public String writeSelected(BufferedByteCountingOutputStream buff) throws Exception {
        String ret = " CodeAtt=" + super.writeSelected(buff);
        ret = ret + ", max_stack =" + max_stack.writeSelected(buff);
        ret = ret + ", max_locals =" + max_locals.writeSelected(buff);
        ret = ret + ", code_length =" + getCodeLength().writeSelected(buff);
        ret = ret + ", " + writeInsructions(buff);
        ret = ret + ", exception_table_length =" + exception_table_length.writeSelected(buff);
        ret = ret + ", exception_table={";
        for (int loop = 0; loop < exception_table_length.intValueUnsigned(); loop++) {
            ret = ret + ((ExceptionTableEntry) exception_table.elementAt(loop)).writeSelected(buff) + ", ";
        }
        ret = ret + "} ";
        ret = ret + ", Attributes ={" + ((AttributeInfoController) attributes).writeSelected(buff) + "}";
        return ret;
    }
}
