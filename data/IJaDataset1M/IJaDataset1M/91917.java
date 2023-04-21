package org.jsmiparser.util.token;

import org.jsmiparser.util.location.Location;

public class StringToken extends GenericToken<String> {

    public StringToken(Location location, String value) {
        super(location, value);
    }
}
