package uk.ac.bris.portlet.bookmarks.database;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import uk.ac.bris.portlet.bookmarks.xbel.model.Xbel;

public class BookmarksFacade {

    private static BookmarksDAO bookmarksDAO;

    public void setBookmarksDAO(BookmarksDAO bookmarksDAO) {
        this.bookmarksDAO = bookmarksDAO;
    }

    public static void writeBookmarks(String user, Xbel xbel) {
        BookmarksModel bkModel = new BookmarksModel(user, xbel);
        bookmarksDAO.writeBookmarks(bkModel);
    }

    public static void writeBookmarks(String user, String xbelString) {
        Xbel xbel = new Xbel();
        StringReader sr = new StringReader(xbelString);
        try {
            xbel = (Xbel) xbel.unmarshal(sr);
            writeBookmarks(user, xbel);
        } catch (MarshalException ex) {
            ex.printStackTrace();
        } catch (ValidationException ex) {
            ex.printStackTrace();
        }
    }

    public static Xbel getBookmarksXbel(String user) {
        if (user != null) {
            List l = bookmarksDAO.getBookmarksEntries(user);
            if (l.size() > 0) {
                BookmarksModel bkModel = (BookmarksModel) l.get(0);
                return bkModel.getXbel();
            } else {
                if (!user.equals("default")) {
                    return getBookmarksXbel("default");
                }
            }
        } else {
            return getBookmarksXbel("default");
        }
        return null;
    }

    public static String getBookmarksXbelString(String user) {
        Xbel xbel = getBookmarksXbel(user);
        StringWriter xml = new StringWriter();
        try {
            xbel.marshal(xml);
        } catch (MarshalException ex) {
            ex.printStackTrace();
        } catch (ValidationException ex) {
            ex.printStackTrace();
        }
        return xml.toString();
    }
}
