package net.wimpi.pim.bookmarks.io.xbel;

import net.wimpi.pim.bookmarks.io.BookmarksMarshaller;
import net.wimpi.pim.bookmarks.model.*;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.TimeZone;

/**
 * Class implementing a <tt>BookmarksMarshaller</tt>
 * for the XBEL format.
 *
 * @author Dieter Wimberger
 * @version @version@ (@date@)
 */
public class XBELMarshaller implements BookmarksMarshaller {

    private static SimpleDateFormat c_DateFormat;

    private String m_Encoding = "utf-8";

    private boolean m_Document = false;

    static {
        c_DateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        c_DateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public XBELMarshaller() {
    }

    public XBELMarshaller(String encoding) {
        m_Encoding = encoding;
    }

    public void setEncoding(String enc) {
        m_Encoding = enc;
    }

    public void setGroupAsDocument(boolean b) {
        m_Document = b;
    }

    public boolean isGroupAsDocument() {
        return m_Document;
    }

    public void marshallBookmarkItemGroup(OutputStream out, BookmarkItemGroup group) {
        PrintWriter pw = new PrintWriter(out);
        if (m_Document) {
            pw.print("<?xml version=\"1.0\" encoding=\"");
            pw.print(m_Encoding);
            pw.println("\"?>");
            pw.println(XBEL_IDENTIFIER);
            pw.print("<xbel version=\"1.0\" id=\"");
            pw.print(group.getUID());
            pw.print("\" added=\"");
            pw.print(c_DateFormat.format(group.getCreationDate()));
            pw.println("\">");
            recurseGroup(group, pw, 1);
            pw.println(END_XBEL_ROOT);
        } else {
            recurseGroup(group, pw, 2);
        }
        pw.flush();
    }

    private static void recurseGroup(BookmarkItemGroup gr, PrintWriter pw, int level) {
        if (level > 1) {
            pw.print("<folder");
            printNodeAttributes(gr, pw);
            pw.print(" folded=\"");
            pw.print(((gr.isFolded()) ? "yes" : "no"));
            pw.println("\">");
        }
        printItemElements(gr, pw);
        for (Iterator iterator = gr.iterator(); iterator.hasNext(); ) {
            BookmarkItem bit = (BookmarkItem) iterator.next();
            if (bit instanceof Bookmark) {
                Bookmark bm = (Bookmark) bit;
                pw.print("<bookmark");
                printNodeAttributes(bm, pw);
                printURLAttributes(bm, pw);
                pw.println(">");
                printItemElements(bm, pw);
                pw.println(END_BOOKMARK_TAG);
            } else if (bit instanceof Separator) {
                pw.println(SEPARATOR_TAG);
            } else if (bit instanceof BookmarkItemGroup) {
                BookmarkItemGroup sgr = (BookmarkItemGroup) bit;
                recurseGroup(sgr, pw, (level + 1));
            }
        }
        if (level > 1) {
            pw.println(END_FOLDER_TAG);
        }
    }

    public void marshallBookmark(OutputStream out, Bookmark bookmark) {
        PrintWriter pw = new PrintWriter(out);
        pw.print("<bookmark");
        printNodeAttributes(bookmark, pw);
        printURLAttributes(bookmark, pw);
        pw.println(">");
        printItemElements(bookmark, pw);
        pw.println(END_BOOKMARK_TAG);
        pw.flush();
    }

    private static final void printNodeAttributes(BookmarkItem item, PrintWriter pw) {
        pw.print(" id=\"");
        pw.print(item.getUID());
        pw.print("\" added=\"");
        pw.print(c_DateFormat.format(item.getCreationDate()));
        pw.print("\" ");
    }

    private static final void printURLAttributes(Bookmark bm, PrintWriter pw) {
        pw.print(" href=\"");
        pw.print(bm.getURI());
        pw.print("\" visited=\"");
        pw.print(c_DateFormat.format(bm.getLastVisitDate()));
        pw.print("\" modified=\"");
        pw.print(c_DateFormat.format(bm.getModificationDate()));
        pw.print("\" ");
    }

    private static final void printInfo(Iterator iter, PrintWriter pw) {
        if (iter.hasNext()) {
            pw.println(INFO_TAG);
            for (; iter.hasNext(); ) {
                Metadata md = (Metadata) iter.next();
                pw.print("<metadata owner=\"");
                pw.print(md.getOwner());
                pw.println("\"/>");
            }
            pw.println(END_INFO_TAG);
        }
    }

    private static final void printItemElements(BookmarkItem item, PrintWriter pw) {
        String str = item.getTitle();
        if (isValidString(str)) {
            pw.print(TITLE_TAG);
            pw.print(str);
            pw.println(END_TITLE_TAG);
        }
        printInfo(item.infoIterator(), pw);
        str = item.getDescription();
        if (isValidString(str)) {
            pw.print(DESC_TAG);
            pw.print(str);
            pw.println(END_DESC_TAG);
        }
    }

    private static boolean isValidString(String str) {
        return (str != null && str.length() > 0);
    }

    private static final String XBEL_IDENTIFIER = "<!DOCTYPE xbel PUBLIC \n" + "    \"+//IDN python.org//DTD XML Bookmark Exchange Language 1.0//EN//XML\" \n" + "    \"http://pyxml.sourceforge.net/topics/dtds/xbel-1.0.dtd\">";

    private static final String END_XBEL_ROOT = "</xbel>";

    private static final String INFO_TAG = "<info>";

    private static final String END_INFO_TAG = "</info>";

    private static final String TITLE_TAG = "<title>";

    private static final String END_TITLE_TAG = "</title>";

    private static final String DESC_TAG = "<desc>";

    private static final String END_DESC_TAG = "</desc>";

    private static final String END_BOOKMARK_TAG = "</bookmark>";

    private static final String SEPARATOR_TAG = "<separator/>";

    private static final String END_FOLDER_TAG = "</folder>";
}
