package za.co.kmz.simpleapp.service;

import java.util.ArrayList;
import java.util.List;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.annotation.Named;
import za.co.kmz.simpleapp.dom.ShoppingList;
import za.co.kmz.simpleapp.dom.StockItem;
import za.co.kmz.simpleapp.service.StockItemRepositoryInterface;

public class StockItemRepository extends AbstractFactoryAndRepository implements StockItemRepositoryInterface {

    public String title() {
        return "Stock Items";
    }

    public String iconName() {
        return "StockItem";
    }

    public List<StockItem> allStockItems() {
        return allInstances(StockItem.class);
    }

    public StockItem newStockItem() {
        StockItem object = newTransientInstance(StockItem.class);
        return object;
    }

    public StockItem findStockItem(@Named("Name") String title) {
        StockItem item = this.firstMatch(StockItem.class, title);
        return item;
    }

    public StockItem newStockItem(String name, String category, String unit) {
        StockItem item = newStockItem();
        item.setItem(name);
        item.setUnits(unit);
        persist(item);
        return item;
    }

    public ShoppingList newShoppingList() {
        ShoppingList list = newPersistentInstance(ShoppingList.class);
        return list;
    }

    public List<ShoppingList> allShoppingLists() {
        return allInstances(ShoppingList.class);
    }

    public List<ShoppingList> allShoppingListsContaining(StockItem item) {
        List<ShoppingList> lists = allShoppingLists();
        if (lists.size() == 0) {
            return null;
        }
        List<ShoppingList> retList = new ArrayList<ShoppingList>();
        for (ShoppingList list : lists) {
            if (list.getItems().contains(item)) {
                retList.add(list);
            }
        }
        if (retList.size() > 0) {
            return retList;
        }
        return null;
    }
}
