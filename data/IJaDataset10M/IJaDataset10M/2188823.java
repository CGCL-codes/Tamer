package org.helianto.process.filter;

import static org.junit.Assert.assertEquals;
import org.helianto.core.Entity;
import org.helianto.core.test.EntityTestSupport;
import org.helianto.process.MeasurementTechnique;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mauricio Fernandes de Castro
 */
public class MeasurementTechniqueFilterAdapterTests {

    public static String OB = "order by alias.measurementTechniqueCode ";

    public static String C0 = "alias.entity.id = 0 ";

    public static String C1 = "AND alias.measurementTechniqueCode = 'CODE' ";

    public static String C2 = "AND lower(alias.measurementTechniqueName) like '%name%' ";

    @Test
    public void empty() {
        assertEquals(C0 + OB, filter.createCriteriaAsString());
    }

    @Test
    public void select() {
        form.setMeasurementTechniqueCode("CODE");
        assertEquals(C0 + C1, filter.createCriteriaAsString());
    }

    @Test
    public void filterName() {
        form.setMeasurementTechniqueName("NAME");
        assertEquals(C0 + C2 + OB, filter.createCriteriaAsString());
    }

    private MeasurementTechniqueFilterAdapter<MeasurementTechnique> filter;

    private MeasurementTechnique form;

    @Before
    public void setUp() {
        Entity entity = EntityTestSupport.createEntity();
        form = new MeasurementTechnique(entity, "");
        filter = new MeasurementTechniqueFilterAdapter<MeasurementTechnique>(form);
    }
}
