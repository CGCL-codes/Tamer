package life.windows.util;

import java.util.Arrays;
import java.util.List;
import org.vaadin.navigator7.Navigator;
import test.MailSender;
import life.container.LifeApp;
import life.dao.StaticBean;
import life.domain_model.RegistrationEvent;
import life.domain_model.User;
import life.ui.util.LifeButton;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class RegistrationWindow extends Window {

    VerticalLayout body = new VerticalLayout();

    HorizontalLayout buttonLine = new HorizontalLayout();

    final TextField firstName = new TextField("First Name : ");

    final TextField lastName = new TextField("Last Name : ");

    final TextField email = new TextField("Email adress :");

    final TextField log = new TextField("Nickname :");

    final TextField password = new TextField("Password :");

    final TextField confirmPassword = new TextField("Password confirmation : ");

    List<String> s = Arrays.asList(new String[] { "M.", "F." });

    final OptionGroup sexe = new OptionGroup(null, s);

    Button confirm = new Button("Ok", new ClickListener() {

        @Override
        public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
            if (!password.getValue().toString().equals(confirmPassword.getValue().toString())) {
                body.getWindow().showNotification("password doesnt typed well.");
            } else {
                if (StaticBean.getUserBean().isUserNickNameExist(log.getValue().toString())) {
                    body.getWindow().showNotification("This Nickname already exist.");
                } else {
                    if (!email.isValid()) {
                        body.getWindow().showNotification("Please enter a valid email adress");
                    } else {
                        boolean sex = false;
                        if (sexe.getValue().equals("F.")) {
                            sex = true;
                        }
                        StaticBean.getUserBean().registrationUser(firstName.getValue().toString(), lastName.getValue().toString(), log.getValue().toString(), password.getValue().toString(), email.getValue().toString(), sex);
                        User user = StaticBean.getUserBean().getUser(log.getValue().toString().toLowerCase(), password.getValue().toString());
                        RegistrationEvent e = new RegistrationEvent(user);
                        StaticBean.getEventBean().save(e);
                        MailSender.sendRegistrationMail(user);
                        body.getWindow().getParent().showNotification("Registration well done !");
                        body.getWindow().getParent().removeWindow(buttonLine.getWindow());
                        Navigator nav = LifeApp.getCurrentNavigableAppLevelWindow().getNavigator();
                        nav.reloadCurrentPage();
                    }
                }
            }
        }
    });

    Button cancel = new Button("Cancel", new ClickListener() {

        @Override
        public void buttonClick(com.vaadin.ui.Button.ClickEvent event) {
            body.getWindow().getParent().removeWindow(buttonLine.getWindow());
        }
    });

    public RegistrationWindow() {
        super();
        sexe.select("M.");
        sexe.setNullSelectionAllowed(false);
        sexe.addStyleName("inline");
        this.setCaption("Registration : ");
        this.setModal(true);
        this.setWidth("400px");
        body.setWidth("350px");
        body.setSpacing(true);
        this.addComponent(body);
        buttonLine.addComponent(new LifeButton(confirm));
        buttonLine.addComponent(new LifeButton(cancel));
        log.setRequired(true);
        email.setRequired(true);
        email.addValidator(new EmailValidator("Please enter a valide email."));
        email.setImmediate(true);
        email.setValidationVisible(false);
        password.setSecret(true);
        password.setRequired(true);
        confirmPassword.setSecret(true);
        confirmPassword.setRequired(true);
        body.addComponent(getHorizontalLayoutWith2Texfield(firstName, lastName));
        body.addComponent(getHorizontalLayoutWith2Texfield(sexe, null));
        body.addComponent(getHorizontalLayoutWith2Texfield(log, email));
        body.addComponent(getHorizontalLayoutWith2Texfield(password, confirmPassword));
        body.addComponent(buttonLine);
        body.setComponentAlignment(buttonLine, Alignment.MIDDLE_RIGHT);
    }

    public HorizontalLayout getHorizontalLayoutWith2Texfield(Component left, Component right) {
        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidth("100%");
        hl.setSpacing(true);
        hl.addComponent(left);
        if (right != null) {
            hl.addComponent(right);
            hl.setComponentAlignment(right, Alignment.MIDDLE_RIGHT);
        }
        return hl;
    }
}
