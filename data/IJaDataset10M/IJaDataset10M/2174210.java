package org.jxpl.primitives.pattern;

import org.w3c.dom.*;
import org.jxpl.*;
import org.jxpl.bindings.*;
import java.util.*;
import org.apache.log4j.*;

/**
A Unification Pattern Matcher
A pattern variable is represented by a list whose first element is
a JxplString with value="pattern-variable".  The second element is
a JxplString whose value is the variable name.
@author Jeff Brown
*/
public class Match implements Primitive {

    private Processor environment;

    private static Logger recorder = Logger.getLogger(Match.class.getName());

    public Match() {
        LogHelper.initialize(this);
    }

    /**
     * Set processor environment
     * @param env 
     */
    public void setProcessor(Processor env) {
        environment = env;
    }

    /**
     * Evaluate the list.
     * Input must be a JxplList starting with the Match primitive.
     * After head two JxplLists to match.  Returns a JxplList.  If list is empty,
     * then no match.  If list contains only the empty list, then literal match.  Otherwise, list contains
     * lists with two elements: one a pattern variable and one what it is bound to (could be another pattern variable).
     * @param input JxplList
     * @return JxplList
     * @throws IllegalArgumentException throw exception if input structure is wrong.
     */
    public JxplElement evaluate(JxplElement input) throws IllegalArgumentException {
        try {
            List list = ((JxplList) input).getElements();
            JxplList one = (JxplList) list.get(1);
            JxplList two = (JxplList) list.get(2);
            Hashtable hash = match(one, two, null);
            return buildBindingsList(hash);
        } catch (Exception ex) {
            recorder.error("", ex);
            throw new IllegalArgumentException(ex.getClass().getName() + "  " + ex.getMessage());
        }
    }

    /**
     *
     * @param first JxplElement
     * @param second JxplElement
     * @param bind Hashtable
     * @return Hastable
     */
    public Hashtable match(JxplElement first, JxplElement second, Hashtable bind) {
        if (bind == null) bind = new Hashtable();
        if (environment.isAtom(first)) {
            if (isVariable(first)) {
                if (variableName(second).equals(variableName(first))) return bind;
                if (bind.containsKey(variableName(first))) {
                    first = (JxplElement) bind.get(variableName(first));
                    if (!isVariable(first)) return null;
                }
                if (containsVariable(second, variableName(first))) return null;
                bind.put(variableName(first), second);
                return bind;
            }
        }
        if (environment.isAtom(second)) {
            if (isVariable(second)) {
                if (bind.containsKey(variableName(second))) {
                    second = (JxplElement) bind.get(variableName(second));
                    if (!isVariable(second)) return null;
                }
                if (containsVariable(first, variableName(second))) return null;
                bind.put(variableName(second), first);
                return bind;
            }
        }
        if (environment.isAtom(first) || environment.isAtom(second)) {
            if (!first.equals(second)) return null; else return bind;
        }
        JxplList fl = (JxplList) first;
        JxplList sl = (JxplList) second;
        bind = match(fl.first(), sl.first(), bind);
        if (bind == null) return null;
        return match(fl.rest(), sl.rest(), bind);
    }

    /**
     * Builds a JxplList from a given Hashtable
     * @param hash Hashtable
     * @return JxplList
     */
    public JxplList buildBindingsList(Hashtable hash) {
        if (hash == null) return new JxplList();
        JxplList out = new JxplList();
        Vector elem = out.getElements();
        JxplList out2 = new JxplList();
        Vector elem2 = out2.getElements();
        elem.add(out2);
        Enumeration enum1 = hash.keys();
        while (enum1.hasMoreElements()) {
            Object one = enum1.nextElement();
            Object two = hash.get(one);
            JxplList n = new JxplList();
            Vector elem3 = n.getElements();
            elem3.add(new JxplVariable((String) one));
            elem3.add(two);
            elem2.add(n);
        }
        return out;
    }

    /**
     * Returns a boolean if the JxplElement is a JxplVariable
     * @param in JxplElement
     * @return boolean
     */
    public boolean isVariable(JxplElement in) {
        return in instanceof JxplVariable;
    }

    /**
     * Returns an empty String in the JxplElement in is not a variable, else
     * the value of the JxplElement is returned
     * @param in JxplElement
     * @return String
     */
    public String variableName(JxplElement in) {
        if (!isVariable(in)) return "";
        return ((JxplVariable) in).getValue();
    }

    /**
     * Returns true if the JxplElement elem contains a variable
     * @param elem JxplElement
     * @param name String
     * @return boolean
     */
    public boolean containsVariable(JxplElement elem, String name) {
        if (isVariable(elem) && name.equals(variableName(elem))) return true;
        if (environment.isAtom(elem)) return false;
        JxplList list = (JxplList) elem;
        Vector e = list.getElements();
        for (int i = 0; i < e.size(); i++) {
            if (containsVariable((JxplElement) e.get(i), name)) return true;
        }
        return false;
    }
}
