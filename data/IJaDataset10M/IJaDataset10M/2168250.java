package foroweb.controllers;

import foroweb.ejb.*;
import foroweb.entidades.*;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.*;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.component.fileupload.FileUpload;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author instalador
 */
@ManagedBean
@ViewScoped
public class PerfilController implements Serializable {

    @EJB
    private AccionUsuario au;

    @EJB
    private Busquedas buscar;

    @EJB
    private AccionPrivado ap;

    private Usuario usuario;

    private String nick;

    public List<Privado> privados;

    public List<Privado> privadosEnv;

    public Privado selectedPrivado;

    public Privado privado;

    private Usuario usuario2;

    private Usuario usuario3;

    private String password1;

    private String password2;

    private Map<Long, Boolean> selectedId = new HashMap<Long, Boolean>();

    private Map<Long, Boolean> selectedIdEnv = new HashMap<Long, Boolean>();

    private List<Privado> selectedPrivados;

    private List<Privado> selectedPrivadosEnv;

    private static Map<String, Object> paises;

    private String charEncondig;

    public PerfilController() {
        privado = new Privado();
        usuario = new Usuario();
        usuario2 = new Usuario();
        password1 = new String();
        password2 = new String();
    }

    public Map<String, Object> getPaises() {
        return paises;
    }

    static {
        paises = new TreeMap<String, Object>();
        Locale[] locales = Locale.getAvailableLocales();
        int i = 1;
        for (Locale locale : locales) {
            String name = locale.getDisplayCountry();
            if (!"".equals(name)) {
                paises.put(name, name);
            }
            i++;
        }
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getSelectedPrivados() {
        Privado privado2;
        selectedPrivados = new ArrayList<Privado>();
        Iterator i = privados.iterator();
        while (i.hasNext()) {
            privado2 = (Privado) i.next();
            if (selectedId.containsKey(privado2.getId())) {
                if (selectedId.get(privado2.getId()).booleanValue()) {
                    selectedPrivados.add(privado2);
                    selectedId.remove(privado2.getId());
                }
            }
        }
        for (Privado selectedMp : selectedPrivados) {
            selectedMp.setBd(true);
            ap.modificar(selectedMp);
        }
        return "miPerfil";
    }

    public String getSelectedPrivadosEnv() {
        Privado privado2;
        selectedPrivadosEnv = new ArrayList<Privado>();
        Iterator i = privadosEnv.iterator();
        while (i.hasNext()) {
            privado2 = (Privado) i.next();
            if (selectedIdEnv.containsKey(privado2.getId())) {
                if (selectedIdEnv.get(privado2.getId()).booleanValue()) {
                    selectedPrivadosEnv.add(privado2);
                    selectedIdEnv.remove(privado2.getId());
                }
            }
        }
        for (Privado selectedMp : selectedPrivadosEnv) {
            selectedMp.setBr(true);
            ap.modificar(selectedMp);
        }
        return "miPerfil";
    }

    public Map<Long, Boolean> getSelectedIdEnv() {
        return selectedIdEnv;
    }

    public Map<Long, Boolean> getSelectedId() {
        return selectedId;
    }

    public Usuario getUsuario3() {
        return usuario3;
    }

    public void setUsuario3(Usuario usuario3) {
        this.usuario3 = usuario3;
    }

    public List<Privado> getPrivadosEnv() {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        privadosEnv = au.obtenerPrivadosEnviados(u.getUsername());
        return privadosEnv;
    }

    public void setPrivadosEnv(List<Privado> privadosEnv) {
        this.privadosEnv = privadosEnv;
    }

    public Privado getSelectedPrivado() {
        return selectedPrivado;
    }

    public void setSelectedPrivado(Privado selectedPrivado) {
        this.selectedPrivado = selectedPrivado;
    }

    public Usuario getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(Usuario usuario2) {
        this.usuario2 = usuario2;
    }

    public Usuario getUsuario() {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("opps");
        usuario = buscar.buscarUsuario(u.getUsername());
        if (usuario.getPerfil().getFnac() != null) {
            Date d = usuario.getPerfil().getFnac();
            Calendar c = new GregorianCalendar();
            c.setTimeInMillis(d.getTime());
            c.add(Calendar.DATE, 1);
            d = new java.util.Date(c.getTimeInMillis());
            usuario.getPerfil().setFnac(d);
        }
        usuario2 = buscar.buscarUsuario(u.getUsername());
        return usuario;
    }

    public String getNick() {
        return nick;
    }

    public void setPrivados(List<Privado> privados) {
        this.privados = privados;
    }

    public List<Privado> getPrivados() {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        privados = au.obtenerPrivadosRecibidos(u.getUsername());
        if (privados.size() > 0) {
            for (Privado p : privados) {
                if (!p.isLeido()) {
                    p.setLeido(true);
                    ap.modificar(p);
                }
            }
        }
        return privados;
    }

    public void setNick(String nick) {
        this.nick = nick;
        if (nick != null && usuario3 == null) {
            usuario3 = buscar.buscarUsuario(nick);
        }
    }

    public Privado getPrivado() {
        return privado;
    }

    public void setPrivado(Privado privado) {
        this.privado = privado;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String modificarPerfil() {
        String mail = buscar.buscarUsuario(usuario.getNick()).getEmail();
        String pass = usuario2.getPassword();
        if (usuario2.getRuta().isEmpty()) {
            usuario2.setRuta("No definido");
        }
        if (!password1.isEmpty() || !password2.isEmpty()) {
            if (password1.isEmpty() || password2.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Un campo de contrase�a no se ha introducido"));
            }
        }
        if (password1 != null && password2 != null && !password1.isEmpty() && !password2.isEmpty()) {
            if (password1.equals(password2)) {
                if (!password1.isEmpty()) {
                    if (password1.length() < 8) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error, la contraseña 1 ha de tener minimo 8 caracteres"));
                        usuario.setPassword(pass);
                    } else {
                        usuario2.setPassword(password1);
                    }
                }
                if (!password2.isEmpty()) {
                    if (password2.length() < 8) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error, la contraseña 2 ha de tener minimo 8 caracteres"));
                        usuario.setPassword(pass);
                    } else {
                        usuario2.setPassword(password1);
                    }
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error, las contrasenias no coinciden"));
                usuario2.setPassword(pass);
            }
        } else {
            usuario2.setPassword(pass);
        }
        for (Usuario usuarios : buscar.buscarUsuarios("")) {
            if (usuarios.getEmail().equals(usuario2.getEmail()) && !usuarios.getNick().equals(usuario2.getNick())) {
                usuario2.setEmail(mail);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error, el e-mail ya existe"));
            }
        }
        au.modificar(usuario2);
        return "miPerfil.xhtml";
    }

    public String modificarPerfil2() {
        au.modificar(usuario2);
        return "listUsuarios.xhtml";
    }

    public void subirImagen(FileUpload event) {
    }

    public void responderPrivado2() {
        System.out.println("HOHOHO");
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        privado.setRemitente(buscar.buscarUsuario(u.getUsername()));
        privado.setDestinatario(buscar.buscarUsuario(nick));
        ExternalContext extC = FacesContext.getCurrentInstance().getExternalContext();
        String charEncondig = extC.getRequestCharacterEncoding();
        ap.enviar(privado);
        try {
            String name = URLEncoder.encode("nick", charEncondig);
            String value = URLEncoder.encode("" + nick, charEncondig);
            String viewId = extC.getRequestContextPath() + "/perfil.faces?" + name + "=" + value;
            String urlLink = extC.encodeActionURL(viewId);
            extC.redirect(urlLink);
        } catch (IOException ex) {
            System.out.println("Error");
        }
    }

    public void responderPrivado() {
        User u = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        privado.setRemitente(buscar.buscarUsuario(u.getUsername()));
        if (selectedPrivado.getRemitente().getNick().equals(u.getUsername())) {
            privado.setDestinatario(selectedPrivado.getDestinatario());
        } else {
            privado.setDestinatario(selectedPrivado.getRemitente());
        }
        if (privado.getTexto().length() < 2) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error, no se pudo enviar mensaje", u.getUsername() + ", el mensaje no se ha enviado"));
        } else {
            ap.enviar(privado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(u.getUsername() + ", el mensaje ha sido enviado a  " + selectedPrivado.getRemitente().getNick()));
            try {
                ExternalContext extC = FacesContext.getCurrentInstance().getExternalContext();
                extC.redirect(extC.getRequestContextPath() + "/miPerfil.faces");
            } catch (IOException ex) {
                Logger.getLogger(NuevoMensajeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
