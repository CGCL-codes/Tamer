package net.sf.openv4j;

/**
 * DOCUMENT ME!
 *
 * @author aploese
 */
public class CycleTimeEntry {

    private int endHour;

    private int endMin;

    private int startHour;

    private int startMin;

    /**
     * DOCUMENT ME!
     *
     * @return the endHour
     */
    public int getEndHour() {
        return endHour;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the endMin
     */
    public int getEndMin() {
        return endMin;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the startHour
     */
    public int getStartHour() {
        return startHour;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the startMin
     */
    public int getStartMin() {
        return startMin;
    }

    /**
     * DOCUMENT ME!
     *
     * @param h DOCUMENT ME!
     * @param min DOCUMENT ME!
     */
    public void setEnd(int h, int min) {
        endHour = h;
        endMin = min;
    }

    /**
     * DOCUMENT ME!
     *
     * @param h DOCUMENT ME!
     * @param min DOCUMENT ME!
     */
    public void setStart(int h, int min) {
        startHour = h;
        startMin = min;
    }
}
