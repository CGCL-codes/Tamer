package eu.sacrej.bggtool.panels;

import eu.sacrej.bggtool.*;
import javax.microedition.lcdui.Command;
import gr.bluevibe.fire.components.Component;
import gr.bluevibe.fire.components.Panel;
import gr.bluevibe.fire.components.Row;
import gr.bluevibe.fire.displayables.FireScreen;
import gr.bluevibe.fire.util.CommandListener;
import gr.bluevibe.fire.util.FireIO;
import gr.bluevibe.fire.util.Lang;

/**
 * Generic error screen
 * @author bkirman
 *
 */
public class ErrorPanel extends Panel implements CommandListener {

    private BGGTool midlet;

    private Panel next_panel;

    private Command ok;

    /**
	 * 
	 * @param midlet
	 * @param next_panel The panel to be shown once finish() is called 
	 */
    public ErrorPanel(BGGTool midlet, String message, Panel next_panel) {
        super();
        this.midlet = midlet;
        this.next_panel = next_panel;
        setCommandListener(this);
        setLabel(Lang.get("Error"));
        this.ok = new Command(Lang.get("OK"), Command.BACK, 1);
        addCommand(ok);
        add(new Separator(Lang.get("There has been an error!"), FireIO.getLocalImage("warning")));
        add(new Row(message));
    }

    public void commandAction(Command cmd, Component c) {
        midlet.screen.setCurrent(next_panel, FireScreen.RIGHT);
    }
}
