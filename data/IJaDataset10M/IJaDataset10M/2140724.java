package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.impl.msg.XMLMessageFormatter;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XNIException;

/**
 * The DTD validator. The validator implements a document
 * filter: receiving document events from the scanner; validating
 * the content and structure; augmenting the InfoSet, if applicable;
 * and notifying the parser of the information resulting from the
 * validation process.
 * <p> Formerly, this component also handled DTD events and grammar construction.
 * To facilitate the development of a meaningful DTD grammar caching/preparsing
 * framework, this functionality has been moved into the XMLDTDLoader
 * class.  Therefore, this class no longer implements the DTDFilter
 * or DTDContentModelFilter interfaces.
 * <p>
 * This component requires the following features and properties from the
 * component manager that uses it:
 * <ul>
 *  <li>http://xml.org/sax/features/namespaces</li>
 *  <li>http://xml.org/sax/features/validation</li>
 *  <li>http://apache.org/xml/features/validation/dynamic</li>
 *  <li>http://apache.org/xml/properties/internal/symbol-table</li>
 *  <li>http://apache.org/xml/properties/internal/error-reporter</li>
 *  <li>http://apache.org/xml/properties/internal/grammar-pool</li>
 *  <li>http://apache.org/xml/properties/internal/datatype-validator-factory</li>
 * </ul>
 * 
 * @xerces.internal
 *
 * @author Elena Litani, IBM
 * @author Michael Glavassevich, IBM
 *

 */
public class XML11NSDTDValidator extends XML11DTDValidator {

    /** Attribute QName. */
    private QName fAttributeQName = new QName();

    /** Bind namespaces */
    protected final void startNamespaceScope(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        fNamespaceContext.pushContext();
        if (element.prefix == XMLSymbols.PREFIX_XMLNS) {
            fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN, "ElementXMLNSPrefix", new Object[] { element.rawname }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
        }
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            String localpart = attributes.getLocalName(i);
            String prefix = attributes.getPrefix(i);
            if (prefix == XMLSymbols.PREFIX_XMLNS || prefix == XMLSymbols.EMPTY_STRING && localpart == XMLSymbols.PREFIX_XMLNS) {
                String uri = fSymbolTable.addSymbol(attributes.getValue(i));
                if (prefix == XMLSymbols.PREFIX_XMLNS && localpart == XMLSymbols.PREFIX_XMLNS) {
                    fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN, "CantBindXMLNS", new Object[] { attributes.getQName(i) }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }
                if (uri == NamespaceContext.XMLNS_URI) {
                    fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN, "CantBindXMLNS", new Object[] { attributes.getQName(i) }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }
                if (localpart == XMLSymbols.PREFIX_XML) {
                    if (uri != NamespaceContext.XML_URI) {
                        fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN, "CantBindXML", new Object[] { attributes.getQName(i) }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
                    }
                } else {
                    if (uri == NamespaceContext.XML_URI) {
                        fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN, "CantBindXML", new Object[] { attributes.getQName(i) }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
                    }
                }
                prefix = localpart != XMLSymbols.PREFIX_XMLNS ? localpart : XMLSymbols.EMPTY_STRING;
                fNamespaceContext.declarePrefix(prefix, uri.length() != 0 ? uri : null);
            }
        }
        String prefix = element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING;
        element.uri = fNamespaceContext.getURI(prefix);
        if (element.prefix == null && element.uri != null) {
            element.prefix = XMLSymbols.EMPTY_STRING;
        }
        if (element.prefix != null && element.uri == null) {
            fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN, "ElementPrefixUnbound", new Object[] { element.prefix, element.rawname }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
        }
        for (int i = 0; i < length; i++) {
            attributes.getName(i, fAttributeQName);
            String aprefix = fAttributeQName.prefix != null ? fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
            String arawname = fAttributeQName.rawname;
            if (arawname == XMLSymbols.PREFIX_XMLNS) {
                fAttributeQName.uri = fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
                attributes.setName(i, fAttributeQName);
            } else if (aprefix != XMLSymbols.EMPTY_STRING) {
                fAttributeQName.uri = fNamespaceContext.getURI(aprefix);
                if (fAttributeQName.uri == null) {
                    fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN, "AttributePrefixUnbound", new Object[] { element.rawname, arawname, aprefix }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }
                attributes.setName(i, fAttributeQName);
            }
        }
        int attrCount = attributes.getLength();
        for (int i = 0; i < attrCount - 1; i++) {
            String auri = attributes.getURI(i);
            if (auri == null || auri == NamespaceContext.XMLNS_URI) {
                continue;
            }
            String alocalpart = attributes.getLocalName(i);
            for (int j = i + 1; j < attrCount; j++) {
                String blocalpart = attributes.getLocalName(j);
                String buri = attributes.getURI(j);
                if (alocalpart == blocalpart && auri == buri) {
                    fErrorReporter.reportError(XMLMessageFormatter.XMLNS_DOMAIN, "AttributeNSNotUnique", new Object[] { element.rawname, alocalpart, auri }, XMLErrorReporter.SEVERITY_FATAL_ERROR);
                }
            }
        }
    }

    /** Handles end element. */
    protected void endNamespaceScope(QName element, Augmentations augs, boolean isEmpty) throws XNIException {
        String eprefix = element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING;
        element.uri = fNamespaceContext.getURI(eprefix);
        if (element.uri != null) {
            element.prefix = eprefix;
        }
        if (fDocumentHandler != null) {
            if (!isEmpty) {
                fDocumentHandler.endElement(element, augs);
            }
        }
        fNamespaceContext.popContext();
    }
}
