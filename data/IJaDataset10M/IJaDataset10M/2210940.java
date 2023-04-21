package org.lindenb.lib.net;

/**
 * @author lindenb
 *
 */
public abstract class HttpParameter {

    public static class KeyValue extends HttpParameter {

        private String value;

        protected KeyValue(String key, String value) {
            super(key);
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (this == obj) return true;
            if (!(obj instanceof KeyValue)) return false;
            KeyValue cp = (KeyValue) obj;
            if (!cp.getKey().equals(getKey())) return false;
            return getValue().equals(cp.getValue());
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }

        public long getLength() {
            return getKey().length() + 1 + getValue().length();
        }
    }

    private String key;

    protected HttpParameter(String key) {
        this.key = key;
    }

    public int hashCode() {
        return getKey().hashCode();
    }

    public String getKey() {
        return this.key;
    }

    public String toString() {
        return getKey();
    }

    public abstract long getLength();
}
