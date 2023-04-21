package org.lobobrowser.js;

import org.mozilla.javascript.*;

/**
 * Java classes used in Javascript should implement this
 * interface. While all classes can be mapped to
 * JavaScript, implementing this interface ensures that
 * the Java object proxy is not garbage collected as long
 * as the Java object is not garbage collected.
 */
public interface ScriptableDelegate {

    public void setScriptable(Scriptable scriptable);

    public Scriptable getScriptable();
}
