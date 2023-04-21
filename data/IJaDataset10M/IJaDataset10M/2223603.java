package com.controltier.ctl.types.controller;

import com.controltier.ctl.CtlException;
import com.controltier.ctl.CtlException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;

/**
 * ControlTier Software Inc.
 * User: alexh
 * Date: Jul 21, 2004
 * Time: 1:12:32 PM
 */
public class InstallDirectories {

    /**
     * Default constructor.
     */
    public InstallDirectories() {
    }

    /**
     * basedir property property. Typically refers to entity.instance.dir
     */
    private File basedir;

    /**
     * getter to basedir property
     *
     * @return
     */
    public File getBasedir() {
        return basedir;
    }

    /**
     * setter for basedir property
     *
     * @param basedir
     */
    public void setBasedir(final File basedir) {
        this.basedir = basedir;
    }

    /**
     * dirlist property. Has a comma separated list of sub dirs. The complete dir path
     * is defined by prepending basedir and the elements of this list.
     */
    private String dirlist;

    /**
     * getter for dirlist
     *
     * @return
     */
    public String getDirlist() {
        return dirlist;
    }

    /**
     * setter for dirlist
     *
     * @param dirlist
     */
    public void setDirlist(final String dirlist) {
        this.dirlist = dirlist;
    }

    /**
     * Utility method returns the dirlist elements as a Collection of Strings
     *
     * @return
     */
    public Collection getDirlistAsCollection() {
        final StringTokenizer st = new StringTokenizer(getDirlist(), ",");
        final Collection c = new ArrayList();
        while (st.hasMoreTokens()) {
            c.add(st.nextToken());
        }
        return c;
    }

    /**
     * validates input to this datatype
     *
     * @throws CtlException
     */
    public void validateAttributes() throws CtlException {
        if (null == basedir) throw new CtlException("basedir attribute not set.");
        final String[] result = basedir.getAbsolutePath().split("\\$\\{");
        if (result.length > 1) {
            throw new CtlException("basedir value contained " + +(result.length - 1) + " unresolved property references: " + basedir.getAbsolutePath());
        }
        if (null == dirlist) throw new CtlException("dirlist attribute not set.");
    }

    /**
     * Returns baseidr and dirlist values
     *
     * @return
     */
    public String toString() {
        return "InstallDirectories{" + "basedir=" + basedir + ", dirlist='" + dirlist + "'" + "}";
    }
}
