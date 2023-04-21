package org.waveprotocol.wave.model.version;

import junit.framework.TestCase;

/**
 * Tests for {@link HashedVersionSerializer}.
 *
 * @author anorth@google.com (Alex North)
 */
public class HashedVersionSerializerTest extends TestCase {

    private static final HashedVersion TYPICAL = HashedVersion.of(5678, new byte[] { -127, -45, 0, 45, 110, 127 });

    private static final HashedVersion UNSIGNED = HashedVersion.unsigned(5678);

    public void testSerializeDeserialize() {
        assertEquals("5678:gdMALW5/", serialize(TYPICAL));
        assertEquals("5678:", serialize(UNSIGNED));
        assertEquals(TYPICAL, HashedVersionSerializer.INSTANCE.fromString(null, TYPICAL));
        assertEquals(null, deserialize(serialize(null)));
        assertEquals(TYPICAL, deserialize(serialize(TYPICAL)));
        assertEquals(UNSIGNED, deserialize(serialize(UNSIGNED)));
    }

    private static final String serialize(HashedVersion v) {
        return HashedVersionSerializer.INSTANCE.toString(v);
    }

    private static final HashedVersion deserialize(String s) {
        return HashedVersionSerializer.INSTANCE.fromString(s);
    }
}
