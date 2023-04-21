package opaqua.ui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * @author Thomas Siegrist
 */
@SuppressWarnings("serial")
public class LevelMappingDialog extends TemplateDialog {

    /** components: */
    private JTable mappingTable;

    private JScrollPane tableScrollPane;

    public LevelMappingDialog(JFrame parent) {
        super("Map a TagType to each level:", parent);
        this.initComponents();
    }

    private void initComponents() {
        this.initMainPanel();
    }

    private void initMainPanel() {
        this.getMainPanel().setLayout(new GridBagLayout());
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.weightx = 1;
        constr.weighty = 1;
        this.mappingTable = new JTable();
        this.tableScrollPane = new JScrollPane(this.mappingTable);
        this.getMainPanel().add(this.tableScrollPane, constr);
    }

    public JTable getMappingTable() {
        return this.mappingTable;
    }
}
