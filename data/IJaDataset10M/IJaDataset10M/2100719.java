package com.gargoylesoftware.htmlunit.javascript;

import java.io.Serializable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.WrapFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * Called by Rhino to Wrap Object as {@link Scriptable}.
 *
 * @version $Revision: 6701 $
 * @author Marc Guillemot
 */
public class HtmlUnitWrapFactory extends WrapFactory implements Serializable {

    /**
     * Constructor.
     */
    public HtmlUnitWrapFactory() {
        setJavaPrimitiveWrap(false);
    }

    /**
     * Wraps some objects used by HtmlUnit (like {@link NodeList}), or delegates directly to the parent class.
     * {@inheritDoc}
     * @see WrapFactory#wrapAsJavaObject(Context, Scriptable, Object, Class)
     */
    @Override
    public Scriptable wrapAsJavaObject(final Context context, final Scriptable scope, final Object javaObject, final Class<?> staticType) {
        final Scriptable resp;
        if (NodeList.class.equals(staticType) || NamedNodeMap.class.equals(staticType)) {
            resp = new ScriptableWrapper(scope, javaObject, staticType);
        } else {
            resp = super.wrapAsJavaObject(context, scope, javaObject, staticType);
        }
        return resp;
    }
}
