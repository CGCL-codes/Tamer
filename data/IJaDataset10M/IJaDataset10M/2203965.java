package uk.ac.imperial.ma.metric.plotting;

import uk.ac.imperial.ma.metric.parsing.Parser;
import java.util.Vector;

/**
 * This class extends MathCoordsVector3D. It generates arrays of coordinates from
 * a Cartesian equation and an existing MathCoords or MathCoordsVector.
 * 
 * Its public methods are
 * setPoints(), which is used to generate the
 * points, and which should be called from outside the paint() method.
 * 
 * zFunc: used to return particular values of the function
 * expression.
 *
 * @author Phil Ramsden
 * @version 0.1
 */
public class ZCoordGenerator3D extends MathCoordsVector3D {

    public ZCoordGenerator3D() {
    }

    public ZCoordGenerator3D(MathPainter3D mp) {
        this.mathPainter3D = mp;
    }

    private Parser zParser;

    private CoordGenerator coordGenerator;

    private void setZParser(String funcString, String[] vars) throws Exception {
        zParser = new Parser(funcString, vars);
    }

    public double zFunc(double xVal, double yVal) throws Exception {
        double[] values = new double[2];
        values[0] = xVal;
        values[1] = yVal;
        return zParser.getValue(values);
    }

    public void setPoints(String zString, String x, String y, MathCoords mc) throws Exception {
        MathCoords3D mc3 = new MathCoords3D();
        String[] vars = new String[2];
        vars[0] = x;
        vars[1] = y;
        setZParser(zString, vars);
        int nPoints = mc.getNPoints();
        double[] xPoints = mc.getXPoints();
        double[] yPoints = mc.getYPoints();
        double[] zPoints = new double[nPoints];
        for (int i = 0; i < nPoints; i++) {
            zPoints[i] = zFunc(xPoints[i], yPoints[i]);
        }
        mc3.setPoints(xPoints, yPoints, zPoints, nPoints);
        this.addMathCoords3D(mc3);
    }

    public void setPoints(String zString, String x, String y, MathCoordsVector mcv, int index) throws Exception {
        setPoints(zString, x, y, (MathCoords) (mcv.getMathCoords().elementAt(index)));
    }

    public void setPoints(String zString, String x, String y, MathCoordsVector mcv) throws Exception {
        Vector coords = mcv.getMathCoords();
        for (int index = 0; index < coords.size(); index++) {
            setPoints(zString, x, y, (MathCoords) (coords.elementAt(index)));
        }
    }
}
