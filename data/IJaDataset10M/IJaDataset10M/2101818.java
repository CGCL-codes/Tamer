package jhard;

import com.icesoft.faces.component.ext.HtmlCommandButton;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.HtmlOutputLabel;
import com.icesoft.faces.component.ext.HtmlOutputText;
import com.icesoft.faces.component.ext.HtmlSelectOneListbox;
import com.icesoft.faces.component.jsfcl.data.PopupBean;
import com.icesoft.faces.component.panelpopup.PanelPopup;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import edu.ues.jhard.beans.BeanBaseJRequest;
import edu.ues.jhard.jpa.Equiposimple;
import edu.ues.jhard.jpa.Estadoequipo;
import edu.ues.jhard.jpa.Tecnico;
import java.util.ArrayList;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 *
 * Bean para la página de JRequestAdministracion, que realiza labores administrativas y de mantenimiento
 * en JRequest
 * @author Hugol
 */
public class JRequestAdministracion extends AbstractPageBean {

    private int __placeholder;

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private HtmlSelectOneListbox listaTecnicos = new HtmlSelectOneListbox();

    public HtmlSelectOneListbox getListaTecnicos() {
        return listaTecnicos;
    }

    public void setListaTecnicos(HtmlSelectOneListbox hsol) {
        this.listaTecnicos = hsol;
    }

    private HtmlSelectOneListbox listaEqS = new HtmlSelectOneListbox();

    public HtmlSelectOneListbox getListaEqS() {
        return listaEqS;
    }

    public void setListaEqS(HtmlSelectOneListbox hsol) {
        this.listaEqS = hsol;
    }

    private HtmlOutputLabel lblNombreTec = new HtmlOutputLabel();

    public HtmlOutputLabel getLblNombreTec() {
        return lblNombreTec;
    }

    public void setLblNombreTec(HtmlOutputLabel hol) {
        this.lblNombreTec = hol;
    }

    private PanelPopup popUpEqSimple = new PanelPopup();

    public PanelPopup getPopUpEqSimple() {
        return popUpEqSimple;
    }

    public void setPopUpEqSimple(PanelPopup pp) {
        this.popUpEqSimple = pp;
    }

    private HtmlInputText txtNomTec = new HtmlInputText();

    public HtmlInputText getTxtNomTec() {
        return txtNomTec;
    }

    public void setTxtNomTec(HtmlInputText hit) {
        this.txtNomTec = hit;
    }

    private HtmlInputText txtApeTec = new HtmlInputText();

    public HtmlInputText getTxtApeTec() {
        return txtApeTec;
    }

    public void setTxtApeTec(HtmlInputText hit) {
        this.txtApeTec = hit;
    }

    private HtmlCommandButton btnOkTec = new HtmlCommandButton();

    public HtmlCommandButton getBtnOkTec() {
        return btnOkTec;
    }

    public void setBtnOkTec(HtmlCommandButton hcb) {
        this.btnOkTec = hcb;
    }

    private HtmlCommandButton btnCerrarTec = new HtmlCommandButton();

    public HtmlCommandButton getBtnCerrarTec() {
        return btnCerrarTec;
    }

    public void setBtnCerrarTec(HtmlCommandButton hcb) {
        this.btnCerrarTec = hcb;
    }

    private PanelPopup popUpAgregarTec = new PanelPopup();

    public PanelPopup getPopUpAgregarTec() {
        return popUpAgregarTec;
    }

    public void setPopUpAgregarTec(PanelPopup pp) {
        this.popUpAgregarTec = pp;
    }

    private HtmlInputText txtNomEQ = new HtmlInputText();

    public HtmlInputText getTxtNomEQ() {
        return txtNomEQ;
    }

    public void setTxtNomEQ(HtmlInputText hit) {
        this.txtNomEQ = hit;
    }

    private HtmlInputText txtPropietarioEQ = new HtmlInputText();

    public HtmlInputText getTxtPropietarioEQ() {
        return txtPropietarioEQ;
    }

    public void setTxtPropietarioEQ(HtmlInputText hit) {
        this.txtPropietarioEQ = hit;
    }

    private PopupBean panelPopup1Bean = new PopupBean();

    public PopupBean getPanelPopup1Bean() {
        return panelPopup1Bean;
    }

    public void setPanelPopup1Bean(PopupBean pb) {
        this.panelPopup1Bean = pb;
    }

    private HtmlCommandButton btnOk = new HtmlCommandButton();

    public HtmlCommandButton getBtnOk() {
        return btnOk;
    }

    public void setBtnOk(HtmlCommandButton hcb) {
        this.btnOk = hcb;
    }

    private PanelPopup popUpMensajes = new PanelPopup();

    public PanelPopup getPopUpMensajes() {
        return popUpMensajes;
    }

    public void setPopUpMensajes(PanelPopup pp) {
        this.popUpMensajes = pp;
    }

    private HtmlOutputText lblMensajes = new HtmlOutputText();

    public HtmlOutputText getLblMensajes() {
        return lblMensajes;
    }

    public void setLblMensajes(HtmlOutputText hot) {
        this.lblMensajes = hot;
    }

    private HtmlCommandButton btnAceptarEQ = new HtmlCommandButton();

    public HtmlCommandButton getBtnAceptarEQ() {
        return btnAceptarEQ;
    }

    public void setBtnAceptarEQ(HtmlCommandButton hcb) {
        this.btnAceptarEQ = hcb;
    }

    private HtmlCommandButton btnCancelarEQ = new HtmlCommandButton();

    public HtmlCommandButton getBtnCancelarEQ() {
        return btnCancelarEQ;
    }

    public void setBtnCancelarEQ(HtmlCommandButton hcb) {
        this.btnCancelarEQ = hcb;
    }

    /**
     * <p>Constructor.</p>
     */
    public JRequestAdministracion() {
        limpiarListasCombos();
        llenarListasCombos();
        this.popUpEqSimple.setRendered(false);
        this.popUpAgregarTec.setRendered(false);
        this.popUpMensajes.setRendered(false);
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
            log("jrequestAdminitracion Initialization Failure", e);
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
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    private int opcionElegida = 0;

    private Tecnico TecnicoElegido;

    private Equiposimple EquipoElegido;

    private Estadoequipo estadoElegido;

    private List tec = new ArrayList();

    private List equipoSimple = new ArrayList();

    private List eeq = new ArrayList();

    public List getEeq() {
        return eeq;
    }

    public List getEquipoSimple() {
        return equipoSimple;
    }

    public List getTec() {
        return tec;
    }

    /**
     * Método para llenar con información a todas las Listas y Comboboxes de esta página. Dichas listas son para técnicos
     * y equipos simples
     */
    private void llenarListasCombos() {
        Tecnico[] tecnicos = new edu.ues.jhard.beans.BeanBaseJRequest().getTecnico();
        TecnicoElegido = tecnicos[0];
        for (int i = 0; i < tecnicos.length; i++) {
            String label = tecnicos[i].getNombres() + " " + tecnicos[i].getApellidos();
            tec.add(new SelectItem(tecnicos[i].getIdtecnico(), label));
        }
        Equiposimple[] eqs = new edu.ues.jhard.beans.BeanBaseJRequest().getEquipoSimple();
        EquipoElegido = eqs[0];
        for (int i = 0; i < eqs.length; i++) {
            String label = eqs[i].getDescripcion();
            equipoSimple.add(new SelectItem(eqs[i].getIdEquipoSimple(), label));
        }
    }

    private String efecto = "";

    /**
     * Método para manejar el evento del cambio en el ítem elegido de la lista de técnicos
     * @param ValueChangeEvent vce
     */
    public void listaTecnicos_processValueChange(ValueChangeEvent vce) {
        String tmp = (String) this.listaTecnicos.getValue();
        if (tmp != null) {
            Integer id = Integer.parseInt(tmp);
            Tecnico T = new BeanBaseJRequest().getEntityManager().find(Tecnico.class, id);
            this.TecnicoElegido = T;
            this.lblNombreTec.setValue(T.getNombres() + " " + T.getApellidos());
            setEfecto("highlight");
            System.out.println(getEfecto());
            System.out.println("eqSimpleElegidoAdmin-->" + TecnicoElegido.getNombres());
        }
    }

    /**
     * Método para manejar el evento del cambio en el ítem elegido de la lista de equipos simples
     *
     * @param ValueChangeEvent vce
     */
    public void listaEqS_processValueChange(ValueChangeEvent vce) {
        String tmp = (String) this.listaEqS.getValue();
        if (tmp != null) {
            Integer id = Integer.parseInt(tmp);
            System.out.println("id-->" + id);
            Equiposimple e = new BeanBaseJRequest().getEntityManager().find(Equiposimple.class, id);
            this.EquipoElegido = e;
            System.out.println("eqSimpleElegidoAdmin-->" + EquipoElegido.getDescripcion());
        }
    }

    /**
     * Método para manejar acción del botón btnAgregarTec
     * @return
     */
    public String btnAgregarTec_action() {
        this.popUpAgregarTec.setRendered(true);
        System.out.println("RENDERICE");
        this.popUpAgregarTec.setVisible(true);
        System.out.println("PUSE VISIBLE");
        this.popUpAgregarTec.setModal(true);
        System.out.println("SOLO EL ES MODIFICABLE");
        return null;
    }

    /**
     * Método para manejar acción del botón btnEliminarTec
     * @return
     */
    public String btnEliminarTec_action() {
        BeanBaseJRequest instance = new BeanBaseJRequest();
        if (TecnicoElegido != null) {
            if (instance.eliminarTecnico(this.TecnicoElegido) == true) {
                System.out.println("Técnico eliminado con éxito");
                this.lblMensajes.setValue("Técnico eliminado con éxito");
                this.popUpMensajes.setRendered(true);
                System.out.println("RENDERICE");
                this.popUpMensajes.setVisible(true);
                System.out.println("PUSE VISIBLE");
                this.popUpMensajes.setModal(true);
                System.out.println("SOLO EL ES MODIFICABLE");
                limpiarListasCombos();
                llenarListasCombos();
            } else {
                this.lblMensajes.setValue("No se puede eliminar dicho técnico. Existen órdenes de trabajo asociadas al mismo.");
                this.popUpMensajes.setRendered(true);
                System.out.println("RENDERICE");
                this.popUpMensajes.setVisible(true);
                System.out.println("PUSE VISIBLE");
                this.popUpMensajes.setModal(true);
                System.out.println("SOLO EL ES MODIFICABLE");
            }
        } else {
            System.out.println("Seleccione un técnico");
            this.lblMensajes.setValue("Seleccione un Tecnico de la Lista");
            this.popUpMensajes.setRendered(true);
            System.out.println("RENDERICE");
            this.popUpMensajes.setVisible(true);
            System.out.println("PUSE VISIBLE");
            this.popUpMensajes.setModal(true);
            System.out.println("SOLO EL ES MODIFICABLE");
        }
        return null;
    }

    public String btnOkTec_action() {
        Tecnico t = new Tecnico();
        t.setNombres(this.txtNomTec.getValue().toString());
        t.setApellidos(this.txtApeTec.getValue().toString());
        t.setCargo("Tecnico");
        new BeanBaseJRequest().registrarTecnico(t);
        this.popUpAgregarTec.setRendered(false);
        System.out.println("RENDERICE");
        this.popUpAgregarTec.setVisible(false);
        System.out.println("PUSE VISIBLE");
        this.popUpAgregarTec.setModal(false);
        System.out.println("SOLO EL ES MODIFICABLE");
        System.out.println("Técnico agregado con éxito");
        this.lblMensajes.setValue("Técnico agregado con éxito");
        this.popUpMensajes.setRendered(true);
        System.out.println("RENDERICE");
        this.popUpMensajes.setVisible(true);
        System.out.println("PUSE VISIBLE");
        this.popUpMensajes.setModal(true);
        System.out.println("SOLO EL ES MODIFICABLE");
        limpiarListasCombos();
        llenarListasCombos();
        return null;
    }

    public String btnCerrarTec_action() {
        this.popUpAgregarTec.setRendered(false);
        System.out.println("RENDERICE");
        this.popUpAgregarTec.setVisible(false);
        System.out.println("PUSE VISIBLE");
        this.popUpAgregarTec.setModal(false);
        System.out.println("SOLO EL ES MODIFICABLE");
        return null;
    }

    public String btnAceptarEQ_action() {
        if (opcionElegida == 1) {
            Equiposimple eq = new Equiposimple();
            eq.setDescripcion((String) this.txtNomEQ.getValue());
            eq.setPropietario((String) this.txtPropietarioEQ.getValue());
            Estadoequipo e = new BeanBaseJRequest().getEntityManager().find(Estadoequipo.class, 1);
            this.estadoElegido = e;
            eq.setIdestado(estadoElegido);
            new BeanBaseJRequest().registrarEquipoSimple(eq);
            this.popUpEqSimple.setRendered(false);
            System.out.println("RENDERICE");
            this.popUpEqSimple.setVisible(false);
            System.out.println("PUSE VISIBLE");
            this.popUpEqSimple.setModal(false);
            System.out.println("SOLO EL ES MODIFICABLE");
            System.out.println("Equipo Simple Agregado con éxito");
            this.lblMensajes.setValue("Equipo Simple Agregado con éxito");
            this.popUpMensajes.setRendered(true);
            System.out.println("RENDERICE");
            this.popUpMensajes.setVisible(true);
            System.out.println("PUSE VISIBLE");
            this.popUpMensajes.setModal(true);
            System.out.println("SOLO EL ES MODIFICABLE");
        }
        if (opcionElegida == 2) {
            this.EquipoElegido.setDescripcion((String) this.txtNomEQ.getValue());
            this.EquipoElegido.setPropietario((String) this.txtPropietarioEQ.getValue());
            Estadoequipo e = new BeanBaseJRequest().getEntityManager().find(Estadoequipo.class, 1);
            this.estadoElegido = e;
            EquipoElegido.setIdestado(estadoElegido);
            new BeanBaseJRequest().modificarEquipoSImple(EquipoElegido);
            this.popUpEqSimple.setRendered(false);
            System.out.println("RENDERICE");
            this.popUpEqSimple.setVisible(false);
            System.out.println("PUSE VISIBLE");
            this.popUpEqSimple.setModal(false);
            System.out.println("SOLO EL ES MODIFICABLE");
            System.out.println("Equipo Simple modificado con éxito");
            this.lblMensajes.setValue("Equipo Simple Modificado con éxito");
            this.popUpMensajes.setRendered(true);
            System.out.println("RENDERICE");
            this.popUpMensajes.setVisible(true);
            System.out.println("PUSE VISIBLE");
            this.popUpMensajes.setModal(true);
            System.out.println("SOLO EL ES MODIFICABLE");
        }
        this.txtNomEQ.setValue("");
        this.txtPropietarioEQ.setValue("");
        limpiarListasCombos();
        this.llenarListasCombos();
        this.opcionElegida = 0;
        return null;
    }

    public String btnCancelarEQ_action() {
        this.popUpEqSimple.setRendered(false);
        System.out.println("RENDERICE");
        this.popUpEqSimple.setVisible(false);
        System.out.println("PUSE VISIBLE");
        this.popUpEqSimple.setModal(false);
        System.out.println("SOLO EL ES MODIFICABLE");
        return null;
    }

    /**
     * Método para agregar un nuevo Equipo simple
     * @return
     */
    public String btnAgregarEqS_action() {
        this.opcionElegida = 1;
        this.popUpEqSimple.setRendered(true);
        System.out.println("RENDERICE");
        this.popUpEqSimple.setVisible(true);
        System.out.println("PUSE VISIBLE");
        this.popUpEqSimple.setModal(true);
        System.out.println("SOLO EL ES MODIFICABLE");
        return null;
    }

    /**
     * Método para modificar un equipo simple existente
     * @return
     */
    public String btnModEqS_action() {
        if (EquipoElegido != null) {
            this.txtNomEQ.setValue(this.EquipoElegido.getDescripcion());
            this.txtPropietarioEQ.setValue(this.EquipoElegido.getPropietario());
            this.opcionElegida = 2;
            System.out.println("SE METE A MODIFICAR");
            this.popUpEqSimple.setRendered(true);
            System.out.println("RENDERICE");
            this.popUpEqSimple.setVisible(true);
            System.out.println("PUSE VISIBLE");
            this.popUpEqSimple.setModal(true);
            System.out.println("SOLO EL ES MODIFICABLE");
        } else {
            System.out.println("Seleccione primero un Equipo Simple");
            this.lblMensajes.setValue("Seleccione primero un Equipo Simple");
            this.popUpMensajes.setRendered(true);
            System.out.println("RENDERICE");
            this.popUpMensajes.setVisible(true);
            System.out.println("PUSE VISIBLE");
            this.popUpMensajes.setModal(true);
            System.out.println("SOLO EL ES MODIFICABLE");
        }
        return null;
    }

    /**
     * Método para eliminar un equipo simple
     * @return
     */
    public String btnEliminarEqS_action() {
        BeanBaseJRequest instance = new BeanBaseJRequest();
        if (EquipoElegido != null) {
            if (instance.eliminarEquipoSimple(EquipoElegido) == true) {
                System.out.println("Equipo Simple eliminado con éxito");
                this.lblMensajes.setValue("Equipo Simple eliminado con éxito");
                this.popUpMensajes.setRendered(true);
                System.out.println("RENDERICE");
                this.popUpMensajes.setVisible(true);
                System.out.println("PUSE VISIBLE");
                this.popUpMensajes.setModal(true);
                System.out.println("SOLO EL ES MODIFICABLE");
            } else {
                this.lblMensajes.setValue("No se puede eliminar el Equipo Simple seleccionado. Ya existen órdenes de trabajo asociados al mismo");
                this.popUpMensajes.setRendered(true);
                this.popUpMensajes.setVisible(true);
                this.popUpMensajes.setModal(true);
            }
        } else {
            System.out.println("Seleccione primero un Equipo Simple");
            this.lblMensajes.setValue("Seleccione primero un Equipo Simple");
            this.popUpMensajes.setRendered(true);
            System.out.println("RENDERICE");
            this.popUpMensajes.setVisible(true);
            System.out.println("PUSE VISIBLE");
            this.popUpMensajes.setModal(true);
            System.out.println("SOLO EL ES MODIFICABLE");
        }
        this.opcionElegida = 0;
        this.EquipoElegido = null;
        limpiarListasCombos();
        llenarListasCombos();
        return null;
    }

    /**
     * Método para limpiar todas las listas y combos
     */
    private void limpiarListasCombos() {
        System.out.println("ME METO A LIMPIAR");
        this.eeq.clear();
        this.equipoSimple.clear();
        this.tec.clear();
        this.lblNombreTec.setValue("");
    }

    public String btnOk_action() {
        this.popUpMensajes.setVisible(false);
        this.popUpMensajes.setRendered(false);
        this.popUpMensajes.setModal(false);
        return null;
    }

    /**
     * @return the efecto
     */
    public String getEfecto() {
        return efecto;
    }

    /**
     * @param efecto the efecto to set
     */
    public void setEfecto(String efecto) {
        this.efecto = efecto;
    }
}
