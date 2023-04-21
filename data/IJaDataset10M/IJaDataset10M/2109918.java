package net.jadoth.sqlengine.types;

/**
 * @author Thomas Muenz
 *
 */
public interface AssembableParamterizedQuery<T> {

    public String assemble(final T... parameters);
}
