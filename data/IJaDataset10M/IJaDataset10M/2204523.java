package gu.client.view.dialogs;

import gu.client.dao.CollectionListener;
import gu.client.model.Worder;
import gu.client.model.User;
import gu.client.view.DatabaseEditorView;
import java.util.Iterator;
import java.util.List;
import com.google.gwt.user.client.ui.ListBox;

public class WorderDialog extends ObjectDialogBox {

    Worder story = new Worder();

    ListBox userList = new ListBox();

    public WorderDialog(DatabaseEditorView view) {
        super("<img src='item-story.png' hspace='3'>Create Work Order", view);
        story = new Worder();
        init();
    }

    public WorderDialog(Worder story, DatabaseEditorView view) {
        super("<img src='item-story.png' hspace='3'>Edit Story", view);
        this.story = story;
        init();
    }

    private void init() {
        addField("WO#", story.get_wo_number());
        addField("Name", story.get_customer_name());
        addField("ShipId", story.get_ship_id());
        addField("ConsId", story.get_cons_id());
        addField("Equipm", story.getDescription());
        addField("Pieces", story.get_pieces());
        addField("Type", story.get_type());
        addField("Lbs", story.get_weight_lbs());
        addField("Kgs", story.get_weight_kgs());
        addField("Pickup", story.getpickup_dt());
        addField("Delivr", story.get_delivery_dt());
        addField("BOL", story.get_bol());
        addButtons();
        final String postedby_id = story.getUser_id();
        getView().getObjectFactory().getUserDAO().getAll(new CollectionListener() {

            public void onCollection(List list) {
                for (Iterator it = list.iterator(); it.hasNext(); ) {
                    User user = (User) it.next();
                    userList.addItem(user.getName(), user.getId());
                    if (postedby_id != null && postedby_id.compareTo(user.getId()) == 0) userList.setSelectedIndex(userList.getItemCount() - 1);
                }
            }
        });
    }

    public void onSubmit() {
        story.set_wo_number(getField(0));
        story.set_customer_name(getField(1));
        story.set_ship_id(getField(2));
        story.set_cons_id(getField(3));
        story.setDescription(getField(4));
        story.set_pieces(getField(5));
        story.set_type(getField(6));
        story.set_weight_lbs(getField(7));
        story.set_weight_kgs(getField(8));
        story.set_pickup_dt(getField(9));
        story.set_delivery_dt(getField(10));
        story.set_bol(getField(11));
        getView().getObjectFactory().getWorderDAO().save(story);
    }
}
