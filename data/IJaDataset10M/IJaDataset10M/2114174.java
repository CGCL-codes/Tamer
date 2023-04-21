package net.sf.jrecipebox.domain.recipe;

import junit.framework.TestCase;

public class CategoryTest extends TestCase {

    Category category;

    protected void setUp() throws Exception {
        super.setUp();
        this.category = new Category();
        category.setName("TestCategory1");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        this.category = null;
    }

    public void testSetAndGetName() {
        assertEquals("TestCategory1", this.category.getName());
        this.category.setName("NewName");
        assertEquals("NewName", this.category.getName());
    }
}
