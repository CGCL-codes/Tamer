package uk.ac.ebi.intact.dbupdate.prot.listener;

import uk.ac.ebi.intact.dbupdate.prot.ProcessorException;
import uk.ac.ebi.intact.dbupdate.prot.event.*;
import java.util.EventListener;

/**
 * Listener for ProteinProcessors
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: ProteinUpdateProcessorListener.java 11678 2008-06-30 07:44:08Z baranda $
 */
public interface ProteinUpdateProcessorListener extends EventListener {

    void onDelete(ProteinEvent evt) throws ProcessorException;

    void onProteinDuplicationFound(DuplicatesFoundEvent evt) throws ProcessorException;

    void onProteinSequenceChanged(ProteinSequenceChangeEvent evt) throws ProcessorException;

    void onProteinCreated(ProteinEvent evt) throws ProcessorException;

    void onUpdateCase(UpdateCaseEvent evt) throws ProcessorException;

    void onNonUniprotProteinFound(ProteinEvent evt) throws ProcessorException;

    void onRangeChanged(RangeChangedEvent evt) throws ProcessorException;
}
