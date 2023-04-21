package huf.data.sort.test;

import huf.data.ArrayIterator;
import huf.data.Container;
import huf.data.IContainer;
import huf.data.Iterator;
import huf.data.LinkedList;
import huf.data.ListSet;
import huf.data.Set;
import huf.data.Stack;
import huf.data.compare.StringComparator;
import huf.data.sort.ISorter;
import huf.misc.Utils;
import huf.misc.tester.Tester;

/**
 * Generic sorter test suite.
 */
public class GenericSorterTest {

    interface IContainerSortTestHelper<T> {

        IContainer<T> getContainer(Iterator<T> i);

        ISorter getSorter();
    }

    /**
	 * Run test suite for specified sorter.
	 *
	 * @param t tester
	 * @param sorter sorter to test
	 */
    void test(Tester t, final ISorter sorter) {
        test(t, new IContainerSortTestHelper<String>() {

            @Override
            public IContainer<String> getContainer(Iterator<String> i) {
                return new Container<String>(i);
            }

            @Override
            public ISorter getSorter() {
                return sorter;
            }
        });
        test(t, new IContainerSortTestHelper<String>() {

            @Override
            public IContainer<String> getContainer(Iterator<String> i) {
                return new LinkedList<String>(i);
            }

            @Override
            public ISorter getSorter() {
                return sorter;
            }
        });
        test(t, new IContainerSortTestHelper<String>() {

            @Override
            public IContainer<String> getContainer(Iterator<String> i) {
                return new ListSet<String>(i);
            }

            @Override
            public ISorter getSorter() {
                return sorter;
            }
        });
        test(t, new IContainerSortTestHelper<String>() {

            @Override
            public IContainer<String> getContainer(Iterator<String> i) {
                return new Set<String>(i);
            }

            @Override
            public ISorter getSorter() {
                return sorter;
            }
        });
        test(t, new IContainerSortTestHelper<String>() {

            @Override
            public IContainer<String> getContainer(Iterator<String> i) {
                return new Stack<String>(i);
            }

            @Override
            public ISorter getSorter() {
                return sorter;
            }
        });
    }

    /**
	 * Get factory and container name.
	 *
	 * @param factory factory
	 * @return factory and container name
	 */
    private String factoryName(IContainerSortTestHelper<String> factory) {
        return Utils.getClassName(factory.getSorter()) + "/" + Utils.getClassName(factory.getContainer(null));
    }

    /**
	 * Set string comparator on a container.
	 *
	 * @param c container
	 * @return configured container
	 */
    private IContainer<String> setComparator(IContainer<String> c) {
        c.setComparator(new StringComparator());
        return c;
    }

    /**
	 * Check if a factory's container is a set.
	 *
	 * @param factory factory
	 * @return <code>true</code> if a factory's container is a set or
	 *        <code>false</code> otherwise
	 */
    private boolean isSet(IContainerSortTestHelper<String> factory) {
        return Utils.getClassName(factory.getContainer(null)).indexOf("Set") >= 0;
    }

    /**
	 * Run test suite for specified factory.
	 *
	 * @param t tester
	 * @param factory factory to test
	 */
    private void test(Tester t, IContainerSortTestHelper<String> factory) {
        String factoryName = factoryName(factory);
        boolean isSet = isSet(factory);
        IContainer<String> cont = null;
        cont = setComparator(factory.getContainer(null));
        factory.getSorter().sort(cont);
        t.test("sort/" + factoryName + "/empty", "", contToString(cont));
        cont = setComparator(factory.getContainer(new ArrayIterator<String>(new String[] { "a", "b", "c", "d" })));
        factory.getSorter().sort(cont);
        t.test("sort/" + factoryName + "/asc/01", "a b c d", contToString(cont));
        cont = setComparator(factory.getContainer(new ArrayIterator<String>(new String[] { "d", "c", "b", "a" })));
        factory.getSorter().sort(cont);
        t.test("sort/" + factoryName + "/asc/02", "a b c d", contToString(cont));
        cont = setComparator(factory.getContainer(new ArrayIterator<String>(new String[] { "a", "d", "c", "b" })));
        factory.getSorter().sort(cont);
        t.test("sort/" + factoryName + "/asc/03", "a b c d", contToString(cont));
        if (!isSet) {
            cont = setComparator(factory.getContainer(new ArrayIterator<String>(new String[] { "a", "c", "d", "a", "c", "b" })));
            factory.getSorter().sort(cont);
            t.test("sort/" + factoryName + "/asc/04", "a a b c c d", contToString(cont));
        }
        cont = setComparator(factory.getContainer(new ArrayIterator<String>(new String[] { "a", "b", "c", "d" })));
        factory.getSorter().sort(cont, ISorter.DESCENDING);
        t.test("sort/" + factoryName + "/desc/01", "d c b a", contToString(cont));
        cont = setComparator(factory.getContainer(new ArrayIterator<String>(new String[] { "d", "c", "b", "a" })));
        factory.getSorter().sort(cont, ISorter.DESCENDING);
        t.test("sort/" + factoryName + "/desc/02", "d c b a", contToString(cont));
        cont = setComparator(factory.getContainer(new ArrayIterator<String>(new String[] { "a", "d", "c", "b" })));
        factory.getSorter().sort(cont, ISorter.DESCENDING);
        t.test("sort/" + factoryName + "/desc/03", "d c b a", contToString(cont));
        if (!isSet) {
            cont = setComparator(factory.getContainer(new ArrayIterator<String>(new String[] { "a", "c", "d", "a", "c", "b" })));
            factory.getSorter().sort(cont, ISorter.DESCENDING);
            t.test("sort/" + factoryName + "/desc/04", "d c c b a a", contToString(cont));
        }
    }

    /**
	 * Returns a string representation of container contents.
	 * 
	 * @param c container
	 * @return tring representation of container contents
	 */
    private String contToString(IContainer<String> c) {
        if (c.isEmpty()) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        Iterator<String> i = c.iterator();
        while (i.hasNext()) {
            buf.append(" " + i.next());
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(0);
        }
        return buf.toString();
    }
}
