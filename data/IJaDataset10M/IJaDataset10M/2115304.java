package wotbz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.openbravo.database.ConnectionProvider;
import org.openbravo.erpCommon.ad_background.BackgroundProcess;
import org.openbravo.erpCommon.ad_background.PeriodicBackground;
import utility.PeriodDate;
import com.mysql.jdbc.Statement;
import dbmanager.DBManager;

public class Wo_TablizationCopy {

    ConnectionProvider conn;

    Statement stmt;

    public void Wo_TablizationCopy(ConnectionProvider conn) {
        this.conn = conn;
    }

    public void createXtabTable() {
        String queryGroupResult = "select xtt.wo_test_trans_id,xtt.tablename,xttp.result,xtt.record,xtg.tablename    " + " from wo_test_trans as xtt,wo_test_trans_para as xttp,wo_test_group as xtg where xtt.record  in " + " (select wo_test_group_id from wo_test_group )" + " and xtt.wo_test_group_id in (select wo_test_group_id from wo_test_group" + " where  upper(name)='TAG' and ad_client_id='0')" + " and lower(xtt.tablename) = 'wo_test_group' and lower(field) = 'wo_test_group_id'" + " and xtt.wo_test_trans_id = xttp.wo_test_trans_id and xtg.wo_test_group_id = xtt.record " + " and xttp.wo_test_para_id in (select wo_test_para_id from wo_test_para" + " where  upper(name)='V2' and ad_client_id='0') limit 15 ";
        ResultSet rsGroupResult = DBManager.getSelect(queryGroupResult);
        try {
            while (rsGroupResult.next()) {
                int count = 0;
                String G_result = rsGroupResult.getString(3);
                String wo_TestGroupID = rsGroupResult.getString(4);
                String xtabTableName = rsGroupResult.getString(5);
                if ((G_result != null || !(G_result.equals("")))) {
                    String queryTabResult = "select xtt.wo_test_trans_id,xtt.tablename,xttp.result" + " from wo_test_trans as xtt,wo_test_trans_para as xttp where " + " xtt.record  in (select ad_tab_id from ad_tab ) and xtt.wo_test_group_id in" + " (select wo_test_group_id from wo_test_group where  upper(name)='TAG' " + " and ad_client_id='0') and lower(tablename) = 'ad_tab' and lower(field) = 'ad_tab_id'" + " and xtt.wo_test_trans_id = xttp.wo_test_trans_id and " + " xttp.wo_test_para_id = (select wo_test_para_id from wo_test_para" + " where  upper(name)='V2' and ad_client_id='0') and xttp.result = '" + G_result + "'";
                    ResultSet rsTabResult = DBManager.getSelect(queryTabResult);
                    while (rsTabResult.next()) {
                        String queryStatus = "select * from wo_test_group where wo_test_group_id = '" + wo_TestGroupID + "'";
                        ResultSet rsStatus = DBManager.getSelect(queryStatus);
                        String xtabTableName2 = "";
                        rsStatus.next();
                        xtabTableName2 = rsStatus.getString("tablename");
                        String T_result = "";
                        T_result = rsTabResult.getString(3);
                        if ((T_result != null || !(T_result.equals(""))) && (xtabTableName2 == null || (xtabTableName2.equals("")))) {
                            if (T_result.equals(G_result)) {
                                count++;
                            }
                        }
                    }
                    rsTabResult.close();
                }
                if (count > 0) {
                    String queryUpdate = " update wo_test_group set tzd_status = '1', tz_status = '1'" + " where wo_test_group_id = '" + wo_TestGroupID + "'";
                    int u = DBManager.getUpdate(queryUpdate);
                    Wo_Tablization tz = new Wo_Tablization();
                    tz.createXtabTable(wo_TestGroupID);
                }
            }
            rsGroupResult.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void copyXtabDataToTable() {
        String queryTestTransID = "select distinct wo_test_trans_id,field,wtt.tablename,record," + " wtt.wo_test_group_id,wtg.name,wtg.tablename" + " from wo_test_trans as wtt,wo_test_group as wtg" + " where record is not null and" + " wtt.wo_test_group_id = wtg.wo_test_group_id  ";
        ResultSet rsTestTransID = DBManager.getSelect(queryTestTransID);
        try {
            while (rsTestTransID.next()) {
                String testTransId = rsTestTransID.getString(1);
                String field = rsTestTransID.getString(2);
                String tableName = rsTestTransID.getString(3);
                String record = rsTestTransID.getString(4);
                String testGroupId = rsTestTransID.getString(5);
                String testGroupName = rsTestTransID.getString(6);
                String xtabTableName = rsTestTransID.getString(7);
                Wo_Tablization tz = new Wo_Tablization();
                boolean alterColFlag = false;
                boolean alterConsFlag = false;
                String queryData = "select wo_test_trans_para_id,wtp.wo_test_para_id,wtp.name,result" + " from wo_test_trans_para as wttp " + " join wo_test_para as wtp on wtp.wo_test_para_id = wttp.wo_test_para_id" + " where wo_test_trans_id ='" + testTransId + "'";
                Vector<Object> testParaId = new Vector<Object>();
                Vector<Object> testParaName = new Vector<Object>();
                Vector<Object> FinalParaName = new Vector<Object>();
                Vector<Object> result = new Vector<Object>();
                ResultSet rsData = DBManager.getSelect(queryData);
                try {
                    while (rsData.next()) {
                        testParaId.add(rsData.getString(2));
                        testParaName.add(rsData.getString(3));
                        if ((rsData.getString(3).toString() != null) || !(rsData.getString(3).toString().equals(""))) {
                            result.add(rsData.getString(4));
                        }
                    }
                    rsData.close();
                } catch (SQLException e) {
                    result.add("");
                    e.printStackTrace();
                }
                for (int i = 0; i < testParaId.size(); i++) {
                    String xtabParaName = testParaName.elementAt(i).toString();
                    while (xtabParaName.indexOf(" ") != -1) {
                        xtabParaName = xtabParaName.replace(" ", "_");
                    }
                    xtabParaName = xtabParaName + "_" + testParaId.elementAt(i);
                    FinalParaName.add(xtabParaName);
                }
                Hashtable<Object, Object> h = new Hashtable<Object, Object>();
                SimpleDateFormat smp = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                Date date = new Date();
                h.put("ad_client_id", "0");
                h.put("ad_org_id", "0");
                h.put("created", date);
                h.put("createdby", "0");
                h.put("updated", date);
                h.put("updatedby", "0");
                h.put("isactive", "Y");
                h.put("tablename", tableName);
                h.put("record", record);
                for (int i = 0; i < FinalParaName.size(); i++) {
                    h.put(FinalParaName.elementAt(i).toString(), result.elementAt(i).toString());
                }
                int inserted = DBManager.getInsert(xtabTableName, h);
                boolean delFlag = false;
                if (inserted == 1) {
                    delFlag = deleteTestData(testTransId);
                    System.out.println("TEst Trans Deleted = " + delFlag);
                }
            }
            rsTestTransID.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteTestData(String testTransId) {
        boolean flag = false;
        String queryTransPara = "delete from wo_test_trans_para where wo_test_trans_id = '" + testTransId + "'";
        int delTransPara = DBManager.getDelete(queryTransPara);
        String queryTestTrans = "delete from wo_test_trans where wo_test_trans_id = '" + testTransId + "'";
        int delTestTrans = DBManager.getDelete(queryTestTrans);
        if (delTestTrans == 1 && delTransPara == 1) flag = true;
        return flag;
    }

    public void createXtabTableSimple() {
        int count = 0;
        String queryGroupInfo = "select * from wo_test_group limit 5 ";
        ResultSet rsGroupInfo = DBManager.getSelect(queryGroupInfo);
        try {
            while (rsGroupInfo.next()) {
                String woTestGroupId = rsGroupInfo.getString("wo_test_group_id");
                String queryUpdate = " update wo_test_group set tzd_status = '1', tz_status = '1'" + " where wo_test_group_id = '" + woTestGroupId + "'";
                int u = DBManager.getUpdate(queryUpdate);
                count = count + 1;
                Wo_Tablization tz = new Wo_Tablization();
                tz.createXtabTable(woTestGroupId);
            }
            rsGroupInfo.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("count = " + count);
    }

    public static void main(String[] args) {
        Wo_TablizationCopy wtc = new Wo_TablizationCopy();
        wtc.createXtabTableSimple();
    }
}
