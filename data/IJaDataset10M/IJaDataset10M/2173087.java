package edu.georgetown.nnj.data.layout;

import java.awt.Rectangle;

/**
 *
 * @author Kentaroh Takagaki
 */
public final class NNJDataLayoutNull extends NNJAbstractDataLayout {

    public static final NNJDataLayout INSTANCE = new NNJDataLayoutNull();

    @Override
    public NNJDataLayout getInstance() {
        return INSTANCE;
    }

    private static String NAME = "NNJDataLayoutNull";

    @Override
    public String getName() {
        return NAME;
    }

    private NNJDataLayoutNull() {
    }

    @Override
    public int getDetectorCount() {
        return 1;
    }

    @Override
    public int getChannelCount() {
        return 2;
    }

    private static int DET_FIELD_X = 0;

    private static int DET_FIELD_Y = 0;

    private static int DET_FIELD_WIDTH = 101;

    private static int DET_FIELD_HEIGHT = 101;

    private static Rectangle DET_FIELD = new Rectangle(DET_FIELD_X, DET_FIELD_X, DET_FIELD_WIDTH, DET_FIELD_HEIGHT);

    public Rectangle getDetectorField() {
        return DET_FIELD;
    }

    public int getDetectorFieldX() {
        return DET_FIELD_X;
    }

    public int getDetectorFieldY() {
        return DET_FIELD_Y;
    }

    public int getDetectorFieldWidth() {
        return DET_FIELD_WIDTH;
    }

    public int getDetectorFieldHeight() {
        return DET_FIELD_HEIGHT;
    }

    public double getDetectorFieldAspectRatio() {
        return 1d;
    }

    @Override
    public boolean isEdge(int det, int n) {
        return true;
    }

    private static int[][] DET_COORDS = { { 50, 50 } };

    @Override
    public int[] getDetectorCoordinates(int detector) {
        return DET_COORDS[detector];
    }

    public int getDetectorX(int detector) {
        return 0;
    }

    public int getDetectorY(int detector) {
        return 0;
    }

    @Override
    public int getCoordinateDetector(int x, int y) {
        return 0;
    }

    @Override
    public int getDetectorRadius() {
        return 50;
    }

    @Override
    public int[][] frameToMatrix(int[] frame) {
        int[][] tempret = new int[1][1];
        tempret[0][0] = frame[0];
        return tempret;
    }

    @Override
    public int[] matrixToFrame(int[][] matrix) {
        int[] tempret = { matrix[0][0] };
        return tempret;
    }

    public int matrixToDetector(int matrix0, int matrix1) {
        return -1;
    }

    public int[] detectorToMatrix(int detector) {
        return null;
    }

    @Override
    public int getMaxRing() {
        return 1;
    }

    @Override
    public int getDirectionCount(int n) {
        return 1;
    }

    @Override
    public int getNeighbor(int det, int direction, int n) {
        return -1;
    }

    @Override
    public int getNeighborDirection(int det, int neighbor, int n) {
        return -1;
    }

    public static int[] INTVECTOR = { -1, -1 };

    public static int[][] INTMATRIX = { INTVECTOR, INTVECTOR };

    public static double[] DOUBLEVECTOR = { Double.NaN, Double.NaN };

    public static double[][] DOUBLEMATRIX = { DOUBLEVECTOR, DOUBLEVECTOR };

    @Override
    public int[] getNeighbors(int det, int n) {
        return INTVECTOR;
    }

    @Override
    public double[] getNeighborVector(int direction, int ring) {
        return DOUBLEVECTOR;
    }

    @Override
    public double[][] getNeighborVectors(int ring) {
        return DOUBLEMATRIX;
    }

    @Override
    public double[] getNeighborVectorFlowExtension(int direction, int ring) {
        return DOUBLEVECTOR;
    }

    @Override
    public double[][] getNeighborVectorsFlowExtension(int ring) {
        return DOUBLEMATRIX;
    }

    @Override
    public int[][] getNeighborCombinationsFlowExtension(int det, int ring) {
        return INTMATRIX;
    }
}
