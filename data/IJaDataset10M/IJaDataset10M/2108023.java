package net.sf.dub.application.view.swing;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import net.sf.dub.application.controller.VersiontableController;
import net.sf.dub.application.db.ExecutionException;
import net.sf.dub.application.db.RetrieveDataException;
import net.sf.dub.miniframework.controller.ProcessException;
import net.sf.dub.miniframework.controller.ProcessableController;
import net.sf.dub.miniframework.util.Messages;
import net.sf.dub.miniframework.view.swing.ApplicationFrame;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * class desciption. Purpose, functionality, etc..
 * 
 * @author  dgm
 * @version $Revision: 1.1 $
 */
public class VersiontablePanel extends DubProcessablePanel {

    protected JTextField textVersiontable = new JTextField("");

    protected JTable tableTables = new JTable();

    public VersiontablePanel(ProcessableController controller, ApplicationFrame application) {
        super(controller, application, Messages.get("VersiontablePanel.title"));
        setupComponents();
        updateView();
    }

    public void setupComponents() {
        FormLayout layout = new FormLayout("4dlu, right:max(72dlu;default), 2dlu, default:grow, 4dlu", "4dlu, default, 8dlu, default, 4dlu, default, 4dlu, fill:default:grow, 4dlu");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(layout);
        builder.add(new JLabel(Messages.get("VersiontablePanel.about")), cc.xyw(2, 2, 3));
        builder.addSeparator(Messages.get("VersiontablePanel.separator.table"), cc.xyw(2, 4, 3));
        builder.addLabel(Messages.get("VersiontablePanel.table.tablename.label"), cc.xy(2, 6));
        builder.add(textVersiontable, cc.xy(4, 6));
        builder.addLabel(Messages.get("VersiontablePanel.table.existingtables.label"), cc.xy(2, 8));
        builder.add(new JScrollPane(tableTables), cc.xy(4, 8));
        tableTables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableTables.setDefaultRenderer(Object.class, new VersiontableCellRenderer());
        tableTables.getTableHeader().setReorderingAllowed(false);
        tableTables.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!tableTables.getSelectionModel().isSelectionEmpty()) {
                    textVersiontable.setText((String) tableTables.getModel().getValueAt(tableTables.getSelectedRow(), 1));
                }
            }
        });
        VersiontableController versionController = (VersiontableController) getController();
        try {
            VersiontableTableModel dataModel = new VersiontableTableModel(versionController.getDatabase());
            tableTables.setModel(dataModel);
            TableColumn columnRownum = tableTables.getColumnModel().getColumn(0);
            columnRownum.setPreferredWidth(64);
            columnRownum.setMaxWidth(64);
            TableColumn columnTablespace = tableTables.getColumnModel().getColumn(2);
            columnTablespace.setPreferredWidth(192);
            columnTablespace.setMaxWidth(192);
        } catch (RetrieveDataException ex) {
            showErrorDialog(Messages.get("VersiontablePanel.error.loadingtable"), ex);
        }
        textVersiontable.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
            }

            public void insertUpdate(DocumentEvent e) {
                setButtonNextEnabled(textVersiontable.getText().length() > 0);
            }

            public void removeUpdate(DocumentEvent e) {
                setButtonNextEnabled(textVersiontable.getText().length() > 0);
            }
        });
        add(builder.getPanel(), BorderLayout.CENTER);
    }

    public void updateView() {
        textVersiontable.setText(getConfiguration().getProperty("update.table"));
    }

    public void onNext() throws ProcessException {
        saveData();
        VersiontableController versionController = (VersiontableController) getController();
        String versionTable = versionController.getConfiguration().getProperty("update.table");
        try {
            if (!versionController.existVersionTable()) {
                String lineSeparator = System.getProperty("line.separator");
                int resultCreateTable = JOptionPane.showConfirmDialog(getApplicationFrame().getFrame(), Messages.get("VersiontablePanel.create.message.1") + lineSeparator + lineSeparator + Messages.get("VersiontablePanel.create.message.2") + " '" + versionTable + "'?" + lineSeparator + lineSeparator + Messages.get("VersiontablePanel.create.message.3"), Messages.get("VersiontablePanel.create.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
                if (resultCreateTable == JOptionPane.CANCEL_OPTION) {
                    throw new ProcessException(null, null, true);
                }
                try {
                    versionController.createVersiontable(versionTable);
                } catch (ExecutionException ee) {
                    throw new ProcessException(Messages.get("VersiontablePanel.error.table.create"), ee);
                }
            }
        } catch (RetrieveDataException rdex) {
            throw new ProcessException(Messages.get("VersiontablePanel.error.load.tables"), rdex);
        }
    }

    public void onBack() {
        saveData();
    }

    public void saveData() {
        getConfiguration().setProperty("update.table", textVersiontable.getText().trim());
    }
}
