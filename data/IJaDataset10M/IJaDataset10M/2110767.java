package tm.clc.analysis;

import java.util.Hashtable;
import tm.utilities.Assert;

/**
 * A table of all <code>CodeGenRule</code>s for a particular language
 * implementation. Rules are accessed by descriptive strings, defined 
 * here where language neutral, or in subclasses for language-specific
 * rules.
 */
public class RuleBase {

    private Hashtable rules = new Hashtable();

    /**
     * Adds a new entry in this <code>RuleBase</code>
     * @param key a <code>String</code> description of the rule, also used 
     * as a key to the rule.
     * @param rule the <code>CodeGenRule</code> to add.
     */
    public void put(String key, CodeGenRule rule) {
        rules.put(key, rule);
        rule.description = key;
    }

    /**
     * Returns the <code>CodeGenRule</code> corresponding to the provided key
     * @param key a <code>String</code> description of the rule, used 
     * as a key to the rule.
     * @return the matching <code>CodeGenRule</code>, or <code>null</code> if
     * no match was found.
     */
    public CodeGenRule get(String key) {
        Object temp = rules.get(key);
        Assert.check(temp != null);
        return (CodeGenRule) temp;
    }
}
