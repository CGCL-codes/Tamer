package org.wings.template;

import java.util.StringTokenizer;
import org.wings.SFont;

/**
 * TemplateUtil.java
 *
 *
 * Created: Tue Aug  6 16:41:22 2002
 *
 * @author (c) mercatis information systems gmbh, 1999-2002
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision: 1759 $
 */
public class TemplateUtil {

    /**
     * 
     */
    public TemplateUtil() {
    }

    public static final SFont parseFont(String value) {
        StringTokenizer s = new StringTokenizer(value, ",");
        String fontName = s.nextToken();
        String tmpFontType = s.nextToken().toUpperCase().trim();
        int fontType = SFont.PLAIN;
        if (tmpFontType.startsWith("B")) fontType = SFont.BOLD; else if (tmpFontType.startsWith("I")) fontType = SFont.ITALIC;
        int fontSize = 12;
        try {
            fontSize = Integer.parseInt(s.nextToken());
        } catch (Exception e) {
        }
        return new SFont(fontName, fontType, fontSize);
    }
}
