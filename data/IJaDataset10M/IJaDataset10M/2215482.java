package pauker.program;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * a search engine for pauker cards based on apache lucene
 * @author Ronny.Standtke@gmx.net
 */
public class SearchEngine {

    /**
     * the identifier for searches at the front side
     */
    public static final String FRONT_SIDE = "frontSide";

    /**
     * the identifier for searches at the reverse side
     */
    public static final String REVERSE_SIDE = "reverseSide";

    private static final String OBJECT_ID = "objectId";

    private static final Logger LOGGER = Logger.getLogger(SearchEngine.class.getName());

    private final Analyzer analyzer;

    private final RAMDirectory ramDirectory;

    private final Map<String, Card> indexedCards;

    private int searchLimit;

    /** Creates a new instance of SearchEngine */
    public SearchEngine() {
        analyzer = new WhitespaceAnalyzer(Version.LUCENE_32);
        ramDirectory = new RAMDirectory();
        indexedCards = new HashMap<String, Card>();
        searchLimit = 1;
        privateInit();
    }

    /**
     * must be called when the search index should be resetted
     * (e.g. for a new lesson or open file)
     */
    public void init() {
        privateInit();
    }

    /**
     * method is indexing all cards of a lesson (used when opening a lesson)
     * @param lesson the lesson to index
     */
    public void index(Lesson lesson) {
        try {
            IndexWriter indexWriter = new IndexWriter(ramDirectory, new IndexWriterConfig(Version.LUCENE_32, analyzer));
            for (Card card : lesson.getCards()) {
                addCard(indexWriter, card);
            }
            indexWriter.optimize();
            indexWriter.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    /**
     * method to index a single new card or index changes of a card
     * (new card, edit card, import lesson)
     * @param card the card to index
     */
    public void index(Card card) {
        try {
            IndexWriter indexWriter = new IndexWriter(ramDirectory, new IndexWriterConfig(Version.LUCENE_32, analyzer));
            addCard(indexWriter, card);
            indexWriter.optimize();
            indexWriter.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    /**
     * remove a card from the index
     * @param card the card to be removed
     */
    public void removeCard(Card card) {
        String cardID = card.getId();
        try {
            IndexWriter indexWriter = new IndexWriter(ramDirectory, new IndexWriterConfig(Version.LUCENE_32, analyzer));
            indexWriter.deleteDocuments(new Term(OBJECT_ID, cardID));
            indexWriter.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
        indexedCards.remove(cardID);
    }

    /**
     * sets a search limit (minimum number of letters a word must contain to be
     * searched)
     * @param searchLimit the search limit
     */
    public void setSearchLimit(int searchLimit) {
        this.searchLimit = searchLimit;
    }

    /**
     * searches for similar cards
     * @param searchString the string that the user already has inserted at the
     * card side
     * @param side the card side identifier string
     * @return similar cards
     */
    public List<Card> search(String searchString, String side) {
        final Set<SearchResult> searchResults = new TreeSet<SearchResult>();
        try {
            final IndexSearcher indexSearcher = new IndexSearcher(ramDirectory, true);
            String[] tokens = searchString.split("\\s");
            StringBuilder stringBuilder = new StringBuilder();
            for (String token : tokens) {
                if (token.length() >= searchLimit) {
                    token = QueryParser.escape(token);
                    stringBuilder.append(token);
                    stringBuilder.append("~");
                    stringBuilder.append(" ");
                }
            }
            String queryString = stringBuilder.toString();
            if (queryString.length() > 0) {
                QueryParser queryParser = new QueryParser(Version.LUCENE_30, side, analyzer);
                Query query = queryParser.parse(queryString);
                Collector collector = new Collector() {

                    private Scorer scorer;

                    @Override
                    public void setScorer(Scorer scorer) throws IOException {
                        this.scorer = scorer;
                    }

                    @Override
                    public void collect(int doc) throws IOException {
                        try {
                            Document document = indexSearcher.doc(doc);
                            String cardID = document.get(OBJECT_ID);
                            Card similarCard = indexedCards.get(cardID);
                            searchResults.add(new SearchResult(scorer.score(), similarCard));
                        } catch (CorruptIndexException ex) {
                            LOGGER.log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            LOGGER.log(Level.SEVERE, null, ex);
                        }
                    }

                    @Override
                    public void setNextReader(IndexReader reader, int docBase) throws IOException {
                    }

                    @Override
                    public boolean acceptsDocsOutOfOrder() {
                        return true;
                    }
                };
                indexSearcher.search(query, collector);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
        List<Card> similarCards = new ArrayList<Card>();
        for (SearchResult searchResult : searchResults) {
            similarCards.add(searchResult.getCard());
        }
        return similarCards;
    }

    private void privateInit() {
        try {
            IndexWriter indexWriter = new IndexWriter(ramDirectory, new IndexWriterConfig(Version.LUCENE_32, analyzer));
            indexWriter.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
        indexedCards.clear();
    }

    private void addCard(IndexWriter indexWriter, Card card) throws IOException {
        String cardID = card.getId();
        String frontSideText = card.getFrontSide().getText();
        String reverseSideText = card.getReverseSide().getText();
        if ((cardID != null) && (frontSideText != null) && (reverseSideText != null)) {
            Document document = new Document();
            document.add(new Field(OBJECT_ID, cardID, Field.Store.YES, Field.Index.NOT_ANALYZED));
            document.add(new Field(FRONT_SIDE, frontSideText, Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field(REVERSE_SIDE, reverseSideText, Field.Store.YES, Field.Index.ANALYZED));
            indexWriter.updateDocument(new Term(OBJECT_ID, cardID), document);
            indexedCards.put(cardID, card);
        }
    }

    private static class SearchResult implements Comparable<SearchResult> {

        private float score;

        private Card card;

        public SearchResult(float score, Card card) {
            this.score = score;
            this.card = card;
        }

        @Override
        public int compareTo(SearchResult other) {
            float otherScore = other.getScore();
            if (score < otherScore) {
                return 1;
            } else if (score > otherScore) {
                return -1;
            } else {
                return card.compareTo(other.getCard());
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SearchResult other = (SearchResult) obj;
            if (score != other.score) {
                return false;
            }
            if ((card != other.card) && (card == null || !card.equals(other.card))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 73 * hash + Float.floatToIntBits(score);
            hash = 73 * hash + (card != null ? card.hashCode() : 0);
            return hash;
        }

        /**
         * returns the score
         * @return the score
         */
        public float getScore() {
            return score;
        }

        /**
         * returns the card
         * @return the card
         */
        public Card getCard() {
            return card;
        }
    }
}
