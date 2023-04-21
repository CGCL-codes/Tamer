package com.frinika.contrib.boblang;

import java.util.*;

/**
  Class to store an arbitrary number of graph points in a vector and
  provide fifo access to its elements
*/
public class GraphPointVector {

    private Vector v;

    private int count, index;

    /**
    Constructor to create the vector and initialise attributes
  */
    public GraphPointVector() {
        v = new Vector();
        count = 0;
        index = 0;
    }

    /**
    Add a point (GraphPoint object) to the end of the vector
  */
    public void addPoint(GraphPoint p) {
        v.add(p);
        count++;
    }

    /**
    Get the first element (GraphPoint object) from the vector
  */
    public GraphPoint getFirstPoint() {
        count = v.size();
        index = 0;
        return getNextPoint();
    }

    /**
    Get the next element (GraphPoint object) from the vector
  */
    public GraphPoint getNextPoint() {
        if (index >= count) {
            return null;
        } else {
            GraphPoint point = (GraphPoint) v.get(index);
            index++;
            return point;
        }
    }
}
