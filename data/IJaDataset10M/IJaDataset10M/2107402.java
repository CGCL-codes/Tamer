package espider;

/**
 * @author christophe
 *
 */
public class ConfigException extends Exception {

    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = 3618981178699363382L;

    public ConfigException() {
        super("Fichier de configuration non valide");
    }

    public ConfigException(String message) {
        super("Fichier de configuration non valide : \n" + message);
    }
}
