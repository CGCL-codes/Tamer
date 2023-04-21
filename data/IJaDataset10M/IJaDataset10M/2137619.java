package au.com.lastweekend.openjaws.units;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Test;
import au.com.lastweekend.openjaws.api.WindUnitValue;

public class WindUnitValueTest {

    @Test
    public void testFormat() throws Exception {
        assertThat(String.format("%s", WindUnitValue.NULL), is("null"));
        assertThat(String.format("%s", new WindUnitValue(0, WindUnit.METRES_PER_SECOND)), is("0.000000m/s -"));
        assertThat(String.format("%s", new WindUnitValue(0, WindUnit.METRES_PER_SECOND, 180.0)), is("0.000000m/s S"));
        assertThat(String.format("%s", new WindUnitValue(0, WindUnit.METRES_PER_SECOND, 212.0)), is("0.000000m/s SWbS"));
        assertThat(String.format("%#2.3s", new WindUnitValue(10.23, WindUnit.METRES_PER_SECOND, 180.0)), is("10.230 m/s S"));
        assertThat(String.format("%#15.3s", new WindUnitValue(10.23, WindUnit.METRES_PER_SECOND, 180.0)), is("   10.230 m/s S"));
        assertThat(String.format("%-15.3s", new WindUnitValue(10.23, WindUnit.METRES_PER_SECOND, 180.0)), is("10.230m/s S    "));
        assertThat(String.format("%-15.3s", new WindUnitValue(10.23, WindUnit.METRES_PER_SECOND, 212.0)), is("10.230m/s SWbS "));
    }
}
