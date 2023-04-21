package org.petsoar.security;

public class Name {

    private String prefix;

    private String first;

    private String middle;

    private String last;

    /**
     * @hibernate.property
     */
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @hibernate.property
     */
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * @hibernate.property
     */
    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    /**
     * @hibernate.property
     */
    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
