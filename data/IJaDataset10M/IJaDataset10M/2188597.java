package de.carne.fs.core;

import java.util.ArrayList;
import de.carne.nio.compression.Decoder;

/**
 * This class defines how data is mapped from a <code>FileScannerInput</code> to another <code>FileScannerInput</code>
 * object. This mapping consists one ore more map entries. Each entry consists of the actual position of the source data
 * as well a possible <code>Decoder</code> for decoding the source data.
 */
public final class DecodeMap {

    /**
	 * A single map entry.
	 */
    public final class Entry {

        private final long entryStart;

        private final long entryEnd;

        private final long padding;

        private final Decoder decoder;

        Entry(long start, long end, long padding, Decoder decoder) {
            this.entryStart = start;
            this.entryEnd = end;
            this.padding = padding;
            this.decoder = decoder;
        }

        /**
		 * @return start
		 */
        public long getStart() {
            return this.entryStart;
        }

        /**
		 * @return end
		 */
        public long getEnd() {
            return this.entryEnd;
        }

        /**
		 * @return padding
		 */
        public long getPadding() {
            return this.padding;
        }

        /**
		 * @return decoder
		 */
        public Decoder getDecoder() {
            return this.decoder;
        }
    }

    private final ArrayList<Entry> entries = new ArrayList<Entry>();

    /**
	 * Add a map entry.
	 * 
	 * @param entryStart The start position of the section to map.
	 * @param entryEnd The end position of the section to map.
	 */
    public void addEntry(long entryStart, long entryEnd) {
        addEntry(entryStart, entryEnd, 0, null);
    }

    /**
	 * Add a map entry.
	 * 
	 * @param entryStart The start position of the section to map.
	 * @param entryEnd The end position of the section to map.
	 * @param padding The number pad bytes following the mapped section.
	 */
    public void addEntry(long entryStart, long entryEnd, long padding) {
        addEntry(entryStart, entryEnd, padding, null);
    }

    /**
	 * Add a map entry.
	 * 
	 * @param entryStart The start position of the section to map.
	 * @param entryEnd The end position of the section to map.
	 * @param decoder The optional <code>Decoder</code> to decode the data.
	 */
    public void addEntry(long entryStart, long entryEnd, Decoder decoder) {
        addEntry(entryStart, entryEnd, 0, decoder);
    }

    /**
	 * Add a map entry.
	 * 
	 * @param entryStart The start position of the section to map.
	 * @param entryEnd The end position of the section to map.
	 * @param padding The number pad bytes following the mapped section.
	 * @param decoder The optional <code>Decoder</code> to decode the data.
	 */
    public void addEntry(long entryStart, long entryEnd, long padding, Decoder decoder) {
        assert 0 <= entryStart;
        assert entryStart <= entryEnd;
        assert padding >= 0;
        this.entries.add(new Entry(entryStart, entryEnd, padding, decoder));
    }

    /**
	 * Get the map entries.
	 * 
	 * @return The map entries of this map.
	 */
    public Entry[] getEntries() {
        return this.entries.toArray(new Entry[this.entries.size()]);
    }

    /**
	 * Get the map entry count.
	 * 
	 * @return The map entry count.
	 */
    public int getEntryCount() {
        return this.entries.size();
    }
}
