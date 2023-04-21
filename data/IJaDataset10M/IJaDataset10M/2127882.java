package com.netx.eap.R1.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import com.netx.basic.R1.eh.Checker;

public class Values {

    final List<Segment> segments;

    final Map<String, Segment> variables;

    private boolean _lenient;

    private final Set<String> _setVars;

    private final Set<String> _setIfs;

    Values() {
        segments = new ArrayList<Segment>();
        variables = new HashMap<String, Segment>();
        _setVars = new HashSet<String>();
        _setIfs = new HashSet<String>();
    }

    Values(Values another) {
        segments = new ArrayList<Segment>(another.segments.size());
        variables = new HashMap<String, Segment>(another.variables.size());
        _setVars = new HashSet<String>();
        _setIfs = new HashSet<String>();
        for (Segment s1 : another.segments) {
            Segment s2 = new Segment(s1);
            segments.add(s2);
            if (s2.isVariable()) {
                variables.put(s2.getName(), s2);
            }
        }
    }

    public int size() {
        return segments.size() + variables.size();
    }

    public boolean getLenient() {
        return _lenient;
    }

    public void setLenient(boolean lenient) {
        _lenient = lenient;
    }

    Values addList(String listName, Segment list) {
        Checker.checkEmpty(listName, "listName");
        for (Segment s : segments) {
            if (s.isList() && s.getName().equals(listName)) {
                throw new IllegalArgumentException("[list] '" + listName + "' has already been added");
            }
        }
        segments.add(list);
        return this;
    }

    Values addIf(String ifName, Segment ifList) {
        Checker.checkEmpty(ifName, "ifName");
        for (Segment s : segments) {
            if (s.isIf() && s.getName().equals(ifName)) {
                throw new IllegalArgumentException("[if] '" + ifName + "' has already been added");
            }
        }
        segments.add(ifList);
        return this;
    }

    public Values set(String varName, Object value) {
        Checker.checkEmpty(varName, "varName");
        if (!_lenient) {
            if (_setVars.contains(varName)) {
                throw new IllegalArgumentException("variable '" + varName + "' has already been set");
            }
        }
        boolean found = false;
        for (Segment s : segments) {
            if (s.isVariable() && s.getName().equals(varName)) {
                s.setValue(value);
                found = true;
                _setVars.add(varName);
            }
        }
        if (found) {
            return this;
        }
        throw new IllegalArgumentException("variable '" + varName + "' not found");
    }

    public ValueList getList(String listName) {
        Checker.checkEmpty(listName, "listName");
        for (Segment s : segments) {
            if (s.isList() && s.getName().equals(listName)) {
                return s.getList();
            }
        }
        throw new IllegalArgumentException("[list] '" + listName + "' not found");
    }

    public Values setIf(String ifName, boolean value) {
        Checker.checkEmpty(ifName, "ifName");
        if (!_lenient) {
            if (_setIfs.contains(ifName)) {
                throw new IllegalArgumentException("[if] '" + ifName + "' has already been set");
            }
        }
        for (Segment s : segments) {
            if (s.isIf() && s.getName().equals(ifName)) {
                s.setValue(value);
                _setIfs.add(ifName);
                return s.getIf();
            }
        }
        throw new IllegalArgumentException("[if] '" + ifName + "' not found");
    }

    void setValuesFrom(Values other) {
        for (Segment s : segments) {
            if (s.isVariable()) {
                s.setValue(other.variables.get(s.getName()).getText());
            }
            if (s.isIf()) {
                for (Segment os : other.segments) {
                    if (os.isIf() && os.getName().equals(s.getName())) {
                        if (os.getRawValue() != null) {
                            s.setValue(os.getRawValue());
                        }
                        s.getIf().setValuesFrom(os.getIf());
                    }
                }
            }
            if (s.isList()) {
                ValueList list = s.getList();
                List<Values> otherList = other.getList(s.getName()).getValues();
                for (Values otherValues : otherList) {
                    Values v = list.next();
                    v.setValuesFrom(otherValues);
                }
            }
        }
    }
}
