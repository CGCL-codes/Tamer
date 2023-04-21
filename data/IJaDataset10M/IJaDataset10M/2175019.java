package exercisecontrolsystem;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.Hyperlink;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.Link;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.component.PanelLayout;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TableColumn;
import com.sun.webui.jsf.component.TableRowGroup;
import gov.nist.mel.emergency.ecs.beans.Scenario;
import exercisecontrolsystem.entities.dataproviders.ScenarioDataProvider;
import gov.nist.exercisecontrolsystem.jsfbeans.SystemMessage;
import javax.faces.FacesException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Guillaume Radde (guillaume.radde@nist.gov)
 */
public class ManageScenarios extends AbstractPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private Page page1 = new Page();

    public Page getPage1() {
        return page1;
    }

    public void setPage1(Page p) {
        this.page1 = p;
    }

    private Html html1 = new Html();

    public Html getHtml1() {
        return html1;
    }

    public void setHtml1(Html h) {
        this.html1 = h;
    }

    private Head head1 = new Head();

    public Head getHead1() {
        return head1;
    }

    public void setHead1(Head h) {
        this.head1 = h;
    }

    private Link link1 = new Link();

    public Link getLink1() {
        return link1;
    }

    public void setLink1(Link l) {
        this.link1 = l;
    }

    private Body body1 = new Body();

    public Body getBody1() {
        return body1;
    }

    public void setBody1(Body b) {
        this.body1 = b;
    }

    private Form form1 = new Form();

    public Form getForm1() {
        return form1;
    }

    public void setForm1(Form f) {
        this.form1 = f;
    }

    private PanelLayout layoutPanel1 = new PanelLayout();

    public PanelLayout getLayoutPanel1() {
        return layoutPanel1;
    }

    public void setLayoutPanel1(PanelLayout pl) {
        this.layoutPanel1 = pl;
    }

    private Label manageScenariosLabel = new Label();

    public Label getManageScenariosLabel() {
        return manageScenariosLabel;
    }

    public void setManageScenariosLabel(Label l) {
        this.manageScenariosLabel = l;
    }

    private Hyperlink backToMainPageHyperlink = new Hyperlink();

    public Hyperlink getBackToMainPageHyperlink() {
        return backToMainPageHyperlink;
    }

    public void setBackToMainPageHyperlink(Hyperlink h) {
        this.backToMainPageHyperlink = h;
    }

    private PanelLayout layoutPanel2 = new PanelLayout();

    public PanelLayout getLayoutPanel2() {
        return layoutPanel2;
    }

    public void setLayoutPanel2(PanelLayout pl) {
        this.layoutPanel2 = pl;
    }

    private Table scenariosTable = new Table();

    public Table getScenariosTable() {
        return scenariosTable;
    }

    public void setScenariosTable(Table t) {
        this.scenariosTable = t;
    }

    private TableRowGroup tableRowGroup1 = new TableRowGroup();

    public TableRowGroup getTableRowGroup1() {
        return tableRowGroup1;
    }

    public void setTableRowGroup1(TableRowGroup trg) {
        this.tableRowGroup1 = trg;
    }

    private TableColumn tableColumn4 = new TableColumn();

    public TableColumn getTableColumn4() {
        return tableColumn4;
    }

    public void setTableColumn4(TableColumn tc) {
        this.tableColumn4 = tc;
    }

    private StaticText staticText4 = new StaticText();

    public StaticText getStaticText4() {
        return staticText4;
    }

    public void setStaticText4(StaticText st) {
        this.staticText4 = st;
    }

    private TableColumn tableColumn5 = new TableColumn();

    public TableColumn getTableColumn5() {
        return tableColumn5;
    }

    public void setTableColumn5(TableColumn tc) {
        this.tableColumn5 = tc;
    }

    private StaticText staticText5 = new StaticText();

    public StaticText getStaticText5() {
        return staticText5;
    }

    public void setStaticText5(StaticText st) {
        this.staticText5 = st;
    }

    private TableColumn tableColumn6 = new TableColumn();

    public TableColumn getTableColumn6() {
        return tableColumn6;
    }

    public void setTableColumn6(TableColumn tc) {
        this.tableColumn6 = tc;
    }

    private Button editButton = new Button();

    public Button getEditButton() {
        return editButton;
    }

    public void setEditButton(Button b) {
        this.editButton = b;
    }

    private TableColumn tableColumn7 = new TableColumn();

    public TableColumn getTableColumn7() {
        return tableColumn7;
    }

    public void setTableColumn7(TableColumn tc) {
        this.tableColumn7 = tc;
    }

    private Button deleteButton = new Button();

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button b) {
        this.deleteButton = b;
    }

    private Hyperlink createScenarioHyperlink = new Hyperlink();

    public Hyperlink getCreateScenarioHyperlink() {
        return createScenarioHyperlink;
    }

    public void setCreateScenarioHyperlink(Hyperlink h) {
        this.createScenarioHyperlink = h;
    }

    private PanelLayout layoutPanel3 = new PanelLayout();

    public PanelLayout getLayoutPanel3() {
        return layoutPanel3;
    }

    public void setLayoutPanel3(PanelLayout pl) {
        this.layoutPanel3 = pl;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public ManageScenarios() {
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
        scenarioDataProvider.setList(getSessionBean1().getScenarioManager().findAllScenarios());
        try {
            _init();
        } catch (Exception e) {
            log("ManageScenarios Initialization Failure", e);
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

    private ScenarioDataProvider scenarioDataProvider = new ScenarioDataProvider();

    public ScenarioDataProvider getScenarioDataProvider() {
        return scenarioDataProvider;
    }

    public void setScenarioDataProvider(ScenarioDataProvider scenarioDataProvider) {
        this.scenarioDataProvider = scenarioDataProvider;
    }

    public String backToMainPageHyperlink_action() {
        return "case3";
    }

    public String editButton_action() {
        Scenario editedScenario = (Scenario) scenarioDataProvider.getObject(tableRowGroup1.getRowKey());
        getSessionBean1().getScenarioManager().setEditedScenario(editedScenario);
        getSessionBean1().setPreviousUrl("ManageScenarios.jsp");
        return "EditScenario";
    }

    public String button2_action() {
        return null;
    }

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ExerciseControlSystemPU");

    public String deleteButton_action() {
        Scenario deletedScenarioId = (Scenario) scenarioDataProvider.getObject(tableRowGroup1.getRowKey());
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Scenario deletedScenario = em.find(Scenario.class, deletedScenarioId.getScenarioId());
            deletedScenario.setEnabled(false);
            em.getTransaction().commit();
            getRequestBean1().setCurrentSystemMessage(new SystemMessage(SystemMessage.SUCCESS, "The Scenario has been succesfully deleted"));
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(getClass().getName()).log(java.util.logging.Level.SEVERE, "exception caught", e);
            em.getTransaction().rollback();
            getRequestBean1().setCurrentSystemMessage(new SystemMessage(SystemMessage.ERROR, "An error occured while trying to delte the scenario"));
        } finally {
            em.close();
        }
        scenarioDataProvider.setList(getSessionBean1().getScenarioManager().findAllScenarios());
        return null;
    }

    public String createScenarioHyperlink_action() {
        return "case2";
    }
}
