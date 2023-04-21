package org.mcisb.subliminal;

import java.beans.*;
import javax.swing.event.*;
import javax.swing.text.*;
import org.mcisb.ontology.*;
import org.mcisb.ontology.chebi.*;
import org.mcisb.util.*;

/**
 *
 * @author Neil Swainston
 */
public class ReactionSearcher extends PropertyChangeSupported implements DocumentListener, PropertyChangeListener, Disposable {

    /**
	 * 
	 */
    public static final String SEARCHING = "SEARCHING";

    /**
	 * 
	 */
    public static final String ONTOLOGY_TERMS = "ONTOLOGY_TERMS";

    /**
	 * 
	 */
    final OntologyTermSearcher ontologyTermSearcher = new OntologyTermSearcher();

    /**
	 * 
	 * @throws Exception 
	 */
    public ReactionSearcher() throws Exception {
        ontologyTermSearcher.addPropertyChangeListener(this);
        ontologyTermSearcher.setOntologySource(ChebiUtils.getInstance());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            update(e);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        try {
            update(e);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        support.firePropertyChange(evt);
    }

    @Override
    public void dispose() throws Exception {
        ontologyTermSearcher.removePropertyChangeListener(this);
    }

    /**
	 * 
	 * @param e
	 * @throws BadLocationException 
	 */
    private void update(final DocumentEvent e) throws BadLocationException {
        final String text = e.getDocument().getText(0, e.getDocument().getLength());
        if (text.length() > 0) {
            ontologyTermSearcher.setSearchTerm(ReactionUtils.getToken(text, e.getOffset() + (e.getType().equals(DocumentEvent.EventType.INSERT) ? e.getLength() : 0)).trim());
        }
    }
}
