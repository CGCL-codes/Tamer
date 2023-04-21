package test.de.glossmaker.gui.bibtex.model;

import java.util.Locale;
import junit.framework.Assert;
import org.junit.Test;
import de.glossmaker.bib.datastructure.BibItems;
import de.glossmaker.bib.datastructure.Booklet;
import de.glossmaker.gui.bibtex.model.BookletTableModel;

public class TestCaseBookletTableModel {

    private BookletTableModel mModel = null;

    private Booklet mItem = null;

    public TestCaseBookletTableModel() {
        mItem = new Booklet("key", "title");
        Locale loc = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);
        BibItems items = new BibItems();
        items.add(mItem);
        mModel = new BookletTableModel(items, mItem);
        Locale.setDefault(loc);
    }

    @Test
    public void testRowCount() {
        Assert.assertEquals(11, mModel.getRowCount());
    }

    @Test
    public void testOptionalRows() {
        Assert.assertEquals(4, mModel.getOptionalRow());
    }

    @Test
    public void testColumnCount() {
        Assert.assertEquals(2, mModel.getColumnCount());
    }

    @Test
    public void testColumnNames() {
        Assert.assertEquals("Description", mModel.getColumnName(0));
        Assert.assertEquals("Value", mModel.getColumnName(1));
    }

    @Test
    public void testGetValueAt() {
        Assert.assertEquals(mItem.getKey(), mModel.getValueAt(1, 1));
        Assert.assertEquals(mItem.getTitle(), mModel.getValueAt(2, 1));
        Assert.assertEquals(mItem.getAuthor(), mModel.getValueAt(5, 1));
        Assert.assertEquals(mItem.getHowPublished(), mModel.getValueAt(6, 1));
        Assert.assertEquals(mItem.getAddress(), mModel.getValueAt(7, 1));
        Assert.assertEquals(mItem.getYear(), mModel.getValueAt(8, 1));
        Assert.assertEquals(mItem.getMonth(), mModel.getValueAt(9, 1));
        Assert.assertEquals(mItem.getNote(), mModel.getValueAt(10, 1));
    }
}
