package org.dbwiki.web.ui.printer.page;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.dbwiki.exception.WikiException;
import org.dbwiki.user.User;
import org.dbwiki.web.html.HtmlLinePrinter;
import org.dbwiki.web.request.WikiPageRequest;
import org.dbwiki.data.wiki.DatabaseWikiPage;
import org.dbwiki.web.ui.CSS;
import org.dbwiki.web.ui.printer.HtmlContentPrinter;

/** Prints history of a wiki page
 * 
 * @author jcheney
 *
 */
public class PageHistoryPrinter implements HtmlContentPrinter {

    private WikiPageRequest<?> _request;

    public PageHistoryPrinter(WikiPageRequest<?> request) {
        _request = request;
    }

    @Override
    public void print(HtmlLinePrinter body) throws WikiException {
        body.paragraph("Page History", CSS.CSSHeadline);
        printPageVersions(_request, body);
    }

    private void printPageVersions(WikiPageRequest<?> request, HtmlLinePrinter body) {
        List<DatabaseWikiPage> versions = null;
        try {
            versions = request.versions();
        } catch (WikiException e) {
            e.printStackTrace();
        }
        body.openTABLE(CSS.CSSObjectFrame);
        body.openTR();
        body.openTD(CSS.CSSObjectListing);
        body.openTABLE(CSS.CSSList);
        body.openTR();
        body.openTH(CSS.CSSList);
        body.add("Version ID");
        body.closeTH();
        body.openTH(CSS.CSSList);
        body.add("Timestamp");
        body.closeTH();
        body.openTH(CSS.CSSList);
        body.add("User");
        body.closeTH();
        body.closeTR();
        String baseURL = _request.wri().getURL();
        assert (versions != null);
        for (DatabaseWikiPage p : versions) {
            Date d = new Date(p.getTimestamp());
            String dateString = new SimpleDateFormat("d MMM yyyy HH:mm:ss").format(d);
            String username = User.UnknownUserName;
            if (p.getUser() != null) {
                username = p.getUser().fullName();
            }
            body.openTR();
            body.openTD(CSS.CSSList);
            body.link(baseURL + "?version=" + Long.toString(p.getTimestamp()), Integer.toString(p.getID()), CSS.CSSList);
            body.closeTD();
            body.openTD(CSS.CSSList);
            body.add(dateString);
            body.closeTD();
            body.openTD(CSS.CSSList);
            body.add(username);
            body.closeTD();
            body.closeTR();
        }
        body.closeTABLE();
        body.closeTD();
        body.closeTR();
        body.closeTABLE();
    }
}
