package huf.data.test;

import huf.data.IMap;
import huf.data.Iterator;
import huf.data.LinkedMap;
import huf.data.Pair;
import huf.misc.tester.Tester;

/**
 * LinkedMap class test suite.
 */
public class LinkedMapTest {

    /**
	 * Run test suite.
	 *
	 * @param t tester
	 */
    public LinkedMapTest(Tester t) {
        t.testClass(new LinkedMap<Object, Object>());
        new GenericMapTest(t, new GenericMapTest.IMapFactory() {

            @Override
            public <K, V> IMap<K, V> newMap() {
                return new LinkedMap<K, V>();
            }

            @Override
            public <K, V> IMap<K, V> newMap(int initNumBuckets, float maxLoadFactor) {
                return new LinkedMap<K, V>(initNumBuckets, maxLoadFactor);
            }
        });
        new GenericIteratorTest(t, new GenericIteratorTest.IteratorFactory<Pair<String, String>>() {

            @Override
            public Iterator<Pair<String, String>> getIterator() {
                return new LinkedMap<String, String>(elements).iterator();
            }

            private final Pair<String, String>[] elements = Pair.cast(new Pair[] { new Pair<String, String>("a", "A"), new Pair<String, String>("b", "B"), new Pair<String, String>("c", "C") });

            @Override
            public Object a() {
                return elements[0];
            }

            @Override
            public Object b() {
                return elements[1];
            }

            @Override
            public Object c() {
                return elements[2];
            }
        }, false);
    }

    /**
	 * Run tests from command line.
	 *
	 * @param args ignored
	 */
    public static void main(String[] args) {
        Tester t = new Tester();
        new LinkedMapTest(t);
        t.totals();
    }
}
