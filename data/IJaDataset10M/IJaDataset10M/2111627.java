package org.itemscript.core.url;

/**
 * A factory to create a new {@link SchemeParser}.
 * 
 * Classes implementing this interface can be passed to {@link Url}.
 * 
 * @author Jacob Davies<br/><a href="mailto:jacob@itemscript.org">jacob@itemscript.org</a>
 *
 */
public interface SchemeParserFactory {

    /**
     * Create a new SchemeParser to parse a URL.
     * 
     * @return A new SchemeParser.
     */
    public SchemeParser create();
}
