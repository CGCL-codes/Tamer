package be.vds.jtb.taskmanager.view;

import java.util.List;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class TaskManagerToolBar extends JToolBar {

    public TaskManagerToolBar() {
        super();
        init();
    }

    private void init() {
    }

    public void addActions(List<Action> actions) {
        for (Action action : actions) {
            JButton b = new JButton(action);
            this.add(b);
        }
        this.validate();
        this.repaint();
    }
}
