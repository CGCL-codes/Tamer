package com.google.code.ptrends.excel.caching;

import junit.framework.Assert;
import org.junit.Test;
import com.google.code.ptrends.excel.caching.CellStylePool;
import com.google.code.ptrends.excel.entities.CellStyle;

public class CellStylePoolTest {

    @Test
    public void getCellStyle() {
        CellStyle cellStyle = CellStylePool.getCellStyle("FFFFFF", "000000", (short) 11, true, true, true);
        Assert.assertNotNull(cellStyle);
        Assert.assertEquals("FFFFFF", cellStyle.getBackground());
        Assert.assertEquals("000000", cellStyle.getForeground());
        Assert.assertEquals(11, cellStyle.getFontSize());
        Assert.assertTrue(cellStyle.isBold());
        Assert.assertTrue(cellStyle.isItalic());
        Assert.assertTrue(cellStyle.isUnderlined());
    }

    @Test
    public void getSameCellStyleTwice_sameHashCode() {
        CellStyle cs1 = CellStylePool.getCellStyle("FFFFFF", "000000", (short) 11, true, true, true);
        CellStyle cs2 = CellStylePool.getCellStyle("FFFFFF", "000000", (short) 11, true, true, true);
        Assert.assertEquals(cs1, cs2);
        Assert.assertEquals(cs1.hashCode(), cs2.hashCode());
    }

    @Test
    public void getDifferentCellStyle_hashCodeDiffers() {
        CellStyle cs1 = CellStylePool.getCellStyle("FFFFFF", "000001", (short) 11, true, true, true);
        CellStyle cs2 = CellStylePool.getCellStyle("FFFFFF", "000000", (short) 12, true, true, true);
        Assert.assertFalse(cs1.equals(cs2));
        Assert.assertFalse(cs1.hashCode() == cs2.hashCode());
    }
}
