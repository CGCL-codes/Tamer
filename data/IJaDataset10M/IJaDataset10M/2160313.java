package org.nakedobjects.viewer.skylark.abstracts;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.utility.ToString;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.Workspace;
import org.nakedobjects.viewer.skylark.drawing.Location;

public class CloseAllViewsForObjectOption extends AbstractUserAction {

    public CloseAllViewsForObjectOption() {
        super("Close all views for this object");
    }

    public void execute(final Workspace workspace, final View view, final Location at) {
        workspace.removeViewsFor((NakedObject) view.getContent().getNaked());
    }

    public String getDescription(final View view) {
        return "Close all views for " + view.getContent().title();
    }

    public String toString() {
        return new ToString(this).toString();
    }
}
