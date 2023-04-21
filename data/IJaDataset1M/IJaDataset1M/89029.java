package confregwar.Administration.Applications;

import com.sun.data.provider.RowKey;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Checkbox;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.Hyperlink;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.Link;
import com.sun.webui.jsf.component.MessageGroup;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.component.RadioButton;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TableColumn;
import com.sun.webui.jsf.component.TableRowGroup;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.event.TableSelectPhaseListener;
import com.sun.webui.jsf.model.DefaultTableDataProvider;
import com.sun.webui.jsf.model.SingleSelectOptionsList;
import confreg.ejb.controls.AdminControlLocal;
import confreg.ejb.controls.RegistrationControlLocal;
import confreg.ejb.domain.ConferencePackStatus;
import confreg.ejb.exceptions.NonExistingConferencePackException;
import confreg.ejb.exceptions.NonExistingUserException;
import confreg.ejb.exceptions.ValidationException;
import javax.faces.FacesException;
import confregwar.RequestBean1;
import confregwar.SessionBean1;
import confregwar.ApplicationBean1;
import javax.ejb.EJB;
import javax.faces.component.html.HtmlPanelGrid;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Administrator
 */
public class index extends AbstractPageBean {

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

    private HtmlPanelGrid gridPanel1 = new HtmlPanelGrid();

    public HtmlPanelGrid getGridPanel1() {
        return gridPanel1;
    }

    public void setGridPanel1(HtmlPanelGrid hpg) {
        this.gridPanel1 = hpg;
    }

    private Label label1 = new Label();

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label l) {
        this.label1 = l;
    }

    private TextField textField1 = new TextField();

    public TextField getTextField1() {
        return textField1;
    }

    public void setTextField1(TextField tf) {
        this.textField1 = tf;
    }

    private Button list = new Button();

    public Button getList() {
        return list;
    }

    public void setList(Button b) {
        this.list = b;
    }

    private MessageGroup messageGroup1 = new MessageGroup();

    public MessageGroup getMessageGroup1() {
        return messageGroup1;
    }

    public void setMessageGroup1(MessageGroup mg) {
        this.messageGroup1 = mg;
    }

    private Table table2 = new Table();

    public Table getTable2() {
        return table2;
    }

    public void setTable2(Table t) {
        this.table2 = t;
    }

    private TableRowGroup tableRowGroup2 = new TableRowGroup();

    public TableRowGroup getTableRowGroup2() {
        return tableRowGroup2;
    }

    public void setTableRowGroup2(TableRowGroup trg) {
        this.tableRowGroup2 = trg;
    }

    private TableColumn tableColumn8 = new TableColumn();

    public TableColumn getTableColumn8() {
        return tableColumn8;
    }

    public void setTableColumn8(TableColumn tc) {
        this.tableColumn8 = tc;
    }

    private StaticText staticText2 = new StaticText();

    public StaticText getStaticText2() {
        return staticText2;
    }

    public void setStaticText2(StaticText st) {
        this.staticText2 = st;
    }

    private Button conferencePack = new Button();

    public Button getConferencePack() {
        return conferencePack;
    }

    public void setConferencePack(Button b) {
        this.conferencePack = b;
    }

    private Button personalInfo = new Button();

    public Button getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(Button b) {
        this.personalInfo = b;
    }

    private Button judgePayment = new Button();

    public Button getJudgePayment() {
        return judgePayment;
    }

    public void setJudgePayment(Button b) {
        this.judgePayment = b;
    }

    private Table tblUser = new Table();

    public Table getTblUser() {
        return tblUser;
    }

    public void setTblUser(Table t) {
        this.tblUser = t;
    }

    private TableRowGroup trgUser = new TableRowGroup();

    public TableRowGroup getTrgUser() {
        return trgUser;
    }

    public void setTrgUser(TableRowGroup trg) {
        this.trgUser = trg;
    }

    private TableColumn tableColumn4 = new TableColumn();

    public TableColumn getTableColumn4() {
        return tableColumn4;
    }

    public void setTableColumn4(TableColumn tc) {
        this.tableColumn4 = tc;
    }

    private StaticText staticText5 = new StaticText();

    public StaticText getStaticText5() {
        return staticText5;
    }

    public void setStaticText5(StaticText st) {
        this.staticText5 = st;
    }

    private TableColumn tableColumn5 = new TableColumn();

    public TableColumn getTableColumn5() {
        return tableColumn5;
    }

    public void setTableColumn5(TableColumn tc) {
        this.tableColumn5 = tc;
    }

    private StaticText staticText6 = new StaticText();

    public StaticText getStaticText6() {
        return staticText6;
    }

    public void setStaticText6(StaticText st) {
        this.staticText6 = st;
    }

    private TableColumn tableColumn6 = new TableColumn();

    public TableColumn getTableColumn6() {
        return tableColumn6;
    }

    public void setTableColumn6(TableColumn tc) {
        this.tableColumn6 = tc;
    }

    private StaticText staticText7 = new StaticText();

    public StaticText getStaticText7() {
        return staticText7;
    }

    public void setStaticText7(StaticText st) {
        this.staticText7 = st;
    }

    private TableColumn trgcUserpSelection = new TableColumn();

    public TableColumn getTrgcUserpSelection() {
        return trgcUserpSelection;
    }

    public void setTrgcUserpSelection(TableColumn tc) {
        this.trgcUserpSelection = tc;
    }

    private TableColumn tableRowGroup1SelectionColumn = new TableColumn();

    public TableColumn getTableRowGroup1SelectionColumn() {
        return tableRowGroup1SelectionColumn;
    }

    public void setTableRowGroup1SelectionColumn(TableColumn tc) {
        this.tableRowGroup1SelectionColumn = tc;
    }

    private RadioButton radioButton1 = new RadioButton();

    public RadioButton getRadioButton1() {
        return radioButton1;
    }

    public void setRadioButton1(RadioButton rb) {
        this.radioButton1 = rb;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public index() {
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
            log("index Initialization Failure", e);
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
    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    public String hyperlink2_action() {
        return null;
    }

    public String hyperlink1_action() {
        return null;
    }

    public String hyperlink3_action() {
        return null;
    }

    public String list_action() {
        getUsersFilter();
        return null;
    }

    public String btnBack_action() {
        return "case1";
    }

    public String btnConference_action() {
        return "case2";
    }

    public String btnPersonal_action() {
        return "case3";
    }

    public String btnJudge_action() {
        return "case4";
    }

    private confreg.ejb.domain.User[] users;

    private boolean refilter;

    private String alertSummary;

    private String alertDetail;

    private String alertType;

    private boolean alertRendered;

    private void setAlert(String summary, String detail, String type) {
        setAlertRendered(true);
        setAlertSummary(summary);
        setAlertDetail(detail);
        setAlertType(type);
    }

    @EJB
    AdminControlLocal adminControlLocal;

    public confreg.ejb.domain.User[] getUsers() {
        if (users == null || refilter) {
            if (getFilter() == null || getFilter().equals("")) {
                users = adminControlLocal.getAllUser().toArray(new confreg.ejb.domain.User[0]);
            } else {
                users = adminControlLocal.getFilterUser(getFilter()).toArray(new confreg.ejb.domain.User[0]);
            }
            refilter = false;
        }
        return users;
    }

    public confreg.ejb.domain.User[] getUsersFilter() {
        return getUsers();
    }

    public String getFilter() {
        return getSessionBean1().getUserFilter();
    }

    public void setFilter(String filter) {
        getSessionBean1().setUserFilter(filter);
        refilter = true;
    }

    public String conference_pack_action() {
        String selectedUserEmail = getSelectedUserEmail();
        if (selectedUserEmail == null) {
            setAlert("Select a user!", "", "error");
            return null;
        } else {
            getRequestBean1().setEmail(selectedUserEmail);
            return "conferencepack";
        }
    }

    public String pay_action() {
        String selectedUserEmail = getSelectedUserEmail();
        if (selectedUserEmail == null) {
            setAlert("Select a user!", "", "error");
            return null;
        } else {
            getRequestBean1().setEmail(selectedUserEmail);
            return "pay";
        }
    }

    public String cancelPack() {
        String userEmail = getSelectedUserEmail();
        if (userEmail != null) {
            try {
                adminControlLocal.cancelConferencePack(userEmail);
                setAlert("Registration cancelled!", "", "error");
                refilter = true;
            } catch (Exception ex) {
                setAlert("Cannot cancel the registration!", ex.getMessage(), "error");
            }
        }
        return null;
    }

    @EJB
    RegistrationControlLocal regControl;

    public String enablePackModify() {
        confreg.ejb.domain.User userEmail = getSelectedUserObject();
        if (userEmail != null) {
            confreg.ejb.domain.ConferencePack confPack = userEmail.getConferencePack();
            try {
                if (confPack != null) {
                    confPack.setStatus(ConferencePackStatus.MODIFYING);
                    regControl.modifyConferencePack(userEmail.getEmail(), confPack);
                    setAlert("Modification reenabled!", "", "error");
                    refilter = true;
                } else {
                    setAlert("Registration not exists!", "", "error");
                }
            } catch (Exception ex) {
                setAlert("Cannot reenable the modification!", ex.getMessage(), "error");
            }
        }
        return null;
    }

    public String getSelectedUserEmail() {
        int selectedRowCount = getTrgUser().getSelectedRowsCount();
        if (selectedRowCount != 1) {
            setAlert("No user selected!", "Select a user to modify!", "error");
            return null;
        }
        RowKey selectedRowKey = getTrgUser().getSelectedRowKeys()[0];
        int rowId = Integer.parseInt(selectedRowKey.getRowId());
        if (getFilter().length() != 0) {
            confreg.ejb.domain.User selectedUser = getUsersFilter()[rowId];
            return selectedUser.getEmail();
        } else {
            confreg.ejb.domain.User selectedUser = getUsers()[rowId];
            return selectedUser.getEmail();
        }
    }

    public String personalInfo_action() {
        String selectedUserEmail = getSelectedUserEmail();
        if (selectedUserEmail == null) {
            setAlert("Select a user!", "", "error");
            return null;
        } else {
            getRequestBean1().setEmail(selectedUserEmail);
            return "personaldata";
        }
    }

    public confreg.ejb.domain.User getSelectedUserObject() {
        if (getTrgUser().getSelectedRowsCount() != 1) return null;
        RowKey selectedRowKey = getTrgUser().getSelectedRowKeys()[0];
        int rowId = Integer.parseInt(selectedRowKey.getRowId());
        if (getFilter().length() != 0) {
            confreg.ejb.domain.User selectedUser = getUsersFilter()[rowId];
            return selectedUser;
        } else {
            confreg.ejb.domain.User selectedUser = getUsers()[rowId];
            return selectedUser;
        }
    }

    public String confirmPayment() {
        confreg.ejb.domain.User selectedUser = getSelectedUserObject();
        if (selectedUser == null) {
            setAlert("Select a user!", "", "error");
        } else {
            try {
                adminControlLocal.confirmPayment(selectedUser.getEmail());
                setAlert("Payment confirmed!", "", "success");
                refilter = true;
            } catch (NonExistingUserException ex) {
                setAlert("The user not exists!", "", "error");
            } catch (NonExistingConferencePackException ex) {
                setAlert("Non existing conference package!", "", "error");
            } catch (ValidationException ex) {
                setAlert("Validation error!", ex.getMessage(), "error");
            }
        }
        return null;
    }

    public String retractPayment() {
        confreg.ejb.domain.User selectedUser = getSelectedUserObject();
        if (selectedUser == null) {
            setAlert("Select a user!", "", "error");
        } else {
            try {
                adminControlLocal.retractPayment(selectedUser.getEmail());
                setAlert("Payment retracted!", "", "information");
                refilter = true;
            } catch (NonExistingUserException ex) {
                setAlert("The user not exists!", "", "error");
            } catch (NonExistingConferencePackException ex) {
                setAlert("Non existing conference package!", "", "error");
            } catch (ValidationException ex) {
                setAlert("Validation error!", ex.getMessage(), "error");
            }
        }
        return null;
    }

    private TableSelectPhaseListener userTablePhaseListener = new TableSelectPhaseListener();

    public void setSelectedUser(Object object) {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        if (rowKey != null) {
            userTablePhaseListener.setSelected(rowKey, object);
        }
    }

    public Object getSelectedUser() {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        return userTablePhaseListener.getSelected(rowKey);
    }

    public Object getSelectedUserValue() {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        return (rowKey != null) ? rowKey.getRowId() : null;
    }

    public boolean getSelectedUserState() {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        return userTablePhaseListener.isSelected(rowKey);
    }

    public String getAlertSummary() {
        return alertSummary;
    }

    public void setAlertSummary(String alertSummary) {
        this.alertSummary = alertSummary;
    }

    public String getAlertDetail() {
        return alertDetail;
    }

    public void setAlertDetail(String alertDetail) {
        this.alertDetail = alertDetail;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public boolean isAlertRendered() {
        return alertRendered;
    }

    public void setAlertRendered(boolean alertRendered) {
        this.alertRendered = alertRendered;
    }
}
