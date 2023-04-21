package org.mcisb.subliminal.annotate.ui;

import java.awt.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import org.mcisb.ontology.*;
import org.mcisb.subliminal.annotate.*;
import org.mcisb.ui.ontology.*;
import org.mcisb.ui.util.*;
import org.mcisb.util.*;

/**
 * 
 * @author Neil Swainston
 */
public class SbmlAnnotatorProgressPanel extends TextProgressPanel implements ChangeListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("org.mcisb.subliminal.annotate.ui.messages");

    /**
	 * 
	 */
    private JProgressBar matchedProgressBar;

    /**
	 * 
	 */
    private JCheckBox silentCheckBox;

    /**
	 * 
	 * @param title
	 * @param textAreaPreferredSize
	 * @param silent
	 */
    public SbmlAnnotatorProgressPanel(final String title, final Dimension textAreaPreferredSize, final boolean silent) {
        super(title, textAreaPreferredSize, true);
        matchedProgressBar = new JProgressBar();
        matchedProgressBar.setBackground(Color.RED);
        matchedProgressBar.setForeground(Color.GREEN);
        matchedProgressBar.setIndeterminate(false);
        matchedProgressBar.setStringPainted(true);
        progressBar.setIndeterminate(false);
        progressBar.setStringPainted(true);
        silentCheckBox = new JCheckBox(resourceBundle.getString("SbmlAnnotatorProgressPanel.silentCheckBoxText"));
        silentCheckBox.setSelected(silent);
        silentCheckBox.addChangeListener(this);
        add(display, 0, 0, true, true, true, false, GridBagConstraints.BOTH);
        add(matchedProgressBar, 0, 1, true, true, false, false, GridBagConstraints.HORIZONTAL);
        add(progressBar, 0, 2, true, true, false, false, GridBagConstraints.HORIZONTAL);
        add(silentCheckBox, 0, 3, true, true, false, true, GridBagConstraints.HORIZONTAL);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        super.propertyChange(e);
        if (!silentCheckBox.isSelected() && e.getPropertyName().equals(SbmlAnnotator.IDS)) {
            final Map<String, Collection<OntologyTerm>> searchResults = (Map<String, Collection<OntologyTerm>>) e.getNewValue();
            final Map.Entry<String, Collection<OntologyTerm>> searchResult = CollectionUtils.getFirst(searchResults.entrySet());
            final Collection<OntologyTerm> ontologyTerms = searchResult.getValue();
            final Container topLevelAncestor = getTopLevelAncestor();
            final JDialog owner = (topLevelAncestor instanceof Frame) ? new JDialog((Frame) topLevelAncestor) : new JDialog();
            owner.setModal(true);
            try {
                final int PAUSE = 1000;
                final OntologyTermDialog ontologyTermDialog = new OntologyTermDialog(owner, resourceBundle.getString("SbmlAnnotatorProgressPanel.title"), true, resourceBundle.getString("SbmlAnnotatorProgressPanel.message") + searchResult.getKey(), searchResult.getKey());
                ontologyTermDialog.setOntologyTerms(ontologyTerms);
                ComponentUtils.setLocationCentral(ontologyTermDialog);
                ontologyTermDialog.setVisible(true);
                firePropertyChange(SbmlAnnotator.TERM_ID, null, ontologyTermDialog.getOntologyTerm());
                Thread.sleep(PAUSE);
            } catch (Exception ex) {
                final JDialog errorDialog = new ExceptionComponentFactory(true).getExceptionDialog(getParent(), ExceptionUtils.toString(ex), ex);
                ComponentUtils.setLocationCentral(errorDialog);
                errorDialog.setVisible(true);
            }
        } else if (e.getPropertyName().equals(SbmlAnnotator.SUCCESS_RATE)) {
            matchedProgressBar.setValue(((Integer) e.getNewValue()).intValue());
        }
    }

    @Override
    public void stateChanged(final ChangeEvent event) {
        firePropertyChange(SbmlAnnotator.SILENT, !silentCheckBox.isSelected(), silentCheckBox.isSelected());
    }

    @Override
    public void dispose() throws Exception {
        super.dispose();
        silentCheckBox.removeChangeListener(this);
    }
}
