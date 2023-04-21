package net.sf.gham.core.entity.match;

import net.sf.jtwa.Messages;

/**
 *
 * @author  Fabio Collini
 */
public class MatchValutation {

    public static String[] levels = new String[] { Messages.getString("very_low"), Messages.getString("low"), Messages.getString("high"), Messages.getString("very_high") };

    private static String[] den = new String[] { Messages.getString("non-existent"), Messages.getString("disastrous"), Messages.getString("wretched"), Messages.getString("poor"), Messages.getString("weak"), Messages.getString("inadequate"), Messages.getString("passable"), Messages.getString("solid"), Messages.getString("excellent"), Messages.getString("formidable"), Messages.getString("outstanding"), Messages.getString("brilliant"), Messages.getString("magnificent"), Messages.getString("world_class"), Messages.getString("supernatural"), Messages.getString("titanic"), Messages.getString("extra-terrestrial"), Messages.getString("mythical"), Messages.getString("magical"), Messages.getString("utopian"), Messages.getString("divine") };

    /** Creates a new instance of MatchRatings */
    private MatchValutation() {
    }

    public static String toString(int i) {
        i--;
        return den[i / 4 + 1] + " " + levels[i % 4];
    }

    public static String toStringShort(int i) {
        i--;
        switch(i % 4) {
            case 0:
                return Integer.toString(i / 4 + 1) + "--";
            case 1:
                return Integer.toString(i / 4 + 1) + "-";
            case 2:
                return Integer.toString(i / 4 + 1) + "+";
            default:
                return Integer.toString(i / 4 + 1) + "++";
        }
    }
}
