package org.jfree.report.modules.factories.data.sql;

import org.jfree.xmlns.parser.XmlReadHandler;
import org.jfree.report.modules.data.sql.ConnectionProvider;

/**
 * Creation-Date: Dec 17, 2006, 8:58:33 PM
 *
 * @author Thomas Morgner
 */
public interface ConnectionReadHandler extends XmlReadHandler {

    public ConnectionProvider getProvider();
}
