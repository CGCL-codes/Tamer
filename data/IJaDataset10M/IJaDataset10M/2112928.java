package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.viewer.skylark.ButtonAction;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Click;
import org.nakedobjects.viewer.skylark.Toolkit;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.abstracts.AbstractControlView;
import org.nakedobjects.viewer.skylark.drawing.Color;
import org.nakedobjects.viewer.skylark.drawing.Size;
import org.nakedobjects.viewer.skylark.drawing.Text;

public class Button extends AbstractControlView {

    private static final int TEXT_PADDING = 12;

    private final int buttonHeight;

    private boolean over;

    private boolean pressed;

    public Button(final ButtonAction action, final View target) {
        super(action, target);
        this.buttonHeight = 4 + Toolkit.getText("control").getTextHeight() + 4;
    }

    public boolean containsFocus() {
        return hasFocus();
    }

    public void draw(final Canvas canvas) {
        int x = 0;
        int y = 0;
        View target = getParent();
        String text = action.getName(target);
        boolean vetoed = action.disabled(target).isVetoed();
        Color color = vetoed ? Toolkit.getColor("menu.disabled") : Toolkit.getColor("black");
        Color border = vetoed ? Toolkit.getColor("secondary3") : Toolkit.getColor("secondary2");
        Text style = Toolkit.getText("control");
        int buttonWidth = TEXT_PADDING + style.stringWidth(text) + TEXT_PADDING;
        canvas.clearBackground(this, Toolkit.getColor("secondary3"));
        canvas.drawRectangle(x, y, buttonWidth, buttonHeight, over & !vetoed ? Toolkit.getColor("primary1") : Toolkit.getColor("black"));
        canvas.draw3DRectangle(x + 1, y + 1, buttonWidth - 2, buttonHeight - 2, border, !pressed);
        canvas.draw3DRectangle(x + 2, y + 2, buttonWidth - 4, buttonHeight - 4, border, !pressed);
        if (((ButtonAction) action).isDefault()) {
            canvas.drawRectangle(x + 3, y + 3, buttonWidth - 6, buttonHeight - 6, border);
        }
        if (hasFocus()) {
            canvas.drawRectangle(x + 3, y + 3, buttonWidth - 6, buttonHeight - 6, Toolkit.getColor("white"));
        }
        canvas.drawText(text, x + TEXT_PADDING, y + buttonHeight / 2 + style.getMidPoint(), color, style);
    }

    public void entered() {
        over = true;
        pressed = false;
        markDamaged();
        super.entered();
    }

    public void exited() {
        over = false;
        pressed = false;
        markDamaged();
        super.exited();
    }

    public Size getMaximumSize() {
        String text = action.getName(getView());
        int buttonWidth = TEXT_PADDING + Toolkit.getText("control").stringWidth(text) + TEXT_PADDING;
        return new Size(buttonWidth, buttonHeight);
    }

    public void mouseDown(final Click click) {
        View target = getParent();
        boolean vetoed = action.disabled(target).isVetoed();
        if (!vetoed) {
            pressed = true;
            markDamaged();
        }
    }

    public void mouseUp(final Click click) {
        View target = getParent();
        boolean vetoed = action.disabled(target).isVetoed();
        if (!vetoed) {
            pressed = false;
            markDamaged();
        }
    }
}
