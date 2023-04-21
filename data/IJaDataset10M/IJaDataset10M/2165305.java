package portal.presentation.wapdir;

import hambo.app.base.PortalPage;
import hambo.mobiledirectory.*;
import hambo.mobiledirectory.*;
import java.io.IOException;
import java.util.Enumeration;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import hambo.app.util.Link;
import hambo.app.util.DOMUtil;
import hambo.util.XMLUtil;
import hambo.mobiledirectory.DirectoryTopRequest;
import hambo.mobiledirectory.DirectorySiteRequest;
import hambo.mobiledirectory.ObjectSite;

/**
 * This class will manage the wap directory first page, it is more or less
 * a link collection.
 */
public class wdbest extends PortalPage {

    /**
     * Category id
     */
    public int id;

    /**
     * The Enumeration of 5 Top sites' ids
     */
    private Enumeration top5;

    /**
     * DirectoryTopRequest object
     */
    private DirectoryTopRequest dtr;

    private DirectorySiteRequest dsr;

    private Element besttemplate;

    public wdbest() {
        super("wdbest", false);
    }

    private void load(String loc) {
        dsr = new DirectorySiteRequest();
        dtr = new DirectoryTopRequest();
        try {
            id = Integer.parseInt((String) getParameter("id"));
        } catch (Throwable e) {
            id = 1;
        }
        dtr.setLocation(loc);
        dsr.setLocation(loc);
        top5 = dtr.getTop5(id).elements();
    }

    public void processPage() {
        String loc_string = getContext().getSessionAttributeAsString("pref_location");
        if (loc_string == null) loc_string = "1";
        load(loc_string);
        besttemplate = getElement("besttemplate");
        while (top5.hasMoreElements()) appendTop();
        removeTemplate();
        Element back = getElement("back");
        Link link = new Link("wdmain");
        link.addParam("id", "" + id);
        back.setAttribute("href", link.toString());
    }

    private void appendTop() {
        ObjectSite os = dsr.getSite(((Integer) top5.nextElement()).intValue());
        Element newbest = (Element) besttemplate.cloneNode(true);
        newbest.removeAttribute("id");
        Element bestlink = getElement(newbest, "bestlink");
        DOMUtil.setFirstNodeText(bestlink, XMLUtil.decode(os.getName()) + " (" + os.getRating() + ")");
        Link link = new Link("wddetail");
        link.addParam("id", "" + id);
        link.addParam("sid", "" + os.getId());
        bestlink.setAttribute("href", link.toString());
        besttemplate.getParentNode().insertBefore(newbest, besttemplate);
    }

    private void removeTemplate() {
        besttemplate.getParentNode().removeChild(besttemplate);
    }
}
