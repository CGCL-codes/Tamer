package cba;

import com.sun.data.provider.FilterCriteria;
import com.sun.data.provider.RowKey;
import com.sun.data.provider.impl.CachedRowSetDataProvider;
import com.sun.data.provider.impl.RegexFilterCriteria;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.sql.rowset.CachedRowSetXImpl;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.HiddenField;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TableRowGroup;
import com.sun.webui.jsf.component.TextArea;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.model.DefaultTableDataProvider;
import com.sun.webui.jsf.model.SingleSelectOptionsList;
import com.sun.webui.jsf.component.RadioButton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.faces.FacesException;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Adeel Ilyas
 */
public class AccountOpenGrid extends AbstractPageBean {

    final int SAVED = 0;

    final int CANCELLED = 97;

    final int REJECTED = 98;

    final int APPROVED = 99;

    final String REQUEST_TYPE = "9006";

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
        ddl_srcDefaultOptions.setOptions(new com.sun.webui.jsf.model.Option[] { new com.sun.webui.jsf.model.Option("*", "All Records"), new com.sun.webui.jsf.model.Option("req_id", "Application ID"), new com.sun.webui.jsf.model.Option("req_status", "Application Status"), new com.sun.webui.jsf.model.Option("acctype", "Account Type"), new com.sun.webui.jsf.model.Option("pro_type", "Product Type"), new com.sun.webui.jsf.model.Option("pro_dsc", "Product Description"), new com.sun.webui.jsf.model.Option("cin", "Applicant CIN(s)") });
        accountOpenGridDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.accopengridRowSet}"));
        ddl_srcDefaultOptions.setSelectedValue("*");
    }

    private CachedRowSetDataProvider accountOpenGridDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getAccountOpenGridDataProvider() {
        return accountOpenGridDataProvider;
    }

    public void setAccountOpenGridDataProvider(CachedRowSetDataProvider crsdp) {
        this.accountOpenGridDataProvider = crsdp;
    }

    private SingleSelectOptionsList ddl_srcDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getDdl_srcDefaultOptions() {
        return ddl_srcDefaultOptions;
    }

    public void setDdl_srcDefaultOptions(SingleSelectOptionsList ssol) {
        this.ddl_srcDefaultOptions = ssol;
    }

    private DropDown ddl_src = new DropDown();

    public DropDown getDdl_src() {
        return ddl_src;
    }

    public void setDdl_src(DropDown dd) {
        this.ddl_src = dd;
    }

    private TableRowGroup tableRowGroup1 = new TableRowGroup();

    public TableRowGroup getTableRowGroup1() {
        return tableRowGroup1;
    }

    public void setTableRowGroup1(TableRowGroup trg) {
        this.tableRowGroup1 = trg;
    }

    public String getCurrentRow() {
        return tableRowGroup1.getRowKey().getRowId();
    }

    public void setCurrentRow(int row) {
    }

    private Object lastSelected;

    public Object getRBSelected() {
        String sv = (String) radioButton1.getSelectedValue();
        return sv.equals(lastSelected) ? sv : null;
    }

    public void setRBSelected(Object selected) {
        lastSelected = selected;
    }

    private Button btn_add = new Button();

    public Button getBtn_add() {
        return btn_add;
    }

    public void setBtn_add(Button b) {
        this.btn_add = b;
    }

    private Button btn_upd = new Button();

    public Button getBtn_upd() {
        return btn_upd;
    }

    public void setBtn_upd(Button b) {
        this.btn_upd = b;
    }

    private Button btn_ext = new Button();

    public Button getBtn_ext() {
        return btn_ext;
    }

    public void setBtn_ext(Button b) {
        this.btn_ext = b;
    }

    private Button btn_src = new Button();

    public Button getBtn_src() {
        return btn_src;
    }

    public void setBtn_src(Button b) {
        this.btn_src = b;
    }

    private TextArea mes = new TextArea();

    public TextArea getMes() {
        return mes;
    }

    public void setMes(TextArea ta) {
        this.mes = ta;
    }

    private Table tbl_acc = new Table();

    public Table getTbl_acc() {
        return tbl_acc;
    }

    public void setTbl_acc(Table t) {
        this.tbl_acc = t;
    }

    private Button btn_prt = new Button();

    public Button getBtn_prt() {
        return btn_prt;
    }

    public void setBtn_prt(Button b) {
        this.btn_prt = b;
    }

    private TextField fld_src = new TextField();

    public TextField getFld_src() {
        return fld_src;
    }

    public void setFld_src(TextField tf) {
        this.fld_src = tf;
    }

    private Label lbl_man = new Label();

    public Label getLbl_man() {
        return lbl_man;
    }

    public void setLbl_man(Label l) {
        this.lbl_man = l;
    }

    private Button btn_view = new Button();

    public Button getBtn_view() {
        return btn_view;
    }

    public void setBtn_view(Button b) {
        this.btn_view = b;
    }

    private RadioButton radioButton1 = new RadioButton();

    public RadioButton getRadioButton1() {
        return radioButton1;
    }

    public void setRadioButton1(RadioButton rb) {
        this.radioButton1 = rb;
    }

    private HiddenField initiatorflowID = new HiddenField();

    public HiddenField getInitiatorflowID() {
        return initiatorflowID;
    }

    public void setInitiatorflowID(HiddenField hf) {
        this.initiatorflowID = hf;
    }

    public AccountOpenGrid() {
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
    HashMap messages;

    String additionalFilter = "";

    String sortCriteria = " order by a.mod_dat desc";

    @Override
    public void init() {
        try {
            getSessionBean1().validateScreen("AccountOpenGrid.jsp");
            int validate = getSessionBean1().checking("AccountOpenGrid.jsp");
            switch(validate) {
                case 1:
                    btn_upd.setDisabled(true);
                    break;
                case 2:
                    btn_add.setDisabled(true);
                    break;
                case 3:
                    break;
                default:
                    btn_upd.setDisabled(true);
                    btn_add.setDisabled(true);
                    btn_view.setDisabled(true);
                    break;
            }
            super.init();
            String query = "select distinct a.req_id,a.ver_id,a.req_status,a.req_sts,a.flow_id," + "case when (a.acc_num IS NULL) then 'Not Assigned' else a.acc_num end as acc_num," + "acctype,a.acc_type,a.pro_type,a.pro_id,a.pro_dsc,case when a.acc_type <>2 then a.cin else fun_mul_cin(a.req_id,a.ver_id) end as cin,a.org_code,a.mod_by,a.mod_dat from " + "(select a.req_id,a.ver_id,aa.flow_id,CASE WHEN a.req_sts ='" + SAVED + "' THEN (SELECT sts_des from ad_status where sts_id = " + SAVED + ") " + "WHEN a.ver_id=0 or aa.flow_id > (select prev.flow_id from dw_account_v " + "prev where prev.req_id = aa.req_id and prev.ver_id = aa.ver_id-1) " + "THEN 'Forwarded' ELSE 'Returned' END " + "AS req_status,aa.req_sts,a.acc_num,coalesce(c.par_dsc,'') as acctype,a.acc_type" + ",coalesce(k.par_dsc,'') as pro_type,a.pro_id,(select pro_dsc from pr_product where pro_id = a.pro_id) as pro_dsc,a.cin,aa.org_code," + "aa.mod_by,aa.mod_dat from dw_account_v a join ad_request aa on a.req_id = aa.req_id " + "and a.ver_id = aa.ver_id left outer join ad_parameter c on " + "a.acc_type = c.par_cod and c.par_typ = 'acc_types' left outer join ad_parameter k on " + "a.pro_type = k.par_cod and k.par_typ = 'pro_typ' where aa.req_type ='" + REQUEST_TYPE + "') a";
            String filter1 = getSessionBean1().getRequestsFilter(REQUEST_TYPE, "dw_account_v", 1);
            String filter2 = getSessionBean1().getRequestsFilter(REQUEST_TYPE, "dw_account_v", 2);
            if (!getSessionBean1().hasWorkflow(filter1) && !getSessionBean1().hasWorkflow(filter2)) {
                btn_add.setDisabled(true);
                btn_upd.setDisabled(true);
                btn_view.setDisabled(true);
            }
            additionalFilter = " WHERE ((" + filter1.substring(6) + ") OR (" + filter2.substring(6) + "))";
            initiatorflowID.setText(getSessionBean1().getInitiatorFlowId(REQUEST_TYPE));
            String addRights = "SELECT w.flow_id from ad_workflow w WHERE w.req_type = " + REQUEST_TYPE + " AND w.ROL_ID = " + getSessionBean1().getRoleId() + " AND w.flow_id <=" + initiatorflowID.getText();
            Vector addRightsV = getSessionBean1().executeSQLSelect(addRights);
            if (addRightsV.size() == 0) {
                btn_add.setDisabled(true);
            }
            getSessionBean1().getAccopengridRowSet().setCommand(query + additionalFilter + sortCriteria);
            getSessionBean1().getAccopengridRowSet().execute();
            getAccountOpenGridDataProvider().refresh();
            tableRowGroup1.clearFilter();
            HttpSession session = ((HttpServletRequest) getExternalContext().getRequest()).getSession();
            if (session.getAttribute("Update") != null) {
                mes.setText(session.getAttribute("Update").toString());
            }
            session.setAttribute("Update", "");
            String[] err = { "CM006", "CM007", "CM018", "CM019", "CM025", "CM043" };
            messages = getSessionBean1().getMessages(err);
            if (messages.containsKey("Exception")) {
                getMes().setText(messages.get("Exception"));
                btn_upd.setDisabled(true);
                btn_add.setDisabled(true);
                btn_view.setDisabled(true);
                ddl_src.setDisabled(true);
                fld_src.setDisabled(true);
                btn_src.setDisabled(true);
            }
            tableRowGroup1.setEmptyDataMsg(messages.get("CM007").toString());
        } catch (Exception ex) {
            System.out.println("AccountOpenGrid, init(), " + getSessionBean1().getUserId() + " - " + ex);
            try {
                this.getExternalContext().redirect("/CBA/faces/InitError.jsp");
            } catch (IOException ioe) {
            }
        }
        try {
            _init();
        } catch (Exception e) {
            log("AccountOpenGrid Code Initialization Failure", e);
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
        try {
            if (getSessionBean1().getRow() == null || (Integer.parseInt(getSessionBean1().getRow()) <= getAccountOpenGridDataProvider().getRowCount())) {
                setRBSelected(getSessionBean1().getRow());
            } else {
                setRBSelected("0");
            }
            getAccountOpenGridDataProvider().refresh();
        } catch (Exception ex) {
            System.out.println("AccountOpenGrid, prerender() , " + getSessionBean1().getUserId() + " - " + ex);
            try {
                this.getExternalContext().redirect("/CBA/faces/InitError.jsp");
            } catch (IOException ioe) {
            }
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
        getAccountOpenGridDataProvider().close();
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

    String RetSts = "";

    private String UpdateView_action() {
        String str;
        try {
            RowKey aRowKey = new RowKey();
            String aRowId = getSessionBean1().getRow();
            aRowKey = getAccountOpenGridDataProvider().getRowKey(aRowId);
            str = getAccountOpenGridDataProvider().getValue("req_id", aRowKey).toString();
            getSessionBean1().setUpd_val(str);
            str = getAccountOpenGridDataProvider().getValue("ver_id", aRowKey).toString();
            getSessionBean1().setUpd_val2(str);
            str = getAccountOpenGridDataProvider().getValue("req_status", aRowKey).toString();
            getSessionBean1().setUpd_val3(str);
            str = getAccountOpenGridDataProvider().getValue("flow_id", aRowKey).toString();
            getSessionBean1().setUpd_val4(str);
            getSessionBean1().setUpd_val5("");
            return "ok";
        } catch (Exception ex) {
            if (lastSelected == null || lastSelected == "") {
                return "noselection";
            } else {
                System.out.println("AccountOpenGrid ,Error in UpdateView_action Method, " + getSessionBean1().getUserId() + " - " + ex);
                return "error";
            }
        }
    }

    public String btn_upd_action() {
        try {
            RetSts = UpdateView_action();
            if (RetSts.equals("error")) {
                getMes().setText(messages.get("CM018"));
                return null;
            } else if (RetSts.equals("noselection")) {
                getMes().setText(messages.get("CM006"));
                return null;
            } else if (RetSts.equals("ok")) {
                if (Integer.parseInt(getSessionBean1().getUpd_val4()) > Integer.parseInt(initiatorflowID.getText().toString())) {
                    getSessionBean1().setPag_nav("Approve");
                } else {
                    getSessionBean1().setPag_nav("Update");
                }
                return "accountopen";
            }
        } catch (Exception ex) {
            System.out.println("AccountOpenGrid, btn_upd_action(), " + getSessionBean1().getUserId() + " - " + ex);
            getMes().setText(messages.get("CM018"));
            return null;
        }
        return null;
    }

    public String btn_add_action() {
        try {
            getSessionBean1().setPag_nav("Add");
            return "accountopen";
        } catch (Exception ex) {
            System.out.println("AccountOpenGrid , btn_Add_action(), " + getSessionBean1().getUserId() + " - " + ex);
            getMes().setText(messages.get("CM025"));
            return null;
        }
    }

    public String btn_ext_action() {
        return "dashboard";
    }

    public String btn_prt_action() {
        Map fillParams = new HashMap();
        String e_qry = "";
        try {
            if (ddl_src.getSelected() == null) {
                return null;
            }
            String searchSelection = ddl_src.getSelected().toString();
            String searchValue = new String();
            if (fld_src.getText() == null) {
                searchValue = "";
            } else {
                searchValue = getSessionBean1().replace(fld_src.getText().toString());
            }
            if (searchSelection.equals("*")) {
                e_qry = additionalFilter;
            } else {
                e_qry = additionalFilter + " and lower(" + searchSelection + ") like lower('%" + searchValue + "%')";
            }
            fillParams.put("UserName", getSessionBean1().getUserId());
            fillParams.put("E_Query", e_qry);
            getSessionBean1().generateReport("PendingApproval", fillParams, getSessionBean1().getUserId());
        } catch (Exception ex) {
            System.out.println("Error in btn_prt_action() AccountOpenGrid.jsp, User ID " + getSessionBean1().getUserId() + "- Exception generating report: " + ex.getMessage());
            getMes().setText(messages.get("CM043"));
        }
        return null;
    }

    public String btn_src_action() {
        getMes().setText(" ");
        try {
            if (ddl_src.getSelected() == null) {
                return null;
            }
            String searchSelection = ddl_src.getSelected().toString();
            String searchValue = new String();
            if (fld_src.getText() == null) {
                searchValue = "";
            } else {
                searchValue = fld_src.getText().toString();
            }
            if (searchSelection.equals("*")) {
                getAccountOpenGridDataProvider().refresh();
                tableRowGroup1.clearFilter();
                fld_src.setText("");
            } else {
                String regExpression = new String();
                for (int i = 0; i < searchValue.length(); i++) {
                    regExpression = regExpression + "[" + searchValue.substring(i, i + 1).toUpperCase() + searchValue.substring(i, i + 1).toLowerCase() + "]";
                }
                regExpression = ".*" + regExpression + ".*";
                tableRowGroup1.getTableDataFilter().setFilterCriteria(new FilterCriteria[] { new RegexFilterCriteria(getAccountOpenGridDataProvider().getFieldKey(searchSelection), regExpression) });
                getAccountOpenGridDataProvider().refresh();
            }
        } catch (Exception ex) {
            System.out.println("AccountOpenGrid, btn_src_action(), " + getSessionBean1().getUserId() + " - " + ex);
            getMes().setText(messages.get("CM019"));
        }
        return null;
    }

    public void radioButton1_processValueChange(ValueChangeEvent event) {
        lastSelected = (String) RadioButton.getSelected("radioGroup");
        getSessionBean1().setRow((String) lastSelected);
    }

    public void ddl_src_processValueChange(ValueChangeEvent event) {
        if (ddl_src.getSelected().equals("*")) {
            fld_src.setDisabled(true);
        } else {
            fld_src.setDisabled(false);
        }
    }

    public String btn_view_action() {
        try {
            RetSts = UpdateView_action();
            if (RetSts.equals("error")) {
                getMes().setText(messages.get("CM018"));
                return null;
            } else if (RetSts.equals("noselection")) {
                getMes().setText(messages.get("CM006"));
                return null;
            } else if (RetSts.equals("ok")) {
                getSessionBean1().setPag_nav("View");
                return "accountopen";
            }
        } catch (Exception ex) {
            System.out.println("AccountOpenGrid, btn_view_action(), " + getSessionBean1().getUserId() + " - " + ex);
            getMes().setText(messages.get("CM018"));
            return null;
        }
        return null;
    }
}
