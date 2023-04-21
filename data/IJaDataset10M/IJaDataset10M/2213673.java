package net.claribole.zvtm.eval;

import net.claribole.zvtm.engine.PostAnimationAction;
import net.claribole.zvtm.lens.Lens;

public class AbstractTV2DZP2LensAction implements PostAnimationAction {

    AbstractTrajectoryViewer2D application;

    public AbstractTV2DZP2LensAction(AbstractTrajectoryViewer2D application) {
        this.application = application;
    }

    public void animationEnded(Object target, short type, String dimension) {
        if (type == PostAnimationAction.LENS) {
            application.vsm.getOwningView(((Lens) target).getID()).setLens(null);
            ((Lens) target).dispose();
            application.setMagFactor(AbstractTrajectoryViewer2D.DEFAULT_MAG_FACTOR);
            application.lens = null;
            application.setLens(AbstractTaskEventHandler.NO_LENS);
        }
    }
}
