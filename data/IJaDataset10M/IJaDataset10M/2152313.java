package org.tripcom.kerneltests.api3;

import java.util.Set;
import org.junit.Test;
import org.openrdf.model.Statement;

public class PolicyTest extends AbstractAPITest {

    @Test
    public void getPolicyTest() throws Exception {
        System.out.println("setting policy");
        api.setPolicy(testTuples, rootspace);
        Set<Statement> result = api.getPolicy(rootspace);
        System.out.println("got policy: " + result);
    }
}
