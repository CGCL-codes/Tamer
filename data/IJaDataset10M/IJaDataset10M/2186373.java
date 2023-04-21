package com.bccapi.core;

/**
 * Extension of the {@link DeterministicECKeyManager}, which supports
 * exporting private keys in plain text.  Use with caution.
 */
public class DeterministicECKeyExporter extends DeterministicECKeyManager {

    /**
    * When constructed with the same seed as the
    * {@link DeterministicECKeyManager} the same series of private keys will
    * get generated.
    * 
    * @param seed
    *           The seed to use.
    */
    public DeterministicECKeyExporter(byte[] seed) {
        super(seed);
    }

    /**
    * Get a {@link PrivateECKeyExporter} instance for a key index.
    * 
    * @param index
    *           The key index to get an exporter for.
    * @return A {@link PrivateECKeyExporter} instance for the given key index
    */
    public PrivateECKeyExporter getPrivateKeyExporter(int index) {
        return new PrivateECKeyExporter(getPrivateKey(index));
    }
}
