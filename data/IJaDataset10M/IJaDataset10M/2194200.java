package org.ztemplates.test.actions.urlhandler.secure.nested;

import org.ztemplates.actions.ZMatch;
import org.ztemplates.actions.ZSecure;

/**
 */
@ZMatch(value = "nested2")
@ZSecure({ "klo", "maus" })
public class NestedHandler2 implements INested {
}
