package net.sf.dbchanges.plan;

import static net.sf.dbchanges.database.DatabaseTD.TESTDB1;
import java.util.Collections;
import net.sf.dbchanges.fs.ScriptTD;
import net.sf.dbchanges.fs.VersionTD;
import net.sf.dbchanges.test.DbChangesTestCase;
import org.junit.Test;

/**
 * @author olex
 */
@SuppressWarnings("unchecked")
public class ExecutionPlanTest extends DbChangesTestCase {

    @Test
    public void test_is_not_empty() throws Exception {
        assertTrue(ExecutionPlanTD.PLAN1.isNotEmpty());
        assertFalse(new ExecutionPlan(TESTDB1).isNotEmpty());
        assertTrue(new ExecutionPlan.Builder(TESTDB1).current(ScriptTD.DATA_LOGS).build().isNotEmpty());
    }

    /**
	 * @see ExecutionPlan#parts() 
	 */
    @Test
    public void test_parts() throws Exception {
        assertEquals(3, ExecutionPlanTD.PLAN1.parts().size());
    }

    /**
	 * @see ExecutionPlan#parts() 
	 */
    @Test
    public void test_no_empty_parts() throws Exception {
        assertEquals(0, new ExecutionPlan.Builder(TESTDB1).build().parts().size());
        assertEquals(0, new ExecutionPlan.Builder(TESTDB1).version(Collections.EMPTY_LIST, VersionTD.VER_10M2).build().parts().size());
    }
}
