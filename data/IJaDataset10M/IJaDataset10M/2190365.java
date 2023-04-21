package zildo.monde.util;

public class Pointf {

    public float x, y;

    public Pointf(float a, float b) {
        x = a;
        y = b;
    }

    public void add(Pointf p_point) {
        add(p_point.x, p_point.y);
    }

    public void add(float p_xPlus, float p_yPlus) {
        this.x += p_xPlus;
        this.y += p_yPlus;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
