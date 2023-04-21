package fr.insa.rennes.pelias.pexecutor;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.HiddenField;
import fr.insa.rennes.pelias.framework.Batch;
import fr.insa.rennes.pelias.pexecutor.validators.IntValidator;
import fr.insa.rennes.pelias.platform.IRepository;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @version Viewbatch.java
 * @version Created on 16 févr. 2009, 22:20:29
 * @author 2bo
 */
public class Viewbatch extends AbstractPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    /** Gestion des lots
     * notDerivatedBatch permet d'activer ou non la saisie dans la page Viewbatch.jsp
     * currentBatch est bindé avec les champs de la page Viewbatch.jsp
     */
    private boolean notDerivatedBatch;

    private Batch currentBatch;

    private Button buttonDerivation = new Button();

    private String idBatch;

    private String onLoad;

    public Button getButtonDerivation() {
        return buttonDerivation;
    }

    public void setButtonDerivation(Button b) {
        this.buttonDerivation = b;
    }

    private Button buttonSave = new Button();

    public Button getButtonSave() {
        return buttonSave;
    }

    public void setButtonSave(Button b) {
        this.buttonSave = b;
    }

    private HiddenField hiddenFieldUUID = new HiddenField();

    public HiddenField getHiddenFieldUUID() {
        return hiddenFieldUUID;
    }

    public void setHiddenFieldUUID(HiddenField hf) {
        this.hiddenFieldUUID = hf;
    }

    private Button buttonEdit = new Button();

    public Button getButtonEdit() {
        return buttonEdit;
    }

    public void setButtonEdit(Button b) {
        this.buttonEdit = b;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public Viewbatch() {
        notDerivatedBatch = true;
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
        Map<String, String> res = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String edit = res.get("edit");
        String uuid = res.get("id");
        try {
            if (uuid == null) {
                throw new IllegalArgumentException();
            }
            idBatch = uuid;
            hiddenFieldUUID.setValue(uuid);
            IRepository<Batch> batchRepository = ApplicationBean1.getBatchIRepository();
            setCurrentBatch(batchRepository.getObject(UUID.fromString(uuid)));
            if (currentBatch == null) {
                throw new IllegalArgumentException();
            }
            if (edit != null) {
                notDerivatedBatch = false;
                buttonSave.setDisabled(false);
                if (edit.equals("1")) {
                    currentBatch.setId(UUID.randomUUID());
                }
            } else {
                buttonSave.setDisabled(true);
            }
            buttonDerivation.setDisabled(false);
            buttonEdit.setDisabled(false);
        } catch (IllegalArgumentException e) {
            UUID id = UUID.randomUUID();
            Batch b = new Batch(id, "Nouveau lot");
            setCurrentBatch(b);
            notDerivatedBatch = false;
            buttonSave.setDisabled(false);
            buttonEdit.setDisabled(true);
            buttonDerivation.setDisabled(true);
        }
        try {
            _init();
        } catch (Exception e) {
            log("Viewbatch Initialization Failure", e);
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
        if (onLoad == null) {
            onLoad = "window.opener.location.assign('Batches.jsp')";
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

    public String buttonDerivation_action() {
        Batch b = ApplicationBean1.getBatchIRepository().getObject(UUID.fromString((String) hiddenFieldUUID.getValue()));
        b.setCptName(b.getCptName() + 1);
        IRepository<Batch> batchRepository = ApplicationBean1.getBatchIRepository();
        batchRepository.putObject(b, true);
        b.setId(UUID.randomUUID());
        b.setLabel(b.getLabel() + "_" + b.getCptName());
        b.setCptName(0);
        batchRepository.putObject(b, true);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("Viewbatch.jsp?id=" + b.getId() + "&edit=1");
        } catch (IOException ex) {
            Logger.getLogger(Viewbatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String buttonSave_action() {
        try {
            currentBatch.setId(UUID.fromString(idBatch));
        } catch (Exception e) {
        }
        IRepository<Batch> batchRepository = ApplicationBean1.getBatchIRepository();
        batchRepository.putObject(getCurrentBatch(), true);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("Viewbatch.jsp?id=" + currentBatch.getId());
        } catch (IOException ex) {
            Logger.getLogger(Viewbatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * @return the notDerivatedBatch
     */
    public boolean isNotDerivatedBatch() {
        return notDerivatedBatch;
    }

    /**
     * @param notDerivatedBatch the notDerivatedBatch to set
     */
    public void setNotDerivatedBatch(boolean notDerivatedBatch) {
        this.notDerivatedBatch = notDerivatedBatch;
    }

    /**
     * @return the currentBatch
     */
    public Batch getCurrentBatch() {
        return currentBatch;
    }

    /**
     * @param currentBatch the currentBatch to set
     */
    public void setCurrentBatch(Batch currentBatch) {
        this.currentBatch = currentBatch;
    }

    public String buttonEdit_action() {
        try {
            currentBatch.setId(UUID.fromString(idBatch));
        } catch (Exception e) {
        }
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("Viewbatch.jsp?id=" + currentBatch.getId() + "&edit=2");
        } catch (IOException ex) {
            Logger.getLogger(Viewbatch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * @return the idBatch
     */
    public String getIdBatch() {
        return idBatch;
    }

    /**
     * @param idBatch the idBatch to set
     */
    public void setIdBatch(String idBatch) {
        this.idBatch = idBatch;
    }

    /**
     * @return the onLoad
     */
    public String getOnLoad() {
        return onLoad;
    }

    /**
     * @param onLoad the onLoad to set
     */
    public void setOnLoad(String onLoad) {
        this.onLoad = onLoad;
    }

    public void maxSize_validate(FacesContext context, UIComponent component, Object value) {
        IntValidator intValid = new IntValidator(value.toString());
        intValid.Validate();
    }
}
