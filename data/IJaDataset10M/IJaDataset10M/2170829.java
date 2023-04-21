package odm.diagram.view.factories;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import odm.ClassAssertion;
import odm.Individual;
import odm.OWL;
import odm.OWLClass;
import odm.OdmFactory;
import odm.diagram.edit.parts.IndividualIndividualCompartmentEditPart;
import odm.diagram.edit.parts.IndividualTagEditPart;
import odm.diagram.edit.parts.IndividualURIEditPart;
import odm.diagram.edit.parts.OWLEditPart;
import odm.diagram.part.OdmVisualIDRegistry;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.view.factories.AbstractShapeViewFactory;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import de.uka.aifb.owl.util.CreateClassForIndividualDialog;

/**
 * @generated
 */
public class IndividualViewFactory extends AbstractShapeViewFactory {

    private int counter = 0;

    /**
	 * @generated 
	 */
    protected List createStyles(View view) {
        List styles = new ArrayList();
        styles.add(NotationFactory.eINSTANCE.createFontStyle());
        styles.add(NotationFactory.eINSTANCE.createDescriptionStyle());
        styles.add(NotationFactory.eINSTANCE.createFillStyle());
        styles.add(NotationFactory.eINSTANCE.createLineStyle());
        return styles;
    }

    protected void decorateView(View containerView, View view, IAdaptable semanticAdapter, String semanticHint, int index, boolean persisted) {
        if (semanticHint == null) {
            semanticHint = OdmVisualIDRegistry.getType(odm.diagram.edit.parts.IndividualEditPart.VISUAL_ID);
            view.setType(semanticHint);
        }
        super.decorateView(containerView, view, semanticAdapter, semanticHint, index, persisted);
        if (!OWLEditPart.MODEL_ID.equals(OdmVisualIDRegistry.getModelID(containerView))) {
            EAnnotation shortcutAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
            shortcutAnnotation.setSource("Shortcut");
            shortcutAnnotation.getDetails().put("modelID", OWLEditPart.MODEL_ID);
            view.getEAnnotations().add(shortcutAnnotation);
        }
        getViewService().createNode(semanticAdapter, view, OdmVisualIDRegistry.getType(IndividualTagEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
        getViewService().createNode(semanticAdapter, view, OdmVisualIDRegistry.getType(IndividualURIEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
        getViewService().createNode(semanticAdapter, view, OdmVisualIDRegistry.getType(IndividualIndividualCompartmentEditPart.VISUAL_ID), ViewUtil.APPEND, true, getPreferencesHint());
        OWL owl = null;
        if (semanticAdapter instanceof CreateElementRequestAdapter) {
            owl = (OWL) ((CreateElementRequestAdapter) semanticAdapter).resolve().eContainer();
        }
        if (semanticAdapter instanceof EObjectAdapter) {
            owl = (OWL) ((EObjectAdapter) semanticAdapter).resolve().eContainer();
        }
        Individual individual = (Individual) semanticAdapter.getAdapter(Individual.class);
        if (individual.getURI() != null) return;
        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        boolean showDialog = false;
        List owlClassList = owl.getOWLClass();
        for (Object obj : owlClassList) {
            OWLClass owlClass = (OWLClass) obj;
            String uri = owlClass.getURIFromOWLEntity();
            ht.put(uri, owlClass);
            showDialog = true;
        }
        CreateClassForIndividualDialog dialog;
        if (showDialog) {
            dialog = new CreateClassForIndividualDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), ht.keySet());
            dialog.open();
        } else {
            MessageDialog msgDialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Error", null, "No class found! You first have to create a class, a individual is always a member of a class.", MessageDialog.ERROR, new String[] { "OK" }, 0);
            msgDialog.open();
            cancel(individual, owl);
            return;
        }
        int button = dialog.getButton();
        if (button == 3) {
            cancel(individual, owl);
        }
        try {
            String key = dialog.getSelectedKey();
            if (button == 1 && key != null && !key.isEmpty()) {
                OWLClass owlClass = (OWLClass) ht.get(key);
                ClassAssertion classAssertion = OdmFactory.eINSTANCE.createClassAssertion();
                classAssertion.setOwlClass(owlClass);
                classAssertion.setIndividual(individual);
                individual.getClassMemberOf().add(classAssertion);
            } else {
                cancel(individual, owl);
            }
        } catch (Exception e) {
            cancel(individual, owl);
            e.printStackTrace();
        }
    }

    private void cancel(Individual individual, OWL owl) {
        owl.getIndividual().remove(individual);
        makeError();
    }

    private void makeError() {
        Object error = null;
        error.getClass();
    }
}
