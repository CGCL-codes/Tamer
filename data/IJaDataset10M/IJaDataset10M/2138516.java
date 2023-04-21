package uk.ac.bolton.archimate.editor.model.viewpoints;

import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * Abstract Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractViewpoint implements IViewpoint {

    private List<EClass> fClassList;

    @Override
    public boolean isElementVisible(EObject object) {
        if (getAllowedList() == null) {
            return true;
        }
        EClass eClass = null;
        if (object instanceof IDiagramModelArchimateObject) {
            eClass = ((IDiagramModelArchimateObject) object).getArchimateElement().eClass();
        } else if (object instanceof IArchimateElement) {
            eClass = object.eClass();
        }
        if (eClass != null && !getAllowedList().contains(eClass)) {
            return false;
        }
        if (object.eContainer() instanceof IDiagramModelObject) {
            return isElementVisible(object.eContainer());
        }
        return true;
    }

    @Override
    public boolean isAllowedType(EClass type) {
        return getAllowedList() == null ? true : getAllowedList().contains(type);
    }

    ;

    /**
     * @return A list of allowed types or null
     */
    protected List<EClass> getAllowedList() {
        if (getAllowedTypes() != null && fClassList == null) {
            fClassList = Arrays.asList(getAllowedTypes());
        }
        return fClassList;
    }
}
