package uk.ac.ebi.intact.core.lifecycle.status;

import org.springframework.stereotype.Controller;
import uk.ac.ebi.intact.core.lifecycle.IllegalTransitionException;
import uk.ac.ebi.intact.core.lifecycle.LifecycleEventListener;
import uk.ac.ebi.intact.core.lifecycle.LifecycleTransition;
import uk.ac.ebi.intact.core.util.DebugUtil;
import uk.ac.ebi.intact.model.CvLifecycleEventType;
import uk.ac.ebi.intact.model.CvPublicationStatusType;
import uk.ac.ebi.intact.model.Publication;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 */
@Controller
public class StartStatus extends GlobalStatus {

    public StartStatus() {
        setStatusType(null);
    }

    /**
     * Create a publication object.
     *
     * @param publication the publication
     * @param mechanism mechanism of creation of the publication
     */
    @LifecycleTransition(toStatus = CvPublicationStatusType.NEW)
    public void create(Publication publication, String mechanism) {
        if (publication.getStatus() != null) {
            throw new IllegalTransitionException("Cannot get publication in status NEW when it's status is already set (" + publication.getStatus().getShortLabel() + "): " + DebugUtil.annotatedObjectToString(publication, false));
        }
        changeStatus(publication, CvPublicationStatusType.NEW, CvLifecycleEventType.CREATED, mechanism);
        for (LifecycleEventListener listener : getListeners()) {
            listener.fireCreated(publication);
        }
    }
}
