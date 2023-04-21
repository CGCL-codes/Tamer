package org.doframework.sample.jdbc_app.test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import junit.framework.TestCase;
import org.doframework.sample.jdbc_app.GlobalContext;
import org.doframework.sample.jdbc_app.entity.Customer;
import org.doframework.sample.jdbc_app.entity.Invoice;
import org.doframework.sample.jdbc_app.entity.Payment;
import org.doframework.sample.jdbc_app.factory.CustomerFactory;

/**
 * User: gordonju Date: Jan 11, 2008 Time: 8:04:30 PM
 */
public abstract class AccountingTest extends TestCase {

    /**
     * Sets up the fixture, for example, open a network connection. This method is called before a
     * test is executed.
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void CanDeleteTestAddCustomerRecord() {
        final String lastName = "Gordon" + System.currentTimeMillis();
        Customer createdCustomer = CustomerTestUtils.createCustomerRecordWithNamePhoneNumber(lastName, "8085551212");
        Customer fetchedCustomer = getCustomerComponent().getById(createdCustomer.getId());
        assertNotNull(fetchedCustomer);
        assertEquals(lastName, fetchedCustomer.getName());
        assertEquals(createdCustomer.getId(), fetchedCustomer.getId());
    }

    public void CanDeleteTestDeleteCustomerRecord() {
        final String lastName = "Smith";
        Customer customer = CustomerTestUtils.createCustomerRecordWithNamePhoneNumber(lastName, "8085551212");
        assertNotNull(getCustomerComponent().getById(customer.getId()));
        customer.delete();
        assertNull(getCustomerComponent().getById(customer.getId()));
    }

    public void CanDeleteTestCreateCustomerRecordThrowsExceptionWhenDuplicateCustomerCreated() {
        String name = "Higgins" + System.currentTimeMillis();
        CustomerTestUtils.createCustomerRecordWithNamePhoneNumber(name, "8085551212");
        try {
            CustomerTestUtils.createCustomerRecordWithNamePhoneNumber(name, "8085551212");
            fail("Expected duplicate customer exception");
        } catch (Exception e) {
        }
    }

    public void DOF_Donald_testAddChargeToCustomerIncreasesCustomersBalance() {
        String name = "Slater" + System.currentTimeMillis();
        CustomerTestUtils.createInvoiceForNewCustomer(name, new Date(), new BigDecimal("12.34"));
    }

    public void DOF_Yes_testGetInvoicesReturnsInvoicesForCustomerInChronologicalOrder() {
        BigDecimal invoiceAmount1 = new BigDecimal("11");
        BigDecimal invoiceAmount2 = new BigDecimal("12");
        BigDecimal invoiceAmount3 = new BigDecimal("13");
        Date today = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, -1);
        Date oneMonthsAgo = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        Date twoMonthsAgo = calendar.getTime();
        Customer lopez = CustomerTestUtils.createInvoiceForNewCustomer("Lopez" + System.currentTimeMillis(), today, invoiceAmount1);
        CustomerTestUtils.createInvoiceWithAmountForCustomer(lopez, twoMonthsAgo, invoiceAmount2);
        CustomerTestUtils.createInvoiceWithAmountForCustomer(lopez, oneMonthsAgo, invoiceAmount3);
        List<Invoice> list = lopez.getInvoices();
        assertEquals(invoiceAmount2, list.get(0).getSubTotal());
        assertEquals(invoiceAmount3, list.get(1).getSubTotal());
        assertEquals(invoiceAmount1, list.get(2).getSubTotal());
    }

    public void DOF_Donald_testRecordPaymentDecreasesCustomerBalanceAndLastInvoicePendingAmount() {
        BigDecimal invoiceAmount = new BigDecimal("12.34");
        Customer irons = CustomerTestUtils.createInvoiceForNewCustomer("Irons" + System.currentTimeMillis(), new Date(), invoiceAmount);
        BigDecimal originalBalance = irons.getBalance();
        Payment newPayment = new Payment();
        final BigDecimal paymentAmount = new BigDecimal("11.00");
        newPayment.setAmount(paymentAmount);
        newPayment.setCustomer(irons);
        newPayment.persist();
        BigDecimal invoiceBalance = originalBalance.subtract(paymentAmount);
        assertEquals(invoiceBalance, irons.getBalance());
        List<Invoice> invoices = irons.getInvoices();
        Invoice invoice = invoices.get(0);
        assertEquals(invoiceBalance, invoice.getPendingBalance());
    }

    public void DOF_Donald_testRecordPaymentDecreasesCustomerBalanceAndLastTwoInvoicePendingAmounts() {
        BigDecimal invoiceAmount1 = new BigDecimal("11");
        BigDecimal invoiceAmount2 = new BigDecimal("12");
        BigDecimal invoiceAmount3 = new BigDecimal("13");
        Date today = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MONTH, -1);
        Date oneMonthsAgo = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        Date twoMonthsAgo = calendar.getTime();
        String uniqueName = "Potter" + System.currentTimeMillis();
        Customer potter = CustomerTestUtils.createInvoiceForNewCustomer(uniqueName, today, invoiceAmount1);
        CustomerTestUtils.createInvoiceWithAmountForCustomer(potter, twoMonthsAgo, invoiceAmount2);
        CustomerTestUtils.createInvoiceWithAmountForCustomer(potter, oneMonthsAgo, invoiceAmount3);
        BigDecimal originalBalance = potter.getBalance();
        Payment newPayment = new Payment();
        final BigDecimal paymentAmount = new BigDecimal("26.00");
        newPayment.setAmount(paymentAmount);
        newPayment.setCustomer(potter);
        newPayment.persist();
        BigDecimal remainingInvoiceBalance = originalBalance.subtract(paymentAmount);
        assertEquals(remainingInvoiceBalance, potter.getBalance());
        List<Invoice> invoices = potter.getInvoices();
        assertEquals(BigDecimal.ZERO, invoices.get(0).getPendingBalance());
        assertEquals(BigDecimal.ZERO, invoices.get(1).getPendingBalance());
        assertEquals(potter.getBalance(), invoices.get(2).getPendingBalance());
    }

    public CustomerFactory getCustomerComponent() {
        return GlobalContext.getFactoryLookupService().getCustomerFactory();
    }
}
