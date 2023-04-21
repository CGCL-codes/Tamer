package icebird.metadata;

/**
 * @author sergey
 */
public class StaticField extends Field {

    /**
	 * @param declType
	 * @param modifiers
	 * @param nameID
	 * @param descriptorID
	 */
    public StaticField(Class declType, int modifiers, int nameID, int descriptorID) {
        super(declType, modifiers, nameID, descriptorID);
    }

    /**
	 * Returns static name.
	 */
    public final String toString() {
        return getDeclaringType().getName() + "." + getName();
    }
}
