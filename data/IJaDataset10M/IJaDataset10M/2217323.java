package common;

/**
 * <p>Title: Picture Editor</p>
 *
 * <p>Description: Editor grafico per sistemi SCADA</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Itaco S.r.l.</p>
 *
 * @author Andrea Annibali
 * @version 1.0
 */
public class CShapePoints {

    private double[] xPnt = null;

    private double[] yPnt = null;

    public CShapePoints(double[] xPnt_, double[] yPnt_) {
        xPnt = xPnt_;
        yPnt = yPnt_;
    }

    public CShapePoints(Double[] xPnt_, Double[] yPnt_) {
        double[] x = new double[xPnt_.length];
        double[] y = new double[yPnt_.length];
        for (int i = 0; i < xPnt_.length; i++) {
            xPnt[i] = xPnt_[i].doubleValue();
        }
        for (int i = 0; i < xPnt_.length; i++) {
            yPnt[i] = yPnt_[i].doubleValue();
        }
    }

    public void setXPnt(double[] xPnt) {
        this.xPnt = xPnt;
    }

    public void setYPnt(double[] yPnt) {
        this.yPnt = yPnt;
    }

    public double[] getXPnt() {
        return xPnt;
    }

    public double[] getYPnt() {
        return yPnt;
    }
}
