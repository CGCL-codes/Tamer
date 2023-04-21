package com.flagstone.transform.util.image;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ImageRegistry is used to provide a directory for registering ImageProviders
 * that are used to decode different image formats.
 */
public final class ImageRegistry {

    /** The table of image providers used to decode each supported format. */
    private static Map<String, ImageProvider> providers = new LinkedHashMap<String, ImageProvider>();

    static {
        for (final ImageEncoding encoding : ImageEncoding.values()) {
            registerProvider(encoding.getMimeType(), encoding.getProvider());
        }
    }

    /**
     * Register an ImageDecoder to handle images in the specified format. The
     * image formats currently supported are defined in the ImageFormat
     * class.
     *
     * @param mimeType
     *            the string identifying the image format.
     * @param decoder
     *            any class that implements the ImageDecoder interface.
     */
    public static void registerProvider(final String mimeType, final ImageProvider decoder) {
        providers.put(mimeType, decoder);
    }

    /**
     * Get the provider that can be used to decode a given image format.
     * @param mimeType the MIME type identifying the image format.
     * @return an object implementing the ImageDecoder interface that can be
     * used to decode the image data.
     */
    public static ImageDecoder getImageProvider(final String mimeType) {
        if (providers.containsKey(mimeType)) {
            return providers.get(mimeType).newDecoder();
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** Private constructor for the image registry. */
    private ImageRegistry() {
    }
}
