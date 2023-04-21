package com.netx.ut.generics.R1;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import com.netx.generics.R1.collections.*;
import com.netx.generics.R1.util.UnitTester;
import org.testng.annotations.Test;

public class UTCollections extends UnitTester {

    @Test
    public void testImmutables() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(100);
        list.add(200);
        list.add(300);
        list.add(400);
        list.add(500);
        IList<Integer> il = new IList<Integer>(list);
        Iterator<Integer> it = il.iterator();
        for (int value = 100; it.hasNext(); value += 100) {
            assertEquals(it.next(), value);
        }
        Integer[] array = new Integer[] { 100, 200, 300, 400, 500 };
        il = new IList<Integer>(array);
        it = il.iterator();
        for (int value = 100; it.hasNext(); value += 100) {
            assertEquals(it.next(), value);
        }
    }

    @Test
    public void testChain() {
        Chain<String> chain = new Chain<String>();
        assertTrue(chain.isEmpty());
        chain.add("four");
        chain.add("three");
        Node<String> two = chain.add("two");
        chain.add("one");
        assertFalse(chain.isEmpty());
        assertEquals(chain.size(), 4);
        _iterate(chain);
        two.addBefore("one.5");
        two.addAfter("two.5");
        _iterate(chain);
        two.remove();
        println("removed element: " + two.getElement());
        try {
            two.remove();
            fail();
        } catch (IllegalStateException ise) {
            println(ise);
        }
        try {
            two.setElement("abc");
            fail();
        } catch (IllegalStateException ise) {
            println(ise);
        }
        chain.getHead().addBefore("zero");
        chain.getTail().addAfter("five");
        println("toString(): " + chain.toString());
        _iterate(chain);
        Iterator<String> it = chain.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.endsWith(".5")) {
                it.remove();
            }
        }
        _iterate(chain);
        assertEquals(chain.size(), 5);
        chain.getHead().remove();
        chain.getTail().remove();
        assertEquals(chain.size(), 3);
        _iterate(chain);
        chain.clear();
        assertEquals(chain.size(), 0);
        assertTrue(chain.isEmpty());
    }

    private void _iterate(Chain<String> chain) {
        print("chain's elements: ");
        printIteratorInLine(chain.iterator());
    }

    @Test
    public void testVector() {
        Vector<String> v = new Vector<String>();
        assertTrue(v.isEmpty());
        assertEquals(v.size(), 0);
        String[] array = new String[] { "one", "two", "three", "four", "five" };
        for (int i = 0; i < array.length; i++) {
            v.append(array[i]);
        }
        assertEquals(v.size(), 5);
        for (int i = 0; i < v.size(); i++) {
            assertEquals(array[i], v.get(i));
        }
    }
}
