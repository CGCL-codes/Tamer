package ca.mcgill.cs.swevo.jayfx.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a class program element.
 */
public class ClassElement extends AbstractElement {

    private static final long serialVersionUID = 8565762363075921025L;

    /**
	 * Initialize a class element with its fully qualified name Class elements
	 * should only be created by a FlyweightElementFactory.
	 * 
	 * @param pId
	 *            The fully qualified name of the class.
	 */
    protected ClassElement(final String pId) {
        super(pId);
    }

    /**
	 * @param pObject
	 *            The object to compare the class to.
	 * @return Whether pObject has the same ID as this element.
	 */
    @Override
    public boolean equals(final Object pObject) {
        if (!(pObject instanceof ClassElement)) return false; else return this.getId().equals(((ClassElement) pObject).getId());
    }

    /**
	 * Returns the category of this element, which always a class.
	 * 
	 * @return the keyword "class".
	 */
    @Override
    public Category getCategory() {
        return Category.CLASS;
    }

    /**
	 * @return The declaring class of this class. null is the element is a
	 *         top-level class.
	 */
    public ClassElement getDeclaringClass() {
        return null;
    }

    /**
	 * @return The name of the package in which this class is defined.
	 */
    public String getPackageName() {
        final int lIndex = this.getId().lastIndexOf(".");
        if (lIndex >= 0) return this.getId().substring(0, this.getId().lastIndexOf(".")); else return "";
    }

    /**
	 * @return The name of the class without the package prefix.
	 */
    @Override
    public String getShortName() {
        final String lPackageName = this.getPackageName();
        if (lPackageName.length() > 0) return this.getId().substring(lPackageName.length() + 1, this.getId().length()); else return this.getId();
    }

    /**
	 * @return A hash code for this element.
	 */
    @Override
    public int hashCode() {
        if (this.getId() == null) this.setId("java.lang.Object");
        return this.getId().hashCode();
    }
}
