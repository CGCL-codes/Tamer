package edu.xtec.jclic.activities.text;

import edu.xtec.util.JDomUtility;
import java.text.Collator;
import edu.xtec.util.Messages;
import edu.xtec.jclic.project.JClicProject;

/**
 *
 * @author Francesc Busquets (fbusquets@xtec.net)
 * @version 1.0
 */
public class BasicEvaluator extends Evaluator {

    public static final String CHECK_CASE = "checkCase";

    public static final String CHECK_ACCENTS = "checkAccents";

    public static final String CHECK_PUNCTUATION = "checkPunctuation";

    public static final String CHECK_DOUBLE_SPACES = "checkDoubleSpaces";

    public static final String PUNCTUATION = ".,;:";

    protected boolean checkCase;

    protected boolean checkAccents;

    protected boolean checkPunctuation;

    protected boolean checkDoubleSpaces;

    int strength;

    /** Creates new BasicEvaluator */
    public BasicEvaluator(JClicProject project) {
        super(project);
        checkCase = false;
        checkAccents = true;
        checkPunctuation = true;
        checkDoubleSpaces = false;
        init();
    }

    public org.jdom.Element getJDomElement() {
        org.jdom.Element e = super.getJDomElement();
        if (checkCase) e.setAttribute(CHECK_CASE, JDomUtility.boolString(checkCase));
        if (!checkAccents) e.setAttribute(CHECK_ACCENTS, JDomUtility.boolString(checkAccents));
        if (!checkPunctuation) e.setAttribute(CHECK_PUNCTUATION, JDomUtility.boolString(checkPunctuation));
        if (checkDoubleSpaces) e.setAttribute(CHECK_DOUBLE_SPACES, JDomUtility.boolString(checkDoubleSpaces));
        return e;
    }

    public void setProperties(org.jdom.Element e, Object aux) throws Exception {
        super.setProperties(e, aux);
        checkCase = JDomUtility.getBoolAttr(e, CHECK_CASE, false);
        checkAccents = JDomUtility.getBoolAttr(e, CHECK_ACCENTS, true);
        checkPunctuation = JDomUtility.getBoolAttr(e, CHECK_PUNCTUATION, true);
        checkDoubleSpaces = JDomUtility.getBoolAttr(e, CHECK_DOUBLE_SPACES, false);
    }

    public void setProperties(edu.xtec.jclic.clic3.Clic3Activity c3a) {
        checkCase = c3a.avMaj;
        checkAccents = c3a.avAcc;
        checkPunctuation = c3a.avPunt;
        checkDoubleSpaces = c3a.avDblSpc;
    }

    protected void init() {
        super.init();
        strength = checkAccents ? checkCase ? Collator.TERTIARY : Collator.SECONDARY : Collator.PRIMARY;
        collator.setStrength(strength);
    }

    public boolean checkText(String text, String match) {
        return collator.equals(getClearedText(text), getClearedText(match));
    }

    public byte[] evalText(String text, String[] match) {
        byte[] flags = new byte[text.length()];
        boolean result = checkText(text, match);
        for (int i = 0; i < flags.length; i++) {
            flags[i] = result ? FLAG_OK : FLAG_DEFAULT_ERROR;
        }
        return flags;
    }

    protected String getClearedText(String src) {
        return getClearedText(src, null);
    }

    protected String getClearedText(String src, boolean[] skipped) {
        if (skipped == null) skipped = new boolean[src.length()];
        for (int i = 0; i < src.length(); i++) skipped[i] = false;
        if (checkPunctuation && checkDoubleSpaces) return src;
        StringBuffer sb = new StringBuffer();
        boolean wasSpace = false;
        for (int i = 0; i < src.length(); i++) {
            char ch = src.charAt(i);
            if (PUNCTUATION.indexOf(ch) >= 0 && !checkPunctuation) {
                if (!wasSpace) sb.append(' '); else skipped[i] = true;
                wasSpace = true;
            } else if (ch == ' ') {
                if (checkDoubleSpaces || !wasSpace) sb.append(ch); else skipped[i] = true;
                wasSpace = true;
            } else {
                wasSpace = false;
                sb.append(ch);
            }
        }
        return sb.substring(0);
    }
}
