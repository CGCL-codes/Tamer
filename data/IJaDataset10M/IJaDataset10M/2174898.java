package org.helianto.document;

import static org.junit.Assert.*;
import org.helianto.core.base.AbstractAssociation;
import org.junit.Test;

/**
 * 
 * @author Mauricio Fernandes de Castro
 */
public class FunctionAssociationTests {

    @Test
    public void constructor() {
        FunctionAssociation assoc = new FunctionAssociation();
        assertTrue(assoc instanceof AbstractAssociation);
    }

    @SuppressWarnings("serial")
    @Test
    public void association() {
        Role parent = new Role() {
        };
        Role child = new Role() {
        };
        FunctionAssociation assoc = new FunctionAssociation();
        assoc.setParent(parent);
        assoc.setChild(child);
        assertSame(parent, assoc.getParent());
        assertSame(child, assoc.getChild());
    }
}
