package de.jmulti.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCSArray;
import com.jstatcom.ts.TS;
import com.jstatcom.ts.TSDate;
import com.jstatcom.ts.TSHolder;
import de.jmulti.tools.ForecastHelper;

public class ToolsTest {

    @Test
    public void testForecastHelper() {
        double[] data = new double[] { 2, 3, 4, 5, 6, 7 };
        TS ts = new TS(data, "a", TSDate.valueOf("1970 Q1"));
        TSHolder.getInstance().addTS(ts);
        JSCNArray selData = new JSCNArray("data", new double[] { 2, 3, 4 });
        JSCNArray forecData = ForecastHelper.extrapolateSelData(new JSCSArray("names", "a"), selData, null, TSDate.valueOf("1970 Q4"), 3);
        assertEquals(forecData.intAt(0, 0), 5);
        assertEquals(forecData.intAt(1, 0), 6);
        assertEquals(forecData.intAt(2, 0), 7);
    }
}
