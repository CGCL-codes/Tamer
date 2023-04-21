package simpleorm.examples;

import static simpleorm.dataset.SFieldFlags.SDESCRIPTIVE;
import static simpleorm.dataset.SFieldFlags.SPRIMARY_KEY;
import simpleorm.dataset.SDataSet;
import simpleorm.dataset.SFieldInteger;
import simpleorm.dataset.SFieldReference;
import simpleorm.dataset.SFieldScalar;
import simpleorm.dataset.SFieldString;
import simpleorm.dataset.SRecordInstance;
import simpleorm.dataset.SRecordMeta;
import simpleorm.sessionjdbc.SSessionJdbc;
import simpleorm.sessionjdbc.SDataLoader;
import simpleorm.utils.SException;

/**
 * This provides a basic test of foreign key references and in particular
 * overlapping foreign keys. 
 * 
 * (Much simplified in SimpleORM 3.0)
 */
public class ReferenceTest {

    public static class RefYY extends SRecordInstance {

        public static final SRecordMeta<RefYY> meta = new SRecordMeta<RefYY>(RefYY.class, "RefYY");

        public static final SFieldString YY_ID = new SFieldString(meta, "YY_ID", 10, SPRIMARY_KEY);

        public static final SFieldString YNAME = new SFieldString(meta, "YNAME", 10, SDESCRIPTIVE);

        public static final SFieldString XX_ID1 = new SFieldString(meta, "XX_ID1", 10);

        public static final SFieldInteger XX_ID2A = new SFieldInteger(meta, "XX_ID2A");

        public static final SFieldInteger XX_ID2B = new SFieldInteger(meta, "XX_ID2B");

        static final SFieldReference<RefXX> XXRA = new SFieldReference<RefXX>(meta, RefXX.meta, "XXRA", new SFieldScalar[] { XX_ID1, XX_ID2A }, new SFieldScalar[] { RefXX.XX_ID1, RefXX.XX_ID2 });

        static final SFieldReference<RefXX> XXRB = new SFieldReference<RefXX>(meta, RefXX.meta, "XXRB", new SFieldScalar[] { XX_ID1, XX_ID2B }, new SFieldScalar[] { RefXX.XX_ID1, RefXX.XX_ID2 });

        @Override
        public SRecordMeta<RefYY> getMeta() {
            return meta;
        }
    }

    public static class RefXX extends SRecordInstance {

        public static final SRecordMeta<RefXX> meta = new SRecordMeta<RefXX>(RefXX.class, "RefXX");

        public static final SFieldString XX_ID1 = new SFieldString(meta, "XX_ID1", 10, SPRIMARY_KEY);

        public static final SFieldInteger XX_ID2 = new SFieldInteger(meta, "XX_ID2", SPRIMARY_KEY);

        public static final SFieldString XNAME = new SFieldString(meta, "XNAME", 10, SDESCRIPTIVE);

        @Override
        public SRecordMeta<RefXX> getMeta() {
            return meta;
        }
    }

    public static void main(String[] argv) throws Exception {
        SSessionJdbc ses = TestUte.initializeTest(ReferenceTest.class);
        System.err.println(RefXX.meta.toLongerString());
        System.err.println(RefYY.meta.toLongerString());
        System.err.println(ses.getDriver().createTableSQL(RefXX.meta) + ";\n");
        System.err.println(ses.getDriver().createTableSQL(RefYY.meta) + ";\n");
        ses.begin();
        TestUte.dropAllTables(ses);
        ses.rawUpdateDB(ses.getDriver().createTableSQL(RefXX.meta));
        ses.rawUpdateDB(ses.getDriver().createTableSQL(RefYY.meta));
        ses.commit();
        testOverlap();
        testRemoveReference();
        testEndingSpaces();
        SSessionJdbc.getThreadLocalSession().close();
    }

    static void testOverlap() {
        SSessionJdbc ses = SSessionJdbc.getThreadLocalSession();
        ses.begin();
        SDataLoader<RefXX> xl = new SDataLoader<RefXX>(ses, RefXX.meta);
        xl.insertRecords(new Object[][] { { "One", 1, "First1" }, { "One", 2, "First2" }, { "Two", 1, "Second1" }, { "Two", 2, "Second2" }, { "Two", 3, "Second3" } });
        SDataLoader<RefYY> yl = new SDataLoader<RefYY>(ses, RefYY.meta);
        yl.insertRecords(new Object[][] { { "Y1", "FirstY", "One", 1, 2 }, { "Y2", "SecondY", "One", 2, 1 } });
        ses.commit();
        ses.begin();
        RefYY y1 = ses.mustFind(RefYY.meta, "Y1");
        TestUte.assertEqual("First2", y1.findReference(RefYY.XXRB).getString(RefXX.XNAME));
        RefXX x21 = ses.mustFind(RefXX.meta, "Two", 1);
        RefXX x23 = ses.mustFind(RefXX.meta, "Two", 3);
        y1.setReference(RefYY.XXRA, x21);
        TestUte.assertEqual("Second2", y1.findReference(RefYY.XXRB).getString(RefXX.XNAME));
        ses.flush();
        y1.setReference(RefYY.XXRA, x23);
        TestUte.assertEqual("Second2", y1.findReference(RefYY.XXRB).getString(RefXX.XNAME));
        ses.flush();
        y1.setReference(RefYY.XXRA, null);
        TestUte.assertTrue(y1.isNull(RefYY.XX_ID1));
        TestUte.assertTrue(y1.isNull(RefYY.XX_ID2A));
        TestUte.assertEqual("2", y1.getString(RefYY.XX_ID2B));
        TestUte.assertTrue(null == y1.findReference(RefYY.XXRB));
        ses.commit();
    }

    static void testRemoveReference() {
        SSessionJdbc ses = SSessionJdbc.getThreadLocalSession();
        ses.begin();
        RefYY y2 = ses.mustFind(RefYY.meta, "Y2");
        RefXX x1 = y2.findReference(RefYY.XXRA);
        TestUte.assertEqual("First2", x1.getString(RefXX.XNAME));
        x1.setString(RefXX.XNAME, "ToBeDisc.");
        ses.getDataSet().removeRecord(x1);
        TestUte.assertTrue(!x1.isNotDestroyed());
        x1 = y2.findReference(RefYY.XXRA);
        TestUte.assertTrue(x1.isNotDestroyed());
        TestUte.assertEqual("First2", x1.getString(RefXX.XNAME));
        ses.commit();
        ses.begin(new SDataSet());
        y2 = ses.mustFind(RefYY.meta, "Y2");
        x1 = y2.findReference(RefYY.XXRA);
        TestUte.assertEqual("First2", x1.getString(RefXX.XNAME));
        x1.setString(RefXX.XNAME, "ToBeDisc.");
        ses.getDataSet().removeRecord(x1);
        x1 = y2.findReference(RefYY.XXRA);
        TestUte.assertTrue(x1.isNotDestroyed());
        TestUte.assertEqual("First2", x1.getString(RefXX.XNAME));
        SDataSet ds = ses.commitAndDetachDataSet();
        y2 = ds.find(RefYY.meta, "Y2");
        x1 = y2.findReference(RefYY.XXRA);
        TestUte.assertEqual("First2", x1.getString(RefXX.XNAME));
        ds.removeRecord(x1);
        try {
            x1 = y2.findReference(RefYY.XXRA);
            TestUte.assertTrue(false);
        } catch (SException.Data e) {
        }
    }

    static void testEndingSpaces() {
        SDataSet ds = new SDataSet();
        SSessionJdbc ses = SSessionJdbc.getThreadLocalSession();
        ses.begin();
        SDataLoader<RefXX> xl = new SDataLoader<RefXX>(ses, RefXX.meta);
        xl.insertRecords(new Object[][] { { "Spaces ", 1, "Fst space" }, { "Spaces ", 2, "Scd space" } });
        SDataLoader<RefYY> yl = new SDataLoader<RefYY>(ses, RefYY.meta);
        yl.insertRecords(new Object[][] { { "Y3", "ThirdY", "Spaces ", 1, 2 } });
        ses.commit();
        ses.begin(ds);
        RefYY y3 = ses.mustFind(RefYY.meta, "Y3");
        TestUte.assertEqual("Fst space", y3.findReference(RefYY.XXRA).getString(RefXX.XNAME));
        RefXX space2 = ses.mustFind(RefXX.meta, "Spaces ", 2);
        ses.commitAndDetachDataSet();
        TestUte.assertEqual("Scd space", y3.findReference(RefYY.XXRB).getString(RefXX.XNAME));
        RefYY y3again = (RefYY) ds.queryReferencing(space2, RefYY.XXRB).get(0);
        TestUte.assertEqual(y3, y3again);
    }
}
