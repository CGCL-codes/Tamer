package org.apache.myfaces.config.element;

import java.util.Iterator;

/**
 * @author Manfred Geiler (latest modification by $Author: slessard $)
 * @version $Revision: 698799 $ $Date: 2008-09-24 21:03:47 -0500 (Wed, 24 Sep 2008) $
 */
public interface ListEntries {

    public String getValueClass();

    /**
     * @return Iterator over {@link ListEntry} entries
     */
    public Iterator<? extends ListEntry> getListEntries();
}
