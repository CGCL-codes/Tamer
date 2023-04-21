package org.openxava.invoicing.tests;

import org.openxava.tests.*;

public class AuthorTest extends ModuleTestBase {

    public AuthorTest(String testName) {
        super(testName, "Invoicing", "Author");
    }

    public void testReadAuthor() throws Exception {
        assertValueInList(0, 0, "JAVIER CORCOBADO");
        execute("Mode.detailAndFirst");
        assertValue("name", "JAVIER CORCOBADO");
        assertCollectionRowCount("products", 2);
        assertValueInCollection("products", 0, "number", "2");
        assertValueInCollection("products", 0, "description", "Arco iris de lágrimas");
        assertValueInCollection("products", 1, "number", "3");
        assertValueInCollection("products", 1, "description", "Ritmo de sangre");
    }
}
