package com.hadeslee.audiotag.tag.lyrics3;

import com.hadeslee.audiotag.tag.datatype.AbstractStringStringValuePair;

public class Lyrics3v2Fields extends AbstractStringStringValuePair {

    private static Lyrics3v2Fields lyrics3Fields;

    /**
     * CRLF int set
     */
    private static final byte[] crlfByte = { 13, 10 };

    /**
     * CRLF int set
     */
    public static final String CRLF = new String(crlfByte);

    public static Lyrics3v2Fields getInstanceOf() {
        if (lyrics3Fields == null) {
            lyrics3Fields = new Lyrics3v2Fields();
        }
        return lyrics3Fields;
    }

    public static final String FIELD_V2_INDICATIONS = "IND";

    public static final String FIELD_V2_LYRICS_MULTI_LINE_TEXT = "LYR";

    public static final String FIELD_V2_ADDITIONAL_MULTI_LINE_TEXT = "INF";

    public static final String FIELD_V2_AUTHOR = "AUT";

    public static final String FIELD_V2_ALBUM = "EAL";

    public static final String FIELD_V2_ARTIST = "EAR";

    public static final String FIELD_V2_TRACK = "ETT";

    public static final String FIELD_V2_IMAGE = "IMG";

    private Lyrics3v2Fields() {
        idToValue.put(FIELD_V2_INDICATIONS, "Indications field");
        idToValue.put(FIELD_V2_LYRICS_MULTI_LINE_TEXT, "Lyrics multi line text");
        idToValue.put(FIELD_V2_ADDITIONAL_MULTI_LINE_TEXT, "Additional information multi line text");
        idToValue.put(FIELD_V2_AUTHOR, "Lyrics/Music Author name");
        idToValue.put(FIELD_V2_ALBUM, "Extended Album name");
        idToValue.put(FIELD_V2_ARTIST, "Extended Artist name");
        idToValue.put(FIELD_V2_TRACK, "Extended Track Title");
        idToValue.put(FIELD_V2_IMAGE, "Link to an image files");
        createMaps();
    }

    /**
     * Returns true if the identifier is a valid Lyrics3v2 frame identifier
     *
     * @param identifier string to test
     * @return true if the identifier is a valid Lyrics3v2 frame identifier
     */
    public static boolean isLyrics3v2FieldIdentifier(String identifier) {
        if (identifier.length() < 3) {
            return false;
        }
        return getInstanceOf().getIdToValueMap().containsKey(identifier.substring(0, 3));
    }
}
