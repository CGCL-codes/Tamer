package org.tripcom.kerneltests.api3;

import static org.junit.Assert.*;
import java.util.Set;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.tripcom.integration.entry.Template;

public class OutSynchroneTest extends AbstractAPITest {

    @Test
    public void outSynchroneTest() throws Exception {
        Set<Statement> tuples = this.createTestTuples(0, 5);
        Set<Set<Statement>> result;
        api.create(this.rootspace);
        api.out(tuples, rootspace);
        Thread.sleep(2500);
        result = api.inmultiple(new Template(query), rootspace, 1000);
        assertEquals(result.iterator().next().size(), 5);
        api.outSynchrone(tuples, rootspace);
        Thread.sleep(2500);
        result = api.inmultiple(new Template(query), rootspace, 1000);
        assertEquals(result.iterator().next().size(), 5);
        api.outSynchrone(tuples.iterator().next(), rootspace);
        Thread.sleep(2500);
        Set<Statement> result2 = api.in(new Template(query), rootspace, 1000);
        assertEquals(result2.size(), 1);
    }
}
