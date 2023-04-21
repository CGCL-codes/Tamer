package mp3.reproductor.restrictions;

import java.util.ArrayList;
import java.util.List;

/**
 * This simple parser selects the adecuate PlayerRestriction class based on the String passed.
 * Keep in mind that the real format for the restrictions are maintained here.
 * <p>Adding a new kind of PlayerRestriction isn't enough to be recognized. Each
 * PlayerRestriction should be added inside "mp3.reproductor.restrictions" package, and it
 * is also necessary to hardcode the restriction here (in method "parseOne") in order to parse the string
 * representing the restriction correctly</p>
 * @author user
 */
public class RestrictionParser {

    /**
     * General expected format is "key#value" where the key helps to select the proper
     * PlayerRestriction and the value is used to initialize it
     * <p>Based on all supported restrictions "generatedString"</p>
     * <p>All supported restrictions are hardcoded here, based on the PlayerRestriction
     * classes of the "mp3.reproductor.restrictions" package</p>
     * @param s string representing a restriction
     * @return a player restriction, or null if no such restriction
     */
    public PlayerRestriction parseOne(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        String[] split;
        if (s.startsWith("os")) {
            split = s.split("#");
            return new OSBasedRestriction(split[1]);
        } else if (s.startsWith("lang")) {
            split = s.split("#");
            return new ConfigLangBasedRestriction(split[1]);
        } else if (s.equals("E")) {
            return new ExistenceRestriction();
        } else if (s.startsWith("slang")) {
            split = s.split("#");
            return new SystemLangBasedRestriction(split[1]);
        } else if (s.startsWith("plist")) {
            split = s.split("#");
            return new PlaylistRestriction(split[1]);
        } else {
            return null;
        }
    }

    /**
     * Parse all the restrictions. Format should be "R1, R2, R3" where each Ri is
     * a known restriction (usually each restriction is like "key#value")
     * <p>Always return an array, even if it's empty</p>
     * @param ss a comma-separated string of representations of restrictions
     * @return a list of PlayerRestriction
     */
    public List<PlayerRestriction> parseAll(String ss) {
        List<PlayerRestriction> result = new ArrayList<PlayerRestriction>(3);
        if (ss == null || ss.trim().isEmpty()) return result;
        String[] split = ss.split(",");
        for (String splitToken : split) {
            PlayerRestriction rest = parseOne(splitToken);
            if (rest != null) result.add(rest);
        }
        return result;
    }

    /**
     * <p>Generates a string representing all restrictions passed as parameters. The string
     * is generated using the "generateStringRestriction" method of each restriction</p>
     * @param lista
     * @return a string representation of the restrictions
     */
    public String restrictionToString(List<PlayerRestriction> lista) {
        if (lista.isEmpty()) return "";
        StringBuilder sb = new StringBuilder(10);
        for (PlayerRestriction restric : lista) {
            sb.append(restric.generateStringRestriction()).append(',');
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }
}
