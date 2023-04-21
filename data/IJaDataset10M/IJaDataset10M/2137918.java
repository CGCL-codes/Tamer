package rafa.math.gen;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import rafa.NameRegistry;
import rafa.math.filter.CropFilter;
import rafa.math.filter.Oolb;
import rafa.math.filter.ShiftFilter;
import rafa.math.filter.SingleValueFilter;

public class FilterStrategyTest {

    private FilterStrategy strategy;

    private NumberGenerator provider;

    private List<SingleValueFilter> filters;

    private final SingleValueFilter add05 = new ShiftFilter(0.5);

    final SingleValueFilter crop133 = new CropFilter(Oolb.NO_VALUE, 1, 3.3);

    @Before
    public void setUp() throws Exception {
        provider = new NumberGenerator("provider", new FunctionStrategy("x"));
        filters = new ArrayList<SingleValueFilter>();
        filters.add(add05);
        strategy = new FilterStrategy(provider, filters);
    }

    @After
    public void tearDown() throws Exception {
        NameRegistry.getInstance().clear();
    }

    @Test
    public void testSetGenerator() throws Exception {
        NumberGenerator another = new NumberGenerator("another", new ConstantStrategy(53.0));
        strategy.setGenerator(another);
        double[] filtered = new double[] { 53.5, 53.5, 53.5, 53.5, 53.5 };
        for (int i = 0; i < filtered.length; i++) {
            assertEquals(filtered[i], strategy.generate(i).get(0));
        }
    }

    @Test
    public void testSetFilters() throws Exception {
        List<SingleValueFilter> otherFilters = new ArrayList<SingleValueFilter>();
        otherFilters.add(crop133);
        strategy.setFilters(otherFilters);
        Double[] filtered = new Double[] { null, 1.0, 2.0, 3.0, null };
        for (int i = 0; i < filtered.length; i++) {
            assertEquals(filtered[i], strategy.generate(i).get(0));
        }
        otherFilters.add(filters.get(0));
        filtered = new Double[] { null, 1.5, 2.5, 3.5, null };
        for (int i = 0; i < filtered.length; i++) {
            assertEquals(filtered[i], strategy.generate(i).get(0));
        }
        strategy.setFilters(filters);
        filtered = new Double[] { 0.5, 1.5, 2.5, 3.5, 4.5 };
        for (int i = 0; i < filtered.length; i++) {
            assertEquals(filtered[i], strategy.generate(i).get(0));
        }
        filters.add(crop133);
        filtered = new Double[] { null, 1.5, 2.5, null, null };
        for (int i = 0; i < filtered.length; i++) {
            assertEquals(filtered[i], strategy.generate(i).get(0));
        }
    }

    @Test
    public void testGenerate() throws Exception {
        double[] filtered = new double[] { 0.5, 1.5, 2.5, 3.5, 4.5 };
        for (int i = 0; i < filtered.length; i++) {
            assertEquals(filtered[i], strategy.generate(i).get(0));
        }
    }

    @Test
    public void testGetDependencies() {
    }
}
