package org.rakiura.evm.grid;

import java.io.File;
import org.rakiura.evm.GridExecutionEngine;

/** 
 * TODO: comments
 * @author Lucien Epiney
 */
public class CellFreezing extends CellStochastic {

    private static final int FREEZING_THRESHOLD = 100;

    private static final int WARMUP_THRESHOLD = 2000;

    private int[][] max;

    private int[][] frozen;

    private int[][] freezing;

    private StoreData data;

    public CellFreezing(ParametersCell parameters, ParametersRewards rewardsParameters, ParametersGrid gridParameters, GridExecutionEngine gee, Environment environment, int id) {
        super(parameters, rewardsParameters, gridParameters, gee, environment, id);
        this.max = new int[parameters.rows][parameters.columns];
        this.frozen = new int[parameters.rows][parameters.columns];
        this.freezing = new int[parameters.rows][2];
    }

    /**
	 * Set all instructions to their initial 
	 * probability distributions.
	 */
    public void reset() {
        super.reset();
        for (int i = 0; i < this.parameters.rows; i++) {
            for (int j = 0; j < this.parameters.columns; j++) {
                this.max[i][j] = 0;
                this.frozen[i][j] = -1;
            }
            this.freezing[i][0] = 0;
            this.freezing[i][1] = 0;
        }
        if (this.parameters.storeResults) this.data = new StoreData(this.parameters.directory + File.separator + "cell" + this.id + ".freezing");
    }

    private void coolDown() {
        for (int i = 0; i < this.parameters.rows; i++) {
            int column = this.freezing[i][0];
            if (column != this.parameters.columns) {
                int instr = this.max[i][column];
                if (this.grid[i][column][instr] > 0.9 * this.rewardsParameters.maxValue) {
                    if (DEBUG) System.out.println("[ Freezing ] Program " + i + " instruction " + column + " value " + getMnemotechnicCentered(instr) + " : freezing level = " + this.freezing[i][1]);
                    if (++this.freezing[i][1] > FREEZING_THRESHOLD) freeze(i);
                } else this.freezing[i][1] = 0;
            }
        }
    }

    /**
	 * If a program has been starving for a too long time, it will be unfrozen.
	 */
    private void warmUp() {
        for (int i = 0; i < this.parameters.rows; i++) {
            if (this.freezing[i][0] > 0 && this.pSearch.starving[i] > WARMUP_THRESHOLD) {
                this.freezing[i][0]--;
                this.frozen[i][this.freezing[i][0]] = -1;
                this.freezing[i][1] = 0;
                this.pSearch.starving[i] = 0;
                shakeTail(i);
                if (DEBUG) System.out.println("[ Freezing ] Unfreeze instruction " + this.freezing[i][0] + " of program " + i);
            }
        }
    }

    /**
	 * Reset all the probabilities equal for the instructions following 
	 * the last frozen cell of this program. "Shake" the program's tail.
	 * @param program
	 */
    private void shakeTail(int program) {
        for (int i = this.freezing[program][0]; i < this.parameters.columns; i++) {
            for (int k = 0; k < this.instructions; k++) {
                this.grid[program][i][k] = this.rewardsParameters.initialValue;
            }
        }
    }

    /**
	 * Freeze the current freezing instruction of this program and reset the rest.
	 * @param program
	 */
    private void freeze(int program) {
        if (DEBUG) System.out.println("[ Freezing ] *** Freeze ***");
        int column = this.freezing[program][0];
        int instr = this.max[program][column];
        this.frozen[program][column] = instr;
        this.freezing[program][1] = 0;
        this.freezing[program][0]++;
        if (this.parameters.storeResults) {
            StringBuffer s = new StringBuffer();
            s.append("Iteration: " + this.iteration + "\tFreezing program " + program + " to ");
            for (int i = 0; i < this.freezing[program][0]; i++) {
                s.append(getMnemotechnic(this.frozen[program][i]));
            }
            this.data.print(s + "\n");
        }
        shakeTail(program);
    }

    /**
	 * Pick randomly a value using the probability distributions.
	 * @param row the row of the instruction 
	 * @param column the column of the instruction
	 * @return the value for this instruction
	 */
    protected int getInstruction(int row, int column) {
        if (this.frozen[row][column] >= 0) return this.frozen[row][column];
        return super.getInstruction(row, column);
    }

    public float getFreezingLevel() {
        int sum = 0;
        for (int i = 0; i < this.parameters.rows; i++) {
            sum += this.freezing[i][0] * FREEZING_THRESHOLD + this.freezing[i][1];
        }
        return ((float) sum) / (this.parameters.rows * this.parameters.columns * FREEZING_THRESHOLD);
    }

    /**
	 * Tell how much entropy has this instruction.
	 * @param row the row of the instruction
	 * @param column the column of the instruction
	 * @param instruction will be set to the name of the best base
	 * machine instruction for this instruction.
	 * @return the probability of picking the best base machine 
	 * instruction.
	 */
    public float[] getValue(int row, int column, StringBuffer instruction) {
        float[] result = { 0f, 1f, 1f, 1f };
        int index = this.max[row][column];
        instruction.append(getMnemotechnicCentered(index));
        if (this.frozen[row][column] < 0) {
            float v = (float) this.grid[row][column][index] / this.sum;
            result[0] = (float) 0.7 * (1 - v);
            result[1] = (float) (0.3 + 0.7 * v);
        }
        return result;
    }

    protected void incrementInstruction(int row, int column, int instruction, int increment) {
        if (this.frozen[row][column] >= 0) return;
        super.incrementInstruction(row, column, instruction, increment);
        int maxInstr = this.max[row][column];
        if (instruction != maxInstr && this.grid[row][column][instruction] > this.grid[row][column][maxInstr]) {
            this.max[row][column] = instruction;
            this.freezing[row][1] = 0;
        }
    }

    protected void decrementInstruction(int row, int column, int instruction, int decrement) {
        if (this.frozen[row][column] >= 0) return;
        super.decrementInstruction(row, column, instruction, decrement);
    }

    public void step() {
        super.step();
        coolDown();
        warmUp();
    }
}
