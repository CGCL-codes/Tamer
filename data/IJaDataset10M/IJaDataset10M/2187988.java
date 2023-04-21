package de.flingelli.scrum.gui.bugzilla;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.ezware.oxbow.swingbits.table.filter.TableRowFilterSupport;
import de.flingelli.sab.Bug;
import de.flingelli.scrum.observer.ProductPropertyChangeSupport;

/**
 * 
 * @author Markus Flingelli
 * 
 */
@SuppressWarnings("serial")
public class BugzillaTablePanel extends JPanel {

    private JTable table;

    public BugzillaTablePanel() {
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(400, 400));
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);
        table = getTable(scrollPane);
    }

    private JTable getTable(JScrollPane scrollPane) {
        if (table == null) {
            table = new JTable();
            scrollPane.setViewportView(table);
            table.setAutoCreateRowSorter(true);
            table.setModel(new BugzillaTableModel());
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent event) {
                    int index = event.getFirstIndex();
                    BugzillaTableModel model = (BugzillaTableModel) table.getModel();
                    Bug bug = model.getBug(index);
                    ProductPropertyChangeSupport.getInstance().selectBugChanged(bug);
                }
            });
            table = TableRowFilterSupport.forTable(table).searchable(true).apply();
        }
        return table;
    }
}
