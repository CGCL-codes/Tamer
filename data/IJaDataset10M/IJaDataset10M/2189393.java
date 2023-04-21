package de.uni_leipzig.lots.jsp.tag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alexander Kiel
 * @version $Id: AttributeTagSupport.java,v 1.6 2007/10/23 06:29:24 mai99bxd Exp $
 */
public final class AttributeTagSupport {

    private static final Pattern validDimensionPattern = Pattern.compile("\\d+\\s*((pt)|(pc)|(in)|(mm)|(cm)|(em)|(ex)|(px)|(%))");

    public static final boolean isValidWidth(String width) {
        if (width == null) return false;
        width = width.trim();
        if ("0".equals(width)) return true;
        Matcher matcher = validDimensionPattern.matcher(width);
        return matcher.matches();
    }
}
