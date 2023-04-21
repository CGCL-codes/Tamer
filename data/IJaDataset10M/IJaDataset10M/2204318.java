package lablog.util.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import lablog.app.Application;

@SuppressWarnings("serial")
public abstract class AbstractDeleteAction extends LablogActionWithTest {

    public AbstractDeleteAction() {
        this(null);
    }

    public AbstractDeleteAction(LablogActionTest actionTest) {
        super(actionTest);
        putValue(Action.NAME, "Delete Entry");
        putValue(Action.SHORT_DESCRIPTION, "Delete the selected item, restricted usage (DEL)");
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        boolean checkResult = true;
        if (actionTest != null) {
            checkResult = actionTest.performTest();
        } else {
            checkResult = performTest();
        }
        if (!checkResult) {
            return;
        }
        int ask = JOptionPane.showConfirmDialog(Application.instance().getMainWindow(), "Are you sure you want to delete the selected entry (entries)?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ask == JOptionPane.NO_OPTION || ask == JOptionPane.CANCEL_OPTION) {
            return;
        }
        testedActionPerformed(event);
    }
}
