package org.rakiura.evm.grid;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import org.rakiura.evm.GridExecutionEngine;
import org.rakiura.evm.Machine;
import org.rakiura.evm.MachinesWorld;

/**
 * TODO: 
 *  - comments
 *  - clean constructors
 * @author Lucien Epiney
 */
public class Web implements MachinesWorld, GridDisplay {

    private Cell[] cells;

    private ParametersGrid parameters;

    private Environment env;

    public StoreData[] data;

    private int[] pseudoOrdered;

    public int iteration;

    public Web(ParametersGrid gp, ParametersCell cp, ParametersRewards rp, Environment env) {
        this(gp, cp, rp, env, null);
    }

    public Cell getCell(int row, int column) {
        return this.cells[coord2id(row, column)];
    }

    public Cell getCell(int id) {
        return this.cells[id];
    }

    private void createCells(ParametersCell cp, ParametersRewards rp, Environment env, Cell[] cells) {
        if (cells == null) this.cells = new Cell[this.parameters.rows * this.parameters.columns]; else this.cells = cells;
        GridExecutionEngine gee = new GridExecutionEngine(this, this.parameters.baseMachine, cp.bmInstructions, cp.rows);
        if (this.parameters.cellType.equalsIgnoreCase("Tree")) gee.autoExpandProgram = true;
        try {
            Class cellClass = Class.forName("org.rakiura.evm.grid.Cell" + this.parameters.cellType);
            Class[] argClass = { ParametersCell.class, ParametersRewards.class, ParametersGrid.class, GridExecutionEngine.class, Environment.class, int.class };
            Constructor constructor = cellClass.getConstructor(argClass);
            for (int id = 0; id < this.cells.length; id++) {
                if (this.cells[id] == null) this.cells[id] = (Cell) constructor.newInstance(cp, rp, this.parameters, gee, env, id); else if (this.parameters.runFrozenCells && this.cells[id] instanceof CellFrozen) ((CellFrozen) this.cells[id]).setUp(gee, env, cp.timeAllowed, cp.checks);
            }
        } catch (Exception e) {
            System.err.println("Creating cells failed.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void cleanDirectory(String dirName) {
        cleanDirectory(new File(dirName));
    }

    public static void cleanDirectory(File dir) {
        File[] elements = dir.listFiles();
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].isDirectory()) cleanDirectory(elements[i]);
            elements[i].delete();
        }
        if (Cell.DEBUG) System.out.println("[ Info ] Directory " + dir + " has now " + dir.list().length + " element(s): ");
        for (int i = 0; i < dir.list().length; i++) System.out.println(dir.list()[i]);
    }

    /**
	 * Create a grid.
	 * If some cells are not initialized, will create new fixed-size
	 * cells for them.
	 * @param gp grid parameters
	 * @param cp cell parameters
	 * @param rp rewards parameters
	 * @param env environment
	 * @param cells already initialized cells
	 */
    public Web(ParametersGrid gp, ParametersCell cp, ParametersRewards rp, Environment env, Cell[] cells) {
        this.parameters = gp;
        this.env = env;
        createCells(cp, rp, env, cells);
        reset(cp.directory);
        if (this.parameters.storeEnvironmentalResults) this.env.setStorageDirectory(cp.directory);
    }

    public void reset(String directory) {
        this.iteration = 0;
        cleanDirectory(directory);
        if (this.parameters.storeResults) {
            this.data = new StoreData[2];
            this.data[0] = new StoreData(directory + File.separator + "grid.bests");
            this.data[1] = new StoreData(directory + File.separator + "grid.average");
        }
        for (int i = 0; i < this.cells.length; i++) if (!(this.cells[i] instanceof CellFrozen)) this.cells[i].reset();
        if (false) {
            int size = this.parameters.rows * this.parameters.columns;
            this.pseudoOrdered = new int[size];
            List<Integer> l = new ArrayList<Integer>();
            for (int i = 0; i < size; i++) l.add(new Integer(i));
            for (int i = 0; i < size; i++) {
                int n = (int) Math.floor(Math.random() * l.size());
                this.pseudoOrdered[i] = l.get(n).intValue();
                l.remove(n);
            }
        }
    }

    /**
	 * To access a neighbour of an inhabitant.
	 * 
	 * @param id id of the inhabitant
	 * @param index of the neighbour
	 * @return the id of the desired neighbour (-1 if does not exist)
	 */
    public int getNeighbour(int id, int index) {
        if (index > this.parameters.neighbourhood || index < 0) {
            System.out.println("[ Error ] Neighbour index " + index + " invalid. Neighbourhood: " + this.parameters.neighbourhood);
            return -1;
        }
        if (!this.parameters.reflection) index += 1;
        switch(index) {
            case 0:
                return id;
            case 1:
                return (id % this.parameters.columns == this.parameters.columns - 1) ? id - this.parameters.columns + 1 : id + 1;
            case 2:
                return (id % this.parameters.columns == 0) ? id + this.parameters.columns - 1 : id - 1;
            case 3:
                return (id / this.parameters.columns == this.parameters.rows - 1) ? id - (this.parameters.rows - 1) * this.parameters.columns : id + this.parameters.columns;
            case 4:
                return (id / this.parameters.columns == 0) ? id + (this.parameters.rows - 1) * this.parameters.columns : id - this.parameters.columns;
            default:
                return -1;
        }
    }

    /**
	 * The inhabitant of the machines world will provide a machine.
	 * @param id of the desired inhabitant
	 * @return the machine
	 */
    public Machine getMachine(int id) {
        return this.cells[id].getMachine();
    }

    /**
	 * Rewards an inhabitant for a successful machine.
	 * 
	 * @param id of the desired inhabitant
	 * @param machine the machine it got the rewards with
	 * @param instructionsUsed an array containing for each instruction
	 * the number of times it has been used
	 * @param reward the reward
	 */
    public void rewards(int id, Machine machine, int[] instructionsUsed, int reward) {
        this.cells[id].rewards(machine, instructionsUsed, reward);
    }

    /**
	 * Run all the cells (that are not frozen) as threads.
	 * Asynchronous implementation. Not really a good idea with Java.
	 * Some cells may wait a very long time before getting CPU.
	 */
    public void runAsynchronous() {
        for (int i = 0; i < this.cells.length; i++) {
            if (!(this.cells[i] instanceof CellFrozen)) {
                new Thread(this.cells[i]).start();
                if (GridExecutionEngine.DEBUG) System.out.println("[ Info ]  Cell " + i + " started.");
            }
        }
    }

    /**
	 * Very simple a 
	 * Basically just swap with the previous cell if has a bigger value.
	 * Uncomment to also swap with the next cell if has a smaller value.
	 */
    private void pseudoSortCells() {
        for (int i = 1; i < this.pseudoOrdered.length; i++) {
            Cell cell = this.cells[this.pseudoOrdered[i]];
            float v = 0;
            if (cell instanceof CellFreezing) v = ((CellFreezing) cell).getFreezingLevel(); else if (cell instanceof CellRandom && ((CellRandom) cell).frozen >= 0) v = 1;
            float vLeft = 0;
            cell = this.cells[this.pseudoOrdered[i - 1]];
            if (cell instanceof CellFreezing) vLeft = ((CellFreezing) cell).getFreezingLevel(); else if (cell instanceof CellRandom && ((CellRandom) cell).frozen >= 0) vLeft = 1;
            if (v > vLeft) {
                int tmp = this.pseudoOrdered[i];
                this.pseudoOrdered[i] = this.pseudoOrdered[i - 1];
                this.pseudoOrdered[i - 1] = tmp;
            }
        }
    }

    /**
	 * Run all cells (that are not frozen). One step per cell.
	 * @return true if a new solution have been found
	 * TODO: unable to execute different tyes of cells
	 */
    public boolean step() {
        boolean result = true;
        int best = 0;
        double average = 0;
        if (this.pseudoOrdered != null) {
            pseudoSortCells();
            for (int i = 0; i < this.pseudoOrdered.length; i++) {
                Cell cell = this.cells[this.pseudoOrdered[i]];
                cell.step();
            }
            result = false;
        } else {
            for (int i = 0; i < this.cells.length; i++) {
                Cell cell = this.cells[i];
                if (cell.pSearch != null) {
                    cell.step();
                    result = false;
                }
            }
        }
        average /= (this.parameters.rows * this.parameters.columns);
        if (Cell.DEBUG) {
            StringBuffer s = new StringBuffer();
            s.append("-----ENVIRONMENT-----\n");
            for (int i = 0; i < this.env.resources.length; i++) {
                Resource r = this.env.resources[i];
                s.append(r.name + "\tquality " + r.quality + "\tquantity: " + r.quantity + "\n");
            }
            s.append("---------------------\n");
            System.out.println(s.toString());
        }
        if (this.parameters.storeEnvironmentalResults) this.env.storeData();
        if (this.parameters.storeResults) {
            this.data[0].addData(best);
            this.data[1].addData(average);
        }
        if (this.env != null) this.env.renew();
        this.iteration++;
        return result;
    }

    /**
	 * Run all cells (that are not frozen). One step per cell at a time.
	 * Synchronous implementation.
	 */
    public void run() {
    }

    /**
	 * Will set what to display for every cell of the grid.
	 * @param row the cell's row
	 * @param column the cell's column
	 * @param inside what will be printed in the cell
	 * @param toolTip to set the tool tip text
	 * @return an array of 4 float values to set the color
	 * (HSB mode + transparency).
	 */
    public float[] getValue(int row, int column, StringBuffer inside, StringBuffer toolTip) {
        return this.cells[row * this.parameters.columns + column].getDisplay(inside, toolTip);
    }

    private int coord2id(int row, int column) {
        return row * this.parameters.columns + column;
    }

    public static void main(String[] args) {
        cleanDirectory("tmp");
    }
}
