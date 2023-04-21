package net.stickycode.configured;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import net.stickycode.configured.placeholder.PlaceholderResolver;
import net.stickycode.configured.placeholder.ResolvedValue;
import net.stickycode.configured.source.StickyApplicationConfigurationSource;
import net.stickycode.configured.source.SystemPropertiesConfigurationSource;
import net.stickycode.stereotype.StickyComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyComponent
public class ConfigurationManifest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private SystemPropertiesConfigurationSource systemProperties;

    @Inject
    private StickyApplicationConfigurationSource applicationConfiguration;

    @Inject
    private Set<ConfigurationSource> sources;

    @Inject
    private ConfigurationKeyBuilder keyBuilder;

    private String environment = System.getProperty("env");

    private Map<String, ResolvedValue> resolved;

    @PostConstruct
    public void startup() {
        if (environment != null) log.info("Using environment {} when resolving configuration", environment); else log.debug("No environment is specified for resolving configuration");
    }

    public void resolve(ConfigurationRepository configurations) {
        PlaceholderResolver resolver = new PlaceholderResolver(this);
        Map<String, ResolvedValue> resolutions = new HashMap<String, ResolvedValue>();
        for (Configuration configuration : configurations) for (ConfigurationAttribute a : configuration) {
            String key = keyBuilder.build(configuration, a);
            String seed = lookupValue(key);
            log.debug("resolving key '{}' with seed '{}'", key, seed);
            resolutions.put(key, resolver.resolve(seed, new ResolvedValue(configuration, a, key, seed)));
        }
        log.debug("resolutions {}", resolutions);
        resolved = resolutions;
    }

    /**
   * @return the value to use or null if one is not defined in any configuration source
   */
    public String lookupValue(String key) {
        if (environment != null) {
            String value = findValueInSources(environment + "." + key);
            if (value != null) return value;
        }
        return findValueInSources(key);
    }

    String findValueInSources(String key) {
        if (applicationConfiguration.hasValue(key)) return applicationConfiguration.getValue(key);
        if (systemProperties.hasValue(key)) return systemProperties.getValue(key);
        for (ConfigurationSource s : sources) {
            if (s.hasValue(key)) return s.getValue(key);
        }
        log.debug("value not found for key '{}'", key);
        return null;
    }

    public ResolvedValue find(String key) {
        return resolved.get(key);
    }

    @Override
    public String toString() {
        return sources.toString();
    }
}
