package md.model.uml.metaschema;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents an UML abstract class. It's a normal class
 * with in addition abstracts methods.
 * @author Bruno da Silva
 * @date november 2008
 *
 */
public class AbstractUMLClass extends NormalUMLClass {

    private Set<AbstractMethod> abstractMethods = new LinkedHashSet<AbstractMethod>();

    /**
	 * Initialize an UML abstract class with a given name and
	 * without abstracts methods
	 * @param n The class name
	 */
    public AbstractUMLClass(String n) {
        super(n);
    }

    /**
	 * Add a new abstract method to the UML abstract class
	 * @param m The new method
	 */
    public void add(AbstractMethod m) {
        this.abstractMethods.add(m);
    }

    /**
	 * Remove the given abstract method from the UML abstract class
	 * @param m The method to remove
	 */
    public void remove(AbstractMethod m) {
        this.abstractMethods.remove(m);
    }

    /**
	 * Return a Set of the abstract methods contained in the UML
	 * abstract class.
	 * Operations on the Set don't affect the UML abstract class,
	 * however, operations on the abstract method reflect on them.
	 * @return The abstract methods
	 */
    public Set<AbstractMethod> abstractMethods() {
        return new LinkedHashSet<AbstractMethod>(this.abstractMethods);
    }

    @Override
    public List<String> validate() {
        List<String> result = super.validate();
        for (AbstractMethod am : abstractMethods()) for (String s : am.validate()) result.add(name() + " : " + s);
        return result;
    }
}
