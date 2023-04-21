package org.hip.vif.core.bom.impl;

import org.hip.kernel.bom.impl.DomainObjectImpl;
import org.hip.vif.core.bom.Bookmark;

/**
 * The model of a Bookmark.
 * 
 * @author Benno Luthiger
 * @see org.hip.vif.core.bom.Bookmark
 * Created on Feb 25, 2004
 */
public class BookmarkImpl extends DomainObjectImpl implements Bookmark {

    public static final String HOME_CLASS_NAME = "org.hip.vif.core.bom.impl.BookmarkHomeImpl";

    /**
	 * BookmarkImpl constructor.
	 */
    public BookmarkImpl() {
        super();
    }

    /**
	 * This Method returns the class name of the home.
	 *
	 * @return java.lang.String
	 */
    public String getHomeClassName() {
        return HOME_CLASS_NAME;
    }
}
