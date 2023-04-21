package org.datanucleus.ide.idea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import com.intellij.util.xmlb.annotations.AbstractCollection;
import org.datanucleus.ide.idea.integration.EnhancerSupport;

/**
 * Holds plugin's persistent state.
 */
public class DNEPersistentState {

    private boolean enhancerEnabled = true;

    private Collection<String> metaDataExtensions = new ArrayList<String>(Arrays.asList("jdo", "orm"));

    /**
     * Indicator if {@link #metaDataExtensions} should be added to compiler resource patterns
     */
    private boolean addToCompilerResourcePatterns = true;

    private boolean includeTestClasses = true;

    private Collection<String> enabledModules = new ArrayList<String>();

    private String api = "JPA";

    private String enhancerSupport = "DATANUCLEUS";

    public boolean isEnhancerEnabled() {
        return this.enhancerEnabled;
    }

    public void setEnhancerEnabled(final boolean enhancerEnabled) {
        this.enhancerEnabled = enhancerEnabled;
    }

    @AbstractCollection(elementTypes = String.class)
    public Collection<String> getMetaDataExtensions() {
        return new LinkedHashSet<String>(this.metaDataExtensions);
    }

    @AbstractCollection(elementTypes = String.class)
    public void setMetaDataExtensions(final Collection<String> metaDataExtensions) {
        this.metaDataExtensions = new LinkedHashSet<String>(metaDataExtensions);
    }

    public boolean isAddToCompilerResourcePatterns() {
        return this.addToCompilerResourcePatterns;
    }

    public void setAddToCompilerResourcePatterns(final boolean addToCompilerResourcePatterns) {
        this.addToCompilerResourcePatterns = addToCompilerResourcePatterns;
    }

    public boolean isIncludeTestClasses() {
        return this.includeTestClasses;
    }

    public void setIncludeTestClasses(final boolean includeTestClasses) {
        this.includeTestClasses = includeTestClasses;
    }

    @AbstractCollection(elementTypes = String.class)
    public Collection<String> getEnabledModules() {
        return new LinkedHashSet<String>(this.enabledModules);
    }

    @AbstractCollection(elementTypes = String.class)
    public void setEnabledModules(final Collection<String> enabledModules) {
        this.enabledModules = new LinkedHashSet<String>(enabledModules);
    }

    public String getApi() {
        return this.api;
    }

    public void setApi(final String api) {
        this.api = api;
    }

    public String getEnhancerSupport() {
        return this.enhancerSupport;
    }

    public void setEnhancerSupport(final String enhancerSupport) {
        this.enhancerSupport = enhancerSupport;
    }

    /**
     * Copy method used to update persistent state with plugin's internal state.
     *
     * @param state plugin's internal state
     * @return persistent copy of plugin's internal state
     */
    @SuppressWarnings({ "FeatureEnvy", "ChainedMethodCall" })
    public DNEPersistentState copyFrom(final DNEState state) {
        this.enhancerEnabled = state.isEnhancerEnabled();
        if (this.metaDataExtensions == null) {
            this.metaDataExtensions = new ArrayList<String>();
        } else {
            this.metaDataExtensions.clear();
        }
        this.metaDataExtensions.addAll(state.getMetaDataExtensions());
        this.includeTestClasses = state.isIncludeTestClasses();
        if (this.enabledModules == null) {
            this.enabledModules = new ArrayList<String>();
        } else {
            this.enabledModules.clear();
        }
        this.enabledModules.addAll(state.getEnabledModules());
        final EnhancerSupport configuredEnhancerSupport = state.getEnhancerSupport();
        final EnhancerSupport usedEnhancerSupport;
        if (configuredEnhancerSupport == null) {
            usedEnhancerSupport = state.getEnhancerSupportRegistry().getDefaultEnhancerSupport();
            this.enhancerSupport = usedEnhancerSupport.getId();
        } else {
            usedEnhancerSupport = configuredEnhancerSupport;
            this.enhancerSupport = configuredEnhancerSupport.getId();
        }
        final PersistenceApi api = state.getApi();
        if (api == null || !usedEnhancerSupport.isSupported(api)) {
            this.api = usedEnhancerSupport.getDefaultPersistenceApi().name();
        } else {
            this.api = api.name();
        }
        return this;
    }
}
