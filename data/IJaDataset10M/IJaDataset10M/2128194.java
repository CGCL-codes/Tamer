package org.fmi.bioinformatics.format.swissprot;

import org.fmi.bioinformatics.format.tools.StringTool;

/**
 * @author mhaimel
 *
 */
public class Description {

    public static String ID = "DE";

    private String descr = null;

    public Description() {
        super();
        descr = "";
    }

    public Description(String descr) {
        this();
        this.descr = descr;
    }

    public Description(Description de) {
        this();
        if (null != de) {
            if (null != de.getDescr()) this.descr = new String(de.getDescr());
        }
    }

    public static String getID() {
        return ID;
    }

    public static void setID(String id) {
        ID = id;
    }

    public void addDescr(String s) {
        String tmp = "";
        if ((null != this.getDescr()) && (this.getDescr().trim().length() > 0)) {
            tmp = this.getDescr() + " ";
        }
        this.setDescr(tmp + s.trim());
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Override
    public String toString() {
        return StringTool.instance().formatStringLines(this.descr, 5, 70, Description.ID);
    }
}
