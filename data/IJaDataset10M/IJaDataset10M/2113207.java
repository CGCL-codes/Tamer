package com.volantis.mcs.protocols.assets;

/**
 * A reference to a link asset.
 * <p>
 * This interface extends the generic asset reference interface to add methods
 * which are link asset reference specific.
 */
public interface LinkAssetReference extends AssetReference {

    /**
     * This method returns the URL string that this Asset resolves to.
     *
     * @return        The URL of this asset as a string, or null if the
     *                resolution fails.
     */
    String getURL();

    /**
     * Returns a reference to this asset's text fallback asset.
     *
     * @return a reference to this asset's text fallback asset.
     */
    TextAssetReference getTextFallback();
}
