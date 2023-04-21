package dovetaildb.dbservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import dovetaildb.apiservice.ApiBuffer;
import dovetaildb.iter.Iter;
import dovetaildb.scan.Scanner;
import dovetaildb.util.Pair;
import dovetaildb.util.Util;
import junit.framework.TestCase;
import org.json.simple.JSONValue;
import org.junit.Test;

public abstract class DbServiceTest extends TestCase {

    protected abstract DbService createService();

    protected DbService db;

    @Override
    public void setUp() {
    }

    public static Object[] yank(Iter iter) {
        ArrayList<Object> rets = new ArrayList<Object>();
        Object[] buffer = new Object[7];
        while (true) {
            int ct = iter.pullAvailable(buffer, true);
            if (ct == -1) break;
            for (int i = 0; i < ct; i++) {
                rets.add(buffer[i]);
            }
        }
        return rets.toArray();
    }

    public void checkEmpty(long txnId) {
        assertEquals(0, yank(db.query("nobag", txnId, null, null)).length);
        assertEquals(0, yank(db.query("people", txnId, null, null)).length);
        assertEquals(0, yank(db.query("people", txnId, Util.literalMap().put("name", "Joe"), null)).length);
    }

    public void checkTwoInserts(long txnId) {
        assertEquals(2, yank(db.query("people", txnId, Util.literalMap(), null)).length);
        Object[] rets;
        rets = yank(db.query("people", txnId, Util.literalMap().p("name", "Joe"), null));
        assertEquals(1, rets.length);
        assertEquals("Joe", ((DbResult) rets[0]).derefByKey("name").getString());
        rets = yank(db.query("people", txnId, Util.literalMap().p("age", Util.literalList().a(">").a(25)), null));
        assertEquals(1, rets.length);
        assertEquals("Joe", ((DbResult) rets[0]).derefByKey("name").getString());
        assertEquals("{\"gender\":\"m\",\"name\":\"Joe\",\"age\":31.0}", JSONValue.toJSONString(rets[0]));
    }

    long initialEmptyTxnId, twoInsertsTxnId;

    @Test
    public void testAll() throws Exception {
        db = createService();
        db.initialize();
        initialEmptyTxnId = 1;
        db.commit(-1, initialEmptyTxnId, new HashMap<String, ApiBuffer>());
        checkEmpty(initialEmptyTxnId);
        twoInsertsTxnId = 2;
        HashMap<String, ApiBuffer> batch = new HashMap<String, ApiBuffer>();
        ApiBuffer buffer = new ApiBuffer();
        buffer.put("ID1", Util.literalMap().p("name", "Joe").p("gender", "m").p("age", 31));
        buffer.put("ID2", Util.literalMap().p("name", "Jill").p("gender", "f").p("age", 23));
        batch.put("people", buffer);
        db.commit(initialEmptyTxnId, twoInsertsTxnId, batch);
        checkTwoInserts(twoInsertsTxnId);
        checkEmpty(initialEmptyTxnId);
    }
}
