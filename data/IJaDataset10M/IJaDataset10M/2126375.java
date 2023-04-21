package org.nakedobjects.plugins.dnd.viewer.action;

import org.nakedobjects.plugins.dnd.ButtonAction;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;

public class Button extends AbstractControlView {

    private ButtonRender buttonRender = new Button3DStyleRender();

    public Button(final ButtonAction action, final View target) {
        super(action, target);
    }

    @Override
    public boolean containsFocus() {
        return hasFocus();
    }

    @Override
    public void draw(final Canvas canvas) {
        final View target = getParent();
        final String text = action.getName(target);
        boolean isDisabled = action.disabled(target).isVetoed();
        boolean isDefault = ((ButtonAction) action).isDefault();
        buttonRender.draw(canvas, getSize(), isDisabled, isDefault, hasFocus(), isOver(), isPressed(), text);
    }

    public Size getMaximumSize() {
        final String text = action.getName(getView());
        return buttonRender.getMaximumSize(text);
    }
}
