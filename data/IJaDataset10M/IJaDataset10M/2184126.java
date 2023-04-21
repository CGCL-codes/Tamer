package portal.presentation.isf.horoscopes;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.Enumeration;
import hambo.app.util.DOMUtil;
import hambo.app.util.Link;
import hambo.util.Device;
import hambo.xpres.XpresApplication;
import portal.presentation.isf.xpres.DateFixer;
import java.util.Vector;
import hambo.xpres.*;
import hambo.app.base.PortalPage;

public class hrpresent extends PortalPage {

    int MAX = 10;

    int MAX_BODY_SIZE = 500;

    String appId = "hr";

    public hrpresent() {
        super("hrpresent");
    }

    public void processPage() {
        if (getContext().getDevice().isHtmlDevice()) {
            doHtmlCompact();
        } else {
            doWML();
        }
    }

    public void setForwardLink(int newFrom, StoryObject so) {
        Element new_news_header = getElement("nextnav");
        String url = so.getUrlWML() + "&fr=" + newFrom;
        DOMUtil.setFirstMatchingAttribute(new_news_header, "href", url);
    }

    public void doHtmlCompact() {
        hrmain.fixNavigation(navigation);
        String node = getParameter("nd");
        NewsHolder newsHolder = NewsHolder.getInstance();
        StoryObject so = newsHolder.getStoryObject(appId, node);
        Element title = getElement("title");
        DOMUtil.setFirstNodeText(title, " " + so.getCaption());
        Element imageElem = getElement("image");
        Vector imgs = so.getImages();
        if (imgs != null && imgs.size() > 0) {
            boolean foundImg = false;
            for (int i = 0; i < imgs.size() && !foundImg; i++) {
                ImageObject imgObj = (ImageObject) imgs.elementAt(i);
                if (imgObj.getSrc() != null && imgObj.getType().equals("icongif")) {
                    String url = XpresApplication.IMG_SERVER + imgObj.getSrc();
                    imageElem.setAttribute("src", url);
                    String caption = imgObj.getCaption();
                    if (caption != null) imageElem.setAttribute("alt", caption);
                    foundImg = true;
                }
            }
            if (!foundImg) {
                logDebug3("No correct image found. Image node removed...(1)");
                DOMUtil.removeNode(imageElem);
            }
        } else {
            logDebug3("No correct image found. Image node removed...(2)");
            DOMUtil.removeNode(imageElem);
        }
        String bodyString = so.getBody(StoryObject.HTML).trim();
        Element headline_body_text = getElement("text");
        DOMUtil.setFirstNodeText(headline_body_text, bodyString);
        Element headline_date_text = getElement("date");
        DOMUtil.setFirstNodeText(headline_date_text, DateFixer.getOkDate(so.getDate(), so.getLanguage(), markup, getContext()));
        Element edit_add = getElement("edit_add");
        MyNews myNews = new MyNews();
        if (so.getBookmark() == null || so.getBookmark().equals("") || user_id == null || myNews.hasBookmark(user_id, so.getBookmark())) {
            DOMUtil.removeNode(edit_add);
        } else {
            Link editBookmarkLink = new Link("xpEditBookmarks");
            editBookmarkLink.addParam("add", so.getBookmark());
            editBookmarkLink.addParam("nd", so.getNodeId());
            String editAddUrl = editBookmarkLink.toString();
            DOMUtil.setFirstMatchingAttribute(edit_add, "href", editAddUrl);
        }
    }

    public void doWML() {
        String node = getParameter("nd");
        int from = 1;
        if (getParameter("fr") != null) try {
            from = Integer.decode(getParameter("fr")).intValue();
        } catch (Exception e) {
        }
        NewsHolder newsHolder = NewsHolder.getInstance();
        StoryObject so = newsHolder.getStoryObject(appId, node);
        Element title = getElement("title");
        DOMUtil.setFirstNodeText(title, " " + so.getCaption());
        Element imageElem = getElement("image");
        Vector imgs = so.getImages();
        if (imgs != null && imgs.size() > 0) {
            boolean foundImg = false;
            for (int i = 0; i < imgs.size() && !foundImg; i++) {
                ImageObject imgObj = (ImageObject) imgs.elementAt(i);
                if (imgObj.getSrc() != null && imgObj.getType().equals("wbmp")) {
                    String url = XpresApplication.IMG_SERVER + imgObj.getSrc();
                    imageElem.setAttribute("src", url);
                    String caption = imgObj.getCaption();
                    if (caption != null) imageElem.setAttribute("alt", caption);
                    foundImg = true;
                }
            }
            if (!foundImg) {
                logDebug3("No correct image found. Image node removed...(1)");
                DOMUtil.removeNode(imageElem);
            }
        } else {
            logDebug3("No correct image found. Image node removed...(2)");
            DOMUtil.removeNode(imageElem);
        }
        String bodyString = so.getBody(StoryObject.WML).trim();
        Element headline_body_text = getElement("text");
        if (headline_body_text == null) logDebug1("TEXTNODE IS NULL!");
        if (bodyString.length() < MAX_BODY_SIZE) {
            DOMUtil.setFirstNodeText(headline_body_text, bodyString);
            DOMUtil.removeNode(getElement("nextnav"));
            DOMUtil.removeNode(getElement("nextnav_br"));
        } else if (bodyString.length() - from < MAX_BODY_SIZE) {
            DOMUtil.setFirstNodeText(headline_body_text, bodyString.substring(from));
            DOMUtil.removeNode(getElement("nextnav"));
            DOMUtil.removeNode(getElement("nextnav_br"));
        } else {
            int newFrom = from + MAX_BODY_SIZE;
            while (bodyString.charAt(newFrom) != ' ') {
                newFrom--;
            }
            setForwardLink(newFrom, so);
            DOMUtil.setFirstNodeText(headline_body_text, bodyString.substring(from, newFrom));
        }
        Element edit_add = getElement("edit_add");
        Element edit_add_br = getElement("edit_add_br");
        MyNews myNews = new MyNews();
        if (so.getBookmark() == null || so.getBookmark().equals("") || user_id == null || myNews.hasBookmark(user_id, so.getBookmark())) {
            DOMUtil.removeNode(edit_add);
            DOMUtil.removeNode(edit_add_br);
        } else {
            Link editBookmarkLink = new Link("xpEditBookmarks", "add=" + so.getBookmark(), null);
            String editAddUrl = editBookmarkLink.toString();
            DOMUtil.setFirstMatchingAttribute(edit_add, "href", editAddUrl);
        }
        if (user_id == null) {
            DOMUtil.removeNode(getElement("my"));
            DOMUtil.removeNode(getElement("my_br"));
        }
    }
}
