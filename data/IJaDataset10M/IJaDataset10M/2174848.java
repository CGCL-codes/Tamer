package edu.uwlax.cs.oayonlinestore.client.gui.shop;

import edu.uwlax.cs.oayonlinestore.client.OnlinestoreRService;
import edu.uwlax.cs.oayonlinestore.client.gui.Store;
import edu.uwlax.cs.oayonlinestore.client.gui.shared.Const.OrderStatus;
import edu.uwlax.cs.oayonlinestore.client.utils.FormTable;
import edu.uwlax.cs.oayonlinestore.client.utils.SelectionList;
import edu.uwlax.cs.oayonlinestore.client.utils.TitlePage;
import edu.uwlax.cs.oayonlinestore.vo.OrderEditVO;

public class PageAdminOrder extends TitlePage {

    private class AdminOrderForm extends FormTable {

        private OrderEditVO order;

        private SelectionList status = new SelectionList(OrderStatus.ALL);

        public AdminOrderForm() {
            super();
            addInput("Order Status", status);
            addButton("Save", Store.getFrame().getSalOrders().getAnchor());
        }

        public void onButtonClicked() {
            order.setStatus(status.getSelection());
            OnlinestoreRService.Util.getInstance().updateOrderEdit(order, false, getSaveHandler());
        }

        public void onDataLoad(Object data) {
            order = (OrderEditVO) data;
            status.setSelection(order.getStatus());
        }
    }

    private AdminOrderForm form;

    protected void create() {
        setTitle("Admin Order");
        add(form = new AdminOrderForm());
    }

    public String getID() {
        return "AdminOrder";
    }

    protected boolean onShow(int param) {
        OnlinestoreRService.Util.getInstance().getOrderEdit(param, form.getLoadHandler());
        return true;
    }
}
