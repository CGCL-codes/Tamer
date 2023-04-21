package svim.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import svim.SvimEditorPane;
import svim.TextIO;

/**
 * 
 * @author sahaqiel
 */
public class OpenFile extends AbstractAction {

    private static final long serialVersionUID = 4847475349339006196L;

    public void actionPerformed(ActionEvent event) {
        final SvimEditorPane target = ActionUtil.getTextComponent(event);
        String filePath = event.getActionCommand();
        if (filePath != null) {
            if (target.getMode().equals(SvimEditorPane.NORMAL_MODE)) {
                String newText = new TextIO().openFilePath(filePath);
                if (newText != null) {
                    target.setText(newText);
                }
            } else if (target.getMode().equals(SvimEditorPane.INSERT_MODE)) {
            }
        }
    }
}
