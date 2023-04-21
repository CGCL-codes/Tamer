package jsesh.mdcDisplayer.swing.editor.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import jsesh.mdcDisplayer.swing.editor.JMDCEditor;

/**
 * Action of adding a new sign as a new cadrat. 
 * @author rosmord
 * @deprecated
 */
public class AddNewCadratAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        JMDCEditor d = (JMDCEditor) e.getSource();
        d.getWorkflow().addSign();
    }
}
