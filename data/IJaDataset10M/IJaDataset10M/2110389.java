package at.redcross.tacos.web.model;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import at.redcross.tacos.dbal.entity.EntityImpl;

public class SelectableItemHelper {

    public static List<SelectItem> convertToItems(List<? extends EntityImpl> entityList) {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>();
        for (EntityImpl entity : entityList) {
            items.add(new SelectableItem(entity).getItem());
        }
        return items;
    }
}
