package com.ohua.tests.operators;

import java.util.logging.Level;
import org.junit.Test;
import com.ohua.engine.operators.PeekOperator;
import com.ohua.engine.utils.OhuaLoggerFactory;
import com.ohua.tests.AbstractIOTestCase;

public class testAccumulatorOperator extends AbstractIOTestCase {

    /**
   * We use 3 identical generators and join them non-deterministically. Afterwards we accumulate
   * the records and put them into a database.<br>
   * To verify the test is working, we check not only for the amount of records in the target
   * table but also make sure that the accumulations worked properly. All should have count 3.
   * While the sums should be between 3 and 1500.
   * @throws Throwable
   */
    @Test
    public void testBasic() throws Throwable {
        outputLogToFile(OhuaLoggerFactory.getLogIDForOperator(PeekOperator.class, "Logger", "6"), Level.INFO);
        runFlowNoAssert(getTestMethodInputDirectory() + "basic-accumulator-correctness-process.xml");
        tableRegressionCheck("accumulation_table", 500);
        columnBoundaryCheck("accumulation_table", "accumulate1_sum", 3, 1500);
        columnBoundaryCheck("accumulation_table", "accumulate2_sum", 3, 1500);
        columnBoundaryCheck("accumulation_table", "count", 3, 3);
    }

    /**
   * This test case should verify that the output of the Accumulator is sorted. We therefore
   * just take the same setup as above but write the data to a file instead of a database.
   * 
   */
    @Test
    public void testSortedOutput() throws Throwable {
        runFlow(getTestMethodInputDirectory() + "basic-accumulator-sorted-output-correctness-process.xml");
    }
}
