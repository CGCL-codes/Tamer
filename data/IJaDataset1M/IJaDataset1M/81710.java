package org.tranche.hash.span;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.tranche.hash.BigHash;
import org.tranche.util.DevUtil;
import org.tranche.util.TrancheTestCase;

/**
 *
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class HashSpanCollectionTest extends TrancheTestCase {

    public void testIsFullHashSpan() throws Exception {
        {
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                hashSpans.add(new HashSpan(HashSpan.FIRST, HashSpan.LAST));
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                assertEquals(true, hsc.isFullHashSpan());
            }
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                hashSpans.add(new HashSpan(HashSpan.FIRST, HashSpan.LAST.getPrevious().getPrevious()));
                hashSpans.add(new HashSpan(HashSpan.LAST.getPrevious(), HashSpan.LAST));
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                assertEquals(true, hsc.isFullHashSpan());
            }
        }
        {
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                assertEquals(false, hsc.isFullHashSpan());
            }
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                hashSpans.add(new HashSpan(HashSpan.FIRST.getNext(), HashSpan.LAST));
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                assertEquals(false, hsc.isFullHashSpan());
            }
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                hashSpans.add(new HashSpan(HashSpan.FIRST, HashSpan.LAST.getPrevious()));
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                assertEquals(false, hsc.isFullHashSpan());
            }
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                hashSpans.add(new HashSpan(HashSpan.FIRST, HashSpan.LAST.getPrevious().getPrevious().getPrevious()));
                hashSpans.add(new HashSpan(HashSpan.LAST.getPrevious(), HashSpan.LAST));
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                assertEquals(false, hsc.isFullHashSpan());
            }
        }
    }

    public void testGetMissingHashSpans() throws Exception {
        {
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                hashSpans.add(new HashSpan(HashSpan.FIRST, HashSpan.LAST));
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                assertEquals(true, hsc.getMissingHashSpans().isEmpty());
            }
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                hashSpans.add(new HashSpan(HashSpan.FIRST, HashSpan.LAST.getPrevious().getPrevious()));
                hashSpans.add(new HashSpan(HashSpan.LAST.getPrevious(), HashSpan.LAST));
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                assertEquals(true, hsc.getMissingHashSpans().isEmpty());
            }
        }
        {
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                Collection<HashSpan> missing = hsc.getMissingHashSpans();
                assertEquals(1, missing.size());
                HashSpan[] array = missing.toArray(new HashSpan[0]);
                assertEquals(array[0].getFirst().toString(), HashSpan.FIRST.toString());
                assertEquals(array[0].getLast().toString(), HashSpan.LAST.toString());
            }
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                hashSpans.add(new HashSpan(HashSpan.FIRST.getNext(), HashSpan.LAST));
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                Collection<HashSpan> missing = hsc.getMissingHashSpans();
                assertEquals(1, missing.size());
                HashSpan[] array = missing.toArray(new HashSpan[0]);
                assertEquals(HashSpan.FIRST.toString(), array[0].getFirst().toString());
                assertEquals(HashSpan.FIRST.toString(), array[0].getLast().toString());
            }
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                hashSpans.add(new HashSpan(HashSpan.FIRST, HashSpan.LAST.getPrevious()));
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                Collection<HashSpan> missing = hsc.getMissingHashSpans();
                assertEquals(1, missing.size());
                HashSpan[] array = missing.toArray(new HashSpan[0]);
                assertEquals(HashSpan.LAST.toString(), array[0].getFirst().toString());
                assertEquals(HashSpan.LAST.toString(), array[0].getLast().toString());
            }
            {
                List<HashSpan> hashSpans = new LinkedList<HashSpan>();
                BigHash middleHash = DevUtil.getRandomBigHash();
                hashSpans.add(new HashSpan(HashSpan.FIRST, middleHash.getPrevious()));
                hashSpans.add(new HashSpan(middleHash.getNext(), HashSpan.LAST));
                HashSpanCollection hsc = new HashSpanCollection(hashSpans);
                Collection<HashSpan> missing = hsc.getMissingHashSpans();
                assertEquals(1, missing.size());
                HashSpan[] array = missing.toArray(new HashSpan[0]);
                assertEquals(middleHash.toString(), array[0].getFirst().toString());
                assertEquals(middleHash.toString(), array[0].getLast().toString());
            }
        }
    }

    public void testAreEqual() throws Exception {
        {
            Collection<HashSpan> c1 = new HashSet<HashSpan>();
            Collection<HashSpan> c2 = new HashSet<HashSpan>();
            HashSpan hs1 = DevUtil.makeRandomHashSpan();
            HashSpan hs2 = hs1.clone();
            c1.add(hs1);
            c2.add(hs2);
            assertTrue(HashSpanCollection.areEqual(c1, c2));
        }
        {
            {
                Collection<HashSpan> c1 = new HashSet<HashSpan>();
                Collection<HashSpan> c2 = new HashSet<HashSpan>();
                c1.add(DevUtil.makeRandomHashSpan());
                c1.add(DevUtil.makeRandomHashSpan());
                c2.add(DevUtil.makeRandomHashSpan());
                assertFalse(HashSpanCollection.areEqual(c1, c2));
            }
            {
                Collection<HashSpan> c1 = new HashSet<HashSpan>();
                Collection<HashSpan> c2 = new HashSet<HashSpan>();
                HashSpan hs1 = DevUtil.makeRandomHashSpan();
                HashSpan hs2 = DevUtil.makeRandomHashSpan();
                c1.add(hs1);
                c2.add(hs2);
                assertFalse(HashSpanCollection.areEqual(c1, c2));
            }
        }
    }

    public void testMerge() throws Exception {
        {
            HashSpan hs1 = DevUtil.makeRandomHashSpan();
            HashSpan hs2 = new HashSpan(hs1.getFirst().getNext(), hs1.getLast().getNext());
            HashSpan merged = HashSpanCollection.merge(hs1, hs2);
            assertEquals(hs1.getFirst(), merged.getFirst());
            assertEquals(hs2.getLast(), merged.getLast());
        }
        {
            HashSpan hs1 = DevUtil.makeRandomHashSpan();
            HashSpan hs2 = new HashSpan(hs1.getLast().getNext().getNext(), hs1.getLast().getNext().getNext());
            assertEquals(null, HashSpanCollection.merge(hs1, hs2));
        }
    }

    public void testMergeCollection() throws Exception {
        {
            HashSpan hs = DevUtil.makeRandomHashSpan();
            HashSpan hs2 = new HashSpan(hs.getLast().getPrevious(), DevUtil.getRandomBigHash());
            List<HashSpan> c = new LinkedList<HashSpan>();
            c.add(hs);
            c.add(hs2);
            HashSpanCollection.merge(c);
            assertEquals(1, c.size());
        }
        {
            HashSpan hs = DevUtil.makeRandomHashSpan();
            HashSpan hs2 = new HashSpan(hs.getLast().getNext().getNext(), hs.getLast().getNext().getNext().getNext());
            List<HashSpan> c = new LinkedList<HashSpan>();
            c.add(hs);
            c.add(hs2);
            HashSpanCollection.merge(c);
            assertEquals(2, c.size());
        }
    }
}
