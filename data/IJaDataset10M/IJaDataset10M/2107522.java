package org.apache.lucene.search;

import org.apache.lucene.util.PriorityQueue;

/** Interface and related classes to implement search results for keywords 
 * 
 * <p> A Keyword is a word with its frequency in the search. Can be used to
 * return TAGs clouds, etc.
 * 
 * @author  Jos� Luis Oramas Mart�n (Sadiel)
 * @since   lucene 2.0
 */
public interface KeywordResults extends CountedResults {

    public interface KeywordItem {

        public String getKeyword();

        public void setKeyword(String keyword);

        public int getFrequency();

        public void setFrequency(int frequency);
    }

    public class KeywordTopDocs extends CountedTopDocs {

        public KeywordTopDocs(int totalHits, KeywordScoreDoc[] scoreDocs) {
            super(totalHits, scoreDocs, 1.0f);
        }
    }

    public class KeywordScoreDoc extends ScoreDoc implements KeywordItem {

        public String keyword;

        public int frequency;

        public KeywordScoreDoc(String keyword, int frequency) {
            super(-1, frequency);
            setKeyword(keyword);
            setFrequency(frequency);
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public void increase() {
            frequency++;
        }

        public void add(int amount) {
            frequency += amount;
        }

        public int compareTo(Object o) {
            KeywordScoreDoc t = (KeywordScoreDoc) o;
            return (frequency < t.getFrequency() ? 1 : (frequency > t.getFrequency() ? -1 : keyword.compareTo(t.getKeyword())));
        }

        public boolean equals(Object o) {
            return compareTo(o) == 0;
        }

        public int hashCode() {
            return keyword.hashCode();
        }
    }

    public class KeywordFrequencyHitQueue extends PriorityQueue {

        public KeywordFrequencyHitQueue(int size) {
            initialize(size);
        }

        protected final boolean lessThan(Object a, Object b) {
            KeywordScoreDoc hitA = (KeywordScoreDoc) a;
            KeywordScoreDoc hitB = (KeywordScoreDoc) b;
            if (hitA.frequency == hitB.frequency) return hitA.keyword.compareTo(hitB.keyword) > 0; else return hitA.frequency < hitB.frequency;
        }
    }

    public class KeywordHitQueue extends PriorityQueue {

        public KeywordHitQueue(int size) {
            initialize(size);
        }

        protected final boolean lessThan(Object a, Object b) {
            KeywordScoreDoc hitA = (KeywordScoreDoc) a;
            KeywordScoreDoc hitB = (KeywordScoreDoc) b;
            int res = hitA.keyword.compareTo(hitB.keyword);
            if (res == 0) return hitA.frequency < hitB.frequency; else return res > 0;
        }
    }
}
