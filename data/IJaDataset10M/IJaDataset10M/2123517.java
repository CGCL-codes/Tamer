package main.usuarios;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import main.utils.Validadores.VerificadorCedula;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import com.novell.ldap.LDAPException;

@Stateful
@Name("ctaUser")
public class RegisterUserAction implements IRegisterUser {

    @Logger
    private Log log;

    @In
    private org.jboss.seam.security.Credentials credentials;

    @Out
    Usuario actual;

    @EJB
    private ICtaUsuario ctausuario;

    Usuario nuevo;

    private String confpassword;

    private String newpassword;

    private String password;

    String nombre_a;

    String apellido_a;

    String ci_a;

    String rol_a;

    String guion_a = "";

    String user_a;

    String nombre_n;

    String apellido_n;

    String ci_n;

    String rol_n;

    String guion_n = "";

    String user_n;

    @Create
    public String nuevo() {
        log.info("RegisterUserAction NUEVO");
        actual = ctausuario.obtenerUsuario(credentials.getUsername());
        if (actual != null) {
            nombre_a = actual.getNombre();
            apellido_a = actual.getApellido();
            ci_a = actual.getCi();
            rol_a = actual.getRol();
        }
        return "ok";
    }

    public String registerUser() {
        if (nombre_n.compareTo("") == 0 || apellido_n.compareTo("") == 0 || ci_n.compareTo("") == 0 || rol_n.compareTo("") == 0 || guion_n.compareTo("") == 0 || user_n.compareTo("") == 0 || confpassword.compareTo("") == 0 || password.compareTo("") == 0) {
            FacesMessages.instance().add("Debe ingresar todos los datos");
            return null;
        }
        try {
            Usuario user = ctausuario.obtenerUsuario(user_n);
            if (user != null) {
                FacesMessages.instance().add("Existe otro usuario registrado como " + nombre_n + ", ingrese otro nombre");
                return null;
            }
            if (confpassword.compareTo(password) != 0) {
                FacesMessages.instance().add("Confirmaci�n de Password incorrecta");
                return null;
            }
            VerificadorCedula vci = VerificadorCedula.getInstance();
            String valci = vci.vCedula(ci_n, guion_n);
            if (valci.compareTo("ok") != 0) {
                FacesMessages.instance().add(valci);
                return null;
            }
            nuevo = new Usuario();
            nuevo.setApellido(apellido_n);
            nuevo.setCi(ci_n);
            nuevo.setGuion(guion_n);
            nuevo.setPassword(password);
            nuevo.setNombre(nombre_n);
            nuevo.setUser(user_n);
            nuevo.setRol(rol_n);
            ctausuario.persistirUsuario(nuevo);
            FacesMessages.instance().add("Se ha ingresado el nuevo Usuario exitosamente");
            clear();
            return "ok";
        } catch (LDAPException e) {
            e.printStackTrace();
            FacesMessages.instance().add("Problemas de comunicaci�n con Base de Usuarios LDAP");
            return null;
        }
    }

    public String modificarPass() {
        if (newpassword.compareTo("") == 0 || confpassword.compareTo("") == 0 || password.compareTo("") == 0) {
            FacesMessages.instance().add("Debe ingresar todos los datos");
            return null;
        }
        if (actual.getPassword().compareTo(password) != 0) {
            FacesMessages.instance().add("Password incorrecto");
            return null;
        }
        if (newpassword.compareTo(confpassword) != 0) {
            FacesMessages.instance().add("Confirmaci�n de Nuevo Password incorrecta");
            return null;
        }
        try {
            ctausuario.modificarPass(actual.getUser(), newpassword);
            FacesMessages.instance().add("Se ha modificado exitosamente su password");
            clear();
            return "ok";
        } catch (LDAPException e) {
            e.printStackTrace();
            FacesMessages.instance().add("Problemas de comunicaci�n con Base de Usuarios LDAP");
            return null;
        }
    }

    public void refrescar() {
    }

    public void clear() {
        newpassword = confpassword = password = "";
        nombre_n = apellido_n = ci_n = rol_n = guion_n = user_n = "";
    }

    @Destroy
    @Remove
    public void destroy() {
        log.info("RegisterUserAction ELIMINADO");
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getConfpassword() {
        return confpassword;
    }

    public void setConfpassword(String confpassword) {
        this.confpassword = confpassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre_a() {
        return nombre_a;
    }

    public void setNombre_a(String nombre_a) {
        this.nombre_a = nombre_a;
    }

    public String getApellido_a() {
        return apellido_a;
    }

    public void setApellido_a(String apellido_a) {
        this.apellido_a = apellido_a;
    }

    public String getCi_a() {
        return ci_a;
    }

    public void setCi_a(String ci_a) {
        this.ci_a = ci_a;
    }

    public String getRol_a() {
        return rol_a;
    }

    public void setRol_a(String rol_a) {
        this.rol_a = rol_a;
    }

    public String getGuion_a() {
        return guion_a;
    }

    public void setGuion_a(String guion_a) {
        this.guion_a = guion_a;
    }

    public String getUser_a() {
        return user_a;
    }

    public void setUser_a(String user_a) {
        this.user_a = user_a;
    }

    public String getNombre_n() {
        return nombre_n;
    }

    public void setNombre_n(String nombre_n) {
        this.nombre_n = nombre_n;
    }

    public String getApellido_n() {
        return apellido_n;
    }

    public void setApellido_n(String apellido_n) {
        this.apellido_n = apellido_n;
    }

    public String getCi_n() {
        return ci_n;
    }

    public void setCi_n(String ci_n) {
        this.ci_n = ci_n;
    }

    public String getRol_n() {
        return rol_n;
    }

    public void setRol_n(String rol_n) {
        this.rol_n = rol_n;
    }

    public String getGuion_n() {
        return guion_n;
    }

    public void setGuion_n(String guion_n) {
        this.guion_n = guion_n;
    }

    public String getUser_n() {
        return user_n;
    }

    public void setUser_n(String user_n) {
        this.user_n = user_n;
    }
}
