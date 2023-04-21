package com.volantis.mcs.protocols.assets.implementation;

import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;

/**
 * This class provides a means of resolving an asset.  It is a callback facade
 * to any implementation that is capable of doing the asset resolution.
 *
 * @mock.generate
 */
public interface AssetResolver {

    /**
     * Retrieve the URL for an asset as a String.  This method should resolve
     * the URL based on the given asset, the current request context, and any
     * asset groups for the asset.
     *
     * @param asset The asset which should have its URL computed
     * @return      A string that contains the URL for the provided asset.
     *
     * @deprecated Use {@link #computeURLAsString(SelectedVariant)}
     */
    String computeURLAsString(Asset asset);

    /**
     * Get the textual content of a variant.
     * 
     * @param selected The selected variant.
     * @return The textual content, may be null.
     */
    String getContentsFromVariant(SelectedVariant selected);

    /**
     * Retrieve the URL of the best variant of the referenced policy.
     *
     * @param reference The reference of the policy.
     * @param requiredEncodings
     * @return The URL of the best variant.
     */
    String retrieveVariantURLAsString(RuntimePolicyReference reference, EncodingCollection requiredEncodings);

    /**
     * Retrieve the URL of the best variant of the referenced policy.
     *
     * @param selected The selected variant.
     * @return The URL of the best variant.
     */
    String retrieveVariantURLAsString(SelectedVariant selected);

    /**
     * Select the best variant.
     *
     * @param reference The reference to a policy.
     * @param requiredEncodings The required encodings, may be null.
     *
     * @return The selected variant.
     */
    SelectedVariant selectBestVariant(RuntimePolicyReference reference, EncodingCollection requiredEncodings);

    String rewriteURLWithPageURLRewriter(String url, PageURLType urlType);

    String computeURLAsString(SelectedVariant selectedVariant);

    AssetGroup getAssetGroup(Asset asset);

    BaseLocation getBaseLocation(SelectedVariant selected);
}
