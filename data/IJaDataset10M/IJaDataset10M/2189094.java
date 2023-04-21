package edu.uvm.cs.calendar.graph.edges;

import edu.uvm.cs.calendar.graph.Edge;

/**
 * For example, FirstMondays = Intersect(Monday, FirstOfMonth). If FirstOfMonth
 * is the first day of every month, FirstMondays are all the first days of the
 * month that are mondays.
 * 
 * @author Jeremy Gustie
 * @version %I%, %G%
 * @since 1.0
 */
public class Intersect extends Edge {

    public String toString() {
        return "-I" + super.toString();
    }

    public boolean acceptTailLayer(int operand, int layer) {
        return ((operand == 0) && (layer == 2)) || ((operand == 1) && (layer == 1 || layer == 2 || layer == 3));
    }

    public int getLayer() {
        return 2;
    }
}
