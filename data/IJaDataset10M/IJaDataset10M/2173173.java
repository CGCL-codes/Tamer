package ExAC.WebJSF;

import com.sun.data.provider.RowKey;
import com.sun.data.provider.impl.CachedRowSetDataProvider;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.StaticText;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version Usuarios_CambioDeClave.java
 * @version Created on Dec 8, 2008, 8:01:08 PM
 * @author janch
 */
public class Usuarios_CambioDeClave extends AbstractPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
        usuariosDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.usuariosRowSet}"));
    }

    private Head head1 = new Head();

    public Head getHead1() {
        return head1;
    }

    public void setHead1(Head h) {
        this.head1 = h;
    }

    private Form forma = new Form();

    public Form getForma() {
        return forma;
    }

    public void setForma(Form f) {
        this.forma = f;
    }

    private HtmlInputSecret txtClaveActual = new HtmlInputSecret();

    public HtmlInputSecret getTxtClaveActual() {
        return txtClaveActual;
    }

    public void setTxtClaveActual(HtmlInputSecret his) {
        this.txtClaveActual = his;
    }

    private HtmlInputSecret txtNuevaClave1 = new HtmlInputSecret();

    public HtmlInputSecret getTxtNuevaClave1() {
        return txtNuevaClave1;
    }

    public void setTxtNuevaClave1(HtmlInputSecret his) {
        this.txtNuevaClave1 = his;
    }

    private HtmlInputSecret txtNuevaClave2 = new HtmlInputSecret();

    public HtmlInputSecret getTxtNuevaClave2() {
        return txtNuevaClave2;
    }

    public void setTxtNuevaClave2(HtmlInputSecret his) {
        this.txtNuevaClave2 = his;
    }

    private CachedRowSetDataProvider usuariosDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getUsuariosDataProvider() {
        return usuariosDataProvider;
    }

    public void setUsuariosDataProvider(CachedRowSetDataProvider crsdp) {
        this.usuariosDataProvider = crsdp;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public Usuarios_CambioDeClave() {
    }

    /**
     * <p>Callback method that is called whenever a page is navigated to,
     * either directly via a URL, or indirectly via page navigation.
     * Customize this method to acquire resources that will be needed
     * for event handlers and lifecycle methods, whether or not this
     * page is performing post back processing.</p>
     * 
     * <p>Note that, if the current request is a postback, the property
     * values of the components do <strong>not</strong> represent any
     * values submitted with this request.  Instead, they represent the
     * property values that were saved for this view when it was rendered.</p>
     */
    @Override
    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("Usuarios_CambioDeClave Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
    }

    /**
     * <p>Callback method that is called after the component tree has been
     * restored, but before any event processing takes place.  This method
     * will <strong>only</strong> be called on a postback request that
     * is processing a form submit.  Customize this method to allocate
     * resources that will be required in your event handlers.</p>
     */
    @Override
    public void preprocess() {
    }

    /**
     * <p>Callback method that is called just before rendering takes place.
     * This method will <strong>only</strong> be called for the page that
     * will actually be rendered (and not, for example, on a page that
     * handled a postback and then navigated to a different page).  Customize
     * this method to allocate resources that will be required for rendering
     * this page.</p>
     */
    @Override
    public void prerender() {
        if (!isPostBack()) {
            procesarLlamada();
        }
        establecerTitulo();
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called (regardless of whether
     * or not this was the page that was actually rendered).  Customize this
     * method to release resources acquired in the <code>init()</code>,
     * <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).</p>
     */
    @Override
    public void destroy() {
        usuariosDataProvider.close();
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    private void establecerTitulo() {
        RowKey rk = ubicarRegistroSolicitado();
        if (rk != null) {
            CachedRowSetDataProvider dp = usuariosDataProvider;
            String nombreDeUsuario = dp.getValue("NombreCompleto").toString();
            String titulo = "- Usuarios - Cambio de Clave (" + nombreDeUsuario + " #" + String.valueOf(_getUsuarioAEditarID()) + ")";
            String tituloDePagina = titulo;
            Encabezado encabezadoFrag = (Encabezado) getBean("Encabezado");
            StaticText txtTituloDePagina = encabezadoFrag.getTxtTituloDePagina();
            txtTituloDePagina.setText(tituloDePagina);
            head1.setTitle(tituloDePagina);
        } else {
            error("NO SE ENCONTRÓ EL USUARIO SOLICTADO!");
        }
    }

    private RowKey ubicarRegistroSolicitado() {
        CachedRowSetDataProvider dp = usuariosDataProvider;
        RowKey rk = dp.findFirst("UsuarioID", _getUsuarioAEditarID());
        dp.setCursorRow(rk);
        return rk;
    }

    private void procesarLlamada() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Object id = request.getParameter("id");
        if (id == null) {
            int usuarioAEditarID = getSessionBean1().getUsuarioAEditarID();
            _setUsuarioAEditarID(usuarioAEditarID);
        } else {
            _setUsuarioAEditarID(Integer.parseInt(id.toString()));
        }
    }

    private int _getUsuarioAEditarID() {
        return getSessionBean1().getUsuarioAEditarID();
    }

    private void _setUsuarioAEditarID(int value) {
        getSessionBean1().setUsuarioAEditarID(value);
    }

    public String imgGuardar_action() {
        ubicarRegistroSolicitado();
        String claveActual = usuariosDataProvider.getValue("Clave").toString();
        String strClaveActualPropuesta = txtClaveActual.getValue().toString();
        byte[] ca = strClaveActualPropuesta.getBytes();
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(ca);
            ca = m.digest(ca);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Usuarios_CambioDeClave.class.getName()).log(Level.SEVERE, null, ex);
        }
        strClaveActualPropuesta = new String(ca);
        if (!claveActual.equals(strClaveActualPropuesta)) {
            error("La clave actual suministrada no coincide con la clave del usuario");
            return null;
        }
        if (!txtNuevaClave1.getValue().toString().equals(txtNuevaClave2.getValue().toString())) {
            error("La nueva clave no coincide.");
            return null;
        }
        if (txtNuevaClave1.getValue().toString().isEmpty()) {
            error("La nueva clave es un campo requerido y está vacío...");
            return null;
        }
        usuariosDataProvider.setValue("Clave", txtNuevaClave1.getValue().toString());
        try {
            usuariosDataProvider.commitChanges();
        } catch (Exception ex) {
            error(PatronesDeExcepcion.adaptarMensaje(ex));
            return null;
        }
        getRequestBean1().setMensajeSiguientePagina("La clave fue cambiada satsifactoriamente.");
        return "Guardar";
    }

    public String lnkGuardar_action() {
        return imgGuardar_action();
    }

    public String imgRegresar_action() {
        return "Regresar";
    }

    public String lnkRegresar_action() {
        return "Regresar";
    }
}
