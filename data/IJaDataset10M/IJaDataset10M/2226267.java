package org.hmaciel.rph.helper.consultants;

import org.hmaciel.rph.ejb.utils.ADXPClass;
import org.hmaciel.rph.helper.SchemaADXPFieldNames;
import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPEntry;

/**
 * @author pclavijo
 */
public class LdapADXPLoader {

    private static LdapADXPLoader instance = null;

    private LdapADXPLoader() {
    }

    public static LdapADXPLoader getInstance() {
        if (instance == null) {
            instance = new LdapADXPLoader();
        }
        return instance;
    }

    /**
	 * 
	 * @param adxpEntry
	 * @return
	 */
    public ADXPClass entryToADXP(LDAPEntry adxpEntry) {
        ADXPClass adxpclass = new ADXPClass();
        if (adxpEntry != null) {
            LDAPAttributeSet attributeSet = adxpEntry.getAttributeSet();
            LDAPAttribute attr;
            attr = attributeSet.getAttribute(SchemaADXPFieldNames.ADDRESS);
            adxpclass.setDir(attr.getStringValue());
            attr = attributeSet.getAttribute(SchemaADXPFieldNames.DETERMINER_CODE);
            adxpclass.setTipo(attr.getStringValue());
        }
        return adxpclass;
    }
}
