package com.oroad.stxx.xform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.struts.action.Action;
import com.oroad.stxx.util.PropertyMessageResources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.axis.transport.http.HTTPConstants;
import org.xmlform.validation.Violation;

/**
 *  Exposes XML forms as a message-style SOAP web service. This service is tied
 *  to Axis and HTTP to integrate smoothly with Struts. It takes one or more XML
 *  elements, validates them, then returns one or more corresponding result
 *  elements including any errors raised in the validation. If validation
 *  succeeds, the form's save method will be called and its return code put in
 *  the result element.<br />
 *  <br />
 *  This service is not thread-safe so it should only be in the request or
 *  session scope.<br />
 *  <br />
 *  This class requires service options to be set in the WSDD:
 *  <ul>
 *    <li> <code>xmlFormClass</code> - The XMLForm-implementing class (required)
 *    </li>
 *    <li> <code>xmlFormName</code> - The form name (optional)</li>
 *    <li> <code>xmlFormPhase</code> - The validation phase to use (required for
 *    validation)</li>
 *    <li> <code>xmlFormSchema</code> - The validation schema file (required for
 *    validation)</li>
 *    <li> <code>xmlFormSchemaNS</code> - The validation schema namespace
 *    (optional)</li>
 *  </ul>
 *
 *
 *@author    brownd
 */
public class XMLFormService {

    /**  The xml form */
    protected XMLForm xmlform = null;

    /**  The logging instance  */
    private static final Log log = LogFactory.getLog(XMLFormService.class);

    /**
     *  Service method, validates, saves, and encodes the results of the XML it
     *  receives.
     *
     *@param  elems  an array of DOM Elements, one for each form XML
     *@return        an array of result DOM Elements, including errors if
     *      appplicable
     */
    public Element[] process(Element[] elems) {
        if (xmlform == null) {
            synchronized (this) {
                loadForm();
            }
        }
        Document ownerDoc = elems[0].getOwnerDocument();
        Document doc = null;
        List results = new ArrayList();
        Element result;
        List violations = null;
        for (int x = 0; x < elems.length; x++) {
            if (log.isDebugEnabled()) {
                log.debug("Importing element " + x);
            }
            try {
                doc = importElement(elems[x]);
            } catch (IllegalArgumentException ex) {
                log.warn("Invalid input element", ex);
                continue;
            }
            result = ownerDoc.createElement("result");
            result.setAttribute("id", String.valueOf(x));
            xmlform.setXML(doc);
            violations = xmlform.validate();
            if (violations != null && violations.size() > 0) {
                encodeViolations(violations, result);
            } else {
                result.appendChild(ownerDoc.createTextNode(String.valueOf(xmlform.save())));
            }
            results.add(result);
        }
        return (Element[]) results.toArray(new Element[] {});
    }

    /**
     *  Encodes the validation errors as DOM elements and stores them in the
     *  result. Any error keys will be looked up in the Struts message resources
     *  in the default locale.
     *
     *@param  violations  A list of validation errors
     *@param  result      The result element to attach the error DOM elements
     */
    protected void encodeViolations(List violations, Element result) {
        ServletContext context = getContext();
        PropertyMessageResources res = (PropertyMessageResources) context.getAttribute(Action.MESSAGES_KEY);
        Violation v;
        Element text;
        Element error;
        Document ownerDoc = result.getOwnerDocument();
        String path;
        for (Iterator i = violations.iterator(); i.hasNext(); ) {
            v = (Violation) i.next();
            path = v.getPath();
            error = ownerDoc.createElement("error");
            error.setAttribute("property", v.getPath());
            error.setAttribute("name", v.getMessage());
            text = ownerDoc.createElement("text");
            text.appendChild(ownerDoc.createTextNode(res.getMessage(v.getMessage())));
            error.appendChild(text);
            result.appendChild(error);
        }
    }

    /**
     *  Converts the element to its own Document for processing.
     *
     *@param  e  The element to convert
     *@return    The element imported in its own document
     */
    protected Document importElement(Element e) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document result = null;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            result = builder.newDocument();
            result.appendChild(result.importNode(e, true));
        } catch (Throwable ex) {
            throw new IllegalArgumentException("Unable to import element:" + ex);
        }
        return result;
    }

    /**  Loads the XML form from Axis service options in the WSDD.  */
    protected void loadForm() {
        MessageContext msgCxt = MessageContext.getCurrentContext();
        SOAPService soapSvc = msgCxt.getService();
        String className = (String) soapSvc.getOption("xmlFormClass");
        try {
            xmlform = (XMLForm) Class.forName(className).newInstance();
        } catch (Exception ex) {
            log.error("Unable to load XMLForm class " + className, ex);
            throw new RuntimeException("Unable to initialize web serivce");
        }
        String name = (String) soapSvc.getOption("xmlFormName");
        if (name == null) {
            log.warn("Unable to find a name for this XML form: " + className + ", using default");
            name = "_default";
        }
        xmlform.setName(name);
        String phase = (String) soapSvc.getOption("xmlFormPhase");
        if (phase == null) {
            log.warn("Unable to find the validation phase for this XML " + " form:" + className + ", disabling validation");
        } else {
            String schema = (String) soapSvc.getOption("xmlFormSchema");
            if (schema == null) {
                log.warn("Unable to find the validating schema for this XML " + "form: " + className + ", disabling validation");
            } else {
                String schemaNS = (String) soapSvc.getOption("xmlFormSchemaNS");
                if (schemaNS == null) {
                    schemaNS = "http://www.ascc.net/xml/schematron";
                }
                xmlform.setSchemaNS(schemaNS);
                xmlform.setPhase(phase);
                ServletContext context = getContext();
                xmlform.setSchema(context.getResourceAsStream(schema));
            }
        }
    }

    /**
     *  Gets the servlet context from the message context
     *
     *@return    The servlet context
     */
    private ServletContext getContext() {
        HttpServlet srv = (HttpServlet) MessageContext.getCurrentContext().getProperty(HTTPConstants.MC_HTTP_SERVLET);
        ServletContext context = srv.getServletContext();
        return context;
    }
}
