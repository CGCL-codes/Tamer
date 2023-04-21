package org.objetdirect.wickext.ui.core;

import org.apache.wicket.markup.html.resources.JavascriptResourceReference;

/**
 * $Id: CoreUIJavaScriptResourceReference.java 61 2008-11-16 20:10:08Z lionel.armanet $
 * <p>
 * 	References the core jQuery UI library.
 * </p>
 * @author Lionel Armanet
 * @since 0.5
 */
public class CoreUIJavaScriptResourceReference extends JavascriptResourceReference {

    private static final long serialVersionUID = 4585057795574929263L;

    private static CoreUIJavaScriptResourceReference instance;

    public static CoreUIJavaScriptResourceReference get() {
        if (instance == null) {
            instance = new CoreUIJavaScriptResourceReference();
        }
        return instance;
    }

    /**
	 * Builds a new instance of {@link CoreUIJavaScriptResourceReference}.
	 */
    private CoreUIJavaScriptResourceReference() {
        super(CoreUIJavaScriptResourceReference.class, "ui.core.js");
    }
}
