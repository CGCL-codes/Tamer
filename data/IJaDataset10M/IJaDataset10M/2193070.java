package intact.exercise.chapter5.exercise1;

import org.hupo.psi.mi.psicquic.wsclient.UniversalPsicquicClient;
import psidev.psi.mi.search.SearchResult;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.tab.model.CrossReference;
import java.util.Collection;

/**
 * Question 2: Access with SOAP to PSICQUIC services has a hard limit of 200 interactions per query.
 * Could you write some code to get all the interactions for pubmed 16189514 from IntAct,
 * which contains more than 2700 interactions?
 *
 * @see org.hupo.psi.mi.psicquic.wsclient.UniversalPsicquicClient
 * @see org.hupo.psi.mi.psicquic.wsclient.UniversalPsicquicClient#getByQuery(String, int, int)
 * @see psidev.psi.mi.search.SearchResult
 * @see psidev.psi.mi.search.SearchResult#getTotalCount()
 * @see psidev.psi.mi.tab.model.BinaryInteraction
 * @see psidev.psi.mi.tab.model.Interactor
 * @see psidev.psi.mi.tab.model.Interactor#getIdentifiers()
 * @see psidev.psi.mi.tab.model.CrossReference
 */
public class Q2_ProcessLargeDatasets {

    public static void main(String[] args) throws Exception {
        String miqlQuery = "pubid:16189514";
        String soapServiceAddress = "http://www.ebi.ac.uk/Tools/webservices/psicquic/intact/webservices/psicquic";
        UniversalPsicquicClient client = new UniversalPsicquicClient(soapServiceAddress);
        SearchResult<BinaryInteraction> results;
        System.out.println("Total results: " + client.getByQuery(miqlQuery, 0, 0).getTotalCount());
        int firstResult = 0;
        final int maxResults = 200;
    }

    private static String getFirstIdentifier(Collection<CrossReference> identifiers) {
        return null;
    }
}
