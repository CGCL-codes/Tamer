package jvc.util.db.page;

import jvc.util.RecordSetUtils;
import jvc.util.db.MyDB;
import jvc.web.module.*;
import java.util.*;
import java.sql.*;

/**
 * <p>Title :OraclePage</p>
 * <p>Description:Oracle��ҳʵ����</p>
 * <p>Created on 2004-3-3</p>
 * <p>Company :JVC</p>
 *  @author : Ru_fj
 *  @version : 1.0
 */
public class OraclePage extends DBPage {

    public boolean init(MyDB mydb, String SourceSql, List Params, int RecordsPerPage, int CurPage) {
        if (!super.init(mydb, SourceSql, Params, RecordsPerPage, CurPage)) {
            try {
                PageContent = new JVCResult();
                String sql = "select count(*) from (" + SourceSql + ")";
                mydb.prepareStatement(sql);
                if (Params != null) {
                    Iterator iter = Params.iterator();
                    for (int i = 1; iter.hasNext(); i++) ((Field) iter.next()).setWhere(mydb, i);
                }
                ResultSet rsTotal = mydb.executeQuery();
                rsTotal.next();
                RecordsCount = rsTotal.getInt(1);
                sql = "select * from (select rownum r_id,t.*  from (" + SourceSql + ") t where rownum <= " + CurPage * RecordsPerPage + ") where r_id >" + (CurPage - 1) * RecordsPerPage;
                mydb.prepareStatement(sql);
                if (Params != null) {
                    Iterator iter = Params.iterator();
                    for (int i = 1; iter.hasNext(); i++) ((Field) iter.next()).setWhere(mydb, i);
                }
                ResultSet rs = mydb.executeQuery();
                TotalPageCount = (int) Math.ceil((RecordsCount + RecordsPerPage - 1) / RecordsPerPage);
                if (CurPage > TotalPageCount) CurPage = TotalPageCount;
                int recordindex = RecordsPerPage * (CurPage - 1);
                boolean isfirst = true;
                while (rs.next()) {
                    Map fs = new Hashtable();
                    fs.put("row", String.valueOf(++recordindex));
                    if (isfirst) PageContent.AddColumn(new Field(Field.FT_INT, "row", "���"));
                    for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
                        fs.put(rs.getMetaData().getColumnName(i).toLowerCase(), RecordSetUtils.getString(rs, i));
                        if (isfirst) PageContent.AddColumn(new Field(rs.getMetaData().getColumnType(i), rs.getMetaData().getColumnName(i), rs.getMetaData().getColumnName(i)));
                    }
                    PageContent.AddResult(fs);
                    isfirst = false;
                }
                this.Result = true;
            } catch (Exception e) {
                e.printStackTrace();
                ErrorMessage = e.getMessage();
            }
        }
        return this.Result;
    }

    public static void main(String[] arg) {
        OraclePage cp = new OraclePage();
        cp.init(new MyDB(), "select item_calltype,item_status,count(*) as item_count from helpdesk_items group by item_calltype,item_status", null, -1, 1);
        System.out.println("RecordsCount:" + cp.getRecordsCount());
        System.out.println("TotalPageCount:" + cp.getTotalPageCount());
    }
}
