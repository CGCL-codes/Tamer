package portal.presentation.bookmarks;

import hambo.util.HTMLUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import hambo.app.util.DOMUtil;
import hambo.app.util.Link;
import hambo.app.base.ProtectedPortalPage;
import hambo.app.base.Navigation;
import java.util.*;
import hambo.mylinks.*;
import java.math.BigDecimal;
import hambo.util.Device;

/**
 * Form to add sites.
 */
public class bositeadd extends ProtectedPortalPage {

    public bositeadd() {
        super("boSiteAdd");
    }

    public void processPage() {
        if (getContext().getDevice().isHtmlDevice()) {
            if (device != Device.HTML_COMPACT) {
                includeMarketMessage(language);
            }
        }
        boolean waponly = getContext().getDevice().isWmlDevice();
        BigDecimal ownerid = new BigDecimal(getContext().getSessionAttributeAsString("oid"));
        BigDecimal icid = (BigDecimal) getContext().getSessionAttribute("bookmarksCid");
        String cid = null;
        if (icid != null) cid = icid.toString();
        if (cid == null || cid.trim().equals("") || cid.equals("0")) {
            cid = "0";
            icid = new BigDecimal(0);
            if (!waponly) {
                Element leadingSep = getElement("leadingSeparator");
                leadingSep.getParentNode().removeChild(leadingSep);
            }
        }
        if (!waponly) {
            Element oldcid = getElement("cid");
            oldcid.setAttribute("value", cid);
            bomain.fixNavigation(navigation, icid);
            BookmarksCategoryRequest dcr = new BookmarksCategoryRequest(ownerid);
            Element path = (Element) getElement("path");
            Stack sPath = dcr.getPath(icid);
            if (sPath != null) {
                Element pathsep = getElement("pathsep");
                while (!sPath.empty()) {
                    BigDecimal Intcid = (BigDecimal) sPath.pop();
                    ObjectCategory theCat = dcr.getCategory(Intcid);
                    Element newpathpart = (Element) path.cloneNode(true);
                    Link linkPath = new Link("bodetail");
                    linkPath.addParam("cid", "" + Intcid);
                    newpathpart.removeAttribute("href");
                    newpathpart.setAttribute("HREF", linkPath.toString());
                    DOMUtil.setFirstNodeText(newpathpart, theCat.getName());
                    Element newpsep = (Element) pathsep.cloneNode(true);
                    path.getParentNode().insertBefore(newpathpart, path);
                    if (!sPath.empty()) path.getParentNode().insertBefore(newpsep, path);
                }
                path.getParentNode().removeChild(path);
                pathsep.getParentNode().removeChild(pathsep);
            }
            String url = getParameter("site_url");
            String url_web = getParameter("site_url_web");
            String name = getParameter("site_name");
            String url_webclipping = getParameter("site_url_webclipping");
            if (url != null) {
                Element site_url = getElement("site_url");
                site_url.setAttribute("value", HTMLUtil.decode(url));
            }
            if (url_web != null) {
                Element site_url_web = getElement("site_url_web");
                site_url_web.setAttribute("value", HTMLUtil.decode(url_web));
            }
            if (url_webclipping != null) {
                Element site_url_webclipping = getElement("site_url_webclipping");
                site_url_webclipping.setAttribute("value", url_webclipping);
            }
            if (name != null) {
                Element site_name = getElement("site_name");
                site_name.setAttribute("value", HTMLUtil.decode(name));
            }
            if (dcr.isWebOnly(icid)) {
                Element waprow = getElement("waprow");
                waprow.getParentNode().removeChild(waprow);
                Element webonly = getElement("webonly");
                webonly.setAttribute("checked", "checked");
            }
        } else {
            Link link = new Link("boLinkAdder");
            link.addParam("cid", cid);
            link.addParam("name=$(name:escape)&#38;wapurl=$(url:escape)&#38;nbback=2");
            if (!cid.equals("0")) link.addParam("pageid", "bodetail"); else link.addParam("pageid", "boLinks");
            Element savelink = getElement("savelink");
            savelink.setAttribute("href", link.toString());
            Link back = getHistoryLink();
            back.addParam("cid", cid);
            Element cancel = getElement("cancel");
            cancel.setAttribute("href", back.toString());
        }
    }

    /**
     * Draws a virtual page containing the marketing message.
     */
    protected void includeMarketMessage(String lang) {
        if (lang.equals("English") || lang.equals("Swedish") || lang.equals("English_us")) {
            mmsg_bodetail mm = new mmsg_bodetail(comms);
            helpComponent = mm;
        } else {
        }
    }
}
