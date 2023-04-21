package org.geonetwork.domain.ows100.common;

/**
 * XML encoded identifier of a standard MIME type, possibly a parameterized MIME type. 
 * 
 * @author heikki doeleman
 *
 */
public class MimeType {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MimeType)) {
            return false;
        }
        MimeType other = (MimeType) obj;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }
}
