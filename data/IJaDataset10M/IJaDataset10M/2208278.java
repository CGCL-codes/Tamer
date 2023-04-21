package j8086emu.model.progcode;

import j8086emu.model.exeptions.IncorrectInstructionException;
import j8086emu.model.exeptions.InvalidArgumentExeption;
import j8086emu.model.hardware.CPU;
import j8086emu.model.interfaces.Performable;

/**
 *
 * @author vlad
 */
class CodeTranslator {

    private final PointerCode pointerCode;

    private final ProgramCode programCode;

    private final InstructionsMap instructionsMap;

    private final String[] instructionPatterns;

    public CodeTranslator(PointerCode pointer, ProgramCode code, CPU cpu) {
        pointerCode = pointer;
        programCode = code;
        instructionsMap = new InstructionsMap(cpu);
        instructionPatterns = instructionsMap.keyArray();
    }

    public void performCurrentInstruction() throws IncorrectInstructionException, InvalidArgumentExeption {
        int currentLineNum = pointerCode.getStringNum();
        String currentLine = programCode.getStringOn(currentLineNum);
        String currentPattern = "";
        int concurrencesNum = 0;
        String instructionArgs;
        for (String pattern : instructionPatterns) {
            if (currentLine.matches(pattern)) {
                concurrencesNum++;
                currentPattern = pattern;
            }
        }
        if (concurrencesNum != 1) {
            throw new IncorrectInstructionException(currentLineNum);
        }
        if (!currentLine.matches(".+ .+\n")) {
            instructionArgs = null;
        } else {
            instructionArgs = currentLine.replaceFirst(".+ ", "");
        }
        Performable currentInstruct = instructionsMap.get(currentPattern);
        if (currentInstruct == null) {
            throw new IncorrectInstructionException(currentLineNum);
        }
        currentInstruct.perform(instructionArgs);
    }
}
