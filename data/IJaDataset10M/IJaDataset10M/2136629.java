package org.proteomecommons.MSExpedite.SignalProcessing;

/**
 *
 * @author takis
 */
public class FwhhAttr implements IAttrReflection {

    int gridSize = Integer.MIN_VALUE;

    int leftTol = Integer.MIN_VALUE;

    int rightTol = Integer.MIN_VALUE;

    /** Creates a new instance of Fwhh */
    public FwhhAttr() {
    }

    public int getGridSize() {
        return gridSize;
    }

    public String[] getProperties() {
        return new String[] { "gridSize", "leftTol", "rightTol" };
    }

    public String getUnits(String property) {
        if (property.equalsIgnoreCase("gridSize")) return "Da";
        if (property.equalsIgnoreCase("leftTol")) return "%";
        if (property.equalsIgnoreCase("rightTol")) return "%";
        return "";
    }

    public String getShortDescription(String property) {
        if (property.equalsIgnoreCase("gridSize")) return "Size Of Partition";
        if (property.equalsIgnoreCase("leftTol")) return "Percent - Tolorance";
        if (property.equalsIgnoreCase("rightTol")) return "Percent + Tolorance";
        return "";
    }

    public int getLeftTol() {
        return leftTol;
    }

    public int getRightTol() {
        return rightTol;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public void setLeftTol(int leftTol) {
        this.leftTol = leftTol;
    }

    public void setRightTol(int rightTol) {
        this.rightTol = rightTol;
    }

    public String toString() {
        return "Grid-Size = " + gridSize + " Left-Tol = " + leftTol + " Right-Tol = " + rightTol;
    }

    public Object getAttribute() {
        return this.getClass();
    }
}
