package demo.processing;

/**
 * @author Michael Nischt
 * @version 0.1
 */
public final class Touch {

    private final float x, y;

    public Touch(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public static float distanceSquared(Touch from, Touch to) {
        final float x = to.x - from.x;
        final float y = to.y - from.y;
        return x * x + y * y;
    }

    public static float distance(Touch from, Touch to) {
        return (float) Math.sqrt(distanceSquared(from, to));
    }
}
