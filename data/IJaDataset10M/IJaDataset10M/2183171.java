package org.rakiura.evm;

import junit.framework.TestCase;

/**
 *
 * <br><br>
 * TestIntStack.java
 * Created: 12/08/2004 11:22:08 
 *
 * @author <a href="mailto:mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @version @version@ $Revision: 1.6 $
 */
public class TestIntStack extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestIntList.class);
    }

    public void testPushIntStack() throws SizeLimitExceededException {
        IntStack s1 = new IntStack();
        IntStack s2 = new IntStack();
        s1.push(1);
        s1.push(2);
        s1.push(3);
        s2.push(0);
        s2.push(s1);
        assertEquals(3, s2.peek());
        assertEquals(0, s2.get(0));
        assertEquals(1, s2.get(1));
        assertEquals(2, s2.get(2));
        assertEquals(3, s2.get(3));
        assertEquals(0, s2.get(4));
        assertEquals(4, s2.size());
        assertEquals(3, s2.pop());
        assertEquals(2, s2.pop());
        assertEquals(1, s2.pop());
        assertEquals(0, s2.pop());
        assertEquals(0, s2.pop());
    }

    public void testGet() throws SizeLimitExceededException {
        IntStack s = new IntStack();
        s.push(0);
        s.push(1);
        s.push(2);
        s.push(3);
        s.push(0);
        assertEquals(0, s.peek());
        assertEquals(0, s.get(0));
        assertEquals(1, s.get(1));
        assertEquals(2, s.get(2));
        assertEquals(3, s.get(3));
        assertEquals(0, s.get(4));
        assertEquals(0, s.get(5));
        assertEquals(1, s.get(6));
        assertEquals(2, s.get(7));
        assertEquals(3, s.get(8));
    }

    public void testPushint() throws SizeLimitExceededException {
        long start = System.currentTimeMillis();
        final int MAX = 100000;
        final IntStack s = new IntStack();
        for (int i = 0; i < MAX; i++) {
            s.push(21);
        }
        long mid = System.currentTimeMillis();
        for (int i = 0; i < MAX; i++) {
            s.pop();
        }
        assertEquals(0, s.pop());
        long end1 = System.currentTimeMillis();
        for (int i = 0; i < MAX; i++) {
            s.push(21);
        }
        long end2 = System.currentTimeMillis();
        for (int i = 0; i < MAX; i++) {
            s.pop();
        }
        assertEquals(0, s.pop());
        long end3 = System.currentTimeMillis();
        System.out.println("[EVM IntStack] (" + MAX + ")  push:" + (mid - start) + "   pop:" + (end1 - mid) + "   push again:" + (end2 - end1) + "   pop again:" + (end3 - end2) + " [millis]");
        start = System.currentTimeMillis();
        java.util.Stack<Integer> js = new java.util.Stack<Integer>();
        for (int i = 0; i < MAX; i++) {
            js.push(new Integer(21));
        }
        mid = System.currentTimeMillis();
        for (int i = 0; i < MAX; i++) {
            js.pop();
        }
        end1 = System.currentTimeMillis();
        for (int i = 0; i < MAX; i++) {
            js.push(new Integer(21));
        }
        end2 = System.currentTimeMillis();
        for (int i = 0; i < MAX; i++) {
            js.pop();
        }
        end3 = System.currentTimeMillis();
        System.out.println("[Java ObjStack] (" + MAX + ") push:" + (mid - start) + "  pop:" + (end1 - mid) + "  push again:" + (end2 - end1) + " pop again:" + (end3 - end2) + " [millis]");
    }
}
