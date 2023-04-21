package org.argeproje.resim.actions.project;

import org.argeproje.resim.ResimProjectEditor;
import org.argeproje.resim.ShapesEditor;
import org.argeproje.resim.proc.tools.ShapeDiagramRunner;
import org.argeproje.resim.ui.model.ShapesDiagram;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;

public class RunProjectACT extends Action {

    public static final String RUN_ACTION_ID = "PROJECT_RUN_ACTION";

    private IWorkbenchWindow _workbenchWindow;

    public RunProjectACT(IWorkbenchWindow window, String displayName) {
        setId(RUN_ACTION_ID);
        setText(displayName);
        _workbenchWindow = window;
    }

    public void run() {
        try {
            IEditorPart lEditorPart = _workbenchWindow.getActivePage().getActiveEditor();
            ResimProjectEditor rpe = (ResimProjectEditor) lEditorPart;
            if (rpe.getActiveEditor() instanceof ShapesEditor) {
                ShapesDiagram lShapesDiagram = ((ShapesEditor) rpe.getActiveEditor()).getModel();
                ShapeDiagramRunner.setShapesDiagram(lShapesDiagram);
                ShapeDiagramRunner.run();
            }
        } catch (Exception exp) {
            System.err.println(exp.getMessage());
        }
    }
}
