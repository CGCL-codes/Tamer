package org.argouml.uml.cognitive.critics;

import java.util.Vector;
import javax.swing.JPanel;
import org.argouml.cognitive.ui.WizStepCue;
import org.argouml.kernel.Wizard;
import org.tigris.gef.util.VectorSet;
import ru.novosoft.uml.foundation.core.MModelElement;

/** A non-modal wizard to help the user change navigability
 *  of an association. */
public class WizCueCards extends Wizard {

    protected Vector _cues = new Vector();

    protected WizStepCue _steps[] = null;

    public WizCueCards() {
    }

    public int getNumSteps() {
        return _cues.size();
    }

    public MModelElement getModelElement() {
        if (_item != null) {
            VectorSet offs = _item.getOffenders();
            if (offs.size() >= 1) {
                MModelElement me = (MModelElement) offs.elementAt(0);
                return me;
            }
        }
        return null;
    }

    public void addCue(String s) {
        _cues.addElement(s);
    }

    /** Create a new panel for the given step.
   *
   * @return a newly created panel or null if there isn't that many steps.
   */
    public JPanel makePanel(int newStep) {
        if (newStep <= getNumSteps()) {
            String c = (String) _cues.elementAt(newStep - 1);
            return new WizStepCue(this, c);
        }
        return null;
    }

    /** This wizard never takes action, it just displays step by step
   *  instructions. */
    public void doAction(int oldStep) {
    }

    /** This wizard cannot automatically finish the task. It can only be
   *  finished when the user is on the last step. */
    public boolean canFinish() {
        return _step == getNumSteps();
    }
}
