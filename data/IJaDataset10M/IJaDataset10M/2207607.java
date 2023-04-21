package org.herasaf.xacml.core.function.impl.setFunction;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.herasaf.xacml.core.function.Function;
import org.herasaf.xacml.core.function.impl.setFunction.DayTimeDurationIntersectionFunction;
import org.herasaf.xacml.core.types.DayTimeDuration;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestIntersection {

    private Function function;

    @BeforeTest
    public void beforeTest() {
        this.function = new DayTimeDurationIntersectionFunction();
    }

    @DataProvider(name = "functionTest")
    public Object[][] createArgs() {
        return new Object[][] { new Object[] { createSet(new String[] { "P1DT3H1M12.45S" }), createSet(new String[] { "P1DT3H1M12.45S" }), createSet(new String[] { "P1DT3H1M12.45S" }) }, new Object[] { createSet(new String[] { "P1DT3H1M12.45S", "P1DT3H1M12S" }), createSet(new String[] { "P1DT3H1M12.45S" }), createSet(new String[] { "P1DT3H1M12.45S" }) }, new Object[] { createSet(new String[] { "P1DT3H1M12.45S", "P1DT3H1M12S" }), createSet(new String[] { "P1DT3H1M12.46S" }), createSet(new String[] {}) }, new Object[] { createSet(new String[] { "P1DT3H1M12.45S", "P1DT3H1M12S" }), createSet(new String[] {}), createSet(new String[] {}) } };
    }

    private List<DayTimeDuration> createSet(String[] strings) {
        List<DayTimeDuration> durations = new ArrayList<DayTimeDuration>();
        for (String str : strings) {
            durations.add(new DayTimeDuration(str));
        }
        return durations;
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "functionTest")
    public void testFunction(List<DayTimeDuration> durations1, List<DayTimeDuration> durations2, List<DayTimeDuration> expectedIntersection) throws Exception {
        List<DayTimeDuration> intersection = (List<DayTimeDuration>) function.handle(durations1, durations2);
        assertTrue(intersection.containsAll(expectedIntersection));
        assertTrue(expectedIntersection.containsAll(intersection));
        assertEquals(intersection.size(), expectedIntersection.size());
    }
}
