package net.sf.ninjakore.packetdata;

import java.util.Map;

/**
 * Immutable - guidelines (temporary doc)
 * primitives (int, short, byte) are okay
 * Strings are okay
 * byte[] should be cloned
 * constructor collections should be new'd
 * returned Collections should be wrapped in unmodifiable
 */
public interface PacketData {

    Map<String, Object> getCopyOfFields();

    short getId();

    String getName();
}
