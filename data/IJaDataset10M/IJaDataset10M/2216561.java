package edu.mit.aero.foamcut;

import java.util.Arrays;

/**
 * Create a sequence of step and direction commands from a Shape.
 * @author mschafer
 */
public class Stepper {

    /** This bit causes the x axis to step. */
    public static final byte X_STEP = 0x01;

    /** This bit sets the x step direction. */
    public static final byte X_DIR = 0x02;

    /** This bit causes the y axis to step. */
    public static final byte Y_STEP = 0x04;

    /** This bit sets the y step direction. */
    public static final byte Y_DIR = 0x08;

    /** The amount to grow the arrays by during step generation. */
    private static final int CHUNK_SIZE = 50000;

    /** Size of a single step in the x direction. Positive. */
    double xStepSize;

    /** Size of a single step in the y direction. Positive. */
    double yStepSize;

    /** The shape that the steps are generated from. */
    Shape shape;

    /** Array to hold step/dir bytes. */
    byte stepDir[] = new byte[CHUNK_SIZE];

    /** S coordinate for each step.  Used for matching up sync points. */
    double sStep[] = new double[CHUNK_SIZE];

    /** X coordinate for each step.  Used for plotting. */
    double xStep[] = new double[CHUNK_SIZE];

    /** Y coordinate for each step.  Used for plotting. */
    double yStep[] = new double[CHUNK_SIZE];

    /** Number of already allocated chunks of storage. */
    int nChunks = 1;

    public static class Point {

        double s;

        double x;

        double y;

        byte stepDir;
    }

    /**
     * Discretize a Shape into steps.
     * @param shape_ Shape to discretize.
     * @param xStepSize_ Step size in x direction.
     * @param yStepSize_ Step size in y direction.
     */
    public Stepper(Shape shape_, double xStepSize_, double yStepSize_) {
        shape = new Shape(shape_);
        xStepSize = xStepSize_;
        yStepSize = yStepSize_;
        makeSteps();
    }

    public int getNumSteps() {
        return stepDir.length;
    }

    public Point getPoint(int idx) {
        Point pt = new Point();
        pt.s = sStep[idx];
        pt.x = xStep[idx];
        pt.y = yStep[idx];
        pt.stepDir = stepDir[idx];
        return pt;
    }

    /**
     * Decode a pair of step / dir bits.
     * @param b A byte whose least significant 2 bits will be decoded
     * @return 1 for + step, -1 for a negative step, and 0 for no step
     */
    public static int decodeStepDirPair(byte b) {
        if ((b & X_STEP) == 0) {
            return 0;
        } else {
            if ((b & X_DIR) == 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /** Grow the result arrays by one chunk. */
    private void grow() {
        nChunks++;
        int nSize = nChunks * CHUNK_SIZE;
        stepDir = Arrays.copyOf(stepDir, nSize);
        sStep = Arrays.copyOf(sStep, nSize);
        xStep = Arrays.copyOf(xStep, nSize);
        yStep = Arrays.copyOf(yStep, nSize);
    }

    /**
     * Discretize the shape into steps.
     * This is a brute force approach designed to be robust in the face of
     * high curvature at the same 'wavelength' as the step size.
     */
    private void makeSteps() {
        double minStep = Math.min(Math.abs(xStepSize), Math.abs(yStepSize));
        double s = 0.;
        double ds = minStep / 5;
        Shape.Point pt = shape.evaluate(s);
        double xWire = pt.x;
        double yWire = pt.y;
        int stepIdx = 0;
        boolean atEnd = false;
        while (!atEnd) {
            s += ds;
            int box[][] = new int[3][3];
            int boxMax = 0;
            int ixmax = -1;
            int iymax = -1;
            double smax = s;
            boolean insideBox = true;
            while (insideBox) {
                pt = shape.evaluate(s);
                int ix = (int) (Math.round((pt.x - xWire) / xStepSize) + 1);
                int iy = (int) (Math.round((pt.y - yWire) / yStepSize) + 1);
                if (ix > 2 || ix < 0 || iy > 2 || iy < 0) {
                    insideBox = false;
                } else if (ix != 1 || iy != 1) {
                    box[ix][iy] += 1;
                    if (box[ix][iy] > boxMax) {
                        boxMax = box[ix][iy];
                        ixmax = ix;
                        iymax = iy;
                        smax = s;
                    }
                }
                s += ds;
                atEnd = s >= shape.getArcLength();
                if (atEnd) {
                    break;
                }
            }
            assert (atEnd || (ixmax != -1 && iymax != -1));
            byte step = 0;
            s = smax;
            switch(ixmax) {
                case 0:
                    step |= X_STEP;
                    xWire -= xStepSize;
                    break;
                case 1:
                    break;
                case 2:
                    step |= X_STEP;
                    step |= X_DIR;
                    xWire += xStepSize;
                    break;
            }
            switch(iymax) {
                case 0:
                    step |= Y_STEP;
                    yWire -= yStepSize;
                    break;
                case 1:
                    break;
                case 2:
                    step |= Y_STEP;
                    step |= Y_DIR;
                    yWire += yStepSize;
                    break;
            }
            assert (step != 0);
            stepDir[stepIdx] = step;
            sStep[stepIdx] = s;
            xStep[stepIdx] = xWire;
            yStep[stepIdx] = yWire;
            stepIdx++;
            if (stepIdx >= nChunks * CHUNK_SIZE) {
                grow();
            }
        }
        stepDir = Arrays.copyOf(stepDir, stepIdx);
        xStep = Arrays.copyOf(xStep, stepIdx);
        yStep = Arrays.copyOf(yStep, stepIdx);
        sStep = Arrays.copyOf(sStep, stepIdx);
    }
}
