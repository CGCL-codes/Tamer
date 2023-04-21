package net.sf.asyncobjects.net.protocols.mime;

/**
 * MIME header
 * 
 * @author const
 */
public class Header {

    /** MIME header name */
    private final String name;

    /** Value */
    private final String value;

    /**
	 * A constructor
	 * 
	 * @param name
	 *            header name
	 * @param value
	 *            header value
	 */
    public Header(final String name, final String value) {
        super();
        if (name == null) {
            throw new NullPointerException("Header name must be specified");
        }
        if (value == null) {
            throw new NullPointerException("Header " + name + " cannot be null");
        }
        this.name = name;
        this.value = value;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        result = PRIME * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Header other = (Header) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    /**
	 * @return name of header
	 */
    public String name() {
        return name;
    }

    /**
	 * @return value of header
	 */
    public String value() {
        return value;
    }
}
