package bookster.backend.persist.beans;

/**
 *
 * @author gurito
 */
public class Language {

    /** A Language has a name **/
    private String name;

    /** A Language has an ISO code **/
    private String iso;

    /**
     * Get the name of this Language
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the 2-digit ISO code for this Language
     * @return
     */
    public String getIso() {
        return this.iso;
    }

    /**
     * Sets the name of this Language
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the 2-digit ISO code for this Language
     * If the given string isn't a 2-digit string, the iso code
     * won't be saved, and the method will return false.
     * Returns true on success
     * @param iso
     * @return
     */
    public boolean setIso(String iso) {
        if (validateIso(iso)) {
            this.iso = iso;
            return true;
        }
        return false;
    }

    /**
     * Validate an ISO Code. It only validates that the string
     * is two digits long.
     * @param iso
     * @return
     */
    private boolean validateIso(String iso) {
        if (iso.length() != 2) {
            return false;
        }
        return true;
    }
}
