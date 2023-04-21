package org.argouml.uml.diagram.ui;

import java.util.List;
import org.argouml.kernel.Project;
import org.tigris.gef.presentation.FigGroup;

/**
 * A fig which contains other figs.  ArgoUMLs version of GEF's FigGroup.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public abstract class ArgoFigGroup extends FigGroup implements ArgoFig {

    public ArgoFigGroup() {
        super();
    }

    public ArgoFigGroup(List arg0) {
        super(arg0);
    }

    public void setProject(Project project) {
        throw new UnsupportedOperationException();
    }

    /**
     * This method is identical to the one in FigNodeModelElement.
     * 
     * @return the project 
     * @see org.argouml.uml.diagram.ui.ArgoFig#getProject()
     */
    public Project getProject() {
        return ArgoFigUtil.getProject(this);
    }
}
