package de.ddb.conversion.util;

import java.nio.charset.Charset;
import de.ddb.charset.MabCharset;
import de.ddb.charset.MabDisketteCharset;
import de.ddb.charset.PicaCharset;

/**
 * @deprecated use de.ddb.charset.CharsetUtil instead
 */
public class CharsetUtil {

    public static final PicaCharset PICA_CHARSET = new PicaCharset();

    public static final MabCharset MAB_CHARSET = new MabCharset();

    public static final MabDisketteCharset MAB_DISKETTE_CHARSET = new MabDisketteCharset();

    public static final Charset forName(String name) {
        if (name.equals(PICA_CHARSET.name())) {
            return PICA_CHARSET;
        }
        if (name.equals(MAB_CHARSET.name())) {
            return MAB_CHARSET;
        }
        if (name.equals(MAB_DISKETTE_CHARSET.name())) {
            return MAB_DISKETTE_CHARSET;
        }
        return Charset.forName(name);
    }
}
