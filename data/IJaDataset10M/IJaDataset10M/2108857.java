package jtrackbase.db;

import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import jtrackbase.Constants;
import oracle.toplink.essentials.exceptions.DatabaseException;

public class TagFacade extends AbstractFacade {

    public static Collection<Tag> findAll() {
        EntityManager em = DBController.getEntityManager();
        Query query = em.createQuery("SELECT t FROM Tag t");
        Collection<Tag> tags;
        try {
            tags = castToList(query.getResultList());
        } catch (DatabaseException ex) {
            return null;
        }
        return tags;
    }

    public static Collection<Tag> findMediumFlags() {
        return findAllForType(TagTarget.MEDIUM);
    }

    public static Collection<Tag> findLabelFlags() {
        return findAllForType(TagTarget.LABEL);
    }

    public static Collection<Tag> findTrackFlags() {
        return findAllForType(TagTarget.TRACK);
    }

    public static Collection<Tag> findArtistFlags() {
        return findAllForType(TagTarget.ARTIST);
    }

    private static Collection<Tag> findAllForType(TagTarget tt) {
        EntityManager em = DBController.getEntityManager();
        Query queryTag = em.createNativeQuery("Select * from Tag where target = #tt", Tag.class);
        queryTag.setParameter("tt", tt.toString());
        Collection<Tag> tags;
        try {
            tags = castToList(queryTag.getResultList());
        } catch (DatabaseException ex) {
            return null;
        }
        return tags;
    }

    public static void initBasicTypes() {
        EntityManager em = DBController.getEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        for (String tagName : Constants.INITIAL_TAGS_MEDIUM) {
            Tag t = new Tag(TagTarget.MEDIUM);
            t.setName(tagName);
            em.persist(t);
        }
        for (String tagName : Constants.INITIAL_TAGS_LABEL) {
            Tag t = new Tag(TagTarget.LABEL);
            t.setName(tagName);
            em.persist(t);
        }
        for (String tagName : Constants.INITIAL_TAGS_TRACK) {
            Tag t = new Tag(TagTarget.TRACK);
            t.setName(tagName);
            em.persist(t);
        }
        for (String tagName : Constants.INITIAL_TAGS_ARTIST) {
            Tag t = new Tag(TagTarget.ARTIST);
            t.setName(tagName);
            em.persist(t);
        }
        et.commit();
    }
}
