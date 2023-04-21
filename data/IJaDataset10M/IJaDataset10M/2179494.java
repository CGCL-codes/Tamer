package test.de.glossmaker.gui.gloss.main.tableview;

import java.util.Locale;
import junit.framework.Assert;
import org.junit.Test;
import de.glossmaker.gloss.datastructure.GlossItem;
import de.glossmaker.gloss.datastructure.GlossItems;
import de.glossmaker.gui.gloss.main.tableview.GlossItemTableModel;

public class TestCaseGlossItemTableModel {

    private GlossItemTableModel mModel = null;

    private GlossItem mItem;

    public TestCaseGlossItemTableModel() {
        GlossItems items = new GlossItems();
        mItem = new GlossItem("IPv4", "I", "IP", "Internet protocol");
        items.add(mItem);
        Locale loc = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
        mModel = new GlossItemTableModel(items);
        Locale.setDefault(loc);
    }

    @Test
    public void testRowCount() {
        Assert.assertEquals(1, mModel.getRowCount());
    }

    @Test
    public void testColumnCount() {
        Assert.assertEquals(4, mModel.getColumnCount());
    }

    @Test
    public void testColumnNames() {
        Assert.assertEquals("Key", mModel.getColumnName(0));
        Assert.assertEquals("Heading", mModel.getColumnName(1));
        Assert.assertEquals("Word", mModel.getColumnName(2));
        Assert.assertEquals("Definition", mModel.getColumnName(3));
    }

    @Test
    public void testGetValueAt() {
        Assert.assertEquals(mItem.getKey(), mModel.getValueAt(0, 0));
        Assert.assertEquals(mItem.getHeading(), mModel.getValueAt(0, 1));
        Assert.assertEquals(mItem.getWord(), mModel.getValueAt(0, 2));
        Assert.assertEquals(mItem.getDefinition(), mModel.getValueAt(0, 3));
    }

    @Test
    public void testIsCellEditable() {
        for (int i = 0; i < mModel.getColumnCount(); i++) {
            Assert.assertTrue(mModel.isCellEditable(0, i));
        }
    }
}
