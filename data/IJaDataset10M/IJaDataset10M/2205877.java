package uk.icat3.sessionbeans;

import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import uk.icat3.entity.Investigation;
import uk.icat3.exceptions.BadParameterException;
import uk.icat3.exceptions.IcatInternalException;
import uk.icat3.exceptions.InsufficientPrivilegesException;
import uk.icat3.exceptions.SessionException;
import uk.icat3.sessionbeans.compatibility.AdvancedSearchDetails;
import uk.icat3.sessionbeans.manager.BeanManagerLocal;

public class ICATCompat extends EJBObject {

    @EJB
    private BeanManagerLocal beanManagerLocal;

    public ICATCompat() {
    }

    private String getIN(List<String> ele) {
        final StringBuilder infield = new StringBuilder("(");
        for (final String t : ele) {
            if (infield.length() != 1) {
                infield.append(',');
            }
            infield.append('\'').append(t).append('\'');
        }
        infield.append(')');
        return infield.toString();
    }

    /**
	 * This gets all the keywords available to the user, they can only see
	 * keywords associated with their investigations or public investigations
	 * 
	 * @param sessionId
	 *            federalId of the user.
	 * @return list of keywords
	 * @throws uk.icat3.exceptions.SessionException
	 * @throws InsufficientPrivilegesException
	 * @throws BadParameterException
	 * @throws IcatInternalException
	 */
    @SuppressWarnings("unchecked")
    @WebMethod
    public List<String> getKeywordsForUser(@WebParam(name = "sessionId") String sessionId) throws SessionException, IcatInternalException, BadParameterException, InsufficientPrivilegesException {
        return (List<String>) this.beanManagerLocal.search(sessionId, "DISTINCT Keyword.name");
    }

    /**
	 * This gets all the keywords available to the user - limited by count
	 * 
	 * @param sessionId
	 *            federalId of the user.
	 * @param numberReturned
	 *            number of results found returned
	 * @return list of keywords
	 * @throws uk.icat3.exceptions.SessionException
	 * @throws InsufficientPrivilegesException
	 * @throws BadParameterException
	 * @throws IcatInternalException
	 */
    @SuppressWarnings("unchecked")
    @WebMethod(operationName = "getKeywordsForUserMax")
    @RequestWrapper(className = "uk.icat3.sessionbeans.jaxws.getKeywordsForUserMax")
    @ResponseWrapper(className = "uk.icat3.sessionbeans.jaxws.getKeywordsForUserMaxResponse")
    public List<String> getKeywordsForUser(@WebParam(name = "sessionId") String sessionId, @WebParam(name = "limit") int limit) throws SessionException, IcatInternalException, BadParameterException, InsufficientPrivilegesException {
        return (List<String>) this.beanManagerLocal.search(sessionId, "0," + limit + " DISTINCT Keyword.name");
    }

    /**
	 * Lists all the investigations for the current user, ie who he is an
	 * investigator of
	 * 
	 * @param sessionId
	 * @throws uk.icat3.exceptions.SessionException
	 *             if the session id is invalid
	 * @return collection
	 * @throws IcatInternalException
	 * @throws InsufficientPrivilegesException
	 * @throws BadParameterException
	 */
    @SuppressWarnings("unchecked")
    @WebMethod
    public List<Investigation> getMyInvestigations(@WebParam(name = "sessionId") String sessionId) throws SessionException, IcatInternalException, BadParameterException, InsufficientPrivilegesException {
        return (List<Investigation>) this.beanManagerLocal.search(sessionId, "Investigation");
    }

    /**
	 * SearchManager by a collection of keywords for investigations that user
	 * has access to view, with AND been operator, fuzzy false, no includes
	 * 
	 * @param sessionId
	 *            sessionId of the user.
	 * @param keywords
	 *            Collection of keywords to search on
	 * @return collection of {@link Investigation} investigation objects
	 * @throws uk.icat3.exceptions.SessionException
	 * @throws IcatInternalException
	 * @throws InsufficientPrivilegesException
	 * @throws BadParameterException
	 */
    @SuppressWarnings("unchecked")
    @WebMethod(operationName = "searchByKeywords")
    public List<Investigation> searchByKeywords(@WebParam(name = "sessionId") String sessionId, @WebParam(name = "keywords") List<String> keywords) throws SessionException, IcatInternalException, BadParameterException, InsufficientPrivilegesException {
        final String query = "DISTINCT Investigation <-> Keyword[name IN " + this.getIN(keywords) + "]";
        return (List<Investigation>) this.beanManagerLocal.search(sessionId, query);
    }

    /**
	 * This searches all DB for investigations with the advanced search criteria
	 * 
	 * @param sessionId
	 *            session id of the user.
	 * @param advancedSearch
	 *            advanced SearchManager details to search with
	 * @throws uk.icat3.exceptions.SessionException
	 *             if the session id is invalid
	 * @return collection of {@link Investigation} investigation objects
	 * @throws IcatInternalException
	 * @throws InsufficientPrivilegesException
	 * @throws BadParameterException
	 */
    @SuppressWarnings("unchecked")
    @WebMethod
    public List<Investigation> searchByAdvanced(@WebParam(name = "sessionId") String sessionId, @WebParam(name = "advancedSearchDetails") AdvancedSearchDetails advancedSearch) throws SessionException, IcatInternalException, BadParameterException, InsufficientPrivilegesException {
        final StringBuilder query = new StringBuilder();
        if (advancedSearch.hasExperimentNumber()) {
            augmentQuery(query, "name", advancedSearch.getExperimentNumber());
        }
        if (advancedSearch.hasInvestigationType()) {
            augmentQuery(query, "type.name", advancedSearch.getInvestigationType());
        }
        if (query.length() == 0) {
            query.append("DISTINCT Investigation");
        } else {
            query.append("]");
        }
        return (List<Investigation>) this.beanManagerLocal.search(sessionId, query.toString());
    }

    private void augmentQuery(StringBuilder query, String field, String value) {
        if (query.length() == 0) {
            query.append("DISTINCT Investigation [");
        } else {
            query.append(" AND ");
        }
        query.append(field + " = '" + value + "'");
    }
}
