package org.mov.chart;

import java.util.Vector;

public class DrawnLine {

    private Coordinate start;

    private Coordinate end;

    public DrawnLine() {
        start = null;
        end = null;
    }

    /**
     * Construct a new DrawnLine with a pair of end points
     * 
     * @param start  The first end point
     * @param end    The second end point
     *
    */
    public DrawnLine(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Return the coordinate of the first point of the line
     * 
     * @return The Coordinate of the start point

    */
    public Coordinate getStart() {
        return start;
    }

    /**
     * Return the coordinate of the last point of the line
     * 
     * @return The Coordinate of the end point

    */
    public Coordinate getEnd() {
        return end;
    }

    /**
     * Set the start point of the line
     *
     * @param p  The Coordinate of the first point in the line
     */
    public void setStart(Coordinate p) {
        start = p;
    }

    /**
     * Set the end point of the line
     *
     * @param p  The Coordinate of the end point in the line
     */
    public void setEnd(Coordinate p) {
        end = p;
    }

    /**
     * 
     * Direct comparison between this line and a specified one
     *
     * @param The line to compare this one to
     * @return True if the line has the same start and end point
     *         false otherwise
       
    */
    public boolean compareTo(DrawnLine l) {
        if (start == null || end == null) {
            return false;
        }
        if (start.compareTo(l.getStart()) && end.compareTo(l.getEnd())) {
            return true;
        }
        return false;
    }

    /**
     * 
     * Direct comparison between this line and a specified one
     *
     * @param The line to compare this one to
     * @return True if the line has the same start and end point
     *         false otherwise
       
    */
    public boolean compareTo(Coordinate start, Coordinate end) {
        DrawnLine temp = new DrawnLine(start, end);
        return compareTo(temp);
    }

    public String toString() {
        String rv = "Start: " + start + " " + "End: " + end;
        return rv;
    }
}
