package org.openscience.references.db;

import java.io.*;

public class Journal implements Serializable {

    public String id;

    public String title;

    public String code;

    public String abbrev;

    public String ISSN;

    public String url;

    public Journal() {
        this.id = "";
        this.title = "";
        this.code = "";
        this.abbrev = "";
        this.ISSN = "";
        this.url = "";
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("id    : " + this.id + "\n");
        buf.append("title : " + this.title + "\n");
        buf.append("code  : " + this.code + "\n");
        buf.append("abbrev: " + this.abbrev + "\n");
        buf.append("ISSN  : " + this.ISSN + "\n");
        buf.append("url   : " + this.url + "\n");
        return buf.toString();
    }
}
