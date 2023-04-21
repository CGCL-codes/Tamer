package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextArea;
import org.argouml.kernel.UmlModelMutator;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;

/**
 * A JTextArea especially made to represent UMLPlainTextDocuments.
 * @author jaap.branderhorst@xs4all.nl
 * @since Dec 28, 2002
 */
@UmlModelMutator
public class UMLTextArea2 extends JTextArea implements PropertyChangeListener, TargettableModelView {

    /**
     * Serial version generated for rev 1.9
     */
    private static final long serialVersionUID = -9172093001792636086L;

    /**
     * Constructor for UMLTextArea2.
     * @param doc the plain text document
     */
    public UMLTextArea2(UMLDocument doc) {
        super(doc);
        setFont(LookAndFeelMgr.getInstance().getStandardFont());
        addCaretListener(ActionCopy.getInstance());
        addCaretListener(ActionCut.getInstance());
        addCaretListener(ActionPaste.getInstance());
        addFocusListener(ActionPaste.getInstance());
    }

    public void propertyChange(PropertyChangeEvent evt) {
        ((UMLDocument) getDocument()).propertyChange(evt);
    }

    public TargetListener getTargettableModel() {
        return ((UMLDocument) getDocument());
    }
}
