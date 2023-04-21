package com.oroad.stxx.transform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import com.oroad.stxx.action.Action;
import com.oroad.stxx.action.ActionTransform;
import com.oroad.stxx.transform.document.BuilderRules;
import com.oroad.stxx.util.StxxProperties;
import com.oroad.stxx.xform.XMLForm;
import com.oroad.stxx.xform.XMLFormFilter;
import org.jdom.Document;
import org.jdom.transform.JDOMResult;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.xmlform.Form;
import org.xmlform.transformation.XMLFormTransformerHandler;
import org.xmlform.validation.Violation;

/**
 *  This transformer extends the XSL transformer to add support for XForms by
 *  using the <a href="http://www.xmlform.org">XMLForm</a> library.
 *
 *@author    Don Brown
 */
public class CachedXFormTransformer extends CachedXSLTransformer implements com.oroad.stxx.transform.Transformer {

    /**  Constructor */
    public CachedXFormTransformer() {
        super();
    }

    /**
     *  Initializes the transformer
     *
     *@param  props    The properties
     *@param  context  The servlet context
     *@param  rules    The builder rules
     *@param  name     Description of the Parameter
     */
    public void init(String name, StxxProperties props, ServletContext context, BuilderRules rules) {
        super.init(name, props, context, rules);
    }

    /**
     *  Creates and transforms the XML using XSL and a special XMLForm
     *  transformer.
     *
     *@param  trans                     The value object holding transformation
     *      information
     *@param  response                  The response object
     *@param  request                   The request object
     *@exception  TransformerException  If something goes wrong
     */
    public void transform(ActionTransform trans, HttpServletRequest request, HttpServletResponse response) throws TransformerException {
        XMLReader reader = null;
        boolean isFormInSession = false;
        ActionMapping mapping = (ActionMapping) request.getAttribute(Action.MAPPING_KEY);
        String frmName = mapping.getAttribute();
        if (frmName == null) {
            frmName = mapping.getName();
        }
        ActionForm aform = null;
        Locale locale = (Locale) request.getSession().getAttribute(Action.LOCALE_KEY);
        if (locale == null) {
            locale = request.getLocale();
        }
        MessageResources msg = (MessageResources) request.getAttribute(com.oroad.stxx.action.Action.MESSAGES_KEY);
        if ("request".equals(mapping.getScope())) {
            aform = (ActionForm) request.getAttribute(frmName);
            request.getSession().removeAttribute(frmName);
            request.removeAttribute(frmName);
        } else {
            isFormInSession = true;
            aform = (ActionForm) request.getSession().getAttribute(frmName);
            request.getSession().removeAttribute(frmName);
        }
        Form form = null;
        if (aform instanceof XMLForm) {
            form = ((XMLForm) aform).getForm();
            if (form == null) {
                throw new TransformerException("The XMLForm was not loaded correctly");
            }
        } else {
            form = new Form(frmName, aform);
        }
        form.save(request, Form.SCOPE_REQUEST);
        addErrors(request, form, locale, msg);
        try {
            reader = XMLReaderFactory.createXMLReader();
            XMLFormTransformerHandler handler = new XMLFormTransformerHandler(request);
            JDOMResult result = new JDOMResult();
            reader.setContentHandler(handler);
            reader.setDTDHandler(handler);
            String xform = null;
            if (aform instanceof XMLForm) {
                XMLFormFilter filter = new XMLFormFilter(locale, msg, (XMLForm) aform);
                xform = ((XMLForm) aform).getFormPath();
                handler.setContentHandler((ContentHandler) filter);
                handler.setLexicalHandler((LexicalHandler) filter);
                filter.setResult(result);
            } else {
                xform = trans.getParameter("xform");
                handler.setResult(result);
            }
            InputSource input = new InputSource(context.getResourceAsStream(xform));
            reader.parse(input);
            Document doc = result.getDocument();
            ArrayList documents = (ArrayList) request.getAttribute(Action.DOCUMENT_KEY);
            if (documents == null) {
                documents = new ArrayList();
            }
            documents.add(doc);
            request.setAttribute(Action.DOCUMENT_KEY, documents);
        } catch (Exception ex) {
            log.error(ex, ex);
        } finally {
            request.removeAttribute(frmName);
        }
        super.transform(trans, request, response);
        if (isFormInSession && aform != null) {
            request.getSession().setAttribute(frmName, aform);
        }
    }

    /**
     *  Adds violations to the xform taken from the action errors.  Also looks
     *  up error keys in the message resources
     *
     *@param  request  The http request
     *@param  form     The XMLForm form
     *@param  locale   The current locale
     *@param  msg      The The message resources
     */
    protected void addErrors(HttpServletRequest request, Form form, Locale locale, MessageResources msg) {
        ActionErrors ae = (ActionErrors) request.getAttribute(Action.ERROR_KEY);
        ActionError error;
        String txt;
        String prop;
        if (ae != null) {
            for (Iterator i = ae.properties(); i.hasNext(); ) {
                prop = (String) i.next();
                for (Iterator it = ae.get(prop); it.hasNext(); ) {
                    error = (ActionError) it.next();
                    if (msg != null) {
                        txt = msg.getMessage(locale, error.getKey(), error.getValues());
                    } else {
                        txt = error.getKey();
                    }
                    form.addViolation(new Violation(prop, txt));
                }
            }
        }
    }
}
