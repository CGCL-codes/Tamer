package org.makumba.commons.documentation;

import java.util.*;
import com.ecyrd.jspwiki.*;
import com.ecyrd.jspwiki.attachment.AttachmentManager;
import com.ecyrd.jspwiki.attachment.Attachment;
import com.ecyrd.jspwiki.plugin.PluginException;
import com.ecyrd.jspwiki.plugin.WikiPlugin;
import com.ecyrd.jspwiki.providers.ProviderException;

public class Img implements WikiPlugin {

    /** The parameter name for setting the src.  Value is <tt>{@value}</tt>. */
    public static final String PARAM_SRC = "src";

    /** The parameter name for setting the align.  Value is <tt>{@value}</tt>. */
    public static final String PARAM_ALIGN = "align";

    /** The parameter name for setting the height.  Value is <tt>{@value}</tt>. */
    public static final String PARAM_HEIGHT = "height";

    /** The parameter name for setting the width.  Value is <tt>{@value}</tt>. */
    public static final String PARAM_WIDTH = "width";

    /** The parameter name for setting the alt.  Value is <tt>{@value}</tt>. */
    public static final String PARAM_ALT = "alt";

    /** The parameter name for setting the caption.  Value is <tt>{@value}</tt>. */
    public static final String PARAM_CAPTION = "caption";

    /** The parameter name for setting the link.  Value is <tt>{@value}</tt>. */
    public static final String PARAM_LINK = "link";

    /** The parameter name for setting the target.  Value is <tt>{@value}</tt>. */
    public static final String PARAM_TARGET = "target";

    /** The parameter name for setting the style.  Value is <tt>{@value}</tt>. */
    public static final String PARAM_STYLE = "style";

    /** The parameter name for setting the class.  Value is <tt>{@value}</tt>. */
    public static final String PARAM_CLASS = "class";

    /** The parameter name for setting the border.  Value is <tt>{@value}</tt>. */
    public static final String PARAM_BORDER = "border";

    /**
     *  This method is used to clean away things like quotation marks which
     *  a malicious user could use to stop processing and insert javascript.
     */
    private static final String getCleanParameter(Map params, String paramId) {
        return TextUtil.replaceEntities((String) params.get(paramId));
    }

    /**
     *  {@inheritDoc}
     */
    public String execute(WikiContext context, Map params) throws PluginException {
        WikiEngine engine = context.getEngine();
        String src = getCleanParameter(params, PARAM_SRC);
        String align = getCleanParameter(params, PARAM_ALIGN);
        String ht = getCleanParameter(params, PARAM_HEIGHT);
        String wt = getCleanParameter(params, PARAM_WIDTH);
        String alt = getCleanParameter(params, PARAM_ALT);
        String caption = getCleanParameter(params, PARAM_CAPTION);
        String link = getCleanParameter(params, PARAM_LINK);
        String target = getCleanParameter(params, PARAM_TARGET);
        String style = getCleanParameter(params, PARAM_STYLE);
        String cssclass = getCleanParameter(params, PARAM_CLASS);
        String border = getCleanParameter(params, PARAM_BORDER);
        if (src == null) {
            throw new PluginException("Parameter 'src' is required for Image plugin");
        }
        if (cssclass == null) cssclass = "imageplugin";
        if (target != null && !validTargetValue(target)) {
            target = null;
        }
        try {
            AttachmentManager mgr = engine.getAttachmentManager();
            Attachment att = mgr.getAttachmentInfo(context, src);
            if (att != null) {
                src = context.getURL(WikiContext.ATTACH, att.getName());
            }
        } catch (ProviderException e) {
            throw new PluginException("Attachment info failed: " + e.getMessage());
        }
        StringBuffer result = new StringBuffer();
        result.append("<span class=\"" + cssclass + "\"");
        if (style != null) {
            result.append(" style=\"" + style);
            if (result.charAt(result.length() - 1) != ';') result.append(";");
            if (align != null && align.equals("center")) {
                result.append(" margin-left: auto; margin-right: auto;");
            }
            result.append("\"");
        } else {
            if (align != null && align.equals("center")) result.append(" style=\"margin-left: auto; margin-right: auto;\"");
        }
        if (align != null && !(align.equals("center"))) result.append(" align=\"" + align + "\"");
        result.append(">\n");
        if (caption != null) {
            result.append("<caption align=bottom>" + TextUtil.replaceEntities(caption) + "</caption>\n");
        }
        if (link != null) {
            result.append("<a href=\"" + link + "\"");
            if (target != null) {
                result.append(" target=\"" + target + "\"");
            }
            result.append(">");
        }
        result.append("<img src=\"" + src + "\"");
        if (ht != null) result.append(" height=\"" + ht + "\"");
        if (wt != null) result.append(" width=\"" + wt + "\"");
        if (alt != null) result.append(" alt=\"" + alt + "\"");
        if (border != null) result.append(" border=\"" + border + "\"");
        result.append(" />");
        if (link != null) result.append("</a>");
        result.append("</span>\n");
        return result.toString();
    }

    private boolean validTargetValue(String s) {
        if (s.equals("_blank") || s.equals("_self") || s.equals("_parent") || s.equals("_top")) {
            return true;
        } else if (s.length() > 0) {
            char c = s.charAt(0);
            return Character.isLowerCase(c) || Character.isUpperCase(c);
        }
        return false;
    }
}
