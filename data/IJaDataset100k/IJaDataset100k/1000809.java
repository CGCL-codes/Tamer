package org.carabiner.infopanel.inspector;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.carabiner.infopanel.BasicCarabinerMessage;
import org.carabiner.util.DataUtils;

/**
 * A Warning represents something in a GUI control that is probably not correct.
 * Rules generate Warnings as part of the inspection process.
 *
 * @author Ben Rady (benrady@gmail.com)
 *
 */
public class Warning extends BasicCarabinerMessage {

    /**
   * Initializes a new instance of the <code>Warning</code> class.
   *
   * @param component Component
   * @param name String
   * @param message String
   */
    public Warning(Component component, String name, String message) {
        this(component, name, message, new ImageIcon(DataUtils.class.getResource("warning.jpg")), null);
    }

    /**
   * Initializes a new instance of the <code>Warning</code> class.
   *
   * @param component Component
   * @param name String
   * @param message String
   * @param highlightColor Color
   */
    public Warning(Component component, String name, String message, Color highlightColor) {
        this(component, name, message, new ImageIcon(DataUtils.class.getResource("warning.jpg")), highlightColor);
    }

    /**
   * Initializes a new instance of the <code>Warning</code> class.
   *
   * @param component Component
   * @param name String
   * @param message String
   * @param icon Icon
   * @param highlightColor color
   */
    public Warning(Component component, String name, String message, Icon icon, Color highlightColor) {
        super(component, name, message, icon, highlightColor);
    }
}
