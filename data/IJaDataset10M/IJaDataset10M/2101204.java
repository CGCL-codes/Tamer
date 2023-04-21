package tethyspict.gui;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public abstract class AbstractTethysTabbedPanel extends JPanel implements TethysTabbedPaneInterface {

    private final String name, tooltip;

    private final ImageIcon icon;

    public AbstractTethysTabbedPanel(String name, String tooltip, ImageIcon icon) {
        this.name = name;
        this.tooltip = tooltip;
        this.icon = icon;
    }

    public String getTitel() {
        return this.name;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public ImageIcon getIcon() {
        return this.icon;
    }
}
