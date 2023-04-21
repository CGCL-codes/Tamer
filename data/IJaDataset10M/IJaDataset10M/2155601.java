package oldstuff;

import java.util.Collection;
import ontology.Constants;
import util.IOUtil;
import datastructures.DocumentCollection;
import extraction.Stopwords;

public class TestDocumentIndexer {

    public static void main(String[] args) {
        DocumentCollection docColl = IOUtil.loadDocumentCollection(Constants.articlePath);
        Collection<String> interestingwords = oldstuff.DocumentIndexer.detectInterestingWords(docColl, 0.5f);
        extraction.Stopwords sw = new extraction.Stopwords(docColl, Stopwords.DetectionStrategy.DocumentOccurence);
        interestingwords = sw.removeStopwords(interestingwords);
        System.out.println("Interesting words:");
        System.out.println(util.CollectionUtil.collectionToString(interestingwords));
    }
}
