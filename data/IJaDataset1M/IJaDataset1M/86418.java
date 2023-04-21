package uk.ac.ebi.intact.core.persistence.dao;

import uk.ac.ebi.intact.annotation.Mockable;
import uk.ac.ebi.intact.model.CvObject;
import java.util.Collection;
import java.util.List;

/**
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: CvObjectDao.java 13109 2009-05-11 10:30:49Z baranda $
 * @since <pre>08-May-2006</pre>
 */
@Mockable
public interface CvObjectDao<T extends CvObject> extends AnnotatedObjectDao<T> {

    List<T> getByPsiMiRefCollection(Collection<String> psiMis);

    /**
     * Returns a list of controlled vocabulary terms having the given MI reference as identity.
     *
     * @param psiMiRef MI identifier we are looking after.
     *
     * @return a controlled vocabulary term of type T.
     */
    T getByPsiMiRef(String psiMiRef);

    List<T> getByObjClass(Class[] objClasses);

    <T extends CvObject> T getByShortLabel(Class<T> cvType, String label);

    <T extends CvObject> T getByPrimaryId(Class<T> cvType, String miRef);

    /**
     * Collects all MIs identifiers of CvInteractorType being NucleicAcid or children term.
     * @return a non null collection of MI identifiers.
     */
    public Collection<String> getNucleicAcidMIs();

    /**
     * Gets the last number for a Cv Object identifier. For instance,
     * if we query for "IA" and in the database exist IA:0001 and IA:0004, the Integer 4 will be returned
     * @param prefix The prefix to query (e.g. MI, IA ...)
     * @return The highest Integer with that prefix. It will return null if no identifiers with the prefix
     * provided were found.
     * @since 1.9.0
     */
    Integer getLastCvIdentifierWithPrefix(String prefix);
}
