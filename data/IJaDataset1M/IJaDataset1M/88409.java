package xbrowser.bookmark.io;

import java.text.*;
import xbrowser.bookmark.XBookmarkFolder;

public interface XBookmarkSerializer {

    public void importFrom(String file_name, XBookmarkFolder root_folder) throws Exception;

    public void exportTo(String file_name, XBookmarkFolder root_folder) throws Exception;

    public DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);
}
