package ExAC.WebJSF;

import com.sun.data.provider.RowKey;
import com.sun.data.provider.impl.CachedRowSetDataProvider;
import com.sun.rave.faces.data.DefaultSelectItemsArray;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Hyperlink;
import com.sun.webui.jsf.component.ImageHyperlink;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.TextField;
import java.util.Calendar;
import java.util.Date;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version Expedientes_AdecuacionesAplicadas_Edicion.java
 * @version Created on Dec 2, 2008, 1:46:17 PM
 * @author janch
 */
public class Expedientes_AdecuacionesAplicadas_Edicion extends AbstractPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
        adecuacionesaplicadasDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.adecuacionesaplicadasRowSet}"));
        vwanoslectivos_seleccionDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.vwanoslectivos_seleccionRowSet}"));
        vwtrimestres_seleccionDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.vwtrimestres_seleccionRowSet}"));
        vwmaterias_seleccionDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.vwmaterias_seleccionRowSet}"));
        vwdocentes_seleccionDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.vwdocentes_seleccionRowSet}"));
        vwtiposdeadecuacion_seleccionDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.vwtiposdeadecuacion_seleccionRowSet}"));
    }

    private Form forma = new Form();

    public Form getForma() {
        return forma;
    }

    public void setForma(Form f) {
        this.forma = f;
    }

    private Head head1 = new Head();

    public Head getHead1() {
        return head1;
    }

    public void setHead1(Head h) {
        this.head1 = h;
    }

    private ImageHyperlink imgGuardar = new ImageHyperlink();

    public ImageHyperlink getImgGuardar() {
        return imgGuardar;
    }

    public void setImgGuardar(ImageHyperlink ih) {
        this.imgGuardar = ih;
    }

    private Hyperlink lnkGuardar = new Hyperlink();

    public Hyperlink getLnkGuardar() {
        return lnkGuardar;
    }

    public void setLnkGuardar(Hyperlink h) {
        this.lnkGuardar = h;
    }

    private ImageHyperlink imgGuardarYNuevo = new ImageHyperlink();

    public ImageHyperlink getImgGuardarYNuevo() {
        return imgGuardarYNuevo;
    }

    public void setImgGuardarYNuevo(ImageHyperlink ih) {
        this.imgGuardarYNuevo = ih;
    }

    private Hyperlink lnkGuardarYNuevo = new Hyperlink();

    public Hyperlink getLnkGuardarYNuevo() {
        return lnkGuardarYNuevo;
    }

    public void setLnkGuardarYNuevo(Hyperlink h) {
        this.lnkGuardarYNuevo = h;
    }

    private ImageHyperlink imgRecargar = new ImageHyperlink();

    public ImageHyperlink getImgRecargar() {
        return imgRecargar;
    }

    public void setImgRecargar(ImageHyperlink ih) {
        this.imgRecargar = ih;
    }

    private Hyperlink lnkRecargar = new Hyperlink();

    public Hyperlink getLnkRecargar() {
        return lnkRecargar;
    }

    public void setLnkRecargar(Hyperlink h) {
        this.lnkRecargar = h;
    }

    private ImageHyperlink imgBorrar = new ImageHyperlink();

    public ImageHyperlink getImgBorrar() {
        return imgBorrar;
    }

    public void setImgBorrar(ImageHyperlink ih) {
        this.imgBorrar = ih;
    }

    private Hyperlink lnkBorrar = new Hyperlink();

    public Hyperlink getLnkBorrar() {
        return lnkBorrar;
    }

    public void setLnkBorrar(Hyperlink h) {
        this.lnkBorrar = h;
    }

    private ImageHyperlink imgRegresar = new ImageHyperlink();

    public ImageHyperlink getImgRegresar() {
        return imgRegresar;
    }

    public void setImgRegresar(ImageHyperlink ih) {
        this.imgRegresar = ih;
    }

    private Hyperlink lnkRegresar = new Hyperlink();

    public Hyperlink getLnkRegresar() {
        return lnkRegresar;
    }

    public void setLnkRegresar(Hyperlink h) {
        this.lnkRegresar = h;
    }

    private DefaultSelectItemsArray dropdown1DefaultItems1 = new DefaultSelectItemsArray();

    public DefaultSelectItemsArray getDropdown1DefaultItems1() {
        return dropdown1DefaultItems1;
    }

    public void setDropdown1DefaultItems1(DefaultSelectItemsArray dsia) {
        this.dropdown1DefaultItems1 = dsia;
    }

    private DefaultSelectItemsArray dropdown1DefaultItems2 = new DefaultSelectItemsArray();

    public DefaultSelectItemsArray getDropdown1DefaultItems2() {
        return dropdown1DefaultItems2;
    }

    public void setDropdown1DefaultItems2(DefaultSelectItemsArray dsia) {
        this.dropdown1DefaultItems2 = dsia;
    }

    private DefaultSelectItemsArray dropdown1DefaultItems3 = new DefaultSelectItemsArray();

    public DefaultSelectItemsArray getDropdown1DefaultItems3() {
        return dropdown1DefaultItems3;
    }

    public void setDropdown1DefaultItems3(DefaultSelectItemsArray dsia) {
        this.dropdown1DefaultItems3 = dsia;
    }

    private CachedRowSetDataProvider adecuacionesaplicadasDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getAdecuacionesaplicadasDataProvider() {
        return adecuacionesaplicadasDataProvider;
    }

    public void setAdecuacionesaplicadasDataProvider(CachedRowSetDataProvider crsdp) {
        this.adecuacionesaplicadasDataProvider = crsdp;
    }

    private CachedRowSetDataProvider vwanoslectivos_seleccionDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getVwanoslectivos_seleccionDataProvider() {
        return vwanoslectivos_seleccionDataProvider;
    }

    public void setVwanoslectivos_seleccionDataProvider(CachedRowSetDataProvider crsdp) {
        this.vwanoslectivos_seleccionDataProvider = crsdp;
    }

    private CachedRowSetDataProvider vwtrimestres_seleccionDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getVwtrimestres_seleccionDataProvider() {
        return vwtrimestres_seleccionDataProvider;
    }

    public void setVwtrimestres_seleccionDataProvider(CachedRowSetDataProvider crsdp) {
        this.vwtrimestres_seleccionDataProvider = crsdp;
    }

    private CachedRowSetDataProvider vwmaterias_seleccionDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getVwmaterias_seleccionDataProvider() {
        return vwmaterias_seleccionDataProvider;
    }

    public void setVwmaterias_seleccionDataProvider(CachedRowSetDataProvider crsdp) {
        this.vwmaterias_seleccionDataProvider = crsdp;
    }

    private CachedRowSetDataProvider vwdocentes_seleccionDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getVwdocentes_seleccionDataProvider() {
        return vwdocentes_seleccionDataProvider;
    }

    public void setVwdocentes_seleccionDataProvider(CachedRowSetDataProvider crsdp) {
        this.vwdocentes_seleccionDataProvider = crsdp;
    }

    private CachedRowSetDataProvider vwtiposdeadecuacion_seleccionDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getVwtiposdeadecuacion_seleccionDataProvider() {
        return vwtiposdeadecuacion_seleccionDataProvider;
    }

    public void setVwtiposdeadecuacion_seleccionDataProvider(CachedRowSetDataProvider crsdp) {
        this.vwtiposdeadecuacion_seleccionDataProvider = crsdp;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public Expedientes_AdecuacionesAplicadas_Edicion() {
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
            log("Expedientes_AdecuacionesAplicadas_Edicion Initialization Failure", e);
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
        ubicarRegistroSolicitado();
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
        cargarListasDesplegables();
        establecerTitulo();
        if (!isPostBack()) {
            adquirirSolicitud();
            procesarMensajes();
            establecerMensajesDeValidacion();
            cargarPagina();
        } else {
            if (getSessionBean1().retrieveData("ClearForm") != null) {
                getSessionBean1().saveData("ClearForm", null);
                cargarPagina();
            }
        }
        if (!isPostBack()) {
            ubicarRegistroSolicitado();
        }
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
        adecuacionesaplicadasDataProvider.close();
        vwanoslectivos_seleccionDataProvider.close();
        vwtrimestres_seleccionDataProvider.close();
        vwmaterias_seleccionDataProvider.close();
        vwdocentes_seleccionDataProvider.close();
        vwtiposdeadecuacion_seleccionDataProvider.close();
    }

    private void activarConfirmacionDeCancelacion(boolean activar) {
        if (activar) {
            warn("Por favor haga clic en [Confirmar Cancelación] para cancelar los cambios que no han sido almacenados");
            lnkRecargar.setText("Confirmar Cancelación");
            lnkRecargar.setStyle("border-color:red; border-width:3px; border-style:solid");
        } else {
            lnkRecargar.setText("Deshacer");
            lnkRecargar.setStyle("");
        }
    }

    private void activarConfirmacionDeEliminacion(boolean activar) {
        if (activar) {
            warn("Por favor haga clic en [Confirmar Eliminación] para eliminar este registro.");
            lnkBorrar.setText("Confirmar Eliminación");
            lnkBorrar.setStyle("border-color:red; border-width:3px; border-style:solid");
        } else {
            lnkBorrar.setText("Eliminar");
            lnkBorrar.setStyle("");
        }
    }

    private void adquirirSolicitud() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Object id = request.getParameter("id");
        if (id != null) {
            try {
                int intID = Integer.parseInt(id.toString());
                _setAdecuacionAplicadaID(intID);
            } catch (Exception ex) {
                error(PatronesDeExcepcion.adaptarMensaje(ex));
            }
        }
    }

    private void cargarPagina() {
        try {
            try {
                forma.discardSubmittedValues("guardar");
            } catch (Exception ex) {
            }
            if (_getAdecuacionAplicadaID() == 0) {
                lnkBorrar.setVisible(false);
                imgBorrar.setVisible(false);
                cargarRegistroVacioEnPagina();
            } else {
                cargarRegistroSolicitadoEnPagina();
            }
            activarConfirmacionDeCancelacion(false);
            activarConfirmacionDeEliminacion(false);
        } catch (Exception ex) {
            error(PatronesDeExcepcion.adaptarMensaje(ex));
        }
    }

    private void cargarRegistroVacioEnPagina() {
        adecuacionesaplicadasDataProvider.revertChanges();
        adecuacionesaplicadasDataProvider.refresh();
        RowKey rk = adecuacionesaplicadasDataProvider.appendRow();
        adecuacionesaplicadasDataProvider.setCursorRow(rk);
    }

    private RowKey cargarRegistroSolicitadoEnPagina() {
        CachedRowSetDataProvider dp = adecuacionesaplicadasDataProvider;
        dp.revertChanges();
        dp.refresh();
        return ubicarCursorEnRegistroAEditar();
    }

    private void establecerMensajesDeValidacion() {
    }

    private void setErrMSG(TextField component, String label) {
        component.setRequiredMessage("Error - " + label + ": El campo es requerido y está vacío ");
    }

    private void cargarListasDesplegables() {
        cargarListaYAgregarEntradaInicial(vwanoslectivos_seleccionDataProvider, "AnoLectivoID", "AnoLectivo", "-Escoja-");
        cargarListaYAgregarEntradaInicial(vwtrimestres_seleccionDataProvider, "TrimestreID", "Trimestre", "-Escoja-");
        cargarListaYAgregarEntradaInicial(vwmaterias_seleccionDataProvider, "MateriaID", "Materia", "-Escoja-");
        cargarListaYAgregarEntradaInicial(vwdocentes_seleccionDataProvider, "DocenteID", "Docente", "-Escoja-");
        cargarListaYAgregarEntradaInicial(vwtiposdeadecuacion_seleccionDataProvider, "TipoDeAdecuacionID", "TipoDeAdecuacion", "-Escoja-");
    }

    private void cargarListaYAgregarEntradaInicial(CachedRowSetDataProvider dp, String colID, String colTitulo, String titulo) {
        dp.refresh();
        RowKey rk;
        dp.cursorFirst();
        rk = dp.getCursorRow();
        if (dp.canInsertRow(rk)) {
            rk = dp.insertRow(rk);
        } else {
            rk = dp.appendRow();
        }
        dp.setCursorRow(rk);
        dp.setValue(colID, 0);
        dp.setValue(colTitulo, titulo);
    }

    private void eliminarRegistro() {
        RowKey rk = cargarRegistroSolicitadoEnPagina();
        adecuacionesaplicadasDataProvider.removeRow(rk);
        adecuacionesaplicadasDataProvider.commitChanges();
    }

    private void establecerTitulo() {
        String titular = _getTitular();
        String titulo = "- Edición de Adecuaciones Aplicadas (" + titular + ") [EXP#" + String.valueOf(_getExpedienteID()) + "]";
        String tituloDePagina = titulo;
        Encabezado encabezadoFrag = (Encabezado) getBean("Encabezado");
        StaticText txtTituloDePagina = encabezadoFrag.getTxtTituloDePagina();
        txtTituloDePagina.setText(tituloDePagina);
        head1.setTitle(tituloDePagina);
    }

    private void procesarMensajes() {
        String mensajePaginaAnterior = getRequestBean1().getMensajeSiguientePagina();
        if (mensajePaginaAnterior.isEmpty()) {
            if (!getSessionBean1().isMensajeOrientacionMostrado_Instituciones_Edicion()) {
                getSessionBean1().setMensajeOrientacionMostrado_Instituciones_Edicion(true);
                if (_getAdecuacionAplicadaID() == 0) {
                    info("Modo Inclusión: Por favor suministre los datos necesarios y haga clic en [Guardar Cambios] para incluir un nuevo registro en la base de datos. Puede utilizar el botón [Cancelar Cambios] para descartar los cambios que no haya guardado y limpiar el formulario.");
                } else {
                    info("Modo Edición: Por favor edite los datos y haga clic en [Guardar Cambios] para almacenar los cambios en la base de datos. También puede hacer clic en [Borrar] para eliminar este registro. Puede utilizar el botón [Cancelar Cambios] para descartar los cambios que no haya guardado y retomar los valores directamente de la base de datos.");
                }
            }
        } else {
            info(mensajePaginaAnterior);
        }
        String mensajeContextual = "<p>Utilice esta pantalla para realizar cambios al registro de Adecuaciones Aplicadas, para agregar nuevas entradas, o para eliminar entradas.</p>";
        ContenidoContextual ccFragmentBean = (ContenidoContextual) getBean("ContenidoContextual");
        StaticText myText = ccFragmentBean.getStaticText2();
        myText.setText(mensajeContextual);
    }

    private RowKey ubicarCursorEnRegistroAEditar() {
        RowKey rk = adecuacionesaplicadasDataProvider.findFirst("AdecuacionAplicadaID", _getAdecuacionAplicadaID());
        adecuacionesaplicadasDataProvider.setCursorRow(rk);
        return rk;
    }

    private void ubicarRegistroSolicitado() {
        String cmd = "SELECT * FROM adecuacionesaplicadas WHERE AdecuacionAplicadaID = " + String.valueOf(_getAdecuacionAplicadaID());
        Date hoy = Calendar.getInstance().getTime();
        String por = "ExAC";
        try {
            adecuacionesaplicadasDataProvider.getCachedRowSet().setCommand(cmd);
            adecuacionesaplicadasDataProvider.refresh();
            if (adecuacionesaplicadasDataProvider.getRowCount() == 0) {
                RowKey rk = adecuacionesaplicadasDataProvider.appendRow();
                adecuacionesaplicadasDataProvider.setCursorRow(rk);
                adecuacionesaplicadasDataProvider.setValue("Creado_Fecha", hoy);
                adecuacionesaplicadasDataProvider.setValue("Creado_Por", por);
                adecuacionesaplicadasDataProvider.setValue("ExpedienteID", _getExpedienteID());
            }
            adecuacionesaplicadasDataProvider.setValue("Modificado_Fecha", hoy);
            adecuacionesaplicadasDataProvider.setValue("Modificado_Por", por);
        } catch (Exception ex) {
            error(PatronesDeExcepcion.adaptarMensaje(ex));
        }
    }

    private void guardarCambios() {
        adecuacionesaplicadasDataProvider.commitChanges();
        adecuacionesaplicadasDataProvider.refresh();
    }

    private void ddlValidation(FacesContext context, UIComponent component, Object value, String ddlName) {
        if (value != null) {
            if (!value.toString().equals("0")) {
                return;
            }
        }
        error(Ayudador_Validaciones.ddlObtenerMensajeDeError(ddlName));
        Ayudador_Validaciones.ddlFailValidation(ddlName, component, context);
    }

    private String _getTitular() {
        return getSessionBean1().getTitular();
    }

    private int _getExpedienteID() {
        return getSessionBean1().getExpedienteID();
    }

    private int _getAdecuacionAplicadaID() {
        return getSessionBean1().getAdecuacionAplicadaID();
    }

    private void _setAdecuacionAplicadaID(int id) {
        getSessionBean1().setAdecuacionAplicadaID(id);
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

    public String imgGuardar_action() {
        try {
            guardarCambios();
            getRequestBean1().setMensajeSiguientePagina("Los datos se guardaron satisfactoriamente.");
        } catch (Exception ex) {
            error(PatronesDeExcepcion.adaptarMensaje(ex));
            return null;
        }
        return "Guardar";
    }

    public String lnkGuardar_action() {
        return imgGuardar_action();
    }

    public String imgGuardarYNuevo_action() {
        try {
            guardarCambios();
            _setAdecuacionAplicadaID(0);
            ubicarRegistroSolicitado();
            info("Los datos se guardaron satisfactoriamente. Se limpió el formulario para ingresar un nuevo registro.");
        } catch (Exception ex) {
            error(PatronesDeExcepcion.adaptarMensaje(ex));
            return null;
        }
        return null;
    }

    public String lnkGuardarYNuevo_action() {
        return imgGuardarYNuevo_action();
    }

    public String imgRecargar_action() {
        if (lnkRecargar.getText().equals("Deshacer")) {
            activarConfirmacionDeCancelacion(true);
            activarConfirmacionDeEliminacion(false);
        } else {
            cargarPagina();
            getSessionBean1().saveData("ClearForm", true);
            activarConfirmacionDeCancelacion(false);
            activarConfirmacionDeEliminacion(false);
            info("Los cambios se cancelaron y se recargaron los datos de la base de datos.");
        }
        return null;
    }

    public String lnkRecargar_action() {
        return imgRecargar_action();
    }

    public String imgBorrar_action() {
        if (lnkBorrar.getText().equals("Eliminar")) {
            activarConfirmacionDeEliminacion(true);
            activarConfirmacionDeCancelacion(false);
            return null;
        } else {
            eliminarRegistro();
            activarConfirmacionDeEliminacion(false);
            activarConfirmacionDeCancelacion(false);
            getRequestBean1().setMensajeSiguientePagina("El registro se eliminó satisfactoriamente");
        }
        return "Eliminar";
    }

    public String lnkBorrar_action() {
        return imgBorrar_action();
    }

    public String imgRegresar_action() {
        return "Regresar";
    }

    public String lnkRegresar_action() {
        return "Regresar";
    }

    public void ddlAnoLectivo_validate(FacesContext context, UIComponent component, Object value) {
        ddlValidation(context, component, value, "Año Lectivo");
    }

    public void ddlTrimestre_validate(FacesContext context, UIComponent component, Object value) {
        ddlValidation(context, component, value, "Trimestre");
    }

    public void ddlMateria_validate(FacesContext context, UIComponent component, Object value) {
        ddlValidation(context, component, value, "Materia");
    }

    public void ddlDocente_validate(FacesContext context, UIComponent component, Object value) {
        ddlValidation(context, component, value, "Docente Encargado");
    }

    public void ddlAdecuacionCurricular_validate(FacesContext context, UIComponent component, Object value) {
        ddlValidation(context, component, value, "Tipo de Adecuación");
    }
}
