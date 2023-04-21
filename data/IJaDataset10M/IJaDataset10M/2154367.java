package rails.ui.swing.elements;

import java.awt.Insets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import rails.common.Config;

/**
 * A JButton which is able to hold/manage a RailsIcon (specifying what
 * text / icon is to be displayed)
 * 
 * @author Frederick Weld
 *
 */
public class RailsIconButton extends JButton {

    private static final long serialVersionUID = 1L;

    private static final Set<String> KEYS_TEXT_DISPLAY = new HashSet<String>(Arrays.asList(new String[] { "text and icon", "only text", "", null }));

    private static final Set<String> KEYS_ICON_DISPLAY = new HashSet<String>(Arrays.asList(new String[] { "text and icon", "only icon" }));

    private static Set<RailsIconButton> railsIconButtons = new HashSet<RailsIconButton>();

    /**
     * null value means that the button is not set up by an appropriate
     * RailsIcon (eg., by calling setText directly).
     */
    private RailsIcon railsIcon = null;

    public RailsIconButton(RailsIcon railsIcon) {
        super();
        setRailsIcon(railsIcon);
        this.setMargin(new Insets(2, 2, 2, 2));
        railsIconButtons.add(this);
    }

    public void setRailsIcon(RailsIcon railsIcon) {
        if (railsIcon == null) railsIcon = RailsIcon.NULL;
        this.railsIcon = railsIcon;
        showRailsIcon();
    }

    /**
     * Display according to configuration.
     * If no text/icon is attached, then icon/text is displayed as fallback
     * (irrespective of configuration).
     * Text becomes the tool tip text in case of icon-only display.
     */
    private void showRailsIcon() {
        if (railsIcon != null) {
            if (isIconAboveText()) {
                setVerticalTextPosition(SwingConstants.BOTTOM);
                setHorizontalTextPosition(SwingConstants.CENTER);
            } else {
                setVerticalTextPosition(SwingConstants.CENTER);
                setHorizontalTextPosition(SwingConstants.TRAILING);
            }
            if (isTextEnabled() || railsIcon.largeIcon == null) {
                super.setText(railsIcon.description);
            } else {
                super.setText(null);
            }
            if (isIconEnabled() || railsIcon.description == null) {
                if (isIconSizeSmall()) {
                    super.setIcon(railsIcon.smallIcon);
                } else {
                    super.setIcon(railsIcon.largeIcon);
                }
                if (!isTextEnabled()) {
                    super.setToolTipText(railsIcon.description);
                }
            } else {
                super.setIcon(null);
            }
        }
    }

    private boolean isTextEnabled() {
        return KEYS_TEXT_DISPLAY.contains(Config.get("button.iconText", ""));
    }

    private boolean isIconEnabled() {
        return KEYS_ICON_DISPLAY.contains(Config.get("button.iconText", ""));
    }

    private boolean isIconSizeSmall() {
        return !"large".equals(Config.get("button.iconSize"));
    }

    private boolean isIconAboveText() {
        return "above".equals(Config.get("button.iconPosition"));
    }

    /**
     * Should only be used if an arbitrary text is to displayed without icon.
     * In any other case, setRailsIcon should be used.
     */
    @Override
    public void setText(String text) {
        super.setText(text);
        setIcon(null);
        railsIcon = null;
    }

    /**
     * To be called upon change of button display type
     */
    public static void resetRailsIcons() {
        for (RailsIconButton rib : railsIconButtons) {
            rib.showRailsIcon();
        }
    }
}
