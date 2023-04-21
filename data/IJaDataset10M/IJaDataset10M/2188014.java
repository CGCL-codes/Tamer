package com.ysh.dbpool;

import java.sql.SQLException;
import com.mysql.jdbc.Driver;
import com.ysh.dbpool.common.MyReader;
import java.util.Stack;

/**
 * A class to test dbpool.<br>
 * This class do not support multithread
 * 
 * @author zhongchuang
 */
public class DBPoolTest implements Runnable {

    private DBPool dbPool;

    private Stack<MyConnection> store;

    private int size;

    public DBPoolTest(DBPool dbPool) throws ClassNotFoundException, SQLException {
        this.dbPool = dbPool;
        this.store = new Stack<MyConnection>();
        this.size = 0;
    }

    public void startTest() throws SQLException, InterruptedException {
        this.help();
        String command = "";
        while (true) {
            DBPoolTest.print(":");
            command = MyReader.readLine();
            this.executeCommand(command);
            Thread.sleep(100);
        }
    }

    private void executeCommand(String command) throws SQLException, InterruptedException {
        command = command.trim().toLowerCase();
        if (command.equals("get")) {
            this.getConnection(true);
        } else if (command.equals("gett")) {
            this.getConnection(false);
        } else if (command.equals("cons")) {
            this.printCount();
        } else if (command.equals("rel")) {
            relaseConnection();
        } else if (command.equals("clo")) {
            closeDBPool();
        } else if (command.equals("info")) {
            this.printInfoOfDBPool();
        } else if (command.equals("help")) {
            this.help();
        } else if (command.equals("exit")) {
            this.closeDBPool();
            DBPoolTest.print("����ɹ��˳�\n");
            System.exit(0);
        } else {
            System.out.println("�Բ��� " + command + " ���޷�ʶ�������");
        }
    }

    private void getConnection(boolean always) throws SQLException, InterruptedException {
        long waitTime = Long.MAX_VALUE;
        if (!always) {
            System.out.println("������ȴ���ʱ��");
            waitTime = MyReader.readLineLong();
        }
        System.out.println("��������Ҫ��ȡ�����ӵĸ���");
        int count = MyReader.readLineInt();
        int temp = 0;
        MyConnection myCon = null;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            myCon = this.dbPool.getConnection(waitTime);
            if (myCon != null) {
                this.store.push(myCon);
                temp++;
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("�ɹ���ȡ " + temp + " ������\t����ʱ��: " + (endTime - startTime) / 1000.0 + "s");
        this.size += temp;
    }

    private void printCount() {
        System.out.println("��ǰ�Ѿ���ȡ���ӵĸ���:\t" + this.size);
    }

    private void relaseConnection() throws SQLException {
        int count = 0;
        System.out.println("������Ҫ�ͷŵ����ӵĸ���");
        boolean done = false;
        do {
            done = true;
            count = MyReader.readLineInt();
            if (count < 0 || count > this.size) {
                System.out.println("���ֳ���");
                System.out.println("����Ӧ������0��" + this.size + "֮��");
                done = false;
            }
        } while (!done);
        for (int i = 0; i < count; i++) this.dbPool.releaseConnection(this.store.pop());
        this.size -= count;
        System.out.println("�ɹ��ͷ�" + count + "������");
    }

    private void closeDBPool() throws SQLException {
        this.dbPool.closeAllConnection();
        while (!this.store.isEmpty()) this.dbPool.releaseConnection(this.store.pop());
        this.size = 0;
    }

    public void printInfoOfDBPool() {
        System.out.println("\n===============��ݿ����ӳ�:" + dbPool + "�Ļ���Ϣ===============");
        System.out.println("��ʼ��������:\t\t" + dbPool.getInitialSize());
        System.out.println("��������:\t\t\t" + dbPool.getIncreaseSize());
        System.out.println("�������������:\t\t" + dbPool.getMaxSize());
        System.out.println("�ڴ�����������������:\t" + dbPool.getMaxConnsInMemory());
        System.out.println("��ǰά�������ӵĸ���:\t" + dbPool.getCurrentSize());
        System.out.println("��ǰջ�п������Ӹ���:\t" + dbPool.getNumOfConnsInStack());
        System.out.println("��ǰ�����Ի�ȡ�����ӵĸ���:\t" + (this.dbPool.getMaxSize() - this.dbPool.getCurrentSize() + this.dbPool.getNumOfConnsInStack()));
        System.out.println("===========================================\n");
    }

    public static void print(String info) {
        System.out.print("<DBTest" + Thread.currentThread().getName() + ">" + info + " ");
        System.out.flush();
    }

    public void help() {
        System.out.println("\n============�����ĵ�================");
        System.out.println("get:\t�����ߵȴ�����ȡ����");
        System.out.println("gett:\t��ָ���ĵȴ�ʱ������ȡ����");
        System.out.println("cons:\t��ӡ��ǰ�Ѿ�ȡ�ĵ����ӵĸ���");
        System.out.println("rel:\t�ͷ�����");
        System.out.println("clo:\t�ر����ӳ�");
        System.out.println("info:\t��ӡ���ӳصĻ���Ϣ");
        System.out.println("help:\t��ӡ������Ϣ");
        System.out.println("exit:\t�˳�����");
        System.out.println("===========================================\n");
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {
        DBPool pool = DBPoolWrapper.getDBPool("dbPool.xml");
        DBPoolTest test = new DBPoolTest(pool);
        test.startTest();
    }

    public void run() {
        try {
            this.startTest();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
