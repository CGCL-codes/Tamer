package de.wadndadn.dilbert4j.impl;

import java.util.HashMap;
import java.util.Map;
import de.wadndadn.dilbert4j.IDilbert;
import de.wadndadn.dilbert4j.IDilbertImageCache;
import de.wadndadn.dilbert4j.errorhandling.NoImageException;

/**
 * TODO Document.
 * 
 * @author SchubertCh
 * 
 * @since 0.1
 */
public final class DilbertImageCache implements IDilbertImageCache {

    /**
     * TODO Document.
     */
    private final Map<IDilbert, byte[]> cache;

    /**
     * Default constructor.
     */
    public DilbertImageCache() {
        cache = new HashMap<IDilbert, byte[]>();
    }

    /**
     * {@inheritDoc}
     * 
     * @see de.wadndadn.dilbert4j.IDilbertImageCache#getImage(IDilbert)
     */
    public final byte[] getImage(final IDilbert dilbert) throws NoImageException {
        return cache.get(dilbert);
    }
}
