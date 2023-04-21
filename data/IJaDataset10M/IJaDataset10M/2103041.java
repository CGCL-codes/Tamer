package net.jakubholy.jedit.autocomplete;

import junit.framework.TestCase;

/**
 * 
 */
public class WordListTest extends TestCase {

    public WordListTest(String arg0) {
        super(arg0);
    }

    WordList wordList;

    Completion[] completions;

    final String prefix = "prefix";

    protected void setUp() throws Exception {
        wordList = new WordListTreeSet();
        completions = new Completion[] { new Completion("pref"), new Completion("prefaBefore"), new Completion("prefix"), new Completion("prefix01"), new Completion("prefix02"), new Completion("prefiyAfter") };
    }

    public void testGetCompletions() {
        wordList.addAll(completions);
        Completion[] words = wordList.getCompletions(prefix);
        assertEquals("The # of completions should be 2 (" + "prefix01, prefix02; excluding 'prefix')", 2, words.length);
        for (int i = 0; i < words.length; i++) {
            assertEquals(words[i], completions[i + 3]);
        }
    }

    public void testGetAllWords() {
        wordList.addAll(completions);
        Completion[] words = wordList.getAllWords();
        for (int i = 0; i < words.length; i++) {
            assertTrue("Get all w. shall insert all the elements we " + "inserted with addAll & in the same order as the added " + "array was ordered", words[i].equals(completions[i]));
        }
    }

    public void testAdd() {
        wordList.add(completions[0]);
        assertEquals("wlist size", 1, wordList.size());
        final String word = completions[0].getWord();
        final String wordPrefix = word.substring(0, word.length() - 1);
        assertNotNull("Completions for '" + wordPrefix + "' shalln't be null.", wordList.getCompletions(wordPrefix));
        assertEquals("# completions for prefix '" + wordPrefix + "' if wordlist=['" + word + "' ]", 1, wordList.getCompletions(wordPrefix).length);
        assertEquals(completions[0], wordList.getCompletions(wordPrefix)[0]);
    }

    public void testRemove() {
        wordList.add(completions[0]);
        assertTrue("Removal of just inserted element shall return true.", wordList.remove(completions[0]));
        Completion[] ca = wordList.getCompletions(completions[0].getWord());
        assertTrue("Removed element shouldn't be there anymore ", ca.length == 0 || !ca[0].equals(completions[0]));
    }

    public void testClear() {
        wordList.addAll(completions);
        wordList.clear();
        assertTrue(wordList.size() == 0);
    }

    public void testAddAll() {
        wordList.addAll(completions);
        for (int i = 0; i < completions.length; i++) {
            assertTrue("The list should contain all completions inserted ", wordList.containes(completions[i]));
        }
    }
}
