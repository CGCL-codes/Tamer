package com.anasoft.os.daofusion.bitemporal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link BitemporalTrace}.
 */
public class BitemporalTraceTest {

    private static final long INFINITY = TimeUtils.ACTUAL_END_OF_TIME;

    private ArrayList<Bitemporal> data;

    private BitemporalTrace tested;

    private Bitemporal a_1;

    private Bitemporal a_2;

    private Bitemporal b_1;

    private Bitemporal b_2;

    private Bitemporal c_1;

    private Bitemporal c_2;

    private Bitemporal d;

    @Before
    public void setUp() {
        data = new ArrayList<Bitemporal>();
        tested = new BitemporalTrace(data);
        a_1 = bitemporalMock(0, INFINITY, 0, 10);
        a_2 = bitemporalMock(0, 15, 10, INFINITY);
        b_1 = bitemporalMock(15, INFINITY, 10, 20);
        b_2 = bitemporalMock(15, 30, 20, INFINITY);
        c_1 = bitemporalMock(30, INFINITY, 20, 30);
        c_2 = bitemporalMock(30, 45, 30, INFINITY);
        d = bitemporalMock(45, INFINITY, 30, INFINITY);
        data.addAll(Arrays.asList(new Bitemporal[] { a_1, a_2, b_1, b_2, c_1, c_2, d }));
    }

    @Test
    public void testHistory() {
        assertThat(tested.getHistory(new DateTime(0)).size(), is(1));
        assertThat(tested.getHistory(new DateTime(10)).size(), is(2));
        assertThat(tested.getHistory(new DateTime(20)).size(), is(3));
        assertThat(tested.getHistory(new DateTime(30)).size(), is(4));
        assertThat(tested.getHistory(new DateTime(INFINITY - 1)).size(), is(4));
    }

    @Test
    public void testEvolution() {
        assertThat(tested.getEvolution(new DateTime(0)).size(), is(2));
        assertThat(tested.getEvolution(new DateTime(15)).size(), is(3));
        assertThat(tested.getEvolution(new DateTime(30)).size(), is(4));
        assertThat(tested.getEvolution(new DateTime(45)).size(), is(4));
        assertThat(tested.getEvolution(new DateTime(INFINITY - 1)).size(), is(4));
    }

    @Test
    public void testGet() throws Exception {
        Collection<Bitemporal> collection = tested.get(new DateTime(15), new DateTime(20));
        assertThat(collection.size(), is(1));
        assertThat(collection.contains(b_2), is(true));
        Collection<Bitemporal> collection2 = tested.get(new DateTime(0), new DateTime(0));
        assertThat(collection2.size(), is(1));
        assertThat(collection2.contains(a_1), is(true));
        Collection<Bitemporal> collection3 = tested.get(new DateTime(50), new DateTime(50));
        assertThat(collection3.size(), is(1));
        assertThat(collection3.contains(d), is(true));
    }

    @Test
    public void testGetItemsThatNeedToBeEnded() throws Exception {
        TimeUtils.setReference(new DateTime(100));
        Collection<Bitemporal> itemsThatNeedToBeEnded = tested.getItemsThatNeedToBeEnded(bitemporalMock(60, INFINITY, 40, INFINITY));
        assertThat(itemsThatNeedToBeEnded.size(), is(1));
        assertThat(itemsThatNeedToBeEnded.contains(d), is(true));
        Collection<Bitemporal> itemsThatNeedToBeEnded2 = tested.getItemsThatNeedToBeEnded(bitemporalMock(10, INFINITY, 10, INFINITY));
        assertThat(itemsThatNeedToBeEnded2.size(), is(4));
        assertThat(itemsThatNeedToBeEnded2.contains(d), is(true));
        assertThat(itemsThatNeedToBeEnded2.contains(c_2), is(true));
        assertThat(itemsThatNeedToBeEnded2.contains(b_2), is(true));
        assertThat(itemsThatNeedToBeEnded2.contains(a_2), is(true));
    }

    private Bitemporal bitemporalMock(long validFrom, long validTo, long recordFrom, long recordTo) {
        return new BitemporalMock(new Interval(validFrom, validTo), new Interval(recordFrom, recordTo));
    }

    public static class BitemporalMock implements Bitemporal {

        private Interval validity;

        private Interval record;

        public BitemporalMock(Interval validity, Interval record) {
            this.validity = validity;
            this.record = record;
        }

        public Bitemporal copyWith(Interval validityInterval) {
            return new BitemporalMock(validityInterval, record);
        }

        public void end() {
            this.record = TimeUtils.interval(record.getStart(), TimeUtils.now());
        }

        public Interval getRecordInterval() {
            return record;
        }

        public Interval getValidityInterval() {
            return validity;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((record == null) ? 0 : record.hashCode());
            result = prime * result + ((validity == null) ? 0 : validity.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            BitemporalMock other = (BitemporalMock) obj;
            if (record == null) {
                if (other.record != null) return false;
            } else if (!record.equals(other.record)) return false;
            if (validity == null) {
                if (other.validity != null) return false;
            } else if (!validity.equals(other.validity)) return false;
            return true;
        }

        @Override
        public String toString() {
            return validity + " " + record;
        }

        public void resurrect() {
            this.record = TimeUtils.from(record.getStart());
        }
    }
}
