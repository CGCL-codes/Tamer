package jahuwaldt.j3d;

import java.util.EventObject;
import javax.media.j3d.Transform3D;

/**
*  An even that represents a change in a transform.
*
*  <p>  Modified by:  Joseph A.Huwaldt  </p>
*
*  @author   Joseph A. Huwaldt   Date:  April 14, 2009
*  @version  April 14, 2009
**/
public class TransformChangeEvent extends EventObject {

    /**
	*  The type of change that has been made to the transform.
	**/
    public enum Type {

        ROTATE, TRANSLATE, ZOOM
    }

    private final Type _type;

    private final Transform3D _transform;

    /**
	* Construct a new event using the specified source, type code and transform.
	*
	*  @param source    The source of the change in the transform.
	*  @param type      The type code for the change as enumerated in this class.
	*  @param transform The new transform after the change.
	**/
    public TransformChangeEvent(Object source, Type type, Transform3D transform) {
        super(source);
        _type = type;
        _transform = transform;
    }

    /**
	*  Return the type of transform.
	**/
    public Type getType() {
        return _type;
    }

    /**
	*  Return the new transformation matrix.
	**/
    public Transform3D getTransform() {
        return _transform;
    }
}
