package astcentric.editor.eclipse;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Control;
import astcentric.editor.common.view.graphic.AbsolutePositionProvider;
import astcentric.editor.common.view.graphic.Point;

public class EclipseAbsolutePositionProvider implements AbsolutePositionProvider {

    private Control _control;

    public void setControl(Control control) {
        _control = control;
    }

    public Point getAbsolutePosition() {
        org.eclipse.swt.graphics.Point position = _control.toDisplay(0, 0);
        if (_control instanceof ScrolledComposite) {
            ScrolledComposite scrolledComposite = (ScrolledComposite) _control;
            org.eclipse.swt.graphics.Point origin = scrolledComposite.getOrigin();
            position.x -= origin.x;
            position.y -= origin.y;
        }
        return new Point(position.x, position.y);
    }
}
