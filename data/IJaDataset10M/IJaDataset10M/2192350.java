package App_Test;

import java.util.ArrayList;

/**
 * Cette classe 
*/
public class Matrix {

    public Matrix(int sizeGrid, double source, int positionSourceX, int positionSourceY, int analysePX, int analysePY) {
        this.sizeGrid = sizeGrid;
        this.positionSourceX = positionSourceX;
        this.positionSourceY = positionSourceY;
        this.analysePX = analysePX;
        this.analysePY = analysePY;
        matrix = new double[4][sizeGrid][sizeGrid];
        loadSourceMatrix(source);
        showMatrix(matrix);
    }

    public void loadSourceMatrix(double source) {
        for (int pix = 0; pix < 4; pix++) {
            for (int x = 0; x < sizeGrid; x++) {
                for (int y = 0; y < sizeGrid; y++) {
                    if (firstIt) {
                        matrix[pix][x][y] = 0.0;
                        firstIt = false;
                    }
                    if (x == positionSourceX && y == positionSourceY) matrix[pix][positionSourceX][positionSourceY] = source;
                }
            }
        }
    }

    public double[][][] computeNextStep() {
        AlgoPixelFlow.init(sizeGrid, 1, positionSourceX, positionSourceY);
        AlgoPixelFlow.setMatrix(matrix);
        matrix = AlgoPixelFlow.getNextStepMatrix();
        return matrix;
    }

    public void showMatrix(double[][][] matrix) {
        flowAll.clear();
        System.out.println("\n-------------------------------------------------------------------------");
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (i <= analysePY + 1 & i >= analysePY - 1 & j <= analysePX + 1 & j >= analysePX - 1) {
                    if (!arrayFlowsIsCreate) {
                        flowTop = new ArrayList<Double>();
                        flowTop.add(Double.valueOf(i));
                        flowTop.add(Double.valueOf(j));
                        flowTop.add(matrix[0][i][j]);
                        flowAllTop.add(flowTop);
                    } else {
                        for (int cell = 0; cell < flowAllTop.size(); cell++) {
                            double convertionNumCellI = (Double) flowAllTop.get(cell).get(0);
                            int numCellI = (int) convertionNumCellI;
                            double convertionNumCellJ = (Double) flowAllTop.get(cell).get(1);
                            int numCellJ = (int) convertionNumCellJ;
                            if (i == numCellI & j == numCellJ) {
                                flowAllTop.get(cell).add(roundDouble((double) matrix[0][i][j]));
                            }
                        }
                    }
                }
                System.out.print("   " + matrix[0][i][j] + "\t \t");
            }
            System.out.println();
            for (int j = 0; j < matrix[0].length; j++) {
                if (i <= analysePY + 1 & i >= analysePY - 1 & j <= analysePX + 1 & j >= analysePX - 1) {
                    if (!arrayFlowsIsCreate) {
                        flowLeft = new ArrayList<Double>();
                        flowLeft.add(Double.valueOf(i));
                        flowLeft.add(Double.valueOf(j));
                        flowLeft.add(matrix[2][i][j]);
                        flowAllLeft.add(flowLeft);
                        flowRight = new ArrayList<Double>();
                        flowRight.add(Double.valueOf(i));
                        flowRight.add(Double.valueOf(j));
                        flowRight.add(matrix[3][i][j]);
                        flowAllRight.add(flowRight);
                    } else {
                        for (int cell = 0; cell < flowAllLeft.size(); cell++) {
                            double convertionNumCellI = (Double) flowAllLeft.get(cell).get(0);
                            int numCellI = (int) convertionNumCellI;
                            double convertionNumCellJ = (Double) flowAllLeft.get(cell).get(1);
                            int numCellJ = (int) convertionNumCellJ;
                            if (i == numCellI & j == numCellJ) {
                                flowAllLeft.get(cell).add(roundDouble((double) matrix[2][i][j]));
                                flowAllRight.get(cell).add(roundDouble((double) matrix[3][i][j]));
                            }
                        }
                    }
                }
                System.out.print(matrix[2][i][j] + "   " + matrix[3][i][j] + "\t");
            }
            System.out.println();
            for (int j = 0; j < matrix[0].length; j++) {
                if (i <= analysePY + 1 & i >= analysePY - 1 & j <= analysePX + 1 & j >= analysePX - 1) {
                    if (!arrayFlowsIsCreate) {
                        flowBottom = new ArrayList<Double>();
                        flowBottom.add(Double.valueOf(i));
                        flowBottom.add(Double.valueOf(j));
                        flowBottom.add(matrix[1][i][j]);
                        flowAllBottom.add(flowBottom);
                    } else {
                        for (int cell = 0; cell < flowAllBottom.size(); cell++) {
                            double convertionNumCellI = (Double) flowAllBottom.get(cell).get(0);
                            int numCellI = (int) convertionNumCellI;
                            double convertionNumCellJ = (Double) flowAllBottom.get(cell).get(1);
                            int numCellJ = (int) convertionNumCellJ;
                            if (i == numCellI & j == numCellJ) {
                                flowAllBottom.get(cell).add(roundDouble((double) matrix[1][i][j]));
                            }
                        }
                    }
                }
                System.out.print("   " + matrix[1][i][j] + "\t \t");
            }
            System.out.println("\n-------------------------------------------------------------------------");
        }
        flowAll.add(flowAllTop);
        flowAll.add(flowAllLeft);
        flowAll.add(flowAllRight);
        flowAll.add(flowAllBottom);
        arrayFlowsIsCreate = true;
    }

    public double roundDouble(double nombre) {
        double xNombreVirgule = Math.pow(10, 5);
        return (Math.round(nombre * xNombreVirgule) / xNombreVirgule);
    }

    private boolean arrayFlowsIsCreate = false;

    private ArrayList<ArrayList> flowAll = new ArrayList<ArrayList>();

    public ArrayList<ArrayList> getFlowAll() {
        return flowAll;
    }

    private ArrayList<ArrayList> flowAllTop = new ArrayList<ArrayList>();

    private ArrayList<ArrayList> flowAllLeft = new ArrayList<ArrayList>();

    private ArrayList<ArrayList> flowAllRight = new ArrayList<ArrayList>();

    private ArrayList<ArrayList> flowAllBottom = new ArrayList<ArrayList>();

    private ArrayList<Double> flowTop = new ArrayList<Double>();

    private ArrayList<Double> flowLeft;

    private ArrayList<Double> flowRight;

    private ArrayList<Double> flowBottom;

    private boolean firstIt = true;

    private int sizeGrid;

    private int positionSourceX;

    private int positionSourceY;

    private double matrix[][][];

    public double[][][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][][] matrix) {
        this.matrix = matrix;
    }

    private int analysePX;

    private int analysePY;

    public void setanalysePX(int analysePX) {
        this.analysePX = analysePX;
    }

    public void setanalysePY(int analysePY) {
        this.analysePY = analysePY;
    }
}
