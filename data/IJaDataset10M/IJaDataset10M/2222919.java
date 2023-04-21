package org.somprocessing.viz;

import java.io.*;
import java.util.Vector;
import java.util.StringTokenizer;
import org.somprocessing.nnet.Neuron;

/** 
 * A class to regularly space neurons of a Self-Organizing Map.
 * The object Umatrix computes a U-Matrix with use of computed 
 * neurons by SOM_PAK.
 *  
 * There are similarities of this model to the Grid object.
 *  
 * @author	Cedric Gabathuler
 * @version 0.2
 */
public class Umatrix {

    private double cellSize;

    private int dimension;

    private int xaxis;

    private int yaxis;

    private String topology;

    private String neighborh;

    private Vector<Neuron> neurons = new Vector<Neuron>();

    /**
	 * The grid values. In a Umatrix all the values separately out of the codebook file.
	 * An example is a 4 dimensional matrix with 6 neurons.
	 * 1,2,3,4 = neuron1
	 * 1,2,3,4 = neuron2
	 * 1,2,3,4 = neuron3
	 * 1,2,3,4 = neuron4
	 * 1,2,3,4 = neuron5
	 * 1,2,3,4 = neuron6
	 */
    private float[][] grid;

    /**
	 * Umatrix with the average of distances (hexagonal=6 neighbors, rectangular=4 neighbors)
	 * d(x,y)=sqrt((x1-x2)^2+(y1-y2)^2+...)
	 */
    private Grid gumat;

    /**
	 * Creates with a constructor a new instance of Umatrix.
	 */
    public Umatrix() {
        this.cellSize = 1;
        this.xaxis = 0;
        this.yaxis = 0;
        this.dimension = 0;
        this.topology = "none";
        this.neighborh = "none";
        this.grid = null;
        this.gumat = null;
    }

    /**
	 * Create with a constructor a special Grid with use of a Codebook file (SOM_PAK)
	 * and Umatrix as an object.
	 * 
	 * @param filePath Path where the file is located
	 * @throws IOException
	 */
    public Umatrix(String filePath) throws IOException {
        this.cellSize = 1;
        BufferedReader reader = null;
        try {
            File file = new File(filePath);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = reader.readLine();
            StringTokenizer tokenizer = new StringTokenizer(line, " ");
            this.dimension = Integer.parseInt(tokenizer.nextToken());
            this.topology = tokenizer.nextToken().trim().toLowerCase();
            this.xaxis = Integer.parseInt(tokenizer.nextToken());
            this.yaxis = Integer.parseInt(tokenizer.nextToken());
            this.neighborh = tokenizer.nextToken().trim().toLowerCase();
            if (this.xaxis <= 0 || this.yaxis <= 0) throw new java.io.IOException();
            this.grid = new float[(this.yaxis * this.xaxis)][this.dimension];
            int xachse = 0;
            int yachse = 0;
            for (int row = 0; row < (this.yaxis * this.yaxis); row++) {
                line = reader.readLine();
                tokenizer = new StringTokenizer(line, " ");
                float[] nvec = new float[dimension];
                if (xachse >= this.xaxis) {
                    xachse = 0;
                    yachse = yachse + 1;
                }
                for (int dimens = 0; dimens < dimension; dimens++) {
                    float v = Float.parseFloat(tokenizer.nextToken());
                    this.setValue(v, dimens, row);
                    nvec[dimens] = v;
                }
                if (tokenizer.hasMoreTokens()) {
                    String name = tokenizer.nextToken();
                    Neuron newunit = new Neuron(xachse, yachse, nvec, name);
                    this.neurons.add(newunit);
                } else {
                    Neuron newunit = new Neuron(xachse, yachse, nvec);
                    this.neurons.add(newunit);
                }
                xachse = xachse + 1;
            }
            String ttopol1 = "rect";
            String ttopol2 = "hexa";
            this.gumat = new Grid(this.xaxis, this.yaxis);
            if (this.topology.equals(ttopol1)) {
                for (int k = 0; k < this.neurons.size(); k++) {
                    double neurondist = 0;
                    int ncounter = 0;
                    Neuron testneuron = this.neurons.get(k);
                    int xval = testneuron.getXval();
                    int yval = testneuron.getYval();
                    for (int l = 0; l < this.neurons.size(); l++) {
                        Neuron testneuron2 = this.neurons.get(l);
                        int xval2 = testneuron2.getXval();
                        int yval2 = testneuron2.getYval();
                        if ((xval2 == (xval + 1)) && (yval2 == yval)) {
                            neurondist = testneuron.getEucDist(testneuron2);
                            ncounter++;
                        } else if ((xval == (xval2 + 1)) && (yval2 == yval)) {
                            neurondist = testneuron.getEucDist(testneuron2);
                            ncounter++;
                        } else if ((xval2 == xval) && (yval2 == (yval + 1))) {
                            neurondist = testneuron.getEucDist(testneuron2);
                            ncounter++;
                        } else if ((xval2 == xval) && (yval == (yval2 + 1))) {
                            neurondist = testneuron.getEucDist(testneuron2);
                            ncounter++;
                        }
                    }
                    neurondist = neurondist / ncounter;
                    gumat.setValue(neurondist, xval, yval);
                }
            }
            if (this.topology.equals(ttopol2)) {
                for (int k = 0; k < this.neurons.size(); k++) {
                    double neurondist = 0;
                    int ncounter = 0;
                    Neuron testneuron = this.neurons.get(k);
                    int xval = testneuron.getXval();
                    int yval = testneuron.getYval();
                    for (int l = 0; l < this.neurons.size(); l++) {
                        Neuron testneuron2 = this.neurons.get(l);
                        int xval2 = testneuron2.getXval();
                        int yval2 = testneuron2.getYval();
                        if (yval % 2 == 0) {
                            if ((xval2 == (xval + 1)) && (yval2 == yval)) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            } else if ((yval2 == (yval + 1)) && (xval2 == xval)) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            } else if ((yval2 == (yval + 1)) && (xval2 == (xval - 1))) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            } else if ((yval2 == yval) && (xval2 == (xval - 1))) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            } else if ((xval2 == xval) && (yval2 == (yval - 1))) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            } else if ((xval2 == (xval - 1)) && (yval2 == (yval - 1))) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            }
                        }
                        if (yval % 2 != 0) {
                            if ((xval2 == (xval + 1)) && (yval2 == yval)) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            } else if ((yval2 == (yval + 1)) && (xval2 == xval)) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            } else if ((yval2 == (yval + 1)) && (xval2 == (xval + 1))) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            } else if ((yval2 == yval) && (xval2 == (xval - 1))) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            } else if ((xval2 == xval) && (yval2 == (yval - 1))) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            } else if ((xval2 == (xval + 1)) && (yval2 == (yval - 1))) {
                                neurondist = testneuron.getEucDist(testneuron2);
                                ncounter++;
                            }
                        }
                    }
                    neurondist = neurondist / ncounter;
                    gumat.setValue(neurondist, xval, yval);
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
	 * A setter method to set a value in the grid.
	 * 
	 * @param value The value to store in the grid.
	 * @param col The column for which a value must be set.
	 * @param row The row for which a value must be set.
	 */
    public void setValue(float value, int col, int row) {
        grid[row][col] = value;
    }

    /**
	 * A setter method to set a value in the grid.
	 * 
	 * @param value The value to store in the grid. The value is cast to a float.
	 * @param col The column for which a value must be set.
	 * @param row The row for which a value must be set.
	 */
    public void setValue(double value, int col, int row) {
        grid[row][col] = (float) value;
    }

    /**
	 * A setter method for the cellsize of the umatrix
	 * 
	 * @param newcellsize The new cell size of the umatrix.
	 */
    public void setCellSize(double newcellsize) {
        this.cellSize = newcellsize;
    }

    /**
	 * A setter method for the dimensions of the umatrix
	 * 
	 * @param newdim The new dimensions of the umatrix.
	 */
    public void setDim(int newdim) {
        this.dimension = newdim;
    }

    /**
	 * A setter method for the X axis of the umatrix
	 * 
	 * @param newxaxis The new X axis of the umatrix.
	 */
    public void setXaxis(int newxaxis) {
        this.xaxis = newxaxis;
    }

    /**
	 * A setter method for the Y axis of the umatrix
	 * 
	 * @param newyaxis The new Y axis of the umatrix.
	 */
    public void setYaxis(int newyaxis) {
        this.yaxis = newyaxis;
    }

    /**
	 * A setter method for the topology of the umatrix
	 * 
	 * @param newtopol The new topology of the umatrix.
	 */
    public void setTopol(String newtopol) {
        this.topology = newtopol;
    }

    /**
	 * A setter method for the neighborhood of the umatrix
	 * 
	 * @param newneigh The new neighborhood of the umatrix.
	 */
    public void setNeighb(String newneigh) {
        this.neighborh = newneigh;
    }

    /**
	 * A setter method for the neurons of the umatrix
	 * 
	 * @param newneurons The new neurons of the umatrix.
	 */
    public void setNeurons(Vector<Neuron> newneurons) {
        this.neurons = newneurons;
    }

    /**
	 * A setter method for the grid of the umatrix
	 * 
	 * @param newgrid The new grid of the umatrix.
	 */
    public void setGrid(float[][] newgrid) {
        this.grid = newgrid;
    }

    /**
	 * A setter method for the grid umatrix of the umatrix
	 * 
	 * @param newgumat The new umatrix grid of the umatrix.
	 */
    public void setUmat(Grid newgumat) {
        this.gumat = newgumat;
    }

    /**
	 * A getter method to return the value at a specific position in the grid.
	 * 
	 * @param col The column for which a value must be returned.
	 * @param row The row for which a value must be returned.
	 * @return Returns the value at the specified position.
	 */
    public float getValue(int col, int row) {
        return grid[row][col];
    }

    /**
	 * A getter method to return the number of columns in the grid.
	 * 
	 * @return The number of columns in the grid.
	 */
    public int getCols() {
        return grid[0].length;
    }

    /**
	 * A getter method to return the number of rows in the grid.
	 * 
	 * @return The number of rows in the grid.
	 */
    public int getRows() {
        return grid.length;
    }

    /**
	 * A getter method to return the whole grid
	 * 
	 * @return The grid with all values in an array
	 */
    public float[][] getGrid() {
        return this.grid;
    }

    /**
	 * A getter method to returns the minimum and the maximum value in the grid.
	 * 
	 * @return Returns an array with two elements. The first element is 
	 * the minimum value in the grid, the second value is the
	 * maximum value in the grid.
	 */
    public float[] getMinMax() {
        final int cols = this.getCols();
        final int rows = this.getRows();
        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;
        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < cols; ++c) {
                if (grid[r][c] < min) min = grid[r][c];
                if (grid[r][c] > max) max = grid[r][c];
            }
        }
        return new float[] { min, max };
    }

    /**
	 * A getter method to return the distance between two neighboring rows or columns.
	 * 
	 * @return The distance between two rows or columns.
	 */
    public double getCellSize() {
        return cellSize;
    }

    /**
	 * A getter method to return the neurons in a vector
	 * 
	 * @return The neurons used for the umatrix.
	 */
    public Vector<Neuron> getNeurons() {
        return this.neurons;
    }

    /**
	 * A getter method to return the xaxis in the umatrix.
	 * 
	 * @return The number of columns/xaxis in the umatrix.
	 */
    public int getXaxis() {
        return this.xaxis;
    }

    /**
	 * A getter method to return the yaxis in the umatrix.
	 * 
	 * @return The number of rows/yaxis in the umatrix.
	 */
    public int getYaxis() {
        return this.yaxis;
    }

    /**
	 * A getter method to return the dimension in the umatrix.
	 * 
	 * @return The used dimensions for the umatrix
	 */
    public int getDim() {
        return this.dimension;
    }

    /**
	 * A getter method to return the topology of the umatrix.
	 * 
	 * @return The used topology for the umatrix
	 */
    public String getTopol() {
        return this.topology;
    }

    /**
	 * A getter method to return the neighborhood of the umatrix.
	 * 
	 * @return The used neighborhood function for the umatrix
	 */
    public String getNeighb() {
        return this.neighborh;
    }

    /**
	 * A getter method to return the dem of a umatrix.
	 * 
	 * @return The used averaged distance grid of a umatrix
	 */
    public Grid getGumat() {
        return this.gumat;
    }
}
