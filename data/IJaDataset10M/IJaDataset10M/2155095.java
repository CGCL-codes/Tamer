package br.com.caelum.jambo.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;
import br.com.caelum.jambo.model.Curriculum;
import br.com.caelum.jambo.model.User;
import br.com.caelum.jambo.util.ResourceLocator;

public class UserSearch {

    private static Logger logger = Logger.getLogger(UserSearch.class);

    private final SearchEngine engine;

    private final ResourceLocator locator;

    public UserSearch(SearchEngine engine, ResourceLocator locator) {
        this.engine = engine;
        this.locator = locator;
    }

    public void add(User user) throws IOException {
        if (user.getCurriculum() == null) {
            add(user, null);
        } else {
            File curriculum = locator.getResourceFromRelativePath(Curriculum.UPLOADED_FILES_PATH + '/' + user.getCurriculum().getInternalName());
            add(user, curriculum);
        }
    }

    public void add(User user, File curriculum) throws IOException {
        delete(user);
        Document document = new Document();
        document.add(new Field("name", user.getName(), Field.Store.NO, Field.Index.TOKENIZED));
        document.add(new Field("id", user.getId().toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        document.add(new Field("type", User.class.getName(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        if (curriculum != null) {
            PDDocument curriculumPD = PDDocument.load(curriculum);
            PDFTextStripper stripper = new PDFTextStripper();
            String curriculumText = stripper.getText(curriculumPD);
            curriculumPD.close();
            Field curriculumField = new Field("curriculum", curriculumText, Field.Store.NO, Field.Index.TOKENIZED);
            document.add(curriculumField);
            logger.info("Indexing curriculum : " + curriculumText.substring(0, Math.min(400, curriculumText.length())));
        }
        engine.write(document);
    }

    public void delete(User user) throws IOException {
        Query q1 = new TermQuery(new Term("type", User.class.getName()));
        Query q2 = new TermQuery(new Term("id", user.getId().toString()));
        BooleanQuery q = new BooleanQuery();
        q.add(q1, BooleanClause.Occur.MUST);
        q.add(q2, BooleanClause.Occur.MUST);
        Hits hits = engine.search(q);
        if (hits.length() > 1) {
            logger.fatal("total topic documents with id " + user.getId() + ": " + hits.length());
            return;
        }
        for (int i = 0; i < hits.length(); i++) {
            engine.delete(hits.id(i));
        }
    }

    public List<Long> searchCurriculum(String string, int begin, int results) throws IOException, ParseException {
        List<Long> list = new ArrayList<Long>();
        Hits hits = hitsCurriculum(string);
        results = results == 0 ? hits.length() : results;
        results = Math.min(results, hits.length() - begin);
        for (int i = begin; i < begin + results; i++) {
            Document d = hits.doc(i);
            Long id = Long.valueOf(d.get("id"));
            list.add(id);
        }
        return list;
    }

    public int numberOfHitsCurriculum(String string) throws ParseException, IOException {
        return hitsCurriculum(string).length();
    }

    private Hits hitsCurriculum(String string) throws ParseException, IOException {
        Query q1 = new TermQuery(new Term("type", User.class.getName()));
        Query q2 = new QueryParser("curriculum", new JamboAnalyzer()).parse(string);
        BooleanQuery q = new BooleanQuery();
        q.add(q1, BooleanClause.Occur.MUST);
        q.add(q2, BooleanClause.Occur.MUST);
        Hits hits = engine.search(q);
        return hits;
    }

    public List<Long> searchName(String string, int begin, int results) throws IOException, ParseException {
        List<Long> list = new ArrayList<Long>();
        Hits hits = hitsName(string);
        results = results == 0 ? hits.length() : results;
        results = Math.min(results, hits.length() - begin);
        for (int i = begin; i < begin + results; i++) {
            Document d = hits.doc(i);
            Long id = Long.valueOf(d.get("id"));
            list.add(id);
        }
        return list;
    }

    public int numberOfHitsName(String string) throws ParseException, IOException {
        return hitsName(string).length();
    }

    private Hits hitsName(String string) throws ParseException, IOException {
        Query q1 = new TermQuery(new Term("type", User.class.getName()));
        Query q2 = new QueryParser("name", new JamboAnalyzer()).parse(string);
        BooleanQuery q = new BooleanQuery();
        q.add(q1, BooleanClause.Occur.MUST);
        q.add(q2, BooleanClause.Occur.MUST);
        Hits hits = engine.search(q);
        return hits;
    }
}
