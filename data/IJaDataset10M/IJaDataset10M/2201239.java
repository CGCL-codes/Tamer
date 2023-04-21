package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */
public class Products2ReferenceAndStereotypeTest extends ModuleTestBase {

    public Products2ReferenceAndStereotypeTest(String testName) {
        super(testName, "Products2ReferenceAndStereotype");
    }

    public void testStereotypeInViewDepedensOnReference() throws Exception {
        execute("CRUD.new");
        String[][] familyValues = { { "", "" }, { "1", "SOFTWARE" }, { "2", "HARDWARE" }, { "3", "SERVICIOS" } };
        assertValue("family.number", "2");
        assertValidValues("family.number", familyValues);
        setValue("family.number", "");
        String[][] voidValues = { { "", "" } };
        assertValue("subfamilyNumber", "");
        assertValidValues("subfamilyNumber", voidValues);
        setValue("family.number", "2");
        String[][] hardwareValues = { { "", "" }, { "12", "PC" }, { "13", "PERIFERICOS" }, { "11", "SERVIDORES" } };
        assertValue("subfamilyNumber", "");
        assertValidValues("subfamilyNumber", hardwareValues);
        setValue("family.number", "1");
        String[][] softwareValues = { { "", "" }, { "1", "DESARROLLO" }, { "2", "GESTION" }, { "3", "SISTEMA" } };
        assertValue("subfamilyNumber", "");
        assertValidValues("subfamilyNumber", softwareValues);
    }
}
