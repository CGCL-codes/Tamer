package org.hmaciel.rph.persisters;

import java.util.Collection;
import org.hmaciel.rph.constants.SchemaIIFieldNames;
import org.hmaciel.rph.constants.TypeCodes;
import org.hmaciel.rph.ejb.utils.IIClass;
import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;

/**
 * @author pclavijo
 */
public class LdapIIPersister {

    private static LdapIIPersister instance = null;

    private LdapIIPersister() {
    }

    /**
	 * 
	 * @return instancia unica de la clase en el sistema
	 */
    public static LdapIIPersister getInstance() {
        if (instance == null) {
            instance = new LdapIIPersister();
        }
        return instance;
    }

    /**
	 * Persiste en LDAP
	 * 
	 * @param ii
	 *            Bean para persistir
	 * @param dnPadre
	 *            punto del arbol de ldap en el que se va a persistir
	 * @param entries
	 *            colleccion de entrys para posteriormente persistir
	 * 
	 * @return genera la entry del bean que se indica y lo agrega a la
	 *         colleccion que recive
	 */
    public void persist(IIClass ii, String dnPadre, LDAPConnection lc, int position) {
        if (ii != null) {
            try {
                String dn = "type=" + TypeCodes.IDENTIFIER + "_" + position + "," + dnPadre;
                lc.add(iiToEntry(dn, ii, position));
            } catch (LDAPException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Genera la entry para ser persistida
	 * 
	 * @param ii
	 * @param dnPadre
	 * @param entries
	 */
    public void generateEntry(IIClass ii, String dnPadre, Collection<LDAPEntry> entries, int position) {
        if (ii != null) {
            String dn = "type=" + TypeCodes.IDENTIFIER + "_" + position + "," + dnPadre;
            entries.add(iiToEntry(dn, ii, position));
        }
    }

    /**
	 * 
	 * @param dnPropio
	 * @param ii
	 * @return
	 */
    public LDAPEntry iiToEntry(String dnPropio, IIClass ii, int position) {
        LDAPEntry newEntry = null;
        if (ii != null) {
            LDAPAttributeSet attributeSet = new LDAPAttributeSet();
            attributeSet.add(new LDAPAttribute("objectclass", new String(SchemaIIFieldNames.OBJECT_CLASS)));
            String extension = ii.getATTR_EXTENSION();
            String root = ii.getATTR_ROOT();
            String aan = ii.getATTR_AAN();
            String displayable = ii.getATTR_DISPLAYABLE();
            attributeSet.add(new LDAPAttribute("type", new String[] { TypeCodes.IDENTIFIER + "_" + position }));
            attributeSet.add(new LDAPAttribute(SchemaIIFieldNames.EXTENSION, new String[] { extension }));
            attributeSet.add(new LDAPAttribute(SchemaIIFieldNames.ROOT, new String[] { root }));
            attributeSet.add(new LDAPAttribute(SchemaIIFieldNames.ASSIGNING_AUTHORITY_NAME, new String[] { aan }));
            attributeSet.add(new LDAPAttribute(SchemaIIFieldNames.DISPLAYABLE, new String[] { displayable }));
            newEntry = new LDAPEntry(dnPropio, attributeSet);
        }
        return newEntry;
    }
}
