package com.nexwave.nsidita;

import java.io.File;

/**
 * Object for describing a dita or html file.
 *
 * @author N. Quaine
 * @version 2.0 2010-08-14
 */
public class DocFileInfo {

    File fullpath = null;

    String title = null;

    String shortdesc = null;

    String relpathToDocRep = null;

    String deltaPathToDocRep = null;

    public DocFileInfo() {
    }

    public DocFileInfo(File file) {
        fullpath = file;
    }

    public DocFileInfo(DocFileInfo info) {
        this.fullpath = info.fullpath;
        this.title = info.title;
        this.shortdesc = info.shortdesc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortdesc(String shortDesc) {
        this.shortdesc = shortDesc;
    }

    /**
     * @return the shortdesc
     */
    public String getShortdesc() {
        return shortdesc;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    public File getFullpath() {
        return fullpath;
    }

    public void setFullpath(File fullpath) {
        this.fullpath = fullpath;
    }
}
