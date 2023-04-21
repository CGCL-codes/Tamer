package org.liris.schemerger.core.pattern;

import java.util.Set;
import java.util.TreeSet;
import org.liris.schemerger.core.dataset.network.Nodeable;
import org.liris.schemerger.core.instance.Value;

/**
 * 
 * A variable for the discovery of chronicle with variables.
 * 
 * @author Damien Cram
 * 
 * @see org.liris.schemerger.chronicle.ComplexChrMiner
 * 
 */
public class Variable implements Nodeable {

    private static int ID_CNT = 1;

    private int id;

    private String label;

    private Set<Value> domain;

    public Variable(String label) {
        super();
        init(label);
        this.domain = new TreeSet<Value>();
    }

    public Variable(String label, Set<Value> domain) {
        super();
        init(label);
        this.domain = domain;
    }

    private void init(String label) {
        this.id = ID_CNT++;
        if (label == null) label = "?x" + id;
        this.label = label;
    }

    public Variable(String label, Value... domain) {
        super();
        init(label);
        this.domain = new TreeSet<Value>();
        for (Value s : domain) this.domain.add(s);
    }

    @Override
    public String toString() {
        String s = label + " in {";
        boolean first = true;
        for (Value v : domain) {
            s += (!first ? "," : "") + v;
            first = false;
        }
        s += "}";
        return label;
    }

    public String getLabel() {
        return label;
    }

    public int getId() {
        return id;
    }

    public Set<Value> getDomain() {
        return domain;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            Variable v = (Variable) obj;
            return label.equals(v.label);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
