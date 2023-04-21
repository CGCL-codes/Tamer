package pcgen.cdom.content;

/**
 * A Modifier is designed to take an object of a specific type (in a given
 * context) and return another object of the same type. In some cases,
 * 
 * @param <T>
 *            The class of object this Modifier acts upon.
 */
public interface Modifier<T> {

    /**
	 * Applies this Modifier to the given input object, in the context of the
	 * given context object.
	 * 
	 * Note that classes that implement this Modifier interface may return the
	 * object passed in as the input object. Therefore, if the input object is
	 * mutable, the caller of the applyModifier method should be aware of that
	 * behavior, and should treat the returned object appropriately.
	 * 
	 * @param obj
	 *            The input object this Modifier will act upon
	 * @param context
	 *            The context of this Modifier, to establish (if necessary),
	 *            whether this Modifier should act upon the input object
	 * @return The modified object, of the same class as the input object.
	 */
    public T applyModifier(T obj, Object context);

    /**
	 * The class of object this Modifier acts upon.
	 * 
	 * @return The class of object this Modifier acts upon.
	 */
    public Class<T> getModifiedClass();

    /**
	 * Returns a representation of this Modifier, suitable for storing in an LST
	 * file.
	 * 
	 * @return A representation of this Modifier, suitable for storing in an LST
	 *         file.
	 */
    public String getLSTformat();
}
