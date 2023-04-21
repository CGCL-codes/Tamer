package action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import app.App;
import archive.Archive;

public class ChangeUserAction extends AbstractAction {

    private String newUser;

    public ChangeUserAction(String newUser) {
        this.newUser = newUser;
        putValue(Action.NAME, newUser);
        putValue(Action.ACTION_COMMAND_KEY, newUser);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        doAction();
    }

    public void doAction() {
        App app = App.getInstance();
        if (newUser == null || newUser.equals("") || newUser.equals(app.getCurrentUser())) {
            return;
        }
        System.out.println("ChangeUserAction:performed:cmd:" + newUser);
        Archive newArchive = new Archive(newUser);
        newArchive.read();
        newArchive.calendarUpdate();
        app.setArchive(newArchive);
    }
}
