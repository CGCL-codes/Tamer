package cba;

import com.sun.data.provider.impl.CachedRowSetDataProvider;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.sql.rowset.CachedRowSetXImpl;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Calendar;
import com.sun.webui.jsf.component.Checkbox;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.HiddenField;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.Link;
import com.sun.webui.jsf.component.Page;
import com.sun.webui.jsf.component.PanelLayout;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TextArea;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.model.SingleSelectOptionsList;
import java.util.Date;
import java.util.Vector;
import java.io.*;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import javax.faces.FacesException;
import javax.faces.convert.BigDecimalConverter;
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
 * @author Fakharunissa Memon
 */
public class Cash_Flow_Add extends AbstractPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
        ad_seasonstatusDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{Seasons_Add.ad_seasonstatusRowSet}"));
        ad_seasonstatusRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        ad_seasonstatusRowSet.setCommand("select \'9999999\' AS par_cod, \'Select\' AS par_dsc UNION ALL \r\n(SELECT ALL par_cod, par_dsc  FROM public.ad_parameter  WHERE par_typ = \'seas_sts\'\r\n and par_sts=\'1\')");
        ad_seasonstatusRowSet.setTableName("ad_seasons");
        pr_cash_flow_catDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{Cash_Flow_Add.pr_cash_flow_catRowSet}"));
        pr_cash_flow_catRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        pr_cash_flow_catRowSet.setCommand("\r\n\r\nselect \'9999999\' AS par_cod, \'Select\' AS par_dsc UNION ALL \r\n(select par_cod,par_dsc  from ad_parameter where par_typ=\'cf_cat\' \r\n and par_sts=\'1\' order by par_dsc)");
        pr_cash_flow_catRowSet.setTableName("pr_cash_flow");
        pr_cash_flow_typDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{Cash_Flow_Add.pr_cash_flow_typRowSet}"));
        pr_cash_flow_typRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        pr_cash_flow_typRowSet.setCommand("\r\n\r\nselect \'9999999\' AS par_cod, \'Select\' AS par_dsc UNION ALL \r\n(select par_cod,par_dsc  from ad_parameter where par_typ=\'cf_typ\' \r\n and par_sts=\'1\' order by par_dsc)");
        pr_cash_flow_typRowSet.setTableName("pr_cash_flow");
        pr_cash_flowstatusDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{Cash_Flow_Add.pr_cash_flowstatusRowSet}"));
        pr_cash_flowstatusRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        pr_cash_flowstatusRowSet.setCommand("\r\n\r\nselect \'9999999\' AS par_cod, \'Select\' AS par_dsc UNION ALL \r\n(select par_cod,par_dsc  from ad_parameter where par_typ=\'cf_sts\' \r\n and par_sts=\'1\' order by par_dsc)");
        pr_cash_flowstatusRowSet.setTableName("pr_cash_flow");
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

    private Button btn_ok = new Button();

    public Button getBtn_ok() {
        return btn_ok;
    }

    public void setBtn_ok(Button b) {
        this.btn_ok = b;
    }

    private PanelLayout lay_man = new PanelLayout();

    public PanelLayout getLay_man() {
        return lay_man;
    }

    public void setLay_man(PanelLayout pl) {
        this.lay_man = pl;
    }

    private PanelLayout lay_ok_ext = new PanelLayout();

    public PanelLayout getLay_ok_ext() {
        return lay_ok_ext;
    }

    public void setLay_ok_ext(PanelLayout pl) {
        this.lay_ok_ext = pl;
    }

    private Table country_tbl = new Table();

    public Table getCountry_tbl() {
        return country_tbl;
    }

    public void setCountry_tbl(Table t) {
        this.country_tbl = t;
    }

    private Checkbox checkbox1 = new Checkbox();

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

    private DropDown ddl_status = new DropDown();

    public DropDown getDdl_status() {
        return ddl_status;
    }

    public void setDdl_status(DropDown dd) {
        this.ddl_status = dd;
    }

    private Label lbl_str_date = new Label();

    public Label getLbl_str_date() {
        return lbl_str_date;
    }

    public void setLbl_str_date(Label l) {
        this.lbl_str_date = l;
    }

    private Label lbl_End_date = new Label();

    public Label getLbl_End_date() {
        return lbl_End_date;
    }

    public void setLbl_End_date(Label l) {
        this.lbl_End_date = l;
    }

    private TextArea mes = new TextArea();

    public TextArea getMes() {
        return mes;
    }

    public void setMes(TextArea ta) {
        this.mes = ta;
    }

    private HiddenField hsel = new HiddenField();

    public HiddenField getHsel() {
        return hsel;
    }

    public void setHsel(HiddenField hf) {
        this.hsel = hf;
    }

    private SingleSelectOptionsList dropDown2DefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getDropDown2DefaultOptions() {
        return dropDown2DefaultOptions;
    }

    public void setDropDown2DefaultOptions(SingleSelectOptionsList ssol) {
        this.dropDown2DefaultOptions = ssol;
    }

    private SingleSelectOptionsList dropDown1DefaultOptions = new SingleSelectOptionsList();

    public SingleSelectOptionsList getDropDown1DefaultOptions() {
        return dropDown1DefaultOptions;
    }

    public void setDropDown1DefaultOptions(SingleSelectOptionsList ssol) {
        this.dropDown1DefaultOptions = ssol;
    }

    private BigDecimalConverter bigDecimalConverter4 = new BigDecimalConverter();

    public BigDecimalConverter getBigDecimalConverter4() {
        return bigDecimalConverter4;
    }

    private CachedRowSetDataProvider ad_seasonstatusDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getAd_seasonstatusDataProvider() {
        return ad_seasonstatusDataProvider;
    }

    public void setAd_seasonstatusDataProvider(CachedRowSetDataProvider crsdp) {
        this.ad_seasonstatusDataProvider = crsdp;
    }

    private CachedRowSetXImpl ad_seasonstatusRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getAd_seasonstatusRowSet() {
        return ad_seasonstatusRowSet;
    }

    public void setAd_seasonstatusRowSet(CachedRowSetXImpl crsxi) {
        this.ad_seasonstatusRowSet = crsxi;
    }

    private CachedRowSetDataProvider pr_cash_flow_catDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getPr_cash_flow_catDataProvider() {
        return pr_cash_flow_catDataProvider;
    }

    public void setPr_cash_flow_catDataProvider(CachedRowSetDataProvider crsdp) {
        this.pr_cash_flow_catDataProvider = crsdp;
    }

    private CachedRowSetXImpl pr_cash_flow_catRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getPr_cash_flow_catRowSet() {
        return pr_cash_flow_catRowSet;
    }

    public void setPr_cash_flow_catRowSet(CachedRowSetXImpl crsxi) {
        this.pr_cash_flow_catRowSet = crsxi;
    }

    private DropDown ddl_cash_flow_cat = new DropDown();

    public DropDown getDdl_cash_flow_cat() {
        return ddl_cash_flow_cat;
    }

    public void setDdl_cash_flow_cat(DropDown dd) {
        this.ddl_cash_flow_cat = dd;
    }

    private CachedRowSetDataProvider pr_cash_flow_typDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getPr_cash_flow_typDataProvider() {
        return pr_cash_flow_typDataProvider;
    }

    public void setPr_cash_flow_typDataProvider(CachedRowSetDataProvider crsdp) {
        this.pr_cash_flow_typDataProvider = crsdp;
    }

    private CachedRowSetXImpl pr_cash_flow_typRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getPr_cash_flow_typRowSet() {
        return pr_cash_flow_typRowSet;
    }

    public void setPr_cash_flow_typRowSet(CachedRowSetXImpl crsxi) {
        this.pr_cash_flow_typRowSet = crsxi;
    }

    private BigDecimalConverter ddl_cash_flow_typConverter = new BigDecimalConverter();

    public BigDecimalConverter getDdl_cash_flow_typConverter() {
        return ddl_cash_flow_typConverter;
    }

    public void setDdl_cash_flow_typConverter(BigDecimalConverter bdc) {
        this.ddl_cash_flow_typConverter = bdc;
    }

    private DropDown ddl_cash_flow_typ = new DropDown();

    public DropDown getDdl_cash_flow_typ() {
        return ddl_cash_flow_typ;
    }

    public void setDdl_cash_flow_typ(DropDown dd) {
        this.ddl_cash_flow_typ = dd;
    }

    private CachedRowSetDataProvider pr_cash_flowstatusDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getPr_cash_flowstatusDataProvider() {
        return pr_cash_flowstatusDataProvider;
    }

    public void setPr_cash_flowstatusDataProvider(CachedRowSetDataProvider crsdp) {
        this.pr_cash_flowstatusDataProvider = crsdp;
    }

    private CachedRowSetXImpl pr_cash_flowstatusRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getPr_cash_flowstatusRowSet() {
        return pr_cash_flowstatusRowSet;
    }

    public void setPr_cash_flowstatusRowSet(CachedRowSetXImpl crsxi) {
        this.pr_cash_flowstatusRowSet = crsxi;
    }

    private BigDecimalConverter ddl_statusConverter = new BigDecimalConverter();

    public BigDecimalConverter getDdl_statusConverter() {
        return ddl_statusConverter;
    }

    public void setDdl_statusConverter(BigDecimalConverter bdc) {
        this.ddl_statusConverter = bdc;
    }

    private TextField fld_cash_flow_dsc = new TextField();

    public TextField getFld_cash_flow_dsc() {
        return fld_cash_flow_dsc;
    }

    public void setFld_cash_flow_dsc(TextField tf) {
        this.fld_cash_flow_dsc = tf;
    }

    private BigDecimalConverter ddl_cash_flow_catConverter = new BigDecimalConverter();

    public BigDecimalConverter getDdl_cash_flow_catConverter() {
        return ddl_cash_flow_catConverter;
    }

    public void setDdl_cash_flow_catConverter(BigDecimalConverter bdc) {
        this.ddl_cash_flow_catConverter = bdc;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public Cash_Flow_Add() {
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
    private TextField txtDate = new TextField();

    public TextField getTxtDate() {
        return txtDate;
    }

    public void setTxtDate(TextField tf) {
        this.txtDate = tf;
    }

    private TextField txtTime = new TextField();

    public TextField getTxtTime() {
        return txtTime;
    }

    public void setTxtTime(TextField tf) {
        this.txtTime = tf;
    }

    HashMap messages;

    @Override
    public void init() {
        super.init();
        sel = getSessionBean1().getPag_nav();
        val = getSessionBean1().replace(getSessionBean1().getUpd_val());
        try {
            getSessionBean1().validateScreen("Cash_Flow_Setup.jsp");
            String[] err = { "CM001", "CM002", "CM004", "CM003", "CM010", "CM014", "CM015", "CM016", "CM017", "CM026", "CM033", "CM044" };
            getMes().setText("");
            messages = getSessionBean1().getMessages(err);
            if (messages.containsKey("Exception")) {
                getMes().setText(messages.get("Exception"));
                getBtn_ok().setDisabled(true);
            }
            int validate = getSessionBean1().checking("Cash_Flow_Setup.jsp");
            switch(validate) {
                case 1:
                case 3:
                    break;
                default:
                    if (!sel.equals("update") && !sel.equals("view")) {
                        btn_ok.setDisabled(true);
                        getMes().setText(messages.get("CM044"));
                    }
                    break;
            }
        } catch (Exception ex) {
            System.out.println("Cash_Flow_Add, init(), " + getSessionBean1().getUserId() + " - " + ex);
            try {
                this.getExternalContext().redirect("/CBA/faces/InitError.jsp");
            } catch (IOException ioe) {
                getMes().setText("Error in init " + ioe);
            }
        }
        try {
            _init();
        } catch (Exception e) {
            log("Document Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
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
    String sel, val;

    @Override
    public void prerender() {
        String sel_qry = "";
        if (sel.equals("0")) {
            sel = getSessionBean1().getPag_nav();
        }
        try {
            getPr_cash_flow_catRowSet().execute();
            getPr_cash_flow_typRowSet().execute();
            getPr_cash_flowstatusRowSet().execute();
            if (sel.equals("update")) {
                getLbl_man().setText("Update Cash Inflow/Outflow Parameters");
            } else if (sel.equals("add")) {
                getDdl_cash_flow_cat().setSelected("9999999");
                getDdl_cash_flow_typ().setSelected("9999999");
                getFld_cash_flow_dsc().setText("");
                getDdl_status().setSelected("9999999");
                getLbl_man().setText("Add Cash Inflow/Outflow Parameters ");
                getHsel().setText(sel);
            } else if (sel.equals("view")) {
                getLbl_man().setText("View Cash Inflow/Outflow Parameters");
                getDdl_cash_flow_cat().setDisabled(true);
                getDdl_cash_flow_typ().setDisabled(true);
                getFld_cash_flow_dsc().setDisabled(true);
                getDdl_status().setDisabled(true);
                getBtn_ok().setDisabled(true);
            }
            if ((sel.equals("update")) || (sel.equals("view"))) {
                Vector doc = new Vector();
                sel_qry = " select cash_flow_cat,cash_flow_type, public.pr_cash_flow.cash_flow_desc, status FROM public.pr_cash_flow  where cash_flow_id=" + val;
                try {
                    doc = (Vector) (getSessionBean1().executeSQLSelect(sel_qry)).get(0);
                    getDdl_cash_flow_cat().setValue(doc.get(0));
                    getDdl_cash_flow_typ().setValue(doc.get(1));
                    getFld_cash_flow_dsc().setText(doc.get(2));
                    getDdl_status().setSelected(doc.get(3));
                    getHsel().setText(sel);
                } catch (Exception ex) {
                    System.out.println("Error Executing Cash Inflow/Outflow Parameters Select Query ,Method: Prerender(), User ID " + getSessionBean1().getUserId() + "- :" + ex.getMessage());
                    getMes().setText(messages.get("CM033"));
                }
            }
        } catch (Exception ex) {
            System.out.println("Cash_Flow_add, prerender(), " + getSessionBean1().getUserId() + " - " + ex.getMessage());
            try {
                this.getExternalContext().redirect("/CBA/faces/InitError.jsp");
            } catch (IOException ioe) {
                getMes().setText(messages.get("CM033"));
            }
        }
        getSessionBean1().setPag_nav("0");
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
        pr_cash_flow_catDataProvider.close();
        pr_cash_flow_typDataProvider.close();
        pr_cash_flowstatusDataProvider.close();
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
    protected Admin getAdmin() {
        return (Admin) getBean("Admin");
    }

    public String btn_ok_action() {
        String category = "", type = "", desc = "", status = "";
        int errDsc = 0;
        String retSts = "";
        getMes().setText("");
        Vector errors = new Vector();
        String sel = hsel.getText().toString();
        try {
            if (getDdl_cash_flow_cat().getSelected().toString().equals("9999999")) {
                errors.add("Cash Flow Category " + messages.get("CM010"));
                errDsc = 1;
            }
            if (getDdl_cash_flow_typ().getSelected().toString().equals("9999999")) {
                errors.add("Cash Flow Type " + messages.get("CM010"));
                errDsc = 1;
            }
            if (getFld_cash_flow_dsc().getText() == "" || getFld_cash_flow_dsc().getText() == null) {
                errors.add("Description " + messages.get("CM004"));
                errDsc = 1;
            }
            if ((errDsc == 0) && getSessionBean1().isNumeric(getFld_cash_flow_dsc().getText().toString())) {
                errors.add("Description " + messages.get("CM014"));
                errDsc = 1;
            }
            if ((errDsc == 0) && getSessionBean1().IsSpecial(getFld_cash_flow_dsc().getText().toString())) {
                errors.add("Description " + messages.get("CM015").toString());
                errDsc = 1;
            }
            if ((errDsc == 0) && getSessionBean1().IsInvalid(getFld_cash_flow_dsc().getText().toString())) {
                errors.add("Description " + messages.get("CM017").toString());
                errDsc = 1;
            }
            if (getDdl_status().getSelected().toString().equals("9999999")) {
                errors.add("Status " + messages.get("CM010"));
                errDsc = 1;
            }
            if (errDsc == 1) {
                for (int i = 0; i < errors.size(); i++) {
                    if (i == 0) {
                        getMes().setText(errors.elementAt(i));
                    } else {
                        getMes().setText(getMes().getText() + "\n" + errors.elementAt(i));
                    }
                }
                return null;
            }
            category = getDdl_cash_flow_cat().getValue().toString();
            type = getDdl_cash_flow_typ().getValue().toString();
            desc = getFld_cash_flow_dsc().getText().toString();
            status = getDdl_status().getSelected().toString();
            retSts = getAdmin().CashFlow_Add(sel, category, type, desc, status, val);
            if (retSts.equals("dsc exist")) {
                getMes().setText("Description  " + messages.get("CM003"));
                return null;
            } else if (retSts.equals("child exist")) {
                getMes().setText(messages.get("CM026"));
                return null;
            } else if (retSts.equals("error")) {
                getMes().setText(messages.get("CM016"));
                return null;
            }
            if (getHsel().getText().toString().equals("update")) {
                HttpSession session = ((HttpServletRequest) getExternalContext().getRequest()).getSession();
                session.setAttribute("Update", "Cash Flow ID " + val + " " + messages.get("CM002"));
                return null;
            } else if (getHsel().getText().toString().equals("add")) {
                getMes().setText("Cash Flow ID " + retSts + " " + messages.get("CM001"));
                getSessionBean1().setPag_nav("add");
            }
            return null;
        } catch (Exception ex) {
            System.out.println("Cash_Flow_add, btn_ok_action(), " + getSessionBean1().getUserId() + " - " + ex);
            getMes().setText(messages.get("CM033"));
            return null;
        }
    }

    public String btn_ext_action() {
        return "back";
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
    protected Pro_Def getPro_Def() {
        return (Pro_Def) getBean("Pro_Def");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected CIF getCIF() {
        return (CIF) getBean("CIF");
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected VaultManagement getVaultManagement() {
        return (VaultManagement) getBean("VaultManagement");
    }

    public void ddl_cash_flow_typ_processValueChange(ValueChangeEvent vce) {
    }

    public void ddl_cash_flow_cat_processValueChange(ValueChangeEvent event) {
    }

    public void ddl_status_processValueChange(ValueChangeEvent vce) {
    }

    public void fld_cash_flow_dsc_processValueChange(ValueChangeEvent event) {
    }
}
