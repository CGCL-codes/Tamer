package fr.crim.lexique.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.store.FSDirectory;
import fr.crim.lexique.common.Candidate;
import fr.crim.lexique.common.LexiqueOrgReader;

/**
 * Une classe qui sert un lexique stocké dans un index lucene
 * et qui "cache" les résultats en mémoire
 * @author Pierre DITTGEN
 */
public class LexiqueOrgLuceneReader implements LexiqueOrgReader {

    /** Cache. */
    private Hashtable<String, List<Candidate>> cache = new Hashtable<String, List<Candidate>>();

    /** Recherche et clef de tri. */
    private IndexSearcher searcher = null;

    private Sort sort = null;

    /**
	 * Constructeur
	 * @param indexDir le dossier où sont stockés les indexes Lucene
	 * @throws Exception
	 */
    public LexiqueOrgLuceneReader(File indexDir) throws Exception {
        searcher = new IndexSearcher(FSDirectory.open(indexDir));
        sort = new Sort(new SortField("freq", SortField.DOUBLE, true));
    }

    /**
	 * Recherche une liste de lemmes candidats avec leur type à partir d'une
	 * forme passée en paramètre 
	 * @param form La forme à chercher
	 * @return La liste des lemmes
	 */
    public List<Candidate> lookupForm(String form) {
        if (cache.contains(form)) {
            return cache.get(form);
        }
        List<Candidate> results = new ArrayList<Candidate>();
        try {
            TermQuery query = new TermQuery(new Term("forme", form));
            TopFieldDocs hits = searcher.search(query, null, 100, sort);
            int max = hits.totalHits;
            for (int i = 0; i < max; i++) {
                ScoreDoc sdoc = hits.scoreDocs[i];
                Document doc = searcher.doc(sdoc.doc);
                results.add(new Candidate(doc.get("lemme"), doc.get("type")));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return results;
        }
        cache.put(form, results);
        return results;
    }

    /**
	 * Clôt le searcher
	 */
    public void close() {
        try {
            searcher.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
