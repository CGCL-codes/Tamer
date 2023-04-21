package net.fckeditor.requestcycle.impl;

import javax.servlet.http.HttpServletRequest;
import net.fckeditor.requestcycle.UserAction;

/**
 * Standard implementation for {@link UserAction}. It always returns
 * <code>true</code>.<br/>
 * 
 * @see EnabledUserAction
 * @version $Id: UserActionImpl.java 4785 2009-12-21 20:10:28Z mosipov $
 * @deprecated Class will be removed in FCKeditor.Java 2.6, functionality now
 *             served by {@link EnabledUserAction}.
 */
@Deprecated
public class UserActionImpl implements UserAction {

    public boolean isEnabledForFileBrowsing(final HttpServletRequest request) {
        return true;
    }

    public boolean isEnabledForFileUpload(final HttpServletRequest request) {
        return true;
    }

    public boolean isCreateFolderEnabled(final HttpServletRequest request) {
        return true;
    }
}
