package net.fortuna.ical4j.model.property;

import java.net.URI;
import java.net.URISyntaxException;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.Property;

/**
 * Defines a URL iCalendar component property.
 * 
 * @author benf
 */
public class Url extends Property {

    private URI uri;

    /**
	 * Default constructor.
	 */
    public Url() {
        super(URL);
    }

    /**
	 * @param aList
	 *            a list of parameters for this component
	 * @param aValue
	 *            a value string for this component
	 * @throws URISyntaxException
	 *             where the specified value string is not a valid uri
	 */
    public Url(final ParameterList aList, final String aValue) throws URISyntaxException {
        super(URL, aList);
        setValue(aValue);
    }

    /**
	 * @param aUri
	 *            a URI
	 */
    public Url(final URI aUri) {
        super(URL);
        uri = aUri;
    }

    /**
	 * @param aList
	 *            a list of parameters for this component
	 * @param aUri
	 *            a URI
	 */
    public Url(final ParameterList aList, final URI aUri) {
        super(URL, aList);
        uri = aUri;
    }

    /**
	 * @return Returns the uri.
	 */
    public final URI getUri() {
        return uri;
    }

    public final void setValue(final String aValue) throws URISyntaxException {
        uri = new URI(aValue);
    }

    public final String getValue() {
        return getUri().toString();
    }

    /**
	 * @param uri
	 *            The uri to set.
	 */
    public final void setUri(final URI uri) {
        this.uri = uri;
    }
}
