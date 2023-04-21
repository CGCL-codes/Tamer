package com.tieland.gentool;

import com.tieland.gentool.support.EntityRender;
import com.tieland.gentool.support.DAORender;
import com.tieland.gentool.support.DAOImplRender;
import com.tieland.gentool.model.Column;
import com.tieland.gentool.model.Table;
import com.tieland.gentool.apps.MainDialog;
import com.tieland.gentool.apps.TableDialog;
import javax.swing.*;
import java.sql.*;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2008-3-12
 * Time: 23:16:51
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, IllegalAccessException, InstantiationException {
        UIManager.setLookAndFeel("org.jvnet.substance.SubstanceLookAndFeel");
        Main main = new Main();
        main.hold = main;
        main.excute();
    }

    Main hold;

    private Table[] tables;

    String packageRoot;

    String dirRoot;

    String srcRoot;

    String viewRoot;

    String schema;

    String[] tableNames;

    String[] dbInfo;

    public String[] getTableNames() {
        return tableNames;
    }

    public void setTableNames(String[] tableNames) {
        this.tableNames = tableNames;
    }

    public void setDirRoot(String dirRoot) {
        this.dirRoot = dirRoot;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getViewRoot() {
        return viewRoot;
    }

    public void setViewRoot(String viewRoot) {
        this.viewRoot = viewRoot;
    }

    public void setPackageRoot(String packageRoot) {
        this.packageRoot = packageRoot;
    }

    public Table[] getTables() {
        return tables;
    }

    public void setTables(Table[] tables) {
        this.tables = tables;
    }

    static String GET_ALL_TABLE_NAME = "SELECT TABLE_NAME FROM TABLES WHERE TABLE_SCHEMA = ?";

    static String GET_ALL_COLUMN = "SELECT COLUMN_NAME FROM COLUMNS WHERE TABLE_NAME = ?";

    /**
     * ��ִ�г���
     */
    private void excute() {
        MainDialog mainDialog = new MainDialog();
        mainDialog.setMain(hold);
        mainDialog.setBounds(250, 200, 500, 300);
        mainDialog.setVisible(true);
        TableDialog tableDialog = new TableDialog();
        tableDialog.setMain(hold);
        tableDialog.setBounds(250, 200, 500, 300);
        tableDialog.setVisible(true);
        this.packageRoot = getPackageRoot();
        this.dirRoot = getDirRoot();
        this.srcRoot = new StringBuffer(dirRoot).append(File.separator).append("src").toString();
        this.viewRoot = new StringBuffer(dirRoot).append(File.separator).append("views").toString();
        this.schema = getSchema();
        dbInfo = getDbInfo();
        Connection connection = getConnection(dbInfo);
        this.tables = getTables(connection);
        closeConnection(connection);
        new EntityRender().render("Entity.vm", srcRoot, packageRoot, tables);
        new DAORender().render("DAO.vm", srcRoot, packageRoot, tables);
        new DAOImplRender().render("DAOImpl.vm", srcRoot, packageRoot, tables);
        System.exit(0);
    }

    /**
     * ��ȡ��������ǰ׺�޶���
     *
     * @return ���ذ���ǰ׺�޶���
     */
    String getPackageRoot() {
        return this.packageRoot;
    }

    /**
     * ����ݿ������л�ȡ�������Ϣģ��
     *
     * @param connection ��ݿ�����
     *
     * @return �����Ϣģ������
     */
    Table[] getTables(Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(GET_ALL_TABLE_NAME);
            preparedStatement.setString(1, schema);
            resultSet = preparedStatement.executeQuery();
            String[][] tableNames = getArr(resultSet);
            Table[] tables = new Table[tableNames.length];
            String[][] colomnNames;
            Column[] columns;
            for (int i = 0; i < tableNames.length; i++) {
                tables[i] = new Table();
                tables[i].setTableName(tableNames[i][0]);
                preparedStatement = connection.prepareStatement(GET_ALL_COLUMN);
                preparedStatement.setString(1, tableNames[i][0]);
                resultSet = preparedStatement.executeQuery();
                colomnNames = getArr(resultSet);
                columns = new Column[colomnNames.length];
                for (int j = 0; j < colomnNames.length; j++) {
                    columns[j] = new Column();
                    columns[j].setColumnName(colomnNames[j][0]);
                }
                tables[i].setColumns(columns);
            }
            preparedStatement.close();
            resultSet.close();
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    /**
     * ���ؽ��Ķ�ά�ַ��������̬
     *
     * @param resultSet JDBC���
     *
     * @return ���Ķ�ά�ַ��������̬
     */
    String[][] getArr(ResultSet resultSet) {
        try {
            resultSet.last();
            int row = resultSet.getRow();
            resultSet.beforeFirst();
            int column = resultSet.getMetaData().getColumnCount();
            String[][] arr = new String[row][column];
            for (int i = 0; i < row; i++) {
                resultSet.next();
                for (int j = 0; j < column; j++) {
                    arr[i][j] = resultSet.getString(j + 1);
                }
            }
            return arr;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ���Ŀ���Ŀ¼
     *
     * @return ��Ŀ¼
     */
    private String getDirRoot() {
        return this.dirRoot;
    }

    void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ��ȡ��ݿ�����
     *
     * @param dbInfo ��ݿ�������Ϣ
     *
     * @return ��ݿ�����
     */
    Connection getConnection(String[] dbInfo) {
        try {
            Class.forName(dbInfo[0]).newInstance();
            return DriverManager.getConnection(dbInfo[1], dbInfo[2], dbInfo[3]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * �����ݿ�������Ϣ
     *
     * @return ��ݿ���Ϣ����
     */
    public String[] getDbInfo() {
        return this.dbInfo;
    }

    String getSchema() {
        return this.schema;
    }

    public void setDbInfo(String[] dbInfo) {
        this.dbInfo = dbInfo;
    }
}
