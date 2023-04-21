package net.sf.doolin.oxml.action;

import net.sf.doolin.oxml.OXMLContext;
import net.sf.doolin.oxml.adapter.OXMLInstanceFactory;
import net.sf.doolin.oxml.annotation.Collection;
import net.sf.doolin.util.Utils;
import net.sf.doolin.util.xml.DOMUtils;
import net.sf.jstring.LocalizableException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <code>create</code> action. This action creates an instance according to its
 * parameters, pushes it on the instance stack, evaluates all nested actions and
 * pops the instance back from the stack.
 * <p>
 * Parameters for this action are:
 * <ul>
 * <li><code>node</code> - (optional) If defined, the class creation is applied
 * for this XPath.
 * <li> <code>class</code> - (required if <code>factory</code> or
 * <code>defId</code> is not provided) Name of the instance type to create
 * <li> <code>factory </code> - (required if <code>class</code> or
 * <code>defId</code> is not provided) Class name for an implementation of the
 * <code>{@link OXMLInstanceFactory}</code> interface, responsible for the
 * creation of the instance
 * <li><code>defId</code> - (required if <code>class</code> or
 * <code>factory</code> is not provided) ID of a <code>createDef</code> action
 * to apply.
 * <li><code>id</code> - (optional) If provided, the created instance will be
 * {@link OXMLContext#instancePut(String, Object) stored} into the context using
 * this id.
 * <li><code>root</code> - (optional) If set to <code>true</code>, the created
 * instance will be set as the {@link OXMLContext#setRoot(Object) root} of the
 * context. Only one action with this attribute set to <code>true</code> is
 * allowed by OXML configuration.
 * <li><code>property</code> - (optional) If provided, this attribute indicates
 * the name of the property of the current instance on the instance stack to
 * fill with the created instance.
 * <li><code>setter</code> - (optional) If provided, this attribute is the name
 * of a method to be called on the current instance on the instance stack with
 * the created instance as a unique parameter
 * <li><code>post</code> - (optional, defaults to <code>false</code>) The
 * <code>property</code> or the <code>setter</code> attribute is used by default
 * <i>before</i> the inner actions are evaluated. Putting this attribute to
 * <code>true</code> will perform this operation <i>after</i> the inner actions
 * have been evaluated.
 * </ul>
 * 
 * @author Damien Coraboeuf
 */
public class CreateOXMLAction extends AbstractSequenceOXMLAction {

    private String node;

    private String className;

    private Class<?> factoryClass;

    private String id;

    private boolean root;

    private String property;

    private String setter;

    private boolean post = false;

    @Override
    public void parse(Element e) throws IOException {
        super.parse(e);
        String defId = DOMUtils.getAttribute(e, "defId", false, null);
        if (defId != null) {
            Element eCreateDef = e.getOwnerDocument().getElementById(defId);
            if (eCreateDef == null) {
                throw new LocalizableException("CreateOXMLAction.CreateDefNotFound", defId);
            }
            parse(eCreateDef);
        }
        this.node = DOMUtils.getAttribute(e, "node", false, null);
        this.className = DOMUtils.getAttribute(e, "class", false, this.className);
        this.id = DOMUtils.getAttribute(e, "id", false, null);
        this.root = DOMUtils.getBooleanAttribute(e, "root", false, false);
        this.property = DOMUtils.getAttribute(e, "property", false, null);
        this.setter = DOMUtils.getAttribute(e, "setter", false, null);
        this.post = DOMUtils.getBooleanAttribute(e, "post", false, false);
        this.factoryClass = DOMUtils.getClassAttribute(e, "factory", this.factoryClass);
    }

    @Override
    public void process(OXMLContext context) {
        if (StringUtils.isNotBlank(this.node)) {
            NodeList nodes = context.getNodeList(this.node);
            int nodeCount = nodes.getLength();
            for (int i = 0; i < nodeCount; i++) {
                Node forNode = nodes.item(i);
                context.nodePush(forNode);
                try {
                    create(context);
                } finally {
                    context.nodePop();
                }
            }
        } else {
            create(context);
        }
    }

    /**
	 * Collects the instances
	 * 
	 * @param instance
	 *            Instance to collect
	 * @param context
	 *            OXML context
	 */
    protected void collect(Object instance, OXMLContext context) {
        Collection collection = Utils.getAnnotation(Collection.class, instance);
        if (collection != null) {
            String collectionName = collection.name();
            if (StringUtils.isBlank(collectionName)) {
                collectionName = StringUtils.substringAfterLast(instance.getClass().getName(), ".");
            }
            String[] collectedProperties = collection.collectedProperties();
            String collectionValue;
            if (collectedProperties == null || collectedProperties.length == 0) {
                collectionValue = ObjectUtils.toString(instance);
            } else {
                List<String> values = new ArrayList<String>();
                for (String collectedProperty : collectedProperties) {
                    try {
                        String collectedValue = BeanUtils.getProperty(instance, collectedProperty);
                        values.add(collectedProperty + "=" + collectedValue);
                    } catch (RuntimeException ex) {
                        throw ex;
                    } catch (Exception ex) {
                        throw new RuntimeException(String.format("Cannot get %2$s property on the %1$s instance.\n%3$s", instance, collectedProperty, ex), ex);
                    }
                }
                collectionValue = StringUtils.join(values, ",");
            }
            context.collect(collectionName, collectionValue);
        }
    }

    /**
	 * Creates the instance
	 * 
	 * @param context
	 *            Execution context
	 */
    protected void create(OXMLContext context) {
        Object instance;
        if (this.className != null) {
            instance = context.newInstance(this.className);
        } else {
            OXMLInstanceFactory<?> factory = (OXMLInstanceFactory<?>) Utils.newInstance(this.factoryClass);
            instance = factory.createInstance(context);
        }
        if (StringUtils.isNotBlank(this.id)) {
            context.instancePut(this.id, instance);
        }
        if (this.root) {
            context.setRoot(instance);
        }
        if (!this.post) {
            setInParent(context, instance);
        }
        context.instancePush(instance);
        try {
            super.process(context);
            collect(instance, context);
        } finally {
            context.instancePop();
            if (this.post) {
                setInParent(context, instance);
            }
        }
    }

    /**
	 * Sets the created instance into its parent
	 * 
	 * @param context
	 *            Execution context
	 * @param instance
	 *            Created instance
	 */
    protected void setInParent(OXMLContext context, Object instance) {
        if (StringUtils.isNotBlank(this.setter)) {
            Object currentInstance = context.instancePeek();
            if (StringUtils.isNotBlank(this.property)) {
                try {
                    Object propertyValue = PropertyUtils.getProperty(currentInstance, this.property);
                    MethodUtils.invokeMethod(propertyValue, this.setter, instance);
                } catch (Exception ex) {
                    throw new RuntimeException("Cannot call method " + this.setter + "on property " + this.property, ex);
                }
            } else {
                try {
                    MethodUtils.invokeMethod(currentInstance, this.setter, instance);
                } catch (Exception ex) {
                    throw new RuntimeException("Cannot call method " + this.setter, ex);
                }
            }
        } else if (StringUtils.isNotBlank(this.property)) {
            try {
                BeanUtils.setProperty(context.instancePeek(), this.property, instance);
            } catch (Exception ex) {
                throw new RuntimeException("Cannot set property " + this.property, ex);
            }
        }
    }
}
