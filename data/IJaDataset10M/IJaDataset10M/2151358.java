package fireteam.interfaces;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Field;
import java.util.Comparator;
import javax.swing.border.Border;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.java.javafx.ui.UIContextImpl;

/**
 *
 * @author Tolik1
 */
public class MyComparator implements Comparator {

    public static interface ICells {

        public String getValue(int cellIndex);
    }

    public TableModel m_model;

    public ICells m_cells;

    public MyComparator(TableModel model, ICells cells) {
        m_cells = cells;
        m_model = model;
    }

    public int compare(Object o1, Object o2) {
        int iRow1 = -1, iCol1 = -1;
        int iRow2 = -1, iCol2 = -1;
        for (int i = 0; i < m_model.getRowCount(); i++) {
            for (int j = 0; j < m_model.getColumnCount(); j++) {
                Object o3 = m_model.getValueAt(i, j);
                if (o3.equals(o1)) {
                    iRow1 = i;
                    iCol1 = j;
                }
                if (o3.equals(o2)) {
                    iRow2 = i;
                    iCol2 = j;
                }
                if (iRow1 != -1 && iRow2 != -1 && iCol1 != -1 && iCol2 != -1) break;
            }
            if (iRow1 != -1 && iRow2 != -1 && iCol1 != -1 && iCol2 != -1) break;
        }
        int iCell1 = iRow1 * m_model.getColumnCount() + iCol1;
        int iCell2 = iRow2 * m_model.getColumnCount() + iCol2;
        String sCel1 = extractString(m_cells.getValue(iCell1));
        String sCel2 = extractString(m_cells.getValue(iCell2));
        return ((Comparable) sCel1).compareTo(sCel2);
    }

    String extractString(String sStr) {
        int i = sStr.indexOf("</");
        if (i == -1) return sStr;
        sStr = sStr.substring(0, i);
        i = sStr.lastIndexOf(">");
        return sStr.substring(i + 1);
    }
}
