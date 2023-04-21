package org.coode.cardinality.ui.roweditor;

import org.coode.cardinality.model.CardinalityRow;
import org.coode.cardinality.model.CardinalityRowFactory;
import org.protege.editor.core.ui.util.ComponentFactory;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLProperty;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: Nick Drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Sep 7, 2007<br><br>
 */
public abstract class CardinalityRowEditorPanel extends JPanel {

    private OWLClass subject;

    private OWLEditorKit eKit;

    private JCheckBox maxEnabled;

    private JSpinner minCardinalitySpinner;

    private JSpinner maxCardinalitySpinner;

    private ChangeListener minSpinnerChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent changeEvent) {
            final int min = (Integer) minCardinalitySpinner.getValue();
            if (min > (Integer) maxCardinalitySpinner.getValue()) {
                maxCardinalitySpinner.setValue(min);
            }
        }
    };

    private ChangeListener maxSpinnerChangeListener = new ChangeListener() {

        public void stateChanged(ChangeEvent changeEvent) {
            final int max = (Integer) maxCardinalitySpinner.getValue();
            if (max < (Integer) minCardinalitySpinner.getValue()) {
                minCardinalitySpinner.setValue(max);
            }
        }
    };

    public CardinalityRowEditorPanel(OWLEditorKit eKit, OWLClass subject) {
        this.eKit = eKit;
        this.subject = subject;
    }

    public void setSubject(OWLClass subject) {
        this.subject = subject;
    }

    protected final OWLClass getSubject() {
        return subject;
    }

    protected final OWLEditorKit getOWLEditorKit() {
        return eKit;
    }

    public final CardinalityRow getRow() {
        return CardinalityRowFactory.createRow(getSubject(), getSelectedProperty(), getSelectedFiller(), getMin(), getMax(), false, getOWLEditorKit().getModelManager());
    }

    protected abstract OWLObject getSelectedFiller();

    protected abstract OWLProperty getSelectedProperty();

    protected final int getMin() {
        if (minCardinalitySpinner != null) {
            return (Integer) minCardinalitySpinner.getValue();
        }
        return 1;
    }

    protected final int getMax() {
        if (maxCardinalitySpinner != null) {
            if (maxEnabled.isSelected()) {
                return (Integer) maxCardinalitySpinner.getValue();
            } else {
                return -1;
            }
        }
        return 1;
    }

    protected JComponent createCardinalityPanel() {
        maxEnabled = new JCheckBox("max", false);
        maxEnabled.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                maxCardinalitySpinner.setEnabled(maxEnabled.isSelected());
            }
        });
        minCardinalitySpinner = new JSpinner(new SpinnerNumberModel(1, 0, null, 1));
        minCardinalitySpinner.addChangeListener(minSpinnerChangeListener);
        maxCardinalitySpinner = new JSpinner(new SpinnerNumberModel(1, 0, null, 1));
        maxCardinalitySpinner.addChangeListener(maxSpinnerChangeListener);
        maxCardinalitySpinner.setEnabled(false);
        JComponent minCardinalitySpinnerEditor = minCardinalitySpinner.getEditor();
        JComponent maxCardinalitySpinnerEditor = maxCardinalitySpinner.getEditor();
        Dimension prefSize = minCardinalitySpinnerEditor.getPreferredSize();
        minCardinalitySpinnerEditor.setPreferredSize(new Dimension(50, prefSize.height));
        maxCardinalitySpinnerEditor.setPreferredSize(new Dimension(50, prefSize.height));
        JPanel minSpinnerHolder = new JPanel(new BorderLayout(4, 4));
        minSpinnerHolder.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        minSpinnerHolder.add(new JLabel("min"), BorderLayout.WEST);
        minSpinnerHolder.add(minCardinalitySpinner, BorderLayout.EAST);
        JPanel maxSpinnerHolder = new JPanel(new BorderLayout(4, 4));
        maxSpinnerHolder.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        maxSpinnerHolder.add(maxEnabled, BorderLayout.WEST);
        maxSpinnerHolder.add(maxCardinalitySpinner, BorderLayout.EAST);
        JPanel spinnerAlignmentPanel = new JPanel();
        spinnerAlignmentPanel.setBorder(ComponentFactory.createTitledBorder("Cardinality"));
        spinnerAlignmentPanel.add(minSpinnerHolder);
        spinnerAlignmentPanel.add(maxSpinnerHolder);
        return spinnerAlignmentPanel;
    }

    public abstract void dispose();
}
