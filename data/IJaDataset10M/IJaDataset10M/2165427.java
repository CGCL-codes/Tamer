package uk.ac.ebi.intact.update.persistence.dao.protein.impl;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.intact.update.model.protein.events.SequenceIdenticalToTranscriptEvent;
import uk.ac.ebi.intact.update.persistence.dao.protein.SequenceIdenticalToTranscriptEventDao;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Default dao for sequence identical to protein transcript events
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>03/08/11</pre>
 */
@Repository
@Transactional(readOnly = true)
@Lazy
public class SequenceIdenticalToTranscriptEventDaoImpl extends ProteinEventDaoImpl<SequenceIdenticalToTranscriptEvent> implements SequenceIdenticalToTranscriptEventDao {

    public SequenceIdenticalToTranscriptEventDaoImpl() {
        super(SequenceIdenticalToTranscriptEvent.class, null);
    }

    public SequenceIdenticalToTranscriptEventDaoImpl(EntityManager entityManager) {
        super(SequenceIdenticalToTranscriptEvent.class, entityManager);
    }

    @Override
    public List<SequenceIdenticalToTranscriptEvent> getByUniprotTranscriptAc(long processId, String transcript) {
        return getSession().createCriteria(getEntityClass()).createAlias("updateProcess", "p").add(Restrictions.eq("p.id", processId)).add(Restrictions.eq("matchingUniprotTranscript", transcript)).addOrder(Order.asc("eventDate")).list();
    }
}
