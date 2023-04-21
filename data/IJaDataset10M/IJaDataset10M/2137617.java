package org.jaffa.security;

/** This class has a ThreadLocal variable to store the variation for a given Thread. It should be invoked by the controller servlet or the WebServices wrapper.
 *
 * @author  GautamJ
 */
public class VariationContext {

    /** This is the default variation. */
    public static final String DEFAULT_VARIATION = "DEF";

    private static ThreadLocal variationContext = new ThreadLocal();

    /** This will set the variation for the current thread. This is typically invoked by the controller servlet or the WebServices wrapper.
     * @param variation The variation for the current thread.
     */
    public static void setVariation(String variation) {
        variationContext.set(variation);
    }

    /** This will return the variation for the current thread. The default variation will be returned, in case no value was set prior to the invocation of this method.
     * @return the variation for the current thread.
     */
    public static String getVariation() {
        String variation = ((String) variationContext.get());
        return variation != null ? variation : DEFAULT_VARIATION;
    }
}
