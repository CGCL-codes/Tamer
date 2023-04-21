package name.emu.webapp.kos.wicket.pages.admin;

import name.emu.webapp.kos.service.data.UserWithoutPassword;
import name.emu.webapp.kos.service.trusted.AdminService;
import name.emu.webapp.kos.wicket.Application;
import name.emu.webapp.kos.wicket.components.CancelButton;
import name.emu.webapp.kos.wicket.model.UserWithoutPasswordModel;
import name.emu.webapp.kos.wicket.pages.BasePage;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

@AuthorizeInstantiation("ADMIN")
public class EditKosUserPage extends BasePage {

    public static final String KOS_USER_PARAM = "id";

    protected IModel<UserWithoutPassword> userDataModel;

    public EditKosUserPage(PageParameters params) {
        super();
        Long id = params.get(KOS_USER_PARAM).toLongObject();
        Form<UserWithoutPassword> form;
        Button button;
        TextField<String> textField;
        DateTextField birthdayField;
        CheckBox checkbox;
        setNavigationVisible(false);
        if (id == null) {
            throw new WicketRuntimeException();
        } else {
            userDataModel = new UserWithoutPasswordModel(id);
        }
        form = new Form<UserWithoutPassword>("form", new CompoundPropertyModel<UserWithoutPassword>(userDataModel));
        textField = new TextField<String>("userName");
        form.add(textField);
        textField = new TextField<String>("familyName");
        form.add(textField);
        textField = new TextField<String>("givenName");
        form.add(textField);
        birthdayField = new DateTextField("birthday");
        form.add(birthdayField);
        checkbox = new CheckBox("admin");
        form.add(checkbox);
        checkbox = new CheckBox("locked");
        form.add(checkbox);
        checkbox = new CheckBox("forcePwChange");
        form.add(checkbox);
        button = new SaveButton("saveBtn");
        button.setDefaultModel(new Model<String>("Save"));
        form.add(button);
        button = new DeleteButton("deleteBtn");
        button.setDefaultModel(new Model<String>("Delete"));
        form.add(button);
        form.add(new CancelButton("cancelBtn", ListKosUserPage.class));
        add(form);
    }

    protected class SaveButton extends Button {

        public SaveButton(String id) {
            super(id);
        }

        @Override
        public void onSubmit() {
            UserWithoutPassword userData = userDataModel.getObject();
            Application app = Application.get();
            AdminService adminService = app.getServiceRegistry().getAdminService();
            adminService.updateUserData(userData);
            setResponsePage(Application.get().getHomePage());
        }
    }

    protected class DeleteButton extends Button {

        public DeleteButton(String id) {
            super(id);
        }

        @Override
        public void onSubmit() {
            UserWithoutPassword userData = userDataModel.getObject();
            Application app = Application.get();
            AdminService adminService = app.getServiceRegistry().getAdminService();
            adminService.deleteUser(userData);
            setResponsePage(Application.get().getHomePage());
        }
    }
}
