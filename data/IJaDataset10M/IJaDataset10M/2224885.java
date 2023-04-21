package org.londonwicket.gallery;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public abstract class TemplatePage extends WebPage {

    private FeedbackPanel feedbackPanel;

    public TemplatePage() {
        add(HeaderContributor.forCss(TemplatePage.class, "style.css"));
        feedbackPanel = new FeedbackPanel("feedback");
        add(feedbackPanel);
        feedbackPanel.setOutputMarkupId(true);
    }

    protected GalleryService getGalleryService() {
        return WicketApplication.get().getGalleryService();
    }

    protected FeedbackPanel getFeedbackPanel() {
        return feedbackPanel;
    }
}
