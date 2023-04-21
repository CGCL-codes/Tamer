package net.sf.shineframework.demoapp.pages;

import wicket.markup.html.panel.Panel;
import net.sf.shineframework.web.wicket.pages.AbstractPage;
import net.sf.shineframework.web.wicket.widgets.PlaceHolderPanel;

public class DefaultApplicationLayoutPage extends AbstractPage {

    private static final long serialVersionUID = 1540196279324083210L;

    public DefaultApplicationLayoutPage() {
        add(getHeaderPanel());
        add(getFooterPanel());
        add(getMenuPanel());
    }

    protected Panel getHeaderPanel() {
        return new PlaceHolderPanel("header");
    }

    protected Panel getFooterPanel() {
        return new PlaceHolderPanel("footer");
    }

    protected Panel getMenuPanel() {
        return new PlaceHolderPanel("menu");
    }
}
