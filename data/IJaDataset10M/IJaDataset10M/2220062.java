package dsrmapeditor;

import javax.swing.JTabbedPane;

public class IconPanelsPanel extends JTabbedPane {

    private static final long serialVersionUID = 1L;

    public IconPanelsPanel(MapEditorMain m) {
        super();
        this.addTab("Floors", null, new FloorIconsPanel(m), "Floors");
        this.addTab("Floor scenery", null, new RaisedFloorIconsPanel(m), "Floor scenery");
        this.addTab("Walls", null, new WallIconsPanel(m), "Walls");
        this.addTab("Misc", null, new MiscIconsPanel(m), "Misc");
        this.addTab("Scenery", null, new SceneryIconsPanel(m), "Scenery");
    }
}
