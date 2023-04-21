package org.mockito.asm.util;

import org.mockito.asm.FieldVisitor;

/**
 * A {@link FieldVisitor} that prints the ASM code that generates the fields it
 * visits.
 * 
 * @author Eric Bruneton
 */
public class ASMifierFieldVisitor extends ASMifierAbstractVisitor implements FieldVisitor {

    /**
     * Constructs a new {@link ASMifierFieldVisitor}.
     */
    public ASMifierFieldVisitor() {
        super("fv");
    }
}
