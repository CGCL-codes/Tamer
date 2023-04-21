package bigraph.biged.ui;

import org.eclipse.ui.IActionBars;
import org.eclipse.ui.views.properties.tabbed.IActionProvider;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;

public class PropertyActionProvider implements IActionProvider {

    public void setActionBars(final ITabbedPropertySheetPageContributor contributor, final IActionBars actionBars) {
        System.err.println("PropertyActionProvider.setActionBars()");
    }
}
