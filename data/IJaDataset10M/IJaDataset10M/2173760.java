package edu.cmu.relativelayout.equation;

import java.util.HashMap;

/**
 * This class is the type for variables in equations.
 * 
 * @author Rachael Bennett (srbennett@gmail.com)
 * @author Brian Ellis (phoenix1701@gmail.com)
 */
public class Variable {

    private static HashMap<String, Variable> map = new HashMap<String, Variable>();

    /**
   * Returns a Variable initialized with the given name, creating it if it does not already exist.
   */
    public static Variable get(String name) {
        if (Variable.map.containsKey(name)) {
            return Variable.map.get(name);
        } else {
            Variable v = new Variable(name);
            Variable.map.put(name, v);
            return v;
        }
    }

    /**
   * Creates a new Variable with the given name. Protected because Variables with the same name are reused.
   */
    protected Variable(String name) {
        this.name = name;
    }

    /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
    @Override
    public boolean equals(Object theObj) {
        if (!(theObj instanceof Variable)) {
            return false;
        }
        return this.name.equals(((Variable) theObj).name);
    }

    /**
   * Returns ths name of this variable.
   */
    public String getName() {
        return this.name;
    }

    /**
   * @see java.lang.Object#hashCode()
   */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    /**
   * The name of this variable.
   */
    private String name;
}
