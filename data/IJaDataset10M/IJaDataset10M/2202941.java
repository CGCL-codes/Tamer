package net.sf.rmoffice.ui.panels;

import javax.swing.JPanel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JTree;
import javax.swing.JTable;

public class SkillsPanel extends JPanel {

    private JTree tree;

    private JTable table;

    /**
	 * Create the panel.
	 */
    public SkillsPanel() {
        super();
        setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow(3)") }, new RowSpec[] { FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow") }));
        add(getTree(), "2, 2, fill, fill");
        add(getTable(), "4, 2, fill, fill");
    }

    public JTree getTree() {
        if (tree == null) {
            tree = new JTree();
        }
        return tree;
    }

    public JTable getTable() {
        if (table == null) {
            table = new JTable();
        }
        return table;
    }
}
