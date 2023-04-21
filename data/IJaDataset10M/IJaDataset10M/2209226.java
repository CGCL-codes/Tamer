package huf.data.test;

import huf.data.Iterator;
import huf.misc.tester.Tester;
import java.util.NoSuchElementException;

/**
 * Generic iterator test suite.
 */
public class GenericIteratorTest {

    public interface IteratorFactory<T> {

        /**
		 * Return iterator containing specified elements.
		 *
		 * @return iterator
		 */
        Iterator<T> getIterator();

        Object a();

        Object b();

        Object c();
    }

    /**
	 * Run test suite.
	 *
	 * @param <T> factory type
	 * @param t tester
	 * @param f factory
	 */
    public <T> GenericIteratorTest(Tester t, IteratorFactory<T> f) {
        this(t, f, true);
    }

    /**
	 * Run test suite.
	 *
	 * @param <T> factory type
	 * @param t tester
	 * @param f factory
	 * @param testRemoving should removing from iterator be tested
	 */
    public <T> GenericIteratorTest(Tester t, IteratorFactory<T> f, boolean testRemoving) {
        t.testClass(f.getIterator());
        next(t, f);
        get(t, f);
        hasNext(t, f);
        if (testRemoving) {
            remove(t, f);
        }
    }

    /**
	 * Run test.
	 *
	 * @param <T> factory type
	 * @param t tester
	 * @param f factory
	 */
    private <T> void next(Tester t, IteratorFactory<T> f) {
        Iterator<T> i = f.getIterator();
        t.test("next01 next() -> \"a\"", f.a(), i.next());
        t.test("next02 next() -> \"b\"", f.b(), i.next());
    }

    /**
	 * Run test.
	 *
	 * @param <T> factory type
	 * @param t tester
	 * @param f factory
	 */
    private <T> void get(Tester t, IteratorFactory<T> f) {
        Iterator<T> i = f.getIterator();
        boolean b1 = false;
        try {
            i.get();
        } catch (NoSuchElementException nsee) {
            b1 = true;
        }
        t.test("get01 no element before first (exception)", true, b1);
        i.next();
        t.test("get01 get() -> \"a\"", f.a(), i.get());
        i.next();
        t.test("get02 get() -> \"b\"", f.b(), i.get());
    }

    /**
	 * Run test.
	 *
	 * @param <T> factory type
	 * @param t tester
	 * @param f factory
	 */
    private <T> void hasNext(Tester t, IteratorFactory<T> f) {
        Iterator<T> i = f.getIterator();
        t.test("hasNext01 at startup hasNext()?", true, i.hasNext());
        i.next();
        i.next();
        t.test("hasNext03 hasNext() (b)?", true, i.hasNext());
        i.next();
        t.test("hasNext02 hasNext() (c)?", false, i.hasNext());
    }

    /**
	 * Run test.
	 *
	 * @param <T> factory type
	 * @param t tester
	 * @param f factory
	 */
    private <T> void remove(Tester t, IteratorFactory<T> f) {
        Iterator<T> i = f.getIterator();
        t.test("remove01 get() -> \"a\"", f.a(), i.next());
        t.test("remove02 get() -> \"b\"", f.b(), i.next());
        i.remove();
        t.test("remove03 after remove get() -> \"a\"", f.a(), i.get());
        t.test("remove04 next() -> \"c\"", f.c(), i.next());
        i.remove();
        t.test("remove05 after remove get() -> \"a\"", f.a(), i.get());
        t.test("remove06 hasNext() after remove?", false, i.hasNext());
        i.remove();
        boolean b2 = false;
        try {
            i.get();
        } catch (NoSuchElementException nsee) {
            b2 = true;
        }
        t.test("remove07 no element after remove (exception)", true, b2);
    }
}
