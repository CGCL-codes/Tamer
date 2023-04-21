package tuner3d.util;

import java.io.IOException;
import tuner3d.util.LocationLexer.TokenEnumeration;

/**
 *  LocationParseException objects are thrown if a parse error occurs during
 *  the parsing of a location string.
 *
 *  @author Kim Rutherford
 *  @version $Id: LocationParseException.java,v 1.2 2004/10/29 09:36:24 tjc Exp $
 *
 */
public class LocationParseException extends IOException {

    /**
   *  Create a new LocationParseException with the given String as the
   *  message.
   *  @param message The detail message.
   *  @param enumTk An enumeration containing the next tokens to be read.  This
   *    is used to give the user an indication of where in the location string
   *    the parsed error happens.
   **/
    public LocationParseException(String message, TokenEnumeration enumTk) {
        super("Parse error at this point: " + enumTk.toString() + ": " + message);
    }

    /**
   *  Create a new LocationParseException with the given String as the
   *  message.
   *  @param message The detail message.
   *  @param location_string The String in which the error occured.
   **/
    public LocationParseException(String message, String location_string) {
        super("Parse error in this location: " + location_string + ": " + message);
    }
}
