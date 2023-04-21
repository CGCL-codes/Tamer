package org.apache.commons.collections.list;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import junit.framework.Test;
import org.apache.commons.collections.BulkTest;

/**
 * Test class for NodeCachingLinkedList, a performance optimised LinkedList.
 * 
 * @version $Revision: 646780 $ $Date: 2008-04-10 13:48:07 +0100 (Thu, 10 Apr 2008) $
 * 
 * @author Jeff Varszegi
 * @author Phil Steitz
 */
public class TestNodeCachingLinkedList extends TestAbstractLinkedList {

    public TestNodeCachingLinkedList(String testName) {
        super(testName);
    }

    public static void main(String args[]) {
        compareSpeed();
        String[] testCaseName = { TestNodeCachingLinkedList.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        return BulkTest.makeSuite(TestNodeCachingLinkedList.class);
    }

    public List makeEmptyList() {
        return new NodeCachingLinkedList();
    }

    public String getCompatibilityVersion() {
        return "3";
    }

    public void testShrinkCache() {
        if (isRemoveSupported() == false || isAddSupported() == false) return;
        resetEmpty();
        NodeCachingLinkedList list = (NodeCachingLinkedList) collection;
        list.addAll(Arrays.asList(new String[] { "1", "2", "3", "4" }));
        list.removeAllNodes();
        ((NodeCachingLinkedList) list).setMaximumCacheSize(2);
        list.addAll(Arrays.asList(new String[] { "1", "2", "3", "4" }));
        checkNodes();
        list.removeNode(list.getNode(0, false));
        list.removeNode(list.getNode(0, false));
        list.removeNode(list.getNode(0, false));
        checkNodes();
        list.addAll(Arrays.asList(new String[] { "1", "2", "3", "4" }));
        checkNodes();
    }

    public static void compareSpeed() {
        NodeCachingLinkedList ncll = new NodeCachingLinkedList();
        LinkedList ll = new LinkedList();
        Object o1 = new Object();
        Object o2 = new Object();
        int loopCount = 4000000;
        long startTime, endTime;
        System.out.println("Testing relative execution time of commonly-used methods...");
        startTime = System.currentTimeMillis();
        for (int x = loopCount; x > 0; x--) {
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
            ll.addFirst(o1);
            ll.addLast(o2);
            ll.removeFirst();
            ll.removeLast();
            ll.add(o1);
            ll.remove(0);
        }
        endTime = System.currentTimeMillis();
        System.out.println("Time with LinkedList: " + (endTime - startTime) + " ms");
        startTime = System.currentTimeMillis();
        for (int x = loopCount; x > 0; x--) {
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
            ncll.addFirst(o1);
            ncll.addLast(o2);
            ncll.removeFirst();
            ncll.removeLast();
            ncll.add(o1);
            ncll.remove(0);
        }
        endTime = System.currentTimeMillis();
        System.out.println("Time with NodeCachingLinkedList: " + (endTime - startTime) + " ms");
    }
}
