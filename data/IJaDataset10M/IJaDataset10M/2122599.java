package com.sshtools.common.mru;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.File;
import javax.swing.AbstractListModel;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.14 $
 */
public class MRUListModel extends AbstractListModel {

    private static Log log = LogFactory.getLog(MRUListModel.class);

    private MRUList mru;

    /**
* Creates a new MRUListModel object.
*/
    public MRUListModel() {
        super();
        setMRUList(new MRUList());
    }

    /**
*
*
* @param f
*/
    public void add(File f) {
        mru.insertElementAt(f, 0);
        for (int i = mru.size() - 1; i >= 1; i--) {
            if (((File) mru.elementAt(i)).equals(f)) {
                mru.removeElementAt(i);
            }
        }
        if (mru.size() > 15) {
            for (int i = mru.size() - 1; i >= 15; i--) {
                mru.removeElementAt(i);
            }
        }
        fireContentsChanged(this, 0, getSize() - 1);
    }

    /**
*
*
* @param i
*
* @return
*/
    public Object getElementAt(int i) {
        return mru.get(i);
    }

    /**
*
*
* @return
*/
    public int getSize() {
        return (mru == null) ? 0 : mru.size();
    }

    /**
*
*
* @param mru
*/
    public void setMRUList(MRUList mru) {
        this.mru = mru;
        fireContentsChanged(this, 0, getSize());
    }

    /**
*
*
* @return
*/
    public MRUList getMRUList() {
        return mru;
    }
}
