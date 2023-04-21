package com.relaxng4j.datatype.helpers;

/**
 * Dummy implementation of {@link com.relaxng4j.datatype.DatatypeBuilder}.
 * 
 * This implementation can be used for Datatypes which have no parameters.
 * Any attempt to add parameters will be rejected.
 * 
 * <p>
 * Typical usage would be:
 * <PRE><XMP>
 * class MyDatatypeLibrary implements DatatypeLibrary {
 *     ....
 *     DatatypeBuilder createDatatypeBuilder( String typeName ) {
 *         return new ParameterleessDatatypeBuilder(createDatatype(typeName));
 *     }
 *     ....
 * }
 * </XMP></PRE>
 * 
 * @author <a href="mailto:kohsuke.kawaguchi@sun.com">Kohsuke KAWAGUCHI</a>
 */
public final class ParameterlessDatatypeBuilder implements com.relaxng4j.datatype.DatatypeBuilder {

    /** This type object is returned for the derive method. */
    private final com.relaxng4j.datatype.Datatype baseType;

    public ParameterlessDatatypeBuilder(com.relaxng4j.datatype.Datatype baseType) {
        this.baseType = baseType;
    }

    public void addParameter(String name, String strValue, com.relaxng4j.datatype.ValidationContext context) throws com.relaxng4j.datatype.DatatypeException {
        throw new com.relaxng4j.datatype.DatatypeException();
    }

    public com.relaxng4j.datatype.Datatype createDatatype() throws com.relaxng4j.datatype.DatatypeException {
        return baseType;
    }
}
