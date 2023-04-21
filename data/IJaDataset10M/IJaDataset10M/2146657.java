package net.sf.vfsjfilechooser.accessories.bookmarks;

/**
 * A specialisation of {@link TitledURLEntry} that can be used to store FTP connection specific
 * information.
 *
 * @author Alex Arana
 */
public class FTPURLEntry extends TitledURLEntry {

    /** Passive FTP option. */
    private boolean passiveFtp;

    /**
     * New FTPURLEntry with title, url and passiveFtp option.
     *
     * @param title
     * @param url
     * @param passiveFtp
     */
    public FTPURLEntry(String title, String url, boolean passiveFtp) {
        super(title, url);
        this.passiveFtp = passiveFtp;
    }

    /**
     * Copy constructor (from TitledURLEntry). Sets the passiveFtp mode flag to <code>false</code>
     * by default.
     *
     * @param entry to read most initial properties from
     */
    FTPURLEntry(TitledURLEntry entry) {
        this(entry.getTitle(), entry.getURL(), false);
    }

    public boolean isPassiveFtp() {
        return passiveFtp;
    }

    public void setPassiveFtp(boolean passiveFtp) {
        this.passiveFtp = passiveFtp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        FTPURLEntry fue = (FTPURLEntry) obj;
        return (passiveFtp == fue.passiveFtp) && ((getURL() == fue.getURL()) || ((fue != null) && getURL().equals(fue.getURL()))) && ((getTitle() == fue.getTitle()) || ((fue != null) && getTitle().equals(fue.getTitle())));
    }

    @Override
    public Object clone() {
        FTPURLEntry fue = (FTPURLEntry) super.clone();
        fue.passiveFtp = passiveFtp;
        return fue;
    }

    @Override
    public String toString() {
        return String.format("%s, passiveMode=%s", super.toString(), passiveFtp);
    }
}
