package lt.baltic_amadeus.jqbridge;

/**
 * 
 * @author Baltic Amadeus, JSC
 * @author Antanas Kompanas
 *
 */
public class Version {

    private String versionString;

    public Version(String versionString) {
        if (versionString == null || versionString.length() == 0) throw new IllegalArgumentException("versionString cannot be null or empty");
        this.versionString = versionString;
    }

    public String getVersionString() {
        return versionString;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false; else if (obj instanceof Version) {
            Version other = (Version) obj;
            return versionString.equals(other.getVersionString());
        } else {
            return versionString.equals(obj);
        }
    }

    public int hashCode() {
        return versionString.hashCode();
    }

    public String toString() {
        return versionString;
    }
}
