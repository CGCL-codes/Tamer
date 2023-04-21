package au.edu.uq.itee.eresearch.dimer.migrate;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import au.edu.uq.itee.eresearch.dimer.core.util.PubmedDownloader;

public class Migration013 extends Migration {

    private static final Logger log = LoggerFactory.getLogger(Migration013.class);

    public Migration013(Session session) {
        super(session);
    }

    @Override
    public void migrateContent() throws Exception {
        Node citations = session.getRootNode().getNode("citations");
        NodeIterator citationNodes = citations.getNodes();
        while (citationNodes.hasNext()) {
            Node citation = citationNodes.nextNode();
            final String pubmedID = citation.getProperty("pubmedID").getString();
            try {
                PubmedDownloader.Result result = PubmedDownloader.download(pubmedID);
                citation.setProperty("title", result.title);
                citation.setProperty("description", result.abstractText);
                citation.setProperty("authors", result.authors.toArray(new String[] {}));
                citation.setProperty("journalTitle", result.journalTitle);
                citation.setProperty("journalVolume", result.journalVolume);
                citation.setProperty("journalIssue", result.journalIssue);
                citation.setProperty("journalPubDate", result.journalPubDate);
                citation.setProperty("bookTitle", result.bookTitle);
                citation.setProperty("bookPublisher", result.bookPublisher);
                citation.setProperty("bookPubDate", result.bookPubDate);
                log.info("Downloaded PubMed information for {}.", pubmedID);
            } catch (Exception e) {
                log.error("Could not download PubMed information for " + pubmedID + ".", e);
            }
        }
    }
}
