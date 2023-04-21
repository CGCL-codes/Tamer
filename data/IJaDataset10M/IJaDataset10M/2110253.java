package net.sf.jga;

import java.io.Serializable;
import java.text.Collator;
import java.text.RuleBasedCollator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Comparator for use in testing various functor primitives.  Wraps a Comparator
 * that is obtained from the standard java environment.
 * <p>
 * Copyright &copy; 2002-2005  David A. Hall
 *
 * @author <a href="mailto:davidahall@users.sourceforge.net">David A. Hall</a>
 */
@SuppressWarnings("serial")
public class SampleStringComparator implements Comparator<String>, Serializable {

    private transient RuleBasedCollator collator;

    public int compare(String arg1, String arg2) {
        return getCollator().compare(arg1, arg2);
    }

    private RuleBasedCollator getCollator() {
        if (collator == null) collator = (RuleBasedCollator) Collator.getInstance(new Locale("en", "US", ""));
        return collator;
    }
}
