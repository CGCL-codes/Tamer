package org.doit.muffin.filter;

import org.doit.muffin.*;
import org.doit.muffin.regexp.Pattern;
import org.doit.muffin.regexp.Factory;

public class Decaf extends AbstractFilterFactory {

    public static final String NOJAVA = "noJava";

    public static final String NOJAVASCRIPT = "noJavaScript";

    private static Pattern javaScriptTags = Factory.instance().getPattern("^(a|input|body|form|area|select|frameset|label|textarea|button|applet|base|basefont|bdo|br|font|frame|head|html|iframe|isindex|meta|param|script|style|title)$");

    private static Pattern javaScriptAttrs = Factory.instance().getPattern("^(onload|onunload|onclick|ondblclick|onmousedown|onmouseup|onmouseover|onmousemove|onmouseout|onfocus|onblur|onkeypress|onkeydown|onkeyup|onsubmit|onreset|onselect|onchange)$");

    /**
     * @see org.doit.muffin.filter.AbstractFilterFactory#doSetDefaultPrefs()     */
    protected void doSetDefaultPrefs() {
        putPrefsBoolean(NOJAVASCRIPT, true);
        putPrefsBoolean(NOJAVA, false);
    }

    /**
     * @see org.doit.muffin.filter.AbstractFilterFactory#doMakeFrame()
     */
    protected AbstractFrame doMakeFrame() {
        return new DecafFrame(this);
    }

    /**
     * @see org.doit.muffin.filter.AbstractFilterFactory#doMakeFilter()
     */
    protected Filter doMakeFilter() {
        return new DecafFilter(this);
    }

    /**
     * @see org.doit.muffin.filter.AbstractFilterFactory#getName()
     */
    public String getName() {
        return "Decaf";
    }

    static boolean isJavaScriptTag(String pattern) {
        return javaScriptTags.matches(pattern);
    }

    static boolean isJavaScriptAttr(String pattern) {
        return javaScriptAttrs.matches(pattern);
    }
}
