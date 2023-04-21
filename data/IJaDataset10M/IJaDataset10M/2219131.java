package org.apache.shindig.social.core.util.atom;

import org.apache.shindig.social.opensocial.model.Activity;

/**
 * this represents atom:entry/atom:generator configured for the Activity
 * representation.
 */
public class AtomGenerator {

    @SuppressWarnings("unused")
    private String uri;

    /**
   * @param activity
   */
    public AtomGenerator(Activity activity) {
        uri = activity.getAppId();
    }
}
