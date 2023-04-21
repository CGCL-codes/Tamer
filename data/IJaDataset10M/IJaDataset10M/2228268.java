package com.cell.midi;

import javax.swing.UIManager;
import com.cell.j2se.CAppBridge;

public class Config extends com.cell.util.Config {

    public static int FRAME_WIDTH = 800;

    public static int FRAME_HEIGHT = 450;

    public static int DEFAULT_FPS = 60;

    static void init() {
        CAppBridge.init();
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}
