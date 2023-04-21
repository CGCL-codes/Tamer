package com.cosylab.vdct.visual.scene;

import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.Widget;

/**
 * Anchor implementation.
 *
 * @author Janez Golob
 */
public class ModelSceneHorizontalAnchor extends Anchor {

    private Widget widget;

    private boolean isSource;

    public ModelSceneHorizontalAnchor(Widget widget, boolean isSource) {
        super(widget);
        this.widget = widget;
        this.isSource = isSource;
    }

    public Result compute(Entry entry) {
        Rectangle bounds = widget.convertLocalToScene(widget.getBounds());
        Point point = bounds.getLocation();
        if (isSource) {
            point.translate(bounds.width, bounds.height / 2);
            return new Anchor.Result(point, Anchor.Direction.RIGHT);
        } else {
            point.translate(0, bounds.height / 2);
            return new Anchor.Result(point, Anchor.Direction.LEFT);
        }
    }
}
