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
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFFaceletAttribute;
import org.apache.myfaces.buildtools.maven2.plugin.builder.annotation.JSFFaceletTag;

/**
 * Define the facets used by this composite component.
 * <p>
 * This tag is used inside composite:interface tag. All facets
 * should be saved under the key UIComponent.FACETS_KEY on the
 * bean descriptor map as a Map&lt;String, PropertyDescriptor&gt;
 * </p>
 * 
 * @author Leonardo Uribe (latest modification by $Author: lu4242 $)
 * @version $Revision: 905407 $ $Date: 2010-02-01 15:45:01 -0500 (Mon, 01 Feb 2010) $
 */
@JSFFaceletTag(name = "composite:facet")
public class FacetHandler extends TagHandler implements InterfaceDescriptorCreator {

    private static final Logger log = Logger.getLogger(FacetHandler.class.getName());

    /**
     * 
     */
    @JSFFaceletAttribute(name = "name", className = "javax.el.ValueExpression", deferredValueType = "java.lang.String")
    private final TagAttribute _name;

    /**
     * 
     */
    @JSFFaceletAttribute(name = "displayName", className = "javax.el.ValueExpression", deferredValueType = "java.lang.String")
    private final TagAttribute _displayName;

    /**
     * Indicate if the attribute is required or not
     * <p>
     * Myfaces specific feature: this attribute is checked only if project stage is
     * not ProjectStage.Production when a composite component is created.
     * </p>
     */
    @JSFFaceletAttribute(name = "required", className = "javax.el.ValueExpression", deferredValueType = "boolean")
    private final TagAttribute _required;

    /**
     * 
     */
    @JSFFaceletAttribute(name = "preferred", className = "javax.el.ValueExpression", deferredValueType = "boolean")
    private final TagAttribute _preferred;

    /**
     * 
     */
    @JSFFaceletAttribute(name = "expert", className = "javax.el.ValueExpression", deferredValueType = "boolean")
    private final TagAttribute _expert;

    /**
     * 
     */
    @JSFFaceletAttribute(name = "shortDescription", className = "javax.el.ValueExpression", deferredValueType = "java.lang.String")
    private final TagAttribute _shortDescription;

    /**
     * Check if the PropertyDescriptor instance created by this handler
     * can be cacheable or not. 
     */
    private boolean _cacheable;

    /**
     * Cached instance used by this component. Note here we have a 
     * "racy single-check". If this field is used, it is supposed 
     * the object cached by this handler is immutable, and this is
     * granted if all properties not saved as ValueExpression are
     * "literal". 
     */
    private PropertyDescriptor _propertyDescriptor;

    public FacetHandler(TagConfig config) {
        super(config);
        _name = getRequiredAttribute("name");
        _displayName = getAttribute("displayName");
        _required = getAttribute("required");
        _preferred = getAttribute("preferred");
        _expert = getAttribute("expert");
        _shortDescription = getAttribute("shortDescription");
        if ((_name.isLiteral()) && (_displayName == null || _displayName.isLiteral()) && (_preferred == null || _preferred.isLiteral()) && (_expert == null || _expert.isLiteral()) && (_shortDescription == null || _shortDescription.isLiteral())) {
            _cacheable = true;
            if (_required == null) {
                _propertyDescriptor = _createFacetPropertyDescriptor(_name.getValue());
            }
        } else {
            _cacheable = false;
        }
    }

    @SuppressWarnings("unchecked")
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
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
        String facetName = _name.getValue(ctx);
        if (isCacheable()) {
            if (_propertyDescriptor == null) {
                _propertyDescriptor = _createFacetPropertyDescriptor(facetName, ctx, parent);
            }
            facetPropertyDescriptorMap.put(facetName, _propertyDescriptor);
        } else {
            PropertyDescriptor facetDescriptor = _createFacetPropertyDescriptor(facetName, ctx, parent);
            facetPropertyDescriptorMap.put(facetName, facetDescriptor);
        }
        nextHandler.apply(ctx, parent);
    }

    /**
     * This method could be called only if it is not necessary to set the following properties:
     * targets, default, required, methodSignature and type
     * 
     * @return
     */
    private PropertyDescriptor _createFacetPropertyDescriptor(String facetName) {
        try {
            CompositeComponentPropertyDescriptor facetPropertyDescriptor = new CompositeComponentPropertyDescriptor(facetName);
            if (_displayName != null) {
                facetPropertyDescriptor.setDisplayName(_displayName.getValue());
            }
            if (_preferred != null) {
                facetPropertyDescriptor.setPreferred(Boolean.valueOf(_preferred.getValue()));
            }
            if (_expert != null) {
                facetPropertyDescriptor.setExpert(Boolean.valueOf(_expert.getValue()));
            }
            if (_shortDescription != null) {
                facetPropertyDescriptor.setShortDescription(_shortDescription.getValue());
            }
            return facetPropertyDescriptor;
        } catch (IntrospectionException e) {
            if (log.isLoggable(Level.SEVERE)) {
                log.log(Level.SEVERE, "Cannot create PropertyDescriptor for facet ", e);
            }
            throw new TagException(tag, e);
        }
    }

    private PropertyDescriptor _createFacetPropertyDescriptor(String facetName, FaceletContext ctx, UIComponent parent) throws TagException, IOException {
        try {
            CompositeComponentPropertyDescriptor facetPropertyDescriptor = new CompositeComponentPropertyDescriptor(facetName);
            if (_displayName != null) {
                facetPropertyDescriptor.setDisplayName(_displayName.getValue(ctx));
            }
            if (_required != null) {
                facetPropertyDescriptor.setValue("required", _required.getValueExpression(ctx, Boolean.class));
            }
            if (_preferred != null) {
                facetPropertyDescriptor.setPreferred(_preferred.getBoolean(ctx));
            }
            if (_expert != null) {
                facetPropertyDescriptor.setExpert(_expert.getBoolean(ctx));
            }
            if (_shortDescription != null) {
                facetPropertyDescriptor.setShortDescription(_shortDescription.getValue(ctx));
            }
            return facetPropertyDescriptor;
        } catch (IntrospectionException e) {
            if (log.isLoggable(Level.SEVERE)) {
                log.log(Level.SEVERE, "Cannot create PropertyDescriptor for attribute ", e);
            }
            throw new TagException(tag, e);
        }
    }

    public boolean isCacheable() {
        return _cacheable;
    }

    public void setCacheable(boolean cacheable) {
        _cacheable = cacheable;
    }
}
