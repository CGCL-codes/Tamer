package net.assimilator.utilities.console.graph;

import net.assimilator.utilities.console.serviceui.ServiceUIPanel;
import javax.swing.*;
import java.awt.*;

/**
 * Container for the service graph view and the graph force controller UI.
 *
 * @author Kevin Hartig
 * @version $Id: ServiceGraphPanel.java 295 2007-09-24 01:09:27Z larrymitchell $
 */
public class ServiceGraphPanel extends JPanel {

    ServiceUIPanel serviceUIPanel;

    JSplitPane splitPane;

    public ServiceGraphPanel(ServiceUIPanel serviceUIPanel) {
        super();
        this.serviceUIPanel = serviceUIPanel;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLoweredBevelBorder());
        setPreferredSize(new Dimension(500, 300));
        splitPane = new JSplitPane();
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(false);
        splitPane.setResizeWeight(1);
        splitPane.setDividerLocation(2000);
        splitPane.setLeftComponent(new JPanel());
        splitPane.setRightComponent(new JPanel());
        add(splitPane);
    }

    /**
     * Adds a service graph view to this panel. The behaviour of adding a new view
     * is handled in this method.
     *
     * @param serviceGraphView The service graph object to display in the panel.
     */
    public void addGraphView(ServiceGraphView serviceGraphView) {
        remove(splitPane);
        serviceGraphView.setServiceUIPanel(serviceUIPanel);
        splitPane.setLeftComponent(serviceGraphView.getDisplay());
        splitPane.setRightComponent(new ServiceGraphController(serviceGraphView));
        reset();
        add(splitPane);
    }

    public void revalidate() {
        super.revalidate();
        reset();
    }

    private void reset() {
        if (splitPane != null) {
            splitPane.resetToPreferredSizes();
            splitPane.setDividerLocation(2000);
        }
    }
}
