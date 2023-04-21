package org.xmldb.common.xml.queries.xt;

import org.xmldb.common.xml.queries.*;

/**
 * @version $Revision: 1.1 $ $Date: 2004/04/11 10:45:41 $
 * @author <a href="http://www.softwarebuero.de">SMB</a>
 * @see XPathQuery
 */
public final class XPathQueryFactoryImpl extends XPathQueryFactory {

    public XPathQueryFactoryImpl() {
    }

    public XPathQuery newXPathQuery() throws XPathQueryConfigurationException {
        return new XPathQueryImpl();
    }
}
