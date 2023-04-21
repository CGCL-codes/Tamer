package com.afp.ines.component.persistence.factory.util;

import java.net.URI;
import org.iptc.nar.core.datatype.QCodeType;

public class PersistenceUtil {

    /**
	 * Method use to make concatenation URI value form the prefix URI value and
	 * the local code
	 * 
	 * @param qcode
	 * @return
	 */
    public static URI buildURIFormQCodeType(QCodeType qcode) {
        URI schemeURI = qcode.getSchemeURI();
        String struri = schemeURI.toString();
        String codeuri = struri + qcode.getLocalCode();
        return URI.create(codeuri);
    }
}
