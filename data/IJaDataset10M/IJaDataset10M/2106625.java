package org.hmaciel.rph.persisters;

import java.text.SimpleDateFormat;
import java.util.*;
import org.hmaciel.rph.constants.SchemaTSFieldNames;
import org.hmaciel.rph.constants.TypeCodes;
import org.hmaciel.rph.ejb.utils.TSClass;
import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;

/**
 * @author pclavijo
 */
public class LdapTSPersister {

    private static LdapTSPersister instance = null;

    private LdapTSPersister() {
    }

    /**
	 * unica instancia de la clase en el sistema
	 * 
	 * @return
	 */
    public static LdapTSPersister getInstance() {
        if (instance == null) {
            instance = new LdapTSPersister();
        }
        return instance;
    }

    /**
	 * Persiste en LDAP
	 * 
	 * @param ts
	 *            Bean para persistir
	 * @param dnPadre
	 *            punto de montaje de la entry en el ldap
	 * @param lc
	 *            coneccion ya establecida por el llamante al ldap
	 */
    public void persist(TSClass ts, String dnPadre, LDAPConnection lc) {
        if (ts != null) {
            try {
                String dn = "type=" + TypeCodes.TIME + "," + dnPadre;
                lc.add(tsToEntry(dn, ts));
            } catch (LDAPException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Genera la entry para ser persistida
	 * 
	 * @param ts
	 * @param dnPadre
	 * @param entries
	 */
    public void generateEntry(TSClass ts, String dnPadre, Collection<LDAPEntry> entries) {
        if (ts != null) {
            String dn = "type=" + TypeCodes.TIME + "," + dnPadre;
            entries.add(tsToEntry(dn, ts));
        }
    }

    /**
	 * 
	 * @param dnPropio
	 * @param ts
	 * @return
	 */
    public LDAPEntry tsToEntry(String dnPropio, TSClass ts) {
        LDAPEntry newEntry = null;
        if (ts != null) {
            String base = "_";
            Date dat = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            dat = ts.getDate();
            String f = sdf.format(dat);
            LDAPAttributeSet attributeSet = new LDAPAttributeSet();
            attributeSet.add(new LDAPAttribute("objectclass", new String(SchemaTSFieldNames.OBJECT_CLASS)));
            attributeSet.add(new LDAPAttribute(SchemaTSFieldNames.BIRTH_TIME, new String[] { base }));
            attributeSet.add(new LDAPAttribute(SchemaTSFieldNames.DATA, new String[] { f }));
            attributeSet.add(new LDAPAttribute("type", new String[] { TypeCodes.TIME }));
            newEntry = new LDAPEntry(dnPropio, attributeSet);
        }
        return newEntry;
    }
}
