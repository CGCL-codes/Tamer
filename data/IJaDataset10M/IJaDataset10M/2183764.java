package net.gcalc.juu.environment;

import java.util.HashMap;

public class Variable implements Comparable<Variable> {

    private static Object lock = new Object();

    private static HashMap<String, Variable> map = new HashMap<String, Variable>();

    private static long counter = 0;

    public final String name;

    private Variable() {
        this("#fresh" + counter);
    }

    public static Variable get(String s) {
        Variable var = null;
        synchronized (map) {
            var = map.get(s);
            if (var == null) map.put(s, var = new Variable(s));
        }
        return var;
    }

    public static Variable fresh() {
        Variable fresh = null;
        synchronized (lock) {
            fresh = new Variable();
        }
        return fresh;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    private Variable(String s) {
        assert (s != null);
        name = s;
    }

    public String toString() {
        return "var(" + name + ")";
    }

    @Override
    public int compareTo(Variable v) {
        return name.compareTo(v.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Variable other = (Variable) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
