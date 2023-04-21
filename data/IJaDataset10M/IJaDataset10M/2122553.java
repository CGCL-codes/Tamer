package org.odlabs.wiquery.ui.progressbar;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;
import org.odlabs.wiquery.core.ui.ICoreUIJavaScriptResourceReference;
import org.odlabs.wiquery.ui.core.CoreUIJavaScriptResourceReference;
import org.odlabs.wiquery.ui.widget.WidgetJavaScriptResourceReference;

/**
 * $Id: ProgressBarJavaScriptResourceReference.java 1714 2011-09-22 20:38:30Z hielke.hoeve
 * $
 * <p>
 * References the JavaScript resource to get the ProgressBar component.
 * </p>
 * 
 * @author Lionel Armanet
 * @since 0.5
 */
public class ProgressBarJavaScriptResourceReference extends WiQueryJavaScriptResourceReference implements ICoreUIJavaScriptResourceReference {

    /** Constant of serialization */
    private static final long serialVersionUID = 3423205998397680042L;

    /**
	 * Singleton instance.
	 */
    private static ProgressBarJavaScriptResourceReference instance = new ProgressBarJavaScriptResourceReference();

    /**
	 * Builds a new instance of {@link ProgressBarJavaScriptResourceReference}.
	 */
    private ProgressBarJavaScriptResourceReference() {
        super(ProgressBarJavaScriptResourceReference.class, "jquery.ui.progressbar.js");
    }

    /**
	 * Returns the {@link ProgressBarJavaScriptResourceReference} instance.
	 */
    public static ProgressBarJavaScriptResourceReference get() {
        return instance;
    }

    @Override
    public AbstractResourceDependentResourceReference[] getDependentResourceReferences() {
        AbstractResourceDependentResourceReference[] list = new AbstractResourceDependentResourceReference[2];
        list[0] = CoreUIJavaScriptResourceReference.get();
        list[1] = WidgetJavaScriptResourceReference.get();
        return list;
    }
}
