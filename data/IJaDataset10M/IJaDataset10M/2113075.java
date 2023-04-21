package org.frameworkset.spi.txsyn;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionManager;

public class A2 implements AI {

    /**
	 * ͬ�����÷���ִ��ʧ��
	 */
    public void testNoTXSyn() throws Exception {
        System.out.println("A2.testNoTXSyn():");
        System.out.println("A2.testNoTXSyn() context tx:" + TransactionManager.getTransaction());
        DBUtil db = new DBUtil();
        db.executeInsert("insert into test(name1) values('A2.testNoTXSyn()')");
    }

    /**
	 * ����ִ��ʧ��
	 */
    public void testTXNoSyn() throws Exception {
        System.out.println("A2.testTXNoSyn():");
        System.out.println("A2.testTXNoSyn() context tx:" + TransactionManager.getTransaction());
        DBUtil db = new DBUtil();
        db.executeInsert("insert into test(name1) values('A2.testTXNoSyn()')");
    }

    /**
	 * ����ִ��ʧ�ܣ�ͬ������������ɹ��������������ʧ��
	 */
    public void testTXSynFailed() throws Exception {
        System.out.println("A2.testTXSynFailed():");
        System.out.println("A2.testTXSynFailed() context tx:" + TransactionManager.getTransaction());
        DBUtil db = new DBUtil();
        db.executeInsert("insert into test(name1) values('A2.testTXSynFailed()')");
    }

    public void testTXSynSuccess() throws Exception {
        System.out.println("A2.testTXSynSuccess():");
        System.out.println("A2.testTXSynSuccess() context tx:" + TransactionManager.getTransaction());
        DBUtil db = new DBUtil();
        String id = db.getNextStringPrimaryKey("test");
        db.executeInsert("insert into test(id,name) values('" + id + "','A2.testTXSynSuccess()')");
    }

    public void testNoTXNoSyn() throws Exception {
        System.out.println("A2.testNoTXNoSyn():");
        System.out.println("A2.testNoTXNoSyn() context tx:" + TransactionManager.getTransaction());
        DBUtil db = new DBUtil();
        String id = db.getNextStringPrimaryKey("test");
        db.executeInsert("insert into test(id,name) values('" + id + "','A2.testNoTXNoSyn()')");
    }

    public void testWithSpecialException(int type) throws Exception {
        System.out.println("A2.testWithSpecialException(int type):");
        System.out.println("A2.testWithSpecialException(int type) context tx:" + TransactionManager.getTransaction());
        DBUtil db = new DBUtil();
        if (type == 1) {
            String id = db.getNextStringPrimaryKey("test");
            db.executeInsert("insert into test(id,name) values('" + id + "','A2.testWithSpecialException(A2 throw tx SynException)')");
            throw new SynException("A2 throw tx SynException .");
        } else {
            String id = db.getNextStringPrimaryKey("test");
            db.executeInsert("insert into test(id,name) values('" + id + "','A2.testWithSpecialException(A2 throw tx Exception)')");
            throw new Exception("A2 throw tx Exception .");
        }
    }
}
