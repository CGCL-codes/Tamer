package net.wotonomy.web.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.wotonomy.foundation.internal.WotonomyException;
import net.wotonomy.foundation.xml.XMLDecoder;
import org.xml.sax.SAXException;

/**
* An implementation of XMLDecoder that reads objects from 
* XMLRPC format. 
* This implementation is not thread-safe, so a new instances
* should be created to accomodate multiple threads.
*/
public class XMLRPCDecoder implements XMLDecoder {

    XMLRPCDecoderHelper helper = new XMLRPCDecoderHelper();

    /**
    * Decodes an object in XML-RPC format from the specified input stream.
    * @param anInputStream The input stream from which to read.
    * The stream will be read fully.
    * @param aDescription A description to accompany error messages
    * for the stream, typically a file name.
    * @param aURL A URL against which relative references within the
    * XML will be resolved.
    * @return The object that was constructed from the XML content,
    * or null if no object could be constructed.
    */
    public Object decode(InputStream anInputStream, String aDescription, URL aURL) {
        Object result = null;
        try {
            SAXParser sparser = SAXParserFactory.newInstance().newSAXParser();
            sparser.parse(anInputStream, helper, aURL.toExternalForm());
            result = helper.getResult();
        } catch (ParserConfigurationException e) {
            throw new WotonomyException("Problem in parser configuration", e);
        } catch (IOException e) {
            throw new WotonomyException("IOException thrown while parsing", e);
        } catch (SAXException e) {
            throw new WotonomyException("SAXException thrown while parsing", e);
        }
        helper.reset();
        return result;
    }

    /**
    * Decodes an XML-RPC message from the specified input stream.
    * Stand-alone values not wrapped in "methodCall" or "param"
    * tags will be treated as a response.
    * @param anInputStream The input stream from which to read.
    * The stream will be read fully.
    * @param aReceiver an XMLRPCReceiver that will be invoked with
    * the appropriate method: request, response, or fault.
    */
    public void decode(InputStream anInputStream, XMLRPCReceiver aReceiver) {
        try {
            SAXParser sparser = SAXParserFactory.newInstance().newSAXParser();
            sparser.parse(anInputStream, helper);
            if (helper.isRequest()) {
                aReceiver.request(helper.getMethodName(), helper.getParameters());
            } else if (helper.isFault()) {
                aReceiver.fault(helper.getFaultCode(), helper.getFaultString());
            } else {
                aReceiver.response(helper.getResult());
            }
        } catch (ParserConfigurationException e) {
            throw new WotonomyException("Problem in parser configuration", e);
        } catch (IOException e) {
            throw new WotonomyException("IOException thrown while parsing", e);
        } catch (SAXException e) {
            throw new WotonomyException("SAXException thrown while parsing", e);
        }
        helper.reset();
    }
}
