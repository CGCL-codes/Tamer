package cardwall.client.view;

import cardwall.client.view.WallPanel;
import cardwall.client.view.HeaderPanel;
import com.google.gwt.user.client.ui.DockPanel;

public class MainPanel extends DockPanel {

    private WallPanel wallPanel;

    private HeaderPanel headerPanel;

    public MainPanel(HeaderPanel headerPanel, WallPanel wallPanel) {
        this.headerPanel = headerPanel;
        this.wallPanel = wallPanel;
    }

    public void init() {
        setSize("100%", "100%");
        add(headerPanel, DockPanel.NORTH);
        setCellHeight(headerPanel, "15px");
        add(wallPanel, DockPanel.CENTER);
    }
}
