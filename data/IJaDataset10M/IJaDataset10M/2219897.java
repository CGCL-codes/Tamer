package pl.agh.mgr.ssm.database;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import pl.agh.mgr.ssm.beans.User;

public class NewUserForm extends Form {

    private User u;

    public NewUserForm(String id, User u) {
        super(id, new CompoundPropertyModel(u));
        this.u = u;
        add(new TextField("nick"));
        add(new TextField("name"));
        add(new TextField("surname"));
        add(new TextField("phone"));
        add(new TextField("email"));
        add(new TextField("role"));
        add(new TextField("gg"));
        add(new TextField("skype"));
    }

    public User getNewUser() {
        return u;
    }
}
