package fr.crim.lucene.alix.doc;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import fr.crim.lucene.alix.AlixDoc;

/**
 * Lucene implementation of Lucene doc
 * 
 * @author Pierre DITTGEN
 */
public class AlixLuceneDoc implements AlixDoc {

    private static String OPTIONS = "A";

    private String idField;

    private Document doc;

    private IndexWriter writer;

    /**
	 * Constructor. 
	 * @param writer for indexing
	 */
    public AlixLuceneDoc(String idField, IndexWriter writer) {
        this.idField = idField;
        this.writer = writer;
        doc = new Document();
    }

    @Override
    public void addField(String name, String value, String options, float boost) {
        if (options == null || "".equals(options)) options = OPTIONS;
        Store store = Store.NO;
        if (options.contains("S")) store = Store.YES;
        Index index;
        if (!options.contains("I")) index = Index.NO; else if (options.contains("T")) {
            if (options.contains("N")) index = Index.ANALYZED; else index = Index.ANALYZED_NO_NORMS;
        } else {
            if (options.contains("N")) index = Index.NOT_ANALYZED; else index = Index.NOT_ANALYZED_NO_NORMS;
        }
        TermVector tv = TermVector.NO;
        if (options.contains("V")) tv = TermVector.YES;
        if (options.contains("o")) tv = TermVector.WITH_OFFSETS;
        if (options.contains("p")) tv = TermVector.WITH_POSITIONS;
        if (options.contains("o") && options.contains("p")) tv = TermVector.WITH_POSITIONS_OFFSETS;
        Field field = new Field(name, value, store, index, tv);
        if (boost != 0) field.setBoost(boost);
        doc.add(field);
    }

    @Override
    public void writeDocument() throws IOException {
        if (idField == null) {
            writer.addDocument(doc);
            return;
        }
        String[] ids = doc.getValues(idField);
        if (ids.length == 0) {
            System.err.println("Id field [" + idField + "] isn't set");
        }
        writer.updateDocument(new Term(idField, ids[0]), doc);
    }
}
