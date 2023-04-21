package de.iqcomputing.flap.rules;

import java.awt.*;
import java.util.*;
import javax.mail.*;
import javax.swing.*;
import org.jdom.*;

public class AllOfCondition extends Condition {

    protected Condition[] conditions;

    public AllOfCondition() {
        this.conditions = new Condition[0];
    }

    public AllOfCondition(Condition[] conditions) {
        this.conditions = conditions;
    }

    public static AllOfCondition constructAllOfCondition(Element allOf, int propertiesVersion) {
        java.util.List conditions = allOf.getChild("conditions").getChildren("condition");
        Iterator i = conditions.iterator();
        ArrayList conds = new ArrayList();
        while (i.hasNext()) conds.add(Condition.constructCondition((Element) i.next(), propertiesVersion));
        return new AllOfCondition((Condition[]) conds.toArray(new Condition[0]));
    }

    public boolean applies(Message msg) throws RuleException {
        int i;
        for (i = 0; i < conditions.length; i++) {
            if (!conditions[i].applies(msg)) return false;
        }
        return true;
    }

    public Element conditionElement() {
        Element condition = new Element("condition");
        Element e;
        int i;
        condition.setAttribute(new Attribute("type", "allOf"));
        e = new Element("conditions");
        for (i = 0; i < conditions.length; i++) e.addContent(conditions[i].conditionElement());
        condition.addContent(e);
        return condition;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("clone not supported");
    }

    public static String getDisplayName() {
        return "Match all conditions";
    }

    public String getDisplayNameWithOptions() {
        return getDisplayName();
    }
}
