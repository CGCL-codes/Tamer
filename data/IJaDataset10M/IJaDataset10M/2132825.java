package cba;

import com.sun.data.provider.impl.CachedRowSetDataProvider;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.sql.rowset.CachedRowSetXImpl;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Checkbox;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.StaticText;
import com.sun.webui.jsf.component.Table;
import com.sun.webui.jsf.component.TextArea;
import com.sun.webui.jsf.component.TextField;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.faces.FacesException;
import javax.faces.convert.NumberConverter;
import com.sun.data.provider.RowKey;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Aisha.Saeed
 */
public class ExchangeDenomination extends AbstractPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
        vl_denominationCashInDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{ExchangeDenomination.vl_denominationCashInRowSet}"));
        vl_denominationCashInRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        vl_denominationCashInRowSet.setCommand("SELECT den_id ,'' as qtyEnter from vl_denomination where den_sts=1 order by 1 desc");
        vl_denominationCashInRowSet.setTableName("vl_cashInDenomination");
        vl_denominationCashOutDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.vl_withdrawalDenominationRowSet}"));
        numberConverter1.setPattern("#,##0.###");
        numberConverter1.setMinIntegerDigits(1);
        numberConverter1.setMaxIntegerDigits(40);
        numberConverter1.setMaxFractionDigits(3);
    }

    private Label lbl_trn_date = new Label();

    public Label getLbl_trn_date() {
        return lbl_trn_date;
    }

    public void setLbl_trn_date(Label l) {
        this.lbl_trn_date = l;
    }

    private CachedRowSetDataProvider vl_denominationCashInDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getVl_denominationCashInDataProvider() {
        return vl_denominationCashInDataProvider;
    }

    public void setVl_denominationCashInDataProvider(CachedRowSetDataProvider crsdp) {
        this.vl_denominationCashInDataProvider = crsdp;
    }

    private CachedRowSetDataProvider vl_denominationCashOutDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getVl_denominationCashOutDataProvider() {
        return vl_denominationCashOutDataProvider;
    }

    public void setVl_denominationCashOutDataProvider(CachedRowSetDataProvider crsdp) {
        this.vl_denominationCashOutDataProvider = crsdp;
    }

    private CachedRowSetXImpl vl_denominationCashInRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getVl_denominationCashInRowSet() {
        return vl_denominationCashInRowSet;
    }

    public void setVl_denominationCashInRowSet(CachedRowSetXImpl crsxi) {
        this.vl_denominationCashInRowSet = crsxi;
    }

    private NumberConverter numberConverter1 = new NumberConverter();

    public NumberConverter getNumberConverter1() {
        return numberConverter1;
    }

    public void setNumberConverter1(NumberConverter nc) {
        this.numberConverter1 = nc;
    }

    private Table tbl_den = new Table();

    public Table getTbl_den() {
        return tbl_den;
    }

    public void setTbl_den(Table t) {
        this.tbl_den = t;
    }

    private TextField textField1 = new TextField();

    public TextField getTextField1() {
        return textField1;
    }

    public void setTextField1(TextField tf) {
        this.textField1 = tf;
    }

    private StaticText staticText1 = new StaticText();

    public StaticText getStaticText1() {
        return staticText1;
    }

    public void setStaticText1(StaticText st) {
        this.staticText1 = st;
    }

    private Label lbl_tot_amt = new Label();

    public Label getLbl_tot_amt() {
        return lbl_tot_amt;
    }

    public void setLbl_tot_amt(Label l) {
        this.lbl_tot_amt = l;
    }

    private Button btn_post = new Button();

    public Button getBtn_post() {
        return btn_post;
    }

    public void setBtn_post(Button b) {
        this.btn_post = b;
    }

    private Button btn_ext = new Button();

    public Button getBtn_ext() {
        return btn_ext;
    }

    public void setBtn_ext(Button b) {
        this.btn_ext = b;
    }

    private TextArea txtComm = new TextArea();

    public TextArea getTxtComm() {
        return txtComm;
    }

    public void setTxtComm(TextArea ta) {
        this.txtComm = ta;
    }

    private TextArea mes = new TextArea();

    public TextArea getMes() {
        return mes;
    }

    public void setMes(TextArea ta) {
        this.mes = ta;
    }

    private Checkbox chkConfirm = new Checkbox();

    public Checkbox getChkConfirm() {
        return chkConfirm;
    }

    public void setChkConfirm(Checkbox c) {
        this.chkConfirm = c;
    }

    private TextField cashInAmount = new TextField();

    public TextField getCashInAmount() {
        return cashInAmount;
    }

    public void setCashInAmount(TextField tf) {
        this.cashInAmount = tf;
    }

    private TextField cashOutAmount = new TextField();

    public TextField getCashOutAmount() {
        return cashOutAmount;
    }

    public void setCashOutAmount(TextField tf) {
        this.cashOutAmount = tf;
    }

    private Button btn_print = new Button();

    public Button getBtn_print() {
        return btn_print;
    }

    public void setBtn_print(Button b) {
        this.btn_print = b;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public ExchangeDenomination() {
    }

    HashMap messages = new HashMap();

    @Override
    public void init() {
        super.init();
        try {
            String[] err = { "CM004", "CM050", "DW043" };
            getMes().setText("");
            messages = getSessionBean1().getMessages(err);
            if (messages.containsKey("Exception")) {
                getMes().setText(messages.get("Exception"));
                getBtn_post().setDisabled(true);
            }
        } catch (Exception ex) {
            System.out.println("ExchangeDenomination.jsp, init(), " + getSessionBean1().getUserId() + " - " + ex);
            try {
                this.getExternalContext().redirect("/CBA/faces/InitError.jsp");
            } catch (IOException ioe) {
            }
        }
        try {
            _init();
        } catch (Exception e) {
            log("ExchangeDenomination Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
    }

    @Override
    public void preprocess() {
    }

    @Override
    public void prerender() {
        try {
            btn_post.setDisabled(true);
            lbl_trn_date.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            Object oCashbox = ((Vector) getSessionBean1().executeSQLSelect("select cas_box_id from vl_cash_box where off_usr_id='" + getSessionBean1().getUserId() + "'")).get(0);
            String sCashBoxId = ((Vector) oCashbox).get(0).toString();
            if (getSessionBean1().getPag_nav().equals("")) {
                getSessionBean1().getVl_withdrawalDenominationRowSet().setCommand("SELECT den_id ,cur_den_qty as available_denominations, '' as qtyEnter from vl_cas_box_den where cas_box_id = '" + sCashBoxId + "' order by 1 desc");
                getSessionBean1().getVl_withdrawalDenominationRowSet().setTableName("vl_denomination");
                getSessionBean1().getVl_withdrawalDenominationRowSet().execute();
                getSessionBean1().setPag_nav("0");
            }
            boolean flag = true;
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
                btn_post.setDisabled(true);
            }
        } catch (Exception ex) {
            System.out.println("Error Prerender() " + ex.getMessage());
        }
    }

    @Override
    public void destroy() {
        vl_denominationCashInDataProvider.close();
        vl_denominationCashOutDataProvider.close();
    }

    protected RequestBean1 getRequestBean1() {
        return (RequestBean1) getBean("RequestBean1");
    }

    protected SessionBean1 getSessionBean1() {
        return (SessionBean1) getBean("SessionBean1");
    }

    protected ApplicationBean1 getApplicationBean1() {
        return (ApplicationBean1) getBean("ApplicationBean1");
    }

    public boolean checkFields() {
        try {
            if (cashInAmount.getText() == null || Double.parseDouble(getSessionBean1().removeCurrencyFormat(cashInAmount.getText().toString(), true)) <= 0.00) {
                getMes().setText("Cash In Amount " + messages.get("CM004"));
                return false;
            }
            if (cashOutAmount.getText() == null || Double.parseDouble(getSessionBean1().removeCurrencyFormat(cashOutAmount.getText().toString(), true)) <= 0.00) {
                getMes().setText("Cash Out Amount " + messages.get("CM004"));
                return false;
            }
            if (!chkConfirm.isChecked()) {
                return false;
            }
            if (Double.parseDouble(getSessionBean1().removeCurrencyFormat(cashInAmount.getText().toString(), true)) != Double.parseDouble(getSessionBean1().removeCurrencyFormat(cashOutAmount.getText().toString(), true)) && Double.parseDouble(getSessionBean1().removeCurrencyFormat(cashInAmount.getText().toString(), true)) > 0.00) {
                getMes().setText("Cash In Amount and Cash Out Amount must be equal");
                return false;
            }
        } catch (Exception ex) {
            System.out.println("ExchangeDenomination, checkFields(), " + getSessionBean1().getUserId() + " - " + ex);
        }
        return true;
    }

    public String btn_print_action() {
        print_code();
        return null;
    }

    private Vector processCashInDenominations() {
        RowKey[] rowKey = getVl_denominationCashInDataProvider().getRowKeys(getVl_denominationCashInDataProvider().getRowCount(), null);
        Vector add_values = new Vector();
        Vector temp;
        try {
            for (int i = 0; i < rowKey.length; i++) {
                temp = new Vector();
                if (getVl_denominationCashInDataProvider().getValue(getVl_denominationCashInDataProvider().getFieldKey("den_id"), rowKey[i]) != null) temp.add(getVl_denominationCashInDataProvider().getValue(getVl_denominationCashInDataProvider().getFieldKey("den_id"), rowKey[i]).toString());
                if (getVl_denominationCashInDataProvider().getValue(getVl_denominationCashInDataProvider().getFieldKey("qtyEnter"), rowKey[i]) != null) temp.add(getVl_denominationCashInDataProvider().getValue(getVl_denominationCashInDataProvider().getFieldKey("qtyEnter"), rowKey[i]).toString()); else temp.add("0.0");
                add_values.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("ExchangeDenomination, processCashInDenominations(), " + getSessionBean1().getUserId() + " - " + ex);
        }
        return add_values;
    }

    private Vector processCashOutDenominations() {
        RowKey[] rowKey = getVl_denominationCashOutDataProvider().getRowKeys(getVl_denominationCashOutDataProvider().getRowCount(), null);
        Vector add_values = new Vector();
        Vector temp;
        try {
            for (int i = 0; i < rowKey.length; i++) {
                temp = new Vector();
                if (getVl_denominationCashOutDataProvider().getValue(getVl_denominationCashOutDataProvider().getFieldKey("den_id"), rowKey[i]) != null) temp.add(getVl_denominationCashOutDataProvider().getValue(getVl_denominationCashOutDataProvider().getFieldKey("den_id"), rowKey[i]).toString());
                if (getVl_denominationCashOutDataProvider().getValue(getVl_denominationCashOutDataProvider().getFieldKey("qtyEnter"), rowKey[i]).equals("") == false) temp.add(getVl_denominationCashOutDataProvider().getValue(getVl_denominationCashOutDataProvider().getFieldKey("qtyEnter"), rowKey[i]).toString()); else temp.add("0.0");
                add_values.add(temp);
            }
        } catch (Exception ex) {
            System.out.println("ExchangeDenomination, processCashOutDenominations(), " + getSessionBean1().getUserId() + " - " + ex);
        }
        return add_values;
    }

    public void print_code() {
        Map fillParams = new HashMap();
        String report_name = "";
        try {
            String deno_recieved = "", deno_out = "";
            RowKey[] rowKey = getVl_denominationCashInDataProvider().getRowKeys(getVl_denominationCashInDataProvider().getRowCount(), null);
            for (int i = 0; i < rowKey.length; i++) {
                if (getVl_denominationCashInDataProvider().getValue(getVl_denominationCashInDataProvider().getFieldKey("den_id"), rowKey[i]) != null && getVl_denominationCashInDataProvider().getValue(getVl_denominationCashInDataProvider().getFieldKey("qtyEnter"), rowKey[i]) != null) {
                    deno_recieved = deno_recieved + "(" + getVl_denominationCashInDataProvider().getValue(getVl_denominationCashInDataProvider().getFieldKey("den_id"), rowKey[i]).toString() + "x" + getVl_denominationCashInDataProvider().getValue(getVl_denominationCashInDataProvider().getFieldKey("qtyEnter"), rowKey[i]).toString() + ")";
                }
            }
            RowKey[] rowKey1 = getVl_denominationCashOutDataProvider().getRowKeys(getVl_denominationCashOutDataProvider().getRowCount(), null);
            for (int i = 0; i < rowKey1.length; i++) {
                if (getVl_denominationCashOutDataProvider().getValue(getVl_denominationCashOutDataProvider().getFieldKey("den_id"), rowKey[i]) != null && !(getVl_denominationCashOutDataProvider().getValue(getVl_denominationCashOutDataProvider().getFieldKey("qtyEnter"), rowKey[i]) == null || getVl_denominationCashOutDataProvider().getValue(getVl_denominationCashOutDataProvider().getFieldKey("qtyEnter"), rowKey[i]).equals(""))) {
                    deno_out = deno_out + "(" + getVl_denominationCashOutDataProvider().getValue(getVl_denominationCashOutDataProvider().getFieldKey("den_id"), rowKey[i]).toString() + "x" + getVl_denominationCashOutDataProvider().getValue(getVl_denominationCashOutDataProvider().getFieldKey("qtyEnter"), rowKey[i]).toString() + ")";
                }
            }
            ServletContext theApplicationsServletContext = (ServletContext) this.getExternalContext().getContext();
            String realPath = theApplicationsServletContext.getRealPath("/WEB-INF/reports/");
            NumToWords obj = new NumToWords();
            String branch = ((Vector) getSessionBean1().executeSQLSelect("select brk4_dsc from ad_brk4 where org_cod='" + getSessionBean1().getOrgaCode().toString() + "'").get(0)).get(0).toString();
            String role = ((Vector) getSessionBean1().executeSQLSelect("select rol_dsc from ad_role where rol_id='" + getSessionBean1().getRoleId().toString() + "'").get(0)).get(0).toString();
            fillParams.put("trn_branch", branch);
            fillParams.put("trn_role", role);
            fillParams.put("user", ((Vector) getSessionBean1().executeSQLSelect("select usr_nam from ad_user where usr_id='" + getSessionBean1().getUserId().toString() + "'").get(0)).get(0).toString());
            fillParams.put("cashin_amt", this.cashInAmount.getText().toString());
            fillParams.put("cashin_deno", deno_recieved);
            fillParams.put("cashin_amt_inword", obj.convert(Long.parseLong(getSessionBean1().removeCurrencyFormat(cashInAmount.getText().toString(), false))));
            fillParams.put("cashout_amt", this.cashOutAmount.getText().toString());
            fillParams.put("cashout_deno", deno_out);
            fillParams.put("cashout_amt_inword", obj.convert(Long.parseLong(getSessionBean1().removeCurrencyFormat(cashOutAmount.getText().toString(), false))));
            fillParams.put("SUBREPORT_DIR", realPath + "\\");
            report_name = "Tel_Exch_Denom_Receipt";
            getSessionBean1().generateReport(report_name, fillParams, getSessionBean1().getUserId());
            getSessionBean1().setPag_nav("");
            emptyCashOutTable();
            emptyCashInTable();
        } catch (Exception ex) {
            System.out.println("Error in btn_prt_action() DPD_View.jsp, User ID " + getSessionBean1().getUserId() + "- Exception generating report: " + ex.getMessage());
            getMes().setText(messages.get("CM043"));
        }
    }

    public String btn_post_action() {
        if (btn_post.getText().toString().equals("Post")) {
            if (checkFields()) {
                Vector vCashInDen = new Vector();
                vCashInDen = processCashInDenominations();
                Vector vCashOutDen = new Vector();
                vCashOutDen = processCashOutDenominations();
                TellerTransactions oTT = new TellerTransactions();
                int iResult = oTT.exchangeDenominations(vCashInDen, vCashOutDen, getSessionBean1().getUserId(), getSessionBean1().getOrgaCode(), null);
                if (iResult == 0) {
                    btn_post.setVisible(false);
                    btn_print.setVisible(true);
                    getMes().setText("Exchange Denominations Transaction has been successfully posted");
                } else if (iResult == 1) {
                    getMes().setText("Cashbox is either closed or inactive. This operations cannot be performed at this time. ");
                } else if (iResult == 2) {
                    getMes().setText("Transaction Time is over. Cashbox operating times do not permit this operation at this time.");
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public boolean emptyCashOutTable() {
        RowKey[] rowKey = getVl_denominationCashOutDataProvider().getRowKeys(getVl_denominationCashOutDataProvider().getRowCount(), null);
        try {
            for (int i = 0; i < rowKey.length; i++) {
                if ((getVl_denominationCashOutDataProvider().getValue(getVl_denominationCashOutDataProvider().getFieldKey("qtyEnter"), rowKey[i]).equals("") == false) && (Integer.parseInt(getVl_denominationCashOutDataProvider().getValue(getVl_denominationCashOutDataProvider().getFieldKey("qtyEnter"), rowKey[i]).toString()) > 0)) {
                    getVl_denominationCashOutDataProvider().setValue(getVl_denominationCashOutDataProvider().getFieldKey("qtyEnter"), null);
                }
            }
        } catch (Exception ex) {
            System.out.println("ExchangeDenomination, emptyCashOutTable(), " + getSessionBean1().getUserId() + " - " + ex);
        }
        return true;
    }

    public boolean emptyCashInTable() {
        RowKey[] rowKey = getVl_denominationCashInDataProvider().getRowKeys(getVl_denominationCashInDataProvider().getRowCount(), null);
        try {
            for (int i = 0; i < rowKey.length; i++) {
                if ((getVl_denominationCashInDataProvider().getValue(getVl_denominationCashInDataProvider().getFieldKey("qtyEnter"), rowKey[i]) != null) && (Integer.parseInt(getVl_denominationCashInDataProvider().getValue(getVl_denominationCashInDataProvider().getFieldKey("qtyEnter"), rowKey[i]).toString()) > 0)) {
                    getVl_denominationCashInDataProvider().setValue(getVl_denominationCashInDataProvider().getFieldKey("qtyEnter"), rowKey[i], "");
                }
            }
        } catch (Exception ex) {
            System.out.println("ExchangeDenomination, emptyCashInTable(), " + getSessionBean1().getUserId() + " - " + ex);
        }
        return true;
    }

    public String btn_ext_action() {
        try {
            this.getExternalContext().redirect("/CBA/faces/DASHBOARD.jsp");
        } catch (Exception ex) {
            return null;
        }
        return null;
    }
}
