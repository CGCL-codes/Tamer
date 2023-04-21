package ar.edu.unq.yaqc4j.generators.java.lang;

import ar.edu.unq.yaqc4j.generators.Arbitrary;
import ar.edu.unq.yaqc4j.generators.Gen;
import ar.edu.unq.yaqc4j.randoms.Distribution;

/**
 * Default character random generator.
 * 
 * @author Pablo
 */
public class CharacterGen implements Gen<Character> {

    /**
     * the MIN_CHAR.
     */
    private static final int MIN_CHAR = 32;

    /**
     * the MAX_CHAR.
     */
    private static final int MAX_CHAR = 355;

    /**
     * @{inheritDoc
     */
    public Character arbitrary(final Distribution random, final long minsize, final long maxsize) {
        return Character.valueOf((char) Arbitrary.choose(random, Math.max(MIN_CHAR, minsize), Math.min(maxsize, MAX_CHAR)));
    }
}
