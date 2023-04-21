package org.kenict.repository.admin.page;

import wicket.markup.html.panel.FeedbackPanel;

public class ErrorPage extends BasePage {

    private static final long serialVersionUID = 1L;

    private static final String MARKUP_ID_FEEDBACKPANEL = "feedbackPanel";

    private static final String PAGE_TITLE = "error";

    /**
	 * ErrorPage has a feedbackpanel. Fatal exceptions occurring in the
	 * application should be set as properties on the websession and appear in
	 * this feedbackpanel: this.getSession().error("blabla Exceptie
	 * opgetreden!"); throw new Excepion();
	 */
    public ErrorPage() {
        super(PAGE_TITLE);
        FeedbackPanel feedbackpanel = new FeedbackPanel(MARKUP_ID_FEEDBACKPANEL);
        add(feedbackpanel);
    }
}
