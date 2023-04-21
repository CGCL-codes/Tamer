package org.escapek.gui.objectmanager.ui.actions;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.escapek.gui.objectmanager.ui.internal.Activator;
import org.escapek.gui.objectmanager.ui.internal.preferences.PreferencesConstants;
import org.escapek.gui.objectmanager.ui.navigator.ObjectBrowser;

public class HierarchicalBrowserAction implements IViewActionDelegate {

    private int defaultStyle;

    private ObjectBrowser browser;

    private IPreferencesService prefService;

    public void init(IViewPart view) {
        Assert.isTrue(view instanceof ObjectBrowser);
        this.browser = (ObjectBrowser) view;
        prefService = Platform.getPreferencesService();
        defaultStyle = prefService.getInt(Activator.PLUGIN_ID, PreferencesConstants.KEY_OBJECT_BROWSER_STYLE, PreferencesConstants.VAL_OBJECT_BROWSER_STYLE_FLAT, null);
    }

    public void selectionChanged(IAction action, ISelection selection) {
        if (defaultStyle == PreferencesConstants.VAL_OBJECT_BROWSER_STYLE_HIERARCHICAL) {
            action.setChecked(true);
        }
    }

    public void run(IAction action) {
        IEclipsePreferences node = new InstanceScope().getNode(Activator.PLUGIN_ID);
        node.putInt(PreferencesConstants.KEY_OBJECT_BROWSER_STYLE, PreferencesConstants.VAL_OBJECT_BROWSER_STYLE_HIERARCHICAL);
        browser.refresh();
    }
}
