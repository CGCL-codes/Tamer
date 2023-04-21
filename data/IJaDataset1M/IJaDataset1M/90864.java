package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.object.Dump;
import org.nakedobjects.object.Naked;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.application.ApplicationClass;
import org.nakedobjects.utility.DebugInfo;
import org.nakedobjects.utility.DebugString;
import org.nakedobjects.utility.InfoDebugFrame;
import org.nakedobjects.viewer.skylark.UserAction;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.Workspace;
import org.nakedobjects.viewer.skylark.abstracts.AbstractUserAction;
import org.nakedobjects.viewer.skylark.drawing.Location;

/**
 * Display debug window
 */
public class DebugSpecificationOption extends AbstractUserAction {

    public DebugSpecificationOption() {
        super("Specification...", UserAction.DEBUG);
    }

    public void execute(final Workspace workspace, final View view, final Location at) {
        InfoDebugFrame f = new InfoDebugFrame();
        f.setInfo(new DebugInfo() {

            public String debugTitle() {
                return "Class Specification";
            }

            public void debugData(final DebugString debug) {
                Naked object = (view.getContent()).getNaked();
                ApplicationClass cls = (ApplicationClass) object.getObject();
                NakedObjectSpecification specification = cls.forObjectType();
                Dump.specification(specification, debug);
            }
        });
        f.show(at.getX() + 50, at.getY() + 6);
    }

    public String getDescription(final View view) {
        Naked object = (view.getContent()).getNaked();
        ApplicationClass cls = (ApplicationClass) object.getObject();
        NakedObjectSpecification specification = cls.forObjectType();
        return "Open debug window about " + specification.getShortName();
    }
}
