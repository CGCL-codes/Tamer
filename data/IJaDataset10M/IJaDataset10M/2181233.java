package uk.ac.ebi.intact.psicquic.wsclient;

import org.junit.Test;
import org.junit.Ignore;
import uk.ac.ebi.intact.psimitab.IntactBinaryInteraction;
import psidev.psi.mi.search.SearchResult;
import psidev.psi.mi.xml.model.Entry;
import psidev.psi.mi.xml.model.Interaction;

/**
 * TODO comment that class header
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: XmlPsicquicClientTest.java 13030 2009-04-16 11:53:26Z baranda $
 */
public class XmlPsicquicClientTest {

    @Test
    public void client() throws Exception {
        XmlPsicquicClient client = new XmlPsicquicClient("http://www.ebi.ac.uk/tc-test/intact/psicquic/webservices/psicquic");
        XmlSearchResult searchResult = client.getByInteractor("brca2", 0, 50);
        for (Entry entry : searchResult.getEntrySet().getEntries()) {
            for (Interaction interaction : entry.getInteractions()) {
                System.out.println(interaction);
            }
        }
    }
}
