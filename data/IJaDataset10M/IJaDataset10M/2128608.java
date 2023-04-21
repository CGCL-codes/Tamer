package org.coode.cardinality.ui.roweditor;

import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.selector.OWLIndividualSelectorPanel;
import org.protege.editor.owl.ui.selector.OWLObjectPropertySelectorPanel;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLProperty;
import javax.swing.*;
import java.awt.*;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Sep 7, 2007<br><br>
 */
public class IndividualFillerRowEditor extends CardinalityRowEditorPanel {

    private OWLObjectPropertySelectorPanel objectPropertySelectorPanel;

    private OWLIndividualSelectorPanel individualSelectorPanel;

    public IndividualFillerRowEditor(OWLEditorKit eKit, OWLClass subject) {
        super(eKit, subject);
        objectPropertySelectorPanel = new OWLObjectPropertySelectorPanel(eKit);
        objectPropertySelectorPanel.setBorder(ComponentFactory.createTitledBorder("Restricted properties"));
        individualSelectorPanel = new OWLIndividualSelectorPanel(eKit);
        individualSelectorPanel.setBorder(ComponentFactory.createTitledBorder("Filler"));
        setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false);
        splitPane.setResizeWeight(0.5);
        splitPane.setLeftComponent(objectPropertySelectorPanel);
        splitPane.setRightComponent(individualSelectorPanel);
        add(splitPane, BorderLayout.CENTER);
        splitPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }

    protected OWLObject getSelectedFiller() {
        return individualSelectorPanel.getSelectedObject();
    }

    protected OWLProperty getSelectedProperty() {
        return objectPropertySelectorPanel.getSelectedObject();
    }

    public void dispose() {
        objectPropertySelectorPanel.dispose();
        individualSelectorPanel.dispose();
        objectPropertySelectorPanel = null;
        individualSelectorPanel = null;
    }
}
