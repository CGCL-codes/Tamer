package cba;

import com.sun.data.provider.RowKey;
import com.sun.data.provider.impl.CachedRowSetDataProvider;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.sql.rowset.CachedRowSetXImpl;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.HiddenField;
import com.sun.webui.jsf.component.ImageComponent;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.PanelLayout;
import com.sun.webui.jsf.component.RadioButton;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TableColumn;
import com.sun.webui.jsf.component.TableRowGroup;
import com.sun.webui.jsf.component.TextArea;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.model.SingleSelectOptionsList;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.BigDecimalConverter;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Irfan.Patoli
 */
public class LoanAccountAuthentication extends AbstractPageBean {

    HashMap messages;

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
        ddl_verify_viaDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{LoanAccountAuthentication.ddl_verify_viaRowSet}"));
        ddl_verify_viaRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        ddl_verify_viaRowSet.setCommand("Select \'-1\' as par_cod,\'Select\' as par_dsc\r\nUnion\r\nSELECT  ad_parameter.par_cod, ad_parameter.par_dsc  FROM ad_parameter WHERE ad_parameter.par_typ = \'accver\' order by par_cod asc");
        ddl_verify_viaRowSet.setTableName("ad_parameter");
        ddl_acc_noDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{LoanAccountAuthentication.ddl_acc_noRowSet}"));
        ddl_acc_noRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        ddl_acc_noRowSet.setCommand("Select 'Select'::varchar as acc_num,0::numeric as acc_sts,''::varchar as org_cod Union select acc_num,acc_sts,org_cod from dw_account where acc_num=-1 order by acc_num desc");
        ddl_acc_noRowSet.setTableName("dw_account");
        dw_tbl_acc_hold_partDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.dw_tbl_acc_hold_parttRowSet}"));
        dw_tbl_signatory_infoDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.dw_tbl_signatory_infoRowSet}"));
    }

    private SingleSelectOptionsList ddl_acc_noDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getDdl_acc_noDefaultOptions() {
        return ddl_acc_noDefaultOptions;
    }

    public void setDdl_acc_noDefaultOptions(SingleSelectOptionsList ssol) {
        this.ddl_acc_noDefaultOptions = ssol;
    }

    private SingleSelectOptionsList ddl_verify_viaDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getDdl_verify_viaDefaultOptions() {
        return ddl_verify_viaDefaultOptions;
    }

    public void setDdl_verify_viaDefaultOptions(SingleSelectOptionsList ssol) {
        this.ddl_verify_viaDefaultOptions = ssol;
    }

    private Button btn_proceed = new Button();

    public Button getBtn_proceed() {
        return btn_proceed;
    }

    public void setBtn_proceed(Button b) {
        this.btn_proceed = b;
    }

    private DropDown ddl_verify_via = new DropDown();

    public DropDown getDdl_verify_via() {
        return ddl_verify_via;
    }

    public void setDdl_verify_via(DropDown dd) {
        this.ddl_verify_via = dd;
    }

    private CachedRowSetDataProvider ddl_verify_viaDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getDdl_verify_viaDataProvider() {
        return ddl_verify_viaDataProvider;
    }

    public void setDdl_verify_viaDataProvider(CachedRowSetDataProvider crsdp) {
        this.ddl_verify_viaDataProvider = crsdp;
    }

    private CachedRowSetXImpl ddl_verify_viaRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getDdl_verify_viaRowSet() {
        return ddl_verify_viaRowSet;
    }

    public void setDdl_verify_viaRowSet(CachedRowSetXImpl crsxi) {
        this.ddl_verify_viaRowSet = crsxi;
    }

    private CachedRowSetDataProvider ddl_acc_noDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getDdl_acc_noDataProvider() {
        return ddl_acc_noDataProvider;
    }

    public void setDdl_acc_noDataProvider(CachedRowSetDataProvider crsdp) {
        this.ddl_acc_noDataProvider = crsdp;
    }

    private CachedRowSetDataProvider dw_tbl_acc_hold_partDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getDw_tbl_acc_hold_partDataProvider() {
        return dw_tbl_acc_hold_partDataProvider;
    }

    public void setDw_tbl_acc_hold_partDataProvider(CachedRowSetDataProvider crsdp) {
        this.dw_tbl_acc_hold_partDataProvider = crsdp;
    }

    private CachedRowSetDataProvider dw_tbl_signatory_infoDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getDw_tbl_signatory_infoDataProvider() {
        return dw_tbl_signatory_infoDataProvider;
    }

    public void setDw_tbl_signatory_infoDataProvider(CachedRowSetDataProvider crsdp) {
        this.dw_tbl_signatory_infoDataProvider = crsdp;
    }

    private CachedRowSetXImpl ddl_acc_noRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getDdl_acc_noRowSet() {
        return ddl_acc_noRowSet;
    }

    public void setDdl_acc_noRowSet(CachedRowSetXImpl crsxi) {
        this.ddl_acc_noRowSet = crsxi;
    }

    private BigDecimalConverter ddl_verify_viaConverter = new BigDecimalConverter();

    public BigDecimalConverter getDdl_verify_viaConverter() {
        return ddl_verify_viaConverter;
    }

    public void setDdl_verify_viaConverter(BigDecimalConverter bdc) {
        this.ddl_verify_viaConverter = bdc;
    }

    private DropDown ddl_acc_no = new DropDown();

    public DropDown getDdl_acc_no() {
        return ddl_acc_no;
    }

    public void setDdl_acc_no(DropDown dd) {
        this.ddl_acc_no = dd;
    }

    private HiddenField url_name = new HiddenField();

    public HiddenField getUrl_name() {
        return url_name;
    }

    public void setUrl_name(HiddenField hf) {
        this.url_name = hf;
    }

    private TextField fld_verify_via = new TextField();

    public TextField getFld_verify_via() {
        return fld_verify_via;
    }

    public void setFld_verify_via(TextField tf) {
        this.fld_verify_via = tf;
    }

    private Button btn_authenticate = new Button();

    public Button getBtn_authenticate() {
        return btn_authenticate;
    }

    public void setBtn_authenticate(Button b) {
        this.btn_authenticate = b;
    }

    private Button btn_clear = new Button();

    public Button getBtn_clear() {
        return btn_clear;
    }

    public void setBtn_clear(Button b) {
        this.btn_clear = b;
    }

    private TextArea mes = new TextArea();

    public TextArea getMes() {
        return mes;
    }

    public void setMes(TextArea ta) {
        this.mes = ta;
    }

    private Table tbl_acc_holder_part = new Table();

    public Table getTbl_acc_holder_part() {
        return tbl_acc_holder_part;
    }

    public void setTbl_acc_holder_part(Table t) {
        this.tbl_acc_holder_part = t;
    }

    private Object RBSelected = new Object();

    private Object lastSelected = "-1";

    public Object getRBSelected() {
        String sv = (String) radioButton1.getSelectedValue();
        return sv.equals(lastSelected) ? sv : null;
    }

    public void setRBSelected(Object selected) {
        if (selected != null) {
            lastSelected = selected;
        }
    }

    private String currentRow = new String();

    public String getCurrentRow() {
        return tableRowGroup1.getRowKey().getRowId();
    }

    public void setCurrentRow(String s) {
        this.currentRow = s;
    }

    private RadioButton radioButton1 = new RadioButton();

    public RadioButton getRadioButton1() {
        return radioButton1;
    }

    public void setRadioButton1(RadioButton rb) {
        this.radioButton1 = rb;
    }

    private HiddenField rbSelection = new HiddenField();

    public HiddenField getRbSelection() {
        return rbSelection;
    }

    public void setRbSelection(HiddenField hf) {
        this.rbSelection = hf;
    }

    private StaticText txt_acc_title = new StaticText();

    public StaticText getTxt_acc_title() {
        return txt_acc_title;
    }

    public void setTxt_acc_title(StaticText st) {
        this.txt_acc_title = st;
    }

    private StaticText txt_acc_status = new StaticText();

    public StaticText getTxt_acc_status() {
        return txt_acc_status;
    }

    public void setTxt_acc_status(StaticText st) {
        this.txt_acc_status = st;
    }

    private StaticText txt_acc_type = new StaticText();

    public StaticText getTxt_acc_type() {
        return txt_acc_type;
    }

    public void setTxt_acc_type(StaticText st) {
        this.txt_acc_type = st;
    }

    private ImageComponent img_photo = new ImageComponent();

    public ImageComponent getImg_photo() {
        return img_photo;
    }

    public void setImg_photo(ImageComponent ic) {
        this.img_photo = ic;
    }

    private StaticText txt_cin = new StaticText();

    public StaticText getTxt_cin() {
        return txt_cin;
    }

    public void setTxt_cin(StaticText st) {
        this.txt_cin = st;
    }

    private StaticText txt_name = new StaticText();

    public StaticText getTxt_name() {
        return txt_name;
    }

    public void setTxt_name(StaticText st) {
        this.txt_name = st;
    }

    private StaticText txt_identification_no = new StaticText();

    public StaticText getTxt_identification_no() {
        return txt_identification_no;
    }

    public void setTxt_identification_no(StaticText st) {
        this.txt_identification_no = st;
    }

    private StaticText txt_contact_no = new StaticText();

    public StaticText getTxt_contact_no() {
        return txt_contact_no;
    }

    public void setTxt_contact_no(StaticText st) {
        this.txt_contact_no = st;
    }

    private ImageComponent img_signature = new ImageComponent();

    public ImageComponent getImg_signature() {
        return img_signature;
    }

    public void setImg_signature(ImageComponent ic) {
        this.img_signature = ic;
    }

    private TableRowGroup tableRowGroup1 = new TableRowGroup();

    public TableRowGroup getTableRowGroup1() {
        return tableRowGroup1;
    }

    public void setTableRowGroup1(TableRowGroup trg) {
        this.tableRowGroup1 = trg;
    }

    private TableColumn tableColumn1 = new TableColumn();

    public TableColumn getTableColumn1() {
        return tableColumn1;
    }

    public void setTableColumn1(TableColumn tc) {
        this.tableColumn1 = tc;
    }

    private PanelLayout lay_signatory_info = new PanelLayout();

    public PanelLayout getLay_signatory_info() {
        return lay_signatory_info;
    }

    public void setLay_signatory_info(PanelLayout pl) {
        this.lay_signatory_info = pl;
    }

    private PanelLayout lay_applicants_tbl1 = new PanelLayout();

    public PanelLayout getLay_applicants_tbl1() {
        return lay_applicants_tbl1;
    }

    public void setLay_applicants_tbl1(PanelLayout pl) {
        this.lay_applicants_tbl1 = pl;
    }

    private Table tbl_signatory_info = new Table();

    public Table getTbl_signatory_info() {
        return tbl_signatory_info;
    }

    public void setTbl_signatory_info(Table t) {
        this.tbl_signatory_info = t;
    }

    private StaticText txt_signatory_auth = new StaticText();

    public StaticText getTxt_signatory_auth() {
        return txt_signatory_auth;
    }

    public void setTxt_signatory_auth(StaticText st) {
        this.txt_signatory_auth = st;
    }

    private Button btn_ext = new Button();

    public Button getBtn_ext() {
        return btn_ext;
    }

    public void setBtn_ext(Button b) {
        this.btn_ext = b;
    }

    private Button btn_proceed1 = new Button();

    public Button getBtn_proceed1() {
        return btn_proceed1;
    }

    public void setBtn_proceed1(Button b) {
        this.btn_proceed1 = b;
    }

    private HiddenField acc_no = new HiddenField();

    public HiddenField getAcc_no() {
        return acc_no;
    }

    public void setAcc_no(HiddenField hf) {
        this.acc_no = hf;
    }

    private HiddenField tmp = new HiddenField();

    public HiddenField getTmp() {
        return tmp;
    }

    public void setTmp(HiddenField hf) {
        this.tmp = hf;
    }

    private HiddenField hold_acc_qry = new HiddenField();

    public HiddenField getHold_acc_qry() {
        return hold_acc_qry;
    }

    public void setHold_acc_qry(HiddenField hf) {
        this.hold_acc_qry = hf;
    }

    private Label lbl_signature = new Label();

    public Label getLbl_signature() {
        return lbl_signature;
    }

    public void setLbl_signature(Label l) {
        this.lbl_signature = l;
    }

    private Label lbl_man = new Label();

    public Label getLbl_man() {
        return lbl_man;
    }

    public void setLbl_man(Label l) {
        this.lbl_man = l;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public LoanAccountAuthentication() {
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
    private String sel;

    @Override
    public void init() {
        super.init();
        try {
            ExternalContext externalContext = getFacesContext().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            HttpSession session = request.getSession();
            if (session.isNew() || getSessionBean1().getUserId() == null) {
                this.getExternalContext().redirect("/CBA/faces/SessionTimeout.html");
            }
            sel = getSessionBean1().getPag_nav();
            String[] err = { "CM010", "CM007", "DW002", "DW003", "DW004", "DW005", "CI018", "DW006", "DW007", "DW008", "DW009", "DW010", "DW011", "DW038", "DW039", "DW040", "DW041", "DW042", "DW043", "DW047", "LW008", "LW009", "LW010", "LW011", "LW012", "LW013", "LW018", "LW019" };
            messages = getSessionBean1().getMessages(err);
            if (messages.containsKey("Exception")) {
                getMes().setText(messages.get("Exception"));
            }
            session = ((HttpServletRequest) getExternalContext().getRequest()).getSession();
            if (session.getAttribute("Update") != null) {
                mes.setText(session.getAttribute("Update").toString());
            }
            session.setAttribute("Update", "");
        } catch (Exception ex) {
            System.out.println("LoanAccountAuthentication, init(), " + getSessionBean1().getUserId() + " - " + ex);
            try {
                this.getExternalContext().redirect("/CBA/faces/InitError.jsp");
            } catch (IOException ioe) {
            }
        }
        try {
            _init();
        } catch (Exception e) {
            log("LoanAccountAuthentication Initialization Failure", e);
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
            if (sel.equals("update")) {
                if (hold_acc_qry.getText() != null) {
                    ddl_acc_noRowSet.setCommand(this.hold_acc_qry.getText().toString());
                    ddl_acc_noRowSet.execute();
                }
                sel = "0";
            }
            if (!sel.equals("0") || url_name.getText() == null) {
                url_name.setText(sel);
                ddl_acc_no.setDisabled(true);
                fld_verify_via.setDisabled(true);
                btn_proceed1.setDisabled(true);
                fld_verify_via.setText("");
                ddl_verify_viaRowSet.setCommand("Select \'-1\' as par_cod,\'Select\' as par_dsc\r\nUnion\r\nSELECT  ad_parameter.par_cod, ad_parameter.par_dsc  FROM ad_parameter WHERE ad_parameter.par_typ = \'accver\' order by par_cod asc");
                ddl_verify_viaRowSet.setTableName("ad_parameter");
                ddl_verify_viaRowSet.execute();
                getSessionBean1().getDw_tbl_acc_hold_parttRowSet().setCommand("select cin::varchar as cin,acc_title::varchar as acc_holder_name,acc_title::text as identification_no,cin as contact_no from dw_account where dw_account.acc_num=-1");
                getSessionBean1().getDw_tbl_acc_hold_parttRowSet().setTableName("dw_account");
                dw_tbl_acc_hold_partDataProvider.refresh();
                getSessionBean1().getDw_tbl_signatory_infoRowSet().setCommand("select cin::varchar as cin,acc_title::varchar as signatory_name,acc_title::text as signatory_img from dw_account where dw_account.acc_num=-1");
                getSessionBean1().getDw_tbl_signatory_infoRowSet().setTableName("dw_account");
                this.dw_tbl_signatory_infoDataProvider.refresh();
                this.setCurrentRow("-1");
                FacesContext fc = FacesContext.getCurrentInstance();
                getSessionBean1().setDw_page_nav((String) fc.getExternalContext().getRequestParameterMap().get("id"));
                url_name.setText(getSessionBean1().getDw_page_nav());
                getSessionBean1().setDw_page_nav("0");
                if (url_name.getText().equals("CD") || url_name.getText().equals("CW")) {
                    boolean flag = true;
                    if (((Vector) getSessionBean1().executeSQLSelect("select cas_box_id from vl_cash_box where off_usr_id = '" + getSessionBean1().getUserId() + "'")).size() > 0) {
                        if (((Vector) getSessionBean1().executeSQLSelect("select cas_box_open from vl_cash_box where off_usr_id = '" + getSessionBean1().getUserId() + "'").get(0)).get(0).toString().equals("0")) {
                            getMes().setText(messages.get("DW042"));
                            flag = false;
                        }
                    }
                    if (flag && ((Vector) getSessionBean1().executeSQLSelect("select cas_box_id from vl_cash_box where off_usr_id = '" + getSessionBean1().getUserId() + "'")).size() == 0) {
                        getMes().setText(messages.get("DW043"));
                        flag = false;
                    }
                    if (flag && ((Vector) getSessionBean1().executeSQLSelect("select (select to_char(now(), 'HH24:MI:SS')) >= (select acc_time_fr from vl_cash_box where off_usr_id = '" + getSessionBean1().getUserId() + "') and (select to_char(now(), 'HH24:MI:SS')) <= (select acc_time_to from vl_cash_box where off_usr_id = '" + getSessionBean1().getUserId() + "')").get(0)).get(0).toString().equals("false")) {
                        Vector vect = getSessionBean1().executeSQLSelect("select acc_time_fr,acc_time_to from vl_cash_box where off_usr_id = '" + getSessionBean1().getUserId() + "'");
                        getMes().setText("Teller Operations can only be perfomed between " + ((Vector) vect.get(0)).get(0).toString() + " to " + ((Vector) vect.get(0)).get(1).toString() + " for this user");
                        flag = false;
                    }
                    if (!flag) {
                        btn_authenticate.setDisabled(true);
                        btn_clear.setDisabled(true);
                        ddl_verify_via.setDisabled(true);
                    }
                }
            }
            getSessionBean1().setPag_nav("0");
            lbl_man.setText("Loan Account Authentication For " + (getLoan().retrieve_req_type(url_name.getText().toString()).toString().split(getLoan().split_string_val))[2]);
        } catch (Exception e) {
            getMes().setText("Error in prerender method " + e);
            System.out.print(e);
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

    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    protected AccountOpening getAccountOpening() {
        return (AccountOpening) getBean("AccountOpening");
    }

    protected Loan getLoan() {
        return (Loan) getBean("Loan");
    }

    public String btn_clear_action() {
        this.fld_verify_via.setText("");
        this.ddl_verify_via.setSelected("-1");
        this.fld_verify_via.setDisabled(true);
        clear();
        return null;
    }

    public String btn_submit_action() {
        return null;
    }

    public String btn_ok_action() {
        return null;
    }

    public String btn_proceed1_action() {
        try {
            if (((Vector) getSessionBean1().executeSQLSelect("select pro_type from dw_account where acc_num='" + getSessionBean1().getUpd_val().toString() + "'").get(0)).get(0).toString().equals("6")) {
                getSessionBean1().setUpd_val4(((Vector) getSessionBean1().executeSQLSelect("select link_acc from lw_app_group where acc_num='" + getSessionBean1().getUpd_val().toString() + "' and cin='" + getDw_tbl_acc_hold_partDataProvider().getValue("cin", getDw_tbl_acc_hold_partDataProvider().getRowKey(getRbSelection().getText().toString())).toString() + "'").get(0)).get(0).toString());
            } else {
                getSessionBean1().setUpd_val4(((Vector) getSessionBean1().executeSQLSelect("select link_acc from lw_application where acc_num='" + getSessionBean1().getUpd_val().toString() + "'").get(0)).get(0).toString());
            }
            if (url_name.getText().toString().equals("LR") && ((Vector) getSessionBean1().executeSQLSelect("select lw_loan_schedule_v.req_id from lw_loan_schedule_v,ad_request where lw_loan_schedule_v.req_id = ad_request.req_id and ad_request.req_type = '9010' and lw_loan_schedule_v.acc_num = '" + getSessionBean1().getUpd_val().toString() + "' and lw_loan_schedule_v.lnk_acc_num='" + getSessionBean1().getUpd_val4().toString() + "'")).size() > 0) {
                getMes().setText(messages.get("LW010") + " " + getSessionBean1().getUpd_val().toString());
                sel = "update";
                return null;
            } else if (url_name.getText().toString().equals("LR") && ((Vector) getSessionBean1().executeSQLSelect("select lw_loan_schedule_v.req_id from lw_loan_schedule_v,ad_request where lw_loan_schedule_v.req_id = ad_request.req_id and ad_request.req_type = '9011' and lw_loan_schedule_v.acc_num = '" + getSessionBean1().getUpd_val().toString() + "' and lw_loan_schedule_v.lnk_acc_num='" + getSessionBean1().getUpd_val4().toString() + "'")).size() > 0) {
                getMes().setText(messages.get("LW011") + " " + getSessionBean1().getUpd_val().toString());
                sel = "update";
                return null;
            } else if (url_name.getText().toString().equals("LP") && ((Vector) getSessionBean1().executeSQLSelect("select lw_loan_schedule_v.req_id from lw_loan_schedule_v,ad_request where lw_loan_schedule_v.req_id = ad_request.req_id and ad_request.req_type = '9010' and lw_loan_schedule_v.acc_num = '" + getSessionBean1().getUpd_val().toString() + "' and lw_loan_schedule_v.lnk_acc_num='" + getSessionBean1().getUpd_val4().toString() + "'")).size() > 0) {
                getMes().setText(messages.get("LW010") + " " + getSessionBean1().getUpd_val().toString());
                sel = "update";
                return null;
            } else if (url_name.getText().toString().equals("LP") && ((Vector) getSessionBean1().executeSQLSelect("select lw_loan_schedule_v.req_id from lw_loan_schedule_v,ad_request where lw_loan_schedule_v.req_id = ad_request.req_id and ad_request.req_type = '9011' and lw_loan_schedule_v.acc_num = '" + getSessionBean1().getUpd_val().toString() + "' and lw_loan_schedule_v.lnk_acc_num='" + getSessionBean1().getUpd_val4().toString() + "'")).size() > 0) {
                getMes().setText(messages.get("LW011") + " " + getSessionBean1().getUpd_val().toString());
                sel = "update";
                return null;
            } else if (url_name.getText().toString().equals("LP") && ((Vector) getSessionBean1().executeSQLSelect("select pre_allow from pr_loan where pro_id=(select pro_id from dw_account where acc_num='" + getSessionBean1().getUpd_val().toString() + "')").get(0)).get(0).toString().equals("0")) {
                getMes().setText(messages.get("LW008"));
                sel = "update";
                return null;
            } else if (url_name.getText().toString().equals("LP") && ((Vector) getSessionBean1().executeSQLSelect("select pre_allow from pr_loan where pro_id=(select pro_id from dw_account where acc_num='" + getSessionBean1().getUpd_val().toString() + "')").get(0)).get(0).toString().equals("1") && ((Vector) getSessionBean1().executeSQLSelect("select case when pre_min_ten_rem=null then false else pre_min_ten_rem>=(select count(*) from lw_loan_schedule where acc_num='" + getSessionBean1().getUpd_val().toString() + "' and lnk_acc_num='" + getSessionBean1().getUpd_val4().toString() + "' and status in('1','3')) end from pr_loan where pro_id=(select pro_id from dw_account where acc_num='" + getSessionBean1().getUpd_val().toString() + "')").get(0)).get(0).equals(false)) {
                getMes().setText(messages.get("LW009"));
                sel = "update";
                return null;
            } else if (url_name.getText().toString().equals("LP") && ((Vector) getSessionBean1().executeSQLSelect("select acc_sts,ad_parameter.par_dsc from dw_account,ad_parameter where acc_num='" + getDw_tbl_acc_hold_partDataProvider().getValue("cin", getDw_tbl_acc_hold_partDataProvider().getRowKey(getRbSelection().getText().toString())).toString() + "' and acc_sts not in(4,6) and ad_parameter.par_typ='acc_sts' and ad_parameter.par_cod=dw_account.acc_sts")).size() > 0) {
                getMes().setText(messages.get("DW002") + ((Vector) getSessionBean1().executeSQLSelect("select ad_parameter.par_dsc from dw_account,ad_parameter where acc_num='" + getDw_tbl_acc_hold_partDataProvider().getValue("cin", getDw_tbl_acc_hold_partDataProvider().getRowKey(getRbSelection().getText().toString())).toString() + "' and acc_sts not in(4,6) and ad_parameter.par_typ='acc_sts' and ad_parameter.par_cod=dw_account.acc_sts").get(0)).get(0).toString());
                sel = "update";
                return null;
            } else if ((url_name.getText().toString().equals("LP") || url_name.getText().toString().equals("LR")) && ((Vector) getSessionBean1().executeSQLSelect("select acc_sts,ad_parameter.par_dsc from dw_account,ad_parameter where acc_num='" + getSessionBean1().getUpd_val().toString() + "' and lnk_acc_num='" + getSessionBean1().getUpd_val4().toString() + "'")).size() > 0) {
                getMes().setText("The loan disbursment has not been yet done for this particular group member");
                sel = "update";
                return null;
            } else if ((url_name.getText().toString().equals("LP") || url_name.getText().toString().equals("LR")) && Integer.parseInt(((Vector) getSessionBean1().executeSQLSelect("select count(*) from lw_loan_schedule where acc_num='" + getSessionBean1().getUpd_val().toString() + "' and lnk_acc_num='" + getSessionBean1().getUpd_val4().toString() + "' and status in ('1','3')").get(0)).get(0).toString()) == 0) {
                if (((Vector) getSessionBean1().executeSQLSelect("select pro_type from dw_account where acc_num='" + getSessionBean1().getUpd_val().toString() + "'").get(0)).get(0).toString().equals("6")) {
                    getMes().setText("Installments has been completed for the selected group member");
                } else {
                    getMes().setText("Installments has been completed for this Account No.");
                }
                sel = "update";
                return null;
            } else if ((url_name.getText().toString().equals("LRe") || url_name.getText().toString().equals("LP") || url_name.getText().toString().equals("LR")) && ((Vector) getSessionBean1().executeSQLSelect("select lw_loan_schedule_v.req_id from lw_loan_schedule_v,ad_request where lw_loan_schedule_v.req_id = ad_request.req_id and ad_request.req_type = '9012' and lw_loan_schedule_v.acc_num = '" + getSessionBean1().getUpd_val().toString() + "' and lw_loan_schedule_v.lnk_acc_num='" + getSessionBean1().getUpd_val4().toString() + "'")).size() > 0) {
                getMes().setText(messages.get("LW012") + " " + getSessionBean1().getUpd_val().toString());
                sel = "update";
                return null;
            } else if (url_name.getText().toString().equals("LRe") && ((Vector) getSessionBean1().executeSQLSelect("select lw_loan_schedule_v.req_id from lw_loan_schedule_v,ad_request where lw_loan_schedule_v.req_id = ad_request.req_id and ad_request.req_type = '9010' and lw_loan_schedule_v.acc_num = '" + getSessionBean1().getUpd_val().toString() + "' and lw_loan_schedule_v.lnk_acc_num='" + getSessionBean1().getUpd_val4().toString() + "'")).size() > 0) {
                getMes().setText(messages.get("LW010") + " " + getSessionBean1().getUpd_val().toString());
                sel = "update";
                return null;
            } else if (url_name.getText().toString().equals("LRe") && ((Vector) getSessionBean1().executeSQLSelect("select lw_loan_schedule_v.req_id from lw_loan_schedule_v,ad_request where lw_loan_schedule_v.req_id = ad_request.req_id and ad_request.req_type = '9011' and lw_loan_schedule_v.acc_num = '" + getSessionBean1().getUpd_val().toString() + "' and lw_loan_schedule_v.lnk_acc_num='" + getSessionBean1().getUpd_val4().toString() + "'")).size() > 0) {
                getMes().setText(messages.get("LW011") + " " + getSessionBean1().getUpd_val().toString());
                sel = "update";
                return null;
            } else if (url_name.getText().toString().equals("LRe") && ((Vector) getSessionBean1().executeSQLSelect("select resch_allow from pr_loan where pro_id=(select pro_id from dw_account where acc_num='" + getSessionBean1().getUpd_val().toString() + "')").get(0)).get(0).toString().equals("0")) {
                getMes().setText(messages.get("LW018") + " " + getSessionBean1().getUpd_val().toString());
                sel = "update";
                return null;
            } else if (url_name.getText().toString().equals("LRe") && ((Vector) getSessionBean1().executeSQLSelect("select resch_allow from pr_loan where pro_id=(select pro_id from dw_account where acc_num='" + getSessionBean1().getUpd_val().toString() + "')").get(0)).get(0).toString().equals("1") && ((Vector) getSessionBean1().executeSQLSelect("select case when resch_min_ten_rem=null then false else resch_min_ten_rem>=(select count(*) from lw_loan_schedule where acc_num='" + getSessionBean1().getUpd_val().toString() + "' and lnk_acc_num='" + getSessionBean1().getUpd_val4().toString() + "' and status in('1','3')) end from pr_loan where pro_id=(select pro_id from dw_account where acc_num='" + getSessionBean1().getUpd_val().toString() + "')").get(0)).get(0).equals(false)) {
                getMes().setText(messages.get("LW019"));
                sel = "update";
                return null;
            } else {
                getSessionBean1().setPag_nav("update");
                System.out.print(getDw_tbl_acc_hold_partDataProvider().getValue("cin", getDw_tbl_acc_hold_partDataProvider().getRowKey(getRbSelection().getText().toString())).toString());
                getSessionBean1().setUpd_val(this.ddl_acc_no.isDisabled() ? getSessionBean1().getUpd_val().toString() : this.ddl_acc_no.getSelected().toString());
                getSessionBean1().setUpd_val1("0");
                getSessionBean1().setUpd_val2("0");
                getSessionBean1().setUpd_val3("0");
                getSessionBean1().setUpd_val5("0");
                getExternalContext().redirect("/CBA/faces/" + (getLoan().retrieve_req_type(url_name.getText().toString()).toString().split(getAccountOpening().split_string_val))[1] + ".jsp");
            }
        } catch (Exception e) {
            getMes().setText("Error in btn_proceed_action method " + e);
            System.out.print(e);
        }
        return null;
    }

    public void ddl_verify_via_processValueChange(ValueChangeEvent event) {
        if (!ddl_verify_via.getSelected().toString().equals("-1")) {
            fld_verify_via.setDisabled(false);
            this.fld_verify_via.setText("");
        } else {
            fld_verify_via.setDisabled(true);
            this.fld_verify_via.setText("");
        }
        if (ddl_verify_via.getSelected().toString().equals("0")) {
            fld_verify_via.setMaxLength(9);
        } else {
            fld_verify_via.setMaxLength(50);
        }
    }

    public String btn_authenticate_action() {
        try {
            fill_data_for_validation_panel_acc_dropdown("", "");
            if (ddl_acc_noDataProvider.getRowCount() != 0) {
                if (check_for_disbursement(ddl_acc_noDataProvider.getRowCount()) == true) {
                    if (ddl_acc_noDataProvider.getRowCount() > 1) {
                        this.ddl_acc_no.setDisabled(false);
                    } else {
                        this.ddl_acc_no.setDisabled(true);
                    }
                    setRBSelected("0");
                    getRbSelection().setText("0");
                    if (ddl_acc_noDataProvider.getRowCount() > 0) {
                        System.out.print(ddl_acc_noRowSet.getCommand());
                        fill_data_for_validation_panel_grid(ddl_acc_noRowSet.getString(1));
                    }
                    fill_data_for_validation_panel_text(ddl_acc_noRowSet.getString(1));
                    this.acc_no.setText(ddl_acc_noRowSet.getString(1));
                    fill_data_for_validation_panel_selection_on_CIN_text(getDw_tbl_acc_hold_partDataProvider().getValue("cin", getDw_tbl_acc_hold_partDataProvider().getRowKey(getRbSelection().getText().toString())).toString());
                    getSessionBean1().setUpd_val(ddl_acc_noRowSet.getString(1));
                    if (txt_acc_type.getText().toString().equals("Corporate")) {
                        img_photo.setVisible(false);
                        img_signature.setVisible(false);
                        lbl_signature.setVisible(false);
                    } else {
                        img_photo.setVisible(true);
                        img_signature.setVisible(true);
                        lbl_signature.setVisible(true);
                    }
                    mes.setText("");
                    this.ddl_verify_via.setDisabled(true);
                    btn_proceed1.setDisabled(false);
                } else {
                    empty_acc_num_RowSet();
                    clear();
                }
            } else {
                empty_acc_num_RowSet();
                clear();
                mes.setText(messages.get("CM007"));
            }
        } catch (Exception e) {
            getMes().setText("Error in btn_authenticate_action method " + e);
            System.out.print(e);
        }
        return null;
    }

    void empty_acc_num_RowSet() {
        try {
            ddl_acc_noRowSet.setCommand("Select 'Select'::varchar as acc_num,0::numeric as acc_sts,''::varchar as org_cod from dw_account order by acc_num desc");
            ddl_acc_noRowSet.setTableName("dw_account");
            ddl_acc_noRowSet.execute();
        } catch (Exception e) {
            getMes().setText("Error in empty_acc_num_RowSet method " + e);
            System.out.print(e);
        }
    }

    void fill_data_for_validation_panel_acc_dropdown(String src_type, String val) {
        try {
            if (ddl_verify_via.getSelected().toString().equals("0")) {
                ddl_acc_noRowSet.setCommand("select distinct dw_account.acc_num as acc_num,dw_account.acc_sts,dw_account.org_cod from dw_account inner join lw_app_group on dw_account.acc_num=lw_app_group.acc_num where lw_app_group.cin = '" + fld_verify_via.getText().toString() + "' union select distinct lw_application.acc_num as acc_num,dw_account.acc_sts,dw_account.org_cod from lw_application inner join dw_account on lw_application.acc_num = dw_account.acc_num where lw_application.cin ='" + fld_verify_via.getText().toString() + "'");
            } else {
                ddl_acc_noRowSet.setCommand("select distinct dw_account.acc_num as acc_num,dw_account.acc_sts,dw_account.org_cod from dw_account where dw_account.acc_num = '" + fld_verify_via.getText().toString() + "'");
            }
            ddl_acc_noRowSet.setTableName("dw_account");
            ddl_acc_noRowSet.execute();
        } catch (Exception e) {
            getMes().setText("Error in fill_data_for_validation_panel_acc_dropdown method " + e);
            System.out.print(e);
        }
    }

    void fill_data_for_validation_panel_grid(String val) {
        try {
            String condition = "";
            if (url_name.getText().equals("LR") || url_name.getText().equals("LP") || url_name.getText().equals("LRe")) {
                condition = " not in (1,2,3,5,6)";
            } else {
                condition = " in (4)";
            }
            String acc_type = ((Vector) getSessionBean1().executeSQLSelect("select dw_account.pro_type from dw_account,ad_parameter where dw_account.acc_num = '" + val + "' and dw_account.acc_sts " + condition + " and ad_parameter.par_typ = 'pro_loan' and ad_parameter.par_cod = dw_account.pro_type").get(0)).get(0).toString();
            String qry = "";
            if (acc_type.equals("6")) {
                qry = "select distinct dw_mul_acc.cin,case when (select cif_type from ci_cif where cin = dw_mul_acc.cin) = 0 then ci_individual.f_name || ' ' || ci_individual.l_name " + " else ci_corporate.cmp_name end " + " as acc_holder_name, " + " case when (select cif_type from ci_cif where cin = dw_mul_acc.cin) = 0 then  (select par_dsc from ad_parameter where par_typ ='Indiv_ID' and par_cod  = (select ci_individual.pid_type from ci_individual where cin = dw_mul_acc.cin)) || ' - ' || ci_individual.pid_num   " + " else (select par_dsc from ad_parameter where par_typ ='Corp_ID' and par_cod  = (select ci_corporate.tid_type from ci_corporate where cin = dw_mul_acc.cin)) || ' - ' || ci_corporate.tid_num end  " + " as identification_no,case when (select cif_type from ci_cif where cin = dw_mul_acc.cin) = 0  then ci_individual.hom_ph else ci_corporate.bus_ph end as contact_no " + " from lw_app_group as dw_mul_acc,ci_individual,ci_corporate where " + " case when (select cif_type from ci_cif where cin = dw_mul_acc.cin) = 0 then " + "  dw_mul_acc.cin = ci_individual.cin else dw_mul_acc.cin = ci_corporate.cin end and dw_mul_acc.acc_num = '" + val + "'";
            } else {
                qry = "select distinct dw_account.cin,case when (select cif_type from ci_cif where cin = dw_account.cin) = 0 then ci_individual.f_name || ' ' || ci_individual.l_name " + " else ci_corporate.cmp_name end " + " as acc_holder_name, " + " case when (select cif_type from ci_cif where cin = dw_account.cin) = 0 then  (select par_dsc from ad_parameter where par_typ ='Indiv_ID' and par_cod  = (select ci_individual.pid_type from ci_individual where cin = dw_account.cin)) || ' - ' || ci_individual.pid_num " + " else (select par_dsc from ad_parameter where par_typ ='Corp_ID' and par_cod  = (select ci_corporate.tid_type from ci_corporate where cin = dw_account.cin)) || ' - ' || ci_corporate.tid_num end  " + " as identification_no,case when (select cif_type from ci_cif where cin = dw_account.cin) = 0  then ci_individual.hom_ph else ci_corporate.bus_ph end as contact_no " + " from dw_account,ci_individual,ci_corporate where " + " case when (select cif_type from ci_cif where cin = dw_account.cin) = 0 then " + " dw_account.cin = ci_individual.cin else dw_account.cin = ci_corporate.cin end and dw_account.acc_num = '" + val + "'";
            }
            getSessionBean1().getDw_tbl_acc_hold_parttRowSet().setCommand(qry);
            getSessionBean1().getDw_tbl_acc_hold_parttRowSet().execute();
            if (acc_type.equals("3")) {
                lay_signatory_info.setVisible(true);
                fill_data_for_signatory_info(val);
            } else {
                lay_signatory_info.setVisible(false);
            }
        } catch (Exception e) {
            getMes().setText("Error in fill_data_for_validation_panel_grid method " + e);
            System.out.print(e);
        }
    }

    void fill_data_for_validation_panel_text(String val) {
        try {
            String condition = "";
            if (url_name.getText().equals("LR") || url_name.getText().equals("LP") || url_name.getText().equals("LRe")) {
                condition = " not in (1,2,3,5,6)";
            } else {
                condition = "in (4)";
            }
            Vector vect = getSessionBean1().executeSQLSelect("select dw_account.acc_title,ad_parameter.par_dsc,ad_parameter.par_dsc,(select par_dsc from ad_parameter where par_cod = (select dw_account.acc_sts from dw_account where acc_num ='" + val + "') and par_typ = 'acc_sts') as acc_sts,''::varchar as sign_auth from dw_account,ad_parameter where dw_account.acc_num = '" + val + "' and dw_account.acc_sts =4 and ad_parameter.par_typ='pro_loan' and ad_parameter.par_cod=dw_account.pro_type");
            txt_acc_title.setText(((Vector) vect.get(0)).get(0) == null ? "" : ((Vector) vect.get(0)).get(0).toString());
            txt_acc_type.setText(((Vector) vect.get(0)).get(1).toString());
            txt_acc_status.setText(((Vector) vect.get(0)).get(3).toString());
            txt_signatory_auth.setText("");
        } catch (Exception e) {
            getMes().setText("Error in fill_data_for_validation_panel_text method " + e);
            System.out.print(e);
        }
    }

    void fill_data_for_validation_panel_selection_on_CIN_text(String val) {
        try {
            String aRowId = getRbSelection().getText().toString();
            RowKey aRowKey = this.getDw_tbl_acc_hold_partDataProvider().getRowKey(aRowId);
            String qry = "select distinct '" + getDw_tbl_acc_hold_partDataProvider().getValue("cin", aRowKey).toString() + "' as cin, '" + getDw_tbl_acc_hold_partDataProvider().getValue("acc_holder_name", aRowKey).toString() + "' as name, '" + getDw_tbl_acc_hold_partDataProvider().getValue("identification_no", aRowKey).toString() + "' as identification_no,case when (select cif_type from ci_cif where cin = '" + val + "') = 0  then (select ci_individual.hom_ph from ci_individual where cin = '" + val + "') else (select ci_corporate.bus_ph from ci_corporate where cin = '" + val + "') end as contact_no,(select ci_images.pic_link from ci_images where cin = '" + val + "'),(select sign_lnk from dw_account where acc_num = '" + this.getAcc_no().getText().toString() + "') from ci_cif,ci_individual,ci_corporate,ci_images,dw_account where ci_cif.cin = ci_individual.cin and ci_cif.cin = ci_images.cin and dw_account.acc_num = '" + this.getAcc_no().getText().toString() + "'";
            Vector vect1 = getSessionBean1().executeSQLSelect(qry);
            this.txt_cin.setText(((Vector) vect1.get(0)).get(0).toString());
            this.txt_name.setText(((Vector) vect1.get(0)).get(1).toString());
            this.txt_identification_no.setText(((Vector) vect1.get(0)).get(2).toString());
            this.txt_contact_no.setText(((Vector) vect1.get(0)).get(3).toString());
            retrieve_image(((Vector) vect1.get(0)).get(4).toString(), "img_photo");
            retrieve_image(((Vector) vect1.get(0)).get(5).toString(), "img_signature");
        } catch (Exception e) {
            getMes().setText("Error in fill_data_for_validation_panel_selection_on_CIN_text method " + e);
            System.out.print(e);
        }
    }

    void fill_data_for_signatory_info(String val) {
        try {
            String qry = "select distinct dw_mul_acc.cin,case when (select cif_type from ci_cif where cin = dw_mul_acc.cin) = 0 then ci_individual.f_name || ' ' || ci_individual.l_name " + "  else ci_corporate.cmp_name end " + "  as signatory_name, " + "  dw_mul_acc.sign_lnk  as signatory_img " + "  from dw_mul_acc,ci_individual,ci_corporate where " + "  case when (select cif_type from ci_cif where cin = dw_mul_acc.cin) = 0 then " + "   dw_mul_acc.cin = ci_individual.cin else dw_mul_acc.cin = ci_corporate.cin end and dw_mul_acc.acc_num = '" + val + "'";
            getSessionBean1().getDw_tbl_signatory_infoRowSet().setCommand(qry);
            getSessionBean1().getDw_tbl_signatory_infoRowSet().execute();
        } catch (Exception e) {
            getMes().setText("Error in fill_data_for_signatory_info method " + e);
            System.out.print(e);
        }
    }

    void retrieve_image(String pic_link, String type) {
        try {
            String uploadDirectory = ((Vector) getSessionBean1().executeSQLSelect("SELECT par_dsc FROM ad_parameter WHERE par_typ = 'img_dir' AND par_cod = '0' and par_sts = '1'").get(0)).get(0).toString();
            ServletContext theApplicationsServletContext = (ServletContext) this.getExternalContext().getContext();
            String realPath = theApplicationsServletContext.getRealPath(uploadDirectory);
            String nophoto = realPath + File.separatorChar + "nophoto.jpg";
            File imgfile = null;
            if (type.equals("img_signature")) {
                pic_link = pic_link.substring(19, pic_link.length());
            }
            imgfile = new File(realPath + File.separatorChar + pic_link);
            if (imgfile.exists()) {
                if (type.equals("img_photo")) {
                    getImg_photo().setUrl(uploadDirectory + pic_link);
                } else {
                    getImg_signature().setUrl(uploadDirectory + pic_link);
                }
            } else {
                if (type.equals("img_photo")) {
                    getImg_photo().setUrl(nophoto);
                    getImg_photo().setVisible(true);
                } else {
                    getImg_signature().setUrl(nophoto);
                    getImg_signature().setVisible(true);
                }
            }
        } catch (Exception e) {
            getMes().setText("Error in retrieve_image method " + e);
            System.out.print(e);
        }
    }

    void clear() {
        try {
            this.ddl_verify_via.setDisabled(false);
            btn_proceed1.setDisabled(true);
            empty_acc_num_RowSet();
            getSessionBean1().getDw_tbl_acc_hold_parttRowSet().setCommand("select cin::varchar as cin,acc_title::varchar as acc_holder_name,acc_title::text as identification_no,cin as contact_no from dw_account where dw_account.acc_num=-1");
            getSessionBean1().getDw_tbl_acc_hold_parttRowSet().setTableName("dw_account");
            dw_tbl_acc_hold_partDataProvider.refresh();
            getSessionBean1().getDw_tbl_signatory_infoRowSet().setCommand("select cin::varchar as cin,acc_title::varchar as signatory_name,acc_title::text as signatory_img from dw_account where dw_account.acc_num=-1");
            getSessionBean1().getDw_tbl_signatory_infoRowSet().setTableName("dw_account");
            this.dw_tbl_signatory_infoDataProvider.refresh();
            ddl_acc_no.setDisabled(true);
            txt_acc_status.setText("");
            txt_acc_title.setText("");
            txt_acc_type.setText("");
            txt_cin.setText("");
            txt_contact_no.setText("");
            txt_identification_no.setText("");
            txt_name.setText("");
            txt_signatory_auth.setText("");
            img_signature.setUrl("");
            img_photo.setUrl("");
            img_signature.setVisible(false);
            img_photo.setVisible(false);
            lay_signatory_info.setVisible(false);
        } catch (Exception e) {
            getMes().setText("Error in clear method " + e);
            System.out.print(e);
        }
    }

    boolean check_for_disbursement(int no_of_rec) {
        String err_text = "";
        try {
            for (int i = 1; i <= no_of_rec; i++) {
                Vector getData = null;
                getData = getSessionBean1().executeSQLSelect("select distinct dw_account.acc_sts,a.par_dsc as acc_sts_dsc,dw_account.acc_type,dw_account.pro_type,c.par_dsc as pro_type_dsc,dw_account.org_cod,dw_account.acc_num,dw_account.amount from dw_account,ad_parameter a,ad_parameter c where dw_account.acc_num = '" + ddl_acc_noRowSet.getString("acc_num") + "' and a.par_typ ='acc_sts' and a.par_cod =  dw_account.acc_sts and c.par_typ ='pro_loan' and c.par_cod =  dw_account.pro_type");
                if (getData.size() > 0) {
                    if (((Vector) getData.get(0)).get(0).toString().equals("1") || ((Vector) getData.get(0)).get(0).toString().equals("2") || ((Vector) getData.get(0)).get(0).toString().equals("3") || ((Vector) getData.get(0)).get(0).toString().equals("5") || ((Vector) getData.get(0)).get(0).toString().equals("6")) {
                        err_text = err_text + messages.get("DW002") + " " + ((Vector) getData.get(0)).get(1).toString() + " for Account No: " + ((Vector) getData.get(0)).get(6).toString() + " " + "\n";
                    } else if (!((Vector) getData.get(0)).get(5).toString().equals(getSessionBean1().getOrgaCode().toString())) {
                        err_text = err_text + " Account No: " + ((Vector) getData.get(0)).get(6).toString() + messages.get("DW004") + "\n";
                    } else if (Double.parseDouble(((Vector) getData.get(0)).get(7).toString()) >= 0) {
                        err_text = err_text + messages.get("LW013") + "\n";
                    }
                    if (no_of_rec != i) {
                        ddl_acc_noDataProvider.cursorNext();
                    }
                } else {
                    err_text = err_text + "The Account No. is not a loan account" + "\n";
                }
            }
            String qry = "";
            if (ddl_verify_via.getSelected().toString().equals("0")) {
                qry = "select distinct dw_account.acc_num as acc_num,dw_account.acc_sts,dw_account.org_cod from dw_account inner join lw_app_group on dw_account.acc_num=lw_app_group.acc_num where lw_app_group.cin = '" + fld_verify_via.getText().toString() + "' and dw_account.org_cod='" + getSessionBean1().getOrgaCode() + "' and dw_account.acc_sts not in (1,2,3,5,6) and dw_account.amount<0 union select distinct dw_account.acc_num as acc_num,dw_account.acc_sts,dw_account.org_cod from dw_account inner join lw_application on dw_account.acc_num=lw_application.acc_num where lw_application.cin = '" + fld_verify_via.getText().toString() + "' and dw_account.org_cod='" + getSessionBean1().getOrgaCode() + "' and dw_account.acc_sts not in (1,2,3,5,6) and dw_account.amount<0 order by acc_num asc";
            } else {
                qry = "select distinct dw_account.acc_num as acc_num,dw_account.acc_sts,dw_account.org_cod from dw_account where dw_account.acc_num = '" + fld_verify_via.getText().toString() + "' and dw_account.org_cod='" + getSessionBean1().getOrgaCode() + "' and dw_account.acc_sts not in (1,2,3,5,6) and dw_account.amount<0 order by acc_num asc";
            }
            ddl_acc_noRowSet.setCommand(qry);
            this.hold_acc_qry.setText(qry);
            ddl_acc_noRowSet.setTableName("dw_account");
            ddl_acc_noRowSet.execute();
            if (ddl_acc_noDataProvider.getRowCount() > 0) {
                err_text = "";
            } else {
                mes.setText(err_text);
            }
        } catch (Exception e) {
            getMes().setText("Error in check_for_disbursement method " + e);
            System.out.print(e);
        }
        if (err_text.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public void radioButton1_processValueChange(ValueChangeEvent event) {
        lastSelected = (String) RadioButton.getSelected("radioGroup");
        getRbSelection().setText((String) lastSelected);
        try {
            getAcc_no().setText(this.ddl_acc_no.getSelected().toString());
            fill_data_for_validation_panel_selection_on_CIN_text(getDw_tbl_acc_hold_partDataProvider().getValue("cin", getDw_tbl_acc_hold_partDataProvider().getRowKey(getRbSelection().getText().toString())).toString());
        } catch (Exception e) {
            getMes().setText("Error in radioButton1_processValueChange method " + e);
            System.out.print(e);
        }
    }

    public void ddl_acc_no_processValueChange(ValueChangeEvent event) {
        try {
            getRbSelection().setText("0");
            this.acc_no.setText(ddl_acc_no.getSelected().toString());
            fill_data_for_validation_panel_text(ddl_acc_no.getSelected().toString());
            fill_data_for_validation_panel_grid(ddl_acc_no.getSelected().toString());
            fill_data_for_validation_panel_selection_on_CIN_text(getDw_tbl_acc_hold_partDataProvider().getValue("cin", getDw_tbl_acc_hold_partDataProvider().getRowKey(getRbSelection().getText().toString())).toString());
            getSessionBean1().setUpd_val(this.ddl_acc_no.getSelected().toString());
            setRBSelected(getRbSelection().getText());
        } catch (Exception e) {
            getMes().setText("Error in ddl_acc_no_processValueChange method " + e);
            System.out.print(e);
        }
    }

    public String btn_ext_action() {
        try {
            this.getExternalContext().redirect("/CBA/faces/DASHBOARD.jsp");
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public void tmp_processValueChange(ValueChangeEvent event) {
        try {
            getSessionBean1().setUpd_val(tmp.getText().toString());
            getSessionBean1().setUpd_val2("*");
        } catch (Exception ex) {
            System.out.println("LoanAccountAuthentication, hyperlink1_action(), " + getSessionBean1().getUserId() + " - " + ex);
        }
    }
}
