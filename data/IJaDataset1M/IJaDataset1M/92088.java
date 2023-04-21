package com.dcivision.setup.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.UserInfoFactory;
import com.dcivision.setup.bean.SetupOptionWorkflowCategory;

/**
 *  SetupOptionWorkflowCategory main DAO
 *
 *  @author           Wong Yam Lee
 *  @company          DCIVision Limited
 *  @creation date    29/10/2003
 *  @version          $Revision: 1.6 $
 */
public class SetupOptionWorkflowCategoryDAObject extends AbstractSetupOptionDAObject {

    public static final String REVISION = "$Revision: 1.6 $";

    public static final String TABLE_NAME = "SETUP_OPTION_WORKFLOW_CATEGORY";

    public static final String OPTION_NAME = "WORKFLOW_CATEGORY_NAME";

    public static final String OPTION_CODE = null;

    public static final boolean HAS_OPTION_CODE = false;

    public static final String DEFAULT_ORDER = OPTION_NAME;

    /**
   * 
   */
    public SetupOptionWorkflowCategoryDAObject() {
        super();
    }

    /**
   * @param dbConn
   */
    public SetupOptionWorkflowCategoryDAObject(Connection dbConn) {
        super(dbConn);
    }

    /**
   * @param sessionContainer
   * @param dbConn
   */
    public SetupOptionWorkflowCategoryDAObject(SessionContainer sessionContainer, Connection dbConn) {
        super(sessionContainer, dbConn);
    }

    protected void initDBSetting() {
        this.baseTableName = TABLE_NAME;
        this.sOptionNameCol = OPTION_NAME;
        this.sOptionCodeCol = OPTION_CODE;
        this.sDefaultOrder = DEFAULT_ORDER;
        this.bHasOptionCode = HAS_OPTION_CODE;
        super.initDBSetting();
    }

    protected synchronized List getList() throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.WORKFLOW_CATEGORY_NAME, A.STATUS, A.DISPLAY_SEQ, A.SYS_IND, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   SETUP_OPTION_WORKFLOW_CATEGORY A ");
                sqlStat.append("WHERE     RECORD_STATUS = 'A' AND STATUS = 'A' ");
                sqlStat.append("ORDER BY  DISPLAY_SEQ ASC, WORKFLOW_CATEGORY_NAME ASC ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    SetupOptionWorkflowCategory tmpSetupOptionWorkflowCategory = new SetupOptionWorkflowCategory();
                    tmpSetupOptionWorkflowCategory.setID(getResultSetInteger(rs, "ID"));
                    tmpSetupOptionWorkflowCategory.setWorkflowCategoryName(getResultSetString(rs, "WORKFLOW_CATEGORY_NAME"));
                    tmpSetupOptionWorkflowCategory.setStatus(getResultSetString(rs, "STATUS"));
                    tmpSetupOptionWorkflowCategory.setDisplaySeq(getResultSetInteger(rs, "DISPLAY_SEQ"));
                    tmpSetupOptionWorkflowCategory.setSysInd(getResultSetString(rs, "SYS_IND"));
                    tmpSetupOptionWorkflowCategory.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpSetupOptionWorkflowCategory.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpSetupOptionWorkflowCategory.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpSetupOptionWorkflowCategory.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpSetupOptionWorkflowCategory.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpSetupOptionWorkflowCategory.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpSetupOptionWorkflowCategory.setCreatorName(UserInfoFactory.getUserFullName(tmpSetupOptionWorkflowCategory.getCreatorID()));
                    tmpSetupOptionWorkflowCategory.setUpdaterName(UserInfoFactory.getUserFullName(tmpSetupOptionWorkflowCategory.getUpdaterID()));
                    result.add(tmpSetupOptionWorkflowCategory);
                }
                return (result);
            } catch (ApplicationException appEx) {
                throw appEx;
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
                throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
            } catch (Exception e) {
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_SELECT_ERROR, e);
            } finally {
                try {
                    rs.close();
                } catch (Exception ignore) {
                } finally {
                    rs = null;
                }
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
        }
    }
}
