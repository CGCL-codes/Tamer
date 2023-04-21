package com.uebbing.atleto.data;

import org.jdom.*;

/**
 * @author Robert Uebbing
 *
 * @TODO add loadXML and toXML
 */
public class Physical {

    int restingHR = 0;

    int weight = 0;

    int bodyFat = 0;

    int rating = 3;

    /**
    *
    */
    public Physical() {
        restingHR = 0;
        weight = 0;
        bodyFat = 0;
        rating = 3;
    }

    /**
    * @param restingHR
    * @param weight
    * @param bodyFat
    * @param rating
    */
    public Physical(int restingHR, int weight, int bodyFat, int rating) {
        this.restingHR = restingHR;
        this.weight = weight;
        this.bodyFat = bodyFat;
        this.rating = rating;
    }

    /**
    * Populate the Physical object using a JDOM Element
    * @param physical element of an atleto data file
    */
    public void setContents(Element dom) {
        try {
            setRestingHR(dom.getAttribute("restinghr").getIntValue());
            setWeight(dom.getAttribute("weight").getIntValue());
            setBodyFat(dom.getAttribute("bodyfat").getIntValue());
            setRating(dom.getAttribute("rating").getIntValue());
        } catch (DataConversionException dce) {
            System.out.println("Exception thrown on data conversion");
            dce.printStackTrace();
        }
    }

    /**
    * @return Returns the bodyFat.
    */
    public int getBodyFat() {
        return bodyFat;
    }

    /**
    * @param bodyFat The bodyFat to set.
    */
    public void setBodyFat(int bodyFat) {
        this.bodyFat = bodyFat;
    }

    /**
    * @return Returns the rating.
    */
    public int getRating() {
        return rating;
    }

    /**
    * @param rating The rating to set.
    */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
    * @return Returns the restingHR.
    */
    public int getRestingHR() {
        return restingHR;
    }

    /**
    * @param restingHR The restingHR to set.
    */
    public void setRestingHR(int restingHR) {
        this.restingHR = restingHR;
    }

    /**
    * @return Returns the weight.
    */
    public int getWeight() {
        return weight;
    }

    /**
    * @param weight The weight to set.
    */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
    * Creates an xml representation of this object indented by tabs.
    * @return xml represention of this weather object
    * @param number of tabs to indent
    * @see #toString()
    */
    public String toXML(int tabs) {
        StringBuffer sb = new StringBuffer();
        StringBuffer tsb = new StringBuffer();
        for (int i = 0; i < tabs; i++) tsb.append("  ");
        sb.append(tsb);
        sb.append("<Physical");
        sb.append(" restinghr=\"" + String.valueOf(getRestingHR()) + "\"");
        sb.append(" weight=\"" + String.valueOf(getWeight()) + "\"");
        sb.append(" bodyfat=\"" + String.valueOf(getBodyFat()) + "\"");
        sb.append(" rating=\"" + String.valueOf(getRating()) + "\" />\n");
        return sb.toString();
    }

    /**
    * Creates a string representation of this object formatted in xml.
    * @return xml represention of a split
    * @see #toXML( int tabs)
    */
    public String toString() {
        return toXML(0);
    }
}
