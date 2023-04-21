package com.ail.core.xmlbinding.castor;

import java.io.IOException;
import java.io.StringWriter;
import org.apache.xml.serializer.ToXMLStream;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.XMLException;
import com.ail.core.XMLString;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.XMLMapping;
import com.ail.core.xmlbinding.ToXMLArgImp;

/**
 * This entry point converts an objects into an XMLString representing it using
 * the castor framework. The object passed as an argument is marshalled into an
 * XMLString and returned. This entry points accepts one argument:
 * <ul>
 * <li>ObjectIn - The object to be marshalled.</li>
 * </ul>
 * And generates one return value:
 * <ul>
 * <li>XmlOut - The result of the marshalling process.</li>
 * </ul>
 * These arguments and returns are encapsulated in an instance of ToXMLArg.
 */
public class CastorToXMLService extends Service {

    private ToXMLArgImp args = null;

    /**
     * Insert an xsi:type attribute in the root element of a string of XML.
     * @param xml XML to insert into
     * @param type Class to insert name of.
     * @return Modified XML string
     */
    @SuppressWarnings("unchecked")
    private String insertRootXsiType(String xml, Class type) {
        int indx = xml.indexOf('>', xml.indexOf('>') + 1);
        if (indx == -1) {
            return xml;
        }
        if (xml.charAt(indx - 1) == '/') {
            indx--;
        }
        StringBuffer xmlSb = new StringBuffer(xml);
        xmlSb.insert(indx, " xsi:type=\"java:" + type.getName() + "\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        return xmlSb.toString();
    }

    /**
     * Fetch the version of this entry point.
     * 
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        Version v = new Version();
        v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
        v.setDate("$Date: 2007/04/15 22:18:34 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/xmlbinding/CastorToXMLService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.7 $");
        return v;
    }

    /**
     * This entry point has no Core requirements, so simply return null.
     * 
     * @return null
     */
    public Core getCore() {
        return null;
    }

    /**
     * Setter used to the set the arguments that <code>invoke()</code> will
     * use when it is called.
     * 
     * @param args
     *            for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (ToXMLArgImp) args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * 
     * @return An instance of LoggerArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /**
     * Use the castor marshaller to translate an object into an XML string.
     */
    public void invoke() throws XMLException, PreconditionException {
        StringWriter str = new StringWriter();
        if (args == null) {
            throw new PreconditionException("args==null");
        }
        if (args.getObjectIn() == null) {
            throw new PreconditionException("args.getObjectIn()==null");
        }
        try {
            CastorMappingContext bindingContext;
            ToXMLStream xmlStreamer = new ToXMLStream();
            xmlStreamer.setWriter(str);
            xmlStreamer.setIndent(true);
            xmlStreamer.setIndentAmount(1);
            xmlStreamer.setEncoding("UTF-8");
            org.xml.sax.ContentHandler handler = xmlStreamer.asContentHandler();
            if (args.getXmlMappingInOut() == null) {
                args.setXmlMappingInOut(new XMLMapping());
            }
            if (args.getXmlMappingInOut().getBindingContext() == null) {
                String additionalMapping = args.getXmlMappingInOut().getDefinition();
                bindingContext = new CastorMappingContext(additionalMapping);
                args.getXmlMappingInOut().setBindingContext(bindingContext);
            } else {
                bindingContext = (CastorMappingContext) args.getXmlMappingInOut().getBindingContext();
            }
            Marshaller m = bindingContext.createMarshaller();
            m.setContentHandler(handler);
            m.marshal(args.getObjectIn());
        } catch (MarshalException e) {
            throw new XMLException("XML Marshal exception", e);
        } catch (ValidationException e) {
            throw new XMLException("XML Validation exception", e);
        } catch (IOException e) {
            throw new XMLException("Failed to load mapping from configuration", e);
        }
        String res = insertRootXsiType(str.toString(), args.getObjectIn().getClass());
        args.setXmlOut(new XMLString(res));
    }
}
