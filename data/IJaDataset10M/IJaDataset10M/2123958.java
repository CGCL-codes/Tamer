package info.joseluismartin.gui.bind;

/**
 * Interface for Binder Factories
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 */
public interface BinderFactory {

    /**
	 * Try to find a binder for a Class, use super Class if none is configured.
	 * 
	 * @param clazz Class to looking for
	 * @return a Binder for that class or null if none
	 */
    public PropertyBinder getBinder(Class<?> clazz);
}
