package de.ios.framework.gui;

import java.awt.*;
import java.util.*;

/**
 * This class is<br>
 * <h1><b><i>Deprecated</i></b></h1>.
 * and should be removed in the future.
 * <b>Don't use this class!</b>.
 * Before this class was used by "MessageDialog", but now 
 * this uses jdk1.1-methods to handle events.<p>
 * <i>Thus:</i> <b>DO-NOT-USE-THIS-CLASS</b>!.
 */
public class MessageDialogEvents extends Dialog {

    /**
   * Konstruktor mit Angaben zu dem uebergeordneten Fenster und dem Titel.
   */
    public MessageDialogEvents(String title, boolean mode) {
        super(new Frame(), title, mode);
    }

    /**
   *
   */
    public void closeWindow() {
        this.hide();
        this.dispose();
    }

    /**
   * Event-Handling
   */
    public boolean handleEvent(Event event) {
        boolean handled = false;
        switch(event.id) {
            case Event.ACTION_EVENT:
                if (debug) System.out.println("ACTION_EVENT: " + event);
                if (event.target == b1) {
                    if (debug) {
                        System.out.println("Button1");
                    }
                    answer = 1;
                    handled = true;
                } else if (event.target == b2) {
                    if (debug) {
                        System.out.println("Button2");
                    }
                    answer = 2;
                    handled = true;
                } else if (event.target == b3) {
                    if (debug) {
                        System.out.println("Button3");
                    }
                    answer = 3;
                    handled = true;
                } else if (debug) {
                    System.out.println("WARNUNG: Ein unbekannter Event ist aufgetreten!");
                }
                if (handled) {
                    this.hide();
                    this.dispose();
                }
                break;
            case Event.KEY_ACTION:
                if (debug) {
                    System.out.print("Action_");
                }
            case Event.KEY_PRESS:
                if (debug) System.out.println("KEY_PRESS: " + event);
                if ((event.target == b1) && (event.key == event.ENTER)) {
                    if (debug) {
                        System.out.println("Button1");
                    }
                    answer = 1;
                    handled = true;
                } else if ((event.target == b2) && (event.key == event.ENTER)) {
                    if (debug) {
                        System.out.println("Button2");
                    }
                    answer = 2;
                    handled = true;
                } else if ((event.target == b3) && (event.key == event.ENTER)) {
                    if (debug) {
                        System.out.println("Button3");
                    }
                    answer = 3;
                    handled = true;
                } else if ((b1 != null) && (event.key == key1)) {
                    if (debug) System.out.println("Button1");
                    answer = 1;
                    handled = true;
                } else if ((b2 != null) && (event.key == key2)) {
                    if (debug) System.out.println("Button2");
                    answer = 2;
                    handled = true;
                } else if ((b3 != null) && (event.key == key3)) {
                    if (debug) System.out.println("Button3");
                    answer = 3;
                    handled = true;
                }
                if (handled) {
                    this.hide();
                    this.dispose();
                }
                break;
            case Event.WINDOW_DESTROY:
                if (debug) System.out.println("Destroy");
                closeWindow();
                break;
            case Event.WINDOW_ICONIFY:
                if (debug) {
                    System.out.println("Iconify");
                }
                handled = true;
                break;
            case Event.WINDOW_DEICONIFY:
                if (debug) {
                    System.out.println("DeIconify");
                }
                handled = true;
                break;
            case Event.WINDOW_MOVED:
                if (debug) {
                    System.out.println("Moved");
                }
                handled = true;
                break;
            case Event.MOUSE_DOWN:
                if (debug) {
                    System.out.println("Mouse_Down");
                }
                break;
            case Event.MOUSE_UP:
                if (debug) {
                    System.out.println("Mouse_Up");
                }
                break;
            case Event.MOUSE_DRAG:
                if (debug) {
                    System.out.println("Mouse_Drag");
                }
                break;
            case Event.KEY_RELEASE:
                if (debug) {
                    System.out.println("Key_Release");
                }
                break;
            case Event.KEY_ACTION_RELEASE:
                if (debug) {
                    System.out.println("Action_KEY_RELEASE");
                }
                break;
            case Event.GOT_FOCUS:
            case Event.LOST_FOCUS:
            case Event.MOUSE_ENTER:
            case Event.MOUSE_EXIT:
            case Event.MOUSE_MOVE:
                if (debug) {
                    System.out.print(".");
                }
                break;
            default:
                if (debug) {
                    System.out.println("WARNUNG: Unerwarteter Event " + event);
                }
                break;
        }
        if (handled) return true; else return super.handleEvent(event);
    }

    /**
   * Variabeln der Klasse
   */
    int answer = 0;

    Panel msg;

    Button b1 = null, b2 = null, b3 = null;

    int key1 = 0, key2 = 0, key3 = 0;

    Panel buttons;

    /**
   * Flag, das interne Debugging-Ausgaben aktiviert
   */
    static final boolean debug = false;
}

;
