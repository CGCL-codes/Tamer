package org.joox;

/**
 * A callback to be executed for a set of matched elements.
 *
 * @author Lukas Eder
 */
public interface Each {

    /**
     * The callback method invoked for every matched element.
     *
     * @param context The context for the current callback call.
     */
    void each(Context context);
}
