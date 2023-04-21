package TitanGeo;

public class Degree {

    public Degree() {
        degree = 0;
        slope = 0.0;
    }

    public Degree(int d) {
        degree = (d % 360);
    }

    public void setDegree(int d) {
        degree = (d % 360);
    }

    public void setSlope(Position a, Position b) {
        rise = (b.y - a.y);
        run = (b.x - a.x);
        slope = rise / run;
    }

    public void setSlope(double Rise, double Run) {
        rise = Rise;
        run = Run;
        slope = rise / run;
    }

    public double getSlope() {
        return slope;
    }

    public int getDegree() {
        if (degree == 0) {
            calcDegree();
        }
        return degree;
    }

    private void calcDegree() {
        if (run > 0) {
            degree = (int) (Math.round(90 - Math.toDegrees(Math.atan2(slope, 1))));
        } else if (run < 0) {
            degree = (int) (Math.round(270 - Math.toDegrees(Math.atan2(slope, 1))));
        } else if (run == 0 && rise > 0) {
            degree = 0;
        } else if (run == 0 && rise < 0) {
            degree = 180;
        }
    }

    protected double slope, rise, run;

    protected int degree;
}
