package javax.xml.transform.sax;

import java.io.InputStream;
import java.io.Reader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * Specifies a SAX XML source. This is a tuple of input source and SAX
 * parser.
 *
 * @author (a href='mailto:dog@gnu.org'>Chris Burdess</a)
 */
public class SAXSource implements Source {

    /**
   * Factory feature indicating that SAX sources are supported.
   */
    public static final String FEATURE = "http://javax.xml.transform.sax.SAXSource/feature";

    private XMLReader xmlReader;

    private InputSource inputSource;

    /**
   * Default constructor.
   */
    public SAXSource() {
    }

    /**
   * Constructor with a SAX parser and input source.
   */
    public SAXSource(XMLReader reader, InputSource inputSource) {
        xmlReader = reader;
        this.inputSource = inputSource;
    }

    /**
   * Constructor with an input source.
   * The SAX parser will be instantiated by the transformer.
   */
    public SAXSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    /**
   * Sets the SAX parser to be used by this source.
   * If null, the transformer will instantiate its own parser.
   */
    public void setXMLReader(XMLReader reader) {
        xmlReader = reader;
    }

    /**
   * Returns the SAX parser to be used by this source.
   * If null, the transformer will instantiate its own parser.
   */
    public XMLReader getXMLReader() {
        return xmlReader;
    }

    /**
   * Sets the input source to parse.
   */
    public void setInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    /**
   * Returns the input source to parse.
   */
    public InputSource getInputSource() {
        return inputSource;
    }

    /**
   * Sets the system ID for this source.
   */
    public void setSystemId(String systemId) {
        if (inputSource != null) {
            inputSource.setSystemId(systemId);
        }
    }

    /**
   * Returns the system ID for this source.
   */
    public String getSystemId() {
        if (inputSource != null) {
            return inputSource.getSystemId();
        }
        return null;
    }

    /**
   * Converts a source into a SAX input source.
   * This method can use a StreamSource or the system ID.
   * @return an input source or null
   */
    public static InputSource sourceToInputSource(Source source) {
        InputSource in = null;
        if (source instanceof SAXSource) {
            in = ((SAXSource) source).getInputSource();
        } else if (source instanceof StreamSource) {
            StreamSource streamSource = (StreamSource) source;
            InputStream inputStream = streamSource.getInputStream();
            if (inputStream != null) {
                in = new InputSource(inputStream);
            } else {
                Reader reader = streamSource.getReader();
                if (reader != null) {
                    in = new InputSource(reader);
                }
            }
            String publicId = streamSource.getPublicId();
            if (publicId != null && in != null) {
                in.setPublicId(publicId);
            }
        }
        String systemId = source.getSystemId();
        if (systemId != null) {
            if (in == null) {
                in = new InputSource(systemId);
            } else {
                in.setSystemId(systemId);
            }
        }
        return in;
    }
}
