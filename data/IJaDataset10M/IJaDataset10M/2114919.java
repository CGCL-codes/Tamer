package org.fpdev.apps.rtemaster.actions;

import org.fpdev.core.transit.SubRoute;
import org.fpdev.core.transit.TransitPath;
import org.fpdev.apps.rtemaster.RouteMaster;

/**
 *
 * @author demory
 */
public class ClipRouteEndAction extends TransitPathModificationAction {

    private int endPLI_;

    public ClipRouteEndAction(SubRoute sub, int endPLI) {
        super(sub);
        endPLI_ = endPLI;
    }

    protected boolean modifyTransitPath(RouteMaster av) {
        TransitPath path = sub_.getPath();
        if (endPLI_ == 0) {
            TransitPath newPath = new TransitPath(av.getEngine(), sub_, path.getType());
            newPath.addDirectedFirstLink(path.startLink(), path.startNode());
            sub_.setPath(newPath);
            return true;
        }
        int curPLI = path.linkCount() - 1;
        while (curPLI > endPLI_) {
            if (path.stopPrecedes(curPLI)) path.deleteStop(path.getStopIndexFromInPLI(curPLI - 1));
            path.removeLastLink();
            curPLI--;
        }
        return true;
    }

    public String getName() {
        return "Clip Path from End";
    }
}
