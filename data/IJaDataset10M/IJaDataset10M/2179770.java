package joptsimple.internal;

/**
 * @author <a href="mailto:pholser@alumni.rice.edu">Paul Holser</a>
 * @version $Id: Classes.java,v 1.8 2009/03/31 03:38:23 pholser Exp $
 */
public abstract class Classes {

    /**
     * Gives the "short version" of the given class name.  Somewhat naive to inner
     * classes.
     *
     * @param className class name to chew on
     * @return the short name of the class
     */
    public static String shortNameOf(String className) {
        return className.substring(className.lastIndexOf('.') + 1);
    }
}
