package com.xsky.common.logic;

import java.util.Calendar;
import junit.framework.TestCase;

public class BillNoGeneratorTest extends TestCase {

    public void testBillno() {
        String billno = BillNoGenerator.getBillNo();
        System.out.println(billno);
        assertEquals(billno.length(), 22);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int h = cal.get(Calendar.HOUR_OF_DAY);
        int m = cal.get(Calendar.MINUTE);
        StringBuilder sb = new StringBuilder();
        sb.append(year);
        sb.append(month > 9 ? month : "0" + month);
        sb.append(day > 9 ? day : "0" + day);
        sb.append(h > 9 ? h : "0" + h);
        sb.append(m > 9 ? m : "0" + m);
        assertEquals(sb.toString(), billno.substring(0, 12));
    }
}
