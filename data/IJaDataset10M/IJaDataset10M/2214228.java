package jsystem.treeui.actionItems;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import jsystem.framework.TestRunnerFrame;
import jsystem.guiMapping.JsystemMapping;
import jsystem.treeui.TestRunner;
import org.jfree.util.Log;

public class ExitAction extends IgnisAction {

    private static final long serialVersionUID = 1L;

    private static ExitAction action;

    private ExitAction() {
        super();
        putValue(Action.NAME, "Exit");
        putValue(Action.SHORT_DESCRIPTION, JsystemMapping.getInstance().getExitButton());
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        putValue(Action.ACTION_COMMAND_KEY, "exit");
    }

    public static ExitAction getInstance() {
        if (action == null) {
            action = new ExitAction();
        }
        return action;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        exit();
    }

    public void exit() {
        int save_Ans = 0;
        int ans = JOptionPane.showConfirmDialog(TestRunnerFrame.guiMainFrame, "Are you sure that you want to exit ?", "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ans == JOptionPane.YES_OPTION) {
            try {
                save_Ans = SaveScenarioAction.getInstance().saveCurrentScenarioWithConfirmation();
            } catch (Exception e1) {
                Log.error(e1.getMessage());
            }
            if (save_Ans != JOptionPane.CANCEL_OPTION) {
                TestRunner.treeView.dispose();
                TestRunner.treeView.getRunner().exit();
            }
        }
    }
}
