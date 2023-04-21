package com.gever.goa.infoservice.dao.impl;

import com.gever.exception.DefaultException;
import com.gever.goa.infoservice.dao.SysInfoTypeDao;
import com.gever.goa.infoservice.vo.SysInfoTypeVO;
import com.gever.jdbc.BaseDAO;
import com.gever.jdbc.sqlhelper.DefaultSQLHelper;
import com.gever.jdbc.sqlhelper.SQLHelper;
import com.gever.vo.VOInterface;

/**
 * <p>Title: ��������DAOʵ����</p>
 * <p>Description: KOBE OFFICE ��Ȩ���У�Υ�߱ؾ���</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: KOBE OFFICE</p>
 * @author Hu.Walker
 * @version 1.0
 */
public class SysInfoTypeDaoImpl extends BaseDAO implements SysInfoTypeDao {

    private static StringBuffer querySQL = new StringBuffer();

    static {
        querySQL.append(" SELECT ");
        querySQL.append(" SYS_INFO_TYPE.TYPE_CODE, ");
        querySQL.append(" SYS_INFO_TYPE.TYPE_NAME, ");
        querySQL.append(" SYS_INFO_TYPE.REMARK ");
        querySQL.append(" FROM ");
        querySQL.append(" SYS_INFO_TYPE ");
        querySQL.append(" WHERE ");
        querySQL.append(" 1=1 ");
    }

    public SysInfoTypeDaoImpl(String dbData) {
        super(dbData);
    }

    public String getPageSql() {
        return querySQL.toString();
    }

    /**
     * ����oracle��ݿ�SQL
     */
    public void setOracleSQL() {
    }

    /**
     * ����
     */
    public int insert(VOInterface vo) throws DefaultException {
        SQLHelper helper = new DefaultSQLHelper(dbData);
        SysInfoTypeVO sitVO = (SysInfoTypeVO) vo;
        String pkName = vo.getPkFields();
        pkName = toPkFld(pkName);
        vo.setValue(pkName, sitVO.getType_code());
        helper.setAutoID(false);
        int iRet = helper.insert(vo);
        return iRet;
    }
}
