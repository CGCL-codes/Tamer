package at.rc.tacos.client.update;

import java.net.URL;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.update.search.BackLevelFilter;
import org.eclipse.update.search.EnvironmentFilter;
import org.eclipse.update.search.UpdateSearchRequest;
import org.eclipse.update.search.UpdateSearchScope;
import org.eclipse.update.ui.UpdateJob;
import org.eclipse.update.ui.UpdateManagerUI;

/**
 * 	Searches for new features that the current application does NOT have.
 * 	@author mheiss
 */
public class AddExtensionAction extends AbstractUpdateAction {

    /**
	 * Default class constructor
	 */
    public AddExtensionAction() {
        setId("at.rc.tacos.client.update.addExtension");
        setText(Messages.getString("AddExtensionAction.searchForNewFeatures"));
        setToolTipText(Messages.getString("AddExtensionAction.searchForNewFeaturesToolTipp"));
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/software-update-available.png"));
    }

    @Override
    protected void run(final IWorkbenchWindow window) {
        BusyIndicator.showWhile(window.getShell().getDisplay(), new Runnable() {

            @Override
            public void run() {
                UpdateJob job = new UpdateJob(Messages.getString("AddExtensionAction.updateJob"), getSearchRequest());
                UpdateManagerUI.openInstaller(window.getShell(), job);
            }
        });
    }

    /**
	 * Helper method to create a search request
	 */
    private UpdateSearchRequest getSearchRequest() {
        Plugin plugin = Activator.getDefault();
        Preferences prefs = plugin.getPluginPreferences();
        String updateSite = prefs.getString("updateSite");
        UpdateSearchRequest result = new UpdateSearchRequest(UpdateSearchRequest.createDefaultSiteSearchCategory(), new UpdateSearchScope());
        result.addFilter(new BackLevelFilter());
        result.addFilter(new EnvironmentFilter());
        UpdateSearchScope scope = new UpdateSearchScope();
        try {
            scope.addSearchSite("TACOS Update Site", new URL(updateSite), null);
        } catch (Exception e) {
        }
        result.setScope(scope);
        return result;
    }
}
