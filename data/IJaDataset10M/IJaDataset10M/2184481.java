package org.opensourcephysics.display3d.simple3d;

import org.opensourcephysics.controls.*;

/**
 * <p>Title: ElementBox</p>
 * <p>Description: Painter's algorithm implementation of a Box</p>
 * @author Francisco Esquembre
 * @version March 2005
 */
public class ElementBox extends AbstractTile implements org.opensourcephysics.display3d.core.ElementBox {

    private boolean closedBottom = true, closedTop = true;

    private boolean changeNTiles = true;

    private int nx = -1, ny = -1, nz = -1;

    private double[][][] standardBox = null;

    {
        getStyle().setResolution(new Resolution(3, 3, 3));
    }

    public void setClosedBottom(boolean close) {
        this.closedBottom = close;
        setElementChanged(true);
        changeNTiles = true;
    }

    public boolean isClosedBottom() {
        return this.closedBottom;
    }

    public void setClosedTop(boolean close) {
        this.closedTop = close;
        setElementChanged(true);
        changeNTiles = true;
    }

    public boolean isClosedTop() {
        return this.closedTop;
    }

    protected synchronized void computeCorners() {
        int theNx = 1, theNy = 1, theNz = 1;
        org.opensourcephysics.display3d.core.Resolution res = getRealStyle().getResolution();
        if (res != null) {
            switch(res.getType()) {
                case Resolution.DIVISIONS:
                    theNx = Math.max(res.getN1(), 1);
                    theNy = Math.max(res.getN2(), 1);
                    theNz = Math.max(res.getN3(), 1);
                    break;
                case Resolution.MAX_LENGTH:
                    theNx = Math.max((int) Math.round(0.49 + Math.abs(getSizeX()) / res.getMaxLength()), 1);
                    theNy = Math.max((int) Math.round(0.49 + Math.abs(getSizeY()) / res.getMaxLength()), 1);
                    theNz = Math.max((int) Math.round(0.49 + Math.abs(getSizeZ()) / res.getMaxLength()), 1);
                    break;
            }
        }
        if (nx != theNx || ny != theNy || nz != theNz || changeNTiles) {
            nx = theNx;
            ny = theNy;
            nz = theNz;
            changeNTiles = false;
            standardBox = createStandardBox(nx, ny, nz, closedTop, closedBottom);
            setCorners(new double[standardBox.length][4][3]);
        }
        for (int i = 0; i < numberOfTiles; i++) {
            for (int j = 0, sides = corners[i].length; j < sides; j++) {
                System.arraycopy(standardBox[i][j], 0, corners[i][j], 0, 3);
                sizeAndToSpaceFrame(corners[i][j]);
            }
        }
        setElementChanged(false);
    }

    /**
   * Returns the data for a standard box (from (-0.5,-0.5,-0.5) to (0.5,0.5,0.5) )
   * with the given parameters
   */
    private static double[][][] createStandardBox(int nx, int ny, int nz, boolean top, boolean bottom) {
        int nTotal = 2 * nx * nz + 2 * ny * nz;
        if (bottom) {
            nTotal += nx * ny;
        }
        if (top) {
            nTotal += nx * ny;
        }
        double[][][] data = new double[nTotal][4][3];
        int tile = 0;
        double dx = 1.0 / nx, dy = 1.0 / ny, dz = 1.0 / nz;
        for (int i = 0; i < nx; i++) {
            double theX = i * dx - 0.5;
            for (int j = 0; j < ny; j++) {
                double theY = j * dy - 0.5;
                if (bottom) {
                    data[tile][0][0] = theX;
                    data[tile][0][1] = theY;
                    data[tile][0][2] = -0.5;
                    data[tile][1][0] = theX + dx;
                    data[tile][1][1] = theY;
                    data[tile][1][2] = -0.5;
                    data[tile][2][0] = theX + dx;
                    data[tile][2][1] = theY + dy;
                    data[tile][2][2] = -0.5;
                    data[tile][3][0] = theX;
                    data[tile][3][1] = theY + dy;
                    data[tile][3][2] = -0.5;
                    tile++;
                }
                if (top) {
                    data[tile][0][0] = theX;
                    data[tile][0][1] = theY;
                    data[tile][0][2] = 0.5;
                    data[tile][1][0] = theX + dx;
                    data[tile][1][1] = theY;
                    data[tile][1][2] = 0.5;
                    data[tile][2][0] = theX + dx;
                    data[tile][2][1] = theY + dy;
                    data[tile][2][2] = 0.5;
                    data[tile][3][0] = theX;
                    data[tile][3][1] = theY + dy;
                    data[tile][3][2] = 0.5;
                    tile++;
                }
            }
        }
        for (int i = 0; i < nx; i++) {
            double theX = i * dx - 0.5;
            for (int k = 0; k < nz; k++) {
                double theZ = k * dz - 0.5;
                data[tile][0][0] = theX;
                data[tile][0][2] = theZ;
                data[tile][0][1] = -0.5;
                data[tile][1][0] = theX + dx;
                data[tile][1][2] = theZ;
                data[tile][1][1] = -0.5;
                data[tile][2][0] = theX + dx;
                data[tile][2][2] = theZ + dz;
                data[tile][2][1] = -0.5;
                data[tile][3][0] = theX;
                data[tile][3][2] = theZ + dz;
                data[tile][3][1] = -0.5;
                tile++;
                data[tile][0][0] = theX;
                data[tile][0][2] = theZ;
                data[tile][0][1] = 0.5;
                data[tile][1][0] = theX + dx;
                data[tile][1][2] = theZ;
                data[tile][1][1] = 0.5;
                data[tile][2][0] = theX + dx;
                data[tile][2][2] = theZ + dz;
                data[tile][2][1] = 0.5;
                data[tile][3][0] = theX;
                data[tile][3][2] = theZ + dz;
                data[tile][3][1] = 0.5;
                tile++;
            }
        }
        for (int k = 0; k < nz; k++) {
            double theZ = k * dz - 0.5;
            for (int j = 0; j < ny; j++) {
                double theY = j * dy - 0.5;
                data[tile][0][2] = theZ;
                data[tile][0][1] = theY;
                data[tile][0][0] = -0.5;
                data[tile][1][2] = theZ + dz;
                data[tile][1][1] = theY;
                data[tile][1][0] = -0.5;
                data[tile][2][2] = theZ + dz;
                data[tile][2][1] = theY + dy;
                data[tile][2][0] = -0.5;
                data[tile][3][2] = theZ;
                data[tile][3][1] = theY + dy;
                data[tile][3][0] = -0.5;
                tile++;
                data[tile][0][2] = theZ;
                data[tile][0][1] = theY;
                data[tile][0][0] = 0.5;
                data[tile][1][2] = theZ + dz;
                data[tile][1][1] = theY;
                data[tile][1][0] = 0.5;
                data[tile][2][2] = theZ + dz;
                data[tile][2][1] = theY + dy;
                data[tile][2][0] = 0.5;
                data[tile][3][2] = theZ;
                data[tile][3][1] = theY + dy;
                data[tile][3][0] = 0.5;
                tile++;
            }
        }
        return data;
    }

    /**
   * Returns an XML.ObjectLoader to save and load object data.
   * @return the XML.ObjectLoader
   */
    public static XML.ObjectLoader getLoader() {
        return new Loader();
    }

    private static class Loader extends org.opensourcephysics.display3d.core.ElementBox.Loader {

        public Object createObject(XMLControl control) {
            return new ElementBox();
        }
    }
}
