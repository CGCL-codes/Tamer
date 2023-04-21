package org.omg.CosPropertyService;

/** Indicates that the supplied property_name is not valid, i.e. the length 
 * of the property_name is zero. 
 **/
public final class InvalidPropertyName extends org.omg.CORBA.UserException {

    public InvalidPropertyName() {
        super(InvalidPropertyNameHelper.id());
    }

    public InvalidPropertyName(String _ob_reason) {
        super(InvalidPropertyNameHelper.id() + " " + _ob_reason);
    }
}
