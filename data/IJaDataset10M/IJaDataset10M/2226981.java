package net.sourceforge.pyrus.bundle.applet;

import javax.swing.JOptionPane;
import net.sourceforge.pyrus.hal.annotation.Dependency;
import net.sourceforge.pyrus.screen.api.Applet;
import net.sourceforge.pyrus.screen.api.Screen;

public class ConfigApplet implements Applet {

    @Dependency
    private Screen screen;

    private String id;

    public String getIcon() {
        return "config";
    }

    public String getName() {
        return "Configuración";
    }

    public void run() {
        JOptionPane.showMessageDialog(null, "TODO!!!");
    }

    public String getDescription() {
        return "Restart applet";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void start() {
    }

    public void stop() {
    }
}
