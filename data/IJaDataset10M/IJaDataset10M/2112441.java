package org.plazmaforge.studio.appmanager.model;

import org.plazmaforge.framework.config.object.IActionConfig;
import org.plazmaforge.framework.config.object.IMenuConfig;
import org.plazmaforge.framework.config.object.IMenuItemConfig;
import org.plazmaforge.framework.config.object.IMenuSeparatorConfig;
import org.plazmaforge.framework.config.object.IObjectConfig;
import org.plazmaforge.framework.config.object.IToolItemConfig;
import org.plazmaforge.framework.config.configurer.ActionConfigurer;
import org.plazmaforge.studio.core.model.nodes.INode;

public class TextProvider {

    private static String EMPTY_STRRING = "";

    private static String MENU_SEPARATOR_STRING = "____";

    private static boolean useIdInName = false;

    private static ActionConfigurer actionConfigurer;

    private TextProvider() {
    }

    public static ActionConfigurer getActionConfigurer() {
        return actionConfigurer;
    }

    public static void setActionConfigurer(ActionConfigurer actionConfigurer) {
        TextProvider.actionConfigurer = actionConfigurer;
    }

    public static String getText(Object obj) {
        if (obj == null) {
            return EMPTY_STRRING;
        }
        if (!(obj instanceof INode)) {
            return EMPTY_STRRING;
        }
        INode node = (INode) obj;
        Object data = node.getData();
        if (!(data instanceof IObjectConfig)) {
            return EMPTY_STRRING;
        }
        IObjectConfig c = (IObjectConfig) data;
        if ("true".equals(node.getData("onlyname"))) {
            return normalizeString(node.getName());
        }
        return normalizeString(getTextByData(c));
    }

    public static String getTextByData(IObjectConfig data) {
        return getTextByData(data, useIdInName);
    }

    public static String getTextByData(IObjectConfig data, boolean useId) {
        if (data == null) {
            return "";
        }
        if (data instanceof IToolItemConfig) {
            return getTextByToolItem((IToolItemConfig) data);
        } else if (data instanceof IMenuConfig) {
            return getTextByMenu((IMenuConfig) data);
        } else if (data instanceof IMenuSeparatorConfig) {
            return EMPTY_STRRING;
        } else if (data instanceof IMenuItemConfig) {
            return getTextByMenuItem((IMenuItemConfig) data);
        }
        return getTextByData(data.getId(), data.getName(), useId);
    }

    public static String getTextByData(String id, String name) {
        return getTextByData(id, name, useIdInName);
    }

    public static String getTextByData(String id, String name, boolean useId) {
        if (name != null) {
            name = name.trim();
        }
        if (!useId) {
            if (isEmpty(name)) {
                return getWrapIdString(id);
            }
            return name;
        }
        if (isEmpty(name)) {
            return getWrapIdString(id);
        }
        return getWrapIdString(id) + " " + name;
    }

    public static String getTextByToolItem(IToolItemConfig toolItem) {
        return getTextByActionId(toolItem.getActionId());
    }

    public static String getTextByMenu(IMenuConfig menu) {
        return getTextByData(menu.getId(), menu.getText());
    }

    public static String getTextByMenuItem(IMenuItemConfig menuItem) {
        return getTextByActionId(menuItem.getActionId());
    }

    public static String getTextByActionId(String actionId) {
        if (getActionConfigurer() == null) {
            return EMPTY_STRRING;
        }
        IActionConfig action = getActionConfigurer().getObjectById(actionId);
        return TextProvider.getTextByData(actionId, (action == null ? null : action.getName()));
    }

    private static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    private static String normalizeString(String str) {
        return str == null ? EMPTY_STRRING : str;
    }

    private static String getWrapIdString(String id) {
        return "[" + id + "]";
    }
}
