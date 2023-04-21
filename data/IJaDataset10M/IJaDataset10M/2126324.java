package enigma.parts;

import enigma.Debug;

/**
 * Das Steckbrett ist immer am Anfang der Maschine.<br />
 * Es leitet die Verschluesselung an die Rotoren weiter und beginnt somit die Verschluesselung. <br />
 * Es koennen die einzelnen Verbindungen nach dem Erstellen der Klasse neu gesetzt werden.
 * 
 * @author Gerald Schreiber, Philip Woelfel, Sebastian Chlan <br />
 * <br />
 * ENIGMA_TEC 2010 <br />
 * technik[at]enigma-ausstellung.at <br />
 * http://enigma-ausstellung.at <br />
 * <br />
 * HTL Rennweg <br />
 * Rennweg 89b <br />
 * A-1030 Wien <br />
 * 
 */
public class Plugboard extends Mapper {

    /**
	 * Default-Konstruktor, welcher ein Steckbrett erzeugt, bei welchem jedes
	 * Zeichen auf sich selbst abgebildet wird.
	 * 
	 */
    public Plugboard() {
        super("Plugboard");
    }

    /**
	 * Konstruktor, welcher ein Steckbrett mit der angegebenen Verdrahtung
	 * erstellt.
	 * 
	 * @param setting
	 *            String, der die Verdrahtung angibt
	 * @throws Exception
	 *             falls der uebergebene String fehlerhaft ist
	 */
    public Plugboard(String name, String setting) {
        super(name, setting);
    }

    /**
	 * Konstruktor, welcher ein Steckbrett mit der angegebenen Verdrahtung
	 * erstellt.
	 * 
	 * @param setting
	 *            char[], welches die Verdrahtung angibt
	 * @throws Exception
	 *             falls das uebergebene char[] fehlerhaft ist
	 */
    public Plugboard(String name, char[] setting) {
        super(name, setting);
    }

    /**
	 * Methode hat die Verbindung zwischen einem Buchstaben und einem anderen
	 * Buchstaben neu gesetzt. Darf jedoch jetzt nicht mehr verwendet werden.
	 * 
	 * @param source
	 *            Quellbuchstabe
	 * @param destination
	 *            Zielbuchstabe
	 * @throws Exception
	 *             da die Methode nicht mehr verwendet werden darf
	 */
    @Deprecated
    public void setConnection(char source, char destination) throws Exception {
        throw new Exception("Method is deprecated, don't use it");
    }

    /**
	 * Setzt alle Verbindungen neu.
	 * 
	 * @param setting
	 *            char[], mit den Zielbuchstaben
	 * @throws Exception
	 *             falls das uebergebene char[] fehlerhaft ist
	 */
    public void setConnections(char[] setting) throws Exception {
        String eingabe = new String(setting);
        stringToSettingArray(eingabe);
    }

    /**
	 * Setzt alle Verbindungen neu.
	 * 
	 * @param setting
	 *            String, mit den Zielbuchstaben
	 * @throws Exception
	 *             falls der uebergebene String fehlerhaft ist
	 */
    public void setConnections(String setting) throws Exception {
        stringToSettingArray(setting);
    }

    /**
	 * Dreht zuerst den darauffolgenden Rotor, wenn vorhanden, und gibt dann das verschluesselte Zeichen an diesen weiter.<br />
	 * Wenn nicht wird nur das verschluesselte Zeichen zurueckgegeben.
	 * 
	 * @param c
	 *		der zu verschluesselnde Buchstabe
	 * @return der verschluesselte Buchstabe
	 * @throws IllegalArgumentException
	 *             falls das uebergeben Zeichen nicht zwischen A-Z ist
	 */
    @Override
    public char encrypt(char c) {
        if (!(c >= 'A' && c <= 'Z')) {
            throw new IllegalArgumentException("Invalid character! Must be A-Z.");
        }
        int pos = c - 'A';
        if (Debug.isDebug()) {
            System.out.println(getName() + ":\nc: '" + c + "'\t out: '" + setting[pos] + "'");
        }
        if (hasNextMapper() && nextMapper instanceof Rotor) {
            Rotor temp = (Rotor) nextMapper;
            temp.rotate();
            return temp.encrypt(setting[pos]);
        }
        return setting[pos];
    }
}
