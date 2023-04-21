package jmplayer.ui.action.batch;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import jmplayer.batch.BatchModel;
import jmplayer.entity.Const;
import jmplayer.ui.MainFrame;
import jmplayer.ui.component.CBatchTable;
import jmplayer.ui.component.TableSorter;

/**
 * 
 * @author lois
 * @version $Revision: 1.2 $
 * 
 */
public class RegexAction extends AbstractAction {

    private static final long serialVersionUID = 3222156473663604132L;

    private CBatchTable table;

    public RegexAction(CBatchTable table) {
        super("use regular expression");
        this.table = table;
    }

    public void actionPerformed(ActionEvent e) {
        BatchModel bm = (BatchModel) ((TableSorter) table.getModel()).getTableModel();
        for (int i = 0; i < table.getSelectedRowCount(); i++) {
            for (int j = 0; j < table.getSelectedColumnCount(); j++) {
                int c = table.getSelectedColumns()[j];
                if (c != Const.BE_NR && c != Const.BE_TIME) {
                    String val = (String) bm.getValueAt(table.getSelectedRows()[i], table.getSelectedColumns()[j]);
                    String sp = MainFrame.getInstance().getBatchFrame().getTfSearchPattern().getText();
                    String rp = MainFrame.getInstance().getBatchFrame().getTfReplacePattern().getText();
                    if (MainFrame.getInstance().getBatchFrame().getCbCaseInsensitive().isSelected()) {
                        val = val.toLowerCase();
                        sp = sp.toLowerCase();
                    }
                    if (MainFrame.getInstance().getBatchFrame().getCbReplaceAll().isSelected()) val = val.replaceAll(sp, rp); else val = val.replaceFirst(sp, rp);
                    bm.setValueAt(val, table.getSelectedRows()[i], table.getSelectedColumns()[j]);
                }
            }
        }
    }
}
