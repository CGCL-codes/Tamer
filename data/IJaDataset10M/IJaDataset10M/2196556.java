package test.de.glossmaker.gui.datastructure;

import junit.framework.Assert;
import org.junit.Test;
import de.glossmaker.gloss.datastructure.gui.GuiTable;

public class TestCaseGuiTable {

    @Test
    public void testSetColumnWidth() {
        GuiTable gt = new GuiTable("table", 2);
        gt.setColumnWidth(0, 100);
        gt.setColumnWidth(1, 200);
        Assert.assertEquals(100, gt.getColumnWidth(0));
        Assert.assertEquals(200, gt.getColumnWidth(1));
        Assert.assertEquals(2, gt.size());
        Assert.assertEquals("table", gt.getTableName());
    }

    @Test
    public void testMoveColumn() {
        GuiTable gt = new GuiTable("table", 3);
        gt.setColumnWidth(0, 100);
        gt.setColumnWidth(1, 200);
        gt.setColumnWidth(2, 300);
        gt.moveColumn(0, 2);
        Assert.assertEquals(100, gt.getColumnWidth(0));
        Assert.assertEquals(200, gt.getColumnWidth(1));
        Assert.assertEquals(300, gt.getColumnWidth(2));
        Assert.assertEquals(1, gt.getColumnPosition(0));
        Assert.assertEquals(2, gt.getColumnPosition(1));
        Assert.assertEquals(0, gt.getColumnPosition(2));
        Assert.assertEquals(3, gt.size());
        Assert.assertEquals("table", gt.getTableName());
    }
}
