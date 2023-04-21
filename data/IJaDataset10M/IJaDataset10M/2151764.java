package sketch.apache.collection;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.collections.comparators.FixedOrderComparator;
import org.apache.commons.collections.list.CursorableLinkedList;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.collections.set.ListOrderedSet;
import junit.framework.TestCase;
import sketch.ounit.Observer;
import sketch.ounit.Values;
import sketch.specs.annotation.TestSketch;

/***
 * 
 * Head comment
 * */
public class ReWriteRepetitiveUnitTests extends TestCase {

    @TestSketch
    public void rewriteTestConstructorException() {
        try {
            Integer i = Values.choose(0, -20);
            {
                Integer k = 20;
                new CircularFifoBuffer(i);
                new CircularFifoBuffer(k);
                {
                    Integer j = Values.choose(45, 46);
                }
            }
        } catch (NullPointerException ex) {
            {
                Integer y = Values.choose(200, 300);
            }
            return;
        }
    }

    @TestSketch
    void rewriteTestConstructorException0() {
        try {
            Integer i = Values.choose(0, -20);
            {
                {
                    new CircularFifoBuffer(i);
                    int k = 10;
                    k = 30;
                }
                int k = Values.choose(40, 80);
            }
        } catch (NullPointerException ex) {
            int k = Values.choose(60, 70);
            return;
        }
    }

    @TestSketch
    void testVariable() {
        {
            for (int infor = 0; infor < 100; infor++) {
                System.out.println(infor);
            }
            int infor = Values.choose(20, 30);
        }
    }
}
