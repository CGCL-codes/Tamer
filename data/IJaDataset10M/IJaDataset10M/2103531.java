package cookxml.cookswing.converter;

import java.awt.*;
import java.util.StringTokenizer;
import cookxml.core.DecodeEngine;
import cookxml.core.interfaces.Converter;

/**
 * Convert a string in "#,#,#,#" format into a Rectangle.
 *
 * @cxdoc
 * The format is "<code>#,#,#,#</code>" in the order of x, y, width, and height.
 *
 * @author Heng Yuan
 * @version $Id: RectangleConverter.java 233 2007-06-06 08:08:49Z coconut $
 * @since CookSwing 1.0
 */
public class RectangleConverter implements Converter {

    public Object convert(String value, DecodeEngine decodeEngine) {
        StringTokenizer tokenizer = new StringTokenizer(value, ", ");
        String str1 = tokenizer.nextToken();
        String str2 = tokenizer.nextToken();
        if (!tokenizer.hasMoreTokens()) return new Rectangle(Integer.parseInt(str1), Integer.parseInt(str2));
        String str3 = tokenizer.nextToken();
        String str4 = tokenizer.nextToken();
        return new Rectangle(Integer.parseInt(str1), Integer.parseInt(str2), Integer.parseInt(str3), Integer.parseInt(str4));
    }
}
