package ring.gui.animationstate;

import java.awt.Toolkit;
import javax.swing.ImageIcon;
import ring.gui.context.UIOperation;
import ring.gui.context.UIState;

/**
 *
 */
public class StepForwardUIOperation extends UIOperation {

    /**
	 * @param state
	 */
    public StepForwardUIOperation(UIState state, boolean enabled) {
        super(state, UIOperation.ACTION_TYPE, enabled, true);
    }

    /**
	 * @see ring.gui.context.UIOperation#execute()
	 */
    public void execute() {
        AnimationUIState animationState = (AnimationUIState) state;
        animationState.stepForward();
    }

    /**
	 * @see ring.gui.context.UIOperation#getName()
	 */
    public String getName() {
        return null;
    }

    /**
	 * @see ring.gui.context.UIOperation#getIcon()
	 */
    public ImageIcon getIcon() {
        return new ImageIcon(Toolkit.getDefaultToolkit().createImage(getClass().getClassLoader().getResource("StepForward.gif")));
    }
}
