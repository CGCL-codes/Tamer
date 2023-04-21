package org.vikamine.gui.util.filters;

import org.vikamine.kernel.data.Attribute;

/**
 * @author Tobias Vogele
 */
public interface AttributeFilter {

    boolean isAccepted(String id);

    boolean isAccepted(Attribute att);

    String getName();

    void initialize();

    void enabled();
}
