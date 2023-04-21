package com.excitingtechnology.tep.imaging.geom;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.log4j.Logger;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 * RANSAC is an abbreviation for "RANdom SAmple Consensus". 
 * <BR>
 * It is an iterative method to estimate parameters of a mathematical model from a set of observed data which contains outliers.
 * <BR>
 * In this case the points are matched to an ellipse.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/RANSAC">http://en.wikipedia.org/wiki/RANSAC</a>
 *
 * @author David McManamon
 * @author Dongheng Li (Matlab version) 
 */
public class RANSACEllipse implements EllipseFitwOutliers {

    private static Logger log = Logger.getLogger(RANSACEllipse.class);

    private Random numGen = new Random();

    private int maxIterations = 100;

    public RANSACEllipse() {
    }

    @Override
    public EllipseAndIndexes fit(List<Point> points, List<Integer> probableInliers, double maxInlierPixelDist) {
        if (points == null || points.size() < 5) {
            String msg = "At least five points are necessary to fit an ellipse.";
            throw new IllegalArgumentException(msg);
        }
        if (log.isDebugEnabled()) {
            log.debug("Fitting ellipse - " + points.size() + " input points: " + points);
        }
        int[] x = new int[points.size()];
        int[] y = new int[points.size()];
        for (int i = 0; i < x.length; i++) {
            Point p = points.get(i);
            x[i] = p.x;
            y[i] = p.y;
        }
        return fit(x, y, probableInliers, maxInlierPixelDist);
    }

    protected EllipseAndIndexes fit(int[] x, int[] y, List<Integer> probableInliers, double maxInlierPixelDist) {
        int nEdgePoints = x.length;
        int iteration = 0;
        Double N = Double.MAX_VALUE;
        Ellipse bestEllipse = null;
        List<Integer> maxInliers = new ArrayList<Integer>(0);
        boolean random = true;
        if (probableInliers != null && probableInliers.size() >= 5) {
            random = false;
        }
        double[][] h = normalizationHomography(x, y);
        double[] nx = normalizedPoints(h[0][0], h[0][2], x);
        double[] ny = normalizedPoints(h[1][1], h[1][2], y);
        Matrix ep = buildMatrixEP(nx, ny);
        double distThreshold = Math.sqrt(3.84) * h[0][0];
        while (N > iteration) {
            Set<Integer> indexes;
            if (random) {
                indexes = randomIndexes(nEdgePoints);
            } else if (probableInliers != null) {
                indexes = new HashSet<Integer>(probableInliers);
                probableInliers = null;
            } else {
                indexes = new HashSet<Integer>(maxInliers);
            }
            double[] nxi = fillArrayFromIndexes(nx, indexes);
            double[] nyi = fillArrayFromIndexes(ny, indexes);
            if (log.isDebugEnabled()) log.debug("Matching ellipse from indexes " + indexes);
            Matrix A = buildMatrixA(nxi, nyi);
            SingularValueDecomposition svd = A.svd();
            Matrix V = svd.getV();
            int lastRow = V.getRowDimension() - 1;
            int lastColumn = V.getColumnDimension() - 1;
            Matrix nConicPar = V.getMatrix(0, lastRow, lastColumn, lastColumn);
            double[][] nConicMatrix = getNConicMatrix(nConicPar);
            Matrix ncm = new Matrix(nConicMatrix);
            Matrix error = ep.times(ncm).arrayTimes(ep);
            double[] errDist = rowSum(error);
            System.out.println("ERROR: " + distThreshold + " " + Arrays.toString(errDist));
            List<Integer> inliers = new LinkedList<Integer>();
            for (int i = 0; i < errDist.length; i++) {
                if (Math.abs(errDist[i]) < distThreshold) inliers.add(i);
            }
            log.debug("Inliers:" + inliers.size() + " of " + nEdgePoints + " iter " + iteration);
            random = true;
            if (inliers.size() > maxInliers.size() && inliers.size() >= 5) {
                Ellipse ePar = convertConicParamsToEllipse(nConicPar);
                Ellipse eDenorm = denormalizeEllipseParameters(ePar, h);
                double ratio = ePar.getA() / ePar.getB();
                maxInliers = inliers;
                bestEllipse = eDenorm;
                double r = Math.pow(inliers.size() / (double) nEdgePoints, 5);
                N = Math.log(1 - 0.99) / Math.log(1 - r + Double.MIN_VALUE);
                random = false;
            }
            iteration++;
            if (iteration > maxIterations) {
                return null;
            }
        }
        if (log.isDebugEnabled()) {
            log.debug(iteration + " iterations matched ellipse " + bestEllipse + " " + maxInliers.size() + " inliers of " + nEdgePoints);
        }
        EllipseAndIndexes ei = new EllipseAndIndexes(bestEllipse, maxInliers);
        return ei;
    }

    protected Ellipse denormalizeEllipseParameters(Ellipse e1, double[][] h) {
        double a = e1.getA() / h[0][0];
        double b = e1.getB() / h[1][1];
        double cx = (e1.getCenterX() - h[0][2]) / h[0][0];
        double cy = (e1.getCenterY() - h[1][2]) / h[1][1];
        double theta = e1.getTheta();
        return new Ellipse(cx, cy, a, b, theta);
    }

    protected Ellipse convertConicParamsToEllipse(Matrix conicPar) {
        double c1 = conicPar.get(0, 0);
        double c2 = conicPar.get(1, 0);
        double c3 = conicPar.get(2, 0);
        double c4 = conicPar.get(3, 0);
        double c5 = conicPar.get(4, 0);
        double c6 = conicPar.get(5, 0);
        double theta = Math.atan2(c2, c1 - c3) / 2.0;
        double ct = Math.cos(theta);
        double st = Math.sin(theta);
        double ap = c1 * ct * ct + c2 * ct * st + c3 * st * st;
        double cp = c1 * st * st - c2 * ct * st + c3 * ct * ct;
        Matrix T = new Matrix(2, 2);
        T.set(0, 0, c1);
        T.set(1, 0, c2 / 2);
        T.set(0, 1, c2 / 2);
        T.set(1, 1, c3);
        Matrix denom = new Matrix(2, 1);
        denom.set(0, 0, c4);
        denom.set(1, 0, c5);
        Matrix t = T.times(2.0).inverse().uminus().times(denom);
        double centerX = t.get(0, 0);
        double centerY = t.get(1, 0);
        Matrix val = ((t.transpose()).times(T)).times(t);
        double scaleInv = val.get(0, 0) - c6;
        double a = Math.sqrt(scaleInv / ap);
        double b = Math.sqrt(scaleInv / cp);
        Ellipse e = new Ellipse(centerX, centerY, a, b, theta);
        return e;
    }

    protected double[][] getNConicMatrix(Matrix nConicPar) {
        double[][] nConicMatrix = new double[3][3];
        nConicMatrix[0][0] = nConicPar.get(0, 0);
        nConicMatrix[0][1] = nConicPar.get(1, 0) / 2.0;
        nConicMatrix[0][2] = nConicPar.get(3, 0) / 2.0;
        nConicMatrix[1][0] = nConicPar.get(1, 0) / 2.0;
        nConicMatrix[1][1] = nConicPar.get(2, 0);
        nConicMatrix[1][2] = nConicPar.get(4, 0) / 2.0;
        nConicMatrix[2][0] = nConicPar.get(3, 0) / 2.0;
        nConicMatrix[2][1] = nConicPar.get(4, 0) / 2.0;
        nConicMatrix[2][2] = nConicPar.get(5, 0);
        return nConicMatrix;
    }

    protected double[] rowSum(Matrix m) {
        int columns = m.getColumnDimension();
        double[] x = new double[m.getRowDimension()];
        for (int row = 0; row < m.getRowDimension(); row++) {
            double sum = 0;
            for (int col = 0; col < columns; col++) {
                sum += m.get(row, col);
            }
            x[row] = sum;
        }
        return x;
    }

    protected Matrix buildMatrixEP(double[] nx, double[] ny) {
        Matrix ep = new Matrix(nx.length, 3);
        for (int i = 0; i < nx.length; i++) {
            ep.set(i, 0, nx[i]);
            ep.set(i, 1, ny[i]);
            ep.set(i, 2, 1.0);
        }
        return ep;
    }

    protected Matrix buildMatrixA(double[] nxi, double[] nyi) {
        Matrix nxiM = new Matrix(nxi, 1);
        Matrix nyiM = new Matrix(nyi, 1);
        Matrix nxiNxi = nxiM.arrayTimes(nxiM);
        Matrix nxiNyi = nxiM.arrayTimes(nyiM);
        Matrix nyiNyi = nyiM.arrayTimes(nyiM);
        Matrix A = new Matrix(nxi.length, 6);
        for (int i = 0; i < nxi.length; i++) {
            A.set(i, 0, nxiNxi.get(0, i));
            A.set(i, 1, nxiNyi.get(0, i));
            A.set(i, 2, nyiNyi.get(0, i));
            A.set(i, 3, nxiM.get(0, i));
            A.set(i, 4, nyiM.get(0, i));
            A.set(i, 5, 1.0);
        }
        return A;
    }

    protected double[] fillArrayFromIndexes(double[] src, Set<Integer> randIndexes) {
        double[] a = new double[randIndexes.size()];
        Iterator<Integer> indexIter = randIndexes.iterator();
        for (int i = 0; i < a.length; i++) a[i] = src[indexIter.next()];
        return a;
    }

    protected static double[] normalizedPoints(double h1, double h2, int[] p) {
        double[] res = new double[p.length];
        for (int i = 0; i < p.length; i++) {
            double val = h1 * p[i] + h2;
            res[i] = val;
        }
        return res;
    }

    protected static double[][] normalizationHomography(int[] x, int[] y) {
        int length = y.length;
        double sumx = 0, sumy = 0, sumDist = 0;
        for (int i = 0; i < length; i++) {
            sumx += x[i];
            sumy += y[i];
            sumDist += Math.sqrt(x[i] * x[i] + y[i] * y[i]);
        }
        double cx = sumx / length;
        double cy = sumy / length;
        double meanDist = sumDist / length;
        double distScale = Math.sqrt(2) / meanDist;
        double[][] h = new double[3][3];
        h[0][0] = distScale;
        h[0][1] = 0;
        h[0][2] = -1.0 * distScale * cx;
        h[1][0] = 0;
        h[1][1] = distScale;
        h[1][2] = -1.0 * distScale * cy;
        h[2][0] = 0;
        h[2][1] = 0;
        h[2][2] = 1;
        return h;
    }

    protected Set<Integer> randomIndexes(int range) {
        Set<Integer> result = new HashSet<Integer>(5);
        while (result.size() < 5) {
            Integer index = numGen.nextInt(range);
            result.add(index);
        }
        return result;
    }
}
