package net.innig.macker.recording;

import net.innig.macker.event.*;
import net.innig.macker.rule.Rule;
import net.innig.macker.rule.RuleSet;
import java.io.PrintWriter;
import java.util.*;
import org.jdom.Element;
import org.jdom.Attribute;
import net.innig.collect.CollectionDiff;

public class GenericRuleRecording extends EventRecording {

    public GenericRuleRecording(EventRecording parent) {
        super(parent);
        events = new HashSet();
    }

    public EventRecording record(MackerEvent event) {
        if (rule == null) rule = event.getRule();
        if (event.getRule() != rule) return getParent().record(event);
        Map eventAttributes = new TreeMap();
        eventType = event.getClass().getName();
        if (eventType.startsWith(DEFAULT_EVENT_PACKAGE)) eventType = eventType.substring(DEFAULT_EVENT_PACKAGE.length());
        eventAttributes.put("type", eventType);
        eventAttributes.put("severity", event.getRule().getSeverity().getName());
        int msgNum = 0;
        for (Iterator msgIter = event.getMessages().iterator(); msgIter.hasNext(); msgNum++) eventAttributes.put("message" + msgNum, msgIter.next());
        if (event instanceof MessageEvent) {
        } else if (event instanceof AccessRuleViolation) {
            AccessRuleViolation arv = (AccessRuleViolation) event;
            eventAttributes.put("from", arv.getFrom().getFullName());
            eventAttributes.put("to", arv.getTo().getFullName());
        } else throw new IllegalArgumentException("Unknown event type: " + event);
        events.add(eventAttributes);
        return this;
    }

    public void read(Element elem) {
        Set eventSet = new HashSet();
        Map baseAtt = getAttributeValueMap(elem);
        for (Iterator evtIter = elem.getChildren("event").iterator(); evtIter.hasNext(); ) {
            Element eventElem = (Element) evtIter.next();
            Map eventAtt = new TreeMap(baseAtt);
            eventAtt.putAll(getAttributeValueMap(eventElem));
            eventType = (String) eventAtt.get("type");
            events.add(eventAtt);
        }
    }

    private Map getAttributeValueMap(Element elem) {
        Map attValues = new TreeMap();
        for (Iterator i = elem.getAttributes().iterator(); i.hasNext(); ) {
            Attribute attr = (Attribute) i.next();
            attValues.put(attr.getName(), attr.getValue());
        }
        return attValues;
    }

    public boolean compare(EventRecording actual, PrintWriter out) {
        if (!super.compare(actual, out)) return false;
        boolean match = true;
        GenericRuleRecording actualGRR = (GenericRuleRecording) actual;
        Set expectedSet = events;
        Set actualSet = actualGRR.events;
        CollectionDiff diff = new CollectionDiff(expectedSet, actualSet);
        if (!diff.getRemoved().isEmpty()) {
            out.println(this + ": missing events:");
            dump(out, diff.getRemoved());
            match = false;
        }
        if (!diff.getAdded().isEmpty()) {
            out.println(this + ": unexpected events:");
            dump(out, diff.getAdded());
            match = false;
        }
        return match;
    }

    private void dump(PrintWriter out, Collection events) {
        for (Iterator i = events.iterator(); i.hasNext(); ) out.println("    " + i.next());
    }

    public String toString() {
        return "[rule:" + eventType + "]";
    }

    public void dump(PrintWriter out, int indent) {
        super.dump(out, indent);
        for (Iterator eventIter = events.iterator(); eventIter.hasNext(); ) {
            Map event = (Map) eventIter.next();
            for (int n = -3; n < indent; n++) out.print(' ');
            out.println(event);
        }
    }

    private Rule rule;

    private String eventType;

    private Set events;

    private static final String DEFAULT_EVENT_PACKAGE = "net.innig.macker.event.";
}
