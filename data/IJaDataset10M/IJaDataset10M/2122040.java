package name.emu.webapp.kos.wicket.pages.account;

import java.security.KeyPair;
import name.emu.webapp.kos.domain.KosUser;
import name.emu.webapp.kos.service.data.AccountWithPassword;
import name.emu.webapp.kos.wicket.Application;
import name.emu.webapp.kos.wicket.components.CancelButton;
import name.emu.webapp.kos.wicket.pages.BasePage;
import name.emu.webapp.kos.wicket.session.WebSession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

@AuthorizeInstantiation("USER")
public class CreateSecretAccountPage extends BasePage {

    protected IModel<SecretAccountData> secretAccountDataModel;

    public CreateSecretAccountPage() {
        Form<SecretAccountData> secretAccountForm;
        secretAccountDataModel = new CompoundPropertyModel<SecretAccountData>(new SecretAccountData());
        setNavigationVisible(false);
        secretAccountForm = new Form<SecretAccountData>("secretAccountForm");
        secretAccountForm.setDefaultModel(secretAccountDataModel);
        secretAccountForm.add(new TextField<String>("system"));
        secretAccountForm.add(new TextField<String>("userName"));
        secretAccountForm.add(new PasswordTextField("password1"));
        secretAccountForm.add(new PasswordTextField("password2"));
        secretAccountForm.add(new TextArea<String>("comment"));
        secretAccountForm.add(new SaveButton("saveBtn"));
        secretAccountForm.add(new CancelButton("cancelBtn", ListSecretAccountPage.class));
        add(secretAccountForm);
    }

    protected class SaveButton extends Button {

        public SaveButton(String id) {
            super(id);
        }

        @Override
        public void onSubmit() {
            SecretAccountData secretAccountData = secretAccountDataModel.getObject();
            if (!secretAccountData.getPassword1().equals(secretAccountData.getPassword2())) {
                CreateSecretAccountPage.this.error("Passwords differ");
            } else {
                AccountWithPassword account = new AccountWithPassword();
                Application app = Application.get();
                WebSession session = WebSession.get();
                KeyPair keyPair;
                KosUser user;
                account.setSystem(secretAccountData.getSystem());
                account.setUserName(secretAccountData.getUserName());
                account.setPassword(secretAccountData.getPassword1());
                account.setComment(secretAccountData.getComment());
                keyPair = session.getKeyPair();
                user = session.getUser();
                app.getServiceRegistry().getEncryptionService().storeNewSecretAccount(account, user, keyPair.getPublic());
                setResponsePage(Application.get().getHomePage());
            }
        }
    }
}
