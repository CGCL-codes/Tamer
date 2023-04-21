package net.sf.eos.trie;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Map.Entry;

public interface TrieSource {

    public static class TrieEntryEvent extends EventObject {

        public TrieEntryEvent(final TrieEntry source) {
            super(source);
        }
    }

    public static interface TrieEntryListener extends EventListener {

        void onEntry(final TrieEntryEvent event);
    }

    /**
     * Represents an entry in the Trie.
     * @author Sascha Kohlmann
     */
    public static class TrieEntry implements Entry<String, String> {

        private final String key;

        private String value;

        /** Creates a new instance for the given key.
         * @param key the key of the entry */
        public TrieEntry(@SuppressWarnings("hiding") final String key) {
            this(key, null);
        }

        /** Creates a new instance for the given key.
         * @param key the key of the entry 
         * @param value the value of the entry */
        @SuppressWarnings("nls")
        public TrieEntry(@SuppressWarnings("hiding") final String key, @SuppressWarnings("hiding") final String value) {
            if (key == null) {
                throw new IllegalArgumentException("key is null");
            }
            this.key = key;
            this.value = value;
        }

        /**
         * @see Entry#getKey()
         */
        public String getKey() {
            return this.key;
        }

        /**
         * @see Entry#getValue()
         */
        public String getValue() {
            return this.value;
        }

        /**
         * @see Entry#setValue(Object)
         */
        public String setValue(@SuppressWarnings("hiding") final String value) {
            final String old = this.value;
            this.value = value;
            return old;
        }
    }

    public void addTrieEntryListener(final TrieEntryListener listener);

    public void removeTrieEntryListener(final TrieEntryListener listener);
}
