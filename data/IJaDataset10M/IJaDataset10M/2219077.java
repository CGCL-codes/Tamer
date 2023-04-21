package cba;

import com.sun.data.provider.RowKey;
import com.sun.data.provider.impl.CachedRowSetDataProvider;
import com.sun.faces.extensions.avatar.components.AjaxZone;
import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.sql.rowset.CachedRowSetXImpl;
import com.sun.webui.jsf.component.Button;
import com.sun.webui.jsf.component.Calendar;
import com.sun.webui.jsf.component.Checkbox;
import com.sun.webui.jsf.component.DropDown;
import com.sun.webui.jsf.component.HiddenField;
import com.sun.webui.jsf.component.Label;
import com.sun.webui.jsf.component.RadioButton;
import com.sun.webui.jsf.component.TableColumn;
import com.sun.webui.jsf.component.TableRowGroup;
import com.sun.webui.jsf.component.TextArea;
import com.sun.webui.jsf.component.TextField;
import com.sun.webui.jsf.event.TableSelectPhaseListener;
import com.sun.webui.jsf.model.DefaultTableDataProvider;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.convert.BigDecimalConverter;
import javax.faces.convert.DateTimeConverter;
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
 * @author Maroof.Mahmud
 */
public class CashBoxSetup_AddUpdateRequest extends AbstractPageBean {

    final String REQUEST_TYPE = "8006";

    final String REQUEST_TYPE2 = "8007";

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
        roleDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{CashBoxSetup_AddUpdateRequest.roleRowSet}"));
        roleRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        roleRowSet.setCommand("SELECT 99999999999 AS rol_id, 'Select' AS rol_dsc  UNION ALL  (SELECT rol_id, rol_dsc FROM ad_role WHERE rol_id IN (SELECT rol_id FROM ad_user WHERE org_cod = (SELECT org_cod FROM vl_vault WHERE val_id= ?)) AND rol_id NOT IN (SELECT rol_id FROM ad_user WHERE usr_id IN (SELECT sec_usr_id FROM vl_vault UNION  SELECT pri_usr_id FROM vl_vault)) ORDER BY UPPER(rol_dsc))");
        roleRowSet.setTableName("ad_role");
        roleDataProvider2.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{CashBoxSetup_AddUpdateRequest.roleRowSet2}"));
        roleRowSet2.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        roleRowSet2.setCommand("SELECT 99999999999 AS rol_id, 'Select' AS rol_dsc  UNION ALL  (SELECT rol_id, rol_dsc FROM ad_role WHERE rol_id IN (SELECT rol_id FROM ad_user WHERE org_cod = (SELECT org_cod FROM vl_vault WHERE val_id= ?)) ORDER BY UPPER(rol_dsc))");
        roleRowSet2.setTableName("ad_role");
        orglvlDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{CashBoxSetup_AddUpdateRequest.orglvlRowSet}"));
        orglvlRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        orglvlRowSet.setCommand("SELECT '99999999999' AS par_cod, 'Select' AS par_dsc UNION ALL (SELECT par_cod,par_dsc FROM ad_parameter WHERE par_typ='OrgCode' ORDER BY par_cod)");
        orglvlRowSet.setTableName("ad_brk4");
        valNameDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{CashBoxSetup_AddUpdateRequest.valNameRowSet}"));
        valNameRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        valNameRowSet.setCommand(" SELECT '99999999999'  AS org_cod,'Select '  AS brk4_dsc UNION SELECT vaul.val_id  AS org_cod, vaul.val_id|| '-' ||tab1.brk4_dsc   AS brk4_dsc FROM ( SELECT org_cod,brk4_dsc FROM ad_brk4 WHERE org_cod IN ( SELECT fun_rel_org(?) FROM dual) AND org_cod IN (SELECT org_cod FROM vl_vault WHERE val_sts = 1 AND cas_box_req = 1 ) )tab1,vl_vault vaul  WHERE tab1.org_cod=vaul.org_cod order BY org_cod DESC");
        valNameRowSet.setTableName("ad_brk4");
        userDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{CashBoxSetup_AddUpdateRequest.userRowSet}"));
        userRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        userRowSet.setCommand("SELECT \'99999999999\' AS usr_id, \'Select\' AS usr_nam\r\nUNION ALL\r\n(SELECT usr_id, usr_id FROM ad_user WHERE status IN (1,3) AND rol_id = ?  AND org_cod= (SELECT org_cod FROM vl_vault WHERE val_id= ?)  AND usr_id NOT IN (SELECT sec_usr_id FROM vl_vault UNION  SELECT pri_usr_id FROM vl_vault  UNION SELECT off_usr_id from vl_cash_box WHERE cas_box_id <> ?) ORDER BY UPPER(usr_id))");
        userRowSet.setTableName("ad_user");
        userDataProvider2.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{CashBoxSetup_AddUpdateRequest.userRowSet2}"));
        userRowSet2.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        userRowSet2.setCommand("SELECT \'99999999999\' AS usr_id, \'Select\' AS usr_nam\r\nUNION ALL\r\n(SELECT usr_id, usr_id FROM ad_user WHERE status IN (1,3) AND rol_id = ? AND org_cod= (SELECT org_cod FROM vl_vault WHERE val_id= ?) ORDER BY UPPER(usr_id))");
        userRowSet2.setTableName("ad_user");
        vault_stsDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{CashBoxSetup_AddUpdateRequest.vault_stsRowSet}"));
        vault_stsRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        vault_stsRowSet.setCommand("SELECT \'99999999999\' AS par_cod, \'Select\' AS par_dsc\r\nUNION ALL\r\n(SELECT par_cod, par_dsc FROM ad_parameter WHERE par_typ = \'vl_sts\' AND par_sts = 1 ORDER BY UPPER(par_dsc))");
        vault_stsRowSet.setTableName("ad_parameter");
        cashboxSetup_UsersUpdateDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{SessionBean1.cashboxSetup_UsersUpdateRowSet}"));
        userUpdateDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{CashBoxSetup_AddUpdateRequest.userUpdateRowSet}"));
        userUpdateRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        userUpdateRowSet.setCommand("SELECT \'99999999999\' AS usr_id, \'Select\' AS usr_nam\r\nUNION ALL\r\n(SELECT usr_id, usr_id FROM ad_user WHERE status IN (1,3) AND rol_id = ?  AND org_cod= (SELECT org_cod FROM vl_vault WHERE val_id= ?)  AND usr_id NOT IN (SELECT sec_usr_id FROM vl_vault UNION  SELECT pri_usr_id FROM vl_vault  UNION SELECT off_usr_id from vl_cash_box WHERE cas_box_id <> ?) ORDER BY UPPER(usr_id))");
        userUpdateRowSet.setTableName("ad_parameter");
        userUpdateDataProvider2.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{CashBoxSetup_AddUpdateRequest.userUpdateRowSet2}"));
        userUpdateRowSet2.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        userUpdateRowSet2.setCommand("SELECT \'99999999999\' AS usr_id, \'Select\' AS usr_nam\r\nUNION ALL\r\n(SELECT usr_id, usr_id FROM ad_user WHERE rol_id = ?  AND org_cod= (SELECT org_cod FROM vl_vault WHERE val_id= ?) ORDER BY UPPER(usr_id))");
        userUpdateRowSet2.setTableName("ad_parameter");
        cashBoxSetup_CommentsRowSet.setDataSourceName("java:comp/env/jdbc/public_PostgreSQL");
        cashBoxSetup_CommentsRowSet.setCommand("SELECT COALESCE(first.comments, '<No Comments Entered>') AS Comment, CASE WHEN first.req_sts NOT IN ('0','97','98','99') AND first.mod_dat = (SELECT MIN(mod_dat) FROM vl_vault_v WHERE val_id = first.val_id) THEN 'Initiated & Forwarded to ' WHEN first.req_sts NOT IN ('0','97','98','99') AND first.mod_dat > (SELECT MIN(mod_dat) FROM vl_vault_v WHERE val_id = first.val_id) THEN 'Update Request Forwarded to ' ELSE 'Initiated & Forwarded' END || fun_req_sts(first.req_sts) AS Action, first.mod_by, ad_brk4.brk4_dsc, first.mod_dat FROM vl_vault_v first INNER JOIN ad_user ON first.mod_by = ad_user.usr_id INNER JOIN ad_role ON ad_user.rol_id = ad_role.rol_id LEFT OUTER JOIN ad_brk4 ON ad_user.org_cod = ad_brk4.org_cod WHERE (first.req_id in (SELECT distinct req_id FROM vl_vault_v WHERE val_id= 0 ) or first.req_id='')AND first.ver_id = '0' AND 1<>1");
        cashBoxSetup_CommentsRowSet.setTableName("vl_vault_v");
        cashBoxSetup_CommentsDataProvider.setCachedRowSet((javax.sql.rowset.CachedRowSet) getValue("#{CashBoxSetup_AddUpdateRequest.cashBoxSetup_CommentsRowSet}"));
    }

    private CachedRowSetXImpl cashBoxSetup_CommentsRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getCashBoxSetup_CommentsRowSet() {
        return cashBoxSetup_CommentsRowSet;
    }

    public void setCashBoxSetup_CommentsRowSet(CachedRowSetXImpl crsxi) {
        this.cashBoxSetup_CommentsRowSet = crsxi;
    }

    private CachedRowSetDataProvider cashBoxSetup_CommentsDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getCashBoxSetup_CommentsDataProvider() {
        return cashBoxSetup_CommentsDataProvider;
    }

    public void setCashBoxSetup_CommentsDataProvider(CachedRowSetDataProvider crsdp) {
        this.cashBoxSetup_CommentsDataProvider = crsdp;
    }

    private CachedRowSetDataProvider cashboxSetup_UsersUpdateDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getCashboxSetup_UsersUpdateDataProvider() {
        return cashboxSetup_UsersUpdateDataProvider;
    }

    public void setCashboxSetup_UsersUpdateDataProvider(CachedRowSetDataProvider crsdp) {
        this.cashboxSetup_UsersUpdateDataProvider = crsdp;
    }

    private CachedRowSetDataProvider roleDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getRoleDataProvider() {
        return roleDataProvider;
    }

    public void setRoleDataProvider(CachedRowSetDataProvider crsdp) {
        this.roleDataProvider = crsdp;
    }

    private CachedRowSetXImpl roleRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getRoleRowSet() {
        return roleRowSet;
    }

    public void setRoleRowSet(CachedRowSetXImpl crsxi) {
        this.roleRowSet = crsxi;
    }

    private CachedRowSetDataProvider roleDataProvider2 = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getRoleDataProvider2() {
        return roleDataProvider2;
    }

    public void setRoleDataProvider2(CachedRowSetDataProvider crsdp) {
        this.roleDataProvider2 = crsdp;
    }

    private CachedRowSetXImpl roleRowSet2 = new CachedRowSetXImpl();

    public CachedRowSetXImpl getRoleRowSet2() {
        return roleRowSet2;
    }

    public void setRoleRowSet2(CachedRowSetXImpl crsxi) {
        this.roleRowSet2 = crsxi;
    }

    private CachedRowSetDataProvider orglvlDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getOrglvlDataProvider() {
        return orglvlDataProvider;
    }

    public void setOrglvlDataProvider(CachedRowSetDataProvider crsdp) {
        this.orglvlDataProvider = crsdp;
    }

    private CachedRowSetXImpl orglvlRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getOrglvlRowSet() {
        return orglvlRowSet;
    }

    public void setOrglvlRowSet(CachedRowSetXImpl crsxi) {
        this.orglvlRowSet = crsxi;
    }

    private CachedRowSetDataProvider valNameDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getValNameDataProvider() {
        return valNameDataProvider;
    }

    public void setValNameDataProvider(CachedRowSetDataProvider crsdp) {
        this.valNameDataProvider = crsdp;
    }

    private CachedRowSetXImpl valNameRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getValNameRowSet() {
        return valNameRowSet;
    }

    public void setValNameRowSet(CachedRowSetXImpl crsxi) {
        this.valNameRowSet = crsxi;
    }

    private CachedRowSetDataProvider userDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getUserDataProvider() {
        return userDataProvider;
    }

    public void setUserDataProvider(CachedRowSetDataProvider crsdp) {
        this.userDataProvider = crsdp;
    }

    private CachedRowSetXImpl userRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getUserRowSet() {
        return userRowSet;
    }

    public void setUserRowSet(CachedRowSetXImpl crsxi) {
        this.userRowSet = crsxi;
    }

    private CachedRowSetDataProvider userDataProvider2 = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getUserDataProvider2() {
        return userDataProvider2;
    }

    public void setUserDataProvider2(CachedRowSetDataProvider crsdp) {
        this.userDataProvider2 = crsdp;
    }

    private CachedRowSetXImpl userRowSet2 = new CachedRowSetXImpl();

    public CachedRowSetXImpl getUserRowSet2() {
        return userRowSet2;
    }

    public void setUserRowSet2(CachedRowSetXImpl crsxi) {
        this.userRowSet2 = crsxi;
    }

    private CachedRowSetDataProvider vault_stsDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getVault_stsDataProvider() {
        return vault_stsDataProvider;
    }

    public void setVault_stsDataProvider(CachedRowSetDataProvider crsdp) {
        this.vault_stsDataProvider = crsdp;
    }

    private CachedRowSetXImpl vault_stsRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getVault_stsRowSet() {
        return vault_stsRowSet;
    }

    public void setVault_stsRowSet(CachedRowSetXImpl crsxi) {
        this.vault_stsRowSet = crsxi;
    }

    private CachedRowSetDataProvider indiv_IDDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getIndiv_IDDataProvider() {
        return indiv_IDDataProvider;
    }

    public void setIndiv_IDDataProvider(CachedRowSetDataProvider crsdp) {
        this.indiv_IDDataProvider = crsdp;
    }

    private TextArea mes = new TextArea();

    public TextArea getMes() {
        return mes;
    }

    public void setMes(TextArea ta) {
        this.mes = ta;
    }

    private Button btn_submit = new Button();

    public Button getBtn_submit() {
        return btn_submit;
    }

    public void setBtn_submit(Button b) {
        this.btn_submit = b;
    }

    private HiddenField updateAddField = new HiddenField();

    public HiddenField getUpdateAddField() {
        return updateAddField;
    }

    public void setUpdateAddField(HiddenField hf) {
        this.updateAddField = hf;
    }

    private TableSelectPhaseListener tablePhaseListener = new TableSelectPhaseListener();

    public void setSelected(Object object) {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        if (rowKey != null) {
            tablePhaseListener.setSelected(rowKey, object);
        }
    }

    public void clearSelected() {
        tablePhaseListener.clear();
    }

    public Object getSelected() {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        return tablePhaseListener.getSelected(rowKey);
    }

    public Object getSelectedValue() {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        return (rowKey != null) ? rowKey.getRowId() : null;
    }

    public boolean getSelectedState() {
        RowKey rowKey = (RowKey) getValue("#{currentRow.tableRow}");
        return tablePhaseListener.isSelected(rowKey);
    }

    private HiddenField reqID = new HiddenField();

    public HiddenField getReqID() {
        return reqID;
    }

    public void setReqID(HiddenField hf) {
        this.reqID = hf;
    }

    private HiddenField version = new HiddenField();

    public HiddenField getVersion() {
        return version;
    }

    public void setVersion(HiddenField hf) {
        this.version = hf;
    }

    private HiddenField prevName = new HiddenField();

    public HiddenField getPrevName() {
        return prevName;
    }

    public void setPrevName(HiddenField hf) {
        this.prevName = hf;
    }

    private DropDown ddl_rol_off = new DropDown();

    public DropDown getDdl_rol_off() {
        return ddl_rol_off;
    }

    public void setDdl_rol_off(DropDown dd) {
        this.ddl_rol_off = dd;
    }

    private DropDown ddl_rol_sup = new DropDown();

    public DropDown getDdl_rol_sup() {
        return ddl_rol_sup;
    }

    public void setDdl_rol_sup(DropDown dd) {
        this.ddl_rol_sup = dd;
    }

    private DropDown ddl_usr_off = new DropDown();

    public DropDown getDdl_usr_off() {
        return ddl_usr_off;
    }

    public void setDdl_usr_off(DropDown dd) {
        this.ddl_usr_off = dd;
    }

    private DropDown ddl_usr_sup = new DropDown();

    public DropDown getDdl_usr_sup() {
        return ddl_usr_sup;
    }

    public void setDdl_usr_sup(DropDown dd) {
        this.ddl_usr_sup = dd;
    }

    private BigDecimalConverter ddl_org_levelConverter = new BigDecimalConverter();

    public BigDecimalConverter getDdl_org_levelConverter() {
        return ddl_org_levelConverter;
    }

    public void setDdl_org_levelConverter(BigDecimalConverter bdc) {
        this.ddl_org_levelConverter = bdc;
    }

    private BigDecimalConverter ddl_val_nameConverter = new BigDecimalConverter();

    public BigDecimalConverter getDdl_val_nameConverter() {
        return ddl_val_nameConverter;
    }

    public void setDdl_val_nameConverter(BigDecimalConverter bdc) {
        this.ddl_val_nameConverter = bdc;
    }

    private BigDecimalConverter ddl_val_stsConverter = new BigDecimalConverter();

    public BigDecimalConverter getDdl_val_stsConverter() {
        return ddl_val_stsConverter;
    }

    public void setDdl_val_stsConverter(BigDecimalConverter bdc) {
        this.ddl_val_stsConverter = bdc;
    }

    private BigDecimalConverter ddl_rol_offConverter = new BigDecimalConverter();

    public BigDecimalConverter getDdl_rol_offConverter() {
        return ddl_rol_offConverter;
    }

    public void setDdl_rol_offConverter(BigDecimalConverter bdc) {
        this.ddl_rol_offConverter = bdc;
    }

    private BigDecimalConverter ddl_rol_supConverter = new BigDecimalConverter();

    public BigDecimalConverter getDdl_rol_supConverter() {
        return ddl_rol_supConverter;
    }

    public void setDdl_rol_supConverter(BigDecimalConverter bdc) {
        this.ddl_rol_supConverter = bdc;
    }

    private DropDown ddl_org_level = new DropDown();

    public DropDown getDdl_org_level() {
        return ddl_org_level;
    }

    public void setDdl_org_level(DropDown dd) {
        this.ddl_org_level = dd;
    }

    private DropDown ddl_val_name = new DropDown();

    public DropDown getDdl_val_name() {
        return ddl_val_name;
    }

    public void setDdl_val_name(DropDown dd) {
        this.ddl_val_name = dd;
    }

    private TextField fld_min_cas_box_lmt = new TextField();

    public TextField getFld_min_cas_box_lmt() {
        return fld_min_cas_box_lmt;
    }

    public void setFld_min_cas_box_lmt(TextField tf) {
        this.fld_min_cas_box_lmt = tf;
    }

    private TextField fld_max_cas_box_lmt = new TextField();

    public TextField getFld_max_cas_box_lmt() {
        return fld_max_cas_box_lmt;
    }

    public void setFld_max_cas_box_lmt(TextField tf) {
        this.fld_max_cas_box_lmt = tf;
    }

    private TextField fld_ngt_lmt = new TextField();

    public TextField getFld_ngt_lmt() {
        return fld_ngt_lmt;
    }

    public void setFld_ngt_lmt(TextField tf) {
        this.fld_ngt_lmt = tf;
    }

    private Checkbox chk_int_trns_alw = new Checkbox();

    public Checkbox getChk_int_trns_alw() {
        return chk_int_trns_alw;
    }

    public void setChk_int_trns_alw(Checkbox c) {
        this.chk_int_trns_alw = c;
    }

    private TextField fld_max_wdrw_lmt = new TextField();

    public TextField getFld_max_wdrw_lmt() {
        return fld_max_wdrw_lmt;
    }

    public void setFld_max_wdrw_lmt(TextField tf) {
        this.fld_max_wdrw_lmt = tf;
    }

    private Checkbox chk_ovr_lmt_alw = new Checkbox();

    public Checkbox getChk_ovr_lmt_alw() {
        return chk_ovr_lmt_alw;
    }

    public void setChk_ovr_lmt_alw(Checkbox c) {
        this.chk_ovr_lmt_alw = c;
    }

    private Checkbox chk_cshbx_alw = new Checkbox();

    public Checkbox getChk_cshbx_alw() {
        return chk_cshbx_alw;
    }

    public void setChk_cshbx_alw(Checkbox c) {
        this.chk_cshbx_alw = c;
    }

    private TextArea txt_comments = new TextArea();

    public TextArea getTxt_comments() {
        return txt_comments;
    }

    public void setTxt_comments(TextArea ta) {
        this.txt_comments = ta;
    }

    private TextField fld_cas_box_id = new TextField();

    public TextField getFld_cas_box_id() {
        return fld_cas_box_id;
    }

    public void setFld_cas_box_id(TextField tf) {
        this.fld_cas_box_id = tf;
    }

    private DropDown ddl_cas_box_sts = new DropDown();

    public DropDown getDdl_cas_box_sts() {
        return ddl_cas_box_sts;
    }

    public void setDdl_cas_box_sts(DropDown dd) {
        this.ddl_cas_box_sts = dd;
    }

    private HiddenField rec_type = new HiddenField();

    public HiddenField getRec_type() {
        return rec_type;
    }

    public void setRec_type(HiddenField hf) {
        this.rec_type = hf;
    }

    private HiddenField fld_int_trns_alw = new HiddenField();

    public HiddenField getFld_int_trns_alw() {
        return fld_int_trns_alw;
    }

    public void setFld_int_trns_alw(HiddenField hf) {
        this.fld_int_trns_alw = hf;
    }

    private HiddenField fld_ovr_lmt_alw = new HiddenField();

    public HiddenField getFld_ovr_lmt_alw() {
        return fld_ovr_lmt_alw;
    }

    public void setFld_ovr_lmt_alw(HiddenField hf) {
        this.fld_ovr_lmt_alw = hf;
    }

    private HiddenField fld_cshbx_alw = new HiddenField();

    public HiddenField getFld_cshbx_alw() {
        return fld_cshbx_alw;
    }

    public void setFld_cshbx_alw(HiddenField hf) {
        this.fld_cshbx_alw = hf;
    }

    private HiddenField fld_atm_atch = new HiddenField();

    public HiddenField getFld_atm_atch() {
        return fld_atm_atch;
    }

    public void setFld_atm_atch(HiddenField hf) {
        this.fld_atm_atch = hf;
    }

    private HiddenField flow_id = new HiddenField();

    public HiddenField getFlow_id() {
        return flow_id;
    }

    public void setFlow_id(HiddenField hf) {
        this.flow_id = hf;
    }

    private HiddenField cas_box_id = new HiddenField();

    public HiddenField getCas_box_id() {
        return cas_box_id;
    }

    public void setCas_box_id(HiddenField hf) {
        this.cas_box_id = hf;
    }

    private Label lbl_man = new Label();

    public Label getLbl_man() {
        return lbl_man;
    }

    public void setLbl_man(Label l) {
        this.lbl_man = l;
    }

    private HiddenField valName = new HiddenField();

    public HiddenField getValName() {
        return valName;
    }

    public void setValName(HiddenField hf) {
        this.valName = hf;
    }

    private TextField fld_min_exch_lmt = new TextField();

    public TextField getFld_min_exch_lmt() {
        return fld_min_exch_lmt;
    }

    public void setFld_min_exch_lmt(TextField tf) {
        this.fld_min_exch_lmt = tf;
    }

    private TextField fld_cas_box_acc_time_frm = new TextField();

    public TextField getFld_cas_box_acc_time_frm() {
        return fld_cas_box_acc_time_frm;
    }

    public void setFld_cas_box_acc_time_frm(TextField tf) {
        this.fld_cas_box_acc_time_frm = tf;
    }

    private TextField fld_cas_box_acc_time_to = new TextField();

    public TextField getFld_cas_box_acc_time_to() {
        return fld_cas_box_acc_time_to;
    }

    public void setFld_cas_box_acc_time_to(TextField tf) {
        this.fld_cas_box_acc_time_to = tf;
    }

    private TextField fld_cas_box_name = new TextField();

    public TextField getFld_cas_box_name() {
        return fld_cas_box_name;
    }

    public void setFld_cas_box_name(TextField tf) {
        this.fld_cas_box_name = tf;
    }

    private CachedRowSetDataProvider userUpdateDataProvider = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getUserUpdateDataProvider() {
        return userUpdateDataProvider;
    }

    public void setUserUpdateDataProvider(CachedRowSetDataProvider crsdp) {
        this.userUpdateDataProvider = crsdp;
    }

    private CachedRowSetXImpl userUpdateRowSet = new CachedRowSetXImpl();

    public CachedRowSetXImpl getUserUpdateRowSet() {
        return userUpdateRowSet;
    }

    public void setUserUpdateRowSet(CachedRowSetXImpl crsxi) {
        this.userUpdateRowSet = crsxi;
    }

    private CachedRowSetDataProvider userUpdateDataProvider2 = new CachedRowSetDataProvider();

    public CachedRowSetDataProvider getUserUpdateDataProvider2() {
        return userUpdateDataProvider2;
    }

    public void setUserUpdateDataProvider2(CachedRowSetDataProvider crsdp) {
        this.userUpdateDataProvider2 = crsdp;
    }

    private CachedRowSetXImpl userUpdateRowSet2 = new CachedRowSetXImpl();

    public CachedRowSetXImpl getUserUpdateRowSet2() {
        return userUpdateRowSet2;
    }

    public void setUserUpdateRowSet2(CachedRowSetXImpl crsxi) {
        this.userUpdateRowSet2 = crsxi;
    }

    private HiddenField userUpdateTblChk = new HiddenField();

    public HiddenField getUserUpdateTblChk() {
        return userUpdateTblChk;
    }

    public void setUserUpdateTblChk(HiddenField hf) {
        this.userUpdateTblChk = hf;
    }

    private TableColumn tableColumn2 = new TableColumn();

    public TableColumn getTableColumn2() {
        return tableColumn2;
    }

    public void setTableColumn2(TableColumn tc) {
        this.tableColumn2 = tc;
    }

    private TableColumn tableColumn3 = new TableColumn();

    public TableColumn getTableColumn3() {
        return tableColumn3;
    }

    public void setTableColumn3(TableColumn tc) {
        this.tableColumn3 = tc;
    }

    private Checkbox chk_approval_req = new Checkbox();

    public Checkbox getChk_approval_req() {
        return chk_approval_req;
    }

    public void setChk_approval_req(Checkbox c) {
        this.chk_approval_req = c;
    }

    private HiddenField fld_approval_req = new HiddenField();

    public HiddenField getFld_approval_req() {
        return fld_approval_req;
    }

    public void setFld_approval_req(HiddenField hf) {
        this.fld_approval_req = hf;
    }

    private BigDecimalConverter bigDecimalConverter1 = new BigDecimalConverter();

    public BigDecimalConverter getBigDecimalConverter1() {
        return bigDecimalConverter1;
    }

    public void setBigDecimalConverter1(BigDecimalConverter bdc) {
        this.bigDecimalConverter1 = bdc;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public CashBoxSetup_AddUpdateRequest() {
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
            getSessionBean1().validateScreen("CashBoxSetupGrid.jsp");
            sel = getSessionBean1().getPag_nav();
            String[] err = { "CM004", "CM010", "CM023", "CM024", "VL003", "VL004", "VL005", "VL014", "VL069" };
            messages = getSessionBean1().getMessages(err);
            if (messages.containsKey("Exception")) {
                getMes().setText(messages.get("Exception"));
                getBtn_submit().setDisabled(true);
            }
        } catch (Exception ex) {
            System.out.println("CashBoxSetup_AddUpdateRequest, init(), " + getSessionBean1().getUserId() + " - " + ex);
            try {
                this.getExternalContext().redirect("/CBA/faces/InitError.jsp");
            } catch (IOException ioe) {
            }
        }
        try {
            _init();
        } catch (Exception e) {
            log("CashBoxSetup_AddUpdateRequest Initialization Failure", e);
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
    private String sel;

    @Override
    public void prerender() {
        try {
            if (sel.equals("0") && !getSessionBean1().getPag_nav().equals("0")) {
                sel = getSessionBean1().getPag_nav();
            }
            if (sel.equals("Add")) {
                initComponents();
            } else if (sel.equals("Update")) {
                loadData();
                getTxt_comments().setText("");
            } else if (sel.equals("View")) {
                loadData();
                fieldsAccessibility(true);
                getLbl_man().setText("View CashBox Request");
            }
            getUpdateAddField().setText(sel);
            getSessionBean1().setPag_nav("0");
        } catch (Exception ex) {
            System.out.println("CashBoxSetup_AddUpdateRequest, prerender(), " + getSessionBean1().getUserId() + " - " + ex);
            try {
                this.getExternalContext().redirect("/CBA/faces/InitError.jsp");
            } catch (IOException ioe) {
            }
        }
    }

    private void checkUser(String record_typ) throws Exception {
        try {
            if (record_typ == null) {
                record_typ = REQUEST_TYPE;
                getLbl_man().setText("Add CashBox Request");
            } else {
                if (record_typ.equals("add")) {
                    record_typ = REQUEST_TYPE;
                    getLbl_man().setText("Add CashBox Request");
                } else {
                    record_typ = REQUEST_TYPE2;
                    getLbl_man().setText("Update CashBox Request");
                }
            }
            getBtn_submit().setDisabled(true);
            Vector handleButtons = getSessionBean1().executeSQLSelect("SELECT flow_id, con_id FROM ad_workflow WHERE rol_id = '" + getSessionBean1().getRoleId() + "' AND req_type = '" + record_typ + "' ORDER BY flow_id");
            for (int i = 0; i < handleButtons.size(); i++) {
                if (((Vector) handleButtons.get(i)).get(0).toString().equals(getSessionBean1().getInitiatorFlowId(record_typ)) && !((Vector) handleButtons.get(i)).get(1).toString().equals("0")) {
                    getBtn_submit().setDisabled(false);
                }
            }
            if (getSessionBean1().getNextWorkflow(record_typ, getFlow_id().getText().toString()).equals("99")) {
                getBtn_submit().setText("Initiate");
            } else {
                getBtn_submit().setText("Request");
            }
        } catch (Exception ex) {
            System.out.println("CashBoxSetup_AddUpdateRequest, checkUser(), " + getSessionBean1().getUserId() + " - " + ex);
            throw ex;
        }
    }

    private void fieldsAccessibility(Boolean decision) {
        getFld_cas_box_id().setDisabled(decision);
        getFld_cas_box_name().setDisabled(decision);
        getDdl_org_level().setDisabled(decision);
        getDdl_val_name().setDisabled(decision);
        getDdl_rol_off().setDisabled(decision);
        getDdl_usr_off().setDisabled(decision);
        getDdl_rol_sup().setDisabled(decision);
        getDdl_usr_sup().setDisabled(decision);
        getFld_cas_box_acc_time_frm().setDisabled(decision);
        getFld_cas_box_acc_time_to().setDisabled(decision);
        getFld_min_cas_box_lmt().setDisabled(decision);
        getFld_max_cas_box_lmt().setDisabled(decision);
        getFld_ngt_lmt().setDisabled(decision);
        getChk_int_trns_alw().setDisabled(decision);
        getFld_min_exch_lmt().setDisabled(decision);
        getFld_max_wdrw_lmt().setDisabled(decision);
        getDdl_cas_box_sts().setDisabled(decision);
        getChk_ovr_lmt_alw().setDisabled(decision);
        getChk_cshbx_alw().setDisabled(decision);
        getTxt_comments().setDisabled(decision);
        getChk_approval_req().setDisabled(decision);
        getBtn_submit().setDisabled(decision);
    }

    private void initComponents() throws Exception {
        getReqID().setText(null);
        getVersion().setText(null);
        getFlow_id().setText(getSessionBean1().getInitiatorFlowId(REQUEST_TYPE));
        getRec_type().setText("add");
        getCas_box_id().setText(null);
        getValName().setText("");
        getUserRowSet().setObject(1, "");
        getUserRowSet().setObject(2, "");
        getUserRowSet().setObject(3, "");
        getUserRowSet().execute();
        getUserRowSet2().setObject(1, "");
        getUserRowSet2().setObject(2, "");
        getUserRowSet2().execute();
        getValNameRowSet().setObject(1, "");
        getValNameRowSet().execute();
        getRoleRowSet().setObject(1, "");
        getRoleRowSet().execute();
        getRoleRowSet2().setObject(1, "");
        getRoleRowSet2().execute();
        getDdl_val_name().setDisabled(true);
        getDdl_rol_off().setDisabled(true);
        getDdl_rol_sup().setDisabled(true);
        getDdl_usr_off().setDisabled(true);
        getDdl_usr_sup().setDisabled(true);
        getUserUpdateRowSet().setObject(1, "");
        getUserUpdateRowSet().setObject(2, "");
        getUserUpdateRowSet().setObject(3, "");
        getUserUpdateRowSet().execute();
        getUserUpdateRowSet2().setObject(1, "");
        getUserUpdateRowSet2().setObject(2, "");
        getUserUpdateRowSet2().execute();
        checkUser(null);
    }

    private void loadData() throws Exception {
        getUserUpdateRowSet().setObject(1, "");
        getUserUpdateRowSet().setObject(2, "");
        getUserUpdateRowSet().setObject(3, "");
        getUserUpdateRowSet().execute();
        getUserUpdateRowSet2().setObject(1, "");
        getUserUpdateRowSet2().setObject(2, "");
        getUserUpdateRowSet2().execute();
        getReqID().setText(getSessionBean1().getUpd_val());
        getVersion().setText(getSessionBean1().getUpd_val2());
        if (getSessionBean1().getUpd_val3() == null) {
            if (getSessionBean1().getUpd_val3().toString().length() == 0) getFlow_id().setText(getSessionBean1().getInitiatorFlowId(REQUEST_TYPE2));
        } else {
            if (getSessionBean1().getUpd_val3().toString().length() == 0) getFlow_id().setText(getSessionBean1().getInitiatorFlowId(REQUEST_TYPE2)); else getFlow_id().setText(getSessionBean1().getUpd_val3());
        }
        getRec_type().setText(getSessionBean1().getUpd_val4());
        String strLoadQuery = "SELECT cas_box_id,cas_box_name,(SELECT par_cod FROM ad_parameter WHERE par_typ='OrgCode' and par_dsc=fun_org_lvl(org_cod)) as OrgLevelCode, org_cod,val_id as val_nam,(SELECT rol_id FROM ad_user WHERE usr_id=off_usr_id) as off_rol_id, off_usr_id,(SELECT rol_id FROM ad_user WHERE usr_id=sup_usr_id) as sup_rol_id, sup_usr_id,TO_CHAR(acc_time_fr,'HH24:MI'),TO_CHAR(acc_time_to,'HH24:MI'), min_cas_box_lmt, max_cas_box_lmt, nig_cas_box_lmt,  CASE WHEN int_lev_trnf=1 THEN true ELSE false END as int_lev_trnf, max_trnf_lmt,max_wdr_lmt,cas_box_sts, CASE WHEN ove_allo=1 THEN true ELSE false END as ove_allo, CASE WHEN mob_cas_box=1 THEN true ELSE false END as mob_cas_box,CASE WHEN eod_trnf=1 THEN true ELSE false END as eod_trnf,comments,CASE WHEN apr_req=1 THEN true ELSE false END as apr_req FROM vl_cash_box_v  a WHERE req_id ='" + getReqID().getText() + "' AND ver_id = '" + getVersion().getText() + "'";
        if (getRec_type() != null) {
            if (getRec_type().getText().toString().equals("approved")) {
                getVersion().setText(null);
                getReqID().setText(null);
                getCas_box_id().setText(getSessionBean1().getUpd_val1());
                strLoadQuery = "SELECT cas_box_id,cas_box_name,(SELECT par_cod FROM ad_parameter WHERE par_typ='OrgCode' and par_dsc=fun_org_lvl(org_cod)) as OrgLevelCode, org_cod,val_id as val_nam,(SELECT rol_id FROM ad_user WHERE usr_id=off_usr_id) as off_rol_id, off_usr_id,(SELECT rol_id FROM ad_user WHERE usr_id=sup_usr_id) as sup_rol_id, sup_usr_id,TO_CHAR(acc_time_fr,'HH24:MI'),TO_CHAR(acc_time_to,'HH24:MI'), min_cas_box_lmt, max_cas_box_lmt, nig_cas_box_lmt,  CASE WHEN int_lev_trnf=1 THEN true ELSE false END as int_lev_trnf, max_trnf_lmt,max_wdr_lmt,cas_box_sts, CASE WHEN ove_allo=1 THEN true ELSE false END as ove_allo, CASE WHEN mob_cas_box=1 THEN true ELSE false END as mob_cas_box,CASE WHEN eod_trnf=1 THEN true ELSE false END as eod_trnf,(SELECT comments FROM vl_cash_box_v WHERE cas_box_id=a.cas_box_id AND req_sts=99 AND req_id=(SELECT MAX(req_id) FROM vl_cash_box_v WHERE cas_box_id=a.cas_box_id  AND comments <> '' AND REQ_STS=99)) as comments,CASE WHEN apr_req=1 THEN true ELSE false END as apr_req FROM vl_cash_box a WHERE cas_box_id='" + getCas_box_id().getText() + "' ";
            } else if (getRec_type().getText().toString().equals("update")) {
                getCas_box_id().setText(getSessionBean1().getUpd_val1());
                strLoadQuery = strLoadQuery + " AND cas_box_id='" + getCas_box_id().getText() + "' ";
            }
            getDdl_org_level().setDisabled(true);
            getDdl_val_name().setDisabled(true);
        }
        Vector cashBoxDetails = (Vector) getSessionBean1().executeSQLSelect(strLoadQuery).get(0);
        String tempCashBoxId = (cashBoxDetails.get(0) == null) ? "0" : cashBoxDetails.get(0).toString();
        getFld_cas_box_id().setText(cashBoxDetails.get(0));
        getFld_cas_box_name().setText(cashBoxDetails.get(1));
        getDdl_org_level().setSelected(cashBoxDetails.get(2));
        getValNameRowSet().setObject(1, cashBoxDetails.get(2));
        getValNameRowSet().execute();
        getDdl_val_name().setSelected(cashBoxDetails.get(4));
        getRoleRowSet().setObject(1, cashBoxDetails.get(4));
        getRoleRowSet().execute();
        getRoleRowSet2().setObject(1, cashBoxDetails.get(4));
        getRoleRowSet2().execute();
        getDdl_rol_off().setSelected(cashBoxDetails.get(5));
        getUserRowSet().setObject(1, cashBoxDetails.get(5));
        getUserRowSet().setObject(2, cashBoxDetails.get(4));
        getUserRowSet().setObject(3, tempCashBoxId);
        getUserRowSet().execute();
        getDdl_usr_off().setSelected(cashBoxDetails.get(6));
        getDdl_rol_sup().setSelected(cashBoxDetails.get(7));
        getUserRowSet2().setObject(1, cashBoxDetails.get(7));
        getUserRowSet2().setObject(2, cashBoxDetails.get(4));
        getUserRowSet2().execute();
        getDdl_usr_sup().setSelected(cashBoxDetails.get(8));
        getFld_cas_box_acc_time_frm().setText(cashBoxDetails.get(9));
        getFld_cas_box_acc_time_to().setText(cashBoxDetails.get(10));
        getFld_min_cas_box_lmt().setText(getSessionBean1().formatCurrency(cashBoxDetails.get(11).toString()));
        getFld_max_cas_box_lmt().setText(getSessionBean1().formatCurrency(cashBoxDetails.get(12).toString()));
        getFld_ngt_lmt().setText(getSessionBean1().formatCurrency(cashBoxDetails.get(13).toString()));
        getChk_int_trns_alw().setSelected(cashBoxDetails.get(14));
        getFld_int_trns_alw().setText(cashBoxDetails.get(14).toString().equals("true") ? "1" : "0");
        getFld_min_exch_lmt().setText(getSessionBean1().formatCurrency(cashBoxDetails.get(15).toString()));
        getFld_max_wdrw_lmt().setText(getSessionBean1().formatCurrency(cashBoxDetails.get(16).toString()));
        getDdl_cas_box_sts().setSelected(cashBoxDetails.get(17));
        getChk_ovr_lmt_alw().setSelected(cashBoxDetails.get(18));
        getFld_ovr_lmt_alw().setText(cashBoxDetails.get(18).toString().equals("true") ? "1" : "0");
        getChk_cshbx_alw().setSelected(cashBoxDetails.get(19));
        getFld_cshbx_alw().setText(cashBoxDetails.get(19).toString().equals("true") ? "1" : "0");
        getTxt_comments().setText(cashBoxDetails.get(21));
        String tempComments = "SELECT COALESCE(first.comments, '<No Comments Entered>') AS Comment, CASE WHEN first.req_sts NOT IN ('0','97','98','99') AND first.mod_dat = (SELECT MIN(mod_dat) FROM vl_cash_box_v WHERE cas_box_id = first.cas_box_id) THEN 'Initiated & Forwarded to ' WHEN first.req_sts NOT IN ('0','97','98','99') AND first.mod_dat > (SELECT MIN(mod_dat) FROM vl_cash_box_v WHERE cas_box_id = first.cas_box_id) THEN 'Update Request Forwarded to '  WHEN first.req_sts  IN ('0','97','98','99')  THEN '' ELSE 'Initiated & Forwarded to ' END || fun_req_sts(first.req_sts) AS Action, first.mod_by, ad_brk4.brk4_dsc, to_char(first.mod_dat,'dd/mm/yyyy hh:mi' )as mod_dat,first.mod_dat as mod_date FROM vl_cash_box_v first INNER JOIN ad_user ON first.mod_by = ad_user.usr_id INNER JOIN ad_role ON ad_user.rol_id = ad_role.rol_id LEFT OUTER JOIN ad_brk4 ON ad_user.org_cod = ad_brk4.org_cod WHERE (first.req_id in (SELECT distinct req_id FROM vl_cash_box_v WHERE cas_box_id=" + tempCashBoxId + ") or first.req_id='" + getReqID().getText() + "')AND first.ver_id = '0' AND first.comments <> '' AND CASE WHEN " + tempCashBoxId + " = 0 THEN 1=1 ELSE cas_box_id=" + tempCashBoxId + " END UNION ALL SELECT DISTINCT COALESCE(nextCashBox.comments, '<No Comments Entered>') AS Comment, CASE WHEN nextCashBox.req_sts NOT IN ('0','97','98','99') THEN (CASE WHEN nextCashBox.flow_id > prevCashBox.flow_id THEN 'Forwarded to ' ELSE 'Returned to ' END || fun_req_sts(nextCashBox.req_sts)) ELSE fun_req_sts(nextCashBox.req_sts) END AS Action, nextCashBox.mod_by, ad_brk4.brk4_dsc, to_char(nextCashBox.mod_dat,'dd/mm/yyyy hh:mi' )as mod_dat,nextCashBox.mod_dat as mod_date FROM vl_cash_box_v prevCashBox INNER JOIN vl_cash_box_v nextCashBox ON prevCashBox.ver_id + 1 = nextCashBox.ver_id AND prevCashBox.req_id = nextCashBox.req_id INNER JOIN ad_user on nextCashBox.mod_by = ad_user.usr_id INNER JOIN ad_role on ad_user.rol_id = ad_role.rol_id INNER JOIN ad_brk4 ON ad_user.org_cod = ad_brk4.org_cod WHERE ((prevCashBox.req_id = (SELECT min(req_id) from vl_cash_box_v where cas_box_id=" + tempCashBoxId + ") or prevCashBox.req_id='" + getReqID().getText() + "') AND nextCashBox.comments <> '') OR ((prevCashBox.req_id IN (SELECT req_id from vl_cash_box_v where cas_box_id = prevCashBox.cas_box_id) or prevCashBox.req_id='" + getReqID().getText() + "') AND prevCashBox.req_id NOT IN (SELECT min(req_id) FROM vl_cash_box_v WHERE cas_box_id = prevCashBox.cas_box_id) AND prevCashBox.cas_box_id=" + tempCashBoxId + " AND prevCashBox.comments <> '') AND nextCashBox.comments <> '' ORDER BY mod_date desc";
        getCashBoxSetup_CommentsRowSet().setCommand(tempComments);
        if (getRec_type().getText().toString().equals("add")) {
            Vector cashBoxExistsResult = (Vector) getSessionBean1().executeSQLSelect("SELECT (SELECT count(*) FROM vl_cash_box WHERE org_cod = '" + cashBoxDetails.get(3) + "') as extChk FROM DUAL").get(0);
            if (Integer.parseInt(cashBoxExistsResult.get(0).toString()) > 0) {
                getDdl_rol_off().setDisabled(true);
                getDdl_rol_sup().setDisabled(true);
            }
        } else {
            userUpdateTableConf();
        }
        getChk_approval_req().setSelected(cashBoxDetails.get(22));
        getFld_approval_req().setText(cashBoxDetails.get(22).toString().equals("true") ? "1" : "0");
        checkUser(getSessionBean1().getUpd_val4());
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
        getRoleDataProvider().close();
        getUserDataProvider().close();
        getVault_stsDataProvider().close();
        getIndiv_IDDataProvider().close();
        userUpdateDataProvider.close();
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

    protected VaultManagement getVaultManagement() {
        return (VaultManagement) getBean("VaultManagement");
    }

    public String btn_submit_action() {
        try {
            if (!fieldValidations()) {
                return null;
            } else {
                getMes().setText("");
                submitData();
            }
            getSessionBean1().setPag_nav("add");
        } catch (Exception ex) {
            getMes().setText(messages.get("CM024"));
            System.out.println("CashBoxSetup_AddUpdateRequest, btn_submit_action(), " + getSessionBean1().getUserId() + " - " + ex);
        }
        return null;
    }

    private boolean fieldValidations() throws Exception {
        try {
            String errMsg = "";
            int check = 0, min_cshbx_lmt = 0, max_cshbx_lmt = 0, ngt_lmt = 0, min_exch_lmt = 0, max_wdrw_lmt = 0;
            if (getFld_cas_box_name().getText() == null || getFld_cas_box_name().getText().equals("")) {
                check += 1;
                errMsg += "Cash Box Name " + messages.get("CM004") + "\n";
            }
            if (getDdl_org_level().getSelected() == null || getDdl_org_level().getSelected().equals("") || getDdl_org_level().getSelected().equals("99999999999")) {
                check += 1;
                errMsg += "Orga Level " + messages.get("CM010") + "\n";
            }
            if (getDdl_val_name().getSelected() == null || getDdl_val_name().getSelected().equals("") || getDdl_val_name().getSelected().equals("99999999999")) {
                check += 1;
                errMsg += "Vault Name " + messages.get("CM010") + "\n";
            }
            if (getDdl_rol_off().getSelected() == null || getDdl_rol_off().getSelected().equals("") || getDdl_rol_off().getSelected().equals("99999999999")) {
                check += 1;
                errMsg += "Role: Cash Officer / Teller " + messages.get("CM010") + "\n";
            }
            if (getDdl_rol_sup().getSelected() == null || getDdl_rol_sup().getSelected().equals("") || getDdl_rol_sup().getSelected().equals("99999999999")) {
                check += 1;
                errMsg += "Role: Supervisor " + messages.get("CM010") + "\n";
            } else if (getDdl_rol_off().getSelected().toString().equals(getDdl_rol_sup().getSelected().toString())) {
                check += 1;
                errMsg += "Cash Officer / Teller and Supervisor Roles canot be same \n";
            }
            if (getDdl_usr_off().getSelected() == null || getDdl_usr_off().getSelected().equals("") || getDdl_usr_off().getSelected().equals("99999999999")) {
                check += 1;
                errMsg += "User: Cash Officer / Teller " + messages.get("CM010") + "\n";
            }
            if (getDdl_usr_sup().getSelected() == null || getDdl_usr_sup().getSelected().equals("") || getDdl_usr_sup().getSelected().equals("99999999999")) {
                check += 1;
                errMsg += "User: Supervisor " + messages.get("CM010") + "\n";
            }
            if (getDdl_rol_sup().getSelected().toString().equals(getDdl_rol_off().getSelected().toString())) {
                check += 1;
                errMsg += "Cash Officer / Teller and Supervisor Roles canot be same \n";
            }
            if (getFld_cas_box_acc_time_frm().getText() == null || getFld_cas_box_acc_time_frm().getText().equals("")) {
                check += 1;
                errMsg += "Cash Box Access Time From " + messages.get("CM004") + "\n";
            } else if (getFld_cas_box_acc_time_frm().getText().toString().length() < 5) {
                check += 1;
                errMsg += "Cash Box Access Time From format is not correct. Valid format is 00:00 \n";
            }
            if (getFld_cas_box_acc_time_to().getText() == null || getFld_cas_box_acc_time_to().getText().equals("")) {
                check += 1;
                errMsg += "Cash Box Access Time To " + messages.get("CM004") + "\n";
            } else if (getFld_cas_box_acc_time_to().getText().toString().length() < 5) {
                check += 1;
                errMsg += "Cash Box Access Time To format is not correct. Valid format is 00:00 \n";
            } else if ((getFld_cas_box_acc_time_frm().getText() != null) && (getFld_cas_box_acc_time_frm().getText().toString().length() == 5)) {
                double to_time = ((double) Integer.parseInt(getFld_cas_box_acc_time_to().getText().toString().substring(0, 2))) + ((double) Integer.parseInt(getFld_cas_box_acc_time_to().getText().toString().substring(3, 5))) / 60;
                double fr_time = Integer.parseInt(getFld_cas_box_acc_time_frm().getText().toString().substring(0, 2)) + Integer.parseInt(getFld_cas_box_acc_time_frm().getText().toString().substring(3, 5)) / 60;
                if (fr_time >= to_time) {
                    check += 1;
                    errMsg += "Cash Box Access Time From " + messages.get("VL003") + " Cash Box Access Time To \n";
                }
            }
            if (getFld_max_cas_box_lmt().getText() == null || getFld_max_cas_box_lmt().getText().equals("")) {
                check += 1;
                errMsg += "Maximum Cash Box limit " + messages.get("CM004") + "\n";
            } else {
                max_cshbx_lmt = Integer.parseInt(getSessionBean1().removeCurrencyFormat(getFld_max_cas_box_lmt().getText().toString(), false));
            }
            if (getFld_min_cas_box_lmt().getText() == null || getFld_min_cas_box_lmt().getText().equals("")) {
                check += 1;
                errMsg += "Minimum Cash Box limit " + messages.get("CM004") + "\n";
            } else if (max_cshbx_lmt != 0) {
                min_cshbx_lmt = Integer.parseInt(getSessionBean1().removeCurrencyFormat(getFld_min_cas_box_lmt().getText().toString(), false));
                if (min_cshbx_lmt >= max_cshbx_lmt) {
                    check += 1;
                    errMsg += "Minimum Cash Box limit " + messages.get("VL003") + " Maximum Cash Box limit\n";
                }
            }
            if (getFld_ngt_lmt().getText() == null || getFld_ngt_lmt().getText().equals("")) {
                check += 1;
                errMsg += "Overnight Cash Box limit " + messages.get("CM004") + "\n";
            } else if (max_cshbx_lmt != 0) {
                ngt_lmt = Integer.parseInt(getSessionBean1().removeCurrencyFormat(getFld_ngt_lmt().getText().toString(), false));
                if (ngt_lmt < min_cshbx_lmt) {
                    check += 1;
                    errMsg += messages.get("VL069") + "\n";
                }
                if (ngt_lmt >= max_cshbx_lmt) {
                    check += 1;
                    errMsg += "Overnight Cash Box limit " + messages.get("VL003") + " Maximum Cash Box limit\n";
                }
            }
            if (getFld_min_exch_lmt().getText() == null || getFld_min_exch_lmt().getText().equals("")) {
                check += 1;
                errMsg += "Maximum Transfer Limit " + messages.get("CM004") + "\n";
            } else if (max_cshbx_lmt != 0) {
                min_exch_lmt = Integer.parseInt(getSessionBean1().removeCurrencyFormat(getFld_min_exch_lmt().getText().toString(), false));
                if (min_exch_lmt >= max_cshbx_lmt) {
                    check += 1;
                    errMsg += "Maximum Transfer Limit " + messages.get("VL003") + " Maximum Cash Box limit\n";
                }
            }
            if (getFld_max_wdrw_lmt().getText() == null || getFld_max_wdrw_lmt().getText().equals("")) {
                check += 1;
                errMsg += "Maximum Withdrawal limit " + messages.get("CM004") + "\n";
            } else if (max_cshbx_lmt != 0) {
                max_wdrw_lmt = Integer.parseInt(getSessionBean1().removeCurrencyFormat(getFld_max_wdrw_lmt().getText().toString(), false));
                if (max_wdrw_lmt >= max_cshbx_lmt) {
                    check += 1;
                    errMsg += "Maximum Withdrawal limit " + messages.get("VL003") + " Maximum Vault limit\n";
                }
            }
            if (getTxt_comments().getText() == null || getTxt_comments().getText().equals("")) {
                check += 1;
                errMsg += "Comments " + messages.get("CM004") + "\n";
            }
            if (check > 5) {
                getMes().setRows(check);
            } else {
                getMes().setRows(5);
            }
            if (check > 0) {
                getMes().setText(errMsg);
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            System.out.println("CashBoxSetup_AddUpdateRequest, fieldValidations(), " + getSessionBean1().getUserId() + " - " + ex);
            throw ex;
        }
    }

    private void submitData() throws Exception {
        try {
            String record_typ = (getRec_type().getText() == null) ? "add" : getRec_type().getText().toString();
            String cashboxID = (getFld_cas_box_id().getText() == null) ? null : getSessionBean1().replace(getFld_cas_box_id().getText().toString());
            String strReqID = (getReqID().getText() == null) ? null : getSessionBean1().replace(getReqID().getText().toString());
            String strFlowID = (getFlow_id().getText() == null) ? null : getSessionBean1().replace(getFlow_id().getText().toString());
            String strVersion = (getVersion().getText() == null) ? null : getSessionBean1().replace(getVersion().getText().toString());
            String cas_box_name = (getFld_cas_box_name().getText() == null) ? null : getFld_cas_box_name().getText().toString();
            String val_name = (getDdl_val_name().getSelected() == null || getDdl_val_name().getSelected().toString().equals("99999999999")) ? null : getDdl_val_name().getSelected().toString();
            String off_usr_id = (getDdl_usr_off().getSelected() == null || getDdl_usr_off().getSelected().toString().equals("99999999999")) ? null : getDdl_usr_off().getSelected().toString();
            String sup_usr_id = (getDdl_usr_sup().getSelected() == null || getDdl_usr_sup().getSelected().toString().equals("99999999999")) ? null : getDdl_usr_sup().getSelected().toString();
            String acc_time_frm = (getFld_cas_box_acc_time_frm().getText() == null) ? null : getFld_cas_box_acc_time_frm().getText().toString();
            String acc_time_to = (getFld_cas_box_acc_time_to().getText() == null) ? null : getFld_cas_box_acc_time_to().getText().toString();
            int apr_req = (getFld_approval_req().getText() == null) ? 0 : Integer.parseInt(getFld_approval_req().getText().toString());
            String min_val_lmt = (getFld_min_cas_box_lmt().getText() == null) ? null : getSessionBean1().removeCurrencyFormat(getSessionBean1().replace(getFld_min_cas_box_lmt().getText().toString()), false);
            String max_val_lmt = (getFld_max_cas_box_lmt().getText() == null) ? null : getSessionBean1().removeCurrencyFormat(getSessionBean1().replace(getFld_max_cas_box_lmt().getText().toString()), false);
            String ovr_ngt_lmt = (getFld_ngt_lmt().getText() == null) ? null : getSessionBean1().removeCurrencyFormat(getSessionBean1().replace(getFld_ngt_lmt().getText().toString()), false);
            String min_exch_lmt = (getFld_min_exch_lmt().getText() == null) ? null : getSessionBean1().removeCurrencyFormat(getSessionBean1().replace(getFld_min_exch_lmt().getText().toString()), false);
            String max_wdrw_lmt = (getFld_max_wdrw_lmt().getText() == null) ? null : getSessionBean1().removeCurrencyFormat(getSessionBean1().replace(getFld_max_wdrw_lmt().getText().toString()), false);
            String cas_box_sts = (getDdl_cas_box_sts().getSelected() == null || getDdl_cas_box_sts().getSelected().toString().equals("99999999999")) ? null : getDdl_cas_box_sts().getSelected().toString();
            int intr_lvl_trns = (getFld_int_trns_alw().getText() == null) ? 0 : Integer.parseInt(getFld_int_trns_alw().getText().toString());
            int ovr_lmt_alw = (getFld_ovr_lmt_alw().getText() == null) ? 0 : Integer.parseInt(getFld_ovr_lmt_alw().getText().toString());
            int mob_cshbx = (getFld_cshbx_alw().getText() == null) ? 0 : Integer.parseInt(getFld_cshbx_alw().getText().toString());
            String comments = (getTxt_comments().getText() == null) ? null : getSessionBean1().replace(getTxt_comments().getText().toString());
            String role_off = (getDdl_rol_off().getSelected() == null || getDdl_rol_off().getSelected().toString().equals("99999999999")) ? null : getDdl_rol_off().getSelected().toString();
            String role_sup = (getDdl_rol_sup().getSelected() == null || getDdl_rol_sup().getSelected().toString().equals("99999999999")) ? null : getDdl_rol_sup().getSelected().toString();
            String[][] updateUsers = null, updateUsersCheck = null;
            int errFlg = 0, errFlg2 = 1;
            if (!record_typ.equals("add")) {
                String off_rol, sup_rol;
                int offUsrFlg = 0, supUsrFlg = 0, off_usr_id_unique = 0;
                Vector updateRoleChk = (Vector) getSessionBean1().executeSQLSelect("SELECT (SELECT rol_id FROM vl_cash_box a INNER JOIN ad_user b ON a.off_usr_id = b.usr_id WHERE a.org_cod=(SELECT org_cod FROM vl_vault WHERE val_id=" + getDdl_val_name().getSelected().toString() + ") LIMIT 1) as off_rol, (SELECT rol_id FROM vl_cash_box a INNER JOIN ad_user b ON a.sup_usr_id = b.usr_id WHERE a.org_cod=(SELECT org_cod FROM vl_vault WHERE val_id=" + getDdl_val_name().getSelected().toString() + ") LIMIT 1) as sup_rol FROM dual").get(0);
                off_rol = updateRoleChk.get(0).toString();
                sup_rol = updateRoleChk.get(1).toString();
                if (!off_rol.equals(getDdl_rol_off().getSelected().toString()) || !sup_rol.equals(getDdl_rol_sup().getSelected().toString())) {
                    updateUsers = new String[getCashboxSetup_UsersUpdateDataProvider().getRowCount()][3];
                    getCashboxSetup_UsersUpdateDataProvider().cursorFirst();
                    for (int i = 0; i < getCashboxSetup_UsersUpdateDataProvider().getRowCount(); i++) {
                        updateUsers[i][0] = getSessionBean1().replace(getCashboxSetup_UsersUpdateDataProvider().getValue("cas_box_id").toString());
                        if (!off_rol.equals(getDdl_rol_off().getSelected().toString())) {
                            updateUsers[i][1] = getCashboxSetup_UsersUpdateDataProvider().getValue(getCashboxSetup_UsersUpdateDataProvider().getFieldKey("off_usr_id")).toString();
                            if (off_usr_id.equals(updateUsers[i][1])) off_usr_id_unique++;
                        } else {
                            updateUsers[i][1] = "";
                        }
                        if (!sup_rol.equals(getDdl_rol_sup().getSelected().toString())) {
                            updateUsers[i][2] = getCashboxSetup_UsersUpdateDataProvider().getValue(getCashboxSetup_UsersUpdateDataProvider().getFieldKey("sup_usr_id")).toString();
                        } else {
                            updateUsers[i][2] = "";
                        }
                        if (getSessionBean1().replace(getCashboxSetup_UsersUpdateDataProvider().getValue("off_usr_id").toString()).equals("99999999999") && !updateUsers[i][1].equals("")) offUsrFlg = offUsrFlg + 1;
                        if (getSessionBean1().replace(getCashboxSetup_UsersUpdateDataProvider().getValue("sup_usr_id").toString()).equals("99999999999") && !updateUsers[i][2].equals("")) supUsrFlg = supUsrFlg + 1;
                        getCashboxSetup_UsersUpdateDataProvider().cursorNext();
                    }
                    updateUsersCheck = updateUsers;
                    for (int i = 0; i < updateUsersCheck.length; i++) {
                        for (int j = 0; j < updateUsers.length; j++) {
                            if (updateUsersCheck[i][1].equals(updateUsers[j][1]) && i != j) off_usr_id_unique++;
                        }
                    }
                    if (off_usr_id_unique != 0) {
                        getMes().setText("For Update Other Cash Box(es), Officer User must be unique \n");
                        errFlg++;
                    } else if (offUsrFlg == 0 && supUsrFlg == 0) {
                    } else if (offUsrFlg != 0 && supUsrFlg != 0) {
                        getMes().setText("For Update Other Cash Box(es), Officer User and Supervisor User " + messages.get("CM010") + "\n");
                        errFlg++;
                    } else if (offUsrFlg != 0) {
                        getMes().setText("For Update Other Cash Box(es), Officer User " + messages.get("CM010") + "\n");
                        errFlg++;
                    } else {
                        getMes().setText("For Update Other Cash Box(es), Supervisor User " + messages.get("CM010") + "\n");
                        errFlg++;
                    }
                }
            }
            Vector app_req_chk = (Vector) getSessionBean1().executeSQLSelect("SELECT CASE WHEN COALESCE(SUM(apr_req),0) = " + apr_req + "  THEN 0 ELSE 1 END as apr_req FROM vl_cash_box WHERE org_cod=(SELECT org_cod FROM vl_vault WHERE val_id=" + getDdl_val_name().getSelected().toString() + ") LIMIT 1").get(0);
            int app_req_sync = Integer.parseInt(app_req_chk.get(0).toString());
            String check = "";
            if (errFlg == 0) {
                check = getVaultManagement().processCashBoxRequest(REQUEST_TYPE, REQUEST_TYPE2, record_typ, cashboxID, strReqID, strVersion, cas_box_name, val_name, off_usr_id, sup_usr_id, acc_time_frm, acc_time_to, min_val_lmt, max_val_lmt, ovr_ngt_lmt, min_exch_lmt, max_wdrw_lmt, cas_box_sts, intr_lvl_trns, ovr_lmt_alw, mob_cshbx, comments, role_off, role_sup, strFlowID, updateUsers, apr_req, app_req_sync);
                if (check.equals("error")) {
                    getMes().setText(messages.get("CM024"));
                } else if (check.equals("Not Allowed")) {
                    getMes().setText("" + messages.get("VL014") + " for Vault");
                } else if (check.substring(0, 1).equals("#")) {
                    getMes().setText(check.substring(1));
                } else errFlg2 = 0;
            }
            if (record_typ == null) {
                record_typ = REQUEST_TYPE;
            } else {
                if (record_typ.equals("add")) record_typ = REQUEST_TYPE; else record_typ = REQUEST_TYPE2;
            }
            String nextAction = getSessionBean1().getNextWorkflow(record_typ, getFlow_id().getText().toString());
            if (errFlg2 == 0) {
                HttpSession session = ((HttpServletRequest) getExternalContext().getRequest()).getSession();
                if (nextAction.equals("99")) {
                    if (app_req_sync != 0 || updateUsers != null) session.setAttribute("Update", "All CashBox(es) at this level are " + messages.get("VL005")); else session.setAttribute("Update", "CashBox for " + check.replace("'", "") + " " + messages.get("VL005"));
                } else {
                    session.setAttribute("Update", "CashBox Request " + messages.get("VL004") + " to " + getSessionBean1().getActionName(nextAction));
                }
            }
        } catch (Exception ex) {
            System.out.println("CashBoxSetup_AddUpdateRequest, submitData(), " + getSessionBean1().getUserId() + " - " + ex);
            getMes().setText(messages.get("CM024"));
            throw ex;
        }
    }

    public String getCurrentRow() {
        return "";
    }

    public void setCurrentRow(int row) {
    }

    private Object lastSelected = "0";

    public Object getRBSelected() {
        return null;
    }

    public void setRBSelected(Object selected) {
        if (selected != null) {
            lastSelected = selected;
        }
    }

    public void radioButton1_processValueChange(ValueChangeEvent event) {
        getSessionBean1().setRow(event.getNewValue().toString());
    }

    /**
     * <p>Return a reference to the scoped data bean.</p>
     *
     * @return reference to the scoped data bean
     */
    protected Admin getAdmin() {
        return (Admin) getBean("Admin");
    }

    public void ddl_rol_off_processValueChange(ValueChangeEvent event) {
        try {
            if (getDdl_rol_off().getSelected().toString().equals("99999999999")) {
                getDdl_usr_off().setDisabled(true);
            } else {
                getDdl_usr_off().setDisabled(false);
            }
            getUserRowSet().setObject(1, getDdl_rol_off().getSelected());
            getUserRowSet().setObject(2, getDdl_val_name().getSelected());
            getUserRowSet().setObject(3, (getCas_box_id().getText() == null) ? "0" : getCas_box_id().getText().toString());
            getUserRowSet().execute();
            if (!getRec_type().getText().toString().equals("add")) userUpdateTableConf();
        } catch (Exception ex) {
            System.out.println("CashBoxSetup_AddUpdateRequest, ddl_rol_off_processValueChange(), " + getSessionBean1().getUserId() + " - " + ex);
        }
    }

    public void ddl_rol_sup_processValueChange(ValueChangeEvent event) {
        try {
            if (getDdl_rol_sup().getSelected().toString().equals("99999999999")) {
                getDdl_usr_sup().setDisabled(true);
            } else {
                getDdl_usr_sup().setDisabled(false);
            }
            getUserRowSet2().setObject(1, getDdl_rol_sup().getSelected());
            getUserRowSet2().setObject(2, getDdl_val_name().getSelected());
            getUserRowSet2().execute();
            if (!getRec_type().getText().toString().equals("add")) userUpdateTableConf();
        } catch (Exception ex) {
            System.out.println("CashBoxSetup_AddUpdateRequest, ddl_rol_sup_processValueChange(), " + getSessionBean1().getUserId() + " - " + ex);
        }
    }

    public void userUpdateTableConf() {
        try {
            String off_rol, sup_rol;
            Vector updateRoleChk = (Vector) getSessionBean1().executeSQLSelect("SELECT (SELECT rol_id FROM vl_cash_box a INNER JOIN ad_user b ON a.off_usr_id = b.usr_id WHERE a.org_cod=(SELECT org_cod FROM vl_vault WHERE val_id=" + getDdl_val_name().getSelected().toString() + ") LIMIT 1) as off_rol, (SELECT rol_id FROM vl_cash_box a INNER JOIN ad_user b ON a.sup_usr_id = b.usr_id WHERE a.org_cod=(SELECT org_cod FROM vl_vault WHERE val_id='" + getDdl_val_name().getSelected().toString() + "') LIMIT 1) as sup_rol FROM dual").get(0);
            off_rol = updateRoleChk.get(0).toString();
            sup_rol = updateRoleChk.get(1).toString();
            if (!off_rol.equals(getDdl_rol_off().getSelected().toString()) || !sup_rol.equals(getDdl_rol_sup().getSelected().toString())) {
                getUserUpdateRowSet().setObject(1, getDdl_rol_off().getSelected());
                getUserUpdateRowSet().setObject(2, getDdl_val_name().getSelected());
                getUserUpdateRowSet().setObject(3, (getCas_box_id().getText() == null) ? "0" : getCas_box_id().getText().toString());
                getUserUpdateRowSet().execute();
                getUserUpdateRowSet2().setObject(1, getDdl_rol_sup().getSelected());
                getUserUpdateRowSet2().setObject(2, getDdl_val_name().getSelected());
                getUserUpdateRowSet2().execute();
                if (getRec_type().getText().toString().equals("approved")) getSessionBean1().getCashboxSetup_UsersUpdateRowSet().setCommand("SELECT cas_box_id,cas_box_name,off_usr_id,sup_usr_id FROM vl_cash_box WHERE org_cod = (SELECT org_cod FROM vl_vault WHERE val_id= " + getDdl_val_name().getSelected().toString() + " )  AND cas_box_id <> " + getFld_cas_box_id().getText() + ""); else getSessionBean1().getCashboxSetup_UsersUpdateRowSet().setCommand("SELECT cas_box_id,cas_box_name,off_usr_id,sup_usr_id FROM vl_cash_box_v WHERE org_cod = (SELECT org_cod FROM vl_vault WHERE val_id= " + getDdl_val_name().getSelected().toString() + "  )  AND cas_box_id <> " + getFld_cas_box_id().getText() + " AND req_id ='" + getReqID().getText() + "' AND ver_id = '" + getVersion().getText() + "'");
                getCashboxSetup_UsersUpdateDataProvider().refresh();
                getUserUpdateTblChk().setText("show");
            } else {
                getUserUpdateRowSet().setObject(1, "");
                getUserUpdateRowSet().setObject(2, "");
                getUserUpdateRowSet().setObject(3, "");
                getUserUpdateRowSet().execute();
                getUserUpdateRowSet2().setObject(1, "");
                getUserUpdateRowSet2().setObject(2, "");
                getUserUpdateRowSet2().execute();
                getUserUpdateTblChk().setText(null);
            }
            if (off_rol.equals(getDdl_rol_off().getSelected().toString())) {
                getTableColumn2().setVisible(false);
            } else {
                getTableColumn2().setVisible(true);
            }
            if (sup_rol.equals(getDdl_rol_sup().getSelected().toString())) {
                getTableColumn3().setVisible(false);
            } else {
                getTableColumn3().setVisible(true);
            }
        } catch (Exception ex) {
            System.out.println("CashBoxSetup_AddUpdateRequest, userUpdateTableConf(), " + getSessionBean1().getUserId() + " - " + ex);
        }
    }

    public void ddl_org_level_processValueChange(ValueChangeEvent event) {
        try {
            if (getDdl_org_level().getSelected().toString().equals("99999999999")) {
                getDdl_val_name().setDisabled(true);
                getDdl_rol_off().setDisabled(true);
                getDdl_rol_sup().setDisabled(true);
                getDdl_usr_off().setDisabled(true);
                getDdl_usr_sup().setDisabled(true);
            } else {
                getDdl_val_name().setDisabled(false);
                getDdl_rol_off().setDisabled(false);
                getDdl_rol_sup().setDisabled(false);
                getDdl_usr_off().setDisabled(false);
            }
            getValNameRowSet().setObject(1, getDdl_org_level().getSelected());
            getValNameRowSet().execute();
            getRoleRowSet().setObject(1, "99999999999");
            getRoleRowSet().execute();
            getRoleRowSet2().setObject(1, "99999999999");
            getRoleRowSet2().execute();
            getUserRowSet().setObject(1, "99999999999");
            getUserRowSet().setObject(2, "99999999999");
            getUserRowSet().setObject(3, "99999999999");
            getUserRowSet().execute();
            getUserRowSet2().setObject(1, "99999999999");
            getUserRowSet2().setObject(2, "99999999999");
            getUserRowSet2().execute();
        } catch (Exception ex) {
            System.out.println("CashBoxSetup_AddUpdateRequest, ddl_rol_sup_processValueChange(), " + getSessionBean1().getUserId() + " - " + ex);
        }
    }

    public String btn_ext_action() {
        return "back";
    }

    public void ddl_val_name_processValueChange(ValueChangeEvent vce) {
        try {
            if (getDdl_val_name().getSelected().toString().equals("99999999999")) {
                getDdl_rol_off().setDisabled(true);
                getDdl_rol_sup().setDisabled(true);
                getDdl_usr_off().setDisabled(true);
                getDdl_usr_sup().setDisabled(true);
                getUserRowSet().setObject(1, "99999999999");
                getUserRowSet2().setObject(1, "99999999999");
            } else {
                Vector cashBoxExistsResult = (Vector) getSessionBean1().executeSQLSelect("SELECT (SELECT count(*) FROM vl_cash_box WHERE org_cod = (SELECT org_cod FROM vl_vault WHERE val_id=" + getDdl_val_name().getSelected().toString() + ")) as extChk, (SELECT rol_id FROM vl_cash_box a INNER JOIN ad_user b ON a.off_usr_id = b.usr_id WHERE a.org_cod=(SELECT org_cod FROM vl_vault WHERE val_id=" + getDdl_val_name().getSelected().toString() + ") LIMIT 1) as offRoleID, (SELECT rol_id FROM vl_vault a INNER JOIN ad_user b ON a.sec_usr_id = b.usr_id WHERE a.val_id =" + getDdl_val_name().getSelected().toString() + ") as supRoleID,(SELECT sec_usr_id FROM vl_vault WHERE val_id =" + getDdl_val_name().getSelected().toString() + ") as supUserID FROM DUAL").get(0);
                if (Integer.parseInt(cashBoxExistsResult.get(0).toString()) > 0) {
                    getDdl_rol_off().setSelected(new BigDecimal(cashBoxExistsResult.get(1).toString()));
                    getDdl_rol_off().setDisabled(true);
                    getUserRowSet().setObject(1, cashBoxExistsResult.get(1).toString());
                } else {
                    getDdl_rol_off().setDisabled(false);
                    getUserRowSet().setObject(1, "99999999999");
                }
                getDdl_rol_sup().setSelected(new BigDecimal(cashBoxExistsResult.get(2).toString()));
                getDdl_usr_sup().setSelected(cashBoxExistsResult.get(3).toString());
                getUserRowSet2().setObject(1, cashBoxExistsResult.get(2).toString());
                getDdl_usr_off().setDisabled(false);
            }
            getRoleRowSet().setObject(1, getDdl_val_name().getSelected());
            getRoleRowSet().execute();
            getRoleRowSet2().setObject(1, getDdl_val_name().getSelected());
            getRoleRowSet2().execute();
            getUserRowSet().setObject(2, getDdl_val_name().getSelected());
            getUserRowSet().setObject(3, (getCas_box_id().getText() == null) ? "0" : getCas_box_id().getText().toString());
            getUserRowSet().execute();
            getUserRowSet2().setObject(2, getDdl_val_name().getSelected());
            getUserRowSet2().execute();
        } catch (Exception ex) {
            System.out.println("CashBoxSetup_AddUpdateRequest, ddl_val_name_processValueChange(), " + getSessionBean1().getUserId() + " - " + ex);
        }
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
    protected Pro_Def getPro_Def() {
        return (Pro_Def) getBean("Pro_Def");
    }
}
