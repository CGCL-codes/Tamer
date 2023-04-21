package org.apache.lucene.index;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import pt.utl.ist.lucene.Globals;
import pt.utl.ist.lucene.LgteIndexTreeIdMapper;
import pt.utl.ist.lucene.Model;
import pt.utl.ist.lucene.utils.IDataCacher;
import pt.utl.ist.lucene.utils.StringComparator;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Jorge Machado
 * @date 16/Dez/2009
 * @time 14:19:07
 * @email machadofisher@gmail.com
 */
public class LgteIsolatedIndexReader extends ProbabilisticIndexReader {

    LgteIndexTreeIdMapper lgteIndexTreeIdMapper = null;

    Readers readers = new Readers();

    private class Readers {

        private Map<String, IndexReader> readers = new HashMap<String, IndexReader>();

        IndexReader[] readersArray;

        List<IndexReader> readerses;

        List<Map.Entry<String, IndexReader>> sortedExpressions;

        private Readers() {
        }

        public IndexReader[] getReadersArray() {
            return readersArray;
        }

        public Collection<IndexReader> getReaders() {
            return readerses;
        }

        public Map<String, IndexReader> getReadersMap() {
            return readers;
        }

        public void setReaders(Map<String, IndexReader> readers) {
            this.readers = readers;
            readerses = new ArrayList<IndexReader>();
            for (IndexReader reader : readers.values()) {
                if (!readerses.contains(reader)) readerses.add(reader);
            }
            sortedExpressions = new ArrayList<Map.Entry<String, IndexReader>>(readers.entrySet());
            Collections.sort(sortedExpressions, new Comparator<Map.Entry<String, IndexReader>>() {

                public int compare(Map.Entry<String, IndexReader> o1, Map.Entry<String, IndexReader> o2) {
                    int compare = o2.getKey().length() - o1.getKey().length();
                    if (compare > 0) return 1; else if (compare < 0) return -1; else return 0;
                }
            });
            readersArray = new IndexReader[readerses.size()];
            for (int i = 0; i < readersArray.length; i++) readersArray[i] = readerses.get(i);
        }

        public IndexReader getReader(String field) {
            IndexReader reader = readers.get(field);
            if (reader == null) {
                for (Map.Entry<String, IndexReader> entry : sortedExpressions) {
                    if (entry.getKey().matches("regexpr(.*)")) {
                        String regExpr = entry.getKey().substring("regexpr(".length(), entry.getKey().length() - 1);
                        if (field.matches(regExpr)) {
                            reader = entry.getValue();
                            readers.put(field, reader);
                            break;
                        }
                    }
                }
                if (reader == null) reader = readers.get("*");
            }
            return reader;
        }

        public Collection<String> getFields() {
            return readers.keySet();
        }
    }

    /**
     * Constructor used if IndexReader is not owner of its directory.
     * This is used for IndexReaders that are used within other IndexReaders that take care or locking directories.
     *
     * @param path of index
     * @param model model
     * @throws java.io.IOException on open
     */
    public LgteIsolatedIndexReader(String path, Model model) throws IOException {
        super(null);
        List<String> fields = new ArrayList<String>();
        File f = new File(path);
        for (File d : f.listFiles()) {
            if (d.isDirectory() && d.getName().endsWith("_field")) {
                fields.add(d.getName().substring(0, d.getName().indexOf("_field")));
            }
        }
        init(path, fields, model);
    }

    /**
     * Constructor used if IndexReader is not owner of its directory.
     * This is used for IndexReaders that are used within other IndexReaders that take care or locking directories.
     *
     * @param paths Pair<FieldName, Path> - The field name can be a regular expression
     * @param model model
     * @throws java.io.IOException on open
     */
    public LgteIsolatedIndexReader(Map<String, String> paths, Model model) throws IOException {
        super(null);
        init(paths, model);
    }

    public LgteIsolatedIndexReader(Map<String, IndexReader> readers) {
        super(null);
        this.readers.setReaders(readers);
    }

    private static final Logger logger = Logger.getLogger(LgteIsolatedIndexReader.class);

    public int[] translateId(int doc, String field) {
        if (lgteIndexTreeIdMapper == null) {
            logger.error("translate request but there are Zero mappings configuration: see LgteIsolatedIndexReader.addMapping method");
            return null;
        }
        return lgteIndexTreeIdMapper.translateId(doc, field);
    }

    public boolean hasMapping(String field) {
        return lgteIndexTreeIdMapper != null && lgteIndexTreeIdMapper.hasMapping(field);
    }

    /**
     *  example:
     *    contents > sentences (doc_id)
     *
     *    Fields contents and sentences using two diferente indexes with a diferent set of id's
     *    Restriction: The Indexes should be created with the same sequence of DOCS.
     *    contents:  DOC_1                    DOC_2            DOC_3
     *    sentences: DOC_1_1 DOC_1_2 DOC_1_3  DOC_2_1 DOC_2_3  DOC_3_1 DOC_3_2
     *
     *    Using this configuration the query:  sentences:(w1 w2) contents(w1 w2)
     *    will map the scores from documents to sentences in order to show a resultSet in sentences space
     *
     * @param parent parent Index
     * @param child child Index
     * @param foreignKeyFieldChild2Parent field in child with the foreignkey to id field in parent
     *
     */
    public void addTreeMapping(IndexReader parent, IndexReader child, String foreignKeyFieldChild2Parent) {
        if (readers.getReader(foreignKeyFieldChild2Parent) == null) throw new RuntimeException("addTreeMapping: can't add a foreign key that is not define in readers map");
        if (lgteIndexTreeIdMapper == null) lgteIndexTreeIdMapper = new LgteIndexTreeIdMapper(this);
        lgteIndexTreeIdMapper.addMapping(parent, child, foreignKeyFieldChild2Parent);
    }

    public void addTreeMapping(IndexReader parent, IndexReader child, IndexReader existentParent, String foreignKeyFieldChild2Parent) {
        if (readers.getReader(foreignKeyFieldChild2Parent) == null) throw new RuntimeException("addTreeMapping: can't add a foreign key that is not define in readers map");
        if (lgteIndexTreeIdMapper == null) lgteIndexTreeIdMapper = new LgteIndexTreeIdMapper(this);
        lgteIndexTreeIdMapper.addMappingUseOtherMappingOffsets(parent, child, existentParent, foreignKeyFieldChild2Parent);
    }

    public int getRealDoc(int docNumber, String field) {
        IndexReader subReader = readers.getReader(field);
        if (lgteIndexTreeIdMapper != null && !lgteIndexTreeIdMapper.isChild(subReader)) {
            docNumber = lgteIndexTreeIdMapper.translateIdInverted(docNumber);
        }
        return docNumber;
    }

    public IndexReader getIndexReader(String field) {
        return readers.getReader(field);
    }

    private void init(String path, List<String> fields, Model model) throws IOException {
        Map<String, IndexReader> readers = new HashMap<String, IndexReader>(fields.size());
        for (String field : fields) {
            if (model.isProbabilistcModel()) {
                IndexReader reader = new LanguageModelIndexReader(IndexReader.open(path + File.separator + field + "_field"));
                readers.put(field, reader);
            } else if (model == Model.VectorSpaceModel) {
                IndexReader reader = IndexReader.open(path + File.separator + field + "_field");
                readers.put(field, reader);
            } else {
                System.err.println("Unknown retrieval model: " + model);
                throw new IllegalArgumentException();
            }
        }
        this.readers.setReaders(readers);
    }

    private void init(Map<String, String> paths, Model model) throws IOException {
        Map<String, IndexReader> readers = new HashMap<String, IndexReader>(paths.size());
        for (Map.Entry<String, String> path : paths.entrySet()) {
            if (model.isProbabilistcModel()) {
                IndexReader reader = new LanguageModelIndexReader(IndexReader.open(path.getValue()));
                readers.put(path.getKey(), reader);
            } else if (model == Model.VectorSpaceModel) {
                IndexReader reader = IndexReader.open(path.getValue());
                readers.put(path.getKey(), reader);
            } else {
                System.err.println("Unknown retrieval model: " + model);
                throw new IllegalArgumentException();
            }
        }
        this.readers.setReaders(readers);
    }

    /**
     * Prbobailistic Reader Interface
     * @param field
     * @return
     * @throws IOException
     */
    public int getCollectionTokenNumber(String field) throws IOException {
        IndexReader reader = readers.getReader(field);
        if (reader != null && reader instanceof ProbabilisticIndexReader) {
            return ((ProbabilisticIndexReader) reader).getCollectionTokenNumber(field);
        }
        throw new NotImplemented("getCollectionSize only implemented for probabilistic models");
    }

    /**
     * Prbobailistic Reader Interface
     * @param field
     * @return
     * @throws IOException
     */
    public double getAvgLenTokenNumber(String field) throws IOException {
        IndexReader reader = readers.getReader(field);
        if (reader != null && reader instanceof ProbabilisticIndexReader) {
            return ((ProbabilisticIndexReader) reader).getAvgLenTokenNumber(field);
        }
        throw new NotImplemented("getAvgLenTokenNumber only implemented for probabilistic models");
    }

    public int collFreq(Term t) throws IOException {
        IndexReader reader = readers.getReader(t.field());
        if (reader != null && reader instanceof ProbabilisticIndexReader) {
            return ((ProbabilisticIndexReader) reader).collFreq(t);
        }
        throw new NotImplemented("getAvgLenTokenNumber only implemented for probabilistic models");
    }

    /**
     * Prbobailistic Reader Interface
     * @return
     * @throws IOException
     */
    public int getCollectionSize() throws IOException {
        int colectionTokenNumber = 0;
        for (IndexReader reader : readers.getReaders()) {
            if (reader != null && reader instanceof ProbabilisticIndexReader) {
                colectionTokenNumber += ((ProbabilisticIndexReader) reader).getCollectionSize();
            }
        }
        return colectionTokenNumber;
    }

    /** Return an array of term frequency vectors for the specified document.
     *  The array contains a vector for each vectorized field in the document.
     *  Each vector vector contains term numbers and frequencies for all terms
     *  in a given vectorized field.
     *  If no such fields existed, the method returns null.
     *
     * All indexes have all documents
     *
     * @author Jorge Machado
     * todo Need optimization
     * todo need debug
     */
    public TermFreqVector[] getTermFreqVectors(int n) throws IOException {
        HashMap<String, HashMap<String, Integer>> map = new HashMap<String, HashMap<String, Integer>>(0);
        for (IndexReader subReader : readers.getReaders()) {
            TermFreqVector[] vectors = subReader.getTermFreqVectors(n);
            for (TermFreqVector vector : vectors) {
                HashMap<String, Integer> field = map.get(vector.getField());
                if (field == null) {
                    field = new HashMap<String, Integer>();
                    map.put(vector.getField(), field);
                }
                for (int i = 0; i < vector.getTerms().length; i++) {
                    String term = vector.getTerms()[i];
                    Integer freq = field.get(term);
                    if (freq == null) field.put(term, vector.getTermFrequencies()[i]); else field.put(term, freq + vector.getTermFrequencies()[i]);
                }
            }
        }
        TermFreqVector[] vectors = new TermFreqVector[map.size()];
        int vectorPos = 0;
        for (Map.Entry<String, HashMap<String, Integer>> field : map.entrySet()) {
            int totalTerms = field.getValue().size();
            String[] terms = new String[totalTerms];
            int cont = 0;
            for (String term : field.getValue().keySet()) {
                terms[cont] = term;
                cont++;
            }
            int[] freqs = new int[totalTerms];
            Arrays.sort(terms, StringComparator.ASC);
            for (int i = 0; i < terms.length; i++) {
                freqs[i] = field.getValue().get(terms[i]);
            }
            TermFreqVector vector = new SegmentTermVector(field.getKey(), terms, freqs);
            vectors[vectorPos] = vector;
            vectorPos++;
        }
        return vectors;
    }

    /** Return term frequency vector for the specified document.
     *
     *  The vector contains term numbers and frequencies for all terms
     *  in a given vectorized field.
     *
     * All indexes have all documents
     *
     * @author Jorge Machado
     * todo Need optimization
     * todo need debug
     */
    public TermFreqVector getTermFreqVector(int n, String field) throws IOException {
        HashMap<String, Integer> map = new HashMap<String, Integer>(0);
        for (IndexReader subReader : readers.getReaders()) {
            TermFreqVector vector = subReader.getTermFreqVector(n, field);
            for (int i = 0; i < vector.getTerms().length; i++) {
                String term = vector.getTerms()[i];
                Integer freq = map.get(term);
                if (freq == null) map.put(term, vector.getTermFrequencies()[i]); else map.put(term, freq + vector.getTermFrequencies()[i]);
            }
        }
        if (map.size() == 0) return null;
        int totalTerms = map.size();
        String[] terms = new String[totalTerms];
        int cont = 0;
        for (String term : map.keySet()) {
            terms[cont] = term;
            cont++;
        }
        int[] freqs = new int[totalTerms];
        Arrays.sort(terms, StringComparator.ASC);
        for (int i = 0; i < terms.length; i++) {
            freqs[i] = map.get(terms[i]);
        }
        return new SegmentTermVector(field, terms, freqs);
    }

    public int getFieldLength(int docNumber, String field) throws IOException {
        return readers.getReader(field).getFieldLength(docNumber, field);
    }

    public int getDocLength(int doc) throws IOException {
        int len = 0;
        for (IndexReader subReader : readers.getReaders()) {
            int docNumber = doc;
            if (lgteIndexTreeIdMapper != null && !lgteIndexTreeIdMapper.isChild(subReader)) {
                docNumber = lgteIndexTreeIdMapper.translateIdInverted(docNumber);
            }
            len += subReader.getDocLength(docNumber);
        }
        return len;
    }

    int numDocs = -1;

    public int numDocs() {
        if (numDocs == -1) {
            int max = 0;
            for (IndexReader reader : readers.getReaders()) {
                int subNum = reader.numDocs();
                max = subNum > max ? subNum : max;
            }
            numDocs = max;
        }
        return numDocs;
    }

    public int numDocs(String field) {
        return readers.getReader(field).numDocs();
    }

    public int maxDoc(String field) {
        return readers.getReader(field).maxDoc();
    }

    int maxDoc = -1;

    public int maxDoc() {
        if (maxDoc == -1) {
            int max = 0;
            for (IndexReader reader : readers.getReaders()) {
                int subNum = reader.maxDoc();
                max = subNum > max ? subNum : max;
            }
            maxDoc = max;
        }
        return maxDoc;
    }

    /**
     * todo Need Check
     * todo need modification if the docs IDs are not equal in all indexes
     * @param doc
     * @return
     * @throws java.io.IOException
     */
    public Document document(int doc) throws IOException {
        boolean idField = false;
        Document d = new Document();
        for (IndexReader reader : readers.getReaders()) {
            int n = doc;
            if (lgteIndexTreeIdMapper != null && !lgteIndexTreeIdMapper.isChild(reader)) {
                n = lgteIndexTreeIdMapper.translateIdInverted(n);
            }
            Document dS = reader.document(n);
            Enumeration fields = dS.fields();
            while (fields.hasMoreElements()) {
                Field field = (Field) fields.nextElement();
                if (readers.getReader(field.name()) == reader) {
                    if (field.name().equals(Globals.DOCUMENT_ID_FIELD)) idField = true;
                    d.add(field);
                }
            }
        }
        if (!idField) {
            d.add(readers.getReaders().iterator().next().document(doc).getField(Globals.DOCUMENT_ID_FIELD));
        }
        return d;
    }

    public Object get(Object key) {
        for (IndexReader reader : readers.getReaders()) {
            if (reader instanceof IDataCacher) {
                Object obj = ((IDataCacher) reader).get(key);
                if (obj != null) return obj;
            }
        }
        return null;
    }

    public boolean hasDeletions() {
        return false;
    }

    protected void doDelete(int n) throws IOException {
        throw new ReadOnlyIndex("LgteIsolatedIndexReader is a ReadOnly Index");
    }

    protected void doUndeleteAll() throws IOException {
        throw new ReadOnlyIndex("LgteIsolatedIndexReader is a ReadOnly Index");
    }

    public boolean isDeleted(int n) {
        return false;
    }

    public byte[] norms(String field) throws IOException {
        return readers.getReader(field).norms(field);
    }

    public void norms(String field, byte[] bytes, int offset) throws IOException {
        readers.getReader(field).norms(field, bytes, offset);
    }

    protected void doSetNorm(int doc, String field, byte value) throws IOException {
        readers.getReader(field).doSetNorm(doc, field, value);
    }

    public TermEnum terms() throws IOException {
        return new MultiTermEnum(readers.getReadersArray(), new int[readers.getReadersArray().length], null);
    }

    public TermEnum terms(Term t) throws IOException {
        if (readers.getReader(t.field()) == null) System.out.println("NULL:" + t.field());
        return readers.getReader(t.field()).terms(t);
    }

    public int docFreq(Term t) throws IOException {
        return readers.getReader(t.field()).docFreq(t);
    }

    public TermDocs termDocs() throws IOException {
        return new MultiTermDocs(readers);
    }

    public TermPositions termPositions() throws IOException {
        return new MultiTermPositions(readers);
    }

    protected void doCommit() throws IOException {
        for (IndexReader reader : readers.getReaders()) {
            reader.commit();
        }
    }

    protected synchronized void doClose() throws IOException {
        for (IndexReader reader : readers.getReaders()) {
            reader.close();
        }
    }

    public Collection getFieldNames() throws IOException {
        return readers.getFields();
    }

    /**
     * @see org.apache.lucene.index.IndexReader#getFieldNames(boolean)
     */
    public Collection getFieldNames(boolean indexed) throws IOException {
        Set fieldSet = new HashSet();
        for (IndexReader reader : readers.getReaders()) {
            Collection<String> names = reader.getFieldNames(indexed);
            for (String field : names) if (!fieldSet.contains(field)) fieldSet.add(field);
        }
        return fieldSet;
    }

    public Collection getIndexedFieldNames(boolean storedTermVector) {
        Set fieldSet = new HashSet();
        for (IndexReader reader : readers.getReaders()) {
            Collection<String> names = reader.getIndexedFieldNames(storedTermVector);
            for (String field : names) if (!fieldSet.contains(field)) fieldSet.add(field);
        }
        return fieldSet;
    }

    public TermDocs termDocs(Term term) throws IOException {
        return readers.getReader(term.field()).termDocs(term);
    }

    public TermPositions termPositions(Term term) throws IOException {
        return readers.getReader(term.field()).termPositions(term);
    }

    public void setNorm(int doc, String field, float value) throws IOException {
        throw new NotImplemented("LgteIsolatedIndexReader does not implements this method yet");
    }

    class MultiTermDocs implements TermDocs {

        Readers readers;

        protected Term term;

        protected TermDocs current;

        public MultiTermDocs(Readers r) {
            readers = r;
        }

        public int doc() {
            return current.doc();
        }

        public int freq() {
            return current.freq();
        }

        public void seek(Term term) throws IOException {
            current = readers.getReader(term.field()).termDocs();
            current.seek(term);
            this.term = term;
        }

        public void seek(TermEnum termEnum) throws IOException {
            seek(termEnum.term());
        }

        public boolean next() throws IOException {
            if (current != null && current.next()) return true; else return false;
        }

        /** Optimized implementation. */
        public int read(final int[] docs, final int[] freqs) throws IOException {
            while (true) {
                while (current == null) {
                    return 0;
                }
                int end = current.read(docs, freqs);
                if (end == 0) {
                    if (current != null) current.close();
                    current = null;
                } else {
                    return end;
                }
            }
        }

        /** As yet unoptimized implementation. */
        public boolean skipTo(int target) throws IOException {
            do {
                if (!next()) return false;
            } while (target > doc());
            return true;
        }

        public void close() throws IOException {
            if (current != null) current.close();
        }
    }

    class MultiTermPositions extends MultiTermDocs implements TermPositions {

        public MultiTermPositions(Readers r) {
            super(r);
        }

        protected TermDocs termDocs(IndexReader reader) throws IOException {
            return reader.termPositions();
        }

        public int nextPosition() throws IOException {
            return ((TermPositions) current).nextPosition();
        }
    }
}
