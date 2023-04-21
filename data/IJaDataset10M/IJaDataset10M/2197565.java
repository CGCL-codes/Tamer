package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.GroupEditPart;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;

/**
 * Property Section for a Group
 * 
 * @author Phillip Beauvoir
 */
public class GroupSection extends AbstractArchimatePropertySection {

    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";

    private Adapter eAdapter = new AdapterImpl() {

        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            if (feature == IArchimatePackage.Literals.NAMEABLE__NAME) {
                refreshNameField();
                fPage.labelProviderChanged(null);
            } else if (feature == IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION) {
                refreshDocumentationField();
            }
        }
    };

    private IDiagramModelGroup fDiagramModelGroup;

    private PropertySectionTextControl fTextName;

    private PropertySectionTextControl fTextDocumentation;

    @Override
    protected void createControls(Composite parent) {
        fTextName = createNameControl(parent, Messages.GroupSection_0);
        fTextDocumentation = createDocumentationControl(parent, Messages.GroupSection_1);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void setElement(Object element) {
        if (element instanceof GroupEditPart) {
            fDiagramModelGroup = (IDiagramModelGroup) ((EditPart) element).getModel();
            if (fDiagramModelGroup == null) {
                throw new RuntimeException("Diagram Group was null");
            }
        } else {
            throw new RuntimeException("Should have been a Group Edit Part");
        }
        refreshControls();
    }

    protected void refreshControls() {
        refreshNameField();
        refreshDocumentationField();
    }

    protected void refreshNameField() {
        if (fIsExecutingCommand) {
            return;
        }
        fTextName.refresh(fDiagramModelGroup);
    }

    protected void refreshDocumentationField() {
        if (fIsExecutingCommand) {
            return;
        }
        fTextDocumentation.refresh(fDiagramModelGroup);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fDiagramModelGroup;
    }

    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
