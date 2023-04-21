package edu.uwlax.cs.oayonlinestore.client.gui.salesdept;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import edu.uwlax.cs.oayonlinestore.client.OnlinestoreRService;
import edu.uwlax.cs.oayonlinestore.client.utils.DefaultAsyncCallback;
import edu.uwlax.cs.oayonlinestore.client.utils.DekoTable;
import edu.uwlax.cs.oayonlinestore.client.utils.TitlePage;
import edu.uwlax.cs.oayonlinestore.client.utils.VSpacer;
import edu.uwlax.cs.oayonlinestore.vo.GiftCardVO;

public class PageSalGiftcards extends TitlePage {

    private class GiftCardTable extends DekoTable {

        Button delete;

        public GiftCardTable() {
            super();
            TextBox val = new TextBox();
            val.setWidth("64px");
            TextBox amo = new TextBox();
            amo.setWidth("64px");
            addTitle("Value");
            addTitle("Card ID");
            addTitle("");
        }

        public void addItem(GiftCardVO gv) {
            AddRow r = addRow(0);
            r.add(gv.getMoneyValue() + " USD");
            r.add(gv.getCardID());
            r.add(delete = new Button("Delete"));
            delete.addClickListener(new ButtonClickListener(gv));
        }

        public class ButtonClickListener implements ClickListener {

            GiftCardVO co = null;

            public ButtonClickListener(GiftCardVO c) {
                co = c;
            }

            public void onClick(Widget arg0) {
                OnlinestoreRService.Util.getInstance().deleteGiftCard(co.getId(), new DoneAddAsycHandler());
            }
        }

        public class DoneAddAsycHandler extends DefaultAsyncCallback {

            public void onSuccess(Object o) {
                onShow();
            }
        }
    }

    GiftCardTable table;

    Button add;

    TextBox ammount, value;

    protected void create() {
        setTitle("Available Gift Cards");
        add(table = new GiftCardTable());
    }

    public String getID() {
        return "PageAdminGiftCards";
    }

    public class CategoryAsycHandler extends DefaultAsyncCallback {

        public void onSuccess(Object o) {
            GiftCardVO[] giftcards = (GiftCardVO[]) o;
            for (int i = 0; i < giftcards.length; i++) table.addItem(giftcards[i]);
        }
    }

    public class ButtonClickListener implements ClickListener {

        int cardvalue, cardammount;

        public void onClick(Widget arg0) {
            try {
                cardvalue = Integer.parseInt(value.getText());
                cardammount = Integer.parseInt(ammount.getText());
            } catch (NumberFormatException e) {
                Window.alert("Enter Valid values please !");
            }
            OnlinestoreRService.Util.getInstance().generateGiftCards(cardammount, cardvalue, new DoneAddAsycHandler());
        }
    }

    public class DoneAddAsycHandler extends DefaultAsyncCallback {

        public void onSuccess(Object o) {
            onShow();
        }
    }

    public void onShow() {
        removeAll();
        setTitle("Available Gift Cards");
        add(new Label("Quantity to add: "));
        add(ammount = new TextBox());
        add(new Label("Value of Gift Cards: "));
        add(value = new TextBox());
        add(add = new Button("Add New Cards"));
        add.addClickListener(new ButtonClickListener());
        add(new VSpacer("20"));
        add(table = new GiftCardTable());
        OnlinestoreRService.Util.getInstance().getGiftCardList(new CategoryAsycHandler());
    }
}
