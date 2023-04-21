package mil.army.usace.ehlschlaeger.rgik.core;

import java.awt.Graphics;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import mil.army.usace.ehlschlaeger.rgik.io.ESRI_ASCII;
import mil.army.usace.ehlschlaeger.rgik.util.BooleanGrid;
import mil.army.usace.ehlschlaeger.rgik.util.MyReader;

/**
 * Defines a rectangular area, subdivided by lines into smaller blocks. Values
 * represent the intersection of lattice lines, not the cells. This class just
 * defines the management of a grid; storage of cell values is handled by
 * sub-classes.
 * <p>
 * Copyright <a href="http://faculty.wiu.edu/CR-Ehlschlaeger2/">Charles R.
 * Ehlschlaeger</a>, work: 309-298-1841, fax: 309-298-3003, This software is
 * freely usable for research and educational purposes. Contact C. R.
 * Ehlschlaeger for permission for other purposes. Use of this software requires
 * appropriate citation in all published and unpublished documentation.
 */
public class GISGrid extends GISData implements Serializable {

    /** Flag for GRASS v.4 ASCII files. */
    public static final int GRASS4 = 1;

    /** Flag for ESRI ASCII files. */
    public static final int ESRI = 2;

    /** Smallest value that we won't consider == zero. */
    public static final double EPSILON = 0.0000001;

    protected double NSRes, EWRes;

    protected int numCols, numRows;

    protected BooleanGrid noData;

    protected int projection, projectionZone;

    /**
     * Determine if another object defines the same grid as this one.
     * 
     * @param otherGrid
     *            the other object to test
     * @return true if the grid is the same (bounds, resolution, and
     *         subdivisions); false if not.
     */
    public boolean equalsGrid(GISGrid otherGrid) {
        boolean equ = equalsBounds(otherGrid);
        if (equ) {
            equ = (NSRes == otherGrid.NSRes && EWRes == otherGrid.EWRes && numCols == otherGrid.numCols && numRows == otherGrid.numRows);
        }
        return equ;
    }

    /**
     * Load a grid from a GRASS v.4 ASCII file.
     * File's name must end with ".gra".
     * GRASS 5.0 ASCII files are not supported.
     * 
     * @param filename path and name of file to load.  ".gra" will be
     *     appended to the end if missing.
     * @return a new GISGrid holding the contents of the file
     * @throws IOException on any file error
     */
    public static GISGrid loadGRASS4(String filename) throws IOException {
        MyReader fr = new MyReader(grassFileName(filename));
        String s;
        s = fr.readLine();
        int startIndex = nextNumber(s, 4);
        s = s.substring(startIndex);
        double north = Double.parseDouble(s.trim());
        s = fr.readLine();
        startIndex = nextNumber(s, 4);
        s = s.substring(startIndex);
        double south = Double.parseDouble(s.trim());
        s = fr.readLine();
        startIndex = nextNumber(s, 8);
        s = s.substring(startIndex);
        double east = Double.parseDouble(s.trim());
        s = fr.readLine();
        startIndex = nextNumber(s, 8);
        s = s.substring(startIndex);
        double west = Double.parseDouble(s.trim());
        s = fr.readLine();
        startIndex = nextNumber(s, 7);
        s = s.substring(startIndex);
        int numrow = Integer.parseInt(s.trim());
        s = fr.readLine();
        startIndex = nextNumber(s, 7);
        s = s.substring(startIndex);
        int numcol = Integer.parseInt(s.trim());
        fr.close();
        double nsres = ((north - south) / numrow);
        double ewres = ((east - west) / numcol);
        GISGrid grid = new GISGrid(west, north, ewres, nsres, numrow, numcol);
        return grid;
    }

    /**
     * Create instance.
	 * 
	 * @param westEdge location of western edge of bounds
	 * @param northEdge location of northern edge of bounds
	 * @param EWResolution width of each cell
	 * @param NSResolution height of each cell
	 * @param numberRows number of cells (NOT lines) tall
	 * @param numberColumns number of cells (NOT lines) wide
	 */
    public GISGrid(double westEdge, double northEdge, double EWResolution, double NSResolution, int numberRows, int numberColumns) {
        super(westEdge, northEdge, westEdge + EWResolution * numberColumns, northEdge - NSResolution * numberRows);
        EWRes = EWResolution;
        NSRes = NSResolution;
        numRows = numberRows;
        numCols = numberColumns;
        noData = new BooleanGrid(numRows + 1, numCols + 1, true);
    }

    /**
     * Create instance, copying size and resolution (but not data) from another
     * grid.
     * 
     * @param existingGrid
     *            grid to copy parameters from
     */
    public GISGrid(GISGrid existingGrid) {
        this(existingGrid.getWestEdge(), existingGrid.getNorthEdge(), existingGrid.getEWResolution(), existingGrid.getNSResolution(), existingGrid.getNumberRows(), existingGrid.getNumberColumns());
        noData = new BooleanGrid(numRows, numCols, true);
    }

    /** in alpha testing */
    public void copyMetaData(GISGrid grid) {
        setWestEdge(grid.getWestEdge());
        setNorthEdge(grid.getNorthEdge());
        setEastEdge(grid.getEastEdge());
        setSouthEdge(grid.getSouthEdge());
        setEWResolution(grid.getEWResolution());
        setNSResolution(grid.getNSResolution());
        setNumberRows(grid.getNumberRows());
        setNumberColumns(grid.getNumberColumns());
    }

    /** @param rowA is the row of the first cell,
	 *  @param colA is the column of the first cell,
	 *  @param rowB is the row of the second cell,
	 *  @param colB is the column of the second cell. */
    public double distance(int rowA, int colA, int rowB, int colB) {
        return ((double) Math.sqrt((double) ((colA - colB) * EWRes * (colA - colB) * EWRes + (rowA - rowB) * NSRes * (rowA - rowB) * NSRes)));
    }

    /** in alpha testing */
    protected static String grassFileName(String fileName) {
        if (!fileName.toLowerCase().endsWith(".gra")) fileName += ".gra";
        return fileName;
    }

    /**
     * Determine the smallest rectangle that surrounds all of the valid cells.
     * Returns a new GISGrid with bounds and subdivisions matching the area of
     * this grid marked as valid, though with none of the flags copied over.
     * 
     * @return a new GISGrid with bounds and subdivisions matching the valid
     *         portion of this grid, or null if the whole grid is no-data.
     */
    public GISGrid getBoundingGrid() {
        int lowRow = -1;
        boolean done = false;
        while (done == false && lowRow < getNumberRows()) {
            lowRow++;
            for (int c = getNumberColumns() - 1; c >= 0; c--) {
                if (isNoData(lowRow, c) == false) {
                    done = true;
                    c = -1;
                }
            }
        }
        if (lowRow == getNumberRows()) return null;
        int hihRow = getNumberRows();
        done = false;
        while (done == false) {
            hihRow--;
            for (int c = getNumberColumns() - 1; c >= 0; c--) {
                if (isNoData(hihRow, c) == false) {
                    done = true;
                    c = -1;
                }
            }
        }
        int lowCol = -1;
        done = false;
        while (done == false) {
            lowCol++;
            for (int r = hihRow; r >= lowRow; r--) {
                if (isNoData(r, lowCol) == false) {
                    done = true;
                    r = -1;
                }
            }
        }
        int hihCol = getNumberColumns();
        done = false;
        while (done == false) {
            hihCol--;
            for (int r = hihRow; r >= lowRow; r--) {
                if (isNoData(r, hihCol) == false) {
                    done = true;
                    r = -1;
                }
            }
        }
        double newWestEdge = getWestEdge() + lowCol * getEWResolution();
        double newNorthEdge = getNorthEdge() - lowRow * getNSResolution();
        int newRows = hihRow - lowRow + 1;
        int newCols = hihCol - lowCol + 1;
        GISGrid newGrid = new GISGrid(newWestEdge, newNorthEdge, getEWResolution(), getNSResolution(), newRows, newCols);
        return newGrid;
    }

    /** in alpha testing */
    public double getCellCenterEasting(int cellRow, int cellColumn) {
        return (getWestEdge() + (cellColumn + (double) 0.5) * getEWResolution());
    }

    /** in alpha testing */
    public double getCellCenterNorthing(int cellRow, int cellColumn) {
        double value = getNorthEdge() - (cellRow + (double) 0.5) * getNSResolution();
        return (value);
    }

    /**
     * Find the closest column number on or west of a point.
     * 
     * @param easting   east-west location of point
     * @param northing  north-south location of point
     * @return int index of data column for given location
     */
    public int getColumnIndex(double easting, double northing) {
        return ((int) ((easting - getWestEdge()) / getEWResolution()));
    }

    /** in alpha testing */
    public double getEWResolution() {
        return (EWRes);
    }

    /** in alpha testing */
    public double getNSResolution() {
        return (NSRes);
    }

    /**
	 * @return number of cells (NOT lines) wide
	 */
    public int getNumberColumns() {
        return (numCols);
    }

    /**
	 * @return number of cells (NOT lines) tall
	 */
    public int getNumberRows() {
        return (numRows);
    }

    public int getProjection() {
        return projection;
    }

    public int getProjectionZone() {
        return projectionZone;
    }

    /**
	 * Find the closest row number on or south of a point.
	 * Northern edge is row 0.
	 * 
	 * @param easting   east-west location of point
	 * @param northing  north-south location of point
	 * @return int index of data row for given location
	 */
    public int getRowIndex(double easting, double northing) {
        return ((int) ((getNorthEdge() - northing) / getNSResolution()));
    }

    public int getExactRowIndex(double northing) {
        double row = (getNorthEdge() - northing) / getNSResolution();
        if (Math.abs(Math.round(row) - row) > EPSILON) throw new DataException("Northing value " + northing + " does not lie on a lattice line");
        return (int) Math.round(row);
    }

    public int getExactColumnIndex(double easting) {
        double col = (easting - getWestEdge()) / getEWResolution();
        if (Math.abs(Math.round(col) - col) > EPSILON) throw new DataException("Easting value " + easting + " does not lie on a lattice line");
        return (int) Math.round(col);
    }

    /**
	 * Determine if a cell contains a value.
	 * 
     * @param row  y-index to test
     * @param col  x-index to test
	 * @return true if cell is undefined, false if it contains a value.
	 */
    public boolean isNoData(int row, int col) {
        if (onMap(row, col) == false) return true;
        return (noData.getBoolean(row, col));
    }

    /**
     * Determine if the cell that represents a certain geographic point
     * contains a value.
     * 
     * @param easting   east-west location of point
     * @param northing  north-south location of point
     * @return true if cell is undefined, false if it contains a value.
     */
    public boolean isNoData(double easting, double northing) {
        int row = getRowIndex(easting, northing);
        int col = getColumnIndex(easting, northing);
        return (isNoData(row, col));
    }

    /**
     * GISGrid does not support realizations.
     * @return false
     */
    public boolean isRealizable() {
        return false;
    }

    /**
     * GISGrid does not support realizations.
     * @throws RuntimeException always
     */
    public void makeRealizations() {
        throw new RuntimeException("Not implemented.");
    }

    /**
     * this method generates SaTScan grid files. Since SaTScan requires the grd
     * extension, this method adds .grd to the end of the filename string.
     * 
     * @throws IOException
     */
    public void writeSaTScanGridFile(String filename) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename + ".grd")));
        for (int c = 0; c < getNumberColumns(); c++) {
            for (int r = getNumberRows() - 1; r >= 0; r--) {
                double x = getCellCenterEasting(r, c);
                double y = getCellCenterNorthing(r, c);
                out.println(x + " " + y);
            }
        }
        out.close();
    }

    /**
     * Determine if given row and column is within the bounds of our map.
     * 
     * @param row  y-index to test
     * @param col  x-index to test
     * @return true if we have data for given cell; false if not.
     */
    public boolean onMap(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return (false);
        }
        return (true);
    }

    public void paint(Graphics g) {
        throw new RuntimeException("Not implemented.");
    }

    /**
	 *  this method prints the metadata information to System.out
	 */
    public void print() {
        System.out.print("w: " + getWestEdge() + ", e: " + getEastEdge() + ", s: " + getSouthEdge() + ", n: " + getNorthEdge());
        System.out.print(", rows: " + getNumberRows() + ", cols: " + getNumberColumns());
        System.out.println(", NS res: " + getNSResolution() + ", EW res: " + getEWResolution());
    }

    /** this method prints the metadata information to System.out
	 */
    public void printGrid() {
        System.out.print("w: " + getWestEdge() + ", e: " + getEastEdge() + ", s: " + getSouthEdge() + ", n: " + getNorthEdge());
        System.out.print(", rows: " + getNumberRows() + ", cols: " + getNumberColumns());
        System.out.println(", NS res: " + getNSResolution() + ", EW res: " + getEWResolution());
    }

    protected void printGrassHeader(PrintWriter out) {
        out.println("north:        " + getNorthEdge());
        out.println("south:        " + getSouthEdge());
        out.println("east:         " + getEastEdge());
        out.println("west:         " + getWestEdge());
        out.println("rows:         " + getNumberRows());
        out.println("cols:         " + getNumberColumns());
    }

    /** segmentGrid( *) has never been tested
	 */
    public GISGrid[] segmentGrid(int rowsColumnsPerSegment) {
        return segmentGrid(rowsColumnsPerSegment, rowsColumnsPerSegment);
    }

    public GISGrid[] segmentGrid(int rowsPerSegment, int columnsPerSegment) {
        return segmentGrid(rowsPerSegment, columnsPerSegment, 0);
    }

    /**
	 * Create a 2-D array of sub-grids, each of which covers a piece of this grid.
	 * 
	 * @param rowsPerSegment number of rows desired in sub-grids.  Some grids may
	 *     be smaller if they are on an edge. 
	 * @param columnsPerSegment number of columns desired in sub-grids.  Some grids may
     *     be smaller if they are on an edge.
	 * @param cellOverLap number of rows and columns by which sub-grids will overlap.
	 * @return 2D grid of sub-grids as a linear array, organized rows first.
	 */
    public GISGrid[] segmentGrid(int rowsPerSegment, int columnsPerSegment, int cellOverLap) {
        int segmentRows = (int) Math.ceil(1.0 * getNumberRows() / rowsPerSegment);
        int segmentColumns = (int) Math.ceil(1.0 * getNumberColumns() / columnsPerSegment);
        int numberSegments = segmentRows * segmentColumns;
        GISGrid[] segments = new GISGrid[numberSegments];
        int doneSegments = 0;
        for (int r = 0; r < segmentRows; r++) {
            int minRow = (int) Math.max(0, r * rowsPerSegment - cellOverLap);
            int maxRow = (int) Math.min(getNumberRows() - 1, (r + 1) * rowsPerSegment + cellOverLap - 1);
            double northEdge = getNorthEdge() - minRow * getNSResolution();
            for (int c = 0; c < segmentColumns; c++) {
                int minColumn = (int) Math.max(0, c * columnsPerSegment - cellOverLap);
                int maxColumn = (int) Math.min(getNumberColumns() - 1, (c + 1) * columnsPerSegment + cellOverLap - 1);
                double westEdge = getWestEdge() + minColumn * getEWResolution();
                segments[doneSegments] = new GISGrid(westEdge, northEdge, getEWResolution(), getNSResolution(), maxRow - minRow + 1, maxColumn - minColumn + 1);
                doneSegments++;
            }
        }
        return segments;
    }

    /**
	 * Mark a cell as valid or undefined.
	 * 
	 * @param easting   easting value of a point within cell
	 * @param northing  northing value of a point within cell
	 * @param value true if cell value is valid, or false if cell is
	 *     undefined (or "empty" or "no-data").
	 */
    public void setIsData(double easting, double northing, boolean value) {
        int row = (int) ((getNorthEdge() - northing) / NSRes);
        int col = (int) ((easting - getWestEdge()) / EWRes);
        noData.setBoolean(row, col, !value);
    }

    /**
     * Mark a cell as valid or undefined.
     * 
     * @param row  row number of cell
     * @param col  column number of cell
     * @param value true if cell value is valid, or false if cell is
     *     undefined (or "empty" or "no-data").
     */
    public void setIsData(int row, int col, boolean value) {
        noData.setBoolean(row, col, !value);
    }

    /** this method does NOT modify other GISGrid parameters. */
    public void setEWResolution(double EWResolution) {
        EWRes = EWResolution;
    }

    /**
     * Returns true if our data came from a source that specified a value for
     * no-data cells. The value is not generally useful to GISGrid, as we
     * maintain a separate array of bits to track this. But it can be useful to
     * classes that want to regenerate the file.
     * <P>
     * Note there is no "getNoDataValue()" method here, as the return type
     * depends on the type of data stored.
     * 
     * @return true if we have a no-data value.
     */
    public boolean hasNoDataValue() {
        return false;
    }

    /**
	 * @return 'true' if any cell anywhere is set to no-data
	 */
    public boolean hasAnyNoData() {
        return noData.cardinality() > 0;
    }

    /**
     * Mark a cell as invalid or undefined.
     * This is the opposite of setData.
     * 
     * @param easting   easting value of a point within cell
     * @param northing  northing value of a point within cell
     * @param value false if cell value is valid, or true if cell is
     *     undefined (or "empty" or "no-data").
     */
    public void setNoData(double easting, double northing, boolean value) {
        int row = (int) ((getNorthEdge() - northing) / NSRes);
        int col = (int) ((easting - getWestEdge()) / EWRes);
        noData.setBoolean(row, col, value);
    }

    /**
     * Mark a cell as invalid or undefined.
     * This is the opposite of setData.
     * 
     * @param row  row number of cell
     * @param col  column number of cell
     * @param value false if cell value is valid, or true if cell is
     *     undefined (or "empty" or "no-data").
     */
    public void setNoData(int row, int col, boolean value) {
        noData.setBoolean(row, col, value);
    }

    /** this method does NOT modify other GISGrid parameters. */
    public void setNSResolution(double NSResolution) {
        NSRes = NSResolution;
    }

    /** in alpha testing.
	 *  this method does NOT modify other GISGrid parameters. */
    public void setNumberColumns(int columns) {
        numCols = columns;
        noData = new BooleanGrid(numRows, numCols, true);
    }

    /** in alpha testing.
	 *  this method does NOT modify other GISGrid parameters. */
    public void setNumberRows(int rows) {
        numRows = rows;
        noData = new BooleanGrid(numRows, numCols, true);
    }

    public void setProjection(int value) {
        projection = value;
    }

    public void setProjectionZone(int value) {
        projectionZone = value;
    }

    /**
     * Load the grid header from an ESRI ASCII file.
     * Grid values will NOT be read, only the header.
     * 
     * @param file  path and name of file to load
     * @return a new GISGrid holding the header of the file
     * @throws IOException on any file error
     */
    public static GISGrid loadEsriAscii(String filename) throws IOException {
        File file = ESRI_ASCII.findFile(filename);
        return loadEsriAscii(file);
    }

    /**
     * Load the grid header from an ESRI ASCII file.
     * File's name must end with ".asc".
     * Grid values will NOT be read, only the header.
     * 
     * @param file  path and name of file to load
     * @return a new GISGrid holding the contents of the file
     * @throws IOException on any file error
     */
    public static GISGrid loadEsriAscii(File file) throws IOException {
        MyReader fr = new MyReader(file);
        try {
            return loadEsriAscii(fr);
        } finally {
            fr.close();
        }
    }

    /**
     * Load the grid header from an opened reader.
     * Reader will be left at the line following "cellsize".
     * Grid values will NOT be read, only the header.
     * 
     * @param fr  reader, already open and positioned at the header
     * @return a new GISGrid holding the contents of the header
     * @throws IOException on any file error
     */
    public static GISGrid loadEsriAscii(MyReader fr) throws IOException {
        int startIndex = 0;
        String k = null;
        String s = null;
        k = "ncols";
        s = fr.readLine().trim();
        if (!s.startsWith(k)) throw new DataException(String.format("Bad map file: expecting \"%s <number>\", found \"%s\"", k, s));
        startIndex = RGISData.nextNumber(s, k.length());
        int numcol = Integer.parseInt(s.substring(startIndex).trim());
        k = "nrows";
        s = fr.readLine().trim();
        if (!s.startsWith(k)) throw new DataException(String.format("Bad map file: expecting \"%s <number>\", found \"%s\"", k, s));
        startIndex = RGISData.nextNumber(s, k.length());
        int numrow = Integer.parseInt(s.substring(startIndex).trim());
        k = "xllcorner";
        s = fr.readLine().trim();
        if (!s.startsWith(k)) throw new DataException(String.format("Bad map file: expecting \"%s <number>\", found \"%s\"", k, s));
        startIndex = RGISData.nextNumber(s, k.length());
        double west = Double.parseDouble(s.substring(startIndex).trim());
        k = "yllcorner";
        s = fr.readLine().trim();
        if (!s.startsWith(k)) throw new DataException(String.format("Bad map file: expecting \"%s <number>\", found \"%s\"", k, s));
        startIndex = RGISData.nextNumber(s, k.length());
        double south = Double.parseDouble(s.substring(startIndex).trim());
        k = "cellsize";
        s = fr.readLine().trim();
        if (!s.startsWith(k)) throw new DataException(String.format("Bad map file: expecting \"%s <number>\", found \"%s\"", k, s));
        startIndex = RGISData.nextNumber(s, k.length());
        double res = Double.parseDouble(s.substring(startIndex).trim());
        double north = south + res * numrow;
        GISGrid grid = new GISGrid(west, north, res, res, numrow, numcol);
        return grid;
    }

    /** in alpha testing 
	 * @throws IOException */
    public void writeAsciiEsri(String fileName) throws IOException {
        if (getEWResolution() != getNSResolution()) {
            throw new DataException("writeAsciiEsri does not support unequal res");
        }
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(ESRI_ASCII.findFile(fileName))));
        out.println("ncols         " + getNumberColumns());
        out.println("nrows         " + getNumberRows());
        out.println("xllcorner     " + (double) getWestEdge());
        out.println("yllcorner     " + (double) getSouthEdge());
        out.println("cellsize      " + (double) getEWResolution());
        out.close();
    }
}
