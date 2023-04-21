package br.com.visualmidia.business;

import java.util.Calendar;
import java.util.List;
import junit.framework.TestCase;

/**
 * @author   Lucas
 */
public class CashFlowItemTest extends TestCase {

    private CashFlowItem _cashFlowItem;

    @SuppressWarnings("unchecked")
    protected void setUp() throws Exception {
        super.setUp();
        _cashFlowItem = new CashFlowItem();
    }

    public void testMethodsAutentication() {
        initialValuesTest();
        flowValuesTest();
        zoomItemsCreditValuesTest();
        zoomItemsDebitValuesTest();
    }

    private void zoomItemsDebitValuesTest() {
        String value;
        Expenditure expenditure = new Expenditure("1", "teste", new GDDate(), 1, 0, new Money(0.65), true, "2.1.1", "");
        _cashFlowItem.addZoomDebit(expenditure);
        value = new Money(-0.65).getFormatedValue();
        assertEquals(value, getTotalZoomItem(_cashFlowItem.getZoomToPay()));
        value = new Money(0.3 + 2.7 + 3.5 - 0.65).getFormatedValue();
        assertEquals(value, getTotalZoomItem(_cashFlowItem.getZoomTotal()));
    }

    private void zoomItemsCreditValuesTest() {
        String value;
        Incoming incoming = new Incoming("1", "teste", new GDDate(), 1, 0, new Money(0.3), true, "1.1.1", "");
        _cashFlowItem.addZoomCredit(incoming);
        assertEquals("0,30", _cashFlowItem.getZoomToReceive().get(0).getValue());
        value = new Money(0.3).getFormatedValue();
        assertEquals(value, getTotalZoomItem(_cashFlowItem.getZoomTotal()));
        Person person = new Person("1", "Laudison");
        Account account = new Account("1", "teste", new GDDate("15/12/2005"), person);
        Operation operation = new Operation("1", account, "1.1.1", new GDDate("15/12/1996"), "teste", 2.7f);
        _cashFlowItem.addZoomCredit(operation);
        value = new Money(2.7).getFormatedValue();
        value = new Money(0.3 + 2.7).getFormatedValue();
        assertEquals(value, getTotalZoomItem(_cashFlowItem.getZoomTotal()));
        Parcel parcel = new Parcel("15/10/2006", 3.5f, 2.5f, 0.0f, 0.0f, 0.0f, 3.5f);
        parcel.setPayDate(new GDDate("29/09/2006"));
        parcel.setPayed(true);
        parcel.setValueBeforeParcelDateExpiration(3.5f);
        _cashFlowItem.addZoomCredit(parcel, person);
        value = new Money(2.7 + 3.5).getFormatedValue();
        value = new Money(0.3 + 2.7 + 3.5).getFormatedValue();
        assertEquals(value, getTotalZoomItem(_cashFlowItem.getZoomTotal()));
    }

    private void flowValuesTest() {
        assertEquals("-2,50", _cashFlowItem.getFlowPaid().getFormatedValue());
        _cashFlowItem.addFlowToPay(new Money(2.7));
        assertEquals("-2,70", _cashFlowItem.getFlowToPay().getFormatedValue());
        assertEquals("3,70", _cashFlowItem.getFlowReceived().getFormatedValue());
        _cashFlowItem.addFlowToReceive(new Money(5.5));
        assertEquals("5,50", _cashFlowItem.getFlowToReceive().getFormatedValue());
        String value = new Money(2.5 - 2.7 - 3.7 + 5.5).getFormatedValue();
        assertEquals(value, _cashFlowItem.getFlowTotal().getFormatedValue());
    }

    private void initialValuesTest() {
        assertEquals("0,00", _cashFlowItem.getFlowPaid().getFormatedValue());
        assertEquals("0,00", _cashFlowItem.getFlowToPay().getFormatedValue());
        assertEquals("0,00", _cashFlowItem.getFlowReceived().getFormatedValue());
        assertEquals("0,00", _cashFlowItem.getFlowToReceive().getFormatedValue());
        assertEquals("0,00", _cashFlowItem.getFlowTotal().getFormatedValue());
    }

    private String getTotalZoomItem(List<ZoomItem> list) {
        Money value = new Money("0");
        for (ZoomItem zoomItem : list) {
            if (zoomItem.isCredit()) value.credit(new Money(zoomItem.getValue())); else value.debit(new Money(zoomItem.getValue()));
        }
        return value.getFormatedValue();
    }
}
