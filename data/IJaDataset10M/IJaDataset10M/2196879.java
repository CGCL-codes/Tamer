package test.net.sf.japi.swing.list;

import javax.swing.event.ListDataEvent;
import net.sf.japi.swing.list.ArrayListModel;
import org.jetbrains.annotations.NotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/** Test for {@link ArrayListModel}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ArrayListModelTest {

    /** Tests that an empty ArrayListModel behaves as expected. */
    @Test
    public void testEmpty() {
        final ArrayListModel testling = new ArrayListModel();
        assertEquals("Newly created ArrayListModel must contain no elements.", 0, testling.getSize());
    }

    /** Tests that an ArrayListModel with one element behaves as expected. */
    @Test
    public void testSingle() {
        final ArrayListModel<String> testling = new ArrayListModel<String>();
        final String foo = "foo";
        testling.add(foo);
        assertEquals("ArrayListModel now must contain one element.", 1, testling.getSize());
        assertSame("ArrayListModel must return the stored element.", foo, testling.getElementAt(0));
    }

    /** Tests that an ArrayListModel with one element does not accept duplicates. */
    @Test
    public void testSingleDuplicate() {
        final ArrayListModel<String> testling = new ArrayListModel<String>();
        final String foo = "foo";
        testling.add(foo);
        testling.add(foo);
        assertEquals("ArrayListModel now must contain one element.", 1, testling.getSize());
        assertSame("ArrayListModel must return the stored element.", foo, testling.getElementAt(0));
    }

    /** Tests that the move operations with elements work as expected for non-exception cases. */
    @Test
    public void testMoveElement() {
        final ArrayListModel<String> testling = new ArrayListModel<String>();
        final String test1 = "Test1";
        final String test2 = "Test2";
        final String test3 = "Test3";
        final String test4 = "Test4";
        final String test5 = "Test5";
        assertTrue("Adding " + test1 + " must work.", testling.add(test1));
        assertTrue("Adding " + test1 + " must work.", testling.add(test2));
        assertTrue("Adding " + test1 + " must work.", testling.add(test3));
        assertTrue("Adding " + test1 + " must work.", testling.add(test4));
        assertTrue("Adding " + test1 + " must work.", testling.add(test5));
        assertMove("Original order must match.", testling, test1, test2, test3, test4, test5);
        assertTrue("Moving " + test5 + " to top must work.", testling.moveToTop(test5));
        assertMove("Test5 now must be first.", testling, test5, test1, test2, test3, test4);
        assertFalse("Moving " + test5 + " to top must fail.", testling.moveToTop(test5));
        assertMove("Test5 now must still be first.", testling, test5, test1, test2, test3, test4);
        assertTrue("Moving " + test5 + " down must work.", testling.moveDown(test5));
        assertMove("Test5 now must be second.", testling, test1, test5, test2, test3, test4);
        assertTrue("Moving " + test5 + " down must work.", testling.moveDown(test5));
        assertMove("Test5 now must be third.", testling, test1, test2, test5, test3, test4);
        assertTrue("Moving " + test5 + " down must work.", testling.moveDown(test5));
        assertMove("Test5 now must be fourth.", testling, test1, test2, test3, test5, test4);
        assertTrue("Moving " + test5 + " down must work.", testling.moveDown(test5));
        assertMove("Test5 now must be last.", testling, test1, test2, test3, test4, test5);
        assertFalse("Moving " + test5 + " down must work.", testling.moveDown(test5));
        assertMove("Test5 now still must be last.", testling, test1, test2, test3, test4, test5);
        assertTrue("Moving " + test5 + " up must work.", testling.moveUp(test5));
        assertMove("Test5 now must be fourth.", testling, test1, test2, test3, test5, test4);
        assertTrue("Moving " + test5 + " up must work.", testling.moveUp(test5));
        assertMove("Test5 now must be third.", testling, test1, test2, test5, test3, test4);
        assertTrue("Moving " + test5 + " up must work.", testling.moveUp(test5));
        assertMove("Test5 now must be second.", testling, test1, test5, test2, test3, test4);
        assertTrue("Moving " + test5 + " up must work.", testling.moveUp(test5));
        assertMove("Test5 now must be first.", testling, test5, test1, test2, test3, test4);
        assertFalse("Moving " + test5 + " up must fail.", testling.moveUp(test5));
        assertMove("Test5 now still must be first.", testling, test5, test1, test2, test3, test4);
        assertTrue("Moving " + test5 + " to bottom must work.", testling.moveToBottom(test5));
        assertMove("Test5 now must be last.", testling, test1, test2, test3, test4, test5);
        assertFalse("Moving " + test5 + " to bottom must fail.", testling.moveToBottom(test5));
        assertMove("Test5 now must still be last.", testling, test1, test2, test3, test4, test5);
    }

    /** Tests that the move operations with indices work as expected for non-exception cases. */
    @Test
    public void testMoveIndex() {
        final ArrayListModel<String> testling = new ArrayListModel<String>();
        final String test1 = "Test1";
        final String test2 = "Test2";
        final String test3 = "Test3";
        final String test4 = "Test4";
        final String test5 = "Test5";
        testling.add(test1);
        testling.add(test2);
        testling.add(test3);
        testling.add(test4);
        testling.add(test5);
        assertMove("Original order must match.", testling, test1, test2, test3, test4, test5);
        assertTrue("Moving >0 to top must work.", testling.moveToTop(4));
        assertMove("Test5 now must be first.", testling, test5, test1, test2, test3, test4);
        assertFalse("Moving 0 to top must fail.", testling.moveToTop(0));
        assertMove("Test5 now must still be first.", testling, test5, test1, test2, test3, test4);
        assertTrue("Moving <end down must work.", testling.moveDown(0));
        assertMove("Test5 now must be second.", testling, test1, test5, test2, test3, test4);
        assertTrue("Moving <end down must work.", testling.moveDown(1));
        assertMove("Test5 now must be third.", testling, test1, test2, test5, test3, test4);
        assertTrue("Moving <end down must work.", testling.moveDown(2));
        assertMove("Test5 now must be fourth.", testling, test1, test2, test3, test5, test4);
        assertTrue("Moving <end down must work.", testling.moveDown(3));
        assertMove("Test5 now must be last.", testling, test1, test2, test3, test4, test5);
        assertFalse("Moving end down must fail.", testling.moveDown(4));
        assertMove("Test5 now still must be last.", testling, test1, test2, test3, test4, test5);
        assertTrue("Moving >0 up must work.", testling.moveUp(4));
        assertMove("Test5 now must be fourth.", testling, test1, test2, test3, test5, test4);
        assertTrue("Moving >0 up must work.", testling.moveUp(3));
        assertMove("Test5 now must be third.", testling, test1, test2, test5, test3, test4);
        assertTrue("Moving >0 up must work.", testling.moveUp(2));
        assertMove("Test5 now must be second.", testling, test1, test5, test2, test3, test4);
        assertTrue("Moving >0 up must work.", testling.moveUp(1));
        assertMove("Test5 now must be first.", testling, test5, test1, test2, test3, test4);
        assertFalse("Moving 0 up must fail.", testling.moveUp(0));
        assertMove("Test5 now still must be first.", testling, test5, test1, test2, test3, test4);
        assertTrue("Moving <end to bottom must work.", testling.moveToBottom(0));
        assertMove("Test5 now must be last.", testling, test1, test2, test3, test4, test5);
        assertFalse("Moving end to bottom must fail.", testling.moveToBottom(4));
        assertMove("Test5 now must still be last.", testling, test1, test2, test3, test4, test5);
    }

    /** Tests that add and remove operations fire the correct events. */
    @Test
    public void testAddRemoveEvents() {
        final ArrayListModel<String> testling = new ArrayListModel<String>();
        final String test1 = "Test1";
        final String test2 = "Test2";
        final String test3 = "Test3";
        final String test4 = "Test4";
        final MockListDataListener listenerMock = new MockListDataListener();
        testling.addListDataListener(listenerMock);
        testling.add(test1);
        listenerMock.assertEvent(ListDataEvent.INTERVAL_ADDED, 0, 0);
        testling.add(test2);
        listenerMock.assertEvent(ListDataEvent.INTERVAL_ADDED, 1, 1);
        testling.add(test3);
        listenerMock.assertEvent(ListDataEvent.INTERVAL_ADDED, 2, 2);
        testling.removeListDataListener(listenerMock);
        testling.add(test4);
        listenerMock.assertNoEvent();
        testling.addListDataListener(listenerMock);
        testling.remove(test1);
        listenerMock.assertEvent(ListDataEvent.INTERVAL_REMOVED, 0, 0);
        testling.remove(test4);
        listenerMock.assertEvent(ListDataEvent.INTERVAL_REMOVED, 2, 2);
        testling.removeListDataListener(listenerMock);
        testling.remove(test3);
        listenerMock.assertNoEvent();
    }

    /** Tests that move element operations fire the correct events. */
    @Test
    public void testMoveElementEvents() {
        final ArrayListModel<String> testling = new ArrayListModel<String>();
        final String test1 = "Test1";
        final String test2 = "Test2";
        final String test3 = "Test3";
        final String test4 = "Test4";
        final String test5 = "Test5";
        final MockListDataListener listenerMock = new MockListDataListener();
        testling.add(test1);
        testling.add(test2);
        testling.add(test3);
        testling.add(test4);
        testling.add(test5);
        testling.addListDataListener(listenerMock);
        testling.moveToTop(test5);
        listenerMock.assertEvent(ListDataEvent.CONTENTS_CHANGED, 0, 4);
        testling.moveToTop(test5);
        listenerMock.assertNoEvent();
        testling.moveDown(test5);
        listenerMock.assertEvent(ListDataEvent.CONTENTS_CHANGED, 0, 1);
        testling.moveDown(test5);
        listenerMock.assertEvent(ListDataEvent.CONTENTS_CHANGED, 1, 2);
        testling.moveDown(test5);
        listenerMock.assertEvent(ListDataEvent.CONTENTS_CHANGED, 2, 3);
        testling.moveDown(test5);
        listenerMock.assertEvent(ListDataEvent.CONTENTS_CHANGED, 3, 4);
        testling.moveDown(test5);
        listenerMock.assertNoEvent();
        testling.moveUp(test5);
        listenerMock.assertEvent(ListDataEvent.CONTENTS_CHANGED, 3, 4);
        testling.moveUp(test5);
        listenerMock.assertEvent(ListDataEvent.CONTENTS_CHANGED, 2, 3);
        testling.moveUp(test5);
        listenerMock.assertEvent(ListDataEvent.CONTENTS_CHANGED, 1, 2);
        testling.moveUp(test5);
        listenerMock.assertEvent(ListDataEvent.CONTENTS_CHANGED, 0, 1);
        testling.moveUp(test5);
        listenerMock.assertNoEvent();
        testling.moveToBottom(test5);
        listenerMock.assertEvent(ListDataEvent.CONTENTS_CHANGED, 0, 4);
        testling.moveToBottom(test5);
        listenerMock.assertNoEvent();
        testling.removeListDataListener(listenerMock);
    }

    /** Tests that remove() with an uncontained element throws an IllegalArgumentException.
     * @throws IllegalArgumentException (expected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRemoveIAE() {
        new ArrayListModel<String>().remove("foo");
    }

    /** Tests that moveToTop() with an uncontained element throws an IllegalArgumentException.
     * @throws IllegalArgumentException (expected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveToTopIAE() {
        new ArrayListModel<String>().moveToTop("foo");
    }

    /** Tests that moveUp() with an uncontained element throws an IllegalArgumentException.
     * @throws IllegalArgumentException (expected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveUpIAE() {
        new ArrayListModel<String>().moveUp("foo");
    }

    /** Tests that moveDown() with an uncontained element throws an IllegalArgumentException.
     * @throws IllegalArgumentException (expected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveDownIAE() {
        new ArrayListModel<String>().moveDown("foo");
    }

    /** Tests that moveToBottom() with an uncontained element throws an IllegalArgumentException.
     * @throws IllegalArgumentException (expected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMoveToBottomIAE() {
        new ArrayListModel<String>().moveToBottom("foo");
    }

    /** Asserts a certain size and order after a move.
     * @param message Message to issue in case the size and order are not as expected.
     * @param model Testling to check.
     * @param elements Elements in expected order.
     */
    public static <E> void assertMove(@NotNull final String message, @NotNull final ArrayListModel<E> model, @NotNull final E... elements) {
        assertEquals("ArrayListModel now must contain " + elements.length + " elements.", elements.length, model.getSize());
        for (int i = 0; i < elements.length; i++) {
            assertSame(message + " / ArrayList must contain element " + elements[i] + " at index " + i + ".", elements[i], model.getElementAt(i));
        }
    }
}
