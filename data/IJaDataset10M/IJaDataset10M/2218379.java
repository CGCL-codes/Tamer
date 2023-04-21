package org.tuba.spatschorke.diploma.operation.ecoretools.loaddiagram.tool;

import org.eclipse.emf.common.util.URI;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.swt.widgets.Display;

public abstract class DiagramLoader {

    public static Diagram getDiagram(URI diagramURI) {
        EditorRunnable editorRunnable = new EditorRunnable(diagramURI);
        boolean runningInGUI = false;
        if (Display.getCurrent() != null) runningInGUI = Thread.currentThread().equals(Display.getCurrent().getThread());
        if (runningInGUI) {
            editorRunnable.run();
        } else {
            Display display = Display.getDefault();
            display.syncExec(editorRunnable);
        }
        return editorRunnable.getDiagram();
    }
}
