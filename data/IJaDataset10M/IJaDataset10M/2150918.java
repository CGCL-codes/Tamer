package cba;

import com.sun.data.provider.FilterCriteria;
import com.sun.data.provider.RowKey;
import com.sun.data.provider.impl.CachedRowSetDataProvider;
import com.sun.data.provider.impl.RegexFilterCriteria;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.Link;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.component.PanelLayout;
import com.sun.webui.jsf.component.RadioButton;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TableColumn;
import com.sun.webui.jsf.component.TableRowGroup;
import com.sun.webui.jsf.component.TextArea;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.model.SingleSelectOptionsList;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
 * @author Javaria Gull
 */
public class Denomination extends AbstractPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
        ddl_srcDefaultOptions.setOptions(new com.sun.webui.jsf.model.Option[] { new com.sun.webui.jsf.model.Option("*", "All Records         "), new com.sun.webui.jsf.model.Option("den_id", "Denomination Amount"), new com.sun.webui.jsf.model.Option("den_atm", "Used in ATM"), new com.sun.webui.jsf.model.Option("den_sts", "Status") });
        vl_denominationDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.vl_denominationRowSet2}"));
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

    private Form frm_rol = new Form();

    public Form getFrm_rol() {
        return frm_rol;
    }

    public void setFrm_rol(Form f) {
        this.frm_rol = f;
    }

    private TextField fld_src = new TextField();

    public TextField getFld_src() {
        return fld_src;
    }

    public void setFld_src(TextField tf) {
        this.fld_src = tf;
    }

    private Button btn_add = new Button();

    public Button getBtn_add() {
        return btn_add;
    }

    public void setBtn_add(Button b) {
        this.btn_add = b;
    }

    private PanelLayout lay_man = new PanelLayout();

    public PanelLayout getLay_man() {
        return lay_man;
    }

    public void setLay_man(PanelLayout pl) {
        this.lay_man = pl;
    }

    private PanelLayout lay_add_upd_del_ext = new PanelLayout();

    public PanelLayout getLay_add_upd_del_ext() {
        return lay_add_upd_del_ext;
    }

    public void setLay_add_upd_del_ext(PanelLayout pl) {
        this.lay_add_upd_del_ext = pl;
    }

    private Button btn_upd = new Button();

    public Button getBtn_upd() {
        return btn_upd;
    }

    public void setBtn_upd(Button b) {
        this.btn_upd = b;
    }

    private Button btn_src = new Button();

    public Button getBtn_src() {
        return btn_src;
    }

    public void setBtn_src(Button b) {
        this.btn_src = b;
    }

    private TableRowGroup tableRowGroup1 = new TableRowGroup();

    public TableRowGroup getTableRowGroup1() {
        return tableRowGroup1;
    }

    public void setTableRowGroup1(TableRowGroup trg) {
        this.tableRowGroup1 = trg;
    }

    private PanelLayout lay_tbl = new PanelLayout();

    public PanelLayout getLay_tbl() {
        return lay_tbl;
    }

    public void setLay_tbl(PanelLayout pl) {
        this.lay_tbl = pl;
    }

    private StaticText txt_tot = new StaticText();

    public StaticText getTxt_tot() {
        return txt_tot;
    }

    public void setTxt_tot(StaticText st) {
        this.txt_tot = st;
    }

    private Button btn_ext = new Button();

    public Button getBtn_ext() {
        return btn_ext;
    }

    public void setBtn_ext(Button b) {
        this.btn_ext = b;
    }

    private Label lbl_man = new Label();

    public Label getLbl_man() {
        return lbl_man;
    }

    public void setLbl_man(Label l) {
        this.lbl_man = l;
    }

    private PanelLayout lay_src = new PanelLayout();

    public PanelLayout getLay_src() {
        return lay_src;
    }

    public void setLay_src(PanelLayout pl) {
        this.lay_src = pl;
    }

    private Table tbl_rol = new Table();

    public Table getTbl_rol() {
        return tbl_rol;
    }

    public void setTbl_rol(Table t) {
        this.tbl_rol = t;
    }

    private TableRowGroup tableRowGroup2 = new TableRowGroup();

    public TableRowGroup getTableRowGroup2() {
        return tableRowGroup2;
    }

    public void setTableRowGroup2(TableRowGroup trg) {
        this.tableRowGroup2 = trg;
    }

    private DropDown ddl_src = new DropDown();

    public DropDown getDdl_src() {
        return ddl_src;
    }

    public void setDdl_src(DropDown dd) {
        this.ddl_src = dd;
    }

    private SingleSelectOptionsList ddl_srcDefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getDdl_srcDefaultOptions() {
        return ddl_srcDefaultOptions;
    }

    public void setDdl_srcDefaultOptions(SingleSelectOptionsList ssol) {
        this.ddl_srcDefaultOptions = ssol;
    }

    private Button btn_vew = new Button();

    public Button getBtn_vew() {
        return btn_vew;
    }

    public void setBtn_vew(Button b) {
        this.btn_vew = b;
    }

    private TextArea mes = new TextArea();

    public TextArea getMes() {
        return mes;
    }

    public void setMes(TextArea ta) {
        this.mes = ta;
    }

    private Button btn_prt = new Button();

    public Button getBtn_prt() {
        return btn_prt;
    }

    public void setBtn_prt(Button b) {
        this.btn_prt = b;
    }

    private PanelLayout layoutPanel1 = new PanelLayout();

    public PanelLayout getLayoutPanel1() {
        return layoutPanel1;
    }

    public void setLayoutPanel1(PanelLayout pl) {
        this.layoutPanel1 = pl;
    }

    private CachedRowSetDataProvider vl_denominationDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getVl_denominationDataProvider() {
        return vl_denominationDataProvider;
    }

    public void setVl_denominationDataProvider(CachedRowSetDataProvider crsdp) {
        this.vl_denominationDataProvider = crsdp;
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
    public Denomination() {
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

    @Override
    public void init() {
        super.init();
        try {
            getSessionBean1().validateScreen("Denomination.jsp");
            int validate = getSessionBean1().checking("Denomination.jsp");
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
                    break;
            }
            HttpSession session = ((HttpServletRequest) getExternalContext().getRequest()).getSession();
            if (session.getAttribute("Update") != null) {
                mes.setText(session.getAttribute("Update").toString());
            }
            session.setAttribute("Update", "");
            String[] err = { "CM006", "CM029", "CM019", "CM018", "CM020", "CM043", "CM007" };
            messages = getSessionBean1().getMessages(err);
            if (messages.containsKey("Exception")) {
                getMes().setText(messages.get("Exception"));
                btn_upd.setDisabled(true);
                btn_add.setDisabled(true);
                btn_prt.setDisabled(true);
                btn_vew.setDisabled(true);
                ddl_src.setDisabled(true);
                fld_src.setDisabled(true);
                btn_src.setDisabled(true);
            }
            tableRowGroup1.setEmptyDataMsg(messages.get("CM007").toString());
        } catch (Exception ex) {
            System.out.println("Denomination, init(), " + getSessionBean1().getUserId() + " - " + ex);
            try {
                this.getExternalContext().redirect("/CBA/faces/InitError.jsp");
            } catch (IOException ioe) {
            }
        }
        try {
            _init();
        } catch (Exception e) {
            log("Page1 Initialization Failure", e);
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
            if (Integer.parseInt(getSessionBean1().getRow()) <= getVl_denominationDataProvider().getRowCount()) {
                setRBSelected(getSessionBean1().getRow());
            } else {
                setRBSelected("0");
            }
            getVl_denominationDataProvider().refresh();
        } catch (Exception ex) {
            System.out.println("Error in Prerender() Denomination.jsp, User ID " + getSessionBean1().getUserId() + "-" + ex.getMessage());
            getMes().setText(messages.get("CM029"));
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
        vl_denominationDataProvider.close();
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

    public String btn_src_action() {
        getMes().setText("");
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
            if (searchValue.equalsIgnoreCase("Active") && searchSelection.equalsIgnoreCase("par_dsc")) searchValue = "Active";
            if (searchSelection.equals("*")) {
                getVl_denominationDataProvider().refresh();
                tableRowGroup2.clearFilter();
                fld_src.setText("");
            } else {
                String regExpression = new String();
                for (int i = 0; i < searchValue.length(); i++) {
                    regExpression = regExpression + "[\\Q" + searchValue.substring(i, i + 1).toUpperCase() + searchValue.substring(i, i + 1).toLowerCase() + "\\E]";
                }
                regExpression = (searchSelection.equals("par_dsc") && searchValue.equalsIgnoreCase("Active")) ? searchValue : ".*" + regExpression + ".*";
                tableRowGroup2.getTableDataFilter().setFilterCriteria(new FilterCriteria[] { new RegexFilterCriteria(getVl_denominationDataProvider().getFieldKey(searchSelection), regExpression) });
                getVl_denominationDataProvider().refresh();
                tableRowGroup2.setEmptyDataMsg(messages.get("CM007").toString());
            }
        } catch (Exception ex) {
            System.out.println("Error in Search Denomination.jsp, Method:btn_src_action(), User ID " + getSessionBean1().getUserId() + "-" + ex.getMessage());
            getMes().setText(messages.get("CM019"));
        }
        return null;
    }

    public String btn_upd_action() {
        if (getSessionBean1().getRow() == "" || getSessionBean1().getRow() == null) {
            getMes().setText(messages.get("CM006"));
            return null;
        }
        String aRowId = "", str = "";
        RowKey aRowKey = new RowKey();
        try {
            aRowId = getSessionBean1().getRow();
            aRowKey = getVl_denominationDataProvider().getRowKey(aRowId);
            str = getVl_denominationDataProvider().getValue("den_id", aRowKey).toString();
        } catch (Exception ex) {
            System.out.println("Error in btn_upd_action() Denomination.jsp, User ID " + getSessionBean1().getUserId() + "-" + ex.getMessage());
            getMes().setText(messages.get("CM018"));
        }
        getSessionBean1().setRow(aRowId);
        getSessionBean1().setUpd_val(str);
        getSessionBean1().setPag_nav("update");
        return "Detail";
    }

    public String btn_add_action() {
        getSessionBean1().setPag_nav("add");
        return "Detail";
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
            if (searchSelection.equals("den_id")) {
                e_qry = "where lower(a." + searchSelection + ") like lower('%" + searchValue + "%')";
            } else if (searchSelection.equals("den_atm")) {
                e_qry = "where lower(a." + searchSelection + ") like lower('%" + searchValue + "%')";
            } else if (searchSelection.equals("den_sts")) {
                e_qry = "where lower(a." + searchSelection + ") like lower('%" + searchValue + "%')";
            }
            fillParams.put("UserName", getSessionBean1().getUserId());
            fillParams.put("E_Query", e_qry);
            getSessionBean1().generateReport("Vault_Denomination", fillParams, getSessionBean1().getUserId());
        } catch (Exception ex) {
            System.out.println("Error in btn_prt_action() Denomination.jsp, User ID " + getSessionBean1().getUserId() + "- Exception generating report: " + ex.getMessage());
            getMes().setText(messages.get("CM043"));
        }
        return null;
    }

    public String btn_vew_action() {
        if (getSessionBean1().getRow() == null || getSessionBean1().getRow() == "") {
            getMes().setText(messages.get("CM006"));
            return null;
        }
        String aRowId = "", str = "";
        RowKey aRowKey = new RowKey();
        try {
            aRowId = getSessionBean1().getRow();
            aRowKey = getVl_denominationDataProvider().getRowKey(aRowId);
            str = getVl_denominationDataProvider().getValue("den_id", aRowKey).toString();
        } catch (Exception ex) {
            System.out.println("Error in btn_vew_action() Denomination.jsp, User ID " + getSessionBean1().getUserId() + "-" + ex.getMessage());
            getMes().setText(messages.get("CM020"));
        }
        getSessionBean1().setRow(aRowId);
        getSessionBean1().setUpd_val(str);
        getSessionBean1().setPag_nav("view");
        return "Detail";
    }

    public void fld_src_processValueChange(ValueChangeEvent vce) {
    }

    public String btn_ext_action() {
        getSessionBean1().setRow("0");
        return "dashboard";
    }

    public String getCurrentRow() {
        return tableRowGroup2.getRowKey().getRowId();
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

    public void radioButton1_processValueChange(ValueChangeEvent event) {
        lastSelected = (String) RadioButton.getSelected("radioGroup");
        getSessionBean1().setRow((String) lastSelected);
    }

    public void ddl_src_processValueChange(ValueChangeEvent event) {
        if (getDdl_src().getSelected().equals("*")) {
            getFld_src().setDisabled(true);
        } else {
            getFld_src().setDisabled(false);
        }
    }
}
