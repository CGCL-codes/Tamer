package fr.gfi.foundation.core.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;
import fr.gfi.foundation.core.email.EmailSender;

/**
 * BeanDefinitionParser for the "emailSender" element.
 * 
 * @author Tiago Fernandez
 * @since 0.1
 */
public final class EmailSenderBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    /**
	 * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#getBeanClass(org.w3c.dom.Element)
	 */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return EmailSender.class;
    }

    /**
	 * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#doParse(
	 * 	org.w3c.dom.Element, org.springframework.beans.factory.support.BeanDefinitionBuilder)
	 */
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        builder.addConstructorArg(element.getAttribute("smtp"));
        builder.addConstructorArg(element.getAttribute("username"));
        builder.addConstructorArg(element.getAttribute("password"));
    }
}
