package org.geergenbein.azw.dao.transcription;

import javax.persistence.EntityManager;
import org.geergenbein.azw.model.transcription.Dialect;
import org.geergenbein.azw.model.transcription.TonedCodePoint;
import org.geergenbein.azw.model.transcription.TranscriptionSystem;
import org.geergenbein.azw.model.transcription.UntonedCodePoint;
import org.geergenbein.dao.GenericHibernateDAO;

/**
 *
 * @author andrew
 */
public class TonedCodePointDAOHibernate extends GenericHibernateDAO<TonedCodePoint, Long> {

    public TonedCodePointDAOHibernate(EntityManager em) {
        super(em);
    }

    public TonedCodePoint find(Dialect dialect, TranscriptionSystem transcriptionSystem, String transcription, Integer tone) {
        return (TonedCodePoint) getSession().getNamedQuery("findTonedCodePointByTranscriptionAndTone").setCacheable(true).setParameter("dialect", dialect).setParameter("transcriptionSystem", transcriptionSystem).setParameter("transcription", transcription).setParameter("tone", tone).uniqueResult();
    }

    public TonedCodePoint find(UntonedCodePoint ucp, Integer tone) {
        return (TonedCodePoint) getSession().getNamedQuery("findTonedCodePoint").setParameter("untonedCodePoint", ucp).setParameter("tone", tone).uniqueResult();
    }

    public TonedCodePoint loadOrPersist(UntonedCodePoint ucp, Integer tone, String name) {
        TonedCodePoint tcp = null;
        if ((tcp = find(ucp, tone)) == null) {
            tcp = new TonedCodePoint(ucp, tone, name);
            makePersistent(tcp);
            log.debug("Persisted TonedCodePoint: " + ucp);
        } else {
            log.debug("Found TonedCodePoint: " + ucp);
        }
        return tcp;
    }
}
