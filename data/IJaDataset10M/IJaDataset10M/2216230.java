package org.nakedobjects.applib.valueholder;

public class MoneyTest extends ValueTestCase {

    public void testSaveRestore() throws Exception {
        Money money1 = new Money();
        money1.parseUserEntry("2003-1-4");
        assertFalse(money1.isEmpty());
        Money money2 = new Money();
        money2.restoreFromEncodedString(money1.asEncodedString());
        assertEquals(money1.floatValue(), money2.floatValue(), 0.0);
        assertFalse(money2.isEmpty());
    }

    public void testSaveRestorOfNull() throws Exception {
        Money money1 = new Money();
        money1.clear();
        assertTrue("Money isEmpty", money1.isEmpty());
        Money money2 = new Money();
        money2.restoreFromEncodedString(money1.asEncodedString());
        assertEquals(money1.floatValue(), money2.floatValue(), 0.0);
        assertTrue(money2.isEmpty());
    }

    public void testComparisons() {
        Money money = new Money(5.00);
        assertTrue("> 1", money.isGreaterThan(1.00));
        assertTrue(">= 5", money.isGreaterThanOrEqualTo(5.00));
        assertTrue(">= 4.9", money.isGreaterThanOrEqualTo(4.90));
        assertTrue("not >= 5.1", !money.isGreaterThanOrEqualTo(5.10));
        assertTrue("< 5.1", money.isLessThan(5.10));
        assertTrue("<= 5.1", money.isLessThanOrEqualTo(5.10));
        assertTrue("<= 5", money.isLessThanOrEqualTo(5.00));
    }
}
