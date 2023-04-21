package net.nan21.ebs.dc;

import java.util.*;
import javax.servlet.http.HttpServletResponse;
import net.nan21.lib.*;
import net.nan21.lib.dc.*;

public class DC0024 extends AbstractDataControl implements IDataControl {

    public void init(HttpRequest request, HttpServletResponse response, HttpSession session, DbManager dbm) throws Exception {
        this._initFields();
        super.init(request, response, session, dbm);
    }

    private void preQuery() {
        if (this.request.getParam("QRY_ACCYEAR_CODE") != null && !this.request.getParam("QRY_ACCYEAR_CODE").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("t.ACCYEAR_CODE like :ACCYEAR_CODE");
            this.queryParams.put("ACCYEAR_CODE", (String) this.request.getParam("QRY_ACCYEAR_CODE"));
        }
        if (this.request.getParam("QRY_CLIENT_ID") != null && !this.request.getParam("QRY_CLIENT_ID").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("t.CLIENT_ID like :CLIENT_ID");
            this.queryParams.put("CLIENT_ID", (String) this.request.getParam("QRY_CLIENT_ID"));
        }
        if (this.request.getParam("QRY_CLOSED") != null && !this.request.getParam("QRY_CLOSED").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("t.CLOSED like :CLOSED");
            this.queryParams.put("CLOSED", (String) this.request.getParam("QRY_CLOSED"));
        }
        if (this.request.getParam("QRY_CODE") != null && !this.request.getParam("QRY_CODE").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("t.CODE like :CODE");
            this.queryParams.put("CODE", (String) this.request.getParam("QRY_CODE").toUpperCase());
        }
        if (this.request.getParam("QRY_ID") != null && !this.request.getParam("QRY_ID").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("t.ID like :ID");
            this.queryParams.put("ID", (String) this.request.getParam("QRY_ID"));
        }
    }

    public void doQuery() throws Exception {
        this.prepareQueryContext();
        this.preQuery();
        this.queryWhere.insert(0, (this.queryWhere.length() > 0) ? " where " : "");
        String sql = "select " + " t.ACCYEAR_CODE" + " ,pbo_client.get_code_by_id(t.client_id) CLIENT_CODE" + " ,t.CLIENT_ID" + " ,t.CLOSED" + " ,t.CODE" + " ,t.CREATEDBY" + " ,t.CREATEDON" + " ,t.ENDDATE" + " ,t.ID" + " ,t.IS_FIRST_PERIOD" + " ,t.MODIFIEDBY" + " ,t.MODIFIEDON" + " ,t.NAME" + " ,t.NOTES" + " ,t.OPENED" + " ,t.PERIODTYPE" + " ,t.PREV_PERIOD_NAME" + " ,t.PROCESSING" + " ,t.STARTDATE" + " from AC_ACC_PERIOD t " + this.queryWhere.toString() + " " + this.queryOrderBy;
        this.writeResultDoQuery(sql);
    }

    public void doExport() throws Exception {
        this.prepareQueryContext();
        this.preQuery();
        this.queryWhere.insert(0, (this.queryWhere.length() > 0) ? " where " : "");
        String sql = "select " + " t.ID" + " ,t.CLIENT_ID" + ",pbo_client.get_code_by_id(t.client_id) CLIENT_CODE" + " ,t.ACCYEAR_CODE" + " ,t.CODE" + " ,t.NAME" + " ,t.STARTDATE" + " ,t.ENDDATE" + " ,t.PREV_PERIOD_NAME" + " ,t.OPENED" + " ,t.CLOSED" + " ,t.NOTES" + " ,t.IS_FIRST_PERIOD" + " ,t.CREATEDON" + " ,t.CREATEDBY" + " ,t.MODIFIEDON" + " ,t.MODIFIEDBY" + " ,t.PERIODTYPE" + " ,t.PROCESSING" + " from AC_ACC_PERIOD t " + this.queryWhere.toString() + " " + this.queryOrderBy;
        this.writeResultDoExport(sql);
    }

    public void fetchRecord() throws Exception {
        this.populateRecordPkFromRequest();
        this.findByPk();
        this.writeResultFetchRecord();
    }

    public void doInsert() throws Exception {
        this.populateRecordFromRequest();
        this.populateRecordWithClientSpecific();
        String sql = "insert into AC_ACC_PERIOD(" + "  ACCYEAR_CODE" + " ,CLIENT_ID" + " ,CODE" + " ,CREATEDBY" + " ,ENDDATE" + " ,ID" + " ,IS_FIRST_PERIOD" + " ,MODIFIEDBY" + " ,NOTES" + " ,PERIODTYPE" + " ,PREV_PERIOD_NAME" + " ,PROCESSING" + " ,STARTDATE" + " ) values ( " + "  :ACCYEAR_CODE" + " ,:CLIENT_ID" + " ,:CODE" + " ,:CREATEDBY" + " ,:ENDDATE" + " ,:ID" + " ,:IS_FIRST_PERIOD" + " ,:MODIFIEDBY" + " ,:NOTES" + " ,:PERIODTYPE" + " ,:PREV_PERIOD_NAME" + " ,:PROCESSING" + " ,:STARTDATE" + ")";
        this.record.put("ID", dbm.getSequenceNextValue("seq_accperiod_id"));
        dbm.executeStatement(sql, this.record);
        this.populateRecordPkFromRecord();
        this.findByPk();
        this.writeResultDoInsert();
    }

    public void doUpdate() throws Exception {
        this.populateRecordFromRequest();
        this.populateRecordWithClientSpecific();
        String sql = "update AC_ACC_PERIOD set " + "  CODE=:CODE" + " ,ENDDATE=:ENDDATE" + " ,ID=:ID" + " ,IS_FIRST_PERIOD=:IS_FIRST_PERIOD" + " ,NOTES=:NOTES" + " ,PREV_PERIOD_NAME=:PREV_PERIOD_NAME" + " ,STARTDATE=:STARTDATE" + " where " + "      ID= :ID" + "";
        dbm.executeStatement(sql, this.record);
        this.populateRecordPkFromRecord();
        this.findByPk();
        this.writeResultDoUpdate();
    }

    public void doDelete() throws Exception {
        this.populateRecordPkFromRequest();
        String sql = "delete from AC_ACC_PERIOD where " + "      ID= :ID" + "";
        dbm.executeStatement(sql, this.recordPk);
        this.writeResultDoDelete();
    }

    public void initNewRecord() throws Exception {
        this.populateRecordFromRequest();
        this.record.put("_p_record_status", "insert");
        this.writeResultInitNewRecord();
    }

    private void findByPk() throws Exception {
        String sql = "select " + " t.ACCYEAR_CODE" + ",pbo_client.get_code_by_id(t.client_id) CLIENT_CODE" + " ,t.CLIENT_ID" + " ,t.CLOSED" + " ,t.CODE" + " ,t.CREATEDBY" + " ,t.CREATEDON" + " ,t.ENDDATE" + " ,t.ID" + " ,t.IS_FIRST_PERIOD" + " ,t.MODIFIEDBY" + " ,t.MODIFIEDON" + " ,t.NAME" + " ,t.NOTES" + " ,t.OPENED" + " ,t.PERIODTYPE" + " ,t.PREV_PERIOD_NAME" + " ,t.PROCESSING" + " ,t.STARTDATE" + " from AC_ACC_PERIOD t" + " where " + "      t.ID= :ID" + "";
        this.record = dbm.executeQuery(sql, this.recordPk).get(0);
    }

    public void doCustomAction(String pName) throws Exception {
        this.populateRecordFromRequest();
        if (pName.equals("OpenAccPeriod")) {
            this.callProc_OpenAccPeriod();
        }
        this.sendRecord();
    }

    private void callProc_OpenAccPeriod() throws Exception {
        ProcParamDef param = null;
        List<ProcParamDef> params = new ArrayList<ProcParamDef>();
        param = new ProcParamDef("p_accperiod_id", "ID", DataType.NUMBER, true, false);
        params.add(param);
        String sql = "BEGIN ps_acc.open_accperiod(?); END;";
        dbm.executeProcedure(sql, params, this.record);
    }

    private void _initFields() {
        this.fields = new HashMap<String, FieldDef>();
        this.fields.put("ACCYEAR_CODE", new FieldDef("STRING"));
        this.fields.put("CLIENT_CODE", new FieldDef("STRING"));
        this.fields.put("CLIENT_ID", new FieldDef("NUMBER"));
        this.fields.put("CLOSED", new FieldDef("BOOLEAN"));
        this.fields.put("CODE", new FieldDef("STRING"));
        this.fields.get("CODE").setCaseRestriction("Upper");
        this.fields.put("CREATEDBY", new FieldDef("STRING"));
        this.fields.put("CREATEDON", new FieldDef("DATE"));
        this.fields.put("ENDDATE", new FieldDef("DATE"));
        this.fields.put("ID", new FieldDef("NUMBER"));
        this.fields.put("IS_FIRST_PERIOD", new FieldDef("BOOLEAN"));
        this.fields.put("MODIFIEDBY", new FieldDef("STRING"));
        this.fields.put("MODIFIEDON", new FieldDef("DATE"));
        this.fields.put("NAME", new FieldDef("STRING"));
        this.fields.put("NOTES", new FieldDef("STRING"));
        this.fields.put("OPENED", new FieldDef("BOOLEAN"));
        this.fields.put("PERIODTYPE", new FieldDef("STRING"));
        this.fields.put("PREV_PERIOD_NAME", new FieldDef("STRING"));
        this.fields.put("PROCESSING", new FieldDef("STRING"));
        this.fields.put("STARTDATE", new FieldDef("DATE"));
        String[] _pkFields = { "ID" };
        this.pkFields = _pkFields;
        String[] _summaryFields = {};
        this.summaryFields = _summaryFields;
        this.queryResultSize = 20;
    }
}
