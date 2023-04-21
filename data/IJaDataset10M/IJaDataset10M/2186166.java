package org.xvr.xvrengine.navigator.content;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;

/**
 * @author raffaello
 *
 */
public class ContextualMenuFilterListener implements IMenuListener {

    @Override
    public void menuAboutToShow(IMenuManager manager) {
        IContributionItem[] items = manager.getItems();
        String id;
        for (IContributionItem item : items) {
            if (item instanceof Separator) continue;
            id = item.getId();
            if (id != null && (!id.contains("org.xvr.xvrengine.actionprovider.ids") && !id.contains("org.eclipse.ui.") && !id.equals("team.main"))) {
                manager.remove(item);
            }
        }
    }
}
