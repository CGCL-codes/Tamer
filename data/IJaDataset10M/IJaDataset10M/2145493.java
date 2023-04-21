package org.openxava.invoicing.tests;

public class InvoiceTest extends CommercialDocumentTest {

    public InvoiceTest(String testName) {
        super(testName, "Invoice");
    }

    public void testAddOrders() throws Exception {
        assertListNotEmpty();
        execute("List.orderBy", "property=number");
        execute("Mode.detailAndFirst");
        String customerNumber = getValue("customer.number");
        deleteDetails();
        assertCollectionRowCount("details", 0);
        assertValue("baseAmount", "0.00");
        execute("Sections.change", "activeSection=1");
        assertCollectionRowCount("orders", 0);
        execute("Invoice.addOrders", "viewObject=xava_view_section1_orders");
        assertCustomerInList(customerNumber);
        assertValueForAllRows(5, "Yes");
        String firstOrderBaseAmount = getValueInList(0, 8);
        int ordersRowCount = getListRowCount();
        execute("AddOrdersToInvoice.add", "row=0");
        assertMessage("1 element(s) added to Orders of Invoice");
        assertCollectionRowCount("orders", 1);
        execute("Sections.change", "activeSection=0");
        assertCollectionNotEmpty("details");
        assertValue("baseAmount", firstOrderBaseAmount);
        execute("Sections.change", "activeSection=1");
        execute("Invoice.addOrders", "viewObject=xava_view_section1_orders");
        assertListRowCount(ordersRowCount - 1);
        execute("AddToCollection.cancel");
        checkRowCollection("orders", 0);
        execute("Collection.removeSelected", "viewObject=xava_view_section1_orders");
        assertCollectionRowCount("orders", 0);
    }

    private void deleteDetails() throws Exception {
        int c = getCollectionRowCount("details");
        for (int i = 0; i < c; i++) {
            checkRowCollection("details", i);
        }
        execute("Collection.removeSelected", "viewObject=xava_view_section0_details");
    }
}
