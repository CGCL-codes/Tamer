package com.healthmarketscience.rmiio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import junit.framework.TestCase;

/**
 * @author James Ahlborn
 */
public class ConverterIOIteratorTest extends TestCase {

    public ConverterIOIteratorTest(String name) {
        super(name);
    }

    public void testConverter() throws Exception {
        final AtomicBoolean closed = new AtomicBoolean();
        List<String> input = Arrays.asList("13", "21", "-3");
        CloseableIOIterator<String> inputIter = new SimpleRemoteIterator<String>(input) {

            private static final long serialVersionUID = 0L;

            @Override
            public void close() {
                super.close();
                closed.set(true);
            }
        };
        List<Integer> output = new ArrayList<Integer>();
        CloseableIOIterator<Integer> iter = null;
        for (iter = new ConverterIOIterator<String, Integer>(inputIter) {

            @Override
            protected Integer convert(String in) {
                return Integer.valueOf(in);
            }
        }; iter.hasNext(); ) {
            output.add(iter.next());
        }
        iter.close();
        assertTrue(closed.get());
        assertEquals(Arrays.asList(13, 21, -3), output);
    }
}
