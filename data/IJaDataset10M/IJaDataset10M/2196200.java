package net.sourceforge.javautil.common.jaxb;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlSeeAlso;
import net.sourceforge.javautil.common.reflection.cache.ClassCache;

/**
 * The context in which {@link JavaXMLBean}'s are initialized.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 * 
 * @see JavaXMLBean#initialize(JavaXMLBeanInitializationContext)
 */
public class JavaXMLBeanInitializationContext {

    protected final IJavaXMLBeanMapper mapper;

    protected final Map<Class<?>, JavaXMLBean> types = new HashMap<Class<?>, JavaXMLBean>();

    public JavaXMLBeanInitializationContext(IJavaXMLBeanMapper mapper) {
        this.mapper = mapper;
    }

    public IJavaXMLBeanMapper getMapper() {
        return mapper;
    }

    public JavaXMLBean resolve(Class<?> type) throws JAXBException {
        JavaXMLBean jtype = types.get(type);
        if (jtype == null) {
            types.put(type, jtype = mapper.createElement(type));
            jtype.initialize(this);
            XmlSeeAlso seeAlso = type.getAnnotation(XmlSeeAlso.class);
            if (seeAlso != null) {
                for (Class<?> xmlClass : seeAlso.value()) {
                    this.resolve(xmlClass);
                }
            }
        }
        return jtype;
    }
}
