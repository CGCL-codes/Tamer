package org.opennms.web.map.config;

/**
 * @author mmigliore
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@SuppressWarnings("unchecked")
public class Avail implements Comparable {

    private int id;

    private int min;

    private String color;

    private boolean flash = false;

    public Avail(int id, int min, String color) {
        this.id = id;
        this.min = min;
        this.color = color;
    }

    /**
	 * Compares the Avail to another in input by min.
	 */
    public int compareTo(Object otherAvail) {
        Avail othAvail = (Avail) otherAvail;
        if (this.min == othAvail.getMin()) return 0; else if (this.min < othAvail.getMin()) return -1; else return 1;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public boolean isFlash() {
        return flash;
    }

    public void setFlash(boolean flash) {
        this.flash = flash;
    }
}
