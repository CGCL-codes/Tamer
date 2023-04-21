package org.waveprotocol.wave.model.id;

import junit.framework.TestCase;

/**
 * Tests for an id serialiser.
 *
 * @author zdwang@google.com (David Wang)
 */
public class LegacyIdSerialiserTest extends TestCase {

    private IdSerialiser serialiser;

    @Override
    public void setUp() {
        serialiser = LegacyIdSerialiser.INSTANCE;
    }

    public void testIdSerialiser() throws InvalidIdException {
        checkSerialise("example.com", "id+part", "example.com!id+part");
        try {
            checkSerialise("", "id", "example.com!id");
            fail("Shouldn't be able to serialise empty domain");
        } catch (IllegalArgumentException expected) {
        }
        checkDeserialise("example.com!id", "example.com", "id");
        try {
            checkDeserialise("id", "", "");
            fail("Shouldn't be able to de-serialise empty domain");
        } catch (IllegalArgumentException ex) {
        }
    }

    private void checkSerialise(String domain, String id, String expectedSerialised) {
        String serialisedWaveId = serialiser.serialiseWaveId(WaveId.of(domain, id));
        assertEquals(expectedSerialised, serialisedWaveId);
        String serialisedWaveletId = serialiser.serialiseWaveletId(WaveletId.of(domain, id));
        assertEquals(expectedSerialised, serialisedWaveletId);
    }

    private void checkDeserialise(String toDeserialise, String expectedDomain, String expectedId) throws InvalidIdException {
        assertEquals(WaveId.of(expectedDomain, expectedId), serialiser.deserialiseWaveId(toDeserialise));
        assertEquals(WaveletId.of(expectedDomain, expectedId), serialiser.deserialiseWaveletId(toDeserialise));
    }
}
