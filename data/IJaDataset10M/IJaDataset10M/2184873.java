package takatuka.optimizer.bytecode.replacer.logic;

import takatuka.classreader.dataObjs.attribute.*;
import takatuka.classreader.logic.constants.*;
import java.util.*;
import takatuka.classreader.logic.file.*;
import takatuka.classreader.logic.logAndStats.*;

/**
 * 
 * Description:
 * <p>
 * Let say we have a JVM instraction A in any function of any class. 
 * We can convert it into B using this class.
 * Not that B will have same operand as A but different opcode.
 * We convert many instructions using this class. In short group of fload 
 * instructions are converted to equivalent iload instruction group. 
 * Example fload_1 --> iload_1.
 * Furthermore we also convert group of dload instruction to corresponding lload instructions 
 * and also store instructions.  
 * </p> 
 * @author Faisal Aslam
 * @version 1.0
 */
public class ReplaceInstrAWithB {

    private static final String OPNEM = "opcode-mnemonic.properties";

    private static final Properties opcodeMnemoicProperties = PropertyReader.getInstanceOf().loadProperties(OPNEM);

    private static HashSet<Integer> uniqueOpcode = new HashSet();

    private static final ReplaceInstrAWithB replacerMain = new ReplaceInstrAWithB();

    private static boolean isReplaced = false;

    public static final ReplaceInstrAWithB getInstanceOf() {
        return replacerMain;
    }

    static String getMnemonic(int opcode) {
        return opcodeMnemoicProperties.getProperty(opcode + "").trim().toUpperCase();
    }

    public static int getTotalInstructionReplaced() {
        return uniqueOpcode.size();
    }

    public static HashSet getInstructionsReplaced() {
        return uniqueOpcode;
    }

    private void convertInstAtoB(Instruction instA, int newOpCode, String mNemonic) {
        int opcode = instA.getOpCode();
        uniqueOpcode.add(instA.getOpCode());
        if (!isReplaced) {
            isReplaced = true;
            LogHolder.getInstanceOf().addLog("Replacing instruction A with B ....");
        }
        instA.setOpCode(newOpCode);
        instA.setMnemonic(mNemonic);
    }

    private int findNewOpCode(int opCode) {
        int newOpcode = -1;
        switch(opCode) {
            case JavaInstructionsOpcodes.FLOAD:
                newOpcode = JavaInstructionsOpcodes.ILOAD;
                break;
            case JavaInstructionsOpcodes.FLOAD_0:
                newOpcode = JavaInstructionsOpcodes.ILOAD_0;
                break;
            case JavaInstructionsOpcodes.FLOAD_1:
                newOpcode = JavaInstructionsOpcodes.ILOAD_1;
                break;
            case JavaInstructionsOpcodes.FLOAD_2:
                newOpcode = JavaInstructionsOpcodes.ILOAD_2;
                break;
            case JavaInstructionsOpcodes.FLOAD_3:
                newOpcode = JavaInstructionsOpcodes.ILOAD_3;
                break;
            case JavaInstructionsOpcodes.DLOAD:
                newOpcode = JavaInstructionsOpcodes.LLOAD;
                break;
            case JavaInstructionsOpcodes.DLOAD_0:
                newOpcode = JavaInstructionsOpcodes.LLOAD_0;
                break;
            case JavaInstructionsOpcodes.DLOAD_1:
                newOpcode = JavaInstructionsOpcodes.LLOAD_1;
                break;
            case JavaInstructionsOpcodes.DLOAD_2:
                newOpcode = JavaInstructionsOpcodes.LLOAD_2;
                break;
            case JavaInstructionsOpcodes.DLOAD_3:
                newOpcode = JavaInstructionsOpcodes.LLOAD_3;
                break;
            case JavaInstructionsOpcodes.DALOAD:
                newOpcode = JavaInstructionsOpcodes.LALOAD;
                break;
            case JavaInstructionsOpcodes.FALOAD:
                newOpcode = JavaInstructionsOpcodes.IALOAD;
                break;
            case JavaInstructionsOpcodes.FSTORE:
                newOpcode = JavaInstructionsOpcodes.ISTORE;
                break;
            case JavaInstructionsOpcodes.FSTORE_0:
                newOpcode = JavaInstructionsOpcodes.ISTORE_0;
                break;
            case JavaInstructionsOpcodes.FSTORE_1:
                newOpcode = JavaInstructionsOpcodes.ISTORE_1;
                break;
            case JavaInstructionsOpcodes.FSTORE_2:
                newOpcode = JavaInstructionsOpcodes.ISTORE_2;
                break;
            case JavaInstructionsOpcodes.FSTORE_3:
                newOpcode = JavaInstructionsOpcodes.ISTORE_3;
                break;
            case JavaInstructionsOpcodes.DSTORE:
                newOpcode = JavaInstructionsOpcodes.LSTORE;
                break;
            case JavaInstructionsOpcodes.DSTORE_0:
                newOpcode = JavaInstructionsOpcodes.LSTORE_0;
                break;
            case JavaInstructionsOpcodes.DSTORE_1:
                newOpcode = JavaInstructionsOpcodes.LSTORE_1;
                break;
            case JavaInstructionsOpcodes.DSTORE_2:
                newOpcode = JavaInstructionsOpcodes.LSTORE_2;
                break;
            case JavaInstructionsOpcodes.DSTORE_3:
                newOpcode = JavaInstructionsOpcodes.LSTORE_3;
                break;
            case JavaInstructionsOpcodes.DASTORE:
                newOpcode = JavaInstructionsOpcodes.LASTORE;
                break;
            case JavaInstructionsOpcodes.FASTORE:
                newOpcode = JavaInstructionsOpcodes.IASTORE;
                break;
            case JavaInstructionsOpcodes.DCONST_0:
                newOpcode = JavaInstructionsOpcodes.LCONST_0;
                break;
            case JavaInstructionsOpcodes.FRETURN:
                newOpcode = JavaInstructionsOpcodes.IRETURN;
                break;
            case JavaInstructionsOpcodes.DRETURN:
                newOpcode = JavaInstructionsOpcodes.LRETURN;
                break;
        }
        return newOpcode;
    }

    public void convertInstruction(Instruction inst) {
        int newOpCode = findNewOpCode(inst.getOpCode());
        if (newOpCode != -1) {
            convertInstAtoB(inst, newOpCode, getMnemonic(newOpCode));
        }
    }

    public void convertInstructions(Vector instructions) {
        Instruction inst = null;
        for (int loop = 0; loop < instructions.size(); loop++) {
            inst = (Instruction) instructions.elementAt(loop);
            convertInstruction(inst);
        }
    }
}
