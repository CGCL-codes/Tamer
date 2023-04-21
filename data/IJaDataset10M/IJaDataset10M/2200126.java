package org.fudaa.fudaa.tr.post.data;

import gnu.trove.TObjectLongHashMap;
import java.io.File;

/**
 * @author Fred Deniger
 * @version $Id: TrPostDataInfoDoc.java,v 1.6 2007-05-04 14:01:52 deniger Exp $
 */
public class TrPostDataInfoDoc {

    TObjectLongHashMap locLastModified = new TObjectLongHashMap();

    /**
   * Constructeur par defaut: certains proprietes sont reprises depuis les prefs.
   */
    public TrPostDataInfoDoc() {
    }

    /**
   * @param _f le fichier source
   */
    public void addFile(final File _f) {
        locLastModified.put(_f.getAbsolutePath(), _f.lastModified());
    }

    /**
   * @return the lastModifiedTime
   */
    public long getLastModifiedTime(String absolutePath) {
        return locLastModified.contains(absolutePath) ? locLastModified.get(absolutePath) : -1;
    }
}
