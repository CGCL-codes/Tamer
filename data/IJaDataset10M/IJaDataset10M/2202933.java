package net.stickycode.configured;

import net.stickycode.stereotype.StickyComponent;

@StickyComponent
public class SimpleNameDotFieldConfigurationKeyBuilder implements ConfigurationKeyBuilder {

    @Override
    public String buildKey(Configuration configuration, ConfigurationAttribute attribute) {
        return configuration.getName() + "." + attribute.getName();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public String build(ConfigurationKeyElement group, ConfigurationKeyElement name) {
        return group.getName() + "." + name.getName();
    }
}
