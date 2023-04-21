package br.com.qotsa.j2me.util;

import br.com.qotsa.j2me.btsoftcontrol.language.ResourceFactory;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Francisco Guimar�es
 */
public class MessageBox extends Alert {

    public static final Command YES = new Command(ResourceFactory.getResource().getYesMesssage(), Command.OK, 0);

    public static final Command NO = new Command(ResourceFactory.getResource().getNoMesssage(), Command.OK, 2);

    public MessageBox(String title, String alertText, Image alertImage, AlertType alertType, CommandListener commandListener) {
        super(title, alertText, alertImage, alertType);
        addCommand(YES);
        addCommand(NO);
        setCommandListener(commandListener);
    }
}
