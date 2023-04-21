package uima.consumers;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Iterator;
import uima.types.Interesting;
import uima.types.NewsItem;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

/**
 * @author pon3
 * Stores interesting scores in the database
 */
public class InterestingConsumer extends AbstractDatabaseConsumer {

    PreparedStatement insertScore, checkScore;

    protected String evalDateString;

    protected Date evalDate;

    protected String interestingTable = "interesting";

    public void initialize() throws ResourceInitializationException {
        super.initialize();
        interestingTable += tablePostfix;
        tableToImport = interestingTable;
    }

    public void processCas(CAS arg0) throws ResourceProcessException {
        FSIndex interestingIndex = null;
        NewsItem newsItem = null;
        try {
            FSIndex newsItemIndex = arg0.getJCas().getJFSIndexRepository().getAnnotationIndex(NewsItem.type);
            if (!newsItemIndex.iterator().hasNext()) return;
            interestingIndex = arg0.getJCas().getJFSIndexRepository().getAnnotationIndex(Interesting.type);
            newsItem = (NewsItem) newsItemIndex.iterator().next();
        } catch (CASException e) {
            e.printStackTrace();
        }
        for (Iterator itr = interestingIndex.iterator(); itr.hasNext(); ) {
            Interesting interesting = (Interesting) itr.next();
            writeToFile(interesting.getThresholdID(), newsItem.getDocID(), interesting.getInteresting());
        }
    }
}
