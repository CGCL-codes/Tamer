package org.openxava.invoicing.tests;

import org.openxava.tests.*;

public class CustomerOrdersTest extends ModuleTestBase {

    public CustomerOrdersTest(String testName) {
        super(testName, "Invoicing", "CustomerOrders");
    }

    public void testLimitingDataVisibility() throws Exception {
        login("lulu@invoicing.com", "lulu");
        assertListNotEmpty();
        int rowCount = getListRowCount();
        for (int row = 0; row < rowCount; row++) {
            assertValueInList(row, "customer.name", "LULU SEMUA");
        }
        execute("CustomerOrders.new");
        assertNoEditable("customer.number");
        assertValue("customer.name", "LULU SEMUA");
        logout();
    }
}
