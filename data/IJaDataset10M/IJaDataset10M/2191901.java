package com.gr.staffpm.widget.dialog.task;

import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import com.gr.staffpm.datatypes.Task;
import com.gr.staffpm.datatypes.User;

/**
 * @author Graham Rhodes 12 Apr 2011 13:37:00
 */
public class AjaxAssignWindow extends ModalWindow {

    private static final long serialVersionUID = 1L;

    public AjaxAssignWindow(String id, List<User> team, Task task) {
        super(id);
        setInitialHeight(220);
        setInitialWidth(600);
        setTitle("Please choose new assignee.");
        setContent(new TaskAssignPanel(getContentId(), team, task) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit(AjaxRequestTarget target, String comment, User user) {
                AjaxAssignWindow.this.submit(target, comment, user);
            }

            @Override
            public void onCancel(AjaxRequestTarget target) {
                AjaxAssignWindow.this.close(target);
            }
        });
    }

    protected void submit(AjaxRequestTarget target, String comment, User user) {
    }
}
