package cba;

import com.sun.data.provider.FilterCriteria;
import com.sun.data.provider.RowKey;
import com.sun.data.provider.impl.CachedRowSetDataProvider;
import com.sun.data.provider.impl.RegexFilterCriteria;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.HiddenField;
import com.sun.webui.jsf.component.PanelLayout;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TableColumn;
import com.sun.webui.jsf.component.TableRowGroup;
import com.sun.webui.jsf.component.TextArea;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.model.SingleSelectOptionsList;
import com.sun.webui.jsf.component.RadioButton;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.faces.FacesException;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseRateDefinition extends AbstractPageBean {

    private void _init() throws Exception {
        ddl_srcDefaultOptions.setOptions(new com.sun.webui.jsf.model.Option[] { new com.sun.webui.jsf.model.Option("*", "Latest Record"), new com.sun.webui.jsf.model.Option("rat_dat", "Date"), new com.sun.webui.jsf.model.Option("par_dsc", "Base Rate") });
        pr_bas_rat_defDataProvider1.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.pr_bas_rat_defRowSet1}"));
    }

    private TextField fld_src = new TextField();

    public TextField getFld_src() {
        return fld_src;
    }

    public void setFld_src(TextField tf) {
        this.fld_src = tf;
    }

    private Object lastSelected;

    public void setRBSelected(Object selected) {
        lastSelected = selected;
    }

    public String getCurrentRow() {
        return tableRowGroup1.getRowKey().getRowId();
    }

    public void setCurrentRow(int row) {
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

    private Button btn_vew = new Button();

    public Button getBtn_vew() {
        return btn_vew;
    }

    public void setBtn_vew(Button b) {
        this.btn_vew = b;
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

    private Table tbl_bas_rat = new Table();

    public Table getTbl_bas_rat() {
        return tbl_bas_rat;
    }

    public void setTbl_bas_rat(Table t) {
        this.tbl_bas_rat = t;
    }

    private TableRowGroup tableRowGroup1 = new TableRowGroup();

    public TableRowGroup getTableRowGroup1() {
        return tableRowGroup1;
    }

    public void setTableRowGroup1(TableRowGroup trg) {
        this.tableRowGroup1 = trg;
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

    private PanelLayout lay_man = new PanelLayout();

    public PanelLayout getLay_man() {
        return lay_man;
    }

    public void setLay_man(PanelLayout pl) {
        this.lay_man = pl;
    }

    private CachedRowSetDataProvider pr_bas_rat_defDataProvider1 = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getPr_bas_rat_defDataProvider1() {
        return pr_bas_rat_defDataProvider1;
    }

    public void setPr_bas_rat_defDataProvider1(CachedRowSetDataProvider crsdp) {
        this.pr_bas_rat_defDataProvider1 = crsdp;
    }

    private RadioButton radioButton1 = new RadioButton();

    public RadioButton getRadioButton1() {
        return radioButton1;
    }

    public void setRadioButton1(RadioButton rb) {
        this.radioButton1 = rb;
    }

    public BaseRateDefinition() {
    }

    HashMap messages;

    @Override
    public void init() {
        super.init();
        try {
            getSessionBean1().validateScreen("BaseRateDefinition.jsp");
            int validate = getSessionBean1().checking("BaseRateDefinition.jsp");
            switch(validate) {
                case 1:
                    break;
                case 2:
                    btn_add.setDisabled(true);
                    break;
                case 3:
                    break;
                default:
                    btn_add.setDisabled(true);
                    break;
            }
            HttpSession session = ((HttpServletRequest) getExternalContext().getRequest()).getSession();
            if (session.getAttribute("Update") != null) {
                mes.setText(session.getAttribute("Update").toString());
            }
            session.setAttribute("Update", "");
            String[] err = { "CM006", "CM007", "CM018", "CM019", "CM020", "CM029", "CM030", "CM031", "CM032", "CM043", "PD003", "CM013" };
            messages = getSessionBean1().getMessages(err);
            if (messages.containsKey("Exception")) {
                getMes().setText(messages.get("Exception"));
                btn_add.setDisabled(true);
                btn_prt.setDisabled(true);
                btn_vew.setDisabled(true);
                ddl_src.setDisabled(true);
                fld_src.setDisabled(true);
                btn_src.setDisabled(true);
            }
            getPr_bas_rat_defDataProvider1().refresh();
        } catch (Exception ex) {
            System.out.println("BaseRateDefinition, init(), " + getSessionBean1().getUserId() + " - " + ex);
            try {
                this.getExternalContext().redirect("/CBA/faces/InitError.jsp");
            } catch (IOException ioe) {
                getMes().setText("Error in init " + ioe);
            }
        }
        try {
            _init();
        } catch (Exception e) {
            log("Page1 Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
    }

    @Override
    public void prerender() {
        try {
            getPr_bas_rat_defDataProvider1().refresh();
            if (getRbSelection().getText() != null) {
                setRBSelected(getRbSelection().getText());
            } else {
                setRBSelected("0");
            }
        } catch (Exception ex) {
            System.out.println("BaseRateDefinition_AddUpdate, prerender(), " + getSessionBean1().getUserId() + " - " + ex);
            try {
                this.getExternalContext().redirect("/CBA/faces/InitError.jsp");
            } catch (IOException ioe) {
                getMes().setText(messages.get("CM029"));
            }
        }
    }

    @Override
    public void destroy() {
        pr_bas_rat_defDataProvider1.close();
    }

    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    String RetSts = "";

    private String UpdateView_action() {
        try {
            if (getSessionBean1().getRow() == "" || getSessionBean1().getRow() == null) {
                getMes().setText(messages.get("CM006"));
                return null;
            }
            RowKey aRowKey = new RowKey();
            String aRowId = getSessionBean1().getRow();
            aRowKey = getPr_bas_rat_defDataProvider1().getRowKey(aRowId);
            getSessionBean1().setRow(aRowId);
            getSessionBean1().setUpd_val(getSessionBean1().replace(getPr_bas_rat_defDataProvider1().getValue("par_dsc", aRowKey).toString()));
            getSessionBean1().setUpd_val1(getSessionBean1().replace(getPr_bas_rat_defDataProvider1().getValue("rat_dat", aRowKey).toString()));
            getSessionBean1().setUpd_val3(getPr_bas_rat_defDataProvider1().getValue("tenure", aRowKey).toString());
        } catch (Exception ex) {
            System.out.println("BaseRateDefinition,Error in UpdateView_action Method, " + getSessionBean1().getUserId() + " - " + ex);
            return "error";
        }
        return "Success";
    }

    public String btn_upd_action() {
        try {
            RetSts = UpdateView_action();
            if (RetSts.equals("error")) {
                getMes().setText(messages.get("CM018"));
                return null;
            } else if (RetSts.equals("Success")) {
                getSessionBean1().setPag_nav("update");
            }
            return "Detail";
        } catch (Exception ex) {
            System.out.println("BaseRateDefinition, btn_upd_action(), " + getSessionBean1().getUserId() + " - " + ex);
            getMes().setText(messages.get("CM018"));
            return null;
        }
    }

    public String btn_add_action() {
        getSessionBean1().setPag_nav("add");
        try {
            Vector v = ((Vector) getSessionBean1().executeSQLSelect("SELECT ALL public.ad_parameter.par_cod  FROM public.ad_parameter WHERE public.ad_parameter.par_typ = 'baserate'and ad_parameter.par_cod not in(Select bas_rat from pr_bas_rat_def where to_char(rat_dat,'dd/mm/yyyy')=to_char(now(),'dd/mm/yyyy')) and par_sts='1'"));
            if (v.size() == 0) {
                getMes().setText("Base Rates " + messages.get("PD003"));
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Error in btn_add_action() BaseRateDefinition.jsp, User ID " + getSessionBean1().getUserId() + "-" + ex.getMessage());
            getMes().setText(messages.get("CM033"));
            return null;
        }
        return "Detail";
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
            if (!searchSelection.equals("*")) {
                if (searchSelection.equals("par_dsc")) {
                    e_qry = "and lower(a." + searchSelection + ") like lower('%" + searchValue + "%')";
                } else if (searchSelection.equals("rat_dat")) {
                    e_qry = "and to_char(pr_bas_rat_def." + searchSelection + ",'dd/mm/yyyy hh:mi') like '%" + searchValue + "%'";
                }
            }
            fillParams.put("UserName", getSessionBean1().getUserId());
            fillParams.put("E_Query", e_qry);
            getSessionBean1().generateReport("base_rate", fillParams, getSessionBean1().getUserId());
        } catch (Exception ex) {
            System.out.println("Error in btn_prt_action() BaseRateDefinition.jsp, User ID " + getSessionBean1().getUserId() + "- Exception generating report: " + ex.getMessage());
            getMes().setText(messages.get("CM043"));
        }
        return null;
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
            if (searchSelection.equals("*")) {
                getPr_bas_rat_defDataProvider1().refresh();
                tableRowGroup1.clearFilter();
                fld_src.setText("");
            } else {
                String regExpression = new String();
                for (int i = 0; i < searchValue.length(); i++) {
                    regExpression = regExpression + "[" + searchValue.substring(i, i + 1).toUpperCase() + searchValue.substring(i, i + 1).toLowerCase() + "]";
                }
                regExpression = ".*" + regExpression + ".*";
                tableRowGroup1.getTableDataFilter().setFilterCriteria(new FilterCriteria[] { new RegexFilterCriteria(getPr_bas_rat_defDataProvider1().getFieldKey(searchSelection), regExpression) });
                getPr_bas_rat_defDataProvider1().refresh();
                tableRowGroup1.setEmptyDataMsg(messages.get("CM007").toString());
            }
        } catch (Exception ex) {
            System.out.println("Menu_Item, btn_src_action(), " + getSessionBean1().getUserId() + " - " + ex);
            getMes().setText(messages.get("CM019"));
        }
        return null;
    }

    public String btn_vew_action() {
        try {
            String retSts = UpdateView_action();
            getSessionBean1().setPag_nav("View");
            if (RetSts.equals("error")) {
                getMes().setText(messages.get("CM020"));
                return null;
            } else if (RetSts.equals("Success")) {
                getSessionBean1().setPag_nav("View");
            }
            return "Detail";
        } catch (Exception ex) {
            System.out.println("BaseRateDefinition, btn_vew_action(), " + getSessionBean1().getUserId() + " - " + ex);
            getMes().setText(messages.get("CM020"));
        }
        return null;
    }

    public void radioButton1_processValueChange(ValueChangeEvent event) {
        lastSelected = (String) RadioButton.getSelected("radioGroup");
        getSessionBean1().setRow((String) lastSelected);
    }

    private HiddenField rbSelection = new HiddenField();

    public HiddenField getRbSelection() {
        return rbSelection;
    }

    public void setRbSelection(HiddenField hf) {
        this.rbSelection = hf;
    }

    public void ddl_src_processValueChange(ValueChangeEvent event) {
        if (getDdl_src().getSelected().equals("*")) {
            getFld_src().setDisabled(true);
        } else {
            getFld_src().setDisabled(false);
        }
    }

    protected Pro_Def getPro_Def() {
        return (Pro_Def) getBean("Pro_Def");
    }

    public void preprocess() {
    }

    public Object getRBSelected() {
        String sv = (String) radioButton1.getSelectedValue();
        return sv.equals(lastSelected) ? sv : null;
    }

    public void fld_src_processValueChange(ValueChangeEvent event) {
    }

    public static String now(String dateFormat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(cal.getTime());
    }
}
