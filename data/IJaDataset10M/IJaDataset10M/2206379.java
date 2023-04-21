package org.waveprotocol.wave.client.common.util;

import org.waveprotocol.wave.model.util.SimpleMap;

/**
 * A map that automatically removes values whose keys are no longer comparable
 *
 * @author danilatos@google.com (Daniel Danilatos)
 *
 * @param <K>
 * @param <V>
 */
public interface PruningMap<K extends VolatileComparable<? super K>, V> extends SimpleMap<K, V> {
}
