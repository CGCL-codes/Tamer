package uk.ac.ebi.intact.kickstart;

import psidev.psi.mi.search.SearchResult;
import uk.ac.ebi.intact.psicquic.wsclient.MitabPsicquicClient;
import uk.ac.ebi.intact.psimitab.IntactBinaryInteraction;

/**
 * This example does not need the database to work and shows how to access the EBI IntAct database
 * remotely.
 *
 */
public class RemotePsicquicSearch {

    public static void main(String[] args) throws Exception {
        MitabPsicquicClient client = new MitabPsicquicClient("http://www.ebi.ac.uk/intact/psicquic/webservices/psicquic");
        SearchResult<IntactBinaryInteraction> result = client.getByInteractor("brca2", 0, 50);
        System.out.println("Interactions found: " + result.getTotalCount());
        for (IntactBinaryInteraction binaryInteraction : result.getData()) {
            String interactorIdA = binaryInteraction.getInteractorA().getIdentifiers().iterator().next().getIdentifier();
            String interactorIdB = binaryInteraction.getInteractorB().getIdentifiers().iterator().next().getIdentifier();
            String interactionAc = binaryInteraction.getInteractionAcs().iterator().next().getIdentifier();
            System.out.println("\tInteraction (" + interactionAc + "): " + interactorIdA + " interacts with " + interactorIdB);
        }
    }
}
