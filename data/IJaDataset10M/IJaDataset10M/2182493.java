package edu.psu.its.lionshare.gui;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import com.limegroup.gnutella.gui.themes.ThemeObserver;
import com.limegroup.gnutella.gui.themes.ThemeSettings;

/**
 * This class is really just a hack to make it easier to get the media player 
 * buttons to display correctly.
 */
public final class StatusBarButton extends JButton implements ThemeObserver {

    private final Icon icon;

    public StatusBarButton(Icon icon) {
        this.icon = icon;
        updateTheme();
    }

    public void updateTheme() {
        setContentAreaFilled(false);
        setBorderPainted(ThemeSettings.isNativeOSXTheme());
        setIcon(icon);
        setHorizontalAlignment(SwingConstants.CENTER);
        setPressedIcon(icon);
        setPreferredSize(new Dimension(getIcon().getIconWidth(), getIcon().getIconHeight()));
        setMargin(new Insets(0, 0, 0, 0));
    }
}
