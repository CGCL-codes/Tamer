package ch.sahits.model;

import java.util.List;
import java.util.Vector;

/**
 * This class is a container for all data class for classes
 * to be generated. This class holds only general attributes
 * that are provided by many object oriented languages such as
 * Java, C++ and PHP.<br>
 * This class is intended to be subclassed to provide a more
 * language specific data model. 
 * of the class to be generated that is 
 * directly provided by the wizard.
 * @author   Andi Hotz
 * @since 0.9.0 r82
 */
public class GeneratedClass implements IGeneratorClass {

    /**
	 * Name of the class
	 * @uml.property  name="className"
	 */
    private String className = null;

    /**
	 * Type of the super-class
	 * @uml.property  name="superClass"
	 */
    @SuppressWarnings("unchecked")
    private List<Class> superClass = new Vector<Class>();

    /**
	 * @param _className Name of the class
	 */
    public GeneratedClass(String _className) {
        super();
        this.className = _className;
    }

    /**
	 * @return  the className
	 * @uml.property  name="className"
	 */
    public String getClassName() {
        return className;
    }

    /**
	 * @param _className  the className to set
	 * @uml.property  name="className"
	 */
    public void setClassName(String _className) {
        this.className = _className;
    }

    /**
	 * @return  the superClass
	 * @uml.property  name="superClass"
	 */
    @SuppressWarnings("unchecked")
    public List<Class> superclasses() {
        return superClass;
    }

    /**
	 * Check if there is any specific superclass defined.
	 * @return true if the super class is not Object
	 */
    public boolean hasSuperClass() {
        return !superClass.isEmpty();
    }

    /**
	 * @param _superClass  the superClass to set
	 * @uml.property  name="superClass"
	 */
    @SuppressWarnings("unchecked")
    public void addSuperClass(Class _superClass) {
        this.superClass.add(_superClass);
    }
}
