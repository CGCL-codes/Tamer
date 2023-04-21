package net.sourceforge.javautil.common.jaxb;

import javax.xml.bind.JAXBException;

/**
 * 
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface IJavaXMLContent {

    void startContent(JavaXMLUnmarshallerContext context);

    void handleContent(JavaXMLUnmarshallerContext context, boolean cdata, char[] buffer, int offset, int length) throws JAXBException;

    void endContent(JavaXMLUnmarshallerContext context);
}
