package xutools.universe.creators.helpers;

import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * A set of sector characters.
 * 
 * @author Tobias Weigel
 * 
 */
public class SectorCharacterSet extends HashSet<SectorCharacter> {

    /**
	 * Creates a new set form given string.
	 * 
	 * @param string
	 *            comma-separated character names.
	 */
    public SectorCharacterSet(String string) {
        StringTokenizer tok = new StringTokenizer(string, ",");
        while (tok.hasMoreTokens()) {
            String t = tok.nextToken().trim();
            SectorCharacter sc = SectorCharacter.fromString(t);
            if (sc == null) throw new IllegalArgumentException("Invalid or unknown sector character name: '" + t + "'!");
            add(sc);
        }
    }

    /**
	 * Creates a new set with one entry.
	 * 
	 * @param character
	 */
    public SectorCharacterSet(SectorCharacter character) {
        this.add(character);
    }

    /**
	 * Copy constructor.
	 * 
	 * @param characters
	 */
    public SectorCharacterSet(SectorCharacterSet characters) {
        super(characters);
    }
}
