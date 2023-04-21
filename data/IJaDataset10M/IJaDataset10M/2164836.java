package simulator.processor.stages;

import main.NumberConversions;
import simulator.memory.Memory;
import simulator.processor.Processor;
import simulator.processor.StageIdentifier;
import simulator.processor.instructions.Instruction;
import java.util.Random;
import static main.SystemConstants.DEFAULT_CODE_START;
import static main.SystemConstants.DELAYED_BRANCHES;
import static simulator.processor.StageIdentifier.STAGE_ID;
import static simulator.processor.StageIdentifier.STAGE_IF;
import static simulator.processor.instructions.InstructionState.COMPLETED;

public class StageIF extends Stage {

    protected int pc;

    protected int newPC;

    protected int raw;

    protected Memory memory;

    protected int memorySize;

    protected StageID stageID;

    Random rnd = new Random();

    public StageIF(Processor processor) {
        super(processor);
    }

    @Override
    public void advance() {
        internalTime = getTime();
        execute();
    }

    @Override
    protected void execute() {
        switch(state) {
            case NORMAL:
                setReservation();
                raw = memory.readInt32(pc);
                instruction = new Instruction();
                processor.addInstruction(instruction);
                instruction.getExecutionData().setCreationTime(internalTime);
                instruction.execute(this);
                if (stageID.isAvailableFor(instruction)) {
                    moveInstruction(stageID, instruction);
                    instruction.getExecutionData().addLogEntry(internalTime, "IF");
                    pc = (pc + 4) % memorySize;
                    clearInstruction();
                    clearReservation();
                } else {
                    changeState(StageState.IF_ID_OCCUPIED);
                    instruction.getExecutionData().inStall();
                    instruction.getExecutionData().addLogEntry(internalTime, "IFst");
                }
                break;
            case IF_ID_OCCUPIED:
                if (stageID.isAvailableFor(instruction)) {
                    moveInstruction(stageID, instruction);
                    instruction.getExecutionData().addLogEntry(internalTime, "IF");
                    pc = (pc + 4) % memorySize;
                    changeState(StageState.NORMAL);
                    clearInstruction();
                    clearReservation();
                } else {
                    instruction.getExecutionData().inStall();
                    instruction.getExecutionData().addLogEntry(internalTime, "IFst");
                }
                break;
            case IF_ID_BRANCH:
                if (instruction == null) {
                    raw = memory.readInt32(pc);
                    instruction = new Instruction();
                    processor.addInstruction(instruction);
                    instruction.getExecutionData().setCreationTime(internalTime);
                    instruction.execute(this);
                }
                instruction.getExecutionData().addLogEntry(internalTime, "IF");
                if (DELAYED_BRANCHES) {
                    moveInstruction(stageID, instruction);
                    instruction.getExecutionData().addLogEntry(internalTime, "IFbds");
                } else {
                    instruction.getExecutionData().addLogEntry(internalTime, "IF");
                    instruction.getExecutionData().addLogEntry(internalTime + 1, "abort");
                    instruction.getExecutionData().setCompletionTime(internalTime + 1);
                    instruction.setState(COMPLETED);
                }
                pc = newPC;
                clearInstruction();
                changeState(StageState.NORMAL);
                break;
            case STOPPING:
                if (instruction == null) {
                    raw = memory.readInt32(pc);
                    instruction = new Instruction();
                    processor.addInstruction(instruction);
                    instruction.getExecutionData().setCreationTime(internalTime);
                    instruction.execute(this);
                }
                instruction.getExecutionData().addLogEntry(internalTime, "IF");
                instruction.getExecutionData().addLogEntry(internalTime + 1, "abort");
                instruction.getExecutionData().setCompletionTime(internalTime + 1);
                instruction.setState(COMPLETED);
                clearInstruction();
                changeState(StageState.STOPPED);
                break;
            case STOPPED:
                break;
            default:
                throw new IllegalStateException("Illegal state encountered: " + state);
        }
    }

    /**
     * Returns current PC.
     */
    public int getPC() {
        return pc;
    }

    /**
     * Sets PC.
     *
     * @throws IllegalArgumentException if pc is out of memory or negative.
     */
    public void setPC(int pc) {
        if ((pc < 0) || (pc >= memorySize)) {
            throw new IllegalArgumentException("PC value out of memory: " + pc);
        }
        this.pc = pc;
    }

    /**
     * Returns current raw instruction word.
     */
    public int getRawInstruction() {
        return raw;
    }

    /**
     * Forces PC update.
     */
    public void forceBranch(int destinationPC) {
        newPC = NumberConversions.unsignedModuloInt32(destinationPC, memorySize);
        changeState(StageState.IF_ID_BRANCH);
    }

    /**
     * Enters trap zero state.
     */
    public void stop() {
        changeState(StageState.STOPPING);
    }

    /**
     * Resets stage.
     */
    @Override
    public void reset() {
        super.reset();
        pc = DEFAULT_CODE_START;
        stageID = processor.getStage(STAGE_ID);
        memory = processor.getCodeMemory();
        memorySize = memory.getSize();
    }

    @Override
    public StageIdentifier getID() {
        return STAGE_IF;
    }
}
