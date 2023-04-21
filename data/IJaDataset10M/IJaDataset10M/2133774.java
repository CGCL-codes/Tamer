package ch.intertec.storybook.view.label;

import javax.swing.JLabel;
import ch.intertec.storybook.model.state.SceneState;
import ch.intertec.storybook.toolkit.I18N;
import ch.intertec.storybook.view.interfaces.IRefreshable;

/**
 * @author martin
 * 
 */
@SuppressWarnings("serial")
public class SceneStateLabel extends JLabel implements IRefreshable {

    private SceneState state;

    private boolean iconOnly;

    public SceneStateLabel(SceneState state) {
        this(state, false);
    }

    public SceneStateLabel(SceneState state, boolean iconOnly) {
        this.state = state;
        this.iconOnly = iconOnly;
        refresh();
    }

    @Override
    public void refresh() {
        if (!iconOnly) {
            setText(state.toString());
        }
        setIcon(state.getIcon());
        setToolTipText(I18N.getMsgColon("msg.status") + " " + state);
    }

    public SceneState getState() {
        return state;
    }

    public void setState(SceneState state) {
        this.state = state;
    }
}
