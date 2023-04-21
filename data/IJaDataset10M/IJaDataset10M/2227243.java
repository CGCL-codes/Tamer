package com.luzan.common.xml;

import com.luzan.common.jaxb.NamespacePrefixMapper;
import org.apache.log4j.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.Map;
import java.io.Writer;
import java.io.Reader;

/**
 * XML Bean Context.
 * <p/>
 * This class is thread safe.
 *
 * @author Alexander Kozlov
 */
public class XMLBeanContext<T> {

    private static final Logger logger = Logger.getLogger(XMLBeanContext.class);

    private final JAXBContext jaxbContext;

    private final com.sun.xml.bind.marshaller.NamespacePrefixMapper namespacePrefixMapper;

    protected XMLBeanContext(final JAXBContext jaxbContext, final com.sun.xml.bind.marshaller.NamespacePrefixMapper namespacePrefixMapper) {
        this.jaxbContext = jaxbContext;
        this.namespacePrefixMapper = namespacePrefixMapper;
    }

    public JAXBContext getJaxbContext() {
        return jaxbContext;
    }

    public com.sun.xml.bind.marshaller.NamespacePrefixMapper getNamespacePrefixMapper() {
        return namespacePrefixMapper;
    }

    public XMLBeanWriter<T> createBeanWriter(final Writer writer) throws XMLBeanException {
        return new XMLBeanWriter<T>(writer, this);
    }

    public XMLBeanReader<T> createBeanReader(final Reader reader) throws XMLBeanException {
        return new XMLBeanReader<T>(reader, this);
    }

    public static XMLBeanContext newInstance(final String classes_or_packages) throws XMLBeanException {
        return newInstance(classes_or_packages, null);
    }

    public static XMLBeanContext newInstance(final String classes_or_packages, final Map<String, String> namespacePrefixes) throws XMLBeanException {
        try {
            return new XMLBeanContext(JAXBContext.newInstance(classes_or_packages), createNamespacePrefixMapper(namespacePrefixes));
        } catch (JAXBException e) {
            logger.error("error", e);
            throw new XMLBeanException(e);
        }
    }

    public static <T> XMLBeanContext<T> newInstance(final Class<T> c) throws XMLBeanException {
        return newInstance(c, null);
    }

    public static <T> XMLBeanContext<T> newInstance(final Class<T> c, final Map<String, String> namespacePrefixes) throws XMLBeanException {
        try {
            return new XMLBeanContext<T>(JAXBContext.newInstance(c), createNamespacePrefixMapper(namespacePrefixes));
        } catch (JAXBException e) {
            logger.error("error", e);
            throw new XMLBeanException(e);
        }
    }

    private static NamespacePrefixMapper createNamespacePrefixMapper(Map<String, String> namespacePrefixes) {
        return namespacePrefixes != null ? new NamespacePrefixMapper(namespacePrefixes) : null;
    }
}
