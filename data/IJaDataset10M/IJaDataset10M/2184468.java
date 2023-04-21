package uk.ac.ebi.intact.util.uniprotExport.event;

import java.util.EventListener;

/**
 * TODO comment this!
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: CcLineEventListener.java 6610 2006-11-10 10:11:09Z baranda $
 * @since <pre>30-Aug-2006</pre>
 */
public interface CcLineEventListener extends EventListener {

    void processNonBinaryInteraction(NonBinaryInteractionFoundEvent evt);

    void drLineProcessed(DrLineProcessedEvent evt);

    void ccLineCreated(CcLineCreatedEvent evt);
}
