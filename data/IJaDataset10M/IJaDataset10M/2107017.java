package org.python.compiler.advanced;

import java.util.List;

/**
 * @author Tobias Ivarsson
 * @param <T> The base type of the scope representation created by this factory.
 */
public interface ScopeFactory<T> {

    /**
     * @param locals
     * @param cell
     * @param hasStarImport
     * @return an instance of a global scope representation, as created by the
     *         specific factory implementation.
     */
    T createGlobal(String[] locals, String[] cell, boolean hasStarImport);

    /**
     * @param name
     * @param locals
     * @param globals
     * @param free
     * @param cell
     * @param hasStarImport
     * @return an instance of a class scope representation, as created by the
     *         specific factory implementation.
     */
    T createClass(String name, String[] locals, String[] globals, String[] free, String[] cell, boolean hasStarImport);

    /**
     * @param name
     * @param parameters
     * @param locals
     * @param globals
     * @param free
     * @param cell
     * @param isGenerator
     * @param hasStarImport
     * @return an instance of a function scope representation, as created by the
     *         specific factory implementation.
     */
    T createFunction(String name, String[] parameters, String[] locals, String[] globals, String[] free, String[] cell, boolean isGenerator, boolean hasStarImport);
}
