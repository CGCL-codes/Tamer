package org.jaqlib;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.jaqlib.core.bean.BeanConventionMappingStrategy;
import org.jaqlib.core.bean.BeanMapping;
import org.jaqlib.core.bean.FieldMapping;

public class BeanConventionMappingStrategyTest extends TestCase {

    private BeanConventionMappingStrategy strategy;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        strategy = new BeanConventionMappingStrategy();
    }

    public void testExecute_Null() {
        try {
            strategy.getMappings(null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testExecute() {
        BeanMapping<AccountImpl> mapping = new BeanMapping<AccountImpl>(AccountImpl.class);
        mapping.setMappingStrategy(strategy);
        List<String> results = getResults(mapping);
        assertEquals(6, results.size());
        assertTrue(results.contains("id"));
        assertTrue(results.contains("lastName"));
        assertTrue(results.contains("firstName"));
        assertTrue(results.contains("balance"));
        assertTrue(results.contains("creditRating"));
        assertTrue(results.contains("active"));
    }

    private List<String> getResults(BeanMapping<AccountImpl> result) {
        List<String> results = new ArrayList<String>();
        for (FieldMapping<?> fieldMapping : result) {
            results.add(fieldMapping.getTargetName());
        }
        return results;
    }
}
