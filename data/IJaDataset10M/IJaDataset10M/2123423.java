package utils;

public class ConversorAngulos {

    public static double convertirARadianes(double ang) {
        return (ang * 2 * Math.PI) / 360.0;
    }

    public static double convertirAGrados(double rad) {
        return (rad * 360.0) / (2 * Math.PI);
    }
}
