package ces.coffice.docmanage.dao.imp;

import java.util.Collection;
import ces.coffice.common.base.BaseVo;
import ces.coffice.common.base.DbBase;
import ces.coffice.common.base.BaseDao;
import java.util.Vector;
import java.sql.ResultSet;
import ces.platform.system.common.ValueAsc;
import ces.coffice.docmanage.vo.ReceiveDocTransact;
import java.sql.Connection;
import ces.coral.dbo.DBOperation;
import java.sql.PreparedStatement;

/**
 *
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: CES</p>
 *
 * @author chenlei
 * @version 1.0
 */
public class ReceiveDocTransactDaoImp extends DbBase implements BaseDao {

    private static final String TRANSACT_TABLE = "coffice_doc_rcv_trasact";

    public void doAddBatch(Collection entitys) {
    }

    public void doDelBatch() {
    }

    public boolean doDelete(BaseVo vo) {
        return false;
    }

    public void doNew(BaseVo bo) {
    }

    public boolean doUpdate(BaseVo bo) {
        return false;
    }

    public void doUpdateBatch() {
    }

    public BaseVo getEntity(String condition) throws Exception {
        ReceiveDocTransact doc = null;
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select id,doc_id,opinion,trasactor_id,trasactor,trsct_dept_id,trsct_dept,trsct_date,task_id from " + TRANSACT_TABLE + " where 1=1 ";
        sql += (condition == null ? "" : condition);
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rset = preparedStatement.executeQuery();
            int i = 1;
            ValueAsc va = new ValueAsc(i);
            if (rset.next()) {
                i = 1;
                va.setStart(i);
                doc = generateRecieve(rset, va);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
        return doc;
    }

    public Vector getEntities(String condition) throws Exception {
        Vector rs = new Vector();
        ReceiveDocTransact doc = null;
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select id,doc_id,opinion,trasactor_id,trasactor,trsct_dept_id,trsct_dept,trsct_date,task_id from " + TRANSACT_TABLE + " where 1=1 ";
        sql += (condition == null ? "" : condition);
        try {
            dbo = createDBOperation();
            connection = dbo.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rset = preparedStatement.executeQuery();
            int i = 1;
            ValueAsc va = new ValueAsc(i);
            while (rset.next()) {
                i = 1;
                va.setStart(i);
                doc = generateRecieve(rset, va);
                rs.add(doc);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
        return rs;
    }

    /**
     * ��ɲ�ѯ���İ������
     * @param result   ��ѯ���
     * @param v        ������
     * @return         ������ɵĶ���
     */
    public static ReceiveDocTransact generateRecieve(ResultSet result, ValueAsc v) {
        ReceiveDocTransact rTemp = null;
        try {
            rTemp = new ReceiveDocTransact(result.getInt(v.next()));
            rTemp.setDocId(result.getInt(v.next()));
            rTemp.setOpinion(result.getString(v.next()));
            rTemp.setTrasactorId(result.getInt(v.next()));
            rTemp.setTrasactor(result.getString(v.next()));
            rTemp.setTrsctDeptId(result.getLong(v.next()));
            rTemp.setTrsctDept(result.getString(v.next()));
            rTemp.setTrsctDate(result.getString(v.next()));
            rTemp.setTaskId(result.getInt(v.next()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rTemp;
    }

    public static void main(String[] args) throws Exception {
        Vector rs = new ReceiveDocTransactDaoImp().getEntities(null);
    }

    public void doDelBatch(Collection entitys) throws Exception {
    }

    public void doUpdateBatch(Collection entitys) throws Exception {
    }
}
