package Rules;

import java.util.TreeMap;

/**
 * Una singola stat.
 */
public class Stat {

    private static java.util.ResourceBundle L10n = java.util.ResourceBundle.getBundle("Text");

    /** Lista completa delle stat. */
    private static TreeMap stats = new TreeMap();

    /** Il nome (univoco) di questa stat. */
    private String name;

    /** Il nome della stat genitrice, <code>null</code> se nessuna. */
    private String parent = null;

    /**
   * Costruttore privato, usare {@link addStat(String)}.
   * @param name il nome della nuova stat
   */
    private Stat(String name) {
        this.name = name;
    }

    /**
   * Aggiunge una data stat con nome dato.
   * @param name il nome della nuova stat
   * @return un veloce riferimento alla nuova stat
   */
    public static Stat addStat(String name) {
        if (stats.containsKey(name)) throw new IllegalArgumentException(java.text.MessageFormat.format(L10n.getString("Rules.Stat.Exists"), new String[] { name }));
        Stat newStat = new Stat(name);
        stats.put(name, newStat);
        return newStat;
    }

    /**
   * Cerca una stat con nome dato.
   * @param name il nome della stat
   * @return la stat cercata
   */
    public static Stat getStat(String name) {
        return (Stat) stats.get(name);
    }

    /**
   * Ritorna il nome della stat.
   * @return il nome della stat cercata
   */
    public String getName() {
        return name;
    }

    /**
   * Ritorna il nome localizzato della stat.
   * @return il nome localizzato della stat cercata
   */
    public String getLocalizedName() {
        return L10n.getString("Rules.Stat.Name_" + name);
    }

    public String toString() {
        StringBuffer str = new StringBuffer("Stat[");
        str.append(name);
        str.append(']');
        return str.toString();
    }
}
