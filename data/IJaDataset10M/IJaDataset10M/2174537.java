package edu.vt.middleware.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import edu.vt.middleware.dictionary.sort.ArraySorter;

/**
 * Utility class for common operations on word lists.
 *
 * @author  Middleware Services
 * @version  $Revision: 1509 $
 */
public final class WordLists {

    /** Case sensitive comparator. */
    public static final Comparator<String> CASE_SENSITIVE_COMPARATOR = new Comparator<String>() {

        public int compare(final String a, final String b) {
            return a.compareTo(b);
        }
    };

    /** Case insensitive comparator. */
    public static final Comparator<String> CASE_INSENSITIVE_COMPARATOR = new Comparator<String>() {

        public int compare(final String a, final String b) {
            return a.compareToIgnoreCase(b);
        }
    };

    /** Index returned when word not found by binary search. */
    public static final int NOT_FOUND = -1;

    /** Private constructor of utility class. */
    private WordLists() {
    }

    /**
   * Performs a binary search of the given word list for the given word.
   *
   * @param  wordList  Word list to search
   * @param  word  Word to search for.
   *
   * @return  Index of given word in list or a negative number if not found.
   */
    public static int binarySearch(final WordList wordList, final String word) {
        final Comparator<String> comparator = wordList.getComparator();
        int low = 0;
        int high = wordList.size() - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            final int cmp = comparator.compare(wordList.get(mid), word);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return NOT_FOUND;
    }

    /**
   * Creates a case-sensitive {@link ArrayWordList} by reading the contents of
   * the given readers.
   *
   * @param  readers  Array of readers.
   *
   * @return  Word list read from the given readers.
   *
   * @throws  IOException  if an error occurs reading from a reader
   */
    public static ArrayWordList createFromReader(final Reader[] readers) throws IOException {
        return createFromReader(readers, true);
    }

    /**
   * Creates an {@link ArrayWordList} by reading the contents of the given
   * readers.
   *
   * @param  readers  Array of readers.
   * @param  caseSensitive  Set to true to create case-sensitive word list
   * (default), false otherwise.
   *
   * @return  Word list read from the given readers.
   *
   * @throws  IOException  if an error occurs reading from a reader
   */
    public static ArrayWordList createFromReader(final Reader[] readers, final boolean caseSensitive) throws IOException {
        return createFromReader(readers, caseSensitive, null);
    }

    /**
   * Creates an {@link ArrayWordList} by reading the contents of the given file
   * with support for sorting file contents.
   *
   * @param  readers  Array of readers.
   * @param  caseSensitive  Set to true to create case-sensitive word list
   * (default), false otherwise.
   * @param  sorter  To sort the input array with
   *
   * @return  Word list read from given readers.
   *
   * @throws  IOException  if an error occurs reading from a reader
   */
    public static ArrayWordList createFromReader(final Reader[] readers, final boolean caseSensitive, final ArraySorter sorter) throws IOException {
        final List<String> words = new ArrayList<String>();
        for (Reader r : readers) {
            final BufferedReader br = new BufferedReader(r);
            try {
                String word = null;
                while ((word = br.readLine()) != null) {
                    word = word.trim();
                    if (!"".equals(word)) {
                        words.add(word);
                    }
                }
            } finally {
                br.close();
            }
        }
        return new ArrayWordList(words.toArray(new String[words.size()]), caseSensitive, sorter);
    }
}
