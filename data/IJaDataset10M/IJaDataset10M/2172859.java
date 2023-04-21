package calclipse.mcomp;

import calclipse.lib.math.rpn.RPNException;

/**
 * Implemented to support MComp URL formats.
 * @author T. Sommerland
 */
public interface MCLocator {

    /**
     * Whether or not this locator supports the given URL.
     * @param referrer the URL to the MComp where this call originated
     * (may be null)
     * @return true if the locator recognizes the format of the specified URL.
     */
    boolean acceptsURL(String url, String referrer);

    /**
     * Attempts to resolve a URL.
     * @param referrer the URL to the MComp where this call originated
     * (may be null)
     * @return null if the MComp was not found.
     */
    MComp getMComp(String url, String referrer) throws RPNException;
}
