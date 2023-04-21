package org.gwtoolbox.commons.generator.rebind;

import com.google.gwt.core.ext.typeinfo.JMethod;

/**
 * @author Uri Boness
 */
public interface JMethodVisitor {

    /**
     * Visits the given method.
     *
     * @param method The method to visit
     * @return Indicates whether the traversal using this visitor should continue or not. Returning {@code true} indicates
     *         the traversal should continue.
     */
    boolean visit(JMethod method);
}
