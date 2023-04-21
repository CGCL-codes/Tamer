package org.doit.muffin.filter;

import org.doit.muffin.Filter;
import org.doit.muffin.regexp.Factory;
import org.doit.muffin.regexp.Pattern;

public class NoCode extends AbstractFilterFactory {

    static final String NOJAVA = "noJava";

    static final String NOJAVASCRIPT = "noJavaScript";

    static final String NOVBSCRIPT = "noVBScript";

    static final String NOOTHERSCRIPT = "noOtherScript";

    static final String NOENCODEDSCRIPT = "noEncodedScript";

    static final String NOEVALINSCRIPT = "noEvalInScript";

    private static Pattern javaScriptTags = Factory.instance().getPattern("^(a|input|body|form|area|select|frameset|label|textarea|button|applet|base|basefont|bdo|br|font|frame|head|html|iframe|isindex|meta|param|script|style|title)$");

    private static Pattern javaScriptAttrs = Factory.instance().getPattern("^(onload|onunload|onclick|ondblclick|onmousedown|onmouseup|onmouseover|onmousemove|onmouseout|onfocus|onblur|onkeypress|onkeydown|onkeyup|onsubmit|onreset|onselect|onchange)$");

    ;

    /**
     * @see org.doit.muffin.filter.AbstractFilterFactory#doSetDefaultPrefs()     */
    protected void doSetDefaultPrefs() {
        putPrefsBoolean(NOJAVASCRIPT, true);
        putPrefsBoolean(NOVBSCRIPT, true);
        putPrefsBoolean(NOOTHERSCRIPT, true);
        putPrefsBoolean(NOENCODEDSCRIPT, true);
        putPrefsBoolean(NOEVALINSCRIPT, true);
        putPrefsBoolean(NOJAVA, false);
    }

    protected AbstractFrame doMakeFrame() {
        return new NoCodeFrame(this);
    }

    /**
     * @see org.doit.muffin.filter.AbstractFilterFactory#doMakeFilter()     */
    protected Filter doMakeFilter() {
        return new NoCodeFilter(this);
    }

    /**
     * @see org.doit.muffin.filter.AbstractFilterFactory#getName()
     */
    public String getName() {
        return "NoCode";
    }

    static boolean isJavaScriptTag(String pattern) {
        return javaScriptTags.matches(pattern);
    }

    static boolean isJavaScriptAttr(String pattern) {
        return javaScriptAttrs.matches(pattern);
    }
}
