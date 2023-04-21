package com.gr.staffpm.widget.dialog.feedback;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.rating.RatingPanel;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import com.gr.staffpm.datatypes.Task;
import com.gr.staffpm.model.StringModel;
import com.gr.staffpm.widget.dialog.feedback.model.RatingModel;

/**
 * @author Graham Rhodes 17 Feb 2011 15:22:31
 */
public abstract class TaskFeedbackPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public static final ResourceReference STAR_EMPTY = new ResourceReference(AjaxFeedbackDialog.class, "StarEmpty.gif");

    public static final ResourceReference STAR_FULL = new ResourceReference(AjaxFeedbackDialog.class, "StarFull.gif");

    private static RatingModel rating = new RatingModel();

    private TextArea<String> commentBox;

    private RatingPanel ratingPanel;

    private final FeedbackPanel feedbackPanel;

    private MultiLineLabel title;

    private Task globalTask;

    public TaskFeedbackPanel(String id, Task task) {
        super(id);
        this.globalTask = task;
        title = new MultiLineLabel("title", "Please leave feedback for " + task.getAssignee().getFullName() + " and task: " + task.getName() + ".");
        title.setOutputMarkupId(true);
        add(title);
        ratingPanel = new RatingPanel("rating", new PropertyModel<Integer>(rating, "rating"), 5, true) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean onIsStarActive(int star) {
                return TaskFeedbackPanel.rating.isActive(star);
            }

            @Override
            public void onRated(int rating, AjaxRequestTarget target) {
                TaskFeedbackPanel.rating.setRating(rating);
            }

            @Override
            protected String getActiveStarUrl(int iteration) {
                return getRequestCycle().urlFor(STAR_FULL).toString();
            }

            @Override
            protected String getInactiveStarUrl(int iteration) {
                return getRequestCycle().urlFor(STAR_EMPTY).toString();
            }
        };
        ratingPanel.setRenderBodyOnly(false);
        ratingPanel.setOutputMarkupId(true);
        add(ratingPanel);
        Form<Void> form = new Form<Void>("form");
        feedbackPanel = new FeedbackPanel("feedback", new ContainerFeedbackMessageFilter(this));
        feedbackPanel.setOutputMarkupId(true);
        form.add(feedbackPanel);
        commentBox = new TextArea<String>("commentBox", new Model<String>());
        commentBox.setEscapeModelStrings(true);
        commentBox.setOutputMarkupId(true);
        form.add(commentBox);
        form.add(new AjaxButton("submitButton") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (!commentBox.getValue().isEmpty()) {
                    rating.setComment(commentBox.getValue());
                    TaskFeedbackPanel.this.onSubmit(target, rating, globalTask);
                } else error("Please leave a comment about about this feedback.");
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.addComponent(feedbackPanel);
            }

            ;
        });
        form.add(new AjaxButton("cancelButton") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                TaskFeedbackPanel.this.onCancel(target);
            }
        });
        add(form);
    }

    public abstract void onSubmit(AjaxRequestTarget target, RatingModel rating, Task task);

    public abstract void onCancel(AjaxRequestTarget target);

    public void update(AjaxRequestTarget target, Task task) {
        globalTask = task;
        title.setDefaultModel(new StringModel("Please leave feedback for " + task.getAssignee().getFullName() + " and task: " + task.getName() + "."));
        rating.reset();
        commentBox.setModelValue(new String[] { "" });
        target.addComponent(title);
        target.addComponent(ratingPanel);
        target.addComponent(commentBox);
    }
}
