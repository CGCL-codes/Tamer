package org.argouml.cognitive.ui;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.argouml.cognitive.ToDoItem;
import org.argouml.i18n.Translator;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.UndoableAction;

/**
 * Action to display the todo pane.
 *
 */
public class ActionGoToCritique extends UndoableAction {

    private ToDoItem item = null;

    /**
     * Constructor.
     *
     * @param theItem The item that we go to.
     */
    public ActionGoToCritique(ToDoItem theItem) {
        super(Translator.localize(theItem.getHeadline()), null);
        putValue(Action.SHORT_DESCRIPTION, Translator.localize(theItem.getHeadline()));
        item = theItem;
    }

    public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
        ((ToDoPane) ProjectBrowser.getInstance().getTodoPane()).selectItem(item);
    }
}
