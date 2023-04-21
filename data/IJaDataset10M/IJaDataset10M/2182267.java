package org.netbeans.modules.visual.router;

import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author David Kaspar
 */
public class DirectRouter implements Router {

    public DirectRouter() {
    }

    public List<Point> routeConnection(ConnectionWidget widget) {
        ArrayList<Point> list = new ArrayList<Point>();
        Anchor sourceAnchor = widget.getSourceAnchor();
        Anchor targetAnchor = widget.getTargetAnchor();
        if (sourceAnchor != null && targetAnchor != null) {
            list.add(sourceAnchor.compute(widget.getSourceAnchorEntry()).getAnchorSceneLocation());
            list.add(targetAnchor.compute(widget.getTargetAnchorEntry()).getAnchorSceneLocation());
        }
        return list;
    }
}
