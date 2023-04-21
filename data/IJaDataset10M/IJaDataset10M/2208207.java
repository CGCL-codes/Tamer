package net.community.chest.javaagent.dumper.data;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import org.w3c.dom.Element;
import net.community.chest.reflect.Visibility;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Aug 11, 2011 9:56:30 AM
 */
public final class InfoUtils {

    public static final String LINE_SEP = System.getProperty("line.separator");

    private InfoUtils() {
    }

    public static final String CLASS_ELEMENT = "class", NAME_ATTR = "name", LOCATION_ATTR = "location";

    public static <A extends Appendable> A appendClassHeader(final A sb, final CharSequence name, final int mod, final URL url) throws IOException {
        return appendClassHeader(sb, name, mod, (url == null) ? null : url.toExternalForm());
    }

    public static <A extends Appendable> A appendClassHeader(final A sb, final CharSequence name, final int mod, final CharSequence location) throws IOException {
        sb.append('<').append(CLASS_ELEMENT);
        appendAttribute(sb, NAME_ATTR, name);
        if ((location != null) && (location.length() > 0)) appendAttribute(sb, LOCATION_ATTR, location);
        appendMethodModifiers(sb, mod);
        sb.append(" >");
        return sb;
    }

    public static <A extends Appendable> A appendClassFooter(final A sb) throws IOException {
        sb.append("</").append(CLASS_ELEMENT).append('>');
        return sb;
    }

    public static final String METHOD_ELEMENT = "method";

    public static final <A extends Appendable> A startMethod(final A sb, final String name) throws IOException {
        sb.append('<').append(METHOD_ELEMENT);
        return appendAttribute(sb, NAME_ATTR, encodeMethodName(name));
    }

    public static final String VISIBILITY_ATTR = "visibility", ABSTRACT_ATTTR = "abstract", STATIC_ATTR = "static", FINAL_ATTR = "final";

    public static final <A extends Appendable> A appendMethodModifiers(final A sb, final int mod) throws IOException {
        appendAttribute(sb, VISIBILITY_ATTR, Visibility.fromModifier(mod).toString());
        appendAttribute(sb, ABSTRACT_ATTTR, String.valueOf(Modifier.isAbstract(mod)));
        appendAttribute(sb, STATIC_ATTR, String.valueOf(Modifier.isStatic(mod)));
        appendAttribute(sb, FINAL_ATTR, String.valueOf(Modifier.isFinal(mod)));
        return sb;
    }

    public static final String encodeMethodName(final String name) {
        final int nLen = (name == null) ? 0 : name.length();
        if (nLen <= 0) return name;
        final char chFirst = name.charAt(0), chLast = name.charAt(nLen - 1);
        if ((chFirst != '<') && (chLast != '>')) return name;
        if (((chFirst == '<') && (chLast != '>')) || ((chFirst != '<') && (chLast == '>'))) throw new IllegalArgumentException("Mismatched c'tor name brackets: " + name);
        return "&lt;" + name.substring(1, nLen - 1) + "&gt;";
    }

    public static final String decodeMethodName(final String name) {
        final int nLen = (name == null) ? 0 : name.length();
        if (nLen <= 0) return name;
        final boolean startsWith = name.startsWith("&lt;"), endsWith = name.endsWith("&gt;");
        if (startsWith != endsWith) throw new IllegalArgumentException("Mismatched c'tor name brackets: " + name);
        if (startsWith) return "<" + name.substring(4, nLen - 4) + ">"; else return name;
    }

    public static final Element appendMethodModifiers(final Element elem, final int mod) {
        elem.setAttribute(VISIBILITY_ATTR, Visibility.fromModifier(mod).toString());
        elem.setAttribute(ABSTRACT_ATTTR, String.valueOf(Modifier.isAbstract(mod)));
        elem.setAttribute(STATIC_ATTR, String.valueOf(Modifier.isStatic(mod)));
        elem.setAttribute(FINAL_ATTR, String.valueOf(Modifier.isFinal(mod)));
        return elem;
    }

    public static final int getModifiers(final Element elem) {
        int mod = 0;
        {
            final String visAttr = elem.getAttribute(VISIBILITY_ATTR);
            final Visibility vis = Visibility.fromString(visAttr);
            if ((vis == null) && (visAttr != null) && (visAttr.length() > 0)) throw new IllegalArgumentException("Unkown visibility value: " + visAttr);
            mod |= vis.getModifier();
        }
        mod |= updateModifier(elem, ABSTRACT_ATTTR, Modifier.ABSTRACT);
        mod |= updateModifier(elem, STATIC_ATTR, Modifier.STATIC);
        mod |= updateModifier(elem, FINAL_ATTR, Modifier.FINAL);
        return mod;
    }

    private static final int updateModifier(final Element elem, final String attrName, final int mod) {
        final String attrVal = elem.getAttribute(attrName);
        if ((attrVal == null) || (attrVal.length() <= 0)) return 0;
        if (Boolean.parseBoolean(attrVal)) return mod; else return 0;
    }

    public static final String PARAM_ELEMENT = "param", TYPE_ATTR = "type";

    public static final <A extends Appendable> A appendParamTypeAttribute(final A sb, final CharSequence type) throws IOException {
        sb.append('<').append(PARAM_ELEMENT);
        appendAttribute(sb, TYPE_ATTR, type);
        sb.append(" />");
        return sb;
    }

    public static final <A extends Appendable> A endMethod(final A sb, final boolean hasParams) throws IOException {
        if (hasParams) sb.append("</").append(METHOD_ELEMENT).append('>'); else sb.append(" />");
        return sb;
    }

    public static final <A extends Appendable> A appendAttribute(final A sb, final CharSequence name, final CharSequence value) throws IOException {
        sb.append(' ').append(name).append("=\"").append(value).append('"');
        return sb;
    }

    public static final String RETURN_TYPE_ATTR = "returnType";

    public static final <A extends Appendable> A appendReturnTypeAttribute(final A sb, final CharSequence type) throws IOException {
        return appendAttribute(sb, RETURN_TYPE_ATTR, type);
    }

    public static final <A extends Appendable> A println(final A sb) throws IOException {
        sb.append(LINE_SEP);
        return sb;
    }
}
