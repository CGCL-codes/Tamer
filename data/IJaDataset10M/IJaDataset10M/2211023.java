package es.iiia.sgi.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import es.iiia.sgi.providers.ShapeContentProvider;
import es.iiia.sgi.views.ShapeView;
import es.iiia.shapeeditor.ShapeEditor;
import es.iiia.shapeeditor.ShapeGrammarInput;
import es.iiia.shapegrammar.shape.ShapeModel;
import es.iiia.shapegrammar.utils.Debugger;

public class AddShapeHandler extends AbstractHandler implements IHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchWindow workbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
        ShapeView shapes = (ShapeView) workbenchWindow.getActivePage().findView(ShapeView.ID);
        ShapeModel shape = this.createShape(shapes);
        ShapeGrammarInput input = new ShapeGrammarInput(shape);
        try {
            ShapeEditor editor = (ShapeEditor) workbenchWindow.getActivePage().openEditor(input, shape.getEditorId() == null ? ShapeEditor.ID : shape.getEditorId());
            shape.addPropertyChangeListener(editor);
            workbenchWindow.getActivePage().showView(IPageLayout.ID_PROP_SHEET);
        } catch (PartInitException e) {
            Debugger.getInstance().addMessage(e.getStackTrace());
        }
        return shape;
    }

    protected ShapeModel createShape(ShapeView shapes) {
        return ((ShapeContentProvider) shapes.getContentProvider()).addShape();
    }
}
