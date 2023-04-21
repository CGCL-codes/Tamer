package net.sourceforge.jenesis4java;

/**
 * <code>Declaration</code> subinterface for import declarations at the
 * beginning of a compilation unit.
 */
public interface Import extends Declaration {

    /**
     * Gets the name of the class or package to be imported.
     */
    String getName();

    /**
     * Returns <code>true</code> if this import is a single-type import,
     * <code>false</code> if it is an import-type-on-demand.
     */
    boolean isSingle();

    /**
     * Sets the name of the class or package to be imported.
     */
    Import setName(String name);
}
