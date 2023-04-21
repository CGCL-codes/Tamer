package com.peusoft.ptcollect.core.persistance.domain;

import java.text.DateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit test for {@link Configuration}.
 * @author Yauheni Prykhodzka
 * @version 1.0
 * @see DateFormat#SHORT
 */
public class ConfigurationTest extends DomainObjectTest {

    private Log LOGGER = LogFactory.getLog(ConfigurationTest.class);

    /**
     * Test method for {@link com.peu.pt.shared.persistance.domain.config.Configuration#getLogicalKey()}.
     */
    @Test
    public final void testGetLogicalKey() {
        Configuration config = new Configuration();
        config.setScale(new Integer(1));
        Object[] key = config.getLogicalKey();
        assertNotNull(key);
        assertEquals("Wrong lenght of the key: ", 1, key.length);
        assertEquals("Wrong value of the key: ", new Integer(1), key[0]);
    }

    /**
     * Tests the saving of address.
     */
    @Test
    public void testSave() {
        LOGGER.info("testSave begins...");
        Configuration config = new Configuration();
        config.setScale(new Integer(1));
        em.persist(config);
        Configuration config2 = em.find(Configuration.class, config.getId());
        assertEquals("Wrong configuration: ", config, config2);
        LOGGER.info("testSave finishs...");
    }

    /**
     * Test method for {@link com.peu.pt.shared.persistance.domain.config.Configuration#getScale()}.
     */
    @Test
    public final void testScale() {
        Configuration config = new Configuration();
        config.setScale(new Integer(1));
        assertEquals("Wrong scale: ", new Integer(1), config.getScale());
    }

    /**
     * Test method for {@link com.peu.pt.shared.persistance.domain.config.Configuration#toString()}.
     */
    @Test
    public final void testToString() {
        Configuration config = new Configuration();
        config.setScale(new Integer(1));
        assertEquals("Wrong scale: ", String.format("%1$d", config.getScale()), config.toString());
    }

    /**
     * Test method for {@link com.peu.pt.shared.persistance.domain.AbstractDomainObject#isUniqKeyCompleted()}.
     */
    @Test
    public final void testIsUniqKeyCompleted() {
        Configuration config = new Configuration();
        assertFalse(config.isLogicalKeyCompleted());
        config.setScale(new Integer(1));
        assertTrue(config.isLogicalKeyCompleted());
    }

    /**
     * Test method for {@link com.peu.pt.shared.persistance.domain.AbstractDomainObject#clone()}.
     */
    @Test
    public final void testClone() throws CloneNotSupportedException {
        Configuration config = new Configuration();
        config.setScale(new Integer(1));
        Configuration config1 = (Configuration) config.clone();
        assertEquals("Not same: ", config, config1);
    }
}
