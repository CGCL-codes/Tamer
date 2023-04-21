package org.codehaus.groovy.grails.web.taglib;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException;

/**
 * Example:
 *
 * <code>
 *  <gr:findAll in="${thing}" expr="it.length() == 3">
 *        <p>${it}</p>
 *  </gr:findAll>
 * </code>
 * @author Graeme Rocher
 * @since 19-Jan-2006
 */
public class GroovyFindAllTag extends GroovySyntaxTag {

    public static final String TAG_NAME = "findAll";

    private static final String ATTRIBUTE_EXPR = "expr";

    public boolean isBufferWhiteSpace() {
        return false;
    }

    public boolean hasPrecedingContent() {
        return true;
    }

    public void doStartTag() {
        String in = (String) attributes.get(ATTRIBUTE_IN);
        String expr = (String) attributes.get(ATTRIBUTE_EXPR);
        if (StringUtils.isBlank(in)) throw new GrailsTagException("Tag [" + TAG_NAME + "] missing required attribute [" + ATTRIBUTE_IN + "]");
        if (StringUtils.isBlank(expr)) throw new GrailsTagException("Tag [" + TAG_NAME + "] missing required attribute [" + ATTRIBUTE_EXPR + "]");
        out.print(in);
        out.print(".findAll {");
        expr = calculateExpression(expr);
        out.print(expr);
        out.print("}");
        doEachMethod("");
    }

    public void doEndTag() {
        out.println("}");
    }

    public String getName() {
        return TAG_NAME;
    }
}
