package org.vizzini.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Provides unit tests for the <code>TokenPositionCollection</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.1
 */
public class TokenPositionCollectionTest extends TestCase {

    /** First collection. */
    private ITokenCollection _collection00;

    /** Second collection. */
    private ITokenCollection _collection11;

    /** Third collection. */
    private ITokenCollection _collection22;

    /**
     * Application method.
     *
     * @param  args  Application arguments.
     *
     * @since  v0.1
     */
    public static void main(String[] args) {
        TestRunner.run(TokenPositionCollectionTest.class);
    }

    /**
     * Test the <code>add()</code> method.
     *
     * @since  v0.1
     */
    public void testAdd() {
        _collection00.clear();
        int size = _collection00.size();
        boolean changed = _collection00.add(TestData.TOKEN0);
        assertTrue(changed);
        assertEquals(size + 1, _collection00.size());
        Iterator<IToken> iter = _collection00.iterator();
        assertTrue(iter.hasNext());
        assertEquals(TestData.TOKEN0, iter.next());
        Exception exception = null;
        try {
            _collection00.add((IToken) null);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    /**
     * Test the <code>addAll()</code> method.
     *
     * @since  v0.1
     */
    public void testAddAll() {
        _collection00.clear();
        List<IToken> list = new ArrayList<IToken>();
        list.add(TestData.TOKEN1);
        list.add(TestData.TOKEN0);
        _collection00.addAll(list);
        assertFalse(_collection00.isEmpty());
        assertEquals(2, _collection00.size());
        Iterator<IToken> iter = _collection00.iterator();
        assertEquals(TestData.TOKEN1, iter.next());
        assertEquals(TestData.TOKEN0, iter.next());
        Exception exception = null;
        try {
            _collection00.addAll(null);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    /**
     * Test the <code>clear()</code> method.
     *
     * @since  v0.1
     */
    public void testClear() {
        assertFalse(_collection00.isEmpty());
        _collection00.clear();
        assertTrue(_collection00.isEmpty());
        TokenPositionCollection collection = (TokenPositionCollection) _collection00;
        Iterator<IPosition> iter = collection.getData().keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object value = collection.getData().get(key);
            System.out.println("testClear " + key + ": " + value);
        }
    }

    /**
     * Test the <code>contains()</code> method.
     *
     * @since  v0.1
     */
    public void testContains() {
        assertTrue(_collection00.contains(TestData.TOKEN2));
        _collection00.remove(TestData.TOKEN2);
        assertFalse(_collection00.contains(TestData.TOKEN2));
    }

    /**
     * Test the <code>containsAll()</code> method.
     *
     * @since  v0.1
     */
    public void testContainsAll() {
        List<IToken> list = new ArrayList<IToken>();
        list.add(TestData.TOKEN1);
        list.add(TestData.TOKEN2);
        assertTrue(_collection00.containsAll(list));
        _collection00.clear();
        assertFalse(_collection00.containsAll(list));
    }

    /**
     * Test the <code>equals()</code> method.
     *
     * @since  v0.1
     */
    public void testEquals() {
        assertTrue(_collection00.equals(_collection00));
        assertFalse(_collection00.equals(_collection11));
        assertTrue(_collection00.equals(_collection22));
    }

    /**
     * Test the <code>findByName()</code> method.
     *
     * @since  v0.1
     */
    public void testFindByName() {
        TokenPositionCollection collection = (TokenPositionCollection) _collection00;
        IToken token = collection.findByName(TestData.TOKEN2.getName());
        assertEquals(TestData.TOKEN2, token);
        token = collection.findByName(TestData.TOKEN1.getName());
        assertEquals(TestData.TOKEN1, token);
    }

    /**
     * Test the <code>get()</code> method.
     *
     * @since  v0.1
     */
    public void testGet() {
        TokenPositionCollection collection = (TokenPositionCollection) _collection00;
        IToken token = collection.get(DefaultToken.class, TestData.AGENT2);
        assertEquals(TestData.TOKEN2, token);
        token = collection.get(TestData.POSITION2);
        assertEquals(TestData.TOKEN2, token);
        token = collection.get(TestData.POSITION1);
        assertEquals(TestData.TOKEN1, token);
        Exception exception = null;
        try {
            collection.get((Class<? extends IToken>) null, TestData.AGENT0);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
        exception = null;
        try {
            collection.get(DefaultToken.class, null);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    /**
     * Test the <code>getKey()</code> method.
     *
     * @since  v0.2
     */
    public void testGetKey() {
        IntegerPosition position = IntegerPosition.get(1, 2, 3);
        String name = "myName";
        int value = 1;
        IToken token = new DefaultToken(position, name, value);
        token.setTeam(TestData.TEAM0);
        token.setAgent(TestData.AGENT0);
        TokenPositionCollection collection = (TokenPositionCollection) _collection00;
        collection.clear();
        assertTrue(collection.isEmpty());
        assertEquals(0, collection.size());
        collection.add(token);
        assertEquals(1, collection.size());
        assertTrue(!collection.isEmpty());
        assertEquals(token, collection.findByName(name));
        IntegerPosition position2 = IntegerPosition.get(3, 4, 5);
        token.setPosition(position2);
        assertEquals(token, collection.get(position2));
        assertTrue(!collection.isEmpty());
        assertEquals(1, collection.size());
    }

    /**
     * Test the <code>hashCode()</code> method.
     *
     * @since  v0.1
     */
    public void testHashCode() {
        assertEquals(_collection00.hashCode(), _collection00.hashCode());
        assertFalse(_collection00.hashCode() == _collection11.hashCode());
        assertEquals(_collection00.hashCode(), _collection22.hashCode());
    }

    /**
     * Test the <code>isEmpty()</code> method.
     *
     * @since  v0.1
     */
    public void testIsEmpty() {
        assertFalse(_collection00.isEmpty());
        _collection00.clear();
        assertTrue(_collection00.isEmpty());
    }

    /**
     * Test the <code>iterator()</code> method.
     *
     * @since  v0.1
     */
    public void testIterator() {
        Iterator<IToken> iter = _collection00.iterator();
        int count = 0;
        while (iter.hasNext()) {
            iter.next();
            count++;
        }
        assertEquals(_collection00.size(), count);
        TokenPositionCollection collection = (TokenPositionCollection) _collection00;
        iter = collection.iterator(DefaultToken.class);
        count = 0;
        while (iter.hasNext()) {
            iter.next();
            count++;
        }
        assertEquals(2, count);
    }

    /**
     * Test the <code>remove()</code> method.
     *
     * @since  v0.1
     */
    public void testRemove() {
        int size = _collection00.size();
        boolean changed = _collection00.remove(TestData.TOKEN2);
        assertTrue(changed);
        assertEquals(size - 1, _collection00.size());
        Exception exception = null;
        try {
            _collection00.remove(null);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    /**
     * Test the <code>removeAll()</code> method.
     *
     * @since  v0.1
     */
    public void testRemoveAll() {
        List<IToken> list = new ArrayList<IToken>();
        list.add(TestData.TOKEN1);
        boolean changed = _collection00.removeAll(list);
        assertTrue(changed);
        assertFalse(_collection00.isEmpty());
        assertEquals(1, _collection00.size());
        Iterator<IToken> iter = _collection00.iterator();
        assertEquals(TestData.TOKEN2, iter.next());
        Exception exception = null;
        try {
            _collection00.removeAll(null);
        } catch (IllegalArgumentException e) {
            exception = e;
        }
        assertNotNull(exception);
    }

    /**
     * Test the <code>retainAll()</code> method.
     *
     * @since  v0.1
     */
    public void testRetainAll() {
        List<IToken> list = new ArrayList<IToken>();
        list.add(TestData.TOKEN1);
        list.add(TestData.TOKEN0);
        _collection00.retainAll(list);
        assertFalse(_collection00.isEmpty());
        assertEquals(2, _collection00.size());
    }

    /**
     * Test the <code>size()</code> method.
     *
     * @since  v0.1
     */
    public void testSize() {
        assertEquals(2, _collection00.size());
    }

    /**
     * Test the <code>toArray()</code> method.
     *
     * @since  v0.1
     */
    public void testToArray() {
        Object[] objects = _collection00.toArray();
        assertNotNull(objects);
        assertEquals(2, objects.length);
        assertEquals(TestData.TOKEN2, objects[0]);
        assertEquals(TestData.TOKEN1, objects[1]);
        IToken[] tokens = _collection00.toArray(new IToken[0]);
        assertNotNull(tokens);
        assertEquals(2, tokens.length);
        assertEquals(TestData.TOKEN2, tokens[0]);
        assertEquals(TestData.TOKEN1, tokens[1]);
        tokens = _collection00.toArray(new IToken[2]);
        assertNotNull(tokens);
        assertEquals(2, tokens.length);
        assertEquals(TestData.TOKEN2, tokens[0]);
        assertEquals(TestData.TOKEN1, tokens[1]);
    }

    /**
     * Test the <code>toString()</code> method.
     *
     * @since  v0.1
     */
    public void testToString() {
        String linefeed = System.getProperty("line.separator");
        String expected = "org.vizzini.game.TokenPositionCollection [" + linefeed + "0 org.vizzini.game.IntegerPosition (1,2,3) : org.vizzini.game.DefaultToken [_name=X,_value=42,_position=org.vizzini.game.IntegerPosition (1,2,3),_team=org.vizzini.game.DefaultTeam [_name=team0],_agent=org.vizzini.game.DefaultAgent [_name=bob,_score=36,_team=org.vizzini.game.DefaultTeam [_name=team0]]]" + linefeed + "1 org.vizzini.game.IntegerPosition (4,5,6) : org.vizzini.game.DefaultToken [_name=O,_value=24,_position=org.vizzini.game.IntegerPosition (4,5,6),_team=org.vizzini.game.DefaultTeam [_name=team1],_agent=org.vizzini.game.DefaultAgent [_name=george,_score=3,_team=org.vizzini.game.DefaultTeam [_name=team1]]]" + linefeed + "]";
        String result = _collection00.toString();
        assertEquals(expected, result);
    }

    /**
     * @return  a new collection.
     */
    protected Collection<? extends IToken> create() {
        return new TokenPositionCollection();
    }

    /**
     * @see  junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        _collection00 = (ITokenCollection) create();
        _collection00.add(TestData.TOKEN0);
        _collection00.add(TestData.TOKEN1);
        _collection00.add(TestData.TOKEN2);
        _collection11 = (ITokenCollection) create();
        _collection11.add(TestData.TOKEN1);
        _collection22 = (ITokenCollection) create();
        _collection22.add(TestData.TOKEN0);
        _collection22.add(TestData.TOKEN1);
        _collection22.add(TestData.TOKEN2);
    }
}
