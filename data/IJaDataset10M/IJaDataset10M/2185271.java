package com.legstar.cixs.jbossesb.test.lsfileax;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import com.legstar.coxb.transform.HostTransformException;
import com.legstar.coxb.transform.HostTransformStatus;
import com.legstar.coxb.transform.IXmlToHostTransformer;

/**
 * Holders are aggregate objects which are not defined at COBOL binding time. Therefore
 * they do not have an associated XML to Host transformer.
 * <p/>
 * This class provides the minimal capabilities required to support converting from
 * XML to java value objects. It does not implement the XML to Host capabilities because
 * this is typically done for each inner class held by the holder. 
 */
public class LsfileacRequestHolderXmlToHostTransformer implements IXmlToHostTransformer {

    /** JAXB Context. */
    private JAXBContext mJaxbContext = null;

    /**
     * Create the holder XML transformer.
     * @throws HostTransformException if JAXB context cannot be created
     */
    public LsfileacRequestHolderXmlToHostTransformer() throws HostTransformException {
        try {
            mJaxbContext = JAXBContext.newInstance(LsfileacRequestHolder.class);
        } catch (JAXBException e) {
            throw new HostTransformException(e);
        }
    }

    /**
     * Parse XML and get resulting java value object.
     * @param source the XML Source to unmarshal XML data from (such as SAXSource, DOMSource, and StreamSource)
     * @return a java value object
     * @throws HostTransformException if transformation fails
     */
    public Object getObjectFromXml(final Source source) throws HostTransformException {
        try {
            Unmarshaller um = getJaxbContext().createUnmarshaller();
            return um.unmarshal(source, LsfileacRequestHolder.class).getValue();
        } catch (JAXBException e) {
            throw new HostTransformException(e);
        }
    }

    /**
     * @return the JAXB Context
     */
    public JAXBContext getJaxbContext() {
        return mJaxbContext;
    }

    /** {@inheritDoc} */
    public byte[] transform(final Source source) throws HostTransformException {
        throw new HostTransformException("Not implemented");
    }

    /** {@inheritDoc} */
    public byte[] transform(final Source source, final String hostCharset) throws HostTransformException {
        throw new HostTransformException("Not implemented");
    }

    /** {@inheritDoc} */
    public byte[] transform(final Source source, final HostTransformStatus status) throws HostTransformException {
        throw new HostTransformException("Not implemented");
    }

    /** {@inheritDoc} */
    public byte[] transform(final Source source, final String hostCharset, final HostTransformStatus status) throws HostTransformException {
        throw new HostTransformException("Not implemented");
    }
}
