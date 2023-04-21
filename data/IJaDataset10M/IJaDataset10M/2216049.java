package org.apache.myfaces.view.facelets.tag.composite;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFFaceletTag;
import org.apache.myfaces.view.facelets.AbstractFaceletContext;
import org.apache.myfaces.view.facelets.FaceletViewDeclarationLanguage;

/**
 * @author Leonardo Uribe (latest modification by $Author: lu4242 $)
 * @version $Revision: 904329 $ $Date: 2010-01-28 19:54:49 -0500 (Thu, 28 Jan 2010) $
 */
@JSFFaceletTag(name = "composite:implementation")
public class ImplementationHandler extends TagHandler {

    private static final Logger log = Logger.getLogger(ImplementationHandler.class.getName());

    public static final String NAME = "implementation";

    public ImplementationHandler(TagConfig config) {
        super(config);
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        AbstractFaceletContext actx = ((AbstractFaceletContext) ctx);
        if (!actx.isBuildingCompositeComponentMetadata()) {
            actx.pushCompositeComponentToStack(parent.getParent());
            nextHandler.apply(ctx, parent);
            actx.popCompositeComponentToStack();
        } else {
            CompositeComponentBeanInfo beanInfo = (CompositeComponentBeanInfo) parent.getAttributes().get(UIComponent.BEANINFO_KEY);
            if (beanInfo == null) {
                if (log.isLoggable(Level.SEVERE)) {
                    log.severe("Cannot found composite bean descriptor UIComponent.BEANINFO_KEY ");
                }
                return;
            }
            BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
            Map<String, PropertyDescriptor> facetPropertyDescriptorMap = (Map<String, PropertyDescriptor>) beanDescriptor.getValue(UIComponent.FACETS_KEY);
            if (facetPropertyDescriptorMap == null) {
                facetPropertyDescriptorMap = new HashMap<String, PropertyDescriptor>();
                beanDescriptor.setValue(UIComponent.FACETS_KEY, facetPropertyDescriptorMap);
            }
            if (!facetPropertyDescriptorMap.containsKey(UIComponent.COMPOSITE_FACET_NAME)) {
                try {
                    facetPropertyDescriptorMap.put(UIComponent.COMPOSITE_FACET_NAME, new CompositeComponentPropertyDescriptor(UIComponent.COMPOSITE_FACET_NAME));
                } catch (IntrospectionException e) {
                    if (log.isLoggable(Level.SEVERE)) {
                        log.log(Level.SEVERE, "Cannot create PropertyDescriptor for facet ", e);
                    }
                    throw new TagException(tag, e);
                }
            }
        }
    }
}
