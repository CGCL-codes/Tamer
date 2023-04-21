package org.nakedobjects.plugins.dnd.help;

import org.nakedobjects.plugins.dnd.drawing.Location;
import org.nakedobjects.plugins.dnd.view.View;
import org.nakedobjects.plugins.dnd.view.Viewer;

public class InternalHelpViewer implements HelpViewer {

    private final Viewer viewer;

    public InternalHelpViewer(final Viewer viewer) {
        this.viewer = viewer;
    }

    public void open(final Location location, final String name, final String description, final String help) {
        viewer.clearAction();
        final View helpView = new HelpView(name, description, help);
        location.add(20, 20);
        helpView.setLocation(location);
        viewer.setOverlayView(helpView);
    }
}
