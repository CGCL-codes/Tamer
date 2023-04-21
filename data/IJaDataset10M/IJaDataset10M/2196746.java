package com.road.index;

import java.util.Date;
import org.apache.lucene.store.RAMDirectory;
import org.testng.annotations.Test;
import com.road.index.TalkIndex;
import com.road.search.SearchException;
import com.road.site.Talk;
import junit.framework.TestCase;

public class TestTalkIndex extends TestCase {

    @Test(groups = { "unit" })
    public void testIndex() throws SearchException {
        TalkIndex ti = new TalkIndex(new RAMDirectory());
        ti.index(new Talk[] { new Talk("t1", "你好，东方路", new Date(), "h@g", "l@g", "浦电路", "浦电路"), new Talk("t1", "t2", "你好，浦建路", new Date(), "l@g", "h@g", "东方路", "东方路") });
        assertEquals(2, ti.searchByTopic("t1").length);
        ti.index(new Talk[] { new Talk("t4", "我很好", new Date(), "h@g", "l@g", "浦电路", "浦电路"), new Talk("t1", "t3", "就这样吧", new Date(), "l@g", "h@g", "东方路", "东方路") });
        assertEquals(3, ti.searchByTopic("t1").length);
        assertEquals(2, ti.searchByUser("l@g").length);
        assertEquals(2, ti.searchByUser("l@g").length);
        assertEquals(2, ti.searchByRoadId("东方路", 10).length);
        assertEquals(1, ti.searchByRoadId("东方路", 1).length);
    }
}
