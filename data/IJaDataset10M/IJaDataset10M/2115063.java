package uk.org.toot.music.tonality;

import java.util.List;

/**
 * The Keys class represents all Keys and provides static methods to find
 * specific Keys which match certain criteria.
 * @author st
 *
 */
public class Keys {

    /**
     * Return a List of Keys with the specified notes
     */
    public static List<Key> withNotes(int[] keynotes) {
        List<Key> match = new java.util.ArrayList<Key>();
        for (Scale scale : Scales.getScales()) {
            for (int pc = 0; pc < 12; pc++) {
                Key key = new Key(pc, scale);
                if (key.contains(keynotes)) match.add(key);
            }
        }
        return match;
    }
}
