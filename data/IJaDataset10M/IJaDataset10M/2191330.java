package osmparser;

import java.text.ParseException;

/**
 * This class represents a member of a relation.
 * @author Stefan Zeller
 *
 */
public class Member {

    public static final byte NODE = 1;

    public static final byte WAY = 2;

    public static final byte RELATION = 3;

    Byte type = 0;

    Long id = 0l;

    /**
	 * @param type
	 * @param id
	 */
    public Member(Byte type, Long id) {
        this.type = type;
        this.id = id;
    }

    /**
	 * @param str
	 * @return
	 */
    public static Byte parseMember(String str) {
        if (str.equalsIgnoreCase("node")) {
            return NODE;
        } else if (str.equalsIgnoreCase("way")) {
            return WAY;
        } else if (str.equalsIgnoreCase("relation")) {
            return RELATION;
        } else {
            throw new IllegalArgumentException("Unknown type " + str);
        }
    }
}
