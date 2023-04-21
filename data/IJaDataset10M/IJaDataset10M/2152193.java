package eu.roelbouwman.adminpanels;

import eu.roelbouwman.Index;
import eu.roelbouwman.PeugeotPanel;
import eu.roelbouwman.peugeotLib.model.User;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author roel
 */
public final class AddUserPanel extends PeugeotPanel {

    private Logger log = LoggerFactory.getLogger(AddUserPanel.class);

    private Form form = new Form("userform");

    private CompoundPropertyModel cModel = new CompoundPropertyModel(new User());

    private FeedbackPanel feedback = new FeedbackPanel("feedback");

    public AddUserPanel(String id) {
        super(id);
        form.setModel(cModel);
        form.add(feedback);
        form.add(new TextField("name").setRequired(true));
        PasswordTextField password = new PasswordTextField("password");
        password.setResetPassword(false);
        form.add(password);
        PasswordTextField controlPassword = new PasswordTextField("controlPassword");
        controlPassword.setModel(password.getModel());
        controlPassword.setResetPassword(false);
        form.add(controlPassword);
        form.add(new EqualPasswordInputValidator(password, controlPassword));
        form.add(new Button("ok") {

            @Override
            public void onSubmit() {
                storeInDB();
                cModel.setObject(new User());
            }
        });
        add(form);
    }

    private void storeInDB() {
        User u = (User) form.getModelObject();
        if (u.getName().equals("root")) {
            u.setAdmin(true);
        }
        log.debug("try to add user");
        if (getPeugeotService().getUser(u.getName()) != null) {
            log.debug("did not add user");
            error("user exists!");
        } else {
            getPeugeotService().set(u);
            log.info("added user" + u.getName());
            setResponsePage(new Index(new AdminLoginPanel("panel")));
        }
    }
}
