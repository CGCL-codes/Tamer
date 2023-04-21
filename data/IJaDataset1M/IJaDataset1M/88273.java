package oclac.view.application.actions;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import oclac.util.I18N;
import oclac.view.ui.MainWindow;

@SuppressWarnings("serial")
public class DeleteTasksAction extends AbstractAction {

    /**
	 * Constructor for this action.
	 */
    public DeleteTasksAction() {
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/oclac/res/delete.png")));
        putValue(Action.SMALL_ICON, icon);
        putValue(Action.NAME, I18N.instance.getMessage("DeleteTasksAction.Label"));
        putValue(Action.SHORT_DESCRIPTION, I18N.instance.getMessage("DeleteTasksAction.ShortDescription"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    }

    /**
	 * Deletes the selected tasks (if there are any).
	 */
    @Override
    public void actionPerformed(ActionEvent e) {
        Component component = MainWindow.instance.getCenterPane().getSelectedComponent();
        if (component instanceof mxGraphComponent) {
            mxGraphActions.getDeleteAction().actionPerformed(new ActionEvent(component, e.getID(), e.getActionCommand()));
        }
    }
}
