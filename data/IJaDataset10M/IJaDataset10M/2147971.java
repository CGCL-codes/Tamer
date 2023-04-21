package com.samskivert.depot;

import java.util.List;
import com.google.common.collect.Lists;
import com.samskivert.depot.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests projection selections, aggregates, etc.
 */
public class ProjectionTest extends TestBase {

    @Before
    public void createRecords() {
        for (int ii = 0; ii < 10; ii++) {
            _repo.insert(createTestRecord(ii));
        }
        _repo.insert(new EnumKeyRecord(EnumKeyRecord.Type.A, "Elvis"));
        _repo.insert(new EnumKeyRecord(EnumKeyRecord.Type.B, "Moses"));
        _repo.insert(new EnumKeyRecord(EnumKeyRecord.Type.C, "Abraham"));
        _repo.insert(new EnumKeyRecord(EnumKeyRecord.Type.D, "Elvis"));
    }

    @After
    public void cleanup() {
        _repo.from(TestRecord.class).whereTrue().delete();
        _repo.from(EnumKeyRecord.class).whereTrue().delete();
    }

    @Test
    public void testProjection() {
        List<Integer> allKeys = _repo.from(TestRecord.class).select(TestRecord.RECORD_ID);
        assertEquals(allKeys, Lists.newArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        List<Integer> someKeys = _repo.from(TestRecord.class).where(TestRecord.RECORD_ID.greaterThan(5)).select(TestRecord.RECORD_ID);
        assertEquals(someKeys, Lists.newArrayList(6, 7, 8, 9));
        List<Tuple2<Integer, String>> data = _repo.from(TestRecord.class).where(TestRecord.RECORD_ID.greaterThan(7)).select(TestRecord.RECORD_ID, TestRecord.NAME);
        List<Tuple2<Integer, String>> want = Lists.newArrayList();
        want.add(Tuple2.create(8, "Elvis"));
        want.add(Tuple2.create(9, "Elvis"));
        assertEquals(data, want);
    }

    @Test
    public void testTupleN() {
        Query<TestRecord> one = _repo.from(TestRecord.class).where(TestRecord.RECORD_ID.eq(1));
        assertEquals(Tuple2.create(1, "Elvis"), one.load(TestRecord.RECORD_ID, TestRecord.NAME));
        assertEquals(Tuple3.create(1, "Elvis", 99), one.load(TestRecord.RECORD_ID, TestRecord.NAME, TestRecord.AGE));
        assertEquals(Tuple4.create(1, "Elvis", 99, "Right here"), one.load(TestRecord.RECORD_ID, TestRecord.NAME, TestRecord.AGE, TestRecord.HOME_TOWN));
        assertEquals(Tuple5.create(1, "Elvis", 99, "Right here", EnumKeyRecord.Type.A), one.load(TestRecord.RECORD_ID, TestRecord.NAME, TestRecord.AGE, TestRecord.HOME_TOWN, TestRecord.TYPE));
    }

    @Test
    public void testProjectedJoin() {
        List<Tuple2<Integer, EnumKeyRecord.Type>> jdata = _repo.from(TestRecord.class).join(TestRecord.NAME, EnumKeyRecord.NAME).select(TestRecord.RECORD_ID, EnumKeyRecord.TYPE);
        assertEquals(20, jdata.size());
    }

    @Test
    public void testNoMatches() {
        Query<TestRecord> empty = _repo.from(TestRecord.class).where(TestRecord.AGE.greaterThan(100));
        assertNull(empty.load(TestRecord.RECORD_ID));
        assertEquals(0, empty.select().size());
        assertNull(empty.load(Funcs.max(TestRecord.RECORD_ID)));
    }

    @Test
    public void testAggregates() {
        List<Tuple2<String, Integer>> ewant = Lists.newArrayList();
        ewant.add(Tuple2.create("Abraham", 1));
        ewant.add(Tuple2.create("Elvis", 2));
        ewant.add(Tuple2.create("Moses", 1));
        assertEquals(ewant, _repo.from(EnumKeyRecord.class).groupBy(EnumKeyRecord.NAME).ascending(EnumKeyRecord.NAME).select(EnumKeyRecord.NAME, Funcs.count(EnumKeyRecord.TYPE)));
    }

    @Test
    public void testFuncs() {
        Query<TestRecord> query = _repo.from(TestRecord.class);
        assertEquals(4, query.load(Funcs.average(TestRecord.RECORD_ID)).intValue());
        assertEquals(4, query.load(Funcs.averageDistinct(TestRecord.RECORD_ID)).intValue());
        assertEquals(10, query.load(Funcs.count(TestRecord.RECORD_ID)).intValue());
        assertEquals(false, query.load(Funcs.every(TestRecord.RECORD_ID.greaterThan(5))));
        assertEquals(true, query.load(Funcs.every(TestRecord.RECORD_ID.lessThan(100))));
        assertEquals(9, query.load(Funcs.max(TestRecord.RECORD_ID)).intValue());
        assertEquals(0, query.load(Funcs.min(TestRecord.RECORD_ID)).intValue());
        assertEquals(45, query.load(Funcs.sum(TestRecord.RECORD_ID)).intValue());
        List<Integer> greatest = Lists.newArrayList(99, 99, 99, 99, 99, 99, 99, 99, 99, 99);
        assertEquals(greatest, query.select(Funcs.greatest(TestRecord.RECORD_ID, TestRecord.AGE)));
        List<Integer> least = Lists.newArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        assertEquals(least, query.select(Funcs.least(TestRecord.RECORD_ID, TestRecord.AGE)));
    }

    @Test
    public void testStringFuncs() {
        Query<TestRecord> first = _repo.from(TestRecord.class).where(TestRecord.RECORD_ID.eq(1));
        assertEquals(5, first.load(StringFuncs.length(TestRecord.NAME)).intValue());
        assertEquals("elvis", first.load(StringFuncs.lower(TestRecord.NAME)));
        assertEquals(3, first.load(StringFuncs.position(Exps.value("vis"), TestRecord.NAME)).intValue());
        assertEquals("lvi", first.load(StringFuncs.substring(TestRecord.NAME, 2, 3)));
        assertEquals("Elvis", first.load(StringFuncs.trim(TestRecord.NAME)));
        assertEquals("ELVIS", first.load(StringFuncs.upper(TestRecord.NAME)));
    }

    @Test
    public void testMathFuncs() {
        Query<TestRecord> first = _repo.from(TestRecord.class).where(TestRecord.RECORD_ID.eq(1));
        assertEquals(99, first.load(MathFuncs.abs(TestRecord.AGE)).intValue());
        assertEquals(1f, first.load(MathFuncs.ceil(TestRecord.AWESOMENESS)).floatValue(), 0);
        assertEquals(0f, first.load(MathFuncs.floor(TestRecord.AWESOMENESS)).floatValue(), 0);
        assertEquals(Math.exp(0.75), first.load(MathFuncs.exp(TestRecord.AWESOMENESS)).doubleValue(), 0.0001);
        assertEquals(Math.log(0.75), first.load(MathFuncs.ln(TestRecord.AWESOMENESS)).doubleValue(), 0.0001);
        assertEquals(Math.PI, first.load(MathFuncs.<Double>pi()), 0.0001);
        assertTrue(first.load(MathFuncs.<Double>random()) < 1.0);
        assertEquals(1, first.load(MathFuncs.round(TestRecord.AWESOMENESS)).intValue());
        assertEquals(1, first.load(MathFuncs.sign(TestRecord.AWESOMENESS)).intValue());
        assertEquals(Math.sqrt(0.75), first.load(MathFuncs.sqrt(TestRecord.AWESOMENESS)).doubleValue(), 0.0001);
        assertEquals(Math.pow(0.75f, 10), first.load(MathFuncs.power(TestRecord.AWESOMENESS, Exps.value(10))).doubleValue(), 0.0001);
    }

    @Test
    public void testSelectInfo() {
        assertEquals("[0 Elvis, 1 Elvis]", _repo.from(TestRecord.class).where(TestRecord.RECORD_ID.lessThan(2)).selectInto(IdName.class, TestRecord.RECORD_ID, TestRecord.NAME).toString());
    }

    @Test
    public void testSelectBuilder() {
        assertEquals("[0 Elvis, 1 Elvis]", _repo.from(TestRecord.class).where(TestRecord.RECORD_ID.lessThan(2)).select(IDNAME_BUILDER, TestRecord.RECORD_ID, TestRecord.NAME).toString());
    }

    protected static class IdName {

        public final int id;

        public final String name;

        public IdName(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return id + " " + name;
        }
    }

    protected static Builder2<IdName, Integer, String> IDNAME_BUILDER = new Builder2<IdName, Integer, String>() {

        public IdName build(Integer a, String b) {
            return new IdName(a, b);
        }
    };

    protected TestRepository _repo = createTestRepository();
}
