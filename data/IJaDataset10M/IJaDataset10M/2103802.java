package abbot.swt.gef.test;

import java.util.Collection;
import java.util.Iterator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import abbot.swt.gef.hierarchy.FigureHierarchy;
import abbot.swt.gef.hierarchy.FigureHierarchyImpl;
import abbot.swt.gef.util.GEFWorkbenchUtilities;
import abbot.swt.hierarchy.VisitableImpl;
import abbot.swt.hierarchy.Visitor;

public class Util {

    public static GraphicalEditor getLogicEditor() {
        return (GraphicalEditor) GEFWorkbenchUtilities.findEditor(GEFTestProjectSetup.LOGIC_EDITOR_ID);
    }

    public static GraphicalEditor getFlowEditor() {
        return (GraphicalEditor) GEFWorkbenchUtilities.findEditor(GEFTestProjectSetup.FLOW_EDITOR_ID);
    }

    public static void activateLogicEditor() {
        GEFWorkbenchUtilities.activateEditor(getLogicEditor());
    }

    public static void activateFlowEditor() {
        GEFWorkbenchUtilities.activateEditor(getFlowEditor());
    }

    public static IFigure getLogicRootFigure() {
        return GEFWorkbenchUtilities.getRootFigure(getLogicEditor());
    }

    public static IFigure getFlowRootFigure() {
        return GEFWorkbenchUtilities.getRootFigure(getFlowEditor());
    }

    public static FigureHierarchy getLogicHierarchy() {
        return new FigureHierarchyImpl(getLogicRootFigure());
    }

    public static FigureHierarchy getFlowHierarchy() {
        return new FigureHierarchyImpl(getFlowRootFigure());
    }

    public interface FigureVisitor extends Visitor<IFigure> {
    }

    public static void visit(IFigure figure, FigureVisitor visitor) {
        new VisitableImpl<IFigure>() {

            public Collection<IFigure> getChildren(IFigure figure) {
                return figure.getChildren();
            }
        }.accept(figure, visitor);
    }

    public interface EditPartVisitor {

        boolean visit(EditPart editPart);
    }

    public static boolean visit(EditPart editPart, EditPartVisitor visitor) {
        if (visitor.visit(editPart)) {
            for (Iterator iterator = editPart.getChildren().iterator(); iterator.hasNext(); ) {
                EditPart child = (EditPart) iterator.next();
                visit(child, visitor);
            }
        }
        return false;
    }
}
