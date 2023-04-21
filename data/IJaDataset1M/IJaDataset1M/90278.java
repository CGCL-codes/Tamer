package pub.test;

import junit.framework.*;
import java.util.*;
import pub.db.annotation_task.GenesWithHits;

public class GenesWithHitsTest extends DatabaseTestCase {

    private List genesLoaded;

    private GenesWithHits tool;

    public void setUp() throws Exception {
        super.setUp();
        genesLoaded = SampleData.addTwoGenes(conn);
        tool = new GenesWithHits(conn);
    }

    public void testNoHits() {
        assertEquals(0, tool.size());
    }
}
