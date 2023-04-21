package com.google.common.collect;

import com.google.common.testutils.SerializableTester;
import junit.framework.TestCase;
import java.util.Collections;
import java.util.Map;

/**
 * Tests for {@code EnumHashBiMap}.
 *
 * @author Mike Bostock
 */
public class EnumHashBiMapTest extends TestCase {

    private enum Currency {

        DOLLAR, PESO, FRANC
    }

    private enum Country {

        CANADA, CHILE, SWITZERLAND
    }

    public void testCreate() {
        EnumHashBiMap<Currency, String> bimap = EnumHashBiMap.create(Currency.class);
        assertTrue(bimap.isEmpty());
        assertEquals("{}", bimap.toString());
        assertEquals(HashBiMap.create(), bimap);
        bimap.put(Currency.DOLLAR, "dollar");
        assertEquals("dollar", bimap.get(Currency.DOLLAR));
        assertEquals(Currency.DOLLAR, bimap.inverse().get("dollar"));
    }

    public void testCreateFromMap() {
        Map<Currency, String> map = ImmutableMap.of(Currency.DOLLAR, "dollar", Currency.PESO, "peso", Currency.FRANC, "franc");
        EnumHashBiMap<Currency, String> bimap = EnumHashBiMap.create(map);
        assertEquals("dollar", bimap.get(Currency.DOLLAR));
        assertEquals(Currency.DOLLAR, bimap.inverse().get("dollar"));
        try {
            EnumHashBiMap.create(Collections.<Currency, String>emptyMap());
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
        }
        Map<Currency, String> emptyBimap = EnumHashBiMap.create(Currency.class);
        bimap = EnumHashBiMap.create(emptyBimap);
        assertTrue(bimap.isEmpty());
        Map<Currency, Country> emptyBimap2 = EnumBiMap.create(Currency.class, Country.class);
        EnumHashBiMap<Currency, Country> bimap2 = EnumHashBiMap.create(emptyBimap2);
        assertTrue(bimap2.isEmpty());
    }

    public void testEnumHashBiMapConstructor() {
        EnumHashBiMap<Currency, String> bimap1 = EnumHashBiMap.create(Currency.class);
        bimap1.put(Currency.DOLLAR, "dollar");
        EnumHashBiMap<Currency, String> bimap2 = EnumHashBiMap.create(bimap1);
        assertEquals("dollar", bimap2.get(Currency.DOLLAR));
        assertEquals(bimap1, bimap2);
        bimap2.inverse().put("franc", Currency.FRANC);
        assertEquals("franc", bimap2.get(Currency.FRANC));
        assertNull(bimap1.get(Currency.FRANC));
        assertFalse(bimap2.equals(bimap1));
        EnumHashBiMap<Currency, String> emptyBimap = EnumHashBiMap.create(Currency.class);
        EnumHashBiMap<Currency, String> bimap3 = EnumHashBiMap.create(emptyBimap);
        assertEquals(bimap3, emptyBimap);
    }

    public void testEnumBiMapConstructor() {
        EnumBiMap<Currency, Country> bimap1 = EnumBiMap.create(Currency.class, Country.class);
        bimap1.put(Currency.DOLLAR, Country.SWITZERLAND);
        EnumHashBiMap<Currency, Object> bimap2 = EnumHashBiMap.<Currency, Object>create(bimap1);
        assertEquals(Country.SWITZERLAND, bimap2.get(Currency.DOLLAR));
        assertEquals(bimap1, bimap2);
        bimap2.inverse().put("franc", Currency.FRANC);
        assertEquals("franc", bimap2.get(Currency.FRANC));
        assertNull(bimap1.get(Currency.FRANC));
        assertFalse(bimap2.equals(bimap1));
        EnumBiMap<Currency, Country> emptyBimap = EnumBiMap.create(Currency.class, Country.class);
        EnumHashBiMap<Currency, Country> bimap3 = EnumHashBiMap.create(emptyBimap);
        assertEquals(bimap3, emptyBimap);
    }

    public void testKeyType() {
        EnumHashBiMap<Currency, String> bimap = EnumHashBiMap.create(Currency.class);
        assertEquals(Currency.class, bimap.keyType());
    }

    public void testSerialization() {
        Map<Currency, String> map = ImmutableMap.of(Currency.DOLLAR, "dollar", Currency.PESO, "peso", Currency.FRANC, "franc");
        EnumHashBiMap<Currency, String> bimap = EnumHashBiMap.create(map);
        BiMap<Currency, String> copy = SerializableTester.reserializeAndAssert(bimap);
        assertEquals(bimap.inverse(), copy.inverse());
    }

    public void testForcePut() {
        EnumHashBiMap<Currency, String> bimap = EnumHashBiMap.create(Currency.class);
        bimap.put(Currency.DOLLAR, "dollar");
        try {
            bimap.put(Currency.PESO, "dollar");
        } catch (IllegalArgumentException expected) {
        }
        bimap.forcePut(Currency.PESO, "dollar");
        assertEquals("dollar", bimap.get(Currency.PESO));
        assertEquals(Currency.PESO, bimap.inverse().get("dollar"));
        assertEquals(1, bimap.size());
        assertEquals(1, bimap.inverse().size());
    }
}
