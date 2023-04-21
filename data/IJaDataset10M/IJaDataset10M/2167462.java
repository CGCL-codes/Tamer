package uk.co.ordnancesurvey.confluence.ui.editor.annotation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.core.ui.util.VerifiedInputEditor;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.AbstractOWLObjectEditor;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 * This annotation editor implements an AbstractOWLObjectEditor that edits
 * OWLAnnotations. Note that OWLAnnotations consist of an OWLAnnotationProperty
 * and an OWLAnnotationValue. This editor does not support editing of the
 * OWLAnnotationProperty, it assumes that the annotation property is fixed at
 * construction time. The annotation value is edited by delegation to a
 * valueEditor, which is also fixed at construction time.
 * 
 * Due to the 'fixing' of annotation property and value editor, this annotation
 * editor is less powerful than the default OWLAnnotationEditor; however, it
 * also results in simpler interfaces for the user. Use this editor or a
 * subclass of this editor when you want to give your user a simple interface
 * and no choice of changing annotation property (and limited choice for
 * changing annotation value).
 * 
 * @author rdenaux
 * 
 */
public class SingleAnnotationPropertyAnnotationEditor extends AbstractOWLObjectEditor<OWLAnnotation> implements VerifiedInputEditor {

    private static final Logger log = Logger.getLogger(SingleAnnotationPropertyAnnotationEditor.class.getName());

    protected final OWLDataFactory owlDataFactory;

    private final OWLAnnotationProperty propertyToEdit;

    private final List<InputVerificationStatusChangedListener> verificationListeners = new ArrayList<InputVerificationStatusChangedListener>();

    protected final OWLObjectEditor<? extends OWLAnnotationValue> valueEditor;

    private boolean ownsValueEditor = false;

    /**
	 * 
	 * @param aDataFactory
	 *            used to create the edited OWLAnnotation
	 * @param aAnnotationProperty
	 *            this editor can only edit annotations that have this
	 *            annotationProperty
	 * @param aValueEditor
	 *            the delegate editor which edits the value of the annototations
	 *            being edited.
	 */
    public SingleAnnotationPropertyAnnotationEditor(OWLDataFactory aDataFactory, OWLAnnotationProperty aAnnotationProperty, OWLObjectEditor<? extends OWLAnnotationValue> aValueEditor) {
        assert (aDataFactory != null);
        assert (aAnnotationProperty != null);
        assert (aValueEditor != null);
        owlDataFactory = aDataFactory;
        propertyToEdit = aAnnotationProperty;
        valueEditor = aValueEditor;
    }

    public void setOwnershipOfValueEditor() {
        ownsValueEditor = true;
    }

    public String getEditorTypeName() {
        return "Edit value for " + propertyToEdit;
    }

    public final boolean canEdit(Object object) {
        boolean result = false;
        if (object instanceof OWLAnnotation) {
            result = canEditAnnotation((OWLAnnotation) object);
        }
        return result;
    }

    protected final boolean canEditAnnotation(OWLAnnotation annotation) {
        boolean result = false;
        if (annotation != null) {
            result = propertyToEdit.equals(annotation.getProperty()) && valueEditor.canEdit(annotation.getValue());
        }
        return result;
    }

    public final JComponent getEditorComponent() {
        return valueEditor.getEditorComponent();
    }

    public final OWLAnnotation getEditedObject() {
        OWLAnnotation result = null;
        OWLAnnotationValue value = valueEditor.getEditedObject();
        if (value != null) {
            result = owlDataFactory.getOWLAnnotation(propertyToEdit, value);
        }
        return result;
    }

    public boolean setEditedObject(OWLAnnotation editedObject) {
        boolean result = false;
        if (canEdit(editedObject)) {
            setValueInValueEditor(valueEditor, editedObject.getValue());
            result = true;
        }
        notifyVerificationStatus();
        return result;
    }

    private void notifyVerificationStatus() {
        for (InputVerificationStatusChangedListener vl : verificationListeners) {
            vl.verifiedStatusChanged(canCreateAnnotation());
        }
    }

    private <T extends OWLAnnotationValue> void setValueInValueEditor(OWLObjectEditor<T> aValueEditor, OWLAnnotationValue value) {
        assert (aValueEditor.canEdit(value));
        aValueEditor.setEditedObject((T) value);
    }

    public void dispose() {
        if (ownsValueEditor) valueEditor.dispose();
    }

    public void addStatusChangedListener(InputVerificationStatusChangedListener listener) {
        verificationListeners.add(listener);
        listener.verifiedStatusChanged(canCreateAnnotation());
    }

    public void removeStatusChangedListener(InputVerificationStatusChangedListener listener) {
        verificationListeners.remove(listener);
    }

    private boolean canCreateAnnotation() {
        return valueEditor.getEditedObject() != null;
    }
}
