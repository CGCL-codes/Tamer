package edu.neu.ccs.demeterf.lib;

/** Represents the color Red for RBTrees */
public class RED extends RBColor {

    public RED() {
    }

    public String toString() {
        return "red";
    }

    public boolean equals(Object o) {
        return (o instanceof RED);
    }

    public boolean isRed() {
        return true;
    }
}
