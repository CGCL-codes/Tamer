package org.argouml.uml.ui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MModelElement;

public class ActionAddNote extends UMLChangeAction {

    protected static final int DISTANCE = 80;

    public static ActionAddNote SINGLETON = new ActionAddNote();

    public ActionAddNote() {
        super("Note");
    }

    public void actionPerformed(ActionEvent ae) {
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        Object target = pb.getDetailsTarget();
        if (target == null) {
            target = pb.getTarget();
            if (target == null || !(target instanceof MModelElement)) return;
        }
        MModelElement elem = (MModelElement) target;
        MComment comment = CoreFactory.getFactory().buildComment(elem);
        Fig elemFig = pb.getActiveDiagram().presentationFor(elem);
        if (elemFig == null) return;
        int x = 0;
        int y = 0;
        Diagram diagram = pb.getActiveDiagram();
        Layer lay = diagram.getLayer();
        Rectangle drawingArea = pb.getEditorPane().getBounds();
        FigComment fig = new FigComment(diagram.getGraphModel(), comment);
        if (elemFig instanceof FigNode) {
            x = elemFig.getX() + elemFig.getWidth() + DISTANCE;
            y = elemFig.getY();
            if (x + fig.getWidth() > drawingArea.getX()) {
                x = elemFig.getX() - fig.getWidth() - DISTANCE;
                if (x < 0) {
                    x = elemFig.getX();
                    y = elemFig.getY() - fig.getHeight() - DISTANCE;
                    if (y < 0) {
                        y = elemFig.getY() + elemFig.getHeight() + DISTANCE;
                        if (y + fig.getHeight() > drawingArea.getHeight()) {
                            comment.remove();
                            return;
                        }
                    }
                }
            }
        } else if (elemFig instanceof FigEdge) {
            comment.remove();
            return;
        }
        fig.setLocation(x, y);
        lay.add(fig);
        FigEdgeNote edge = new FigEdgeNote(elem, comment);
        lay.add(edge);
        lay.sendToBack(edge);
        edge.damage();
        fig.damage();
        elemFig.damage();
        super.actionPerformed(ae);
    }

    public boolean shouldBeEnabled() {
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        Object target = pb.getDetailsTarget();
        return super.shouldBeEnabled() && (target instanceof MModelElement) && (pb.getActiveDiagram().presentationFor(target) instanceof FigNode);
    }
}
