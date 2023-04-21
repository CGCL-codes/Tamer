package org.fsm4j;

import java.util.regex.Pattern;

/**
 * FSMCondition that matches a given regular expression.
 */
public class Regex implements FSMCondition {

    private Pattern pattern;

    /**
     * Define the regular expression to match. This uses Java's regular
     * expression matching.
     */
    public Regex(String regex) {
        if (regex == null) FSM.stop_NullParameter("Regex.this", "regex");
        pattern = Pattern.compile(regex);
    }

    /**
     * Returns true if the event's toString() method returns a string that
     * matches the regular expression given in the constructor.
     */
    public boolean isMatch(Object event) {
        return pattern.matcher(event.toString()).matches();
    }
}
