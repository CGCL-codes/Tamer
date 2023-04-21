package tafat.engine.interpolatedfunction;

public abstract class InterpolationMethod {

    public abstract double getY(double X);

    protected double[] getArrayX(Coordinate[] coordinates) {
        return getArray(coordinates, "X");
    }

    protected double[] getArrayY(Coordinate[] coordinates) {
        return getArray(coordinates, "Y");
    }

    private double[] getArray(Coordinate[] coordinates, String xy) {
        double[] array = new double[coordinates.length];
        if ("X".equals(xy.toUpperCase())) for (int i = 0; i < coordinates.length; i++) array[i] = coordinates[i].x; else for (int i = 0; i < coordinates.length; i++) array[i] = coordinates[i].y;
        return array;
    }
}
