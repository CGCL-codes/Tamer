package net.carpenomen.finder.engine.impl;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import net.carpenomen.finder.MockObjectTestCase;
import net.carpenomen.finder.engine.Discoverer;
import net.carpenomen.finder.trie.Trie;
import org.jmock.Expectations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author badger
 *
 */
public class DefaultDiscovererTest extends MockObjectTestCase {

    private Trie titleTrie;

    private Trie forenameTrie;

    private Trie surnameTrie;

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        titleTrie = mock(Trie.class);
        forenameTrie = mock(Trie.class);
        surnameTrie = mock(Trie.class);
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for {@link net.carpenomen.finder.discoverer.Discoverer#getNames(java.util.List)}.
	 */
    @Test
    public void testDiscovererWithMr_David_A_Johnson() {
        List<String> words = new ArrayList<String>();
        words.add("Mr");
        words.add("David");
        words.add("A");
        words.add("Johnson");
        checking(new Expectations() {

            {
                one(titleTrie).contains("Mr");
                will(returnValue(true));
                one(titleTrie).contains("David");
                will(returnValue(false));
                one(titleTrie).contains("A");
                will(returnValue(false));
                one(titleTrie).contains("Johnson");
                will(returnValue(false));
                one(forenameTrie).contains("David");
                will(returnValue(true));
                one(forenameTrie).contains("A");
                will(returnValue(false));
                one(forenameTrie).contains("Johnson");
                will(returnValue(false));
                one(surnameTrie).contains("Johnson");
                will(returnValue(true));
                one(surnameTrie).contains("Johnson");
                will(returnValue(true));
            }
        });
        Discoverer discoverer = new DefaultDiscoverer(titleTrie, forenameTrie, surnameTrie);
        List<List<String>> names = discoverer.parse(words);
        Assert.assertEquals(1, names.size());
    }

    /**
	 * Test method for {@link net.carpenomen.finder.discoverer.Discoverer#getNames(java.util.List)}.
	 */
    @Test
    public void testDiscovererWithMr_David_Johnson() {
        List<String> words = new ArrayList<String>();
        words.add("Mr");
        words.add("David");
        words.add("Johnson");
        checking(new Expectations() {

            {
                one(titleTrie).contains("Mr");
                will(returnValue(true));
                one(titleTrie).contains("David");
                will(returnValue(false));
                one(titleTrie).contains("Johnson");
                will(returnValue(false));
                one(forenameTrie).contains("David");
                will(returnValue(true));
                one(forenameTrie).contains("Johnson");
                will(returnValue(false));
                one(surnameTrie).contains("Johnson");
                will(returnValue(true));
                one(surnameTrie).contains("Johnson");
                will(returnValue(true));
            }
        });
        Discoverer discoverer = new DefaultDiscoverer(titleTrie, forenameTrie, surnameTrie);
        List<List<String>> names = discoverer.parse(words);
        Assert.assertEquals(1, names.size());
    }

    /**
	 * Test method for {@link net.carpenomen.finder.discoverer.Discoverer#getNames(java.util.List)}.
	 */
    @Test
    public void testDiscovererWithDavid_Johnson() {
        List<String> words = new ArrayList<String>();
        words.add("David");
        words.add("Johnson");
        checking(new Expectations() {

            {
                one(titleTrie).contains("David");
                will(returnValue(false));
                one(titleTrie).contains("Johnson");
                will(returnValue(false));
                one(forenameTrie).contains("David");
                will(returnValue(true));
                one(forenameTrie).contains("Johnson");
                will(returnValue(false));
                one(surnameTrie).contains("Johnson");
                will(returnValue(true));
                one(surnameTrie).contains("Johnson");
                will(returnValue(true));
            }
        });
        Discoverer discoverer = new DefaultDiscoverer(titleTrie, forenameTrie, surnameTrie);
        List<List<String>> names = discoverer.parse(words);
        Assert.assertEquals(1, names.size());
    }

    /**
	 * Test method for {@link net.carpenomen.finder.discoverer.Discoverer#getNames(java.util.List)}.
	 */
    @Test
    public void testDiscovererWithMr_Johnson() {
        List<String> words = new ArrayList<String>();
        words.add("Mr");
        words.add("Johnson");
        checking(new Expectations() {

            {
                one(titleTrie).contains("Mr");
                will(returnValue(true));
                one(titleTrie).contains("Johnson");
                will(returnValue(false));
                one(forenameTrie).contains("Johnson");
                will(returnValue(false));
                one(surnameTrie).contains("Johnson");
                will(returnValue(true));
                one(surnameTrie).contains("Johnson");
                will(returnValue(true));
            }
        });
        Discoverer discoverer = new DefaultDiscoverer(titleTrie, forenameTrie, surnameTrie);
        List<List<String>> names = discoverer.parse(words);
        Assert.assertEquals(1, names.size());
    }

    /**
	 * Test method for {@link net.carpenomen.finder.discoverer.Discoverer#getNames(java.util.List)}.
	 */
    @Test
    public void testDiscovererWithMr_D_Johnson() {
        List<String> words = new ArrayList<String>();
        words.add("Mr");
        words.add("Johnson");
        checking(new Expectations() {

            {
                one(titleTrie).contains("Mr");
                will(returnValue(true));
                one(titleTrie).contains("Johnson");
                will(returnValue(false));
                one(forenameTrie).contains("Johnson");
                will(returnValue(false));
                one(surnameTrie).contains("Johnson");
                will(returnValue(true));
                one(surnameTrie).contains("Johnson");
                will(returnValue(true));
            }
        });
        Discoverer discoverer = new DefaultDiscoverer(titleTrie, forenameTrie, surnameTrie);
        List<List<String>> names = discoverer.parse(words);
        Assert.assertEquals(1, names.size());
    }
}
