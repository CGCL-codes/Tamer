package MyForms;

import javax.microedition.lcdui.*;

/**
 * Instanzen dieser Klasse repräsentieren ein Fenster in der eine Nachricht
 * angezeigt wird.
 * @author Nico
 */
public class FormMessage extends Form {

    /**
     * Erzeugt ein neues Fenster mit einer Nachricht.
     * @param title  Der Titel des Fensters.
     * @param message  Die Nachricht die angezeigt werden soll.
     */
    public FormMessage(String title, String message) {
        super(title);
        this.append(message);
    }
}
