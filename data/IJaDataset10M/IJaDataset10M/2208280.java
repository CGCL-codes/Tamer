package us.wthr.jdem846.gis.projections;

public class MapPoint {

    public double row;

    public double column;

    public double z = 0;

    public MapPoint() {
    }

    public double getRow() {
        return row;
    }

    public void setRow(double row) {
        this.row = row;
    }

    public double getColumn() {
        return column;
    }

    public void setColumn(double column) {
        this.column = column;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
