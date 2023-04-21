package isp.apps.example.shoppingcart.action;

import com.opensymphony.xwork.ActionContext;
import com.opensymphony.xwork.ActionSupport;
import isp.apps.example.shoppingcart.persistence.ItemDirectory;
import isp.apps.example.shoppingcart.persistence.factory.ItemPersistenceFactory;
import java.util.List;
import java.util.Map;

public class ListItemsAction extends ActionSupport {

    private List allItems, validItems;

    private ItemDirectory itemDirectory;

    public ListItemsAction() {
        itemDirectory = ItemPersistenceFactory.getItemDirectory();
    }

    public List getItems() {
        return allItems;
    }

    public String execute() {
        allItems = itemDirectory.getItems();
        validItems = itemDirectory.getValidItems();
        return SUCCESS;
    }

    public List getValidItems() {
        return validItems;
    }
}
