package org.plazmaforge.bsolution.goods.client.swing.actions;

import java.awt.event.ActionEvent;
import org.plazmaforge.bsolution.goods.client.swing.forms.PurchaseOrderList;
import org.plazmaforge.framework.client.swing.SwingFormManager;
import org.plazmaforge.framework.core.exception.ApplicationException;

/**
 * Created on 31.05.2006
 */
public class PurchaseOrderListAction extends GUIGoodsAction {

    public void perform(ActionEvent e) throws ApplicationException {
        SwingFormManager.showListForm(PurchaseOrderList.class);
    }
}
