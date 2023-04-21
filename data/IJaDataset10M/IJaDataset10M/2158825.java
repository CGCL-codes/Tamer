package com.amazon.carbonado.lob;

/**
 * Marker interface for {@link Blob Blobs} and {@link Clob Clobs}.
 *
 * @author Brian S O'Neill
 */
public interface Lob {

    /**
     * Returns an object which identifies the Lob data, which may be null if
     * not supported.
     *
     * @since 1.2
     */
    Object getLocator();

    /**
     * Two Lobs are considered equal if the object instances are the same or if
     * they point to the same content. Lob data is not compared, as that would
     * be expensive or it may result in a fetch exception.
     */
    boolean equals(Object obj);
}
