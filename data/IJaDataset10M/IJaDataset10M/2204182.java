package edu.hawaii.ics.ami.element.model.module;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import edu.hawaii.ics.ami.event.model.LocationParameterEvent;

/**
 * Eye tracking grid mode.
 *
 * @author   king
 * @since    October 20, 2004
 */
public class EyeTrackingGridMode extends EyeTrackingMode {

    /** The number of columns to use for grid display. */
    private int columns = 10;

    /** The number of rows to use for grid display. */
    private int rows = 10;

    /**
   * Constructor for the eye tracking mode, that defines a type of
   * visualization. 
   * 
   * @param eyeTrackingTargetModule  The eye tracking target module that belongs to it.
   */
    public EyeTrackingGridMode(EyeTrackingTargetModule eyeTrackingTargetModule) {
        super(eyeTrackingTargetModule);
    }

    /**
   * Returns the number of columns to use if grid display.
   * 
   * @return  The number of columns to use for grid display.
   */
    public int getColumns() {
        return this.columns;
    }

    /**
   * Sets the number of columns to use for grid display.
   * 
   * @param columns  Sets the number of columns to use for grid display.
   */
    public void setColumns(int columns) {
        this.columns = columns;
        setChanged();
        notifyObservers();
    }

    /**
   * Returns the number of rows to use if grid display.
   * 
   * @return  The number of rows to use for grid display.
   */
    public int getRows() {
        return this.rows;
    }

    /**
   * Sets the number of rows to use for grid display.
   * 
   * @param rows  Sets the number of rows to use for grid display.
   */
    public void setRows(int rows) {
        this.rows = rows;
        setChanged();
        notifyObservers();
    }

    /**
   * Returns the number of samples that fell into each grid cell.
   * 
   * @return  The number of samples for each grid cell.
   */
    public int[][] getGrid() {
        List fixationEvents = this.eyeTrackingTargetModule.getEyeFixationEvents();
        int grid[][] = new int[rows][columns];
        for (int i = 0; i < fixationEvents.size(); i++) {
            LocationParameterEvent fixationEvent = (LocationParameterEvent) fixationEvents.get(i);
            float x = fixationEvent.getX().floatValue();
            float y = fixationEvent.getY().floatValue();
            int samples = ((Integer) fixationEvent.getParameter("Samples")).intValue();
            int gridX = (int) (x * columns);
            int gridY = (int) (y * rows);
            if ((gridX >= 0) && (gridX < columns) && (gridY >= 0) && (gridY < rows)) {
                grid[gridY][gridX] += samples;
            }
        }
        return grid;
    }

    /**
   * Returns the number of total samples for all the grids together.
   * 
   * @return  The total number of samples for all grids.
   */
    public int getGridTotal() {
        List fixationEvents = this.eyeTrackingTargetModule.getEyeFixationEvents();
        int total = 0;
        for (int i = 0; i < fixationEvents.size(); i++) {
            LocationParameterEvent fixationEvent = (LocationParameterEvent) fixationEvents.get(i);
            int samples = ((Integer) fixationEvent.getParameter("Samples")).intValue();
            total += samples;
        }
        return total;
    }

    /**
   * Saves the data from this target module to a file.
   * 
   * @param fileName  The file name where to save the data to.
   * @throws Exception  If there is a problem saving the file.
   */
    public void save(String fileName) throws Exception {
        File file = new File(fileName);
        DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(file));
        outputStream.writeBytes("x,y,Samples\n");
        int columns = getColumns();
        int rows = getRows();
        int[][] grid = getGrid();
        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                int samples = grid[y][x];
                outputStream.writeBytes(x + "," + y + "," + samples);
                outputStream.writeBytes("\n");
            }
        }
        outputStream.close();
    }
}
