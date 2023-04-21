package jm2pc.client.plugins;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import jm2pc.client.JM2PCMIDlet;

public class CanvasPlugins extends Canvas implements CommandListener {

    private JM2PCMIDlet midlet;

    private int width;

    private int height;

    private String plugin;

    private String lastCommand;

    private Command cmBack;

    public CanvasPlugins(JM2PCMIDlet midlet) {
        this.midlet = midlet;
        width = getWidth();
        height = getHeight();
        plugin = null;
        lastCommand = null;
        cmBack = new Command(midlet.messages.getMessage("back"), Command.BACK, 1);
        addCommand(cmBack);
        setCommandListener(this);
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public void paint(Graphics g) {
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
        g.setColor(0, 0, 230);
        g.drawString("JM2PC", width / 2, 5, Graphics.TOP | Graphics.HCENTER);
        if (lastCommand != null) {
            g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_LARGE));
            g.setColor(0, 230, 0);
            g.drawString(lastCommand, width / 2, height / 2, Graphics.BASELINE | Graphics.HCENTER);
        }
        if (plugin != null) {
            g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
            g.setColor(230, 230, 230);
            g.drawString(plugin, width / 2, height - 5, Graphics.BOTTOM | Graphics.HCENTER);
        }
    }

    public void keyPressed(int keyCode) {
        if (plugin != null) {
            lastCommand = getKeyName(keyCode);
            midlet.sendCommand(plugin, lastCommand);
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == cmBack) {
            midlet.getDisplayManager().popDisplayable();
        }
    }
}
