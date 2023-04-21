package org.argouml.uml.diagram.ui;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * Static utility methods for use with ArgoFigs.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class ArgoFigUtil {

    /**
     * Find the Project that contains a given figure.  Because we don't have
     * a single reliable way to do this, we try a bunch of different approachs.
     * 
     * @param fig the Fig to return the project of
     * @return the project containing the given fig
     */
    public static Project getProject(ArgoFig fig) {
        if (fig instanceof Fig) {
            Fig f = (Fig) fig;
            LayerPerspective layer = (LayerPerspective) f.getLayer();
            if (layer == null) {
                Editor editor = Globals.curEditor();
                if (editor == null) {
                    return ProjectManager.getManager().getCurrentProject();
                }
                Layer lay = editor.getLayerManager().getActiveLayer();
                if (lay instanceof LayerPerspective) {
                    layer = (LayerPerspective) lay;
                }
            }
            if (layer == null) {
                return ProjectManager.getManager().getCurrentProject();
            }
            GraphModel gm = layer.getGraphModel();
            if (gm instanceof UMLMutableGraphSupport) {
                Project project = ((UMLMutableGraphSupport) gm).getProject();
                if (project != null) {
                    return project;
                }
            }
            return ProjectManager.getManager().getCurrentProject();
        }
        return null;
    }
}
