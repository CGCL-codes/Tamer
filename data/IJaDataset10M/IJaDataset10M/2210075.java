package de.fzi.injectj.model.exception;

/**
 * @author gutzmann
 *
 * Thrown if a modifier/combination of modifiers it not valid on this weavepoint.
 * In java, this may be e.g.
 * <ul>
 * <li> the combination of "private" and "public" for any method
 * <li> the combination of "final" and "abstract" for any class
 * <li> "private" for an interface member or abstract method in a class
 * </ul>
 */
public class ModifierException extends ModelException {

    /**
	 * @param message
	 */
    public ModifierException(String message) {
        super(message);
    }

    /**
	 * 
	 */
    public ModifierException() {
        super();
    }

    public boolean isHandable() {
        return false;
    }

    public boolean isFatal() {
        return false;
    }

    public boolean isIgnorable() {
        return false;
    }
}
