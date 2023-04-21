package allensoft.javacvs.client.ui.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import allensoft.javacvs.client.*;
import java.util.*;
import allensoft.gui.*;

/** A component that displays the editors of files.
 @author Nicholas Allen. */
public class EditorsView extends JPanel {

    /** Creates a new EditorsView. */
    public EditorsView(EditorsResponse[] responses) {
        super(new BorderLayout());
        for (int i = 0; i < responses.length; i++) {
            EditorsResponse response = responses[i];
            for (int j = 0; j < response.getFileEditDetailsCount(); j++) m_FileEditDetails.add(response.getFileEditDetails(j));
        }
        SortedTableModel model = new SortedTableModel(new FileEditDetailsTableModel());
        JTable table = new JTable(model);
        model.addMouseListenerToHeaderInTable(table);
        add(new JScrollPane(table));
    }

    public EditorsView(EditorsResponse response) {
        this(new EditorsResponse[] { response });
    }

    private class FileEditDetailsTableModel extends AbstractTableModel {

        public String getColumnName(int col) {
            return g_ColNames[col];
        }

        public int getColumnCount() {
            return 5;
        }

        public int getRowCount() {
            return m_FileEditDetails.size();
        }

        public Object getValueAt(int row, int col) {
            EditorsResponse.FileEditDetails details = (EditorsResponse.FileEditDetails) m_FileEditDetails.get(row);
            if (col == 0) return details.getFile().getAbsolutePath();
            if (col == 1) return details.getEditor();
            if (col == 2) return details.getDate();
            if (col == 3) return details.getHost();
            return details.getPath();
        }
    }

    private java.util.List m_FileEditDetails = new ArrayList();

    private static String[] g_ColNames = { "File", "Editor", "Date", "Host", "Path" };
}
