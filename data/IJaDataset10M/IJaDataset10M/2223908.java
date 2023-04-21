package test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import pelore.MoneyLoan;

public class MoneyLoanTest {

    MoneyLoan moneyLoan1, moneyLoan2, moneyLoan3;

    @Before
    public void setUp() {
        try {
            moneyLoan1 = new MoneyLoan(BigDecimal.valueOf(100.50));
            moneyLoan2 = new MoneyLoan(moneyLoan1);
            moneyLoan3 = new MoneyLoan(BigDecimal.valueOf(-1.0));
            fail("ArithmeticException fail");
        } catch (ArithmeticException ex) {
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMoneyLoan() {
        assertEquals(BigDecimal.valueOf(100.50), moneyLoan1.getAmount());
        assertEquals(BigDecimal.valueOf(100.50), moneyLoan2.getAmount());
    }

    @Test
    public void testSetAmount() {
        assertEquals(BigDecimal.valueOf(100.50), moneyLoan1.getAmount());
        moneyLoan1.setAmount(BigDecimal.valueOf(101.30));
        assertEquals(BigDecimal.valueOf(101.30), moneyLoan1.getAmount());
    }

    @Test
    public void testSumAmount() {
        assertEquals(BigDecimal.valueOf(100.50), moneyLoan2.getAmount());
        moneyLoan2.sumAmount(BigDecimal.valueOf(1.35));
        assertEquals(BigDecimal.valueOf(101.85), moneyLoan2.getAmount());
    }

    @Test
    public void testSubtractAmount() {
        try {
            assertEquals(BigDecimal.valueOf(101.85), moneyLoan2.getAmount());
            moneyLoan2.subtractAmount(BigDecimal.valueOf(1.85));
            assertEquals(BigDecimal.valueOf(100), moneyLoan2.getAmount());
            moneyLoan2.subtractAmount(BigDecimal.valueOf(120));
            fail("IllegalArgumentException fail");
        } catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testEquals() {
        MoneyLoan moneyLoan4 = new MoneyLoan(BigDecimal.valueOf(100));
        assertFalse(moneyLoan1.equals(moneyLoan4));
        assertTrue(moneyLoan2.equals(moneyLoan4));
    }

    @Test
    public void testToString() {
        assertEquals("101,30", moneyLoan1.toString());
    }
}
