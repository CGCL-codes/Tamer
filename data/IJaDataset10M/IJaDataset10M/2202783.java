package de.herberlin.webapp.text.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import de.herberlin.webapp.core.AbstractAction;
import de.herberlin.webapp.core.AppException;
import de.herberlin.webapp.db.Service;
import de.herberlin.webapp.text.Page;

/**
 * StoreTextAction.java
 * 
 * Created on 26. Oktober 2007, 23:06
 * 
 * 
 * @author herberlin
 */
public class StoreTextAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    private String pageId = null;

    private String headline = null;

    private String text = null;

    /**
	 * Creates a new instance of StoreTextAction
	 */
    public StoreTextAction() {
    }

    @Validate(required = true)
    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    @DefaultHandler
    public Resolution perform() {
        logger.debug("Headline=" + headline);
        logger.debug("Text=" + text);
        logger.debug("PageId=" + pageId);
        Page page = Service.getItem(Page.class, pageId);
        if (page == null) {
            throw new AppException("Page not found for PageId:" + pageId);
        }
        page.setHeadline(headline);
        if (text != null) {
            text = text.replaceAll("'", "&#39;");
            text = text.replaceAll("ß", "&szlig;");
            text = text.replaceAll("\r", "");
            text = text.replaceAll("\n", "");
        }
        page.setText(text);
        Service.persist(page);
        logger.debug("Page: " + page + " successfully stored.");
        return new RedirectResolution(getUrl(page));
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setText(String text) {
        this.text = text;
    }
}
