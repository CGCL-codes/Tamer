package mecca.sis.obj;

import java.util.Vector;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1
 */
public class Program {

    private String code;

    private String name;

    private String abbrev;

    private Vector tracks;

    /**
	 * @return Returns the abbrev.
	 */
    public String getAbbrev() {
        return abbrev;
    }

    /**
	 * @param abbrev The abbrev to set.
	 */
    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    /**
	 * @return Returns the code.
	 */
    public String getCode() {
        return code;
    }

    /**
	 * @param code The code to set.
	 */
    public void setCode(String code) {
        this.code = code;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the tracks.
	 */
    public Vector getTracks() {
        return tracks;
    }

    /**
	 * @param tracks The tracks to set.
	 */
    public void setTracks(Vector tracks) {
        this.tracks = tracks;
    }
}
