package org.nakedobjects.plugins.dnd.view.debug;

import java.util.ArrayList;
import java.util.List;
import org.nakedobjects.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.plugins.dnd.drawing.Location;
import org.nakedobjects.plugins.dnd.service.PerspectiveContent;
import org.nakedobjects.plugins.dnd.view.Content;
import org.nakedobjects.plugins.dnd.view.Toolkit;
import org.nakedobjects.plugins.dnd.view.View;
import org.nakedobjects.plugins.dnd.view.Workspace;
import org.nakedobjects.plugins.dnd.view.option.UserActionAbstract;
import org.nakedobjects.runtime.userprofile.PerspectiveEntry;

/**
 * Display debug window
 */
public class DebugOption extends UserActionAbstract {

    public DebugOption() {
        super("Debug...", NakedObjectActionType.DEBUG);
    }

    @Override
    public void execute(final Workspace workspace, final View view, final Location at) {
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
        DebugInfo[] info = debug.toArray(new DebugInfo[debug.size()]);
        at.add(50, 6);
        Toolkit.getViewer().showDebugFrame(info, at);
    }

    @Override
    public String getDescription(final View view) {
        return "Open debug window about " + view;
    }
}
