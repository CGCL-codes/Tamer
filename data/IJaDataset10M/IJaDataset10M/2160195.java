package net.sf.javadc.gui.model;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import junit.framework.TestCase;

/**
 * @author Timo Westk�mper
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TableModelFilterDecoratorTest extends TestCase {

    private String[][] content = { { "aAa", "bbb", "ccc" }, { "AAB", "bbc", "cccd" }, { "abc", "abc", "abc" } };

    private TableModel tableModel = new MyTableModel(content);

    private TableModelFilterDecorator filteredTableModel = new TableModelFilterDecorator(tableModel);

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testBasic() throws Exception {
        assertTrue(tableModel.getRowCount() == filteredTableModel.getRowCount());
        assertEquals(tableModel.getValueAt(0, 0), filteredTableModel.getValueAt(0, 0));
    }

    public void testFiltering1() {
        filteredTableModel.addFilter(0, "b");
        assertEquals(filteredTableModel.getRowCount(), 2);
        assertEquals(filteredTableModel.getValueAt(0, 0), "AAB");
        assertEquals(filteredTableModel.getValueAt(1, 0), "abc");
    }

    public void testFiltering2() {
        filteredTableModel.addFilter(0, "AA");
        assertEquals(filteredTableModel.getRowCount(), 2);
    }

    /** ********************************************************************** */
    private class MyTableModel extends AbstractTableModel {

        String[][] content;

        public MyTableModel(String[][] _content) {
            content = _content;
        }

        public int getColumnCount() {
            return content[0].length;
        }

        public int getRowCount() {
            return content.length;
        }

        public Object getValueAt(int arg0, int arg1) {
            return content[arg0][arg1];
        }
    }
}
