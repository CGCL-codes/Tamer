package jhard;

import com.icesoft.faces.component.ext.HtmlOutputLabel;
import com.icesoft.faces.component.ext.HtmlOutputText;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import edu.ues.jhard.beans.BeanBaseJHardmin;
import edu.ues.jhard.beans.BeanBaseJProcur;
import edu.ues.jhard.jhardmin.LoggedUser;
import edu.ues.jhard.jhardmin.LoginManager;
import edu.ues.jhard.jpa.Comentarios;
import edu.ues.jhard.jpa.Entrada;
import edu.ues.jhard.jpa.Usuario;
import edu.ues.jhard.util.popUp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p> Page Bean que corresponde a la pagina JSP de igual nombre.
 * Esta clase contiene la definicion e inicializacion para todos
 * los componentes que se usan en la pagina.
 * Basicamente, aqui esta la logica para la GUI administrativa del mini CMS de jHard.
 * </p>
 */
public class jprocurAdmin extends AbstractPageBean {

    static final String INVITADO = "Invitado";

    static final int MAX_ENTRADAS = 30;

    static final int MAX_COMENTARIOS = 30;

    static final int MAX_ETIQUETAS = 30;

    static final String EMPTY_STRING = new String();

    static final int ROL_EDITORCONTENIDO = 4;

    static final int ROL_ADMINISTRADOR = 1;

    private Entrada entradaActual = null;

    private Entrada entradaNueva = new Entrada();

    private List<Entrada> listaEntradas = new ArrayList<Entrada>();

    private List<Comentarios> listaComentarios = new ArrayList<Comentarios>();

    private Integer tabIndex = new Integer(0);

    private HtmlOutputLabel lblUser = new HtmlOutputLabel();

    private Boolean showPPMesaje = new Boolean(false);

    private HtmlOutputText lblPPMesajes = new HtmlOutputText();

    private popUp popup = new popUp("Aviso", "", false);

    private Boolean addModTag = new Boolean(false);

    public Boolean getAddModTag() {
        return addModTag;
    }

    public void setAddModTag(Boolean addModTag) {
        this.addModTag = addModTag;
    }

    public popUp getPopup() {
        return popup;
    }

    public void setPopup(popUp popup) {
        this.popup = popup;
    }

    public HtmlOutputText getLblPPMesajes() {
        return lblPPMesajes;
    }

    public void setLblPPMesajes(HtmlOutputText lblPPMesajes) {
        this.lblPPMesajes = lblPPMesajes;
    }

    public Boolean getShowPPMesaje() {
        return showPPMesaje;
    }

    public void setShowPPMesaje(Boolean showPPMesaje) {
        this.showPPMesaje = showPPMesaje;
    }

    public HtmlOutputLabel getLblUser() {
        return lblUser;
    }

    public void setLblUser(HtmlOutputLabel hol) {
        this.lblUser = hol;
    }

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private LoggedUser lu;

    private Usuario U;

    public LoggedUser getLu() {
        return this.lu;
    }

    public Usuario getUser() {
        return this.U;
    }

    public String getCurrentUserName() {
        if (this.lu == null) return INVITADO;
        if (this.U == null) return INVITADO;
        this.lu = getJHardminInstance().getCurrentUser();
        if (this.lu == null) {
            this.U = null;
            return INVITADO;
        } else {
            this.U = LoginManager.getInstance().getUsuario(this.lu);
        }
        return this.U.getNombre();
    }

    public BeanBaseJHardmin getJHardminInstance() {
        return (BeanBaseJHardmin) getBean("JHardminInstance");
    }

    private BeanBaseJProcur jprocurInstance = new BeanBaseJProcur();

    public BeanBaseJProcur getJProcurInstance() {
        return this.jprocurInstance;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public jprocurAdmin() {
        this.lu = getJHardminInstance().getCurrentUser();
        this.listaEntradas = this.getJProcurInstance().getAllEntradas();
        this.listaComentarios = this.getJProcurInstance().getComentariosNoAprobados();
        if (this.listaEntradas.size() > 0) this.entradaActual = this.listaEntradas.get(0);
        if (this.lu != null) {
            this.U = LoginManager.getInstance().getUsuario(lu);
            this.lblUser.setValue(U.getNombre());
            switch(this.U.getIdrol().getIdrol()) {
                case ROL_ADMINISTRADOR:
                case ROL_EDITORCONTENIDO:
                default:
                    break;
            }
        } else this.lblUser.setValue(INVITADO);
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
            log("jrequestUser Initialization Failure", e);
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

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    public Entrada getEntradaNueva() {
        return entradaNueva;
    }

    public void setEntradaNueva(Entrada entradaNueva) {
        this.entradaNueva = entradaNueva;
    }

    public List<Comentarios> getListaComentarios() {
        return listaComentarios;
    }

    public void setListaComentarios(List<Comentarios> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    private Boolean editandoEntrada = new Boolean(false);

    public List<Entrada> getListaEntradas() {
        return listaEntradas;
    }

    public void setListaEntradas(List<Entrada> listaEntradas) {
        this.listaEntradas = listaEntradas;
    }

    /**
     * Metodo para saber si se ve una o varias Entradas
     * @param varias
     */
    public Boolean getEditandoEntrada() {
        return this.editandoEntrada;
    }

    /**
     * Metodo para establecer si se ve una o varias Entradas
     * @param varias
     */
    public void setEditandoEntrada(Boolean editandoentrada) {
        this.editandoEntrada = editandoentrada;
    }

    /**
     * Metodo para obtener una entrada por su ID
     * @param identrada id de la entada que se desea
     * @return
     */
    public Entrada getEntradaActual() {
        return this.entradaActual;
    }

    /**
     * Metodo para obtener una entrada por su ID
     * @param identrada id de la entada que se desea
     * @return
     */
    public String elegirEntradaActual() {
        String idSeleccionado = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("idSeleccionado");
        Integer id = new Integer(idSeleccionado);
        this.entradaActual = this.jprocurInstance.getEntrada(id.intValue());
        this.setEditandoEntrada(true);
        return EMPTY_STRING;
    }

    public boolean getShowPagEntradas() {
        if (this.listaEntradas.size() > MAX_ENTRADAS) return true;
        return false;
    }

    public String EliminarEntrada() {
        this.setPopupElimEntrada(true);
        this.lblMensajesEntrada.setValue("¿Seguro que desea Eliminar la entrada seleccionada?");
        String idEntrada = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idEntrada");
        Integer id = Integer.parseInt(idEntrada);
        if (id > 0) this.idEntradaEliminar = id;
        return EMPTY_STRING;
    }

    public String AprobarComentario() {
        this.setPopupComentario(true);
        this.setAprobarComentario(true);
        this.setEliminarComentario(false);
        this.lblMensajesComentarios.setValue("¿Seguro que desea Aprobar el comentario seleccionado?");
        String idComent = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idComentario");
        Integer id = Integer.parseInt(idComent);
        if (id > 0) this.idComentario = id;
        return EMPTY_STRING;
    }

    public String EliminarComentario() {
        this.setPopupComentario(true);
        this.setAprobarComentario(false);
        this.setEliminarComentario(true);
        this.lblMensajesComentarios.setValue("¿Seguro que desea Eliminar el comentario seleccionado?");
        String idComent = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idComentario");
        Integer id = Integer.parseInt(idComent);
        if (id > 0) this.idComentario = id;
        return EMPTY_STRING;
    }

    public String btnEliminarEntrada_action() {
        try {
            if (this.idEntradaEliminar > 0) {
                this.jprocurInstance.deleteEntrada(this.idEntradaEliminar);
                this.listaEntradas = this.jprocurInstance.getAllEntradas();
                this.lblMensajesEntrada.setValue("Entrada eliminada con éxito.");
            }
        } catch (Exception e) {
            this.lblMensajesEntrada.setValue("Ocurrió un error al intentar borrar la Entrada.");
        } finally {
            this.idEntradaEliminar = -1;
        }
        return EMPTY_STRING;
    }

    public String btnAprobarComentario_action() {
        this.setAprobarComentario(false);
        this.setEliminarComentario(false);
        try {
            if (this.idComentario > 0) {
                this.jprocurInstance.aprobarComentario(this.idComentario);
                this.listaComentarios = this.jprocurInstance.getComentariosNoAprobados();
                this.lblMensajesComentarios.setValue("El comentario fué aprobado.");
            }
        } catch (Exception e) {
            this.lblMensajesComentarios.setValue("Ocurrió un error al intentar moderar el comentario.");
        } finally {
            this.idComentario = -1;
        }
        return EMPTY_STRING;
    }

    public String btnEliminarComentario_action() {
        this.setAprobarComentario(false);
        this.setEliminarComentario(false);
        try {
            if (this.idComentario > 0) {
                this.jprocurInstance.deleteComentario(this.idComentario);
                this.listaComentarios = this.jprocurInstance.getComentariosNoAprobados();
                this.lblMensajesComentarios.setValue("El comentario fué aprobado.");
            }
        } catch (Exception e) {
            this.lblMensajesComentarios.setValue("Ocurrió un error al intentar moderar el comentario.");
        } finally {
            this.idComentario = -1;
        }
        return EMPTY_STRING;
    }

    private HtmlOutputText lblMensajesEntrada = new HtmlOutputText();

    private HtmlOutputText lblMensajesComentarios = new HtmlOutputText();

    public HtmlOutputText getLblMensajesComentarios() {
        return lblMensajesComentarios;
    }

    public void setLblMensajesComentarios(HtmlOutputText lblMensajesComentarios) {
        this.lblMensajesComentarios = lblMensajesComentarios;
    }

    public HtmlOutputText getLblMensajesEntrada() {
        return lblMensajesEntrada;
    }

    public void setLblMensajesEntrada(HtmlOutputText hot) {
        this.lblMensajesEntrada = hot;
    }

    private Boolean popupElimEntrada = new Boolean(false);

    private Boolean popupComentario = new Boolean(false);

    private Boolean aprobarComentario = new Boolean(false);

    private Boolean eliminarComentario = new Boolean(false);

    private Boolean agregandoEtiqueta = new Boolean(false);

    public Boolean getAgregandoEtiqueta() {
        return agregandoEtiqueta;
    }

    public void setAgregandoEtiqueta(Boolean agregandoEtiqueta) {
        this.agregandoEtiqueta = agregandoEtiqueta;
    }

    public Boolean getEliminarComentario() {
        return eliminarComentario;
    }

    public void setEliminarComentario(Boolean eliminarComentario) {
        this.eliminarComentario = eliminarComentario;
    }

    public Boolean getAprobarComentario() {
        return aprobarComentario;
    }

    public void setAprobarComentario(Boolean aprobarComentario) {
        this.aprobarComentario = aprobarComentario;
    }

    public Boolean getPopupComentario() {
        return popupComentario;
    }

    public void setPopupComentario(Boolean popupComentario) {
        this.popupComentario = popupComentario;
    }

    public Boolean getPopupElimEntrada() {
        return popupElimEntrada;
    }

    public void setPopupElimEntrada(Boolean popupElimEntrada) {
        this.popupElimEntrada = popupElimEntrada;
    }

    public String btnAceptarElimEntr_action() {
        return this.btnEliminarEntrada_action();
    }

    public String btnCancelarMensajes_action() {
        this.setPopupElimEntrada(false);
        this.setPopupComentario(false);
        this.idEntradaEliminar = -1;
        this.idComentario = -1;
        return EMPTY_STRING;
    }

    private Integer idEntradaEliminar = new Integer(-1);

    private Integer idComentario = new Integer(-1);

    public Integer getIdComentarioAprobar() {
        return idComentario;
    }

    public void setIdComentarioAprobar(Integer idComentarioAprobar) {
        this.idComentario = idComentarioAprobar;
    }

    public boolean getEntradaValida() {
        return (this.idEntradaEliminar > 0);
    }

    public boolean getComentarioValido() {
        return (this.idComentario > 0);
    }

    public boolean getShowPagComentarios() {
        if (this.listaComentarios.size() > MAX_COMENTARIOS) return true;
        return false;
    }

    public Boolean getHayEntradas() {
        if (this.listaEntradas.size() > 0) return true;
        return false;
    }

    public Boolean getHayComentarios() {
        if (this.listaComentarios.size() > 0) return true;
        return false;
    }

    public String EditarEntrada() {
        String idComent = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idEntrada");
        Integer id = Integer.parseInt(idComent);
        this.entradaActual = this.jprocurInstance.getEntrada(id.intValue());
        this.setEditandoEntrada(true);
        return EMPTY_STRING;
    }

    public String cancelarGuardarEntrada() {
        this.entradaActual = null;
        this.entradaNueva = new Entrada();
        this.setEditandoEntrada(false);
        this.tabIndex = 0;
        return EMPTY_STRING;
    }

    public String modificarEntrada() {
        this.entradaActual.setIdusuario(this.U);
        if (this.jprocurInstance.updateEntrada(this.entradaActual) != null) {
            this.lblPPMesajes.setValue("Se modificó la Entrada.");
            this.showPPMesaje = true;
            this.setEditandoEntrada(false);
        } else {
            this.lblPPMesajes.setValue("Ocurrió un problema al modificar la Entrada.");
            this.showPPMesaje = true;
            this.listaEntradas.clear();
            this.listaEntradas = this.getJProcurInstance().getAllEntradas();
        }
        this.tabIndex = 0;
        this.entradaActual = new Entrada();
        return EMPTY_STRING;
    }

    public String agregarEntrada() {
        this.entradaNueva.setIdusuario(this.U);
        this.entradaNueva.setFechahora(new Date());
        if (this.jprocurInstance.createEntrada(this.entradaNueva)) {
            this.lblPPMesajes.setValue("Entrada agregada con éxito.");
            this.showPPMesaje = true;
            this.listaEntradas.clear();
            this.listaEntradas = this.getJProcurInstance().getAllEntradas();
            this.entradaNueva = new Entrada();
        } else {
            this.lblPPMesajes.setValue("Ocurrió un error al intentar agregar una Entrada.");
            this.showPPMesaje = true;
        }
        this.setEditandoEntrada(false);
        this.entradaNueva = new Entrada();
        return EMPTY_STRING;
    }

    public TimeZone getTimeZone() {
        return java.util.TimeZone.getDefault();
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public void setTabIndex(String tabIndex) {
        this.tabIndex = Integer.parseInt(tabIndex);
    }

    public String btnOK_action() {
        this.tabIndex = 0;
        this.idEntradaEliminar = -1;
        this.idComentario = -1;
        this.showPPMesaje = false;
        return EMPTY_STRING;
    }

    public String btnClose_action() {
        this.popup.setVisible(false);
        return EMPTY_STRING;
    }

    public Integer getRolUsuarioConectado() {
        if (this.U == null) return -1;
        return this.U.getIdrol().getIdrol();
    }

    public boolean getPermisos() {
        switch(this.getRolUsuarioConectado()) {
            case -1:
                return false;
            case ROL_ADMINISTRADOR:
            case ROL_EDITORCONTENIDO:
                return true;
        }
        return false;
    }
}
