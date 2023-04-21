package net.sourceforge.javautil.common.jaxb;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import net.sourceforge.javautil.common.jaxb.xml.IXMLElement;
import net.sourceforge.javautil.common.reflection.cache.ClassCache;
import net.sourceforge.javautil.common.reflection.cache.ClassDescriptor;
import net.sourceforge.javautil.common.reflection.cache.ClassMemberException;
import net.sourceforge.javautil.common.reflection.cache.ClassMethod;
import net.sourceforge.javautil.common.reflection.cache.ClassProperty;
import net.sourceforge.javautil.common.reflection.cache.IClassMemberWritableValue;

/**
 * 
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class JavaXMLBean {

    protected final ClassDescriptor<?> descriptor;

    protected final String name;

    protected final String namespace;

    protected final boolean root;

    protected final Map<String, IJavaXMLAttribute> attributes = new HashMap<String, IJavaXMLAttribute>();

    protected final Map<String, IJavaXMLElement> elements = new HashMap<String, IJavaXMLElement>();

    protected IJavaXMLElement anyElement;

    protected IJavaXMLComment comment;

    protected IJavaXMLContent content;

    protected ClassProperty parentInjection;

    protected ClassProperty xmlElementName;

    public JavaXMLBean(Class<?> descriptor, String name, String namespace, boolean root) {
        this.descriptor = ClassCache.getFor(descriptor);
        this.namespace = namespace;
        this.name = name;
        this.root = root;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public boolean isRoot() {
        return root;
    }

    public Object newInstance(JavaXMLUnmarshallerContext context) {
        Object parent = null;
        if (parentInjection != null) {
            for (int i = context.elementStack.size() - 1; i >= 0; i--) {
                Object possible = context.elementStack.get(i).getInstance(context);
                if (parentInjection.getBaseType().isInstance(possible)) {
                    parent = possible;
                    break;
                }
            }
        }
        return this.newInstance(parent);
    }

    public Object newInstance(Object parent) {
        Object object = descriptor.newInstance();
        if (this.parentInjection != null) {
            try {
                if (parent != null) {
                    this.parentInjection.setValue(object, parent);
                }
            } catch (ClassMemberException e) {
                throw new IllegalArgumentException("Invalid XMLParent method: " + parentInjection.getJavaMember(), e);
            }
        }
        return object;
    }

    /**
	 * @param context The context in which to initialize this XML bean template
	 */
    public void initialize(JavaXMLBeanInitializationContext context) throws JAXBException {
        IJavaXMLBeanMapper mapper = context.getMapper();
        this.attributes.putAll(mapper.getAttributes(descriptor, context));
        this.elements.putAll(mapper.getElements(descriptor, context));
        this.content = mapper.getContentHandler(descriptor, context);
        this.anyElement = mapper.getAnyElementHandler(descriptor, context);
        this.parentInjection = descriptor.getProperty(XmlParent.class);
        this.xmlElementName = descriptor.getProperty(XmlElementName.class);
    }

    public IJavaXMLElement createElement(String namespace, String name) {
        String id = namespace == null ? name : (namespace + ":" + name);
        IJavaXMLElement element = this.elements.get(id);
        return element == null ? this.anyElement : element;
    }
}
