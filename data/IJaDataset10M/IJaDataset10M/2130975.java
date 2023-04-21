package org.jlense.uiworks.internal;

import javax.swing.JToolBar;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.jlense.uiworks.action.IContributionItem;
import org.jlense.uiworks.action.IContributionManager;
import org.jlense.uiworks.workbench.IActionBars;
import org.jlense.uiworks.workbench.IWorkbenchActionConstants;
import org.jlense.uiworks.workbench.IWorkbenchWindow;

/**
 * This builder reads the actions for an action set from the registry.
 */
public class PluginActionSetBuilder extends PluginActionBuilder {

    public static final String TAG_ACTION_SET = "actionSet";

    public static final String ATT_PULLDOWN = "pulldown";

    private PluginActionSet actionSet;

    private IWorkbenchWindow window;

    /**
 * Constructs a new builder.
 */
    public PluginActionSetBuilder() {
    }

    protected void addGroup(IContributionManager mgr, String name) {
        String actionSetId = actionSet.getDesc().getId();
        IContributionItem refItem = findInsertionPoint(IWorkbenchActionConstants.MB_ADDITIONS, actionSetId, mgr, true);
        ActionSetSeparator group = new ActionSetSeparator(new JToolBar.Separator(), name, actionSetId);
        if (refItem == null) mgr.addContribution(group); else mgr.insertAfter(refItem.getId(), group);
    }

    /**
 * This factory method returns a new ActionDescriptor for the
 * configuration element.  
 */
    protected ActionDescriptor createActionDescriptor(IConfigurationElement element) {
        String pulldown = element.getAttribute(ATT_PULLDOWN);
        ActionDescriptor desc = null;
        if (pulldown != null && pulldown.equals("true")) desc = new ActionDescriptor(element, ActionDescriptor.T_WORKBENCH_PULLDOWN, window); else desc = new ActionDescriptor(element, ActionDescriptor.T_WORKBENCH, window);
        WWinPluginAction action = (WWinPluginAction) desc.getAction();
        action.setActionSetId(actionSet.getDesc().getId());
        actionSet.addPluginAction(action);
        return desc;
    }

    /**
 * Returns the insertion point for a new contribution item.  Clients should
 * use this item as a reference point for insertAfter.
 *
 * @param startId the reference id for insertion
 * @param sortId the sorting id for the insertion.  If null then the item
 *		will be inserted at the end of all action sets.
 * @param mgr the target menu manager.
 * @param startVsEnd if <code>true</code> the items are added at the start of
 *		action with the same id; else they are added to the end
 * @return the insertion point, or null if not found.
 */
    public static IContributionItem findInsertionPoint(String startId, String sortId, IContributionManager mgr, boolean startVsEnd) {
        IContributionItem[] items = mgr.getItems();
        int insertIndex = 0;
        while (insertIndex < items.length) {
            if (startId.equals(items[insertIndex].getId())) break;
            ++insertIndex;
        }
        if (insertIndex >= items.length) return null;
        int compareMetric = 0;
        if (startVsEnd) compareMetric = 1;
        for (int nX = insertIndex + 1; nX < items.length; nX++) {
            IContributionItem item = items[nX];
            if (item instanceof IActionSetContributionItem) {
                if (sortId != null) {
                    String testId = ((IActionSetContributionItem) item).getActionSetId();
                    if (sortId.compareTo(testId) < compareMetric) break;
                }
                insertIndex = nX;
            } else {
                break;
            }
        }
        return items[insertIndex];
    }

    protected void insertAfter(IContributionManager mgr, String refId, IContributionItem item) {
        String actionSetId = actionSet.getDesc().getId();
        IContributionItem refItem = findInsertionPoint(refId, actionSetId, mgr, true);
        if (refItem != null) {
            mgr.insertAfter(refItem.getId(), item);
        } else {
            WorkbenchPlugin.log("Reference action not found: " + refId);
        }
    }

    /**
 * Read the actions within a config element.
 */
    public void readActionExtensions(PluginActionSet set, IWorkbenchWindow window, IActionBars bars) throws CoreException {
        this.actionSet = set;
        this.window = window;
        readElements(new IConfigurationElement[] { set.getConfigElement() });
        if (cache != null) {
            contribute(window, bars.getMenuManager(), bars.getToolBarManager(), true);
        } else {
            WorkbenchPlugin.log("Action Set is empty: " + set.getDesc().getId());
        }
    }

    /**
 * Implements abstract method to handle the provided XML element
 * in the registry.
 */
    protected boolean readElement(IConfigurationElement element) {
        String tag = element.getName();
        if (tag.equals(TAG_ACTION_SET)) {
            readElementChildren(element);
            return true;
        }
        return super.readElement(element);
    }
}
