package com.googlecode.janrain4j.conf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ConfigHolder.class)
public class ConfigHolderTest {

    @Test
    public void testSetGetConfig() {
        Config config = mock(Config.class);
        ConfigHolder.setConfig(config);
        assertSame(config, ConfigHolder.getConfig());
    }

    @Test
    public void testGetConfigCreatesNewConfig() throws Exception {
        PropertyConfig config = mock(PropertyConfig.class);
        ConfigHolder.setConfig(null);
        whenNew(PropertyConfig.class).withNoArguments().thenReturn(config);
        assertNotNull(ConfigHolder.getConfig());
        verifyNew(PropertyConfig.class);
    }
}
