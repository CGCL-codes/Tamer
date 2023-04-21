package org.jmlspecs.checker;

import org.multijava.util.compiler.TokenReference;

/**
 * This is the super class for the classes representing loop-invariant
 * and variant-function annotations for loop-stmt.
 *
 * @author Curtis Clifton
 * @version $Revision: 1.2 $ */
public abstract class JmlLoopSpecification extends JmlNode {

    public JmlLoopSpecification(TokenReference where, boolean isRedundantly) {
        super(where);
        this.isRedundantly = isRedundantly;
    }

    public boolean isRedundantly() {
        return isRedundantly;
    }

    private boolean isRedundantly;
}
