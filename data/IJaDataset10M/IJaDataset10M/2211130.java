package main.usuarios;

import java.io.UnsupportedEncodingException;
import javax.ejb.Stateless;
import org.opensih.servicioJMX.invocador.InvocadorService2;
import org.opensih.servicioJMX.invocador.InvocadorServiceBean2;
import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPModification;
import com.novell.ldap.LDAPSearchResults;

@Stateless
public class CtaUsuario implements ICtaUsuario {

    int ldapPort = LDAPConnection.DEFAULT_PORT;

    int ldapVersion = LDAPConnection.LDAP_V3;

    String loginDN = "cn=Manager,dc=usuariosREHM";

    public void persistirUsuario(Usuario user) throws LDAPException {
        LDAPConnection lc = new LDAPConnection();
        InvocadorService2 inv = InvocadorServiceBean2.getInstance();
        String ldapHost = inv.getLdap_Host();
        String password = inv.getLdap_Password();
        LDAPAttributeSet attributeSet = new LDAPAttributeSet();
        attributeSet.add(new LDAPAttribute("objectClass", new String[] { "top", "uidObject", "person", "inetOrgPerson" }));
        attributeSet.add(new LDAPAttribute("uid", user.getUser()));
        attributeSet.add(new LDAPAttribute("roles", "'cn=" + user.getRol() + ",ou=Roles,dc=usuariosREHM'"));
        attributeSet.add(new LDAPAttribute("enabled", "TRUE"));
        attributeSet.add(new LDAPAttribute("cn", user.getNombre() + " " + user.getApellido()));
        attributeSet.add(new LDAPAttribute("gn", user.getNombre()));
        attributeSet.add(new LDAPAttribute("sn", user.getApellido()));
        attributeSet.add(new LDAPAttribute("userPassword", user.getPassword()));
        attributeSet.add(new LDAPAttribute("description", user.getCi() + user.getGuion()));
        String dn = "uid=" + user.getUser() + ",ou=people,dc=usuariosREHM";
        LDAPEntry newEntry1 = new LDAPEntry(dn, attributeSet);
        try {
            lc.connect(ldapHost, ldapPort);
            lc.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
            lc.add(newEntry1);
            lc.disconnect();
        } catch (LDAPException e) {
            throw e;
        } catch (UnsupportedEncodingException e) {
        }
    }

    public Usuario obtenerUsuario(String nombre) {
        Usuario user = null;
        LDAPConnection lc = new LDAPConnection();
        InvocadorService2 inv = InvocadorServiceBean2.getInstance();
        String ldapHost = inv.getLdap_Host();
        String password = inv.getLdap_Password();
        try {
            lc.connect(ldapHost, ldapPort);
            lc.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
            boolean soloMostrarTipos = false;
            int scope = LDAPConnection.SCOPE_SUB;
            String baseBusqueda = "uid=" + nombre + ",ou=people,dc=usuariosREHM";
            String filtro = "";
            String[] atributos = { "*" };
            LDAPSearchResults searchResults = lc.search(baseBusqueda, scope, filtro, atributos, soloMostrarTipos);
            while (searchResults.hasMore()) {
                LDAPEntry entry = searchResults.next();
                user = new Usuario();
                user.setNombre(entry.getAttribute("givenName").getStringValue());
                user.setApellido(entry.getAttribute("sn").getStringValue());
                user.setPassword(entry.getAttribute("userPassword").getStringValue());
                user.setCi(entry.getAttribute("description").getStringValue());
                user.setUser(entry.getAttribute("uid").getStringValue());
                String role = entry.getAttribute("roles").getStringValue();
                user.setRol(role.substring(4, role.indexOf(",ou=Roles")));
            }
            lc.disconnect();
        } catch (LDAPException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return user;
    }

    public void modificarPass(String nombre, String nuevopass) throws LDAPException {
        LDAPConnection lc = new LDAPConnection();
        InvocadorService2 inv = InvocadorServiceBean2.getInstance();
        String ldapHost = inv.getLdap_Host();
        String password = inv.getLdap_Password();
        String dn = "uid=" + nombre + ",ou=people,dc=usuariosREHM";
        try {
            lc.connect(ldapHost, ldapPort);
            lc.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
            LDAPAttribute nueva = new LDAPAttribute("userPassword", nuevopass);
            LDAPModification mod = new LDAPModification(LDAPModification.REPLACE, nueva);
            lc.modify(dn, mod);
            lc.disconnect();
        } catch (LDAPException e) {
            throw e;
        } catch (UnsupportedEncodingException e) {
        }
    }
}
