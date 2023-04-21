package motej.event;

/**
 * 
 * <p>
 * @author <a href="mailto:vfritzsch@users.sourceforge.net">Volker Fritzsch</a>
 */
public class AccelerometerEvent<T> {

    private int x;

    private int y;

    private int z;

    private T source;

    public AccelerometerEvent(T source, int x, int y, int z) {
        this.source = source;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public T getSource() {
        return source;
    }
}
