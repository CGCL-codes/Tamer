package org.genos.gmf.resources.formatters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.genos.gmf.Configuration;
import org.genos.gmf.RequestParameters;
import org.genos.gmf.resources.formatters.FormatterResult.RESULTCONTENTTYPE;
import org.genos.xml.xmlTransform;
import org.genos.xml.xmlUtil;
import org.genos.xml.xslCache;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class XSLTFormatter extends RFormatter {

    private static Vector<String> paramnames;

    static {
        paramnames = new Vector<String>();
        paramnames.add("context");
        paramnames.add("rid");
        paramnames.add("uid");
        paramnames.add("displayname");
    }

    /**
     * XSL file
     */
    private String fxsl = null;

    /**
     * Additional XMLs array
     */
    private ArrayList<IXMLSource> addXmls = null;

    /**
     * Defines the xsl file to use in the transformation
     * @param xsl   XSL file
     */
    public void setXsl(String xsl) {
        fxsl = xsl;
    }

    /**
     * Defines an XML code that will be added to the generated XML code.
     * This method can be called multiple times with different xmls to be added.
     * @param dom	XML Document
     */
    public void addXmlSource(IXMLSource s) {
        if (s == null) return;
        if (addXmls == null) addXmls = new ArrayList<IXMLSource>();
        addXmls.add(s);
    }

    /**
     * Appends xml code with localized strings for the current language
     * @param e			Element where we will append the xml with the strings
     * @param xslFile	Xsl file with the localized strings to be imported
     * @param lang		Active language
     */
    private void addLocalizedStrings(Element e, String xslFile, String lang) {
        if (xslFile == null) return;
        File fXslFile = new File(xslFile);
        String fxslname = fXslFile.getName();
        String docStringsFile = Configuration.homeConfig + "lang/xsl/" + fxslname.substring(0, fxslname.lastIndexOf('.')) + "." + lang + ".xml";
        Configuration.logger.debug("XSLTFormatter.buildHTML(): looking for localized strings file: " + docStringsFile);
        File fStringsFile = new File(docStringsFile);
        if (fStringsFile.exists()) {
            Document docStrings = xslCache.getDocument(docStringsFile);
            if (docStrings != null) {
                Element root = docStrings.getDocumentElement();
                if (root != null) {
                    Node n = e.getOwnerDocument().importNode(root, true);
                    e.appendChild(n);
                }
            }
        }
    }

    /**
     * Build the html.
     * This document has the following format:
     * 
     * <workarea>
     * 		buildXML code
     * 		additional XML code
     * </workarea>
     * 
     * or
     * 
     * <async>
     *      buildXML code
     * </async>
     * 
     * @param	uid		User id
     * @param	rid		Resource id
     * @return			HTML/XML code.
     */
    public FormatterResult buildResult(RequestParameters rp, Integer uid, Integer rid) throws Exception {
        FormatterResult fr = new FormatterResult();
        Node n;
        Document docXSL = null;
        StringBuilder html = null;
        xmlTransform xmlt = null;
        String lang = Configuration.getUserProperty(uid, "lang");
        DocumentBuilderFactory docbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docb = docbf.newDocumentBuilder();
        Document docXML = docb.newDocument();
        String async = rp.getParameter("async");
        if (async != null) {
            String callback = rp.getParameter("callback");
            if (callback != null) return callbackMethod(async, callback);
        }
        Document built = buildXML(uid, rid, async);
        if (built == null) {
            Configuration.logger.error("XSLTFormatter.buildHTML(): built xml is null.");
            return null;
        }
        if (async != null) {
            Element param = docXML.createElement("async");
            docXML.appendChild(param);
            Element root = built.getDocumentElement();
            if (root != null) {
                n = docXML.importNode(root, true);
                param.appendChild(n);
            }
            addLocalizedStrings(param, fxsl, lang);
        } else {
            Element wa = docXML.createElement("workarea");
            docXML.appendChild(wa);
            Element root = built.getDocumentElement();
            if (root != null) {
                n = docXML.importNode(root, true);
                wa.appendChild(n);
            }
            addLocalizedStrings(wa, fxsl, lang);
            if (addXmls != null) {
                for (IXMLSource s : addXmls) {
                    s.setUid(uid);
                    Document add = s.buildXml();
                    if (add == null) {
                        Configuration.logger.error("XSLTFormatter.buildHTML(): additional xml is null.");
                        return null;
                    }
                    root = add.getDocumentElement();
                    if (root != null) {
                        n = docXML.importNode(root, true);
                        wa.appendChild(n);
                    } else {
                        Configuration.logger.debug("XSLTFormatter.buildHTML(): additional XML code has a null root element: rid=" + rid + "; uid=" + uid);
                    }
                }
            }
        }
        if (Configuration.logformattersxml && Configuration.logger.isDebugEnabled()) Configuration.logger.debug("XSLTFormatter.buildHTML(): xml=" + xmlUtil.NodeToString(docXML));
        if (fxsl == null) {
            fr.setResult(xmlUtil.NodeToString(docXML));
            fr.setContentType(RESULTCONTENTTYPE.XML);
            return fr;
        }
        xmlt = new xmlTransform(docXML, fxsl);
        try {
            Vector<Object> paramvalues = new Vector<Object>();
            paramvalues.add(Configuration.context);
            paramvalues.add(rid);
            paramvalues.add(uid);
            paramvalues.add(rp.getParameter("session_displayname"));
            html = new StringBuilder(xmlt.transform(paramnames, paramvalues));
        } catch (Exception e) {
            Configuration.logger.error("XSLTFormatter.buildHTML(): Error doing xml/xsl transformation. xml='" + xmlUtil.NodeToString(docXML) + "', xsl='" + xmlUtil.NodeToString(docXSL) + "': ", e);
            return null;
        }
        fr.setContentType(RESULTCONTENTTYPE.HTML);
        fr.setResult(html.toString());
        if (async != null) return fr;
        return doChain(fr);
    }

    /**
     * Builds the xml for a subform.
     * The format is like this:
     * <subform name="n">
     *      xml of the subform resources, as generated by formatters defined for every one of them.
     * </subform>
     * @param uid           User id
     * @param d             XML document
     * @param p             Input Parameters Map.
     * @param param     VParam name.
     * @param o             VParam value.
     * @return              The generated xml tree.
     */
    protected Element buildXmlSubform(Integer uid, Document d, HashMap<String, Object> p, String param, Object o) throws Exception {
        Element e = d.createElement("subform");
        e.setAttribute("name", param);
        HashMap<Integer, RFormatter> sff = (HashMap<Integer, RFormatter>) o;
        for (Integer sfrid : sff.keySet()) {
            RFormatter f = sff.get(sfrid);
            if (!(f instanceof XSLTFormatter)) {
                throw new Exception("XSLTFormatter.buildXmlSubform(): the formatter of a resource in the subform is not an instance of XSLTFormatter. Subform resource id = " + sfrid);
            }
            XSLTFormatter xsltf = (XSLTFormatter) f;
            Document xmlRes = xsltf.buildXML(uid, sfrid, null);
            if (xmlRes == null) {
                Configuration.logger.error("XSLTFormatter.buildXmlSubform(): the formatter of a subform's resource has returned null. Subform resource id = " + sfrid);
                continue;
            }
            Element root = xmlRes.getDocumentElement();
            if (root == null) {
                Configuration.logger.error("XSLTFormatter.buildXmlSubform(): root element of the xml document returned by a formatter of a subform's resource is null. Subform resource id = " + sfrid);
                continue;
            }
            Node n = d.importNode(root, true);
            e.appendChild(n);
        }
        return e;
    }

    /**
     * Builds the XML definition of a VParam.
     * The format is like this:
     *          <param name="n" value="v" description="desc"/>
     *          ...
     *          <param name="n" multivalue="yes">
     *              <value>v1</value>
     *              ...
     *          </param>
     * @param d             XML Document.
     * @param p             Input Parameters Map.
     * @param param     VParam name.
     * @param o             VParam value.
     * @return              The generated xml tree.
     */
    protected Element buildXmlParam(Document d, HashMap<String, Object> p, String param, Object o) {
        Element e = d.createElement("param");
        e.setAttribute("name", param);
        if (o instanceof ArrayList) {
            e.setAttribute("multivalue", "yes");
            ArrayList values = (ArrayList) o;
            for (Iterator it = values.iterator(); it.hasNext(); ) {
                Object value = it.next();
                xmlUtil.appendChild(e, "value", vparamValueToString(value));
            }
        } else {
            e.setAttribute("value", vparamValueToString(o));
        }
        o = p.get("__" + param + "_description");
        if (o != null) e.setAttribute("description", (String) o);
        return e;
    }

    /**
     * Builds all the XML for all the defined VParams, and appends it under the root element.
     * @param uid       User id.
     * @param xml       XML Document
     * @param root          Root element under which we'll append the generated xml code for the vparams.
     * @param p             Input parameters Map
     * @throws Exception
     */
    protected void buildXmlVParams(Integer uid, Document xml, Element root, HashMap<String, Object> p) throws Exception {
        Object o;
        Element e2;
        for (String param : p.keySet()) {
            if (param.startsWith("__")) continue;
            o = p.get(param);
            if (o instanceof HashMap) {
                e2 = buildXmlSubform(uid, xml, p, param, o);
            } else {
                e2 = buildXmlParam(xml, p, param, o);
            }
            root.appendChild(e2);
        }
    }

    /**
     * Calls a callback method and returns its results (instead of calling buildXML and return the usual xml document)
     * This is a way to be able to call a method in the formatter to return direct results.
     * The url of this call would be: mainUrl+'&async=[asyncvalue]&callback=[method]&usercommit=0'. Async and callback
     * must be both present, and their actual meaning is up to the formatter.
     * @param param		Parameter name.
     * @param method	Method name
     * @return			The return of the method
     */
    protected abstract FormatterResult callbackMethod(String async, String callback);

    /**
     * Buils the XML document.
     * @param uid     User id
     * @param rid     Resource id
     * @param parameter Parameter to be build. If null, it build the complete form.
     * @return  XML document
     */
    protected abstract Document buildXML(Integer uid, Integer rid, String parameter);
}
