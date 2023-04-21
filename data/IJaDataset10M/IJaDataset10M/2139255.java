package org.progeeks.meta.xml;

import java.util.*;
import org.progeeks.meta.*;
import org.progeeks.meta.beans.BeanUtils;
import org.progeeks.util.ObjectUtils;
import org.progeeks.util.beans.*;
import org.progeeks.util.xml.XmlPrintWriter;

/**
 *  Default renderer for generating output for a bean.  Normally,
 *  it is better to use a meta-class and meta-object to render a
 *  bean because it offers more flexibility in how properties are
 *  interpreted, etc..  This renderer can be useful in the cases
 *  where the objects are simple and the application otherwise
 *  does not use meta-objects for very much.
 *
 *  <p>Also, this is the only real way to render beans that are
 *  also collections.  It will use the new direct-nesting style
 *  that ObjectXmlReader supports.</p>
 *
 *  <p>This class uses BeanInspector to discover the properties
 *  of a class and by default will only write out the read+write
 *  properties.  BeanInspector uses Java beans utilities to build
 *  its property lists and so does directly support PropertyInfo
 *  and BeanInfo constructs.</p>
 *
 *  @version   $Revision: 3815 $
 *  @author    Paul Speed
 */
public class BeanXmlPropertyRenderer extends AbstractXmlPropertyRenderer {

    private List<String> fieldOverrides;

    private Map<Class, List<String>> fieldCache = new HashMap<Class, List<String>>();

    private boolean useReferencing = false;

    /**
     *  Creates a renderer that will renderer all the fields of
     *  a meta-object.
     */
    public BeanXmlPropertyRenderer() {
    }

    /**
     *  Creates a renderer that will renderer the specified fields of
     *  a bean.
     */
    public BeanXmlPropertyRenderer(List<String> fields) {
        this.fieldOverrides = fields;
    }

    public BeanXmlPropertyRenderer(String... fields) {
        this.fieldOverrides = Arrays.asList(fields);
    }

    public void setFields(List<String> fields) {
        this.fieldOverrides = fields;
    }

    protected List<String> getFields() {
        return (fieldOverrides);
    }

    /**
     *  Set to true if the created beans should be registered
     *  for object referencing.  False if every bean should be
     *  written out entirely.  Defaults to false which means
     *  the same .equals() equivalent bean will get written
     *  every time this renderer encounters it.
     */
    public void setUseReferencing(boolean b) {
        this.useReferencing = b;
    }

    protected List<String> getFields(BeanInspector bean) {
        if (fieldOverrides != null) return (fieldOverrides);
        List<String> result = fieldCache.get(bean.getBeanClass());
        if (result != null) return (result);
        result = new ArrayList<String>();
        for (String s : bean.getPropertyNames()) {
            if (bean.isReadableProperty(s) && bean.isWriteableProperty(s)) result.add(s);
        }
        fieldCache.put(bean.getBeanClass(), result);
        return result;
    }

    /**
     *  Calculates the current set of RendererProxies for the specified bean.
     *  This can't be cached because it may be different for each bean value.
     *  We calculate it separately to make it easier to use in the attribute
     *  and element passes without relooking it up.
     */
    protected Map<String, RendererProxy> getProxies(Object bean, BeanInspector beanInfo, List<String> fields, XmlRenderContext context) {
        Map<String, RendererProxy> result = new HashMap<String, RendererProxy>();
        for (String s : fields) {
            Object value = beanInfo.getProperty(bean, s);
            if (!renderProperty(bean, s, value)) continue;
            PropertyType pType = null;
            if (value instanceof MetaObject) pType = new MetaClassPropertyType(((MetaObject) value).getMetaClass()); else pType = new ClassPropertyType(value.getClass());
            RendererProxy rp = context.getRendererProxy(pType, value);
            result.put(s, rp);
        }
        return result;
    }

    /**
     *  Called to determine if the specified property should be rendered
     *  or not.  Subclasses can override to provide their own behavior.
     *  The default implementation checks for null and the Collections.EMPTY_LIST,
     *  EMPTY_SET, and EMPTY_MAP values.  If any of that is true then it returns
     *  false.
     */
    protected boolean renderProperty(Object bean, String field, Object value) {
        if (value == null) return false;
        if (value == Collections.EMPTY_LIST || value == Collections.EMPTY_SET || value == Collections.EMPTY_MAP) {
            return false;
        }
        return (true);
    }

    /**
     *  Called to determine if a specific property value should be
     *  rendered as an attribute or a nested element.
     *  Default implementation returns rp.canRenderAsAttribute().
     */
    protected boolean isRenderedAsAttribute(Object bean, String field, Object value, RendererProxy rp) {
        return (rp.canRenderAsAttribute(value));
    }

    protected void renderAttributes(Object bean, BeanInspector beanInfo, List<String> fields, Map<String, RendererProxy> proxies, XmlPrintWriter out, XmlRenderContext context) {
        for (String s : fields) {
            RendererProxy rp = proxies.get(s);
            if (rp == null) continue;
            Object value = beanInfo.getProperty(bean, s);
            if (!isRenderedAsAttribute(bean, s, value, rp)) continue;
            out.printAttribute(s, rp.getAsAttribute(value, context));
        }
    }

    protected void renderElements(Object bean, BeanInspector beanInfo, List<String> fields, Map<String, RendererProxy> proxies, XmlPrintWriter out, XmlRenderContext context) {
        for (String s : fields) {
            RendererProxy rp = proxies.get(s);
            if (rp == null) continue;
            Object value = beanInfo.getProperty(bean, s);
            if (isRenderedAsAttribute(bean, s, value, rp)) continue;
            out.pushTag(s);
            rp.render(value, false, context, false);
            out.popTag(s);
        }
    }

    /**
     *  Renders the specified bean's value to the specified writer.
     */
    public void render(Object bean, PropertyType type, boolean forceWrap, XmlRenderContext context) {
        XmlPrintWriter out = context.getWriter();
        if (bean == null) {
            out.printTag("null");
            return;
        }
        Class c = bean.getClass();
        BeanInspector beanInfo = BeanInspector.getInspector(c);
        List<String> fields = getFields(beanInfo);
        Map<String, RendererProxy> proxies = getProxies(bean, beanInfo, fields, context);
        boolean wrap = forceWrap || !ArrayList.class.equals(c) || fields.size() > 0;
        if (wrap) {
            String cname = BeanUtils.getNameForClass(c);
            out.pushTag(context.transformClassName(cname));
            if (useReferencing) {
                String oid = context.registerObject(bean);
                out.printAttribute(org.progeeks.util.xml.ObjectXmlReader.OID_DIRECTIVE, oid);
            }
            renderAttributes(bean, beanInfo, fields, proxies, out, context);
            renderElements(bean, beanInfo, fields, proxies, out, context);
        }
        if (bean instanceof Collection) {
            for (Iterator i = ((Collection) bean).iterator(); i.hasNext(); ) {
                Object item = i.next();
                context.renderObject(item, true, false);
            }
        }
        if (wrap) out.popTag();
    }
}
