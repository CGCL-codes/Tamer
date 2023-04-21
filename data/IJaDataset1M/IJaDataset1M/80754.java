package net.sf.joafip.java.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import junit.framework.TestCase;
import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;

/**
 * to test {@link List} implementation<br>
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@DoNotTransform
public class ListTest {

    private static final String MUST_HAVE_PREVIOUS_ELEMENT = "must have previous element";

    private static final String MUST_NOT_HAVE_PREVIOUS_ELEMENT = "must not have previous element";

    private static final String MUST_HAVE_NEXT_ELEMENT = "must have next element";

    private static final String MUST_NOT_HAVE_NEXT_ELEMENT = "must not have next element";

    private static final String BAD_INDEX = "bad index";

    private static final String BAD_ELEMENT = "bad element";

    private static final String BAD_SIZE = "bad size";

    private List<String> list;

    private final transient CollectionTest collectionTest;

    public ListTest(final boolean concurrentAccess, final boolean acceptNullElement) {
        super();
        collectionTest = new CollectionTest(concurrentAccess, acceptNullElement);
    }

    public void setList(final List<String> list) {
        this.list = list;
        collectionTest.setCollection(list);
    }

    public void testAddAll() {
        collectionTest.testAddAll();
    }

    public void testClear() {
        collectionTest.testClear();
    }

    public void testContains() {
        collectionTest.testContains();
    }

    public void testContainsAll() {
        collectionTest.testContainsAll();
    }

    public void testEquals() throws InstantiationException, IllegalAccessException {
        collectionTest.testEquals();
    }

    public void testIterator() {
        collectionTest.testIterator();
    }

    public void testIteratorRemove() {
        collectionTest.testIteratorRemove();
    }

    public void testIteratorRemove2() {
        collectionTest.testIteratorRemove2();
    }

    public void testRemoveAll() {
        collectionTest.testRemoveAll();
    }

    public void testRetainAll() {
        collectionTest.testRetainAll();
    }

    public void testSizeAndIsEmpty() {
        collectionTest.testSizeAndIsEmpty();
    }

    public void testToArray1() {
        collectionTest.testToArrayOrdered1();
    }

    public void testToArray2() {
        collectionTest.testToArrayNotOrdered2();
    }

    public void testToArray3() {
        collectionTest.testToGreaterArrayNotOrdered();
    }

    public void testToArray4() {
        collectionTest.testToLessArrayNotOrdered();
    }

    public void testRemove() {
        list.add("a");
        list.add("b");
        list.add("c");
        list.remove("b");
        TestCase.assertEquals(BAD_SIZE, 2, list.size());
        final String[] array = new String[2];
        list.toArray(array);
        TestCase.assertEquals(BAD_ELEMENT, "a", array[0]);
        TestCase.assertEquals(BAD_ELEMENT, "c", array[1]);
    }

    public void testRemoveAt() {
        list.add("a");
        list.add("b");
        list.add("c");
        list.remove(1);
        TestCase.assertEquals(BAD_SIZE, 2, list.size());
        final String[] array = new String[2];
        list.toArray(array);
        TestCase.assertEquals(BAD_ELEMENT, "a", array[0]);
        TestCase.assertEquals(BAD_ELEMENT, "c", array[1]);
    }

    public void testAddAll2() {
        list.add("a");
        list.add("b");
        list.add("c");
        final Collection<String> collection = new LinkedList<String>();
        collection.add("x");
        collection.add("y");
        list.addAll(1, collection);
        TestCase.assertEquals(BAD_SIZE, 5, list.size());
        final String[] array = new String[5];
        list.toArray(array);
        TestCase.assertEquals(BAD_ELEMENT, "a", array[0]);
        TestCase.assertEquals(BAD_ELEMENT, "x", array[1]);
        TestCase.assertEquals(BAD_ELEMENT, "y", array[2]);
        TestCase.assertEquals(BAD_ELEMENT, "b", array[3]);
        TestCase.assertEquals(BAD_ELEMENT, "c", array[4]);
    }

    public void testGet() {
        list.add("a");
        list.add("b");
        list.add("c");
        TestCase.assertEquals(BAD_ELEMENT, "a", list.get(0));
        TestCase.assertEquals(BAD_ELEMENT, "b", list.get(1));
        TestCase.assertEquals(BAD_ELEMENT, "c", list.get(2));
    }

    public void testSet() {
        list.add("a");
        list.add("b");
        list.add("c");
        list.set(0, "aa");
        list.set(1, "bb");
        list.set(2, "cc");
        final String[] array = new String[3];
        list.toArray(array);
        TestCase.assertEquals(BAD_ELEMENT, "aa", array[0]);
        TestCase.assertEquals(BAD_ELEMENT, "bb", array[1]);
        TestCase.assertEquals(BAD_ELEMENT, "cc", array[2]);
    }

    public void testAdd() {
        list.add("a");
        list.add("b");
        list.add("c");
        TestCase.assertEquals(BAD_SIZE, 3, list.size());
        final String[] array = new String[3];
        list.toArray(array);
        TestCase.assertEquals(BAD_ELEMENT, "a", array[0]);
        TestCase.assertEquals(BAD_ELEMENT, "b", array[1]);
        TestCase.assertEquals(BAD_ELEMENT, "c", array[2]);
    }

    public void testAddAt() {
        list.add("a");
        list.add("b");
        list.add("c");
        list.add(1, "x");
        TestCase.assertEquals(BAD_SIZE, 4, list.size());
        final String[] array = new String[4];
        list.toArray(array);
        TestCase.assertEquals(BAD_ELEMENT, "a", array[0]);
        TestCase.assertEquals(BAD_ELEMENT, "x", array[1]);
        TestCase.assertEquals(BAD_ELEMENT, "b", array[2]);
        TestCase.assertEquals(BAD_ELEMENT, "c", array[3]);
    }

    public void testIndexOf() {
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("b");
        list.add("d");
        TestCase.assertEquals(BAD_INDEX, 1, list.indexOf("b"));
        TestCase.assertEquals(BAD_INDEX, -1, list.indexOf("x"));
    }

    public void testLastIndexOf() {
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("b");
        list.add("d");
        TestCase.assertEquals(BAD_INDEX, 3, list.lastIndexOf("b"));
        TestCase.assertEquals(BAD_INDEX, -1, list.lastIndexOf("x"));
    }

    public void testListIterator() {
        list.add("a");
        list.add("b");
        list.add("c");
        ListIterator<String> iterator = list.listIterator();
        TestCase.assertTrue(MUST_HAVE_NEXT_ELEMENT, iterator.hasNext());
        TestCase.assertEquals(BAD_INDEX, 0, iterator.nextIndex());
        TestCase.assertEquals("bad index", -1, iterator.previousIndex());
        TestCase.assertEquals(BAD_ELEMENT, "a", iterator.next());
        TestCase.assertTrue(MUST_HAVE_NEXT_ELEMENT, iterator.hasNext());
        TestCase.assertEquals(BAD_INDEX, 1, iterator.nextIndex());
        TestCase.assertEquals(BAD_INDEX, 0, iterator.previousIndex());
        TestCase.assertEquals(BAD_ELEMENT, "b", iterator.next());
        TestCase.assertTrue(MUST_HAVE_NEXT_ELEMENT, iterator.hasNext());
        TestCase.assertEquals(BAD_INDEX, 2, iterator.nextIndex());
        TestCase.assertEquals(BAD_INDEX, 1, iterator.previousIndex());
        TestCase.assertEquals(BAD_ELEMENT, "c", iterator.next());
        TestCase.assertFalse(MUST_HAVE_NEXT_ELEMENT, iterator.hasNext());
        TestCase.assertTrue(MUST_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        TestCase.assertEquals(BAD_ELEMENT, "c", iterator.previous());
        TestCase.assertTrue(MUST_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        TestCase.assertEquals(BAD_ELEMENT, "b", iterator.previous());
        TestCase.assertTrue(MUST_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        TestCase.assertEquals(BAD_ELEMENT, "a", iterator.previous());
        TestCase.assertFalse(MUST_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        iterator = list.listIterator(1);
        TestCase.assertTrue(MUST_HAVE_NEXT_ELEMENT, iterator.hasNext());
        TestCase.assertEquals(BAD_INDEX, 1, iterator.nextIndex());
        TestCase.assertEquals(BAD_INDEX, 0, iterator.previousIndex());
        TestCase.assertEquals(BAD_ELEMENT, "b", iterator.next());
        TestCase.assertTrue(MUST_HAVE_NEXT_ELEMENT, iterator.hasNext());
        TestCase.assertEquals(BAD_INDEX, 2, iterator.nextIndex());
        TestCase.assertEquals(BAD_INDEX, 1, iterator.previousIndex());
        TestCase.assertEquals(BAD_ELEMENT, "c", iterator.next());
        TestCase.assertFalse(MUST_HAVE_NEXT_ELEMENT, iterator.hasNext());
        iterator = list.listIterator(1);
        TestCase.assertTrue(MUST_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        TestCase.assertEquals(BAD_ELEMENT, "a", iterator.previous());
        TestCase.assertFalse(MUST_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        iterator = list.listIterator();
        TestCase.assertTrue(MUST_HAVE_NEXT_ELEMENT, iterator.hasNext());
        TestCase.assertEquals(BAD_ELEMENT, "a", iterator.next());
        TestCase.assertTrue(MUST_HAVE_NEXT_ELEMENT, iterator.hasNext());
        TestCase.assertEquals(BAD_INDEX, 1, iterator.nextIndex());
        TestCase.assertEquals(BAD_INDEX, 0, iterator.previousIndex());
        TestCase.assertEquals(BAD_ELEMENT, "b", iterator.next());
        TestCase.assertEquals(BAD_INDEX, 2, iterator.nextIndex());
        TestCase.assertEquals(BAD_INDEX, 1, iterator.previousIndex());
        iterator.remove();
        TestCase.assertTrue(MUST_HAVE_NEXT_ELEMENT, iterator.hasNext());
        TestCase.assertEquals(BAD_INDEX, 1, iterator.nextIndex());
        TestCase.assertEquals(BAD_INDEX, 0, iterator.previousIndex());
        TestCase.assertEquals(BAD_ELEMENT, "c", iterator.next());
        TestCase.assertFalse(MUST_HAVE_NEXT_ELEMENT, iterator.hasNext());
        TestCase.assertEquals(BAD_SIZE, 2, list.size());
        String[] array = new String[2];
        list.toArray(array);
        TestCase.assertEquals(BAD_ELEMENT, "a", array[0]);
        TestCase.assertEquals(BAD_ELEMENT, "c", array[1]);
        list.add("d");
        iterator = list.listIterator(2);
        TestCase.assertTrue(MUST_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        TestCase.assertEquals(BAD_ELEMENT, "c", iterator.previous());
        iterator.remove();
        TestCase.assertTrue(MUST_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        TestCase.assertEquals(BAD_ELEMENT, "a", iterator.previous());
        TestCase.assertFalse(MUST_NOT_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        TestCase.assertEquals(BAD_SIZE, 2, list.size());
        array = new String[2];
        list.toArray(array);
        TestCase.assertEquals(BAD_ELEMENT, "a", array[0]);
        TestCase.assertEquals(BAD_ELEMENT, "d", array[1]);
        list.add("e");
        iterator = list.listIterator();
        iterator.next();
        iterator.next();
        iterator.remove();
        TestCase.assertTrue(MUST_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        TestCase.assertEquals(BAD_ELEMENT, "a", iterator.previous());
        list.add("f");
        iterator = list.listIterator();
        iterator.next();
        iterator.remove();
        TestCase.assertFalse(MUST_NOT_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        try {
            iterator.previous();
            TestCase.fail("must throws NoSuchElementException");
        } catch (NoSuchElementException exception) {
        }
        iterator = list.listIterator();
        iterator.next();
        iterator.next();
        TestCase.assertFalse(MUST_NOT_HAVE_NEXT_ELEMENT, iterator.hasNext());
        iterator.remove();
        TestCase.assertTrue(MUST_HAVE_PREVIOUS_ELEMENT, iterator.hasPrevious());
        TestCase.assertEquals(BAD_ELEMENT, "e", iterator.previous());
        list.add("f");
        iterator = list.listIterator();
        iterator.next();
        iterator.next();
        TestCase.assertFalse(MUST_NOT_HAVE_NEXT_ELEMENT, iterator.hasNext());
        iterator.remove();
        TestCase.assertFalse(MUST_NOT_HAVE_NEXT_ELEMENT, iterator.hasNext());
        try {
            final String next = iterator.next();
            TestCase.fail("must throws NoSuchElementException, not have \"" + next + "\"");
        } catch (NoSuchElementException exception) {
        }
    }

    public void testSubList() {
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        final List<String> subList = list.subList(1, 4);
        TestCase.assertEquals(BAD_SIZE, 3, subList.size());
        String[] array = new String[3];
        subList.toArray(array);
        TestCase.assertEquals(BAD_ELEMENT, "b", array[0]);
        TestCase.assertEquals(BAD_ELEMENT, "c", array[1]);
        TestCase.assertEquals(BAD_ELEMENT, "d", array[2]);
        subList.remove(1);
        TestCase.assertEquals(BAD_SIZE, 4, list.size());
        array = new String[4];
        list.toArray(array);
        TestCase.assertEquals(BAD_ELEMENT, "a", array[0]);
        TestCase.assertEquals(BAD_ELEMENT, "b", array[1]);
        TestCase.assertEquals(BAD_ELEMENT, "d", array[2]);
        TestCase.assertEquals(BAD_ELEMENT, "e", array[3]);
        subList.clear();
        TestCase.assertEquals(BAD_SIZE, 2, list.size());
        array = new String[2];
        list.toArray(array);
        TestCase.assertEquals(BAD_ELEMENT, "a", array[0]);
        TestCase.assertEquals(BAD_ELEMENT, "e", array[1]);
    }

    public void testNullElement() {
        collectionTest.testNullElement();
    }
}
