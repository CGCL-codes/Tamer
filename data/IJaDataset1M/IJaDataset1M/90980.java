package org.jmlspecs.racwrap.runner;

import java.util.Enumeration;

/**
This component implements the common functionality required in both branch 
and leaf nodes. Like getting and setting to allow precondition (etc) checks,
and getting and setting the name. Look at the javadocs for Node for more
information.
*/
public abstract class CommonImpl implements Node {

    private boolean checkable = false;

    private boolean wrap = false;

    private boolean checkPrecondition = false;

    private boolean checkPostcondition = false;

    private boolean checkInvariant = false;

    public boolean isCheckable() {
        return checkable;
    }

    public boolean isWrap() {
        return wrap;
    }

    public boolean isCheckPrecondition() {
        return checkPrecondition;
    }

    public boolean isCheckPostcondition() {
        return checkPostcondition;
    }

    public boolean isCheckInvariant() {
        return checkInvariant;
    }

    public void setCheckable(boolean b) {
        checkable = b;
    }

    /**
        @param propagate if set to true will also set the children.
    */
    public void setWrap(boolean b, boolean propagate) {
        if (!checkable) return;
        wrap = b;
        if (propagate && !this.isEmpty()) {
            Enumeration e = this.getChildren();
            while (e.hasMoreElements()) {
                Node n = (Node) e.nextElement();
                n.setWrap(b, propagate);
            }
        }
    }

    public void setCheckPrecondition(boolean b, boolean propagate) {
        if (!checkable) return;
        checkPrecondition = b;
        if (propagate && !this.isEmpty()) {
            Enumeration e = this.getChildren();
            while (e.hasMoreElements()) {
                Node n = (Node) e.nextElement();
                n.setCheckPrecondition(b, propagate);
            }
        }
    }

    public void setCheckPostcondition(boolean b, boolean propagate) {
        if (!checkable) return;
        checkPostcondition = b;
        if (propagate && !this.isEmpty()) {
            Enumeration e = this.getChildren();
            while (e.hasMoreElements()) {
                Node n = (Node) e.nextElement();
                n.setCheckPostcondition(b, propagate);
            }
        }
    }

    public void setCheckInvariant(boolean b, boolean propagate) {
        if (!checkable) return;
        checkInvariant = b;
        if (propagate && !this.isEmpty()) {
            Enumeration e = this.getChildren();
            while (e.hasMoreElements()) {
                Node n = (Node) e.nextElement();
                n.setCheckInvariant(b, propagate);
            }
        }
    }

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        String result = this.getName();
        result = result + " (";
        if (this.checkable) {
            if (this.isWrap()) result = result + "wrap, "; else result = result + "----, ";
            if (this.isCheckPrecondition()) result = result + "pre, "; else result = result + "---, ";
            if (this.isCheckPostcondition()) result = result + "post, "; else result = result + "----, ";
            if (this.isCheckInvariant()) result = result + "inv"; else result = result + "---";
        } else {
            result = result + "uncheckable";
        }
        result = result + ")";
        return result;
    }
}
