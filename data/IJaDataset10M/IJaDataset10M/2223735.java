package de.rowbuddy.business;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import de.rowbuddy.entities.Boat;
import de.rowbuddy.entities.BoatDamage;
import de.rowbuddy.entities.Member;
import de.rowbuddy.exceptions.RowBuddyException;

@Stateless
public class BoatManagement {

    @PersistenceContext
    private EntityManager em;

    public BoatManagement() {
    }

    public List<Boat> getBoatOverview() {
        TypedQuery<Boat> q = em.createQuery("SELECT b FROM Boat b WHERE b.deleted = false", Boat.class);
        return q.getResultList();
    }

    public Boat getBoat(Long id) throws RowBuddyException {
        if (id == null) {
            throw new RowBuddyException("Boot id darf nicht null sein");
        }
        Boat boat = em.find(Boat.class, id);
        if (boat == null) {
            throw new RowBuddyException("Boot existiert nicht");
        }
        return boat;
    }

    public Boat addBoat(Boat addBoat) throws RowBuddyException {
        if (addBoat.getId() != null) {
            throw new RowBuddyException("Boot darf keine vordefinierte Id haben");
        }
        if (addBoat.isDeleted()) {
            throw new RowBuddyException("Cannot add deleted boat");
        }
        addBoat.validate();
        em.persist(addBoat);
        return addBoat;
    }

    public Boat updateBoat(Boat updateBoat) throws RowBuddyException {
        if (updateBoat.getId() == null) {
            throw new RowBuddyException("Boot Id darf nicht null sein");
        }
        if (updateBoat.isDeleted()) {
            throw new RowBuddyException("Update darf nicht auf einem geloeschten Boot aufgerufen wird");
        }
        Boat dbBoat = getBoat(updateBoat.getId());
        if (dbBoat.isDeleted()) {
            throw new RowBuddyException("Update darf nicht auf einem geloeschten Boot aufgerufen wird");
        }
        updateBoat.validate();
        em.merge(updateBoat);
        return updateBoat;
    }

    public List<BoatDamage> getDamages(ListType type) {
        TypedQuery<BoatDamage> q = null;
        if (type == ListType.ALL) {
            q = em.createQuery("SELECT b FROM BoatDamage b", BoatDamage.class);
        } else {
            q = em.createQuery("SELECT b FROM BoatDamage b WHERE b.fixed = false", BoatDamage.class);
        }
        return q.getResultList();
    }

    public void addDamage(BoatDamage damage, Member logger, Long boatId) throws RowBuddyException {
        if (damage.getId() != null) {
            throw new RowBuddyException("Id darf nicht null sein");
        }
        damage.setLogDate(new Date());
        damage.setLogger(logger);
        damage.validate();
        if (damage.getBoat() != null) {
            throw new RowBuddyException("Boot darf nicht null sein");
        }
        Boat persistedBoat = getBoat(boatId);
        damage.setBoat(persistedBoat);
        em.persist(damage);
        persistedBoat.addBoatDamage(damage);
    }

    public BoatDamage updateDamage(BoatDamage damage) throws RowBuddyException {
        damage.validate();
        em.merge(damage);
        return damage;
    }

    public void deleteBoat(Long id) throws RowBuddyException {
        if (id == null) {
            throw new RowBuddyException("Es muss eine Id angegeben werden");
        }
        Boat boat = getBoat(id);
        if (boat.isDeleted()) {
            throw new RowBuddyException("Boot ist schon geloescht");
        }
        boat.setDeleted(true);
    }

    public List<Boat> searchBoat(String search) {
        TypedQuery<Boat> q = em.createQuery("SELECT b FROM Boat b WHERE lower(b.name) like :searchString AND b.deleted=false", Boat.class);
        q.setParameter("searchString", "%" + search.toLowerCase() + "%");
        return q.getResultList();
    }

    public List<Boat> searchBoatNotLocked(String search) {
        TypedQuery<Boat> q = em.createQuery("SELECT b FROM Boat b WHERE lower(b.name) like :searchString AND b.deleted=false AND b.locked=false", Boat.class);
        q.setParameter("searchString", "%" + search.toLowerCase() + "%");
        return q.getResultList();
    }

    public BoatDamage getDamage(Long id) throws RowBuddyException {
        if (id == null) {
            throw new RowBuddyException("Id muss angegeben werden");
        }
        return em.find(BoatDamage.class, id);
    }
}
