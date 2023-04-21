package de.shipdown.util.mysql.gui;

import java.text.MessageFormat;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import de.shipdown.util.mysql.AnalysisModel;
import de.shipdown.util.mysql.Table;
import de.shipdown.util.mysql.index.IndexDescriptor;
import de.shipdown.util.mysql.render.SizeUtil;

/**
 * TreeNode to put a {@link Table} into a {@link JTree}.
 * 
 * @author ds
 */
public class TableTreeNode extends DefaultMutableTreeNode {

    /** generated serialVersionUID */
    private static final long serialVersionUID = 5300072887215779810L;

    private String displayString;

    private long maxEstSavings;

    @Override
    public String toString() {
        return displayString;
    }

    public TableTreeNode(Table aTable, AnalysisModel aModel) {
        super(aTable, true);
        long tSavings = 0;
        for (IndexDescriptor tDescriptor : aModel.getObsoletedByTable(aTable)) {
            tSavings += tDescriptor.getEstimatedSize();
        }
        maxEstSavings = tSavings;
        displayString = MessageFormat.format("{0} (max. est. savings {1})", aTable.toString(), SizeUtil.format(tSavings));
    }

    public long getMaxEstSavings() {
        return maxEstSavings;
    }
}
