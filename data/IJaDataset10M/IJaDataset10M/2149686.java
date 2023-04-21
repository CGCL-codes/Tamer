package nl.adaptivity.parser.languages;

import nl.adaptivity.parser.tokens.CharToken;

public final class CharStreamEnum implements Language {

    public static final CharStreamEnum CHARTOKEN = new CharStreamEnum("chartoken", CharToken.class);

    private final Class aType;

    private final String aName;

    private CharStreamEnum(final String pName, final Class pType) {
        aType = pType;
        aName = pName;
    }

    public Class getType() {
        return aType;
    }

    public String name() {
        return aName;
    }

    public String toString() {
        return aName;
    }
}
