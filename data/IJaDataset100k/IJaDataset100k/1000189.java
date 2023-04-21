package org.apache.fop.viewer;

import java.awt.event.ActionEvent;
import org.apache.fop.messaging.MessageHandler;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import java.net.*;

/**
 * Klasse f�r UI-Kommandos. Die Kommandos k�nnen in das Men�system oder
 * in eine Toolbar eingef�gt werden.<br>
 * <code>Commands</code> unterst�tzen mehrsprachigkeit.<br>
 * Durch �berschreiben der Methode <code>doit<code> kann die Klasse customisiert werden.
 * �ber die Methode <code>undoit</code> kann Undo-Funktionalit�t unterst�tzt werden.<br>
 *
 * @author Juergen.Verwohlt@jcatalog.com
 * @version 1.0 18.03.99
 */
public class Command extends AbstractAction {

    public static String IMAGE_DIR = "/org/apache/fop/viewer/Images/";

    public Command(String name) {
        this(name, (ImageIcon) null);
    }

    public Command(String name, ImageIcon anIcon) {
        super(name, anIcon);
    }

    public Command(String name, String iconName) {
        super(name);
        String path = IMAGE_DIR + iconName + ".gif";
        URL url = getClass().getResource(path);
        if (url == null) {
            MessageHandler.errorln("Icon not found: " + path);
        } else putValue(SMALL_ICON, new ImageIcon(url));
    }

    public void actionPerformed(ActionEvent e) {
        doit();
    }

    public void doit() {
        MessageHandler.errorln("Not implemented.");
    }

    public void undoit() {
        MessageHandler.errorln("Not implemented.");
    }
}
