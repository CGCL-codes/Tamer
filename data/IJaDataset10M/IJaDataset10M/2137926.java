package org.homemotion.ui.admin.buildings;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import org.homemotion.building.Building;
import org.homemotion.building.BuildingManager;
import org.homemotion.di.Registry;

@ManagedBean(name = "buildingLists")
@ApplicationScoped
public final class ListProvider {

    public List<SelectItem> getAllBuildings() {
        BuildingManager itemManager = Registry.getInstance(BuildingManager.class);
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        List<Building> items = itemManager.getAllItems();
        for (Building item : items) {
            selectItems.add(new SelectItem(item, item.getName()));
        }
        return selectItems;
    }
}
