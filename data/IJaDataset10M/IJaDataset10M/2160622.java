package net.cevn.model;

/**
 * The <code>Normal</code> class represents a normal in a 3D model.
 * 
 * @author Christopher Field <cfield2@gmail.com>
 * @version
 * @since 0.0.1
 */
public class Normal extends Vertex {

    /**
	 * Creates a new <code>Normal</code> instance.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 */
    public Normal(final float x, final float y, final float z) {
        super(x, y, z);
    }
}
