package gca.matrixCA;

import gca.matrixCA.function.FunctionCA;
import gca.matrixCA.matrix.Matrix;

public class Grid {

    protected float[][] grid;

    int width, height;

    FunctionCA func;

    Matrix matr;

    public Grid(int w, int h, FunctionCA f, Matrix m) {
        grid = new float[width = w][height = h];
        func = f;
        matr = m;
    }

    public int getGridWidth() {
        return width;
    }

    public int getGridHeight() {
        return height;
    }

    public float getGrid(int right, int down) {
        return grid[right][down];
    }

    public void setGrid(int right, int down, float val) {
        grid[right][down] = val;
    }

    public void reset() {
        grid = new float[width][height];
    }

    public void setFunction(FunctionCA inFunction) {
        func = inFunction;
        for (int j = 0; j < height; j++) for (int i = 0; i < width; i++) grid[i][j] = func.computeResult(grid[i][j]);
    }

    public void setMatrix(Matrix inMatrix) {
        matr = inMatrix;
    }

    public void step() {
        int i, j;
        float[][] tgrid = new float[width][height];
        for (j = 0; j < height; j++) for (i = 0; i < width; i++) tgrid[i][j] = func.computeResult(matr.matrixCalc(i, j, grid));
        grid = tgrid;
    }
}
