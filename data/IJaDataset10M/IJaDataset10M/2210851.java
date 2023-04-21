package uk.co.ordnancesurvey.confluence.ui.editor.annotation.value;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLConstantEditor;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.editor.OWLObjectEditorHandler;
import org.protege.editor.owl.ui.framelist.IValidatedInput;
import org.protege.editor.owl.ui.framelist.IValidationChangeListener;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import uk.ac.leeds.comp.ui.util.MvcHtmlBuilder;
import uk.co.ordnancesurvey.confluence.ui.CflTextKey;
import uk.co.ordnancesurvey.confluence.vocab.VocabularyAdapter;
import uk.co.ordnancesurvey.kanga.KangaVocabulary;

/**
 * Edits the value of an annotation and adds a reference to a knowledge source.
 * 
 * 
 * @author rdenaux
 * 
 * @deprecated right now this implementation is quite messy. This class should
 *              be rewritten and is just a prototype to test ideas during user
 *              test.
 * 
 * 
 */
public class OWLConstantWithReferenceEditor extends JPanel implements OWLObjectEditor<OWLLiteral>, IValidatedInput {

    private static final long serialVersionUID = 2954980164069485986L;

    private static final Logger log = Logger.getLogger(OWLConstantWithReferenceEditor.class.getName());

    private final OWLEditorKit editorKit;

    private final OWLConstantEditor delegate;

    private final CflAnnotationComboBoxModel refModel;

    private final CflAnnotationComboBoxController refController;

    private final List<IValidationChangeListener> validationListeners;

    private final List<String> invalidMsgs;

    private final DocumentListener docListener;

    private final ItemListener itemListener;

    public OWLConstantWithReferenceEditor(OWLEditorKit aEditorKit) {
        editorKit = aEditorKit;
        validationListeners = new ArrayList<IValidationChangeListener>();
        invalidMsgs = new ArrayList<String>();
        delegate = new OWLConstantEditor(aEditorKit);
        delegate.hideDatatype(true);
        delegate.hideLang(true);
        docListener = new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                notifyChangedValidation();
            }

            public void insertUpdate(DocumentEvent e) {
                notifyChangedValidation();
            }

            public void removeUpdate(DocumentEvent e) {
                notifyChangedValidation();
            }
        };
        itemListener = new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                notifyChangedValidation();
            }
        };
        getTextArea().getDocument().addDocumentListener(docListener);
        refModel = new CflAnnotationComboBoxModel(getActiveOntology(), new VocabularyAdapter(KangaVocabulary.KNOWLEDGE_SOURCE));
        refController = new CflAnnotationComboBoxController();
        refController.setModel(refModel);
        refController.getView().addItemListener(itemListener);
        init();
    }

    private OWLOntology getActiveOntology() {
        return editorKit.getOWLModelManager().getActiveOntology();
    }

    public void addValidationChangeListener(IValidationChangeListener listener) {
        synchronized (validationListeners) {
            validationListeners.add(listener);
        }
    }

    public boolean canEdit(Object object) {
        return delegate.canEdit(object);
    }

    /**
     * Clears listeners and resources used.
     * 
     * @see org.protege.editor.owl.ui.frame.OWLAnnotationValueEditor#dispose()
     */
    public void dispose() {
        if (!validationListeners.isEmpty()) {
            log.severe("There are validation listeners still registered!!");
            validationListeners.clear();
        }
        getTextArea().getDocument().removeDocumentListener(docListener);
        refController.getView().removeItemListener(itemListener);
        delegate.dispose();
        refController.dispose();
    }

    public OWLLiteral getEditedObject() {
        final String annValue = getTextArea().getText();
        final Object cbItemSelected = refController.getView().getItemSelected();
        String refLabel = " [No reference!!]";
        if (cbItemSelected != null) {
            refLabel = " [" + cbItemSelected.toString() + "]";
        }
        assert (!annValue.endsWith(refLabel));
        final String valueWithRef = annValue + refLabel;
        return owlDataFactory().getOWLLiteral(valueWithRef, "");
    }

    private OWLDataFactory owlDataFactory() {
        return editorKit.getOWLModelManager().getOWLDataFactory();
    }

    public String getEditorTypeName() {
        return delegate.getEditorTypeName();
    }

    public List<String> getInvalidMessages() {
        invalidMsgs.clear();
        if (!isTextAreaValid()) {
            invalidMsgs.add(CflTextKey.INVALID_NATURAL_LANGUAGE_DESCRIPTION.getText());
        }
        if (!isKnowledgeResourceRefValid()) {
            invalidMsgs.add(CflTextKey.INVALID_KNOWLEDGE_SOURCE_REF.getText());
        }
        return Collections.unmodifiableList(invalidMsgs);
    }

    public String getSingleMessage() {
        return getSingleMessageAsHtml();
    }

    /**
     * Returns a single message for this {@link IValidatedInput} as a html
     * string.
     * 
     * @return
     */
    private String getSingleMessageAsHtml() {
        final MvcHtmlBuilder hb = new MvcHtmlBuilder();
        if (isInputValid()) {
            hb.append(CflTextKey.OK_TO_ACCEPT_NLDESC.getText());
        } else {
            hb.append(CflTextKey.INVALID_INPUT.getText());
            hb.appendAsUnorderedList(getInvalidMessages());
        }
        return hb.toString();
    }

    /**
     * Ugly hack to retrieve the text area defined by the delegate where the
     * actual value can be found. We'll probably not reuse the p4 components
     * anyway as they are difficult to extend, so this class will be replaced by
     * one to which we have full control.
     * 
     * @return
     */
    private JTextArea getTextArea() {
        JTextArea result = null;
        result = getTextArea(delegate.getEditorComponent());
        assert result != null;
        return result;
    }

    /**
     * Recursively iterate through aComponent until a JTextArea is found.
     * 
     * @param component
     * @return
     */
    private JTextArea getTextArea(JComponent component) {
        JTextArea result = null;
        for (final Component subComp : component.getComponents()) {
            if (subComp instanceof JTextArea) {
                result = (JTextArea) subComp;
                break;
            } else if (subComp instanceof JComponent) {
                result = getTextArea((JComponent) subComp);
                if (result != null) {
                    break;
                }
            }
        }
        return result;
    }

    private void init() {
        setLayout(new BorderLayout());
        add(delegate, BorderLayout.CENTER);
        add(refController.getView(), BorderLayout.SOUTH);
    }

    public boolean isInputValid() {
        return isTextAreaValid() && isKnowledgeResourceRefValid();
    }

    /**
     * The knowledge resource ref field is valid if it contains some selection.
     * 
     * @return
     */
    private boolean isKnowledgeResourceRefValid() {
        final Object cbItemSelected = refController.getView().getItemSelected();
        return cbItemSelected != null;
    }

    public boolean isPreferred(Object object) {
        return delegate.isPreferred(object);
    }

    /**
     * The text area is valid if it contains some kind of text.
     * 
     * @return
     */
    private boolean isTextAreaValid() {
        boolean result = false;
        final JTextArea annValue = getTextArea();
        result = annValue != null;
        if (result) {
            result = annValue.getText().length() > 0;
        }
        return result;
    }

    /**
     * Notifies the listeners of this {@link IValidatedInput} that a change of
     * validation happened.
     */
    private void notifyChangedValidation() {
        synchronized (validationListeners) {
            for (final IValidationChangeListener l : validationListeners) {
                log.fine("Notifying " + l + " about validation change");
                l.validationChanged(this);
            }
        }
    }

    public void removeValidationChangeListener(IValidationChangeListener listener) {
        synchronized (validationListeners) {
            validationListeners.remove(listener);
        }
    }

    public boolean setEditedObject(OWLLiteral object) {
        delegate.setEditedObject(object);
        refModel.updateModel();
        final JTextArea annValue = getTextArea();
        if (object != null && annValue != null) {
            final String valueWithRef = annValue.getText();
            final int beginRef = valueWithRef.lastIndexOf('[');
            final int endRef = valueWithRef.lastIndexOf(']');
            log.fine("setEditedObject to " + object.getLiteral());
            log.fine("Value with ref " + valueWithRef + " with ref at " + beginRef + ":" + endRef);
            if ((beginRef > 1) && ((beginRef + 1) <= (endRef - 1)) && (endRef > 0)) {
                final String refString = valueWithRef.substring(beginRef + 1, endRef);
                final String valueNoRef = valueWithRef.substring(0, beginRef - 1);
                log.fine("refString " + refString);
                log.fine("value wo ref " + valueNoRef);
                annValue.setText(valueNoRef);
                refController.setSelectedItem(refString);
            }
        }
        return true;
    }

    public void clear() {
        delegate.clear();
    }

    public Set<OWLLiteral> getEditedObjects() {
        return delegate.getEditedObjects();
    }

    public JComponent getEditorComponent() {
        return this;
    }

    public OWLObjectEditorHandler<OWLLiteral> getHandler() {
        return delegate.getHandler();
    }

    public boolean isMultiEditSupported() {
        return delegate.isMultiEditSupported();
    }

    public void setHandler(OWLObjectEditorHandler<OWLLiteral> handler) {
        delegate.setHandler(handler);
    }
}
