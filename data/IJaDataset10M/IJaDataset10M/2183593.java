package org.openxava.test.tests;

import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class TransportChargesTest extends TransportChargesTestBase {

    public TransportChargesTest(String testName) {
        super(testName, "TransportCharges");
    }

    public void testKeyNestedReferences() throws Exception {
        deleteAll();
        execute("CRUD.new");
        execute("Reference.search", "keyProperty=xava.TransportCharge.delivery.number");
        String year = getValueInList(0, 0);
        String number = getValueInList(0, 1);
        assertTrue("It is required that year in invoice has value", !Is.emptyString(year));
        assertTrue("It is required that number in invoice has value", !Is.emptyString(number));
        execute("ReferenceSearch.choose", "row=0");
        assertNoErrors();
        assertValue("delivery.invoice.year", year);
        assertValue("delivery.invoice.number", number);
        setValue("amount", "666");
        execute("CRUD.save");
        assertNoErrors();
        assertValue("delivery.invoice.year", "");
        assertValue("delivery.invoice.number", "");
        assertValue("amount", "");
        execute("Mode.list");
        execute("Mode.detailAndFirst");
        assertNoErrors();
        assertValue("delivery.invoice.year", year);
        assertValue("delivery.invoice.number", number);
        assertValue("amount", "666");
        setValue("amount", "777");
        execute("CRUD.save");
        assertNoErrors();
        assertValue("delivery.invoice.year", "");
        assertValue("delivery.invoice.number", "");
        assertValue("amount", "");
        execute("Mode.list");
        execute("Mode.detailAndFirst");
        assertNoErrors();
        assertValue("delivery.invoice.year", year);
        assertValue("delivery.invoice.number", number);
        assertValue("amount", "777");
        execute("CRUD.delete");
        assertMessage("TransportCharge deleted successfully");
    }

    public void testKeyNestedReferencesInList() throws Exception {
        deleteAll();
        createSome();
        execute("List.filter");
        execute("List.orderBy", "property=amount");
        assertListRowCount(2);
        assertValueInList(0, 0, String.valueOf(getCharge1().getDelivery().getInvoice().getYear()));
        assertValueInList(0, 1, String.valueOf(getCharge1().getDelivery().getInvoice().getNumber()));
        assertValueInList(0, 2, String.valueOf(getCharge1().getDelivery().getNumber()));
        assertValueInList(0, 3, "100");
        assertValueInList(1, 0, String.valueOf(getCharge2().getDelivery().getInvoice().getYear()));
        assertValueInList(1, 1, String.valueOf(getCharge2().getDelivery().getInvoice().getNumber()));
        assertValueInList(1, 2, String.valueOf(getCharge2().getDelivery().getNumber()));
        assertValueInList(1, 3, "200");
        String[] condition = { String.valueOf(getCharge2().getDelivery().getInvoice().getYear()), String.valueOf(getCharge2().getDelivery().getInvoice().getNumber()), String.valueOf(getCharge2().getDelivery().getNumber()) };
        setConditionValues(condition);
        execute("List.filter");
        assertListRowCount(1);
        assertValueInList(0, 0, String.valueOf(getCharge2().getDelivery().getInvoice().getYear()));
        assertValueInList(0, 1, String.valueOf(getCharge2().getDelivery().getInvoice().getNumber()));
        assertValueInList(0, 2, String.valueOf(getCharge2().getDelivery().getNumber()));
        assertValueInList(0, 3, "200");
    }
}
