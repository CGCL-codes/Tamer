package com.gr.staffpm.pages.management.review.command;

import java.io.Serializable;
import org.apache.wicket.ajax.AjaxRequestTarget;
import com.gr.staffpm.gof.command.IAjaxCommand;

/**
 * @author Graham Rhodes 17 Feb 2011 14:49:14
 */
public class AcceptCommand implements IAjaxCommand, Serializable {

    private static final long serialVersionUID = 1L;

    private final IReviewCommandReciever reviewPage;

    public AcceptCommand(IReviewCommandReciever reviewPage) {
        this.reviewPage = reviewPage;
    }

    @Override
    public void execute(AjaxRequestTarget target) {
        reviewPage.acceptTasks(target);
    }
}
