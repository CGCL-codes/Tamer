package org.hmaciel.rph.persisters;

import java.util.Collection;
import org.hmaciel.rph.constants.SchemaTELFieldNames;
import org.hmaciel.rph.constants.TypeCodes;
import org.hmaciel.rph.ejb.utils.TELClass;
import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;

/**
 * @author pclavijo
 */
public class LdapTELPersister {

    private static LdapTELPersister instance = null;

    private LdapTELPersister() {
    }

    public static LdapTELPersister getInstance() {
        if (instance == null) {
            instance = new LdapTELPersister();
        }
        return instance;
    }

    /**
	 * Persiste en LDAP
	 * 
	 * @param tel
	 *            Bean para persistir
	 * @param dnPadre
	 *            punto de montaje de la entry en el ldap
	 * @param lc
	 *            coneccion ya establecida por el llamante al ldap
	 */
    public void persist(TELClass tel, String dnPadre, LDAPConnection lc) {
        if (tel != null) {
            try {
                String dn = "type=" + TypeCodes.TELEPHONE + "," + dnPadre;
                lc.add(telToEntry(dn, tel));
            } catch (LDAPException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Genera la entry para ser persistida
	 * 
	 * @param tel
	 *            Bean para persistir
	 * @param dnPadre
	 *            punto del arbol de ldap en el que se va a persistir
	 * @param entries
	 *            colleccion de entrys para posteriormente persistir
	 * 
	 * @return genera la entry del bean que se indica y lo agrega a la
	 *         colleccion que recive
	 */
    public void generateEntry(TELClass tel, String dnPadre, Collection<LDAPEntry> entries) {
        if (tel != null) {
            String dn = "type=" + TypeCodes.TELEPHONE + "," + dnPadre;
            entries.add(telToEntry(dn, tel));
        }
    }

    /**
	 * 
	 * @param dnPropio
	 * @param tel
	 * @return
	 */
    public LDAPEntry telToEntry(String dnPropio, TELClass tel) {
        LDAPEntry newEntry = null;
        if (tel != null) {
            String num = "_";
            num = tel.getTel();
            LDAPAttributeSet attributeSet = new LDAPAttributeSet();
            attributeSet.add(new LDAPAttribute("objectclass", new String(SchemaTELFieldNames.OBJECT_CLASS)));
            attributeSet.add(new LDAPAttribute(SchemaTELFieldNames.TELEPHONE_NUMBER, new String[] { num }));
            attributeSet.add(new LDAPAttribute("type", new String[] { TypeCodes.TELEPHONE }));
            newEntry = new LDAPEntry(dnPropio, attributeSet);
        }
        return newEntry;
    }
}
