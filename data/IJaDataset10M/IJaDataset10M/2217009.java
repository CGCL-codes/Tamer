package it.uniroma3.plasm.actions;

import it.uniroma3.plasm.core.Plasm;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;

/**
 * @author root
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EvaluateSelection extends EvaluateAction {

    private ISelection selection;

    public EvaluateSelection() {
        super();
    }

    public void evaluate() {
        if (selection != null && selection instanceof ITextSelection) {
            ITextSelection textSelection = (ITextSelection) selection;
            if (!textSelection.isEmpty()) {
                String cmd = textSelection.getText();
                cmd = cmd.trim();
                if (cmd.length() != 0) {
                    try {
                        Plasm.getInstance().eval(cmd);
                    } catch (java.io.IOException e) {
                    }
                }
            }
        }
    }

    public void setSelection(ISelection s) {
        this.selection = s;
    }
}
