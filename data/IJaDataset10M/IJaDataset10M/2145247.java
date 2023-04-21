package org.apache.webdav.ant.taskdefs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.webdav.ant.WebdavFileSet;

/**
 * Baseclass of all WebDAV tasks that work on sets of WebDAV resources.
 * 
 * <p>Provides nested {@link org.apache.webdav.ant.WebdavFileSet}s.
 * 
 */
public abstract class WebdavMatchingTask extends WebdavTask {

    protected List filesets = new ArrayList();

    public void addDavfileset(WebdavFileSet set) {
        filesets.add(set);
    }

    protected Iterator getFileSets() {
        return this.filesets.iterator();
    }
}
