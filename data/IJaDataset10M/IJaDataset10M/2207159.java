package org.openxava.test.tests;

import org.openxava.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class DeliveryTypesTest extends ModuleTestBase {

    public DeliveryTypesTest(String testName) {
        super(testName, "OpenXavaTest", "DeliveryTypes");
    }

    public void testSaveActionNotResetRefreshData() throws Exception {
        execute("CRUD.new");
        setValue("number", "66");
        setValue("description", "JUNIT");
        execute("DeliveryTypes.saveNotReset");
        assertNoErrors();
        assertValue("number", "66");
        assertValue("description", "JUNIT CREATED");
        assertNoEditable("number");
        assertEditable("description");
        execute("DeliveryTypes.saveNotReset");
        assertValue("number", "66");
        assertValue("description", "JUNIT CREATED MODIFIED");
        assertNoEditable("number");
        assertEditable("description");
        execute("CRUD.delete");
        assertNoErrors();
        assertMessage("Delivery type deleted successfully");
    }

    public void testPostmodifiyCalculatorNotOnRead() throws Exception {
        assertListNotEmpty();
        execute("Mode.detailAndFirst");
        assertNoErrors();
        String description = getValue("description");
        assertTrue("Description must have value", !Is.emptyString(description));
        execute("Mode.list");
        assertNoErrors();
        execute("Mode.detailAndFirst");
        assertNoErrors();
        assertValue("description", description);
    }

    public void testRemoveValidator_postcreateCalculator_postmodifyCalculator() throws Exception {
        execute("CRUD.new");
        setValue("number", "66");
        setValue("description", "JUNIT");
        execute("CRUD.save");
        assertNoErrors();
        setValue("number", "66");
        execute("CRUD.search");
        assertValue("number", "66");
        assertValue("description", "JUNIT CREATED");
        setValue("description", "JUNIT");
        execute("CRUD.save");
        assertNoErrors();
        setValue("number", "66");
        execute("CRUD.search");
        assertValue("number", "66");
        assertValue("description", "JUNIT MODIFIED");
        Delivery delivery = new Delivery();
        Invoice invoice = new Invoice();
        invoice.setYear(2002);
        invoice.setNumber(1);
        delivery.setInvoice(invoice);
        DeliveryType deliveryType = new DeliveryType();
        deliveryType.setNumber(66);
        delivery.setType(deliveryType);
        delivery.setNumber(66);
        delivery.setDescription("JUNIT FOR DELIVERY TYPE");
        XHibernate.getSession().save(delivery);
        XHibernate.commit();
        execute("CRUD.delete");
        assertError("Delivery type can not delete because it is used in deliveries");
        assertEditable("description");
        XHibernate.getSession().delete(delivery);
        XHibernate.commit();
        execute("CRUD.delete");
        assertNoErrors();
        assertMessage("Delivery type deleted successfully");
    }
}
