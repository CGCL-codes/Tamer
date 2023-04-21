package targetanalyzer.core.analysis.image.createTemplate;

public class Point2D {

    public float x;

    public float y;

    public float value;

    public Point2D() {
        this.clear();
    }

    public Point2D(float xx, float yy, float val) {
        this.x = xx;
        this.y = yy;
        this.value = val;
    }

    public Point2D(Point2D origpt) {
        this.Copy(origpt);
    }

    public void clear() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.value = -1.0f;
    }

    public void Copy(Point2D origpt) {
        this.x = origpt.x;
        this.y = origpt.y;
        this.value = origpt.value;
    }

    public double length() {
        return (this.sqrt(this.x * this.x + this.y * this.y));
    }

    public float sep(Point2D origpt) {
        return (this.sqrt((this.x - origpt.x) * (this.x - origpt.x) + (this.y - origpt.y) * (this.y - origpt.y)));
    }

    double sqrt(double x) {
        return (Math.sqrt(x));
    }

    float sqrt(float x) {
        return (float) (Math.sqrt(x));
    }

    @Override
    public String toString() {
        return (" " + this.x + ", " + this.y + "  val " + this.value);
    }
}
