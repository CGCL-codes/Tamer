package net.innig.macker.event;

import net.innig.macker.rule.Rule;
import java.util.*;

public class MackerIsMadEvent extends EventObject {

    public MackerIsMadEvent(Rule rule, String description, List messages) {
        super(rule);
        this.rule = rule;
        this.description = description;
        this.messages = Collections.unmodifiableList(new ArrayList(messages));
    }

    public Rule getRule() {
        return rule;
    }

    public String getDescription() {
        return description;
    }

    public List getMessages() {
        return messages;
    }

    public String toString() {
        return getDescription();
    }

    public String toStringVerbose() {
        final String CR = System.getProperty("line.separator");
        StringBuffer s = new StringBuffer();
        s.append(getDescription());
        s.append(CR);
        for (Iterator i = messages.iterator(); i.hasNext(); ) {
            s.append("- ");
            s.append(i.next().toString());
            s.append(CR);
        }
        return s.toString();
    }

    private final Rule rule;

    private final String description;

    private final List messages;
}
