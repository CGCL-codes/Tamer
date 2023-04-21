package patterns;

public class Marble extends AbstractPattern {

    private final Perturberation perturberation;

    private final Slope slope;

    public Marble() {
        perturberation = null;
        slope = new Sine();
    }

    public Marble(Slope slope) {
        if (slope == null) throw new NullPointerException();
        this.slope = slope;
        perturberation = null;
    }

    public Marble(Perturberation perturberation) {
        this.perturberation = perturberation;
        slope = new Sine();
    }

    public Marble(Slope slope, Perturberation perturberation) {
        if (slope == null) throw new NullPointerException();
        this.slope = slope;
        this.perturberation = perturberation;
    }

    public float valueAt(float x, float y, float z) {
        if (perturberation != null) x += perturberation.valueAt(x, y, z);
        return slope.valueAt(x - (float) Math.floor(x));
    }
}
