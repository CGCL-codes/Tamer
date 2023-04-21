package org.apache.ws.jaxme.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.ws.jaxme.JMUnmarshaller;
import org.apache.ws.jaxme.util.DOMSerializer;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/** JaxMe's {@link javax.xml.bind.Unmarshaller} implementation.
 */
public class JMUnmarshallerImpl extends JMControllerImpl implements JMUnmarshaller {

    private static final SAXParserFactory spf;

    static {
        spf = SAXParserFactory.newInstance();
        spf.setValidating(false);
        spf.setNamespaceAware(true);
    }

    private boolean validating;

    public boolean isValidating() {
        return validating;
    }

    public void setValidating(boolean pValidating) {
        validating = pValidating;
    }

    public Object unmarshal(URL pURL) throws JAXBException {
        InputSource isource;
        try {
            isource = new InputSource(pURL.openStream());
            isource.setSystemId(pURL.toString());
        } catch (IOException e) {
            throw new UnmarshalException("Failed to open URL " + pURL, e);
        }
        return unmarshal(isource);
    }

    public Object unmarshal(File pFile) throws JAXBException {
        InputSource isource;
        try {
            isource = new InputSource(new FileInputStream(pFile));
            isource.setSystemId(pFile.toURL().toString());
        } catch (IOException e) {
            throw new UnmarshalException("Failed to open file " + pFile, e);
        }
        return unmarshal(isource);
    }

    public Object unmarshal(InputStream pStream) throws JAXBException {
        return unmarshal(new InputSource(pStream));
    }

    public Object unmarshal(InputSource pSource) throws JAXBException {
        UnmarshallerHandler uh = getUnmarshallerHandler();
        try {
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setContentHandler(uh);
            xr.parse(pSource);
        } catch (SAXException e) {
            if (e.getException() != null) {
                throw new UnmarshalException(e.getException());
            } else {
                throw new UnmarshalException(e);
            }
        } catch (IOException e) {
            throw new UnmarshalException(e);
        } catch (ParserConfigurationException e) {
            throw new UnmarshalException(e);
        }
        return uh.getResult();
    }

    public Object unmarshal(Node pNode) throws JAXBException {
        UnmarshallerHandler uh = getUnmarshallerHandler();
        DOMSerializer ds = new DOMSerializer();
        try {
            ds.serialize(pNode, uh);
        } catch (SAXException e) {
            if (e.getException() != null) {
                throw new UnmarshalException(e.getException());
            } else {
                throw new UnmarshalException(e);
            }
        }
        return uh.getResult();
    }

    public Object unmarshal(Source pSource) throws JAXBException {
        if (pSource instanceof SAXSource) {
            SAXSource ss = (SAXSource) pSource;
            InputSource is = ss.getInputSource();
            if (is == null) {
                throw new UnmarshalException("The SAXResult doesn't have its InputSource set.");
            }
            XMLReader xr = ss.getXMLReader();
            if (xr == null) {
                return unmarshal(is);
            } else {
                UnmarshallerHandler uh = getUnmarshallerHandler();
                xr.setContentHandler(uh);
                try {
                    xr.parse(is);
                } catch (IOException e) {
                    throw new JAXBException(e);
                } catch (SAXException e) {
                    if (e.getException() != null) {
                        throw new JAXBException(e.getException());
                    } else {
                        throw new JAXBException(e);
                    }
                }
                return uh.getResult();
            }
        } else if (pSource instanceof StreamSource) {
            StreamSource ss = (StreamSource) pSource;
            InputSource iSource = new InputSource();
            iSource.setPublicId(ss.getPublicId());
            iSource.setSystemId(ss.getSystemId());
            Reader r = ss.getReader();
            if (r == null) {
                InputStream is = ss.getInputStream();
                if (is == null) {
                    throw new IllegalArgumentException("The StreamSource doesn't have its Reader or InputStream set.");
                } else {
                    iSource.setByteStream(is);
                }
            } else {
                iSource.setCharacterStream(r);
            }
            return unmarshal(iSource);
        } else if (pSource instanceof DOMSource) {
            Node node = ((DOMSource) pSource).getNode();
            if (node == null) {
                throw new IllegalArgumentException("The DOMSource doesn't have its Node set.");
            }
            return unmarshal(node);
        } else {
            throw new IllegalArgumentException("Unknown type of Source: " + pSource.getClass().getName() + ", only SAXSource, StreamSource and DOMSource are supported.");
        }
    }

    public UnmarshallerHandler getUnmarshallerHandler() {
        return new JMUnmarshallerHandlerImpl(this);
    }

    /** <p>Returns the schema location. The marshaller will use this to
     * create an attribute <code>xsi:schemaLocation</code>. Equivalent
     * to <code>setProperty(JAXB_SCHEMA_LOCATION, pValue)</code>.
     * Defaults to null, in which case the attribute isn't created.</p>
     * @see Marshaller#JAXB_SCHEMA_LOCATION
     * @see #setProperty(String, Object)
     * @see #setSchemaLocation(String)
     */
    public String getSchemaLocation() {
        return null;
    }

    /** <p>Returns the schema location. The marshaller will use this to
     * create an attribute <code>xsi:noNamespaceSchemaLocation</code>. Equivalent
     * to <code>setProperty(JAXB_SCHEMA_LOCATION, pValue)</code>.
     * Defaults to null, in which case the attribute isn't created.</p>
     * @see Marshaller#JAXB_NO_NAMESPACE_SCHEMA_LOCATION
     * @see #setProperty(String, Object)
     * @see #setNoNamespaceSchemaLocation(String)
     */
    public String getNoNamespaceSchemaLocation() {
        return null;
    }
}
