package uk.ac.manchester.ac.uk.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.Set;

/**
 * Author: Simon Jupp<br>
 * Date: May 2, 2011<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
@RemoteServiceRelativePath("rpcQuery")
public interface KUPKBQueryService extends RemoteService {

    KUPKBExpData[] query(String queryString);

    KUPKBExpData[] query(String[] queryString);

    KUPKBExpData[] advancedQuery(String[] geneIds, String[] expressionIds, String[] locationIds, String[] conditionIds, String[] processIds, boolean intersecting);

    KUPKBQueryObject[] suggest(String input);

    KUPKBQueryObject[] suggest(String[] input);

    KUPKBOWLClass[] suggestClass(String input);

    KUPKBOWLClass[] suggestClass(String[] input);

    String getNameSpaces();

    String executeTemplateQuery(String templateId, String format);

    String evaluateSparqlQuery(String query, String format);
}
