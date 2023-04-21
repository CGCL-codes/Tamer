package com.volantis.mcs.runtime;

import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.runtime.configuration.project.AssetConfiguration;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.ObjectHelper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class DynamicProjectKey {

    private final PolicySource policySource;

    private final String generatedResourceBaseDir;

    private final MarinerURL baseURL;

    private final Map variantType2PrefixURL;

    private CacheControlConstraintsMap cacheControlConstraintsMap;

    public DynamicProjectKey(PolicySource policySource, AssetsConfiguration config, String generatedResourceBaseDir, CacheControlConstraintsMap cacheControlConstraintsMap) {
        this.policySource = policySource;
        String baseURLString = config.getBaseUrl();
        baseURL = (baseURLString != null ? new MarinerURL(baseURLString) : null);
        variantType2PrefixURL = new HashMap();
        addVariantPrefixURL(VariantType.AUDIO, config.getAudioAssets());
        addVariantPrefixURL(VariantType.VIDEO, config.getDynamicVisualAssets());
        addVariantPrefixURL(VariantType.IMAGE, config.getImageAssets());
        addVariantPrefixURL(VariantType.SCRIPT, config.getScriptAssets());
        addVariantPrefixURL(VariantType.TEXT, config.getTextAssets());
        this.generatedResourceBaseDir = generatedResourceBaseDir;
        this.cacheControlConstraintsMap = cacheControlConstraintsMap;
    }

    public PolicySource getPolicySource() {
        return policySource;
    }

    public String getGeneratedResourceBaseDir() {
        return generatedResourceBaseDir;
    }

    public MarinerURL getBaseURL() {
        return baseURL;
    }

    /**
     * A utility method for extracting prefix URL strings from asset
     * configuration information, creating new MarinerURL objects
     * based on the extracted prefix and storing it in the map.
     *
     * @param configuration The configuration from which the prefixURL should
     *                    be extracted.
     */
    private void addVariantPrefixURL(VariantType variantType, AssetConfiguration configuration) {
        MarinerURL prefixURL;
        if (configuration == null) {
            prefixURL = null;
        } else {
            prefixURL = new MarinerURL(configuration.getPrefixUrl());
            prefixURL.makeReadOnly();
        }
        variantType2PrefixURL.put(variantType, prefixURL);
    }

    public RuntimeProject getProject() {
        RuntimeProjectBuilder builder = new RuntimeProjectBuilder();
        builder.setPolicySource(policySource);
        builder.setGeneratedResourceBaseDir(generatedResourceBaseDir);
        builder.setAssetsBaseURL(baseURL);
        builder.setCacheControlDefaultsMap(cacheControlConstraintsMap);
        for (Iterator i = variantType2PrefixURL.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            VariantType variantType = (VariantType) entry.getKey();
            MarinerURL prefixURL = (MarinerURL) entry.getValue();
            builder.addVariantPrefixURL(variantType, prefixURL);
        }
        return builder.getProject();
    }

    public boolean equals(Object object) {
        if (object == null || !(object instanceof DynamicProjectKey)) {
            return false;
        }
        DynamicProjectKey key = (DynamicProjectKey) object;
        return ObjectHelper.equals(policySource, key.policySource) && ObjectHelper.equals(generatedResourceBaseDir, key.generatedResourceBaseDir) && ObjectHelper.equals(baseURL, key.baseURL) && ObjectHelper.equals(variantType2PrefixURL, key.variantType2PrefixURL) && ObjectHelper.equals(cacheControlConstraintsMap, key.cacheControlConstraintsMap);
    }

    public int hashCode() {
        int result = 0;
        result = 37 * result + ObjectHelper.hashCode(policySource);
        result = 37 * result + ObjectHelper.hashCode(generatedResourceBaseDir);
        result = 37 * result + ObjectHelper.hashCode(baseURL);
        result = 37 * result + ObjectHelper.hashCode(variantType2PrefixURL);
        result = 37 * result + ObjectHelper.hashCode(cacheControlConstraintsMap);
        return result;
    }
}
