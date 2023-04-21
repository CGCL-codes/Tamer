package net.ontopia.utils;

import java.util.*;
import junit.framework.TestCase;

public abstract class AbstractCollectionTest extends TestCase {

    protected int intended_size = 8;

    public AbstractCollectionTest(String name) {
        super(name);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    protected Collection getCollection(int size) {
        Collection list = new ArrayList();
        int c = 65;
        for (int i = 0; i < size; i++) {
            list.add("" + (char) (c + i));
        }
        return list;
    }

    public void printCollection(Collection c) {
        Iterator i = c.iterator();
        System.out.println();
        while (i.hasNext()) {
            System.out.print(i.next());
        }
        System.out.println();
    }

    protected void testCollection(Collection coll, Collection identical, Collection smaller) {
        if (coll.size() > 0) assertTrue("coll is not empty[1]", coll.isEmpty() == false); else assertTrue("coll is empty[1]", coll.isEmpty());
        assertTrue("coll size[1]", coll.size() == intended_size);
        if (coll.size() > 0) assertTrue("coll is not empty[2]", coll.isEmpty() == false); else assertTrue("coll is empty[2]", coll.isEmpty());
        assertTrue("coll size[2]", coll.size() == intended_size);
        assertTrue("coll toarray[1]", coll.toArray().length == intended_size);
        assertTrue("coll equals identical" + coll.size() + "" + identical.size(), coll.equals(identical));
        assertTrue("coll contains all", coll.containsAll(identical));
        if (coll.size() > 0) assertTrue("coll contains A", coll.contains("A"));
        if (coll.size() > 1) assertTrue("coll contains B", coll.contains("B"));
        assertTrue("coll contains all smaller", coll.containsAll(smaller));
        if (coll.size() > 0) assertTrue("smaller contains all coll", !smaller.containsAll(coll)); else assertTrue("smaller contains all coll", smaller.containsAll(coll));
        if (coll.size() > 0) assertTrue("coll equals smaller", !coll.equals(smaller)); else assertTrue("coll equals smaller", coll.equals(smaller));
        assertTrue("coll equals identical", coll.equals(identical));
    }
}
