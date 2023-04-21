package org.chabanois.isbn.extractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Keeps the isbn candidates for a file
 * 
 * @author cchabanois at gmail.com
 *
 */
public class ISBNCandidates implements Iterable<ISBN> {

    private Map<String, ISBN> isbns = new HashMap<String, ISBN>();

    private float highScore = 0;

    public void addISBN(ISBN isbn) {
        ISBN actualIsbn = isbns.get(isbn.getIsbnWithoutSeparators());
        if (actualIsbn != null) {
            if (actualIsbn.getScore() < isbn.getScore()) {
                actualIsbn.setScore(isbn.getScore());
            }
            isbn = actualIsbn;
        } else {
            isbns.put(isbn.getIsbnWithoutSeparators(), isbn);
        }
        if (isbn.getScore() > highScore) {
            highScore = isbn.getScore();
        }
    }

    public ISBN getHighestScoreISBN() {
        float highScore = 0;
        ISBN bestIsbn = null;
        for (ISBN isbn : isbns.values()) {
            if (isbn.getScore() > highScore) {
                bestIsbn = isbn;
                highScore = isbn.getScore();
            }
        }
        return bestIsbn;
    }

    public float getHighScore() {
        return highScore;
    }

    /**
	 * iterator over the ISBN. First one returned is the isbn with the highest score
	 */
    public Iterator<ISBN> iterator() {
        List<ISBN> values = new ArrayList<ISBN>(isbns.values());
        Collections.sort(values, new Comparator<ISBN>() {

            public int compare(ISBN isbn1, ISBN isbn2) {
                return ((Float) isbn2.getScore()).compareTo((Float) isbn1.getScore());
            }
        });
        return values.iterator();
    }

    public boolean isEmpty() {
        return isbns.isEmpty();
    }

    public int size() {
        return isbns.size();
    }
}
