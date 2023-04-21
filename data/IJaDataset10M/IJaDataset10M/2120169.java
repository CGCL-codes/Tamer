package net.jadoth.lang.types;

/**
 * Although every Object has String toString() automatically, the meaning of this interface is to "type"
 * a class to have a toString() method that returns a reasonable value for the runtime context.
 * <p>
 * For example: An object of type SELECT will return the proper SQL query it represents instead of simple debug info
 *
 * @author Thomas M�nz
 *
 */
public interface ToString {

    /**
	 * To string.
	 *
	 * @return the string
	 */
    @Override
    String toString();
}
