package org.qedeq.kernel.se.base.module;

/**
 * List of imports.
 *
 * @author  Michael Meyling
 */
public interface ImportList {

    /**
     * Get size of list.
     *
     * @return  List size.
     */
    public int size();

    /**
     * Get <code>index</code>-th element of list.
     *
     * @param   index   Index.
     * @return  Index-th element.
     */
    public Import get(int index);
}
