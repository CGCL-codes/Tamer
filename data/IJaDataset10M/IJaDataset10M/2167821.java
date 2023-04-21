package com.wateray.ipassbook.ui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import com.wateray.ipassbook.commom.Constant.Type;
import com.wateray.ipassbook.domain.form.PassbookTradeForm;
import com.wateray.ipassbook.ui.StandardFrame;
import com.wateray.ipassbook.ui.dialog.TransferDialog;
import com.wateray.ipassbook.ui.model.TransferTableModel;
import com.wateray.ipassbook.util.ImageManager;
import com.wateray.ipassbook.util.LanguageLoader;

/**
 * @author wateray
 * @create 2009-5-27
 */
public class DeleteTransferAction extends AbstractAction {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    private Object data;

    public DeleteTransferAction() {
        super(LanguageLoader.getString("Delete"));
        putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Delete_Transfer"));
        this.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PassbookTradeForm transferForm = null;
        if (data instanceof JTable) {
            JTable table = (JTable) data;
            if (table.getSelectedRow() != -1) {
                int selectedIndex = table.convertRowIndexToModel(table.getSelectedRow());
                transferForm = (PassbookTradeForm) ((TransferTableModel) table.getModel()).getRowObject(selectedIndex);
            }
        }
        TransferDialog dialog = new TransferDialog(StandardFrame.getInstance(), LanguageLoader.getString("Delete_Transfer"), Type.DELETE, transferForm);
        dialog.getFinishButton().setText(LanguageLoader.getString("Delete"));
        dialog.setVisible(true);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
