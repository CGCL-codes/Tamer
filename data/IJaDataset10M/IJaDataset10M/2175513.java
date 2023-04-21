package net.sourceforge.javautil.common.jaxb;

import javax.xml.bind.JAXBException;
import net.sourceforge.javautil.common.ReflectionUtil;
import net.sourceforge.javautil.common.reflection.cache.IClassMemberWritableValue;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class JavaXMLAttribute implements IJavaXMLAttribute {

    protected final IClassMemberWritableValue member;

    public JavaXMLAttribute(IClassMemberWritableValue member) {
        this.member = member;
    }

    @Override
    public void setValue(JavaXMLUnmarshallerContext context, String value) throws JAXBException {
        member.setValue(context.getCurrentInstance(), ReflectionUtil.coerce(member.getBaseType(), value));
    }

    @Override
    public String getValue(JavaXMLMarshallerContext context) throws JAXBException {
        return ReflectionUtil.coerce(String.class, member.getValue(context.getInstance()));
    }
}
