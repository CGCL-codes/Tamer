package com.jvantage.ce.facilities.external.ejb.instances.jvantage;

import javax.ejb.Remote;

/**
 *
 * @author Brent Clay
 */
@Remote
public interface EnumGeneratorRemote {

    public com.jvantage.ce.facilities.external.SnapInResponse generateJavaEnumeration(com.jvantage.ce.presentation.PageContext pageContext) throws com.jvantage.ce.facilities.external.SnapInException;

    public java.lang.StringBuilder generateEnumForTable(com.jvantage.ce.session.SessionID sessionID, java.lang.String tableName);
}
