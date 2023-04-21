package org.odlabs.wiquery.ui.accordion;

import org.apache.wicket.resource.dependencies.AbstractResourceDependentResourceReference;
import org.odlabs.wiquery.core.resources.WiQueryJavaScriptResourceReference;
import org.odlabs.wiquery.core.ui.ICoreUIJavaScriptResourceReference;
import org.odlabs.wiquery.ui.core.CoreUIJavaScriptResourceReference;
import org.odlabs.wiquery.ui.widget.WidgetJavaScriptResourceReference;

/**
 * $Id: AccordionJavaScriptResourceReference.java 869 2011-05-04 12:26:32Z
 * hielke.hoeve@gmail.com $
 * <p>
 * References the JavaScript resource to get the DatePicker component.
 * </p>
 * 
 * @author Julien Roche
 * @since 1.0
 */
public class AccordionJavaScriptResourceReference extends WiQueryJavaScriptResourceReference implements ICoreUIJavaScriptResourceReference {

    private static final long serialVersionUID = -4771815414204892357L;

    /**
	 * Singleton instance.
	 */
    private static AccordionJavaScriptResourceReference instance = new AccordionJavaScriptResourceReference();

    /**
	 * Builds a new instance of {@link AccordionJavaScriptResourceReference}.
	 */
    private AccordionJavaScriptResourceReference() {
        super(AccordionJavaScriptResourceReference.class, "jquery.ui.accordion.js");
    }

    /**
	 * Returns the {@link AccordionJavaScriptResourceReference} instance.
	 */
    public static AccordionJavaScriptResourceReference get() {
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
