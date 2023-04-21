package org.spantus.core.marker;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Reperesent collection of different layers of segmentations as phones, words etc.
 * 
 * @author mondhs
 *
 */
public class MarkerSetHolder implements Serializable, Iterable<Map.Entry<String, MarkerSet>> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1670053945249372837L;

    private Map<String, MarkerSet> markerSets;

    /**
	 * Recommended marker set types
	 * 
	 * @author mondhs
	 *
	 */
    public enum MarkerSetHolderEnum {

        phone, syllable, word, sentence
    }

    /**
	 * key should be the same as {@link MarkerSet#getMarkerSetType()}. Recommended use {@link MarkerSetHolderEnum} for key values
	 * 
	 * @return
	 */
    public Map<String, MarkerSet> getMarkerSets() {
        if (markerSets == null) {
            markerSets = new LinkedHashMap<String, MarkerSet>();
        }
        return markerSets;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0} [layers:{1}]", getClass().getName(), getMarkerSets().size());
    }

    @Override
    public Iterator<Entry<String, MarkerSet>> iterator() {
        return getMarkerSets().entrySet().iterator();
    }
}
