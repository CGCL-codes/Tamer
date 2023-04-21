package org.castafiore.shoppingmall.contacts.ui;

import java.util.ArrayList;
import java.util.List;
import org.castafiore.contact.Contact;
import org.castafiore.shoppingmall.user.ShoppingMallUserManager;
import org.castafiore.shoppingmall.util.list.ListItem;
import org.castafiore.spring.SpringUtil;
import org.castafiore.ui.Container;
import org.castafiore.ui.ex.EXContainer;
import org.castafiore.ui.ex.form.EXCheckBox;

public class EXContactList extends EXContainer {

    public EXContactList(String name) {
        super(name, "table");
        EXContainer head = new EXContainer("", "thead");
        addChild(head);
        EXContainer tr = new EXContainer("", "tr");
        head.addChild(tr);
        tr.addChild(new EXContainer("", "th").setStyle("width", "15px").addChild(new EXCheckBox("selectAll")));
        tr.addChild(new EXContainer("", "th").setStyle("width", "180px").setText("  "));
        tr.addChild(new EXContainer("", "th").setStyle("width", "300px").setText("Name"));
        tr.addChild(new EXContainer("", "th").setText("E-Mail"));
        EXContainer body = new EXContainer("body", "tbody");
        addChild(body);
    }

    public List<ListItem<Contact>> getSelectedItems() {
        List<ListItem<Contact>> result = new ArrayList<ListItem<Contact>>();
        Container body = getChild("body");
        for (Container c : body.getChildren()) {
            if (c instanceof EXContactListItem) {
                if (((EXContactListItem) c).isChecked()) result.add((EXContactListItem) c);
            }
        }
        return result;
    }

    public void showContacts(String category) {
        String page = getAttribute("page");
        int iPage = 0;
        try {
            iPage = Integer.parseInt(page);
        } catch (Exception e) {
        }
        showContacts(category, iPage);
    }

    public void addPage(String category, int page) {
        setAttribute("page", page + "");
        List<Contact> contacts = SpringUtil.getBeanOfType(ShoppingMallUserManager.class).getCurrentUser().getContacts(category, page * 20, (20 * page) + 20);
        for (Contact contact : contacts) {
            EXContactListItem item = new EXContactListItem("");
            item.setItem(contact);
            getChild("body").addChild(item);
        }
    }

    public void showContacts(String category, int page) {
        setAttribute("page", page + "");
        getChild("body").getChildren().clear();
        getChild("body").setRendered(false);
        List<Contact> contacts = SpringUtil.getBeanOfType(ShoppingMallUserManager.class).getCurrentUser().getContacts(category, page * 20, (20 * page) + 20);
        for (Contact contact : contacts) {
            EXContactListItem item = new EXContactListItem("");
            item.setItem(contact);
            getChild("body").addChild(item);
        }
    }
}
