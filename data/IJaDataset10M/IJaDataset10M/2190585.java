package eu.roelbouwman.webproject.forms;

import static eu.roelbouwman.webproject.Application.getApp;
import java.util.Observable;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Label;
import eu.roelbouwman.menu.Controller;
import eu.roelbouwman.webproject.Application;

public class HelloForm extends DefaultMainContent {

    private Label user = new Label("empty");

    private Controller controller = getApp().getController();

    public HelloForm() {
        super();
        Application.getApp().getUser().addObserver(this);
        initComponents();
        user.setText(Application.getApp().getUser().getUser().getName().getFirstName());
    }

    @Override
    protected void fillView() {
        super.fillView();
    }

    @Override
    public void update(Observable o, Object arg) {
        super.update(o, arg);
        if (Application.getApp().getUser().getUser() != null) {
            user.setText(Application.getApp().getUser().getUser().getName().getFirstName());
        }
    }

    @Override
    protected void validateFields() {
        super.validateFields();
    }

    private void initComponents() {
        Column c = new Column();
        c.setInsets(new Insets(10, 10, 0, 0));
        c.add(new Label("Hello World!"));
        c.add(makeStrut(10, 0));
        c.add(user);
        add(c);
    }
}
