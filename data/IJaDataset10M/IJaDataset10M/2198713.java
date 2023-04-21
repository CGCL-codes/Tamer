package cnery;

import cnerydb.HibernateUtil;
import cnerydb.Scene;
import com.daveoxley.cnery.AbstractCNeryPageBean;
import com.sun.webui.jsf.component.Alert;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.model.Option;
import com.sun.webui.jsf.model.SingleSelectOptionsList;
import javax.faces.FacesException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Dave Oxley <dave@daveoxley.co.uk>
 */
public class SceneActivation extends AbstractCNeryPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private Alert alert1 = new Alert();

    public Alert getAlert1() {
        return alert1;
    }

    public void setAlert1(Alert a) {
        this.alert1 = a;
    }

    private DropDown dropDown1 = new DropDown();

    public DropDown getDropDown1() {
        return dropDown1;
    }

    public void setDropDown1(DropDown dd) {
        this.dropDown1 = dd;
    }

    private DropDown dropDown2 = new DropDown();

    public DropDown getDropDown2() {
        return dropDown2;
    }

    public void setDropDown2(DropDown dd) {
        this.dropDown2 = dd;
    }

    private DropDown dropDown3 = new DropDown();

    public DropDown getDropDown3() {
        return dropDown3;
    }

    public void setDropDown3(DropDown dd) {
        this.dropDown3 = dd;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public SceneActivation() {
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
            log("SceneActivation Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
        cnerydb.SceneActivation sceneActivation = getSessionBean1().getSceneActivation();
        if (sceneActivation == null) {
            sceneActivation = new cnerydb.SceneActivation();
            this.addMode = true;
        }
        this.sceneActivation = sceneActivation;
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
        super.preprocess();
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
        super.prerender();
        initFieldValues();
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
        super.destroy();
    }

    private boolean addMode = false;

    private boolean initFieldValues = true;

    private void initFieldValues() {
        if (initFieldValues && !addMode) {
            getDropDown1().setSelected(sceneActivation.getGroupAddress());
            getDropDown2().setSelected(sceneActivation.getGroupOnAction());
            getDropDown3().setSelected(sceneActivation.getGroupOffAction());
        }
        initFieldValues = true;
    }

    private cnerydb.SceneActivation sceneActivation;

    public String okButton1_action() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (addMode) {
                Scene scene = getSessionBean1().getScene();
                session.evict(scene);
                scene = (Scene) session.get(Scene.class, scene.getId());
                scene.addChild(sceneActivation);
            } else {
                session.evict(sceneActivation);
                sceneActivation = (cnerydb.SceneActivation) session.get(cnerydb.SceneActivation.class, sceneActivation.getId());
            }
            String group = (String) getDropDown1().getSelected().toString();
            sceneActivation.setGroupAddress(group);
            String groupOnAction = (String) getDropDown2().getSelected().toString();
            sceneActivation.setGroupOnAction(groupOnAction.charAt(0));
            String groupOffAction = (String) getDropDown3().getSelected().toString();
            sceneActivation.setGroupOffAction(groupOffAction.charAt(0));
            if (addMode) session.saveOrUpdate(sceneActivation); else {
                session.evict(sceneActivation);
                session.merge(sceneActivation);
            }
            tx.commit();
        } catch (Exception e) {
            handleException(e);
            if (tx != null) tx.rollback();
            initFieldValues = false;
            return null;
        }
        return "scene_activations_case";
    }

    public String cancelButton1_action() {
        return "scene_activations_case";
    }

    public Option[] getSceneActionOptions() {
        return cnerydb.SceneActivation.getSceneActionOptions();
    }
}
