package wicketrocks.feedback;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import entity.contact.Contact;

/**
 * @author manuelbarzi
 * @version 20120103142027 
 */
public class PopupFeedbackPanel extends Panel {

    public PopupFeedbackPanel(String id) {
        super(id);
        final wicket.feedback.PopupFeedbackPanel feedback = new wicket.feedback.PopupFeedbackPanel("feedback");
        add(feedback);
        ContainerFeedbackMessageFilter filter = new ContainerFeedbackMessageFilter(this);
        feedback.setFilter(filter);
        final Contact contact = new Contact();
        Form<Contact> form = new Form<Contact>("form") {

            @Override
            protected void onSubmit() {
                super.onSubmit();
                if (!"peter".equals(contact.getName())) {
                    error("you are not peter");
                } else {
                    error("hi peter!");
                }
                feedback.refresh();
            }

            @Override
            protected void onError() {
                super.onError();
                feedback.refresh();
            }
        };
        add(form);
        TextField<String> name = new TextField<String>("name", new PropertyModel<String>(contact, "name"));
        name.setRequired(true);
        form.add(name);
        final Contact contact2 = new Contact();
        Form<Contact> ajaxForm = new Form<Contact>("ajaxForm");
        add(ajaxForm);
        AjaxButton ajaxSubmit = new AjaxButton("ajaxSubmit") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit();
                if (!"peter".equals(contact2.getName())) {
                    error("you are not peter");
                } else {
                    error("hi peter!");
                }
                feedback.refresh(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
                feedback.refresh(target);
            }
        };
        ajaxForm.add(ajaxSubmit);
        TextField<String> name2 = new TextField<String>("name", new PropertyModel<String>(contact2, "name"));
        name2.setRequired(true);
        ajaxForm.add(name2);
    }
}
