package vse.editor.designerEditor.model;

import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import vse.core.IControl;
import vse.core.PropAccess;

public class RadioButtonFigure extends WidgetFigure {

    public RadioButtonFigure(IControl __mdl) {
        super(__mdl);
    }

    @Override
    protected void fillShape(Graphics graphics) {
        PropAccess prop = new PropAccess(getModel().get_properties());
        int topL_x = getBounds().getTopLeft().x;
        int topL_y = getBounds().getTopLeft().y;
        if (!prop.get_visible()) {
            graphics.drawRectangle(topL_x, topL_y, 13, 13);
            graphics.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
            graphics.fillRectangle(topL_x, topL_y, 13, 13);
        } else {
            graphics.drawRectangle(topL_x, topL_y, 13, 13);
        }
        if (prop.get_name().length() == 0) prop.set_name("radiobutton");
        graphics.drawText(prop.get_name(), topL_x + 16, topL_y + 1);
    }

    @Override
    protected void outlineShape(Graphics graphics) {
    }
}
