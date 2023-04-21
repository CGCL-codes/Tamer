package net.nan21.ebs.dc;

import java.util.*;
import javax.servlet.http.HttpServletResponse;
import net.nan21.lib.*;
import net.nan21.lib.dc.*;

public class DC0086 extends AbstractDataControl implements IDataControl {

    public void init(HttpRequest request, HttpServletResponse response, HttpSession session, DbManager dbm) throws Exception {
        this._initFields();
        super.init(request, response, session, dbm);
    }

    private void preQuery() {
        if (this.request.getParam("QRY_CODE") != null && !this.request.getParam("QRY_CODE").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("t.CODE like :CODE");
            this.queryParams.put("CODE", (String) this.request.getParam("QRY_CODE"));
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
        String sql = "select " + " t.CODE" + " ,t.CREATEDBY" + " ,t.CREATEDON" + " ,t.DESCRIPTION" + " ,t.ID" + " ,t.MODIFIEDBY" + " ,t.MODIFIEDON" + " from MM_ORG_INV_TYPE t " + this.queryWhere.toString() + " " + this.queryOrderBy;
        this.writeResultDoQuery(sql);
    }

    public void doExport() throws Exception {
        this.prepareQueryContext();
        this.preQuery();
        this.queryWhere.insert(0, (this.queryWhere.length() > 0) ? " where " : "");
        String sql = "select " + " t.ID" + " ,t.CODE" + " ,t.DESCRIPTION" + " ,t.CREATEDON" + " ,t.CREATEDBY" + " ,t.MODIFIEDON" + " ,t.MODIFIEDBY" + " from MM_ORG_INV_TYPE t " + this.queryWhere.toString() + " " + this.queryOrderBy;
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
        String sql = "insert into MM_ORG_INV_TYPE(" + "  CODE" + " ,DESCRIPTION" + " ,ID" + " ) values ( " + "  :CODE" + " ,:DESCRIPTION" + " ,:ID" + ")";
        this.record.put("ID", dbm.getSequenceNextValue("SEQ_ORGINVTYP_ID"));
        dbm.executeStatement(sql, this.record);
        this.populateRecordPkFromRecord();
        this.findByPk();
        this.writeResultDoInsert();
    }

    public void doUpdate() throws Exception {
        this.populateRecordFromRequest();
        this.populateRecordWithClientSpecific();
        String sql = "update MM_ORG_INV_TYPE set " + "  CODE=:CODE" + " ,CREATEDBY=:CREATEDBY" + " ,CREATEDON=:CREATEDON" + " ,DESCRIPTION=:DESCRIPTION" + " ,ID=:ID" + " ,MODIFIEDBY=:MODIFIEDBY" + " ,MODIFIEDON=:MODIFIEDON" + " where " + "      ID= :ID" + "";
        dbm.executeStatement(sql, this.record);
        this.populateRecordPkFromRecord();
        this.findByPk();
        this.writeResultDoUpdate();
    }

    public void doDelete() throws Exception {
        this.populateRecordPkFromRequest();
        String sql = "delete from MM_ORG_INV_TYPE where " + "      ID= :ID" + "";
        dbm.executeStatement(sql, this.recordPk);
        this.writeResultDoDelete();
    }

    public void initNewRecord() throws Exception {
        this.populateRecordFromRequest();
        this.record.put("_p_record_status", "insert");
        this.writeResultInitNewRecord();
    }

    private void findByPk() throws Exception {
        String sql = "select " + " t.CODE" + " ,t.CREATEDBY" + " ,t.CREATEDON" + " ,t.DESCRIPTION" + " ,t.ID" + " ,t.MODIFIEDBY" + " ,t.MODIFIEDON" + " from MM_ORG_INV_TYPE t" + " where " + "      t.ID= :ID" + "";
        this.record = dbm.executeQuery(sql, this.recordPk).get(0);
    }

    public void doCustomAction(String pName) throws Exception {
        this.populateRecordFromRequest();
        this.sendRecord();
    }

    private void _initFields() {
        this.fields = new HashMap<String, FieldDef>();
        this.fields.put("CODE", new FieldDef("STRING"));
        this.fields.put("CREATEDBY", new FieldDef("STRING"));
        this.fields.put("CREATEDON", new FieldDef("DATE"));
        this.fields.put("DESCRIPTION", new FieldDef("STRING"));
        this.fields.put("ID", new FieldDef("NUMBER"));
        this.fields.put("MODIFIEDBY", new FieldDef("STRING"));
        this.fields.put("MODIFIEDON", new FieldDef("DATE"));
        String[] _pkFields = { "ID" };
        this.pkFields = _pkFields;
        String[] _summaryFields = {};
        this.summaryFields = _summaryFields;
        this.queryResultSize = 20;
    }
}
