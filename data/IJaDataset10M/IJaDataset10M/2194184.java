package org.shapelogic.streams;

import junit.framework.TestCase;

/** Test SingleListStream.
 * 
 * @author Sami Badawi
 *
 */
public class SingleListStreamTest extends TestCase {

    ListStream<Integer> singleListStream;

    Integer ANSWER = 42;

    public void setUp() {
        singleListStream = new SingleListStream<Integer>() {

            @Override
            public Integer invoke() {
                return 42;
            }
        };
    }

    public void testGet() {
        assertEquals(ANSWER, singleListStream.get(0));
        assertNull(singleListStream.get(1));
    }

    public void testNext() {
        assertEquals(ANSWER, singleListStream.next());
        assertNull(singleListStream.next());
    }

    public void testGetValue() {
        assertEquals(ANSWER, singleListStream.getValue());
    }
}
