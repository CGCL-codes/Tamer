package eu.sacrej.bggtool.panels;

import eu.sacrej.bggtool.*;
import eu.sacrej.bggtool.net.*;
import javax.microedition.lcdui.Command;
import java.util.Calendar;
import java.util.Hashtable;
import gr.bluevibe.fire.components.Component;
import gr.bluevibe.fire.components.DateTimeRow;
import gr.bluevibe.fire.components.Panel;
import gr.bluevibe.fire.components.Row;
import gr.bluevibe.fire.displayables.FireScreen;
import gr.bluevibe.fire.util.CommandListener;
import gr.bluevibe.fire.util.FireIO;
import gr.bluevibe.fire.util.Lang;

/**
 * Log plays for a game. Uses a modified version of FIRE DTR since most BGG users will be logging plays in the past.
 * @author bkirman
 *
 */
public class LogPlayPanel extends Panel implements CommandListener, NetManagerListener {

    private BGGTool midlet;

    private Command back, log;

    private GameDetailsPanel prev;

    private String gameid, gamename;

    private DateTimeRow dtr;

    private Row num_field;

    public LogPlayPanel(BGGTool midlet, GameDetailsPanel previous, String gameid, String gamename) {
        super();
        this.midlet = midlet;
        this.prev = previous;
        this.gameid = gameid;
        this.gamename = gamename;
        setCommandListener(this);
        setLabel(Lang.get("Logging Play"));
        back = new Command(Lang.get("Back"), Command.EXIT, 1);
        addCommand(back);
        add(new Separator(Lang.get("Logging play of ") + gamename));
        dtr = new DateTimeRow(Calendar.getInstance(), false);
        dtr.setLabel(Lang.get("When?"));
        add(dtr);
        num_field = new Row("1");
        num_field.setEditable(true);
        num_field.setLabel(Lang.get("How many times?"), FireScreen.defaultLabelFont, new Integer(midlet.screen.getWidth() / 3), FireScreen.CENTRE);
        num_field.setTextBoxSize(3);
        add(num_field);
        log = new Command("", Command.OK, 1);
        Row save_button = new Row(Lang.get("Save"));
        save_button.setAlignment(FireScreen.CENTRE);
        save_button.addCommand(log);
        save_button.setCommandListener(this);
        save_button.setImage(FireIO.getLocalImage("filesave"));
        add(save_button);
    }

    public void commandAction(Command cmd, Component c) {
        if (cmd == back) {
            midlet.screen.setCurrent(new MenuPanel(midlet), FireScreen.RIGHT);
        }
        if (cmd == log) {
            Calendar date = dtr.getDate();
            midlet.net.logPlay(this, gameid, new Integer(date.get(Calendar.YEAR)).toString(), new Integer(date.get(Calendar.MONTH) + 1).toString(), new Integer(date.get(Calendar.DAY_OF_MONTH)).toString(), num_field.getText());
            midlet.screen.setCurrent(new LoadingPanel(midlet, Lang.get("Logging Play...")), FireScreen.LEFT);
        }
    }

    public void netResponse(GameList response) {
        midlet.screen.setCurrent(prev, FireScreen.RIGHT);
    }

    public void netError(String error) {
    }
}
