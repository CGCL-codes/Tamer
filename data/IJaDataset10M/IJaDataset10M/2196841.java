package net.nan21.ebs.dc;

import java.util.*;
import javax.servlet.http.HttpServletResponse;
import net.nan21.lib.*;
import net.nan21.lib.dc.*;

public class DC0067 extends AbstractDataControl implements IDataControl {

    public void init(HttpRequest request, HttpServletResponse response, HttpSession session, DbManager dbm) throws Exception {
        this._initFields();
        super.init(request, response, session, dbm);
    }

    private void preQuery() {
        if (this.request.getParam("QRY_ID") != null && !this.request.getParam("QRY_ID").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("r.ID like :ID");
            this.queryParams.put("ID", (String) this.request.getParam("QRY_ID"));
        }
        if (this.request.getParam("QRY_NAME") != null && !this.request.getParam("QRY_NAME").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("r.NAME like :NAME");
            this.queryParams.put("NAME", (String) this.request.getParam("QRY_NAME").toUpperCase());
        }
    }

    public void doQuery() throws Exception {
        this.prepareQueryContext();
        this.preQuery();
        this.queryWhere.insert(0, (this.queryWhere.length() > 0) ? " where " : "");
        String sql = "select " + " r.DESCRIPTION" + " ,r.ID" + " ,r.NAME" + " from SYS_ROLE r " + this.queryWhere.toString() + " " + this.queryOrderBy;
        this.writeResultDoQuery(sql);
    }

    public void doExport() throws Exception {
        this.prepareQueryContext();
        this.preQuery();
        this.queryWhere.insert(0, (this.queryWhere.length() > 0) ? " where " : "");
        String sql = "select " + " r.ID" + " ,r.NAME" + " ,r.DESCRIPTION" + " from SYS_ROLE r " + this.queryWhere.toString() + " " + this.queryOrderBy;
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
        String sql = "insert into SYS_ROLE(" + "  DESCRIPTION" + " ,ID" + " ,NAME" + " ) values ( " + "  :DESCRIPTION" + " ,:ID" + " ,:NAME" + ")";
        this.record.put("ID", dbm.getSequenceNextValue("SEQ_ROLE_ID"));
        dbm.executeStatement(sql, this.record);
        this.populateRecordPkFromRecord();
        this.findByPk();
        this.writeResultDoInsert();
    }

    public void doUpdate() throws Exception {
        this.populateRecordFromRequest();
        this.populateRecordWithClientSpecific();
        String sql = "update SYS_ROLE set " + "  DESCRIPTION=:DESCRIPTION" + " ,ID=:ID" + " ,NAME=:NAME" + " where " + "      ID= :ID" + "";
        dbm.executeStatement(sql, this.record);
        this.populateRecordPkFromRecord();
        this.findByPk();
        this.writeResultDoUpdate();
    }

    public void doDelete() throws Exception {
        this.populateRecordPkFromRequest();
        String sql = "delete from SYS_ROLE where " + "      ID= :ID" + "";
        dbm.executeStatement(sql, this.recordPk);
        this.writeResultDoDelete();
    }

    public void initNewRecord() throws Exception {
        this.populateRecordFromRequest();
        this.record.put("_p_record_status", "insert");
        this.writeResultInitNewRecord();
    }

    private void findByPk() throws Exception {
        String sql = "select " + " r.DESCRIPTION" + " ,r.ID" + " ,r.NAME" + " from SYS_ROLE r" + " where " + "      r.ID= :ID" + "";
        this.record = dbm.executeQuery(sql, this.recordPk).get(0);
    }

    public void doCustomAction(String pName) throws Exception {
        this.populateRecordFromRequest();
        this.sendRecord();
    }

    private void _initFields() {
        this.fields = new HashMap<String, FieldDef>();
        this.fields.put("CREATEDBY", new FieldDef("STRING"));
        this.fields.get("CREATEDBY").setInDS(false);
        this.fields.put("CREATEDON", new FieldDef("DATE"));
        this.fields.get("CREATEDON").setInDS(false);
        this.fields.put("DESCRIPTION", new FieldDef("STRING"));
        this.fields.put("ID", new FieldDef("NUMBER"));
        this.fields.put("MODIFIEDBY", new FieldDef("STRING"));
        this.fields.get("MODIFIEDBY").setInDS(false);
        this.fields.put("MODIFIEDON", new FieldDef("DATE"));
        this.fields.get("MODIFIEDON").setInDS(false);
        this.fields.put("NAME", new FieldDef("STRING"));
        this.fields.get("NAME").setCaseRestriction("Upper");
        String[] _pkFields = { "ID" };
        this.pkFields = _pkFields;
        String[] _summaryFields = {};
        this.summaryFields = _summaryFields;
        this.queryResultSize = 20;
    }
}
