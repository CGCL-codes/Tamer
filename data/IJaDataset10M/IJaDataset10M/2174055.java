package de.fmf.lucene;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.lucene.document.Document;
import de.fmf.io.FileUtil;

public class Search {

    private static File testIndex1Dir = new File("d:\\index_common_orisa_all");

    private File testData1 = new File("d:\\wikidata\\");

    public static void main(String[] args) {
        new Search();
    }

    public Search() {
        createIndex();
        searchForName("*.pdf");
    }

    private void createIndex() {
        Indexer id = new Indexer();
        id.indexFolder(testData1, testIndex1Dir);
    }

    private void searchForName(String search) {
        IndSearcher ins = new IndSearcher(testIndex1Dir);
        ArrayList<SearchResult> results = ins.search(search, ISearch.NAME);
        for (Iterator<SearchResult> iterator = results.iterator(); iterator.hasNext(); ) {
            SearchResult sRes = iterator.next();
            Document doc = sRes.getDocument();
            System.out.println(doc.get("path") + "\thitcount:\t" + sRes.getScore());
        }
    }

    private void searchForContent(String search) {
        IndSearcher ins = new IndSearcher(testIndex1Dir);
        ArrayList<SearchResult> results = ins.search(search, ISearch.CONTENTS);
        for (Iterator<SearchResult> iterator = results.iterator(); iterator.hasNext(); ) {
            SearchResult sRes = iterator.next();
            Document doc = sRes.getDocument();
            System.out.println(doc.get("path") + "\thitcount:\t" + sRes.getScore());
        }
    }

    private void searchForPath(String search) {
        IndSearcher ins = new IndSearcher(testIndex1Dir);
        ArrayList<SearchResult> results = ins.search(search, ISearch.PATH);
        for (Iterator<SearchResult> iterator = results.iterator(); iterator.hasNext(); ) {
            SearchResult sRes = iterator.next();
            Document doc = sRes.getDocument();
            System.out.println(doc.get("path") + "\thitcount:\t" + sRes.getScore());
        }
    }
}
