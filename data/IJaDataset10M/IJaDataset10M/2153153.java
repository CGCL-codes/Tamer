package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */
public class Products3WithGroupTest extends ModuleTestBase {

    public Products3WithGroupTest(String testName) {
        super(testName, "OpenXavaTest", "Products3WithGroup");
    }

    public void testNotOnChangeActionWhenSearch() throws Exception {
        execute("CRUD.new");
        execute("Products3.showDescription");
        setValue("number", "77");
        execute("CRUD.search");
        assertValue("description", "ANATHEMA");
        assertEditable("description");
    }

    public void testDescriptionsListWithHiddenKeyThrowsChanged() throws Exception {
        execute("CRUD.new");
        execute("Products3.showDescription");
        assertNoErrors();
        assertValue("comments", "");
        setValue("family.oid", "1037101892379");
        assertValue("comments", "Family changed");
    }

    public void testSetValueNotifyingOnReferenceWithHiddenKeyNotResetGroup() throws Exception {
        execute("CRUD.new");
        execute("Products3.showDescription");
        setValue("description", "HOLA");
        execute("Products3.changeFamily");
        assertValue("description", "HOLA");
    }
}
