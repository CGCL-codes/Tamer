package com.pobox.tupletest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

public class Item implements Comparable<Item> {

    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(Item.class);

    private int dimension;

    private char feature;

    public Item(int dimension, char feature) {
        this.dimension = dimension;
        this.feature = feature;
    }

    public Item() {
        this.dimension = 0;
        this.feature = '?';
    }

    public int getDimension() {
        logger.debug("getDimension() - start");
        return dimension;
    }

    public void setDimension(int dimension) {
        logger.debug("setDimension(int) - start - dimension=" + dimension);
        this.dimension = dimension;
    }

    public char getFeature() {
        logger.debug("getFeature() - start");
        return feature;
    }

    public void setFeature(char feature) {
        logger.debug("setFeature(char) - start - char=" + feature);
        this.feature = feature;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object object) {
        logger.debug("equals(Object) - start");
        if (!(object instanceof Item)) {
            logger.debug("equals(Object) - end - not correct instanceOf so false");
            return false;
        }
        if (this == object) {
            logger.debug("equals(Object) - end - true as same object");
            return true;
        }
        Item rhs = (Item) object;
        boolean returnboolean = getDimension() == rhs.getDimension() && getFeature() == rhs.getFeature();
        logger.debug("equals(Object) - end - returnBoolean = " + returnboolean);
        return returnboolean;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        logger.debug("hashCode() - start");
        int returnint = (1777 * getDimension()) + (4241 * getFeature());
        logger.debug("hashCode() - end - returnInt=" + returnint);
        return returnint;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        logger.debug("toString() - start");
        String returnString = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("dimension", this.dimension).append("feature", this.feature).toString();
        logger.debug("toString() - end + " + returnString);
        return returnString;
    }

    public int compareTo(Item other) {
        logger.debug("compareTo(Object) - start");
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;
        if (this == other) {
            logger.debug("compareTo(Object) - end - same instance");
            return EQUAL;
        }
        Item otherItem = (Item) other;
        if (this.getDimension() < otherItem.getDimension()) {
            logger.debug("compareTo(Object) - end - dimension less than");
            return BEFORE;
        }
        if (this.getDimension() > otherItem.getDimension()) {
            logger.debug("compareTo(Object) - end - dimension greater than");
            return AFTER;
        }
        if (this.getFeature() < otherItem.getFeature()) {
            logger.debug("compareTo(Object) - end - feature less than");
            return BEFORE;
        }
        if (this.getFeature() > otherItem.getFeature()) {
            logger.debug("compareTo(Object) - end - feature greater than");
            return AFTER;
        }
        assert this.equals(other) : "compareTo inconsistent with equals.";
        logger.debug("compareTo(Object) - end - sending back equal");
        return EQUAL;
    }
}
