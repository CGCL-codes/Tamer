package org.argouml.uml.diagram.state.ui;

import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.state.StateDiagramGraphModel;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;

/** Abstract class to with common behavior for nestable nodes in UML
    MState diagrams. */
public abstract class FigStateVertex extends FigNodeModelElement {

    public FigStateVertex() {
    }

    public FigStateVertex(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    public void setEnclosingFig(Fig encloser) {
        super.setEnclosingFig(encloser);
        if (!(getOwner() instanceof MStateVertex)) return;
        MStateVertex sv = (MStateVertex) getOwner();
        MCompositeState m = null;
        if (encloser != null && (encloser.getOwner() instanceof MCompositeState)) {
            m = (MCompositeState) encloser.getOwner();
        } else {
            ProjectBrowser pb = ProjectBrowser.TheInstance;
            if (pb.getTarget() instanceof UMLDiagram) {
                try {
                    GraphModel gm = ((UMLDiagram) pb.getTarget()).getGraphModel();
                    StateDiagramGraphModel sdgm = (StateDiagramGraphModel) gm;
                    m = (MCompositeState) sdgm.getMachine().getTop();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (m != null) sv.setContainer(m);
    }
}
