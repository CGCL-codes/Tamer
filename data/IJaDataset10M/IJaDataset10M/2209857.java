package org.restfaces.taglib;

import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.restfaces.component.UIRestLink;
import org.restfaces.util.Util;
import com.sun.jsftemplating.annotation.UIComponentFactory;
import com.sun.jsftemplating.component.factory.ComponentFactoryBase;
import com.sun.jsftemplating.layout.descriptors.LayoutComponent;

@UIComponentFactory("rest:urlBuffer")
public class URLBufferFactory extends ComponentFactoryBase {

    private static final Logger logger = Util.getLogger(Util.TAGLIB);

    /**
	 * <p>
	 * This is the factory method responsible for creating the
	 * <code>UIComponent</code>.
	 * </p>
	 * 
	 * @param context
	 *            The <code>FacesContext</code>
	 * @param descriptor
	 *            The {@link LayoutComponent} descriptor associated with the
	 *            requested <code>UIComponent</code>.
	 * @param parent
	 *            The parent <code>UIComponent</code>
	 * 
	 * @return The newly created <code>UIRestLink</code>.
	 */
    public UIComponent create(FacesContext context, LayoutComponent descriptor, UIComponent parent) {
        UIRestLink comp = (UIRestLink) createComponent(context, UIRestLink.COMPONENT_TYPE, descriptor, parent);
        comp.setRendererType(RENDERER_TYPE);
        setOptions(context, descriptor, comp);
        return comp;
    }

    /**
	 * <p>
	 * The <code>Renderer</code> type that must be registered in the
	 * <code>faces-config.xml</code> file mapping to the Renderer class to use
	 * for this <code>UIComponent</code>.
	 * </p>
	 */
    public static final String RENDERER_TYPE = "org.restfaces.URLBufferRenderer";
}
