package br.edu.ufcg.ccc.javalog.event;

/** 
 * Event thrown by a package object.
 * @author Allyson Lima, Diego Pedro, Victor Freire
 * @version 03/11/09 
 */
@SuppressWarnings("serial")
public class PackageEvent extends java.util.EventObject {

    private String property;

    /**
	 * Returns the property that has been modified.
	 */
    public String getProperty() {
        return property;
    }

    /**
	 * Default event constructor.
	 * @param source who fired the event
	 */
    public PackageEvent(Object source) {
        super(source);
    }

    /**
	 * Constructor for property-related events.
	 * @param source who fired the event
	 * @param propertyChanged the property that was modified
	 */
    public PackageEvent(Object source, String propertyChanged) {
        super(source);
        property = propertyChanged;
    }
}
