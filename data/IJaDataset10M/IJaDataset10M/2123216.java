package btaddon.MenuForm;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.List;
import btaddon.MediaForm.Media;
import core.Action;

public class FFormatChoice extends Form implements CommandListener {

    private static btaddon.Menu menu;

    public static ChoiceGroup vyber;

    public FFormatChoice(btaddon.Menu m) {
        super("Choose a format");
        menu = m;
        setTicker(Media.ticker);
        vyber = new ChoiceGroup("Available media:", Choice.EXCLUSIVE);
        append(vyber);
        setCommandListener(this);
        addCommand(Action.back);
        addCommand(Action.ok);
        addCommand(Action.msgqueue);
    }

    public void commandAction(Command c, Displayable dis) {
        if (c == Action.back) {
            menu.FormExchange(-1);
        } else if (c == Action.ok || c == List.SELECT_COMMAND) {
            if (vyber.size() == 0) return;
            String tmp = vyber.getString(vyber.getSelectedIndex());
            if (tmp.equals("text")) {
                menu.FormShow(menu.text);
            } else if (tmp.startsWith("audio") || tmp.startsWith("video")) {
                menu.audiovideo.isAudio(true);
                menu.FormShow(menu.audiovideo);
                menu.audiovideo.format = tmp;
            } else if (tmp.startsWith("image")) {
                menu.FormShow(menu.foto);
                menu.foto.format = tmp;
            }
            Media.ticker.setString("Send " + tmp);
        } else if (c == Action.msgqueue) {
            menu.FormShow(menu.mnotifier);
        }
    }

    public static void insertFormats(final String[] formats) {
        int i;
        RemoveDevices();
        if (formats == null) return;
        for (i = 0; i < formats.length; i++) vyber.append(formats[i], null);
    }

    public static void RemoveDevices() {
        vyber.deleteAll();
    }
}
