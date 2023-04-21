package de.spieleck.app.jacson.select;

import org.apache.oro.text.regex.*;
import de.spieleck.config.ConfigNode;
import de.spieleck.config.ConfigVerify.Acceptor;
import de.spieleck.app.jacson.JacsonSelect;
import de.spieleck.app.jacson.JacsonRegistry;
import de.spieleck.app.jacson.SelectionResult;
import de.spieleck.app.jacson.JacsonConfigException;
import de.spieleck.app.jacson.util.RegExpUtil;
import de.spieleck.app.jacson.util.ConfigUtil;

/**
 * Select all the groups in the matching regexp.
 * @author fsn
 */
public class RegExpGroupSelect implements JacsonSelect, Acceptor {

    /** oro-Regexp-Pattern */
    protected Pattern pattern;

    protected RegExpUtil rutil;

    protected String curChunk;

    protected SimpleResult result = new SimpleResult();

    protected MatchResult match = null;

    protected int matchCount = 0;

    protected PatternMatcherInput pmi;

    public RegExpGroupSelect() {
    }

    public void init(ConfigNode config, JacsonRegistry registry) throws JacsonConfigException {
        rutil = registry.getRegExpUtil();
        pattern = rutil.obtainPattern("RegExpExtract", config, registry);
        ConfigUtil.verify(config, this);
    }

    public boolean accept(ConfigNode node) {
        return rutil.accept(node);
    }

    public void setChunk(String chunk) {
        curChunk = chunk;
        match = null;
        pmi = new PatternMatcherInput(curChunk);
    }

    public SelectionResult getNextSelection() {
        if (curChunk == null) return null; else {
            while (true) {
                if (match == null) {
                    PatternMatcher matcher = rutil.getMatcher();
                    if (!matcher.contains(pmi, pattern)) return null;
                    match = matcher.getMatch();
                    matchCount = 1;
                }
                int n = match.groups();
                while (matchCount < n) {
                    int start = match.beginOffset(matchCount);
                    int end = match.endOffset(matchCount);
                    String grp = match.group(matchCount);
                    matchCount++;
                    if (grp != null) {
                        result.set(start, end, grp);
                        return result;
                    }
                }
                match = null;
            }
        }
    }
}
