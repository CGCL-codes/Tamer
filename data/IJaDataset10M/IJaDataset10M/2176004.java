package portal.presentation.wapdir;

import hambo.app.base.PortalPage;
import hambo.mobiledirectory.*;
import hambo.mobiledirectory.*;
import java.io.IOException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import hambo.app.util.Link;
import hambo.app.util.DOMUtil;

public class wdsearch extends PortalPage {

    /**
     * Category id. To get back where we were.
     */
    public int id;

    public wdsearch() {
        super("wdsearch", false);
    }

    private void load() {
        getContext().removeSessionAttribute("wapdirSearchResult");
        try {
            id = Integer.parseInt((String) getParameter("id"));
        } catch (Throwable e) {
            id = 1;
        }
    }

    public void processPage() {
        load();
        fixLinks();
    }

    private void fixLinks() {
        Element search = getElement("search");
        Element back = getElement("back");
        Link linksearch = new Link("wdsearchresult");
        linksearch.addParam("searchbox=$(searchbox:escape)");
        DOMUtil.setAttribute(search, "href", linksearch.toString());
        Link linkback = new Link("wdoptions");
        linkback.addParam("id", "" + id);
        DOMUtil.setAttribute(back, "href", linkback.toString());
    }
}
