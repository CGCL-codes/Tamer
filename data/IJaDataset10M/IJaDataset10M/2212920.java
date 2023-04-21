package gumbo.tech.math;

/**
 * A 3D entity with an orientation. Concrete instances must define the
 * relationship between the direction vectors and the entity shape (e.g. local
 * -Z and +Y).
 * @author jonb
 */
public interface Orientable3 extends Directable3 {

    /**
	 * Gets the direction of "up" relative for this entity. Typically
	 * orthogonal, but never parallel to the forward direction.
	 * @return Temp output value. Never null.
	 */
    public UnitVector3 getUpward();
}
