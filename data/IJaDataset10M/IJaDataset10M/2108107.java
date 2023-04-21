package takatuka.offlineGC.logic;

import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.dataObjs.attribute.Instruction;

/**
 * 
 * Description:
 * <p>
 * </p> 
 * @author Faisal Aslam
 * @version 1.0
 */
public class InstructionSequenceBlock {

    private int blockId = -1;

    private Vector<InstructionAndIndex> instructions = new Vector();

    private int methodNumber = -1;

    private ClassFile cFile = null;

    private int currentInstructionIndex = -1;

    public InstructionSequenceBlock(int blockId, int methodNumber, ClassFile cFile) {
        this.blockId = blockId;
        this.methodNumber = methodNumber;
        this.cFile = cFile;
    }

    public int getMethodNumber() {
        return methodNumber;
    }

    public ClassFile getClassFile() {
        return cFile;
    }

    public int getBlockId() {
        return blockId;
    }

    public Vector getInstructions() {
        return instructions;
    }

    public void addInstrution(Instruction inst) {
        currentInstructionIndex++;
        instructions.addElement(new InstructionAndIndex(inst, currentInstructionIndex));
    }
}
