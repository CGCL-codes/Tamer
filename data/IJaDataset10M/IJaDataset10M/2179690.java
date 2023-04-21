package info.absu.snow.prototype;

import java.util.List;

/**
 * A specialized interface of an {@link ObjectPrototype} which allows it to accept the content of object's
 * dependencies from instances of {@link info.absu.snow.submapper.ObjectSubMapper}.
 * @author Denys Rtveliashvili
 *
 */
@SuppressWarnings("unchecked")
public interface ObjectPrototypeModifier {

    /**
	 * Pushes in the value of the next property. 
	 * @param value {@link Prototype} of the value of the property
	 */
    void addProperty(Prototype value);

    /**
	 * Pushes in the mapping of values which should be added into the object if the object is a map. The data is
	 * represented by two lists of {@link Prototype}s. And it is supposed that they will have the same length.
	 * @param keys a list of {@link Prototype}s representing the <i>key</i> objects.
	 * @param values a list of {@link Prototype}s representing the <i>value</i> objects.
	 */
    void addMappingContent(List<Prototype> keys, List<Prototype> values);

    /**
	 * Pushes in the list of {@link Prototype}s of values which should be added into the object if the object is
     * a collection.
	 * @param values the list of {@link Prototype}s
	 */
    void addListContent(List<Prototype> values);

    /**
	 * Pushes in the value of the next constructor argument.
	 * @param argument {@link Prototype} of the value of the constructor argument
	 */
    void addConstructorArg(Prototype argument);

    /**
	 * Tells the ID of the object
	 * @param objectId the ID of the object
	 */
    void setObjectId(String objectId);
}
