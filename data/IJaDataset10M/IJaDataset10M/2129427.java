package no.tstsolutions.tvedere.tvedereapp.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

/**
 * Should be the parent of all pages, specifically the DefaultPage, extend from DefaultPage, not this.
 * Sets up stuff that should be present in them all...
 *
 * @author asm
 */
public class BasePage extends WebPage {

    private static final long serialVersionUID = 2L;

    public BasePage() {
        Label pageTitle = new Label("page.title", new StringResourceModel("page.title", this, new Model(this), "page.title"));
        pageTitle.setRenderBodyOnly(true);
        add(pageTitle);
    }
}
