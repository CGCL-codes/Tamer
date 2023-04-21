package org.kku.gui;

import com.jgoodies.plaf.*;
import com.jgoodies.plaf.plastic.*;
import com.jgoodies.plaf.plastic.theme.*;
import org.kku.gui.remote.*;
import org.kku.gui.util.*;
import org.kku.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import java.awt.*;
import java.awt.event.*;
import java.util.prefs.*;

public class DukeJukeBox {

    private static String frameFullScreen = "fullScreen";

    private static String frameXPreference = "frameX";

    private static String frameYPreference = "frameY";

    private static String frameHeightPreference = "frameHeight";

    private static String frameWidthPreference = "frameWidth";

    private FullScreenFrame fullScreenFrame;

    private DukeJukeBoxPanel content;

    private boolean fullScreen;

    private RemoteUIControl remoteUIControl;

    public DukeJukeBox() {
        fullScreen = getPrefs().getBoolean(frameFullScreen, false);
        fullScreen = false;
        fullScreenFrame = new FullScreenFrame("The Duke's JukeBox", fullScreen);
        init();
    }

    private void init() {
        JFrame frame;
        content = new DukeJukeBoxPanel(this);
        frame = fullScreenFrame.getFrame();
        frame.getContentPane().add(BorderLayout.CENTER, content);
        if (!fullScreen) {
            frame.setSize(500, 500);
            getPreferences();
        }
        frame.setVisible(true);
        fullScreenFrame.addWindowListener(getWindowListener());
        remoteUIControl = new RemoteUIControl(this);
    }

    public void setFullScreen(boolean fullScreen) {
        fullScreenFrame.setFullScreen(fullScreen);
        fullScreenFrame.getFrame().getContentPane().add(BorderLayout.CENTER, content);
        fullScreenFrame.getFrame().show();
        this.fullScreen = fullScreen;
        putPreferences();
    }

    public DukeJukeBoxPanel getDukeJukeBoxPanel() {
        return content;
    }

    public WindowListener getWindowListener() {
        return new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                putPreferences();
                System.exit(1);
            }
        };
    }

    private Preferences getPrefs() {
        return AppPreferences.getPreferences(getClass());
    }

    private void getPreferences() {
        Preferences prefs;
        Rectangle newBounds;
        Rectangle oldBounds;
        prefs = getPrefs();
        oldBounds = fullScreenFrame.getFrame().getBounds();
        newBounds = new Rectangle();
        newBounds.x = prefs.getInt(frameXPreference, oldBounds.x);
        newBounds.y = prefs.getInt(frameYPreference, oldBounds.y);
        newBounds.height = prefs.getInt(frameHeightPreference, oldBounds.height);
        newBounds.width = prefs.getInt(frameWidthPreference, oldBounds.width);
        fullScreenFrame.getFrame().setBounds(newBounds);
    }

    public void putPreferences() {
        Preferences prefs;
        Rectangle bounds;
        prefs = AppPreferences.getPreferences(getClass());
        if (!fullScreen) {
            bounds = fullScreenFrame.getFrame().getBounds();
            prefs.putInt(frameXPreference, bounds.x);
            prefs.putInt(frameYPreference, bounds.y);
            prefs.putInt(frameHeightPreference, bounds.height);
            prefs.putInt(frameWidthPreference, bounds.width);
        } else {
            prefs.putBoolean(frameFullScreen, fullScreen);
        }
    }

    public static void main(String[] args) throws Exception {
        Db.initDatabase();
        PlasticLookAndFeel.setMyCurrentTheme(new SkyBluer());
        UIManager.put("Button.margin", new InsetsUIResource(4, 4, 4, 4));
        UIManager.put("ToggleButton.margin", new InsetsUIResource(4, 4, 4, 4));
        UIManager.put("SplitPaneDivider.border", new BorderUIResource(new EmptyBorder(10, 10, 2, 2)));
        UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        new DukeJukeBox();
    }
}
