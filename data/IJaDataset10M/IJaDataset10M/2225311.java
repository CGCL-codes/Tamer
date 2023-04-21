package testingapplication.flexmenu;

import com.softaspects.jsf.component.flexMenu.FlexMenuTextItem;
import com.softaspects.jsf.component.flexMenu.FlexMenuConfiguration;
import com.softaspects.jsf.component.flexMenu.FlexMenu;

public class FlexMenuDataModelUtil {

    public static FlexMenuConfiguration getMenuConfiguration() {
        FlexMenuConfiguration config = new FlexMenuConfiguration();
        config.setNormalStyleClass("normal");
        config.setActiveStyleClass("active");
        config.setDisabledStyleClass("disabled");
        return config;
    }

    public static FlexMenuTextItem getMenuItem(String id, String text) {
        FlexMenuTextItem item = new FlexMenuTextItem();
        item.setId(id);
        item.setText(text);
        item.setRendered(true);
        item.setEnabled(true);
        return item;
    }

    public static FlexMenuTextItem getMenuItem(FlexMenuTextItem parent, String text) {
        return getMenuItem(parent, text.replaceAll(" ", ""), text);
    }

    public static FlexMenuTextItem getMenuItem(FlexMenuTextItem parent, String id, String text) {
        FlexMenuTextItem result = getMenuItem(id, text);
        if (parent != null) {
            parent.getChildren().add(result);
        }
        return result;
    }

    public static FlexMenuTextItem getMenuItem(FlexMenu parent, String text) {
        return getMenuItem(parent, text.replaceAll(" ", ""), text);
    }

    public static FlexMenuTextItem getMenuItem(FlexMenu parent, String id, String text) {
        FlexMenuTextItem result = getMenuItem(id, text);
        if (parent != null) {
            parent.getChildren().add(result);
        }
        return result;
    }
}
