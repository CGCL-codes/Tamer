package org.jlense.uiworks.internal;

import javax.swing.Action;
import org.eclipse.core.runtime.IConfigurationElement;
import org.jlense.uiworks.workbench.IViewPart;
import org.jlense.uiworks.workbench.IWorkbenchActionConstants;
import org.jlense.uiworks.workbench.IWorkbenchWindow;

/**
 * When 'action' tag is found in the registry, an object of this
 * class is created. It creates the appropriate action object
 * and captures information that is later used to add this action
 * object into menu/tool bar. This class is reused for
 * global (workbench) menu/tool bar, popup menu actions,
 * as well as view's pulldown and local tool bar.
 */
public class ActionDescriptor {

    private PluginAction action;

    private String toolbarPath;

    private String menuPath;

    private String id;

    private String menuGroup;

    private String toolbarGroup;

    public static final int T_POPUP = 0x1;

    public static final int T_VIEW = 0x2;

    public static final int T_WORKBENCH = 0x3;

    public static final int T_EDITOR = 0x4;

    public static final int T_WORKBENCH_PULLDOWN = 0x5;

    public static final String ATT_ID = "id";

    public static final String ATT_HELP_CONTEXT_ID = "helpContextId";

    public static final String ATT_LABEL = "label";

    public static final String ATT_NAME = "name";

    public static final String ATT_STATE = "state";

    public static final String ATT_DESCRIPTION = "description";

    public static final String ATT_TOOLTIP = "tooltip";

    public static final String ATT_MENUBAR_PATH = "menubarPath";

    public static final String ATT_TOOLBAR_PATH = "toolbarPath";

    public static final String ATT_ICON = "icon";

    public static final String ATT_HOVERICON = "hoverIcon";

    public static final String ATT_DISABLEDICON = "disabledIcon";

    public static final String ATT_CLASS = "class";

    public static final String ATT_RETARGET = "retarget";

    public static final String ATT_ALLOW_LABEL_UPDATE = "allowLabelUpdate";

    /**
 * Creates a new descriptor with the specified target.
 */
    public ActionDescriptor(IConfigurationElement actionElement, int targetType) {
        this(actionElement, targetType, null);
    }

    /**
 * Creates a new descriptor with the target and destination workbench part
 * it will go into.
 */
    public ActionDescriptor(IConfigurationElement actionElement, int targetType, Object target) {
        id = actionElement.getAttribute(ATT_ID);
        String name = actionElement.getAttribute(ATT_NAME);
        String label = actionElement.getAttribute(ATT_LABEL);
        String tooltip = actionElement.getAttribute(ATT_TOOLTIP);
        String helpContextId = actionElement.getAttribute(ATT_HELP_CONTEXT_ID);
        String mpath = actionElement.getAttribute(ATT_MENUBAR_PATH);
        String tpath = actionElement.getAttribute(ATT_TOOLBAR_PATH);
        String state = actionElement.getAttribute(ATT_STATE);
        String icon = actionElement.getAttribute(ATT_ICON);
        String hoverIcon = actionElement.getAttribute(ATT_HOVERICON);
        String disabledIcon = actionElement.getAttribute(ATT_DISABLEDICON);
        String description = actionElement.getAttribute(ATT_DESCRIPTION);
        if (label == null) {
            WorkbenchPlugin.log("Invalid action declaration (label == null): " + id);
            label = WorkbenchMessages.getString("ActionDescriptor.invalidLabel");
        }
        String mgroup = null;
        String tgroup = null;
        if (mpath != null) {
            int loc = mpath.lastIndexOf('/');
            if (loc != -1) {
                mgroup = mpath.substring(loc + 1);
                mpath = mpath.substring(0, loc);
            } else {
                mgroup = mpath;
                mpath = null;
            }
        }
        if (targetType == T_POPUP && mgroup == null) mgroup = IWorkbenchActionConstants.MB_ADDITIONS;
        if (tpath != null) {
            int loc = tpath.lastIndexOf('/');
            if (loc != -1) {
                tgroup = tpath.substring(loc + 1);
                tpath = tpath.substring(0, loc);
            } else {
                tgroup = tpath;
                tpath = null;
            }
        }
        menuPath = mpath;
        menuGroup = mgroup;
        toolbarPath = tpath;
        toolbarGroup = tgroup;
        action = createAction(targetType, actionElement, target);
        action.setText(label);
        if (name != null) action.putValue(Action.NAME, name);
        if (tooltip != null) action.setToolTipText(tooltip);
        if (helpContextId != null) {
            String fullID = helpContextId;
            if (fullID.indexOf(".") == -1) fullID = actionElement.getDeclaringExtension().getDeclaringPluginDescriptor().getUniqueIdentifier() + "." + helpContextId;
        }
        if (description != null) action.setDescription(description);
        if (state != null) {
            action.setChecked(state.equals("true"));
        }
        if (icon != null) {
            action.setImage(WorkbenchImages.getImageFromExtension(actionElement.getDeclaringExtension(), icon));
        }
        if (hoverIcon != null) {
            action.setHoverImage(WorkbenchImages.getImageFromExtension(actionElement.getDeclaringExtension(), hoverIcon));
        }
        if (disabledIcon != null) {
            action.setDisabledImage(WorkbenchImages.getImageFromExtension(actionElement.getDeclaringExtension(), disabledIcon));
        }
    }

    /**
 * Creates an instance of PluginAction. Depending on the target part,
 * subclasses of this class may be created.
 */
    private PluginAction createAction(int targetType, IConfigurationElement actionElement, Object target) {
        switch(targetType) {
            case T_VIEW:
                return new ViewPluginAction(actionElement, ATT_CLASS, (IViewPart) target);
            case T_WORKBENCH:
                return new WWinPluginAction(actionElement, ATT_CLASS, (IWorkbenchWindow) target);
            case T_WORKBENCH_PULLDOWN:
                return new WWinPluginPulldown(actionElement, ATT_CLASS, (IWorkbenchWindow) target);
            case T_POPUP:
                return new ObjectPluginAction(actionElement, ATT_CLASS);
            default:
                WorkbenchPlugin.log("Unknown Action Type: " + targetType);
                return null;
        }
    }

    /**
 * Returns the action object held in this descriptor.
 */
    public PluginAction getAction() {
        return action;
    }

    /**
 * Returns action's id as defined in the registry.
 */
    public String getId() {
        return id;
    }

    /**
 * Returns named slot (group) in the menu where this action
 * should be added.
 */
    public String getMenuGroup() {
        return menuGroup;
    }

    /**
 * Returns menu path where this action should be added. If null,
 * the action will not be added into the menu.
 */
    public String getMenuPath() {
        return menuPath;
    }

    /**
 * Returns the named slot (group) in the tool bar where this
 * action should be added.
 */
    public String getToolbarGroup() {
        return toolbarGroup;
    }

    /**
 * Returns path in the tool bar where this action should be added.
 * If null, action will not be added to the tool bar.
 */
    public String getToolbarPath() {
        return toolbarPath;
    }

    /**
 * For debugging only.
 */
    public String toString() {
        return "ActionDescriptor(" + id + ")";
    }
}
