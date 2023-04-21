package net.sourceforge.pebble.web.view.impl;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.web.view.HtmlView;

/**
 * Represents a page containing a single static page.
 *
 * @author    Simon Brown
 */
public class StaticPageView extends HtmlView {

    public void prepare() {
        ContentDecoratorContext context = new ContentDecoratorContext();
        context.setView(ContentDecoratorContext.DETAIL_VIEW);
        context.setMedia(ContentDecoratorContext.HTML_PAGE);
        StaticPage staticPage = (StaticPage) getModel().get(Constants.STATIC_PAGE_KEY);
        staticPage.getBlog().getContentDecoratorChain().decorate(context, staticPage);
    }

    /**
   * Gets the title of this view.
   *
   * @return the title as a String
   */
    public String getTitle() {
        StaticPage staticPage = (StaticPage) getModel().get(Constants.STATIC_PAGE_KEY);
        return staticPage.getTitle();
    }

    /**
   * Gets the URI that this view represents.
   *
   * @return the URI as a String
   */
    public String getUri() {
        return "/WEB-INF/jsp/staticPage.jsp";
    }
}
