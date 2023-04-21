package org.hmaciel.rph.helper.consultants.finders;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import org.hmaciel.rph.ejb.querys.ParameterItemClass;
import org.hmaciel.rph.ejb.querys.SemanticNames;
import org.hmaciel.rph.ejb.utils.ADClass;
import org.hmaciel.rph.ejb.utils.CEClass;
import org.hmaciel.rph.ejb.utils.ENClass;
import org.hmaciel.rph.ejb.utils.IIClass;
import org.hmaciel.rph.helper.FieldConstant;
import org.hmaciel.rph.helper.consultants.FactoryLoadersManagers;
import org.hmaciel.rph.helper.rimclassextension.PersonClassExtension;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;

/**
 * @author pclavijo
 */
public class FinderByConcrete {

    /**
	 * @author pclavijo
	 */
    public List<PersonClassExtension> getPotencialPersons(ParameterItemClass parameter) {
        List<PersonClassExtension> potencials = new LinkedList<PersonClassExtension>();
        int ldapPort = LDAPConnection.DEFAULT_PORT;
        int ldapVersion = LDAPConnection.LDAP_V3;
        String ldapHost = FieldConstant.LDAP_HOST;
        String loginDN = FieldConstant.LOGIN_DN;
        String password = FieldConstant.SERVER_PASSWORD;
        LDAPConnection lc = new LDAPConnection();
        try {
            lc.connect(ldapHost, ldapPort);
            lc.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
            String[] atributos = { "*" };
            String baseBusqueda = FieldConstant.DN_BASE;
            boolean soloMostrarTipos = false;
            int scope = LDAPConnection.SCOPE_SUB;
            String semanticText = parameter.getSemanticText();
            String filtro = "";
            if (semanticText.equalsIgnoreCase(SemanticNames.PATIENT_ADDRESS)) {
                ADClass adcl = (ADClass) parameter.getValue();
                filtro = UtilsFilters.makeFilterStringForAD(adcl);
            } else if (semanticText.equalsIgnoreCase(SemanticNames.LIVING_SUBJECT_ADMINISTRATIVE_GENDER)) {
                CEClass cecl = (CEClass) parameter.getValue();
                filtro = UtilsFilters.makeFilterStringForCE(cecl);
            } else if (semanticText.equalsIgnoreCase(SemanticNames.LIVING_SUBJECT_BIRTH_TIME)) {
            } else if (semanticText.equalsIgnoreCase(SemanticNames.LIVING_SUBJECT_NAME)) {
                ENClass encl = (ENClass) parameter.getValue();
                filtro = UtilsFilters.makeFilterStringForEN(encl);
            } else if (semanticText.equalsIgnoreCase(SemanticNames.OTHERS_IDS_SCOPING_ORGANIZATION)) {
                IIClass iicl = (IIClass) parameter.getValue();
                filtro = UtilsFilters.makeFilterStringForII(iicl);
            }
            System.out.println(filtro);
            LDAPSearchResults searchResults = lc.search(baseBusqueda, scope, filtro, atributos, soloMostrarTipos);
            Finder find = FactoryLoadersManagers.getInstance().getFinder();
            while (searchResults.hasMore()) {
                LDAPEntry entry = searchResults.next();
                String extencion = UtilsFilters.getPatientExtencion(entry);
                potencials.add(find.getPersonById(extencion));
            }
            lc.disconnect();
        } catch (LDAPException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return potencials;
    }
}
