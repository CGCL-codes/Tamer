package es.iiia.sgi.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import es.iiia.sgi.editors.RenderingEditor;
import es.iiia.sgi.views.ShapeView;
import es.iiia.shapegrammar.model.ShapeGrammarModel;

public class ShowRendererHandler extends AbstractHandler implements IHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
        try {
            if (RenderingEditor.CURRENT_INPUT != null && ShapeGrammarModel.ACTIVE_SHAPE_GRAMMAR != null) {
                page.openEditor(RenderingEditor.CURRENT_INPUT, RenderingEditor.ID);
            }
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        return null;
    }
}
