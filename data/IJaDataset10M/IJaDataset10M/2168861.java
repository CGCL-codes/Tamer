package octoshare.common.request.messages;

import octoshare.common.Version;

/**
 * Parses a Version-Message:
 * 
 * versionID|codename|major release|minor release|patch level|Date|built number
 * 
 */
public class VersionMessage extends AbstractMessage {

    private String codename;

    private int majorRelease;

    private int minorRelease;

    private int patchLevel;

    private String date;

    private long builtNumber;

    public static final String MESSAGE_ID = "versionID";

    protected VersionMessage(String messageString) {
        super(messageString);
        if (countArguments() != 7) {
            setInvalid();
        } else {
            try {
                codename = getArgumentAsString(1);
                majorRelease = getArgumentAsInt(2);
                minorRelease = getArgumentAsInt(3);
                patchLevel = getArgumentAsInt(4);
                date = getArgumentAsString(5);
                builtNumber = getArgumentAsLong(6);
            } catch (IndexOutOfBoundsException e) {
                setInvalid();
            } catch (NumberFormatException e) {
                setInvalid();
            }
        }
    }

    public VersionMessage(Version version) {
        super();
        setMajorRelease(version.getMajorRelease());
        setMinorRelease(version.getMinorRelease());
        setPatchLevel(version.getPatchLevel());
        setDate(version.getDate());
        setBuiltNumber(version.getBuiltNumber());
        setCodename(version.getCodename());
    }

    public String toMessageString() {
        return MESSAGE_ID + SEPARATOR + codename + SEPARATOR + majorRelease + SEPARATOR + minorRelease + SEPARATOR + patchLevel + SEPARATOR + date + SEPARATOR + builtNumber;
    }

    @Override
    public String toString() {
        return toMessageString();
    }

    public long getBuiltNumber() {
        return builtNumber;
    }

    private void setBuiltNumber(long builtNumber) {
        this.builtNumber = builtNumber;
    }

    public String getCodename() {
        return codename;
    }

    private void setCodename(String codename) {
        this.codename = codename;
    }

    public String getDate() {
        return date;
    }

    private void setDate(String date) {
        this.date = date;
    }

    public int getMajorRelease() {
        return majorRelease;
    }

    private void setMajorRelease(int majorRelease) {
        this.majorRelease = majorRelease;
    }

    public int getMinorRelease() {
        return minorRelease;
    }

    private void setMinorRelease(int minorRelease) {
        this.minorRelease = minorRelease;
    }

    public int getPatchLevel() {
        return patchLevel;
    }

    private void setPatchLevel(int patchLevel) {
        this.patchLevel = patchLevel;
    }
}
