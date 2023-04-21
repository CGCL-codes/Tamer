package uk.ac.ebi.rhea.ws.core.response;

import java.io.File;
import java.util.List;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.webapp.pub.search.SearchBean;
import uk.ac.ebi.rhea.ws.core.exception.RheaResponseException;
import uk.ac.ebi.rhea.ws.core.exception.RheasResponseException;
import uk.ac.ebi.rhea.ws.request.RheaWsRequest;

/**
 * An interface defining methods for creating a response for Rhea Search
 * request.
 *
 * @author <a href="mailto:hongcao@ebi.ac.uk">Hong Cao</a>
 * @since 1.0
 * @version $LastChangedRevision: 1315 $ <br/>
 *          $LastChangedDate: 2011-02-10 11:38:10 -0500 (Thu, 10 Feb 2011) $
 *          $Author: hongcao $
 */
public interface IRheasWsResponse {

    /**
     * Creates response file for a search request based on a collection of
     * search results and a search request. This method should only be used when
     * the search process has been done and the results are ready to be
     * converted to a response file.
     * @param searchResults The collection of search results to be converted
     * to a response file.
     * @param rheasRequest The search request object containing the request
     * parameters and other necessary data to create the response.
     * @return A response file
     * @throws RheasRequestProcessingException
     */
    public File create(Object[] searchResults, RheaWsRequest rheasRequest) throws RheasResponseException;

    /**
     * Creates response file for a search request based on a search bean and a
     * search request. The method perform the search and convert the results to
     * a response file.
     * @param searchBean The search bean which has search methods to perform
     * the search.
     * @param rheasRequest The search request object containing the request
     * parameters and other necessary data to create the response.
     * @return A response file
     * @throws RheasRequestProcessingException
     */
    public File create(SearchBean searchBean, RheaWsRequest rheasRequest) throws RheasResponseException;

    /**
     * Creates response file for a search request based on a reaction list and a
     * search request. This method should be used to create response that contains
     * a list of reactions in detail.
     * a response file
     * @param reactionList The list of reactions
     * @param rheasRequest The search request object containing the request
     * parameters and other necessary data to create the response.
     * @return A response file
     * @throws RheaRequestProcessingException
     */
    public File create(List<Reaction> reactionList, RheaWsRequest rheasRequest) throws RheaResponseException;
}
