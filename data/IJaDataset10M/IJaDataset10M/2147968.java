package org.helianto.partner.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.helianto.core.Entity;
import org.helianto.core.test.EntityTestSupport;
import org.helianto.partner.PrivateEntity;
import org.helianto.partner.filter.PrivateEntityFilterAdapter;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Maur�cio Fernandes de Castro
 */
public class PrivateEntityFilterAdapterTests {

    @Test
    public void constructor() {
        Entity entity = EntityTestSupport.createEntity();
        filter = new PrivateEntityFilterAdapter(entity, "ALIAS");
        assertSame(entity, filter.getForm().getEntity());
        assertEquals("ALIAS", filter.getForm().getEntityAlias());
    }

    public static String C1 = "alias.entity.id = 0 ";

    public static String C2 = "AND alias.entityAlias = 'ALIAS' ";

    public static String C3 = "AND lower(alias.entityName) like '%name%' ";

    @Test
    public void empty() {
        assertEquals(C1, filter.createCriteriaAsString());
    }

    @Test
    public void select() {
        sample.setPartnerAlias("ALIAS");
        assertEquals(C1 + C2, filter.createCriteriaAsString());
    }

    @Test
    public void name() {
        sample.setPartnerName("NAME");
        assertEquals(C1 + C3, filter.createCriteriaAsString());
    }

    private PrivateEntityFilterAdapter filter;

    private PrivateEntity sample;

    @Before
    public void setUp() {
        Entity entity = EntityTestSupport.createEntity();
        sample = new PrivateEntity(entity, "");
        filter = new PrivateEntityFilterAdapter(sample);
    }
}
