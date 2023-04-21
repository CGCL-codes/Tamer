package ppr.jiasharm.framework;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Objekte dieser Klasse verwalten Einstellungen und Programmparameter.
 */
public class Settings {

    /** Parameterbezeichnung f�r den TCP/IP-Port. */
    public static final String PORT = "-port";

    /** Parameterbezeichnung f�r den Klassenpfad. */
    public static final String CLASSPATH = "-classpath";

    /** Parameterbezeichnung f�r den Dateiname der Klassenauflistungsdatei. */
    public static final String CLASSLISTFILENAME = "-classlist";

    /** Parameterbezeichnung f�r die Hilfeoption. Ausgabe eines Hilfetextes. */
    public static final String HELP = "-h";

    /** Verwaltet die Einstellungen. */
    private Hashtable settings;

    /** Konstante. Zeichen des Subtraktionssymbols/Bindestrichs. */
    public static final String MINUSZEICHEN = "-";

    /** Zustand des endlichen Automaten, in dem der Parametername gelesen
     *  wird. */
    private static final int READPARAMNAME = 0;

    /** Zustand des endlichen Automaten, in dem der Parameterwert gelesen
     *  wird. */
    private static final int READVALUE = 1;

    /** Zustand des endlichen Automaten, in dem Paramname und Wert hinzugef�gt
     *  werten. */
    private static final int ADD = 2;

    /** Zustand des endlichen Automaten, der eine Fehlerhafte Verarbeitung 
     *  signalisiert. */
    private static final int ERROR = 3;

    /** Erzeugt Objekte dieser Klasse. */
    public Settings() {
        this.settings = new Hashtable();
    }

    /**
     * F�gt der Einstellungsliste einen Parameter hinzu.
     *
     * @param paramName  Parametername, Bezeichnung dieser Einstellung.
     * @param value      Wert dieser Einstellung
     */
    public void add(String paramName, String value) {
        this.settings.put(paramName, value);
    }

    /**
     * Verarbeitet eine Parameterauflistung, wie sie als Komandozeilen�bergaben
     * einer main-Methode �bergeben werden.
     *
     * @param args  Komandozeilenparameter, wie sie einer Main-Methode �bergeben
     *              werden.
     */
    public void add(String[] args) {
        String element;
        String name = "";
        String value = "";
        Integer i = 0;
        int state = READPARAMNAME;
        while (((state != ERROR) && (i < args.length)) || (state == ADD)) {
            switch(state) {
                case READPARAMNAME:
                    element = args[i];
                    if (this.isParam(element)) {
                        name = element;
                        state = READVALUE;
                        i++;
                    } else {
                        state = ERROR;
                    }
                    break;
                case READVALUE:
                    element = args[i];
                    if (this.isValue(element)) {
                        value = element;
                        state = ADD;
                        i++;
                    } else {
                        value = "";
                        state = ADD;
                    }
                    break;
                case ADD:
                    this.add(name, value);
                    name = "";
                    value = "";
                    state = READPARAMNAME;
                    break;
                default:
                    System.out.println("Error in Settings.add");
                    break;
            }
        }
        if (state == READVALUE) {
            this.add(name, "");
        }
        if (state == ERROR) {
            throw new IllegalArgumentException("Parameter fehlerhaft! Index: " + i.toString());
        }
    }

    /**
     * Pr�ft, ob der �bergebene Text dem Format einer Parameterbezeichnung
     * entspricht, also mit einem minus beginnt.
     *
     * @param text  Zu �berpr�fende Parameterbezeichnung
     * @return  true, wenn der �bergebene Text, dem Format eines Parameters
     *          entspricht, sonst false.
     */
    private Boolean isParam(String text) {
        Pattern p = Pattern.compile("-[a-z]+");
        Matcher m = p.matcher(text);
        return m.matches();
    }

    /**
     * Pr�ft ob der �bergebene Text dem Format eines Parameterwertes entspricht.
     *
     * @param text  Zu �berpr�fender Text.
     * @return      true, wenn der �bergebene Text dem Format eines
     *              Parameterwertes entspricht, sonst false.
     */
    public Boolean isValue(String text) {
        if (text.length() > 0) {
            return (MINUSZEICHEN.equals(text.substring(0, 1))) ? false : true;
        } else {
            return false;
        }
    }

    /**
     * Gibt den Wert zu einem angegebenen Parameter zur�ck.
     *
     * @param paramName   Parametername, dessen Wert gesucht wird.
     * @return  Parameterwert oder Leerstring, wenn der Parameter nicht
     *          existiert.
     */
    public String getValue(String paramName) {
        if (this.settings.containsKey(paramName)) {
            return (String) this.settings.get(paramName);
        } else {
            return "";
        }
    }

    /**
     * Pr�ft, ob der angegebne Parameter in der Auflistung der Einstellungen
     * existiert.
     *
     * @param paramName  Parameterbezeichnung.
     * @return  true, wenn der Parameter existiert, sonst false.
     */
    public boolean paramExists(String paramName) {
        return this.settings.containsKey(paramName);
    }
}
