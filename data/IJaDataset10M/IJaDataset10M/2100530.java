package pcgen.gui2.util;

import pcgen.gui2.util.table.SortableTableModel;
import pcgen.gui2.util.treetable.TreeTableModel;

/**
 *
 * @author Connor Petty <mistercpp2000@gmail.com>
 */
public class JTreeTablePane extends JTablePane {

    private static final long serialVersionUID = -2581915114516430509L;

    public JTreeTablePane() {
        this(null);
    }

    public JTreeTablePane(TreeTableModel model) {
        super(new JTreeTable(model));
    }

    @Override
    protected JTreeTable getTable() {
        return (JTreeTable) super.getTable();
    }

    @Override
    public void setModel(SortableTableModel model) {
    }

    public void setTreeTableModel(TreeTableModel model) {
        getTable().setTreeTableModel(model);
    }
}
