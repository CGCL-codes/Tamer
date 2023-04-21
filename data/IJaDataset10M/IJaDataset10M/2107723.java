package com.jme3.asset;

import java.io.IOException;

/**
 * An interface for asset loaders. An <code>AssetLoader</code> is responsible
 * for loading a certain type of asset associated with file extension(s).
 * The loader will load the data in the provided {@link AssetInfo} object by
 * calling {@link AssetInfo#openStream() }, returning an object representing
 * the parsed data.
 */
public interface AssetLoader {

    /**
     * Loads asset from the given input stream, parsing it into
     * an application-usable object.
     *
     * @return An object representing the resource.
     * @throws java.io.IOException If an I/O error occurs while loading
     */
    public Object load(AssetInfo assetInfo) throws IOException;
}
