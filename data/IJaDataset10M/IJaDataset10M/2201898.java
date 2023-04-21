package org.waveprotocol.wave.model.id;

import org.waveprotocol.wave.model.util.Serializer;

/**
 * Serializer for wavelet ids to be embedded in strings.
 *
 * @author anorth@google.com (Alex North)
 */
public final class WaveletIdSerializer {

    /**
   * Singleton instance
   */
    public static final Serializer<WaveletId> INSTANCE = new Serializer<WaveletId>() {

        @Override
        public WaveletId fromString(String s) {
            return fromString(s, null);
        }

        /**
     * Deserializes a wavelet id string.
     *
     * @param s serialized wavelet id
     * @return wavelet id represented by {@code s}
     */
        private WaveletId deserialize(String s) {
            try {
                return ModernIdSerialiser.INSTANCE.deserialiseWaveletId(s);
            } catch (InvalidIdException e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public WaveletId fromString(String s, WaveletId defaultValue) {
            return (s != null) ? deserialize(s) : defaultValue;
        }

        @Override
        public String toString(WaveletId x) {
            return (x != null) ? ModernIdSerialiser.INSTANCE.serialiseWaveletId(x) : null;
        }
    };

    private WaveletIdSerializer() {
    }
}
