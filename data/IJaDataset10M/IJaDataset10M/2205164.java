package net.sf.echobinding.controls;

import junit.framework.TestCase;
import net.sf.echobinding._TestHelper;
import net.sf.echobinding.binding.OgnlBindingContext;
import net.sf.echobinding.binding.OgnlPropertyAdapter;
import net.sf.echobinding.testbeans.Customer;
import net.sf.echobinding.testbeans.PaymentMethod;

/**
 *
 */
public class _SelectFieldTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSelectField() {
        _TestHelper testUtil = new _TestHelper();
        Customer customer = _TestHelper.createCustomer();
        PaymentMethod pmVisa = new PaymentMethod("Visa", null);
        PaymentMethod pmMaster = new PaymentMethod("Master", null);
        PaymentMethod pmAmex = new PaymentMethod("Amex", null);
        testUtil.createPaymentMethodsList(pmVisa, pmMaster, pmAmex);
        customer.setPaymentMethod(pmVisa);
        assertEquals(pmVisa, customer.getPaymentMethod());
        OgnlBindingContext ctx = new OgnlBindingContext(customer);
        ctx.addModel("util", testUtil);
        ctx.add("allPaymentMethods", new OgnlPropertyAdapter("#util.allPaymentMethods"));
        SelectField sf = new SelectField("paymentMethod", "allPaymentMethods", ctx);
        sf.update();
        sf.setSelectedIndex(2);
        sf.save();
        assertEquals(pmMaster, customer.getPaymentMethod());
    }

    public void testGetAndSetValue() {
        _TestHelper testUtil = new _TestHelper();
        Customer customer = _TestHelper.createCustomer();
        PaymentMethod pmVisa = new PaymentMethod("Visa", null);
        PaymentMethod pmMaster = new PaymentMethod("Master", null);
        PaymentMethod pmAmex = new PaymentMethod("Amex", null);
        testUtil.createPaymentMethodsList(pmVisa, pmMaster, pmAmex);
        customer.setPaymentMethod(pmVisa);
        OgnlBindingContext ctx = new OgnlBindingContext(customer);
        ctx.addModel("helper", testUtil);
        ctx.add("allPaymentMethods", new OgnlPropertyAdapter("#helper.allPaymentMethods"));
        SelectField sf = new SelectField("paymentMethod", "allPaymentMethods", ctx);
        sf.update();
        assertEquals("(A1.1)", pmVisa, customer.getPaymentMethod());
        assertEquals("(A1.2)", pmVisa, sf.getValue());
        sf.setValue(pmMaster);
        assertEquals("(A2.1)", pmMaster, sf.getValue());
        sf.save();
        assertEquals("(A2.2)", sf.getValue(), customer.getPaymentMethod());
        sf.setValue(null);
        sf.save();
        assertEquals("A3.1", null, customer.getPaymentMethod());
    }
}
