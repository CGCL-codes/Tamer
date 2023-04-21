package fontview.fontlist;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import javax.swing.table.AbstractTableModel;
import org.jsresources.apps.jmvp.model.ModelEvent;
import org.jsresources.apps.jmvp.JTable.ModelListeningTableModel;

public class FontListTableModel extends AbstractTableModel implements ModelListeningTableModel {

    private Font[] m_aFonts;

    public FontListTableModel() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        m_aFonts = ge.getAllFonts();
    }

    public int getRowCount() {
        return m_aFonts.length;
    }

    public int getColumnCount() {
        return 1;
    }

    public Object getValueAt(int nRow, int nColumn) {
        return m_aFonts[nRow].getFontName();
    }

    public void modelChanged(ModelEvent event) {
    }
}
