package org.jhotdraw.app.action;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.AbstractAction;
import org.jhotdraw.app.DefaultOSXApplication;

/**
 * OSXTogglePaletteAction.
 * 
 * @author Werner Randelshofer.
 * @version 1.0 June 11, 2006 Created.
 */
public class OSXTogglePaletteAction extends AbstractAction {

    private Window palette;

    private DefaultOSXApplication app;

    private WindowListener windowHandler;

    /** Creates a new instance. */
    public OSXTogglePaletteAction(DefaultOSXApplication app, Window palette, String label) {
        super(label);
        this.app = app;
        windowHandler = new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                putValue(Actions.SELECTED_KEY, false);
            }
        };
        setPalette(palette);
        putValue(Actions.SELECTED_KEY, true);
    }

    public void putValue(String key, Object newValue) {
        super.putValue(key, newValue);
        if (key == Actions.SELECTED_KEY) {
            if (palette != null) {
                boolean b = (Boolean) newValue;
                if (b) {
                    app.addPalette(palette);
                    palette.setVisible(true);
                } else {
                    app.removePalette(palette);
                    palette.setVisible(false);
                }
            }
        }
    }

    public void setPalette(Window newValue) {
        if (palette != null) {
            palette.removeWindowListener(windowHandler);
        }
        palette = newValue;
        if (palette != null) {
            palette.addWindowListener(windowHandler);
            if (getValue(Actions.SELECTED_KEY) == Boolean.TRUE) {
                app.addPalette(palette);
                palette.setVisible(true);
            } else {
                app.removePalette(palette);
                palette.setVisible(false);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (palette != null) {
            putValue(Actions.SELECTED_KEY, !palette.isVisible());
        }
    }
}
