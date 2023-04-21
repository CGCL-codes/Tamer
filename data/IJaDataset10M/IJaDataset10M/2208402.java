package org.metadon.client.widget;

import org.metadon.client.MetamapPanel;
import com.google.gwt.user.client.ui.DockPanel;

/**
 * 
 * @author Hannes Weingartner
 *
 */
public class AppContainer {

    private AppRootPanel parent;

    private DockPanel.DockLayoutConstant position;

    protected AppContainer(AppRootPanel _parent, Position _position) {
        parent = _parent;
        position = _position.get();
    }

    public AppVerticalPanel withVerticalPanel() {
        final AppVerticalPanel child = new AppVerticalPanel();
        parent.add(child);
        return child;
    }

    public AppHorizontalPanel withHorizontalPanel() {
        final AppHorizontalPanel child = new AppHorizontalPanel();
        parent.add(child);
        return child;
    }

    public AppContentPanel withContentPanel() {
        final AppContentPanel child = new AppContentPanel();
        parent.add(child);
        return child;
    }

    public MetamapPanel withMapContentPanel() {
        final MetamapPanel child = new MetamapPanel();
        parent.add(child);
        return child;
    }
}
