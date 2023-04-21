package issrg.utils.repository;

import javax.naming.directory.DirContext;
import javax.naming.CompositeName;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import java.util.Vector;
import java.security.Principal;
import org.apache.log4j.*;

/**
 * This class is the implementation of the Attribute Repository for 
 * multithreaded access to a cluster of repositories.
 * It can be built out of an array of Repositories. Each of these repositories
 * constitutes a root for searches. 
 *
 * <p>The object can be used for retrieving similar information from multiple
 * directories simultaneously. For example, it is useful when retrieving X.509
 * Attribute Certificates for PMI entities that possess ACs issued by different
 * issuers (therefore, stored in different repositories available to them).
 *
 * <p>The object creates multiple threads (one per repository) when attributes
 * are requested and performs the searches simultaneously. This improves 
 * efficiency, since most of the time the repositories
 * are waiting for a reply. The object waits till all of the contexts return
 * anything or report an error, so the result is always complete.
 *
 * @author A Otenko
 * @version 0.2
 */
public class MultiRepository extends DefaultRepository {

    private AttributeRepository[] initialRepositories;

    private MultiThreadSearch mts = new MultiThreadSearch();

    private static Logger logger = Logger.getLogger(MultiRepository.class);

    protected MultiRepository() {
    }

    /**
   * This constructor builds the MultiRepository with a number of roots.
   *
   * @param repositories An array of AttributeRepositories to be used as the
   *   search roots
   */
    public MultiRepository(AttributeRepository[] repositories) {
        AttributeRepository[] r = new AttributeRepository[repositories.length];
        System.arraycopy(repositories, 0, r, 0, r.length);
        int j = 0;
        for (int i = 0; i < r.length; i++) {
            if (r[j] == null && r[i] != null) {
                r[j] = r[i];
                r[i] = null;
            }
            if (r[j] != null) j++;
        }
        initialRepositories = new AttributeRepository[j];
        System.arraycopy(r, 0, initialRepositories, 0, initialRepositories.length);
    }

    /**
    * This creates a MultiRepository with a single root repository
    *
    * @param repository A AttributeRepository
    */
    public MultiRepository(AttributeRepository repository) {
        this(new AttributeRepository[] { repository });
    }

    /**
    * This method searches for the given attributes in the repositories provided
    * at construction time.
    *
    * @param DN - the Principal naming the entry in the repositories
    * @param AttributeNames - the names of the attributes to be retrieved; if 
    *   null, all available attributes will be returned
    *
    * @return the requested Attributes
    */
    public Attributes getAttributes(java.security.Principal DN, String[] AttributeNames) throws RepositoryException {
        java.security.Principal dns[] = new java.security.Principal[initialRepositories.length];
        for (int i = 0; i < dns.length; i++) {
            dns[i] = DN;
        }
        return getAttributes(dns, initialRepositories, AttributeNames);
    }

    /**
    * This method will collect attributes from all the entries identified by
    * the TokenLocator. Each TokenLocator in the chain points to a location in
    * a repository; if the repository in it is null, all the repositories 
    * provided at construction time
    * MultiRepository will be searched; otherwise only the repository stated in
    * the TokenLocator
    * will be searched.
    *
    * @param locator - the locator of the entries with the attributes
    * @param AttributeNames - the names of the attributes
    *
    * @return Attributes that are located in all the locations pointed to by the
    *   TokenLocator
    */
    public Attributes getAttributes(TokenLocator locator, String[] AttributeNames) throws RepositoryException {
        Vector dns = new Vector();
        Vector reps = new Vector();
        while (locator != null) {
            dns.add(locator.getLocator());
            AttributeRepository r = locator.getRepository();
            reps.add(r == null ? this : r);
            locator = locator.getAlternativeLocator();
        }
        return getAttributes((Principal[]) dns.toArray(new Principal[0]), (AttributeRepository[]) reps.toArray(new AttributeRepository[0]), AttributeNames);
    }

    /**
    * This is method searches for a single attribute. This is a shortcut for
    * getAttributes(locator, new String[]{AttributeName}).get(AttributeName);
    *
    * @param locator - the TokenLocator identifying multiple locations of the
    *   attribute
    * @param AttributeName - the name of the attribute to retrieve
    *
    * @return the requested Attribute
    */
    public Attribute getAttribute(TokenLocator locator, String AttributeName) throws RepositoryException {
        String[] AttributeNames = new String[] { AttributeName };
        Attributes attrs = getAttributes(locator, AttributeNames);
        return (attrs == null) ? null : (attrs.get(AttributeName));
    }

    /**
    * This is the root method called by any other getAttributes that gets the 
    * set of named attributes from the entries with the DNs.
    * It searches all provided repositories simultaneously. If the DN and named 
    * attribute
    * exist in more than one of the named contexts, then multiple attribute
    * values will be returned.
    *
    * <p>The list of entry names DN is synchronised with the list of 
    * repositories so that each entry name is used only for the corresponding
    * repository, but the whole list of attributes is requested from each
    * repository. When other getAttributes methods use this method, they simply
    * construct such synchronous lists of entry names and repositories. These 
    * methods do
    * not update the status or diagnosis set by this method, and they propagate
    * the exceptions thrown by this method.
    *
    * <p>Effectively, this method constructs a new MultiThreadSearch and invokes
    * a getAttributes method on it. Because this may cause recursive invocation
    * of this method from different threads, this method is NOT synchronized. 
    * For example,
    * a search for attributes in two chained TokenLocators with no repository
    * specified constructs one MultiThreadSearch, which spawns two threads, each 
    * attempting to invoke getAttributes
    * method on this very MultiRepository object; this should be allowed (no
    * mutex on the method), and in its turn creates two other MultiThreadSearches,
    * each spawning one thread for each initial repository passed to 
    * the MultiRepository at construction time. The side effect of this is that
    * during the call to getAttributes the status and diagnosis values are 
    * undetermined, since the calls to multiple MultiThreadSearch will 
    * temporarily set
    * the status and diagnosis of this MultiRepository, until the recursion 
    * unwinds back, and the MultiThreadSearch at the root of this recursion will
    * set the ultimate diagnosis and status of invocation of getAttributes
    * method. Note also that if the getAttributes method on the corresponding
    * repositories is synchronized, this will only slow down the multithreaded
    * search, but will not block the process (unless by some bad design they
    * use the same MultiRepository that invoked them).
    *
    * <p>After calling the method the repository will be set into one of the
    * states: FAILURE_STATUS, SUCCESS_STATUS or PARTIAL_SUCCESS_STATUS. Failure
    * means there were no roots that succeeded. Success means that all of the
    * roots succeeded (the entries were found and some or no attributes were
    * retrieved). Partial success means that some of the roots failed, but some
    * have succeeded, which may be in case some of the roots do not contain
    * the required entry. The caller must find out himself what the cause is, and
    * decide if the results are sufficiently successful.
    *
    * @param DN The distinguished names of the entry from which the attributes
    *    are requested
    * @param repositories The repositories to be searched; must have the same number 
    *    of elements, as DN; each DN must have a non-null repository corresponding
    * @param AttributeNames The array of LDAP names for the attributes; can be
    *    null, if all available attributes and their values are to be retrieved
    *
    * @return the requested attributes; the Repository status reflects the status
    *    of retrieval, the diagnosis contains exceptions the underlying objects
    *    threw, if they failed
    *
    * @throws RepositoryException, if all of the repositories failed, in which case the
    *    embedded exception will be the Throwable returned by <code>getDiagnosis</code>
    *    method; FAILURE_STATUS will also be set
    *
    * @see MultiThreadSearch#getAttributes
    */
    protected Attributes getAttributes(java.security.Principal[] DN, AttributeRepository[] repositories, String[] AttributeNames) throws RepositoryException {
        MultiThreadSearch ms = new MultiThreadSearch();
        try {
            logger.debug("get attributes based on ");
            for (int i = 0; i < DN.length; i++) logger.debug("dn = " + DN[i].getName());
            for (int i = 0; i < repositories.length; i++) logger.debug("repository = " + repositories[i].getClass().getName());
            for (int i = 0; i < AttributeNames.length; i++) logger.debug("attribute name = " + AttributeNames[i]);
            return ms.getAttributes(DN, repositories, AttributeNames);
        } finally {
            mts = ms;
        }
    }

    /**
    * This method returns the status of the repository. It is set when returning
    * attributes.
    *
    * @return the integer value corresponding to the status
    *
    * @see getAttributes(java.security.Principal,String[])
    */
    public int getStatus() {
        return mts.status;
    }

    /**
    * This method returns the Throwable, representing the error, or null, if no
    * error has been encountered (only if the repository is in SUCCESS_STATUS).
    * The Throwable contains an error message and the stack trace of the error.
    *
    * @return Throwable object, representing the error, or null, if no error occured
    */
    public Throwable getDiagnosis() {
        return mts.diagnosis;
    }

    /**
    * This method returns the array of repositories used by the MultiRepository
    * by default (when TokenLocators do not refer to a specific repository).
    *
    * @return the array of AttributeRespositories used by MultiRepository by
    *   default; is never null, does not contain null entries, but may be empty
    *   (zero length)
    */
    public AttributeRepository[] getRepositories() {
        return initialRepositories;
    }
}
