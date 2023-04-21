package au.com.kelpie.fgfp.map;

/**
 * @
 */
public interface IProjector {

    /**
     * @param size
     */
    public abstract void setSize(double size);

    /**
     * @param size
     */
    public abstract double getSize();

    /**
     * @param d
     * @param i
     * @param j
     */
    public abstract void setCenter(double lon, double d);

    /**
     * @param lat
     * @param lon
     * @return
     */
    public abstract double[] project(double lat, double lon);

    /**
     * @param x
     * @param y
     * @return
     */
    public abstract double[] unProject(double x, double y, double z);
}
