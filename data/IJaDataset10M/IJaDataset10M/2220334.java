package org.rakiura.evm;

import junit.framework.TestCase;

/**
 *
 * <br><br>
 * TestQList.java
 * Created: 25/01/2005 17:30:26 
 *
 * @author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @version @version@ $Revision: 1.4 $
 * 
 */
public class TestQList extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestQList.class);
    }

    public void testAdd() {
        QList l = new QList();
        QNode n1 = new QNode(1, 10);
        QNode n2 = new QNode(2, 5);
        QNode n3 = new QNode(3, 15);
        QNode n4 = new QNode(4, 1);
        QNode n5 = new QNode(5, 13);
        l.add(n1);
        l.add(n2);
        l.add(n3);
        l.add(n4);
        l.add(n5);
        assertEquals(l.find(4), n4);
        assertEquals(l.find(1), n1);
        assertEquals(l.find(2), n2);
        assertEquals(l.find(3), n3);
        assertEquals(l.size(), 5);
        l.remove(n5);
        l.remove(2);
        l.remove(4);
        assertEquals(l.find(1), n1);
        assertEquals(l.find(3), n3);
        assertEquals(l.size(), 2);
    }
}
