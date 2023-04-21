package org.RSG.Application;

import org.RSG.Interface.*;
import org.RSG.Resources.*;

public class Application {

    public static String colour = "255000000255";

    public static ImageWindow inFocus;

    public static int IMGN = 1;

    public static AppDesktop appdesktop;

    public static int currentTool = 0;

    public Application() {
        Icons icons = new Icons();
        icons.LoadIcon("LayerVisible.png", 1);
        icons.LoadIcon("LayerHidden.png", 2);
        icons.LoadIcon("Pencil_Icon.png", 3);
        icons.LoadIcon("Picker_Icon.png", 4);
        icons.LoadIcon("FloodFill_Icon.png", 5);
        appdesktop = new AppDesktop(640, 480, "RainSquared Owl");
        org.RSG.Interface.MenuBar menubar = new org.RSG.Interface.MenuBar();
        Tools toolkit = new Tools();
        appdesktop.window.setJMenuBar(menubar.menubar);
        appdesktop.desktop.add(toolkit.window);
    }

    public static void main(String[] args) {
        new Application();
    }

    public static void Save() {
        if (Application.inFocus != null) {
            System.out.println("Saving!");
            new BMP(Application.inFocus.ImagePanel.pixels, Application.inFocus.width, Application.inFocus.height);
        }
    }
}
