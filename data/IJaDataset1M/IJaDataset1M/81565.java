package org.hmaciel.rph.persisters;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hmaciel.rph.constants.SchemaPatientFieldNames;
import org.hmaciel.rph.constants.TypeCodes;
import org.hmaciel.rph.ejb.entity.OrganizationClass;
import org.hmaciel.rph.ejb.rols.PatientRolClass;
import org.hmaciel.rph.ejb.utils.CEClass;
import org.hmaciel.rph.ejb.utils.CSClass;
import org.hmaciel.rph.ejb.utils.IIClass;
import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;

/**
 * @author pclavijo
 */
public class LdapPatientRolPersister {

    private static LdapPatientRolPersister instance = null;

    private LdapPatientRolPersister() {
    }

    /**
	 * @return instancia unica de la clase en el sistema
	 */
    public static LdapPatientRolPersister getInstance() {
        if (instance == null) {
            instance = new LdapPatientRolPersister();
        }
        return instance;
    }

    /**
	 * Persiste en LDAP
	 * 
	 * @param pat
	 *            Bean para persistir
	 * @param dnPadre
	 *            punto de montaje de la entry en el ldap
	 * @param lc
	 *            coneccion ya establecida por el llamante al ldap
	 */
    public void persist(PatientRolClass pat, String dnPadre, LDAPConnection lc) {
        if (pat != null) {
            try {
                String idMaciel = "_";
                List<IIClass> ids = pat.getId();
                Iterator<IIClass> it;
                if (ids != null) {
                    it = ids.iterator();
                    if (it.hasNext()) {
                        idMaciel = ((IIClass) it.next()).getATTR_EXTENSION();
                    }
                }
                String dn = "type=" + TypeCodes.PATIENT + "_" + idMaciel + "," + dnPadre;
                int posCodes = 0;
                lc.add(patientRolToEntry(dn, pat));
                CSClass classCode = pat.getClassCode();
                LdapCSPersister ldapCSPersister = FactoryPersistenceManagers.getInstance().getLdapCSPersister();
                ldapCSPersister.persist(classCode, dn, lc, posCodes);
                posCodes++;
                LdapIIPersister ldapPersister = FactoryPersistenceManagers.getInstance().getLdapIIPersister();
                int pos = 0;
                for (it = pat.getId().iterator(); it.hasNext(); ) {
                    ldapPersister.persist(((IIClass) it.next()), dn, lc, pos);
                    pos++;
                }
                CSClass statusCode = pat.getStatusCode();
                ldapCSPersister = FactoryPersistenceManagers.getInstance().getLdapCSPersister();
                ldapCSPersister.persist(statusCode, dn, lc, posCodes);
                posCodes++;
                CEClass veryImportantPersonCode = pat.getVeryImportantPersonCode();
                int posCodeE = 0;
                LdapCEPersister ldapCEPersister = FactoryPersistenceManagers.getInstance().getLdapCEPersister();
                ldapCEPersister.persist(veryImportantPersonCode, dn, lc, posCodeE);
                posCodeE++;
                CEClass code = pat.getCode();
                ldapCEPersister = FactoryPersistenceManagers.getInstance().getLdapCEPersister();
                ldapCEPersister.persist(code, dn, lc, posCodeE);
                posCodeE++;
            } catch (LDAPException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Genera la entry para ser persistida
	 * 
	 * @param pat
	 *            Bean para persistir
	 * @param dnPadre
	 *            punto del arbol de ldap en el que se va a persistir
	 * @param entries
	 *            colleccion de entrys para posteriormente persistir
	 * 
	 * @return genera la entry del bean que se indica y lo agrega a la
	 *         colleccion que recive
	 */
    public void generateEntry(PatientRolClass pat, String dnPadre, Collection<LDAPEntry> entries) {
        if (pat != null) {
            String idMaciel = "_";
            List<IIClass> ids = pat.getId();
            Iterator<IIClass> it;
            if (ids != null) {
                it = ids.iterator();
                if (it.hasNext()) {
                    IIClass ii = (IIClass) it.next();
                    idMaciel = ii.getATTR_EXTENSION();
                }
            }
            String dn = "type=" + TypeCodes.PATIENT + "_" + idMaciel + "," + dnPadre;
            int posCodes = 0;
            entries.add(patientRolToEntry(dn, pat));
            CSClass classCode = pat.getClassCode();
            LdapCSPersister ldapCSPersister = FactoryPersistenceManagers.getInstance().getLdapCSPersister();
            ldapCSPersister.generateEntry(classCode, dn, entries, posCodes);
            posCodes++;
            LdapIIPersister ldapPersister = FactoryPersistenceManagers.getInstance().getLdapIIPersister();
            int pos = 0;
            for (it = pat.getId().iterator(); it.hasNext(); ) {
                ldapPersister.generateEntry(((IIClass) it.next()), dn, entries, pos);
                pos++;
            }
            CSClass statusCode = pat.getStatusCode();
            ldapCSPersister = FactoryPersistenceManagers.getInstance().getLdapCSPersister();
            ldapCSPersister.generateEntry(statusCode, dn, entries, posCodes);
            posCodes++;
            CEClass veryImportantPersonCode = pat.getVeryImportantPersonCode();
            int posCodeE = 0;
            LdapCEPersister ldapCEPersister = FactoryPersistenceManagers.getInstance().getLdapCEPersister();
            ldapCEPersister.generateEntry(veryImportantPersonCode, dn, entries, posCodeE);
            posCodeE++;
            CEClass code = pat.getCode();
            ldapCEPersister = FactoryPersistenceManagers.getInstance().getLdapCEPersister();
            ldapCEPersister.generateEntry(code, dn, entries, posCodeE);
            posCodeE++;
            OrganizationClass org = (OrganizationClass) pat.getScoper();
            LdapOrganizationPersister ldapOrganizationPersister = FactoryPersistenceManagers.getInstance().getLdapOrganizationPersister();
            ldapOrganizationPersister.generateEntry(org, dn, entries);
        }
    }

    /**
	 * 
	 * @param dnPropio
	 * @param pat
	 * @return
	 */
    public LDAPEntry patientRolToEntry(String dnPropio, PatientRolClass pat) {
        LDAPEntry newEntry = null;
        if (pat != null) {
            String idMaciel = "_";
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy H:mm:ss");
            String f_mod = formatoDelTexto.format(new Date());
            LDAPAttributeSet attributeSet = new LDAPAttributeSet();
            attributeSet.add(new LDAPAttribute("objectclass", new String(SchemaPatientFieldNames.OBJECT_CLASS)));
            Iterator<IIClass> it;
            List<IIClass> ids = pat.getId();
            if (ids != null) {
                it = ids.iterator();
                if (it.hasNext()) {
                    IIClass ii = (IIClass) it.next();
                    idMaciel = ii.getATTR_EXTENSION();
                }
            }
            attributeSet.add(new LDAPAttribute(SchemaPatientFieldNames.ID_MACIEL, new String[] { idMaciel }));
            attributeSet.add(new LDAPAttribute(SchemaPatientFieldNames.MODIFICATION_TIME, new String[] { f_mod }));
            attributeSet.add(new LDAPAttribute("type", new String[] { TypeCodes.PATIENT + "_" + idMaciel }));
            newEntry = new LDAPEntry(dnPropio, attributeSet);
        }
        return newEntry;
    }
}
