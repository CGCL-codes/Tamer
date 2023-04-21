package org.xBaseJ.test;

import junit.framework.TestCase;
import org.xBaseJ.DBF;
import org.xBaseJ.Util;
import org.xBaseJ.fields.CharField;

public class testMaxLength extends TestCase {

    public void testMaxFieldLength() {
        try {
            Util.setxBaseJProperty("ignoreDBFLengthCheck", "true");
            DBF d1 = new DBF("testfiles/test.dbf", true);
            for (int i = 0; i < 50; i++) {
                CharField c = new CharField("C" + i, 100);
                d1.addField(c);
            }
            d1.close();
            d1 = new DBF("testfiles/a.dbf");
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
