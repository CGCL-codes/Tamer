package jlibs.nblr.editor.actions;

import jlibs.nblr.editor.widgets.NBLRWidget;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.Widget;

/**
 * @author Santhosh Kumar T
 */
public class Highlighter implements TwoStateHoverProvider {

    private TwoStateHoverProvider delegate;

    public Highlighter(TwoStateHoverProvider delegate) {
        this.delegate = delegate;
    }

    private Widget resolve(Widget widget) {
        ObjectScene scene = (ObjectScene) widget.getScene();
        return scene.findWidget(scene.findObject(widget));
    }

    public void unsetHovering(Widget widget) {
        widget = resolve(widget);
        if (widget == null) return;
        ((NBLRWidget) widget).highLight(false);
        if (delegate != null) delegate.unsetHovering(widget);
    }

    public void setHovering(Widget widget) {
        widget = resolve(widget);
        if (widget == null) return;
        ((NBLRWidget) widget).highLight(true);
        if (delegate != null) delegate.setHovering(widget);
    }
}
