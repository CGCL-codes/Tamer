package net.nan21.ebs.dc;

import java.util.*;
import javax.servlet.http.HttpServletResponse;
import net.nan21.lib.*;
import net.nan21.lib.dc.*;

public class DC0048 extends AbstractDataControl implements IDataControl {

    public void init(HttpRequest request, HttpServletResponse response, HttpSession session, DbManager dbm) throws Exception {
        this._initFields();
        super.init(request, response, session, dbm);
    }

    private void preQuery() {
        if (this.request.getParam("QRY_CREATEDBY") != null && !this.request.getParam("QRY_CREATEDBY").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("CREATEDBY like :CREATEDBY");
            this.queryParams.put("CREATEDBY", (String) this.request.getParam("QRY_CREATEDBY"));
        }
        if (this.request.getParam("QRY_CREATEDON") != null && !this.request.getParam("QRY_CREATEDON").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("CREATEDON like :CREATEDON");
            this.queryParams.put("CREATEDON", (String) this.request.getParam("QRY_CREATEDON"));
        }
        if (this.request.getParam("QRY_ID") != null && !this.request.getParam("QRY_ID").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("ID like :ID");
            this.queryParams.put("ID", (String) this.request.getParam("QRY_ID"));
        }
        if (this.request.getParam("QRY_MODIFIEDBY") != null && !this.request.getParam("QRY_MODIFIEDBY").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("MODIFIEDBY like :MODIFIEDBY");
            this.queryParams.put("MODIFIEDBY", (String) this.request.getParam("QRY_MODIFIEDBY"));
        }
        if (this.request.getParam("QRY_MODIFIEDON") != null && !this.request.getParam("QRY_MODIFIEDON").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("MODIFIEDON like :MODIFIEDON");
            this.queryParams.put("MODIFIEDON", (String) this.request.getParam("QRY_MODIFIEDON"));
        }
        if (this.request.getParam("QRY_NOTE") != null && !this.request.getParam("QRY_NOTE").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("NOTE like :NOTE");
            this.queryParams.put("NOTE", (String) this.request.getParam("QRY_NOTE"));
        }
        if (this.request.getParam("QRY_PROJECT_ISSUE_ID") != null && !this.request.getParam("QRY_PROJECT_ISSUE_ID").equals("")) {
            this.queryWhere.append((this.queryWhere.length() > 0) ? " and " : "");
            this.queryWhere.append("PROJECT_ISSUE_ID like :PROJECT_ISSUE_ID");
            this.queryParams.put("PROJECT_ISSUE_ID", (String) this.request.getParam("QRY_PROJECT_ISSUE_ID"));
        }
    }

    public void doQuery() throws Exception {
        this.prepareQueryContext();
        this.preQuery();
        this.queryWhere.insert(0, (this.queryWhere.length() > 0) ? " where " : "");
        String sql = "select " + " CREATEDBY" + " ,CREATEDON" + " ,ID" + " ,MODIFIEDBY" + " ,MODIFIEDON" + " ,NOTE" + " ,PROJECT_ISSUE_ID" + " from PROJECT_ISSUE_NOTE  " + this.queryWhere.toString() + " " + this.queryOrderBy;
        this.writeResultDoQuery(sql);
    }

    public void doExport() throws Exception {
        this.prepareQueryContext();
        this.preQuery();
        this.queryWhere.insert(0, (this.queryWhere.length() > 0) ? " where " : "");
        String sql = "select " + " ID" + " ,PROJECT_ISSUE_ID" + " ,NOTE" + " ,CREATEDBY" + " ,CREATEDON" + " ,MODIFIEDBY" + " ,MODIFIEDON" + " from PROJECT_ISSUE_NOTE  " + this.queryWhere.toString() + " " + this.queryOrderBy;
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
        String sql = "insert into PROJECT_ISSUE_NOTE(" + "  CREATEDBY" + " ,CREATEDON" + " ,ID" + " ,MODIFIEDBY" + " ,MODIFIEDON" + " ,NOTE" + " ,PROJECT_ISSUE_ID" + " ) values ( " + "  :CREATEDBY" + " ,:CREATEDON" + " ,:ID" + " ,:MODIFIEDBY" + " ,:MODIFIEDON" + " ,:NOTE" + " ,:PROJECT_ISSUE_ID" + ")";
        this.record.put("ID", dbm.getSequenceNextValue("seq_prjissnote_id"));
        dbm.executeStatement(sql, this.record);
        this.populateRecordPkFromRecord();
        this.findByPk();
        this.writeResultDoInsert();
    }

    public void doUpdate() throws Exception {
        this.populateRecordFromRequest();
        this.populateRecordWithClientSpecific();
        String sql = "update PROJECT_ISSUE_NOTE set " + "  ID=:ID" + " ,NOTE=:NOTE" + " ,PROJECT_ISSUE_ID=:PROJECT_ISSUE_ID" + " where " + "      ID= :ID" + "";
        dbm.executeStatement(sql, this.record);
        this.populateRecordPkFromRecord();
        this.findByPk();
        this.writeResultDoUpdate();
    }

    public void doDelete() throws Exception {
        this.populateRecordPkFromRequest();
        String sql = "delete from PROJECT_ISSUE_NOTE where " + "      ID= :ID" + "";
        dbm.executeStatement(sql, this.recordPk);
        this.writeResultDoDelete();
    }

    public void initNewRecord() throws Exception {
        this.populateRecordFromRequest();
        this.record.put("_p_record_status", "insert");
        this.writeResultInitNewRecord();
    }

    private void findByPk() throws Exception {
        String sql = "select " + " CREATEDBY" + " ,CREATEDON" + " ,ID" + " ,MODIFIEDBY" + " ,MODIFIEDON" + " ,NOTE" + " ,PROJECT_ISSUE_ID" + " from PROJECT_ISSUE_NOTE " + " where " + "      ID= :ID" + "";
        this.record = dbm.executeQuery(sql, this.recordPk).get(0);
    }

    public void doCustomAction(String pName) throws Exception {
        this.populateRecordFromRequest();
        this.sendRecord();
    }

    private void _initFields() {
        this.fields = new HashMap<String, FieldDef>();
        this.fields.put("CREATEDBY", new FieldDef("STRING"));
        this.fields.put("CREATEDON", new FieldDef("DATE"));
        this.fields.put("ID", new FieldDef("NUMBER"));
        this.fields.put("MODIFIEDBY", new FieldDef("STRING"));
        this.fields.put("MODIFIEDON", new FieldDef("DATE"));
        this.fields.put("NOTE", new FieldDef("STRING"));
        this.fields.put("PROJECT_ISSUE_ID", new FieldDef("NUMBER"));
        String[] _pkFields = { "ID" };
        this.pkFields = _pkFields;
        String[] _summaryFields = {};
        this.summaryFields = _summaryFields;
        this.queryResultSize = -1;
    }
}
