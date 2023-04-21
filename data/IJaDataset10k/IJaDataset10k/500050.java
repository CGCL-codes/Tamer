package ws.prova.ide.eclipse.editors;

import org.eclipse.ui.part.IDropActionDelegate;

/**
 * @author beaumont
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RuleDropActionDelegate implements IDropActionDelegate {

    public static final String ID = "ws_prova_ide_rule_drop_actions";

    public boolean run(Object source, Object target) {
        System.out.println("drag and drop:" + source + " to: " + target);
        return false;
    }
}
