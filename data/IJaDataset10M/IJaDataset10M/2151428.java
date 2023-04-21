package saadadb.vo.request.formator;

import java.util.ArrayList;
import java.util.Map;
import adqlParser.SaadaADQLQuery;
import saadadb.exceptions.QueryException;
import saadadb.query.result.ADQLResultSet;
import saadadb.query.result.SaadaInstanceResultSet;

/**
 * This is a simple interface with the very common services a formator must support.
 * @author laurent
 * * @version $Id: Formator.java 118 2012-01-06 14:33:51Z laurent.mistahl $

 * @version 06/2011 creation
 */
public interface Formator {

    /**
	 * Ask for adding extensions with associated data
	 * @param relationName  
	 * @throws QueryException if the relation does not exist or if this feature is not supported by the formator
	 */
    public void includeRelationInResponse(String relationName) throws QueryException;

    /**
	 * Return true if the formator can build response with linked data
	 * @return
	 */
    public boolean supportResponseInRelation();

    /**
	 * Provide the formator with the list of OIDs from which the reponse must be built
	 * This mode require one query per oid: usually OK except for entries
	 * @param resultSet
	 * @throws QueryException 
	 */
    public void setResultSet(ArrayList<Long> oids) throws QueryException;

    /**
	 * Provide the formator with the "allcolumn" Saada result SET
	 * Must be used for large collections (e.g. ENTRY)
	 * @param saadaInstanceResultSet
	 * @throws QueryException
	 */
    public void setResultSet(SaadaInstanceResultSet saadaInstanceResultSet) throws QueryException;

    /**	 
	 *  Provide the formator with an ADQL result set
	 * Only used for ADQL formators, does nothing orherwise
	 * @param adqlResultSet
	 */
    public void setAdqlResultSet(ADQLResultSet adqlResultSet);

    /**
	 *  Provide the formator with an ADQL query 
	 * Only used for ADQL formators, does nothing orherwise
	 * @param adqlQuery
	 */
    public void setAdqlQuery(SaadaADQLQuery adqlQuery);

    /**
	 * Set the map of parameter possibly used by the formator (query pos, frame....)
	 * There is no standard vocabulary for keys. The VORequest is supposed to know what the Formator needs
	 * @param fmtParams parameter map
	 * @throws Exception 
	 */
    public void setProtocolParams(Map<String, String> params) throws Exception;

    /**
	 * Set the map of parameter possibly used by the formator (format) in meta data mode
	 * There is no standard vocabulary for keys. The VORequest is supposed to know what the Formator needs
	 * @param fmtParams parameter map
	 * @throws Exception 
	 */
    public void setProtocolMetaParams(Map<String, String> params) throws Exception;

    /**
	 * Set the directory where the response will be written and build a response file name using the prefix
	 * @param responseDir
	 * @param prefix
	 * @throws Exception
	 */
    public void setResponseFilePath(String responseDir, String prefix) throws Exception;

    /**
	 * @param responseFilePath can either be a filename or path
	 * @throws Exception
	 */
    public void setResponseFilePath(String responseFilePath) throws Exception;

    /**
	 * Do the main job: builds the response
	 * @throws Exception
	 */
    public void buildDataResponse() throws Exception;

    /**
	 * Do the main job: builds the response for meta data query
	 * @throws Exception
	 */
    public void buildMetaResponse() throws Exception;

    /**
	 * Builds an error response
	 * @param e originally of the error
	 * @throws Exception
	 */
    public void buildErrorResponse(Exception e) throws Exception;

    /**
	 * Returns the actual full response path
	 * @return
	 */
    public String getResponseFilePath();

    /**
	 * Returns the limit of the number of data supported by protocol
	 * @return
	 */
    public int getLimit();
}
