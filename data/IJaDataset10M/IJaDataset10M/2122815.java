package com.google.gwt.maps.jsio.client;

/**
 * Allows by-name references to JavaScript values. This is intended for use with
 * opaque values such as those used in enumeration-like types.
 */
public class JSOpaque {

    /**
   * Stores the named reference. This value is never intended to be read or
   * dereferenced by GWT developers.
   */
    private final String reference;

    /**
   * Constructor.
   * 
   * @param reference A named reference to a globally-defined value.
   */
    public JSOpaque(String reference) {
        this.reference = reference;
    }

    /**
   * Object identity between JSOpaque instances is based on their reference.
   * 
   * @return <code>true</code> if the other JSOpaque has the same reference.
   */
    public final boolean equals(JSOpaque o) {
        return reference.equals(o.reference);
    }

    /**
   * Allows comparisons of the JSOpaque to JavaScriptObjects.
   */
    @Override
    public native boolean equals(Object o);

    @Override
    public int hashCode() {
        return reference.hashCode();
    }

    /**
   * Convenience method for comparing object identity.
   * 
   * @return <code>true</code> if the value represented by the JSOpaque shares
   *         identity with the value represented by <code>o</code>.
   */
    public final native boolean identityEquals(JSOpaque o);

    /**
   * Convenience method for comparing object identity.
   * 
   * @return <code>true</code> if the value represented by the JSOpaque shares
   *         identity with <code>o</code>
   */
    public final native boolean identityEquals(Object o);

    @Override
    public native String toString();
}
