package net.sf.btw.cervii;

import java.util.Hashtable;

/**
 * Provides internalization support.
 * 
 * @author Martin Vysny
 */
public final class I18n {

    /**
	 * Not instantiable.
	 */
    private I18n() {
        super();
    }

    private static final Hashtable en = new Hashtable();

    static {
        en.put("ERROR", "Error");
        en.put("BT_ERROR", "Failed to initialize bluetooth. Please check your bluetooth settings.");
        en.put("START_SHORT", "Start");
        en.put("START_LONG", "Start the game");
        en.put("HELP_TEXT", "Each player controls a single worm. Player starts on a random place and moves forward. You can change the direction using left and right arrow keys or a joystick. Player loses when he crashes to a worm. Player wins if his worm is the last worm alive.");
        en.put("START_SERVER", "Start a server");
        en.put("CONNECT_TO", "Connect to");
        en.put("COMM_ERR", "Communication error");
        en.put("ERR_CREATING_SERVER", "Error creating server");
        en.put("DISCONNECTED", "Disconnected from the server");
    }

    private static final Hashtable sk = new Hashtable();

    static {
        sk.put("ERROR", "Chyba");
        sk.put("BT_ERROR", "Chyba pri inicializácii subsystému Bluetooth. Prosím uistite sa, že je Bluetooth zapnutý.");
        sk.put("START_SHORT", "Štart");
        sk.put("START_LONG", "Štart hry");
        sk.put("HELP_TEXT", "Každý hráč ovláda jedného červa. Hráč začína na náhodnom mieste a pomaly sa posúva vpred. Smer je možné ovládať šípkami vpravo a vľavo, resp. joystickom. Hráč prehrá, ak narazí do červa. Hráč vyhrá, keď jeho červ ostane posledný živý.");
        sk.put("START_SERVER", "Spustiť server");
        sk.put("CONNECT_TO", "Pripojiť sa k");
        sk.put("COMM_ERR", "Chyba v komunikácii");
        sk.put("ERR_CREATING_SERVER", "Chyba pri vytváraní servera");
        sk.put("DISCONNECTED", "Odpojený od servera");
    }

    private static final Hashtable langs = new Hashtable();

    static {
        langs.put("en", en);
        langs.put("sk", sk);
    }

    /**
	 * Current locale. Defaults to <code>en</code> if the locale cannot be
	 * found in {@link #langs}.
	 */
    private static Hashtable lang;

    static {
        lang = (Hashtable) langs.get(System.getProperty("microedition.locale").substring(0, 2));
        if (lang == null) lang = en;
    }

    /**
	 * Returns message bound to given key.
	 * 
	 * @param key
	 *            the key to retrieve.
	 * @return message. If a message is not defined for such key then
	 *         <code>!key!</code> is returned.
	 */
    public static String getMessage(final String key) {
        final String result = (String) lang.get(key);
        if (result == null) return "!" + key + "!";
        return result;
    }
}
