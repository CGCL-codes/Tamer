package org.coinjema.context;

import java.io.Serializable;

/**
 * A CoinjemaContext object points to a separate context which may or may not hold individual config information
 * for the system.  It is normally used to create a new object in a different context, so that it receives different
 * configuration information than the calling class.  A CoinjemaContext is really no more than a String, but is used
 * to avoid namespace problems.  CoinjemaContext objects are really just pointers to the actual context - you can easily
 * create objects that point to non-existent contexts.  As such, they are cheap objects to create and leave for GC.
 * <p>
 * Context paths are made using strings of the form "parentContext/childContext/grandChildContext".
 * @author mikes
 *
 */
public final class CoinjemaContext implements Serializable {

    private static final long serialVersionUID = 1;

    static final CoinjemaContext rootContext = new CoinjemaContext();

    CoinjemaContext() {
        super();
        name = "";
        calcHash();
    }

    int reverseIndex = Integer.MIN_VALUE;

    int hash = 7;

    int depth = -1;

    /** 
     * Create a context that is a child of the given parent.
     * @param parent
     * @param subContext
     */
    public CoinjemaContext(final CoinjemaContext parent, final String subContext) {
        this(new StringBuffer(parent.name).append(subContext));
    }

    CoinjemaContext(final CoinjemaContext parent, CoinjemaContext child) {
        name = parent.getName() + child.getName();
        calcHash();
        reverseIndex = parent.getName().length() > 0 ? parent.getName().length() : Integer.MIN_VALUE;
    }

    private void calcHash() {
        hash = name.hashCode() * 3 + 7;
    }

    /**
     * Create a context with the given name.
     * @param n
     */
    public CoinjemaContext(final String n) {
        this(new StringBuffer((n != null) ? n : ""));
    }

    CoinjemaContext(String n, boolean prevetted) {
        name = n;
        calcHash();
        if (n.length() > 2) reverseIndex = name.lastIndexOf("/", name.length() - 2) + 1; else reverseIndex = -1;
    }

    /**
     * Create a context with the given name.
     * @param n
     */
    private CoinjemaContext(final StringBuffer n) {
        int len = n.length();
        if (len > 0) {
            if (n.charAt(len - 1) != '/') {
                n.append('/');
            }
            if (n.charAt(0) == '/') n.delete(0, 1);
            name = n.toString();
            reverseIndex = name.lastIndexOf("/", name.length() - 2) + 1;
        } else name = "";
        calcHash();
    }

    final synchronized int getDepth() {
        if (depth == -1) {
            depth = 0;
            int index = -1;
            while (name.length() > (index + 1) && (index = name.indexOf("/", index + 1)) > -1) {
                depth++;
            }
        }
        return depth;
    }

    final CoinjemaContext getParentContext() {
        if (reverseIndex > -1) return new CoinjemaContext(name.substring(0, reverseIndex), true); else if (reverseIndex == -1) return rootContext; else return null;
    }

    String name;

    @Override
    public final boolean equals(final Object obj) {
        if (obj instanceof CoinjemaContext) {
            CoinjemaContext c = (CoinjemaContext) obj;
            return (c.name == null && name == null) || (c.name != null && c.name.compareTo(name) == 0);
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return hash;
    }

    public final String toString() {
        return "Context:" + name + "(" + hashCode() + ")";
    }

    public final String getName() {
        return name;
    }
}
