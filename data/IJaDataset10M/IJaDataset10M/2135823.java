package com.gever.jdbc.database.dialect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ��ݿⷽ��,��װ��ݿ������﷨
 * @author Hu.Walker
 */
public abstract class Dialect {

    /**
     * �����ݿⶼ�з�ҳ�������﷨,���������Ŀ�ľ��ǽ�һ���sql��ѯ���ת��Ϊ��ҳ���
     * @param sql ԴSQL
     * @return ��ҳSQL,��������placeHolder(?),��ʼ��¼�����ѯ�ļ�¼��Ŀ
     */
    public String getLimitString(String sql) {
        return sql;
    }

    public PreparedStatement setStatementPageValue(PreparedStatement pstmt, int start, int startValue, int end, int endValue) throws SQLException {
        pstmt.setInt(start, startValue);
        pstmt.setInt(end, endValue);
        return pstmt;
    }
}
