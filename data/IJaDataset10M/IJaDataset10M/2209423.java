package com.entelience.test.test05util;

import org.junit.*;
import static org.junit.Assert.*;
import com.entelience.util.Excel;

public class test09Excel {

    /**
     * Simple test to be sure my regexp is OK
     *
     */
    @Test
    public void test01_NewSheet() throws Exception {
        Excel ex = new Excel(null);
        ex.newWorkBook();
        ex.newSheet("1234567891234567891234567891234567899");
        ex = new Excel(null);
        ex.newWorkBook();
        ex.newSheet(null);
        ex = new Excel(null);
        ex.newWorkBook();
        ex.newSheet("Some \\ forbidden chars ");
        ex = new Excel(null);
        ex.newWorkBook();
        ex.newSheet("Some forbidden chars ? ");
        ex = new Excel(null);
        ex.newWorkBook();
        ex.newSheet("Some  forbidden * chars ");
        ex = new Excel(null);
        ex.newWorkBook();
        ex.newSheet("Some [ forbidden] chars ");
        ex = new Excel(null);
        ex.newWorkBook();
        ex.newSheet("Some  /\\*?[]  forbidden chars ");
    }
}
