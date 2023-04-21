package net.sourceforge.x360mediaserve.dataManager.impl.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.x360mediaserve.api.plugins.config.ComboListActionItem;
import net.sourceforge.x360mediaserve.api.plugins.config.ListActionItem;
import net.sourceforge.x360mediaserve.api.plugins.config.NewConfigInfo;
import net.sourceforge.x360mediaserve.api.plugins.config.NewConfigItem;
import net.sourceforge.x360mediaserve.api.plugins.config.StatusItem;
import net.sourceforge.x360mediaserve.api.plugins.config.TextActionItem;

public class DataManagerConfigInfo implements NewConfigInfo {

    DataManagerConfigModel model;

    public DataManagerConfigInfo(DataManagerConfigModel model) {
        this.model = model;
    }

    public List<NewConfigItem> getFields() {
        ArrayList<NewConfigItem> items = new ArrayList<NewConfigItem>();
        items.add(new StatusItem("scanningStatus", "Scanning status:", "String"));
        items.add(new ListActionItem("dirToDelete", "dirs", "Directories", "String"));
        items.add(new TextActionItem("dirToAdd", "Directory to add:", "String"));
        items.add(new ComboListActionItem("scanOption", "scanOptions", "Rescan", "String"));
        return items;
    }

    public DataManagerConfigModel getModel() {
        return model;
    }

    public String getName() {
        return "DataManager";
    }

    public void setModel(Serializable model) {
        this.model = (DataManagerConfigModel) model;
    }

    public boolean needsSubmit() {
        return false;
    }
}
