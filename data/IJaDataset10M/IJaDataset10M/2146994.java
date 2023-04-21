package net.sf.unruly.spike;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Jeff Drost
 */
public class ListIteratorTests {

    private static final Log LOG = LogFactory.getLog(ListIteratorTests.class);

    @Test
    public void testOne() {
        int size = 10;
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        ListIterator<Integer> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            int nextIndex = listIterator.nextIndex();
            int previousIndex = listIterator.previousIndex();
            int value = listIterator.next();
            LOG.info("nextIndex=" + nextIndex + "; previousIndex=" + previousIndex + "; value=" + value);
        }
    }
}
