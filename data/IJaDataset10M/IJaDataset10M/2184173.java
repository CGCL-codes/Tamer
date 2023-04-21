package jhard;

import com.icesoft.faces.component.ext.HtmlCommandButton;
import com.icesoft.faces.component.ext.HtmlInputTextarea;
import com.icesoft.faces.component.ext.HtmlOutputLabel;
import com.icesoft.faces.component.jsfcl.data.PopupBean;
import com.icesoft.faces.component.outputconnectionstatus.OutputConnectionStatus;
import com.icesoft.faces.component.panelpopup.PanelPopup;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import edu.ues.jhard.beans.BeanBaseJHardmin;
import edu.ues.jhard.beans.BeanBaseJWiki;
import edu.ues.jhard.jhardmin.LoggedUser;
import edu.ues.jhard.jpa.Usuario;
import edu.ues.jhard.jwiki.JreqArticulo;
import edu.ues.jhard.util.Navegacion;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;

/**
 * Bean para página JSP de jrequestUser, que sirve para buscar posibles soluciones a los problemas
 * informñaticosque tienen los usuarios comunes de la UES-FMO
 */
public class jrequestUser extends AbstractPageBean {

    private int __placeholder;

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private HtmlInputTextarea txtProblemas = new HtmlInputTextarea();

    public HtmlInputTextarea getTxtProblemas() {
        return txtProblemas;
    }

    public void setTxtProblemas(HtmlInputTextarea hit) {
        this.txtProblemas = hit;
    }

    private HtmlCommandButton btnBuscar = new HtmlCommandButton();

    public HtmlCommandButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(HtmlCommandButton hcb) {
        this.btnBuscar = hcb;
    }

    private HtmlCommandButton btnSolicitud = new HtmlCommandButton();

    public HtmlCommandButton getBtnSolicitud() {
        return btnSolicitud;
    }

    public void setBtnSolicitud(HtmlCommandButton hcb) {
        this.btnSolicitud = hcb;
    }

    private PopupBean panelPopup1Bean = new PopupBean();

    public PopupBean getPanelPopup1Bean() {
        return panelPopup1Bean;
    }

    public void setPanelPopup1Bean(PopupBean pb) {
        this.panelPopup1Bean = pb;
    }

    private PanelPopup popUpRegister = new PanelPopup();

    public PanelPopup getPopUpRegister() {
        return popUpRegister;
    }

    public void setPopUpRegister(PanelPopup pp) {
        this.popUpRegister = pp;
    }

    private HtmlCommandButton btnOk = new HtmlCommandButton();

    public HtmlCommandButton getBtnOk() {
        return btnOk;
    }

    public void setBtnOk(HtmlCommandButton hcb) {
        this.btnOk = hcb;
    }

    private List<JreqArticulo> listaArticulos;

    private String dirWiki;

    private boolean renderer;

    /**
     * @return the renderer
     */
    public boolean isRenderer() {
        return renderer;
    }

    /**
     * @param renderer the renderer to set
     */
    public void setRenderer(boolean renderer) {
        this.renderer = renderer;
    }

    private LoggedUser lu;

    private Usuario U;

    public LoggedUser getLu() {
        return lu;
    }

    public void setLu(LoggedUser lu) {
        this.lu = lu;
    }

    public Usuario getU() {
        return U;
    }

    public void setU(Usuario u) {
        this.U = u;
    }

    public BeanBaseJHardmin getJHardminInstance() {
        return (BeanBaseJHardmin) getBean("JHardminInstance");
    }

    private HtmlOutputLabel txtMensajeWiki = new HtmlOutputLabel();

    public HtmlOutputLabel getTxtMensajeWiki() {
        return txtMensajeWiki;
    }

    public void setTxtMensajeWiki(HtmlOutputLabel hol) {
        this.txtMensajeWiki = hol;
    }

    private OutputConnectionStatus estatus = new OutputConnectionStatus();

    public OutputConnectionStatus getEstatus() {
        return estatus;
    }

    public void setEstatus(OutputConnectionStatus ocs) {
        this.estatus = ocs;
    }

    /**
     * <p>Constructor.</p>
     */
    public jrequestUser() {
        dirWiki = "jwikiUser.iface?wkid=";
        String criterios = "display Linux tasks top";
        BeanBaseJWiki instance = new BeanBaseJWiki();
        this.listaArticulos = instance.searchEnWiki(criterios);
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
        this.renderer = false;
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

    /**
     * Método para para buscar las posibles soluciones según las palabras clave
     * que haya ingresado el usuario
     * @return
     */
    public String btnBuscar_action() {
        this.estatus.setActiveLabel("Buscando en JWiki");
        BeanBaseJWiki instance = new BeanBaseJWiki();
        List<JreqArticulo> ja = instance.searchEnWiki((String) this.txtProblemas.getValue());
        if (ja == null) this.txtMensajeWiki.setValue("No existen resultados de acuerdo a su búsqueda. Intente de nuevo. "); else {
            if (!(ja.size() > 0)) this.txtMensajeWiki.setValue("No existen resultados de acuerdo a su búsqueda. Intente de nuevo. "); else {
                this.txtMensajeWiki.setValue("Resultados de la búsqueda JWiki ");
                System.out.println("Articulos encontrados:");
                for (JreqArticulo a : ja) {
                    System.out.println("ID:" + a.getIdarticulo() + ", TITULO ARTICULO: " + a.getTitulo());
                }
                this.listaArticulos.clear();
                this.listaArticulos = ja;
                System.out.println("Exito en la prueba!");
            }
        }
        return null;
    }

    /**
     * Método para enviar hacia el artículo seleccionado al usuario, dado el ID del artículo
     * @return
     */
    public String irWiki() {
        String idArt = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idarticulo");
        dirWiki += idArt;
        System.out.println(dirWiki);
        Navegacion n;
        n = (Navegacion) getBean("Navegacion");
        n.setModuloActual("JWiki");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(dirWiki);
        } catch (IOException ex) {
            Logger.getLogger(jrequestUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "done";
    }

    public String btnSolicitud_action() {
        String navigation = "";
        lu = getJHardminInstance().getCurrentUser();
        if (lu == null) {
            navigation = null;
            this.renderer = true;
        } else navigation = "case1";
        return navigation;
    }

    public String btnOk_action() {
        this.renderer = false;
        return null;
    }

    /**
     * @return the listaArticulos
     */
    public List<JreqArticulo> getListaArticulos() {
        return listaArticulos;
    }

    /**
     * @param listaArticulos the listaArticulos to set
     */
    public void setListaArticulos(List<JreqArticulo> listaArticulos) {
        this.listaArticulos = listaArticulos;
    }

    /**
     * @return the dirWiki
     */
    public String getDirWiki() {
        return dirWiki;
    }

    /**
     * @param dirWiki the dirWiki to set
     */
    public void setDirWiki(String dirWiki) {
        this.dirWiki = dirWiki;
    }
}
