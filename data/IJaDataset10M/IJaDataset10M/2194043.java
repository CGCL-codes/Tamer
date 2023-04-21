package xml.load;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashSet;
import org.w3c.dom.Node;

/**
 *
 * @author Niall Gallagher
 */
final class CollectionFactory extends Factory {

    public CollectionFactory(Class type) {
        super(type);
    }

    /**
    * Creates the collection to use. If there is a "class" attribute then 
    * this must be used as the type for the collection. If there is no
    * class attribute specified then it will be choosen dynamically
    * trying to match the type of the field.
    */
    public Collection getInstance(Node node) throws Exception {
        Class override = getOverride(node);
        if (override != null) {
            Log.log("FOUND AN OVERIDE [" + override.getName() + "]");
            return getInstance(override);
        }
        if (!isInstantiable(type)) {
            type = getConversion(type);
        }
        if (!isCollection(type)) {
            throw new InstantiationException("Type is not a collection %s", type);
        }
        return (Collection) type.newInstance();
    }

    public Collection getInstance(Class type) throws Exception {
        if (!isInstantiable(type)) {
            throw new InstantiationException("Could not instantiate class %s", type);
        }
        if (!isCollection(type)) {
            throw new InstantiationException("Type is not a collection %s", type);
        }
        return (Collection) type.newInstance();
    }

    public Class getConversion(Class type) {
        if (type.isAssignableFrom(ArrayList.class)) {
            return ArrayList.class;
        }
        if (type.isAssignableFrom(HashSet.class)) {
            return HashSet.class;
        }
        if (type.isAssignableFrom(TreeSet.class)) {
            return TreeSet.class;
        }
        throw new InstantiationException("Cannot instantiate %s", type);
    }

    private boolean isCollection(Class type) {
        return Collection.class.isAssignableFrom(type);
    }
}
