package uk.ac.bolton.archimate.editor.model.viewpoints;

import org.eclipse.emf.ecore.EClass;

/**
 * Layered Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class LayeredViewpoint extends AbstractViewpoint {

    @Override
    public String getName() {
        return Messages.LayeredViewpoint_0;
    }

    @Override
    public int getIndex() {
        return LAYERED_VIEWPOINT;
    }

    @Override
    public EClass[] getAllowedTypes() {
        return null;
    }
}
