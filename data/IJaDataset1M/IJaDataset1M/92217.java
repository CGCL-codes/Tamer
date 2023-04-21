package de.hpi.eworld.simulationstatistic;

import javax.swing.JPanel;
import de.hpi.eworld.core.ui.docking.DockingView;

public class MapVisualizationDock extends DockingView {

    private static final long serialVersionUID = -307625095470681979L;

    public MapVisualizationDock(JPanel mainPanel) {
        super("Map Visualization", mainPanel);
    }

    @Override
    public String getDockIdentifier() {
        return "a5bce220-77a6-11df-93f2-0800200c9a66";
    }
}
