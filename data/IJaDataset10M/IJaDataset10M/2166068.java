package com.muchsoft.util;

import org.junit.Test;
import com.muchsoft.util.naming.GlassFishNamingStrategy;

public class ServiceLocatorTest {

    @Test
    public void settingNamingStrategyOnceShouldWork() {
        ServiceLocator.setNamingStrategy(new GlassFishNamingStrategy());
    }

    @Test(expected = IllegalStateException.class)
    public void settingNamingStrategyTwiceShouldFail() {
        ServiceLocator.setNamingStrategy(new GlassFishNamingStrategy());
        ServiceLocator.setNamingStrategy(new GlassFishNamingStrategy());
    }

    @Test(expected = IllegalArgumentException.class)
    public void settingNamingStrategyToNullShouldFail() {
        ServiceLocator.setNamingStrategy(null);
    }
}
