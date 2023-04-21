package org.nakedobjects.plugins.dndviewer.viewer.debug;

import java.util.ArrayList;
import java.util.List;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.plugins.dndviewer.Content;
import org.nakedobjects.plugins.dndviewer.UserAction;
import org.nakedobjects.plugins.dndviewer.View;
import org.nakedobjects.plugins.dndviewer.Workspace;
import org.nakedobjects.plugins.dndviewer.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dndviewer.viewer.content.PerspectiveContent;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Location;
import org.nakedobjects.runtime.userprofile.PerspectiveEntry;

/**
 * Display debug window
 */
public class DebugOption extends AbstractUserAction {

    public DebugOption() {
        super("Debug...", UserAction.DEBUG);
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
        final InfoDebugFrame f = new InfoDebugFrame();
        final Content content = view.getContent();
        final NakedObject object = content == null ? null : content.getNaked();
        List<DebugInfo> debug = new ArrayList<DebugInfo>();
        if (content instanceof PerspectiveContent) {
            PerspectiveEntry perspective = ((PerspectiveContent) content).getPerspective();
            debug.add(perspective);
        } else {
            debug.add(new DebugObjectSpecification(content.getSpecification()));
        }
        if (object != null) {
            debug.add(new DebugAdapter(object));
            debug.add(new DebugObjectGraph(object));
        }
        debug.add(new DebugViewStructure(view));
        debug.add(new DebugContent(view));
        debug.add(new DebugDrawing(view));
        debug.add(new DebugDrawingAbsolute(view));
        f.setInfo(debug.toArray(new DebugInfo[debug.size()]));
        f.show(at.getX() + 50, at.getY() + 6);
    }

    @Override
    public String getDescription(final View view) {
        return "Open debug window about " + view;
    }
}
