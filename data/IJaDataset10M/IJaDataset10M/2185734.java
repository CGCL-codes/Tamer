package org.jdas.model;

import org.junit.Test;

public abstract class DOMDocumentTestCase extends GenericDocumentTestCase {

    @Test
    public abstract void testGetDocument();

    @Test
    public abstract void testGetNumberOfTags();
}
