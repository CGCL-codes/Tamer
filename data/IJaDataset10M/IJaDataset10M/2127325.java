package com.tomgibara.money;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import junit.framework.TestCase;

public class MoneySplitterTest extends TestCase {

    private static List<Money> list(Money[] money) {
        return Arrays.asList(money);
    }

    public void testSplit() {
        Random r = new Random(0L);
        MoneyType type = new MoneyType(Locale.US);
        for (int i = 0; i < 100; i++) {
            Money money = type.money(r.nextInt(1000));
            for (int j = 0; j < 10; j++) {
                int parts = 1 + r.nextInt(10);
                for (int k = 0; k < 3; k++) {
                    testSplit(money.calc(k, null), parts);
                }
            }
        }
    }

    private void testSplit(MoneyCalc calc, int parts) {
        Money[] split = calc.splitter().setParts(parts).split();
        calc = calc.calc();
        for (Money m : split) calc.subtract(m);
        assertTrue(calc.getAmount().signum() == 0);
    }

    public void testSplitProportions() {
        MoneyType type = new MoneyType(Locale.US);
        Money money = type.money(100);
        MoneyCalc calc = money.calc(2, null);
        Money[] ms;
        ms = calc.splitter().setProportions(BigDecimal.valueOf(5)).split();
        assertEquals(money, ms[0]);
        ms = calc.splitter().setProportions(BigDecimal.valueOf(1), BigDecimal.valueOf(1)).split();
        assertEquals(type.money(50), ms[0]);
        assertEquals(type.money(50), ms[1]);
        ms = calc.splitter().setProportions(BigDecimal.valueOf(1), BigDecimal.valueOf(4)).split();
        assertEquals(type.money(20), ms[0]);
        assertEquals(type.money(80), ms[1]);
        ms = calc.splitter().setProportions(BigDecimal.valueOf(4), BigDecimal.valueOf(1)).split();
        assertEquals(type.money(80), ms[0]);
        assertEquals(type.money(20), ms[1]);
        ms = calc.splitter().setProportions(BigDecimal.valueOf(1), BigDecimal.valueOf(9), BigDecimal.valueOf(90)).split();
        assertEquals(type.money(1), ms[0]);
        assertEquals(type.money(9), ms[1]);
        assertEquals(type.money(90), ms[2]);
        ms = calc.splitter().setProportions(BigDecimal.valueOf(1), BigDecimal.valueOf(1000)).split();
        assertEquals(type.money(0), ms[0]);
        assertEquals(type.money(100), ms[1]);
    }

    public void testSplitZeros() {
        MoneyType type = new MoneyType(Locale.US);
        Money zero = type.calc(2, null).money();
        Money one = type.money(1).calc(2, null).money();
        MoneyCalc calc = one.calc(2, null);
        assertEquals(list(new Money[] { zero, zero, one }), list(calc.splitter().setProportions(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE).split()));
        assertEquals(list(new Money[] { zero, one, zero }), list(calc.splitter().setProportions(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ZERO).split()));
        assertEquals(list(new Money[] { one, zero, zero }), list(calc.splitter().setProportions(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO).split()));
    }
}
