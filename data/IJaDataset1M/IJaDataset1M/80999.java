package myComponents.myToolbar;

import java.awt.Point;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import tools.MySeriesLogger;

/**
 *
 * @author ssoldatos
 */
public class ToolbarSeperator extends JLabel implements ToolbarButtonActions {

    private static final long serialVersionUID = 3249182490184L;

    private int actionName = NONE;

    private String tooltip;

    public Point origin = new Point();

    public Point startPoint = new Point();

    public ToolbarSeperator() {
        super();
    }

    ToolbarSeperator(int actionName, String tooltip, String image) {
        super();
        this.actionName = actionName;
        this.tooltip = tooltip;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
        if (image.equals("")) {
            setIcon(new ImageIcon(getClass().getResource("/images/sep.png")));
        } else {
            setIcon(new ImageIcon(getClass().getResource("/images/" + image)));
        }
        setToolTipText(tooltip);
        MySeriesLogger.logger.log(Level.FINE, "Toolbar Seperator was created");
    }

    /**
     * @return the actionName
     */
    public int getActionName() {
        return actionName;
    }

    /**
     * @param actionName the actionName to set
     */
    public void setActionName(int actionName) {
        this.actionName = actionName;
    }

    /**
     * @return the tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * @param tooltip the tooltip to set
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
}
