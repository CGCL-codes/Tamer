package org.archive.crawler.filter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.archive.crawler.deciderules.DecideRule;
import org.archive.crawler.deciderules.DecidingFilter;
import org.archive.crawler.framework.Filter;
import org.archive.crawler.settings.SimpleType;
import org.archive.util.SURT;
import org.archive.util.SurtPrefixSet;

/**
 * A filter which tests a URI against a set of SURT 
 * prefixes, and if the URI's prefix is in the set,
 * returns the chosen true/false accepts value. 
 * 
 * @author gojomo
 * @deprecated As of release 1.10.0.  Replaced by {@link DecidingFilter} and
 * equivalent {@link DecideRule}.
 */
public class SurtPrefixFilter extends Filter {

    private static final long serialVersionUID = -6933592892325852022L;

    public static final String ATTR_SURTS_SOURCE_FILE = "surts-source-file";

    public static final String ATTR_MATCH_RETURN_VALUE = "if-match-return";

    SurtPrefixSet surtPrefixes = null;

    /**
     * @param name
     */
    public SurtPrefixFilter(String name) {
        super(name, "SURT prefix filter *Deprecated* Use" + "DecidingFilter and equivalent DecideRule instead.");
        addElementToDefinition(new SimpleType(ATTR_MATCH_RETURN_VALUE, "What to return when " + "a prefix matches.\n", new Boolean(true)));
        addElementToDefinition(new SimpleType(ATTR_SURTS_SOURCE_FILE, "Source file from which to infer SURT prefixes. Any URLs " + "in file will be converted to the implied SURT prefix, and " + "literal SURT prefixes may be listed on lines beginning " + "with a '+' character.", ""));
    }

    protected synchronized boolean innerAccepts(Object o) {
        if (surtPrefixes == null) {
            readPrefixes();
        }
        String s = SURT.fromURI(o.toString());
        if (s.startsWith("https:")) {
            s = "http:" + s.substring(6);
        }
        return surtPrefixes.containsPrefixOf(s);
    }

    private void readPrefixes() {
        surtPrefixes = new SurtPrefixSet();
        String sourcePath = (String) getUncheckedAttribute(null, ATTR_SURTS_SOURCE_FILE);
        File source = new File(sourcePath);
        if (!source.isAbsolute()) {
            source = new File(getSettingsHandler().getOrder().getController().getDisk(), sourcePath);
        }
        FileReader fr = null;
        try {
            fr = new FileReader(source);
            try {
                surtPrefixes.importFromMixed(fr, true);
            } finally {
                fr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Re-read prefixes after a settings update.
     * 
     */
    public synchronized void kickUpdate() {
        super.kickUpdate();
        readPrefixes();
    }
}
