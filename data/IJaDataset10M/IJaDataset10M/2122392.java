package org.radeox.example;

import org.radeox.api.engine.context.RenderContext;
import org.radeox.api.engine.RenderEngine;
import org.radeox.engine.context.BaseRenderContext;
import org.radeox.engine.BaseRenderEngine;

/**
 * An xslt extension function to render snip content using radeox.
 *
 * Example usage:
 * <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 * xmlns:radeox="http://snipsnap.org/org.radeox.example.XSLTExtension">
 * �
 * <xsl:template match="content">
 *   <content><xsl:value-of select="radeox:render(.)" disable-output-escaping="yes"/></content>
 * </xsl:template>
 * �
 * </xsl:stylesheet>
 * @author Micah Dubinko
 * @version $Id: XSLTExtension.java 11 2008-03-02 05:47:42Z iamkeke $
 */
public class XSLTExtension {

    public XSLTExtension() {
    }

    public static String render(String arg) {
        if (arg == null) {
            arg = "";
        }
        RenderContext context = new BaseRenderContext();
        RenderEngine engine = new BaseRenderEngine();
        return (engine.render(arg, context));
    }
}
