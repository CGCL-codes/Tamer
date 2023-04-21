package org.rr.jsendfile.util.typ;

import java.io.File;

/**
 *
 * @author  R�diger Rauschenbach
 * Class describes a Sendable Object like a Text or a File. This object knows two Users!
 */
public abstract class Transferable {

    protected User from;

    protected User to;

    protected File spoolFilePath;

    /** Creates a new instance of Message */
    public Transferable() {
    }

    public Transferable(User inFrom, User inTo) {
        this.from = inFrom;
        this.to = inTo;
    }

    public User getUserFrom() {
        return this.from;
    }

    public String getUserFromRealname() {
        return this.from.getUserRealName();
    }

    public User getUserTo() {
        return this.to;
    }

    public void setUserFrom(User u) {
        this.from = u;
    }

    public void setUserFromRealname(String u) {
        this.from.setUserRealName(u);
    }

    public void setUserTo(User u) {
        this.to = u;
    }

    public String toString() {
        return ("From: " + from.toString() + " To: " + to.toString());
    }

    /** Set Spoolfilepath (without the Username at the End */
    public void setSpoolPath(File f) {
        this.spoolFilePath = f;
    }

    /** Set Spoolfilepath (without the Username at the End */
    public void setSpoolPath(String s) {
        this.spoolFilePath = new File(s);
    }

    /** get the full Path of the Spoolfile with Username as Last Dir */
    public File getFinalSpoolPath() {
        if (this.getUserTo() != null) {
            return new File(spoolFilePath.getPath() + "/" + this.getUserTo());
        } else {
            return null;
        }
    }

    /** get the full Path of the Spoolfile with Username and Tempath as Last Dir */
    public File getFinalTempSpoolPath() {
        if (this.getUserTo() != null) {
            return new File(spoolFilePath.getPath() + "/" + this.getUserTo() + "/temp");
        } else {
            return null;
        }
    }

    /** get the full Path of the Spoolfile with Username as Last Dir */
    public File getSpoolPath() {
        return spoolFilePath;
    }
}
