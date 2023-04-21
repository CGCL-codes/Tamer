package name.emu.games.q3mapgen.id;

/**
 * @author emu
 *
 * Not an aircraft but rather a geometrical plane, defining one of the side of a Brush.
 */
public class Plane {

    private String texture;

    private Vector3d[] vertices = new Vector3d[3];

    private double shiftX, shiftY;

    private double rotation, scaleX, scaleY;

    private double contents;

    private int flags, value;

    /**
	 * Constructor. Params match the plane specification used in id's map format.
	 */
    public Plane(Vector3d v1, Vector3d v2, Vector3d v3, String texture, double shiftX, double shiftY, double rotation, double scaleX, double scaleY, double contents, int flags, int value) {
        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;
        this.texture = texture;
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.rotation = rotation;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.contents = contents;
        this.flags = flags;
        this.value = value;
    }

    /**
	 * Simple constructor.
	 * @param v1 
	 * @param v2
	 * @param v3
	 * @param texture
	 */
    public Plane(Vector3d v1, Vector3d v2, Vector3d v3, String texture) {
        this(v1, v2, v3, texture, 0, 0, 0, 1, 1, 0, 0, 0);
    }

    /**
	 * Returns a string representing this plane as defined by ID Software's map file format.
	 */
    public String toString() {
        return "( " + vertices[0] + " ) ( " + vertices[1] + " ) ( " + vertices[2] + " ) " + texture + " " + shiftX + " " + shiftY + " " + rotation + " " + scaleX + " " + scaleY;
    }
}
