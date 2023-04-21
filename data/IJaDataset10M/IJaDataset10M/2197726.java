package net.carpenomen.finder.trie;

import java.io.IOException;
import net.carpenomen.finder.trie.impl.NonUniqueKeyException;

/**
 * @author David Johnson
 *
 */
public interface Trie {

    /**
	 * Inserts the provided object into the trie with the given key.
	 * @param key the key
	 * @param item the item
	 * @throws NonUniqueKeyException when a key is not unique
	 */
    void insert(String key, Object item) throws NonUniqueKeyException;

    /**
	 * Returns a trie for multiple matches, else returns null for no object
	 * matched, or returns the object matched.
	 * @param key the key to search with
	 * @return the Trie that matches the key
	 */
    Object search(final String key);

    /**
	 * Returns a trie for multiple matches, else returns null for no object
	 * matched, or returns the object matched.
	 * @param key the key to search with
	 * @return the Trie that matches the key
	 */
    Object searchExact(final String key);

    /**
	 * Removes the object with the given key from the trie.
	 * @param key the key of the objec to remove
	 * @throws NonUniqueKeyException the key was not unique
	 */
    void remove(final String key) throws NonUniqueKeyException;

    /**
	 * Gets the number of objects in the trie.
	 * @return the length of the trie
	 */
    int length();

    /**
	 * Loads the tries with the contents of the file.
	 * 
	 * @param filePath The file name to load
	 * @throws IOException if there was a problem with the file
	 * @throws NonUniqueKeyException an object with the same key exists
	 */
    void load(String filePath) throws IOException, NonUniqueKeyException;

    /**
	 * Determines if the Trie contains the word.
	 * @param word The word under test
	 * @return if it is contained in the Trie
	 */
    boolean contains(String word);
}
