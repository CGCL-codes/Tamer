package net.toften.prop4j.loaders;

import net.toften.prop4j.BadPropertyInfoException;
import net.toften.prop4j.Prop;
import net.toften.prop4j.Prop4jProperties;
import net.toften.prop4j.config.ManualConfig;
import net.toften.prop4j.config.Prop4jConfig;
import net.toften.prop4j.test.ValidPropertyInterface;
import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.*;

public class MemoryLoaderTest {

    private Prop4jConfig config;

    @Before
    public void setup() throws ClassNotFoundException {
        System.getProperties().remove(Prop4jProperties.PROP_PROPERTIES);
        Prop.init();
        config = Prop.getProp4jConfig();
        assertNotNull(config);
        assertEquals(Class.forName(Prop.getProp4jProperties().getDefaultConfigClassName()), config.getClass());
    }

    @Test
    public void testDefaultLoader() {
        assertEquals(1, ((ManualConfig) config).getLoaderCount());
        assertEquals(MemoryPropertyLoader.class, config.getDefaultLoader().getClass());
    }

    private final String NEW_SERVER_NAME = "newhost";

    @Test
    public void testChangeOfProperty() throws BadPropertyInfoException {
        ValidPropertyInterface pi = Prop.getProp(ValidPropertyInterface.class);
        assertEquals(1, ((ManualConfig) config).getLoaderCount());
        assertEquals(config.getDefaultLoader(), pi.getLoader());
        assertEquals(ValidPropertyInterface.DEFAULT_SERVER_NAME, pi.getServerName());
        pi.setProperty(pi.SERVER_NAME_PROPERTY, NEW_SERVER_NAME);
        assertEquals(NEW_SERVER_NAME, pi.getServerName());
    }

    @Test
    public void testTwoInterfaces() throws BadPropertyInfoException {
        ValidPropertyInterface pi1 = Prop.getProp(ValidPropertyInterface.class);
        PIWithSameNameProperty pi2 = Prop.getProp(PIWithSameNameProperty.class);
        pi1.setProperty(ValidPropertyInterface.SERVER_NAME_PROPERTY, NEW_SERVER_NAME);
        assertEquals(pi1.getServerName(), pi2.getServer());
        assertSame(pi1.getServerName(), pi2.getServer());
        assertTrue(pi1.getServerName() == pi2.getServer());
        assertTrue(pi1.getLoader() == pi2.getLoader());
    }

    @Test
    public void testTwoLoadersAndTwoInterfaces() throws BadPropertyInfoException {
        config.addLoader(new MemoryPropertyLoader(), PIWithSameNameProperty.class.getName());
        ValidPropertyInterface pi1 = Prop.getProp(ValidPropertyInterface.class);
        PIWithSameNameProperty pi2 = Prop.getProp(PIWithSameNameProperty.class);
        pi1.setProperty(ValidPropertyInterface.SERVER_NAME_PROPERTY, NEW_SERVER_NAME);
        assertEquals(pi1.getServerName(), NEW_SERVER_NAME);
        assertEquals(pi2.getServer(), "");
        assertNotSame(pi1.getServerName(), pi2.getServer());
        assertFalse(pi1.getLoader() == pi2.getLoader());
    }
}
