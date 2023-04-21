package org.jfree.report.modules.factories.report.compatibility.simple;

import org.jfree.xmlns.parser.XmlDocumentInfo;
import org.jfree.xmlns.parser.XmlFactoryModule;
import org.jfree.xmlns.parser.XmlReadHandler;

/**
 * Creation-Date: 08.04.2006, 14:38:27
 *
 * @author Thomas Morgner
 */
public class SimpleXmlFactoryModule implements XmlFactoryModule {

    public SimpleXmlFactoryModule() {
    }

    public int getDocumentSupport(XmlDocumentInfo documentInfo) {
        return 0;
    }

    public XmlReadHandler createReadHandler(XmlDocumentInfo documentInfo) {
        return null;
    }

    public String getDefaultNamespace(XmlDocumentInfo documentInfo) {
        return null;
    }
}
