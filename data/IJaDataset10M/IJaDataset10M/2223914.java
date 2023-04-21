package org.dllearner.tools.ore;

import javax.swing.event.ListSelectionEvent;
import org.dllearner.core.owl.NamedClass;

/**
 * Wizard panel descriptor for selecting one of the atomic classes in OWL-ontology that 
 * has to be (re)learned.
 * @author Lorenz Buehmann
 *
 */
public class ClassPanelOWLDescriptor extends WizardPanelDescriptor implements javax.swing.event.ListSelectionListener {

    /**
	 * Identification string for class choose panel.
	 */
    public static final String IDENTIFIER = "CLASS_CHOOSE_OWL_PANEL";

    /**
     * Information string for class choose panel.
     */
    public static final String INFORMATION = "In this panel all atomic classes in the ontology are shown in the list above. " + "Select one of them which should be (re)learned from, then press \"Next-Button\"";

    private ClassPanelOWL owlClassPanel;

    /**
     * Constructor creates new panel and adds listener to list.
     */
    public ClassPanelOWLDescriptor() {
        owlClassPanel = new ClassPanelOWL();
        owlClassPanel.addSelectionListener(this);
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(owlClassPanel);
    }

    @Override
    public Object getNextPanelDescriptor() {
        return LearningPanelDescriptor.IDENTIFIER;
    }

    @Override
    public Object getBackPanelDescriptor() {
        return KnowledgeSourcePanelDescriptor.IDENTIFIER;
    }

    @Override
    public void aboutToDisplayPanel() {
        getWizard().getInformationField().setText(INFORMATION);
        setNextButtonAccordingToConceptSelected();
    }

    /**
     * Method is called when other element in list is selected, and sets next button enabled.
     * @param e ListSelectionEvent
     */
    public void valueChanged(ListSelectionEvent e) {
        setNextButtonAccordingToConceptSelected();
        if (!e.getValueIsAdjusting()) {
            getWizardModel().getOre().setClassToLearn((NamedClass) owlClassPanel.getList().getSelectedValue());
        }
    }

    private void setNextButtonAccordingToConceptSelected() {
        if (owlClassPanel.getList().getSelectedValue() != null) {
            getWizard().setNextFinishButtonEnabled(true);
        } else {
            getWizard().setNextFinishButtonEnabled(false);
        }
    }

    /**
	 * Returns the JPanel with the GUI elements.
	 * @return extended JPanel
	 */
    public ClassPanelOWL getOwlClassPanel() {
        return owlClassPanel;
    }
}
