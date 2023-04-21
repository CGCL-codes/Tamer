package org.openremote.android.console.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used to share information between all calls to local logic.
 * 
 * @author Eric Bariaux (eric@openremote.org)
 *
 */
public class LocalLogicContext {

    public static Map<String, Object> contextMap = new ConcurrentHashMap<String, Object>();
}
