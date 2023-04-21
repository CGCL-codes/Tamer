package org.base.apps.xml;

import javax.xml.transform.Source;

/**
 * Base interface for stackable parser.
 * 
 * @author Kevan Simpson
 *
 */
public interface Parser<R> {

    public static final String BASE_NS_URI = "http://base-apps.org/";

    public R parse(Source src) throws Exception;
}
