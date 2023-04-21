package juploader.plugin;

import javax.swing.Icon;

/**
 * Klasa zawierająca informacje o pluginie.
 *
 * @author Adam Pawelec
 */
public class PluginInfo {

    /** Nazwa serwera. */
    private final String name;

    /** Autor wtyczki. */
    private final String author;

    /** Email autora wtyczki. */
    private final String authorEmail;

    /** Adres serwera. */
    private final String www;

    /** Numer wersji wtyczki. */
    private final int version;

    /** Ikonka reprezentująca wtyczkę. */
    private final Icon icon;

    public PluginInfo(String name, String author, String authorEmail, String www, int version, Icon icon) {
        this.name = name;
        this.author = author;
        this.authorEmail = authorEmail;
        this.www = www;
        this.version = version;
        this.icon = icon;
    }

    public String getAuthor() {
        return author;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public int getVersion() {
        return version;
    }

    public Icon getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getWww() {
        return www;
    }
}
