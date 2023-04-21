package com.evasion.plugin.donation;

import com.evasion.entity.Don;
import com.evasion.entity.Person;
import com.evasion.exception.PersistenceViolationException;
import com.evasion.ejb.DonationManagerLocal;
import com.evasion.ejb.DonationManagerRemote;
import javax.persistence.EntityManager;

/**
 *
 * @author sebastien.glon
 */
public class DonationManager implements DonationManagerLocal, DonationManagerRemote {

    private EntityManager em;

    public DonationManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long savePromesse(Person p, Long l, String d) throws PersistenceViolationException {
        if (l == null || l <= 0L) {
            throw new IllegalArgumentException("le montant ne peut etre null ou <=0");
        }
        Don don = new Don(p, l, d);
        try {
            em.persist(don);
            em.flush();
            return don.getId();
        } catch (Exception e) {
            throw new PersistenceViolationException("Save promesse en Echec", e);
        }
    }

    @Override
    public void validDon(Long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
