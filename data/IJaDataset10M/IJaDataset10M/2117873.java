package es.eucm.eadventure.editor.control.tools.assessment;

import es.eucm.eadventure.common.data.assessment.TimedAssessmentEffect;
import es.eucm.eadventure.common.data.assessment.TimedAssessmentRule;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.tools.Tool;

/**
 * Edition Tool for adding an effect in a timedAssessmentRule
 * 
 * @author Javier
 * 
 */
public class AddEffectTool extends Tool {

    protected TimedAssessmentRule containsAS;

    protected TimedAssessmentEffect oldEffectBlock;

    protected int index;

    protected int mode;

    public AddEffectTool(TimedAssessmentRule rule, int index) {
        this.containsAS = rule;
        this.index = index;
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public boolean canUndo() {
        return oldEffectBlock != null;
    }

    @Override
    public boolean combine(Tool other) {
        return false;
    }

    @Override
    public boolean doTool() {
        containsAS.addEffect();
        oldEffectBlock = containsAS.getLastEffect();
        return true;
    }

    @Override
    public boolean redoTool() {
        containsAS.getEffects().add(oldEffectBlock);
        Controller.getInstance().updatePanel();
        return true;
    }

    @Override
    public boolean undoTool() {
        containsAS.getEffects().remove(oldEffectBlock);
        Controller.getInstance().updatePanel();
        return true;
    }
}
