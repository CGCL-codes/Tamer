package com.teliose.accessor.administrator;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Query;
import org.springframework.dao.DataAccessException;
import com.teliose.accessor.GenericHibernateDAO;
import com.teliose.entity.persistence.administrator.TblUserLog;
import com.teliose.entity.persistence.administrator.TblUserLogId;

/**
 * 
 * @author Prabath Ariyarathna
 */
public class TblUserLogDAOImpl extends GenericHibernateDAO<TblUserLog, TblUserLogId> implements TblUserLogDAO {

    @Override
    public TblUserLog registerLogOutInfo(String sessionUserLoginId, String userId) {
        TblUserLog userLogOutFinalData = new TblUserLog();
        try {
            TblUserLogId uniquerLogId = new TblUserLogId();
            uniquerLogId.setLogId(sessionUserLoginId);
            uniquerLogId.setUserId(userId);
            Object params[] = new Object[2];
            params[0] = uniquerLogId.getUserId();
            params[1] = uniquerLogId.getLogId();
            List<TblUserLog> userLogOut = super.findByNamedQuery("findLogDetails", params);
            System.out.println("list size==" + userLogOut.size());
            userLogOut.get(0).setLastLogoutTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
            userLogOutFinalData = super.makePersistent(userLogOut.get(0));
        } catch (DataAccessException e) {
            logger.error("DataAcessException :" + e);
            return null;
        } catch (Exception e) {
            logger.error("Exception :" + e);
            return null;
        }
        return userLogOutFinalData;
    }

    @Override
    public TblUserLog registerLogInInfo(TblUserLog userLogIn) {
        TblUserLog userLog = new TblUserLog();
        try {
            userLog = super.makePersistent(userLogIn);
            return userLog;
        } catch (DataAccessException e) {
            logger.error("DataAccessException : " + e);
            return null;
        } catch (Exception e) {
            logger.error("Exception :" + e);
            return null;
        }
    }

    @Override
    public List<TblUserLog> getUserLogList(int min, int size) {
        try {
            List<TblUserLog> userLogDetails = null;
            Query q = getSession().getNamedQuery("getAllUserLogDetails");
            q.setFirstResult(min);
            q.setMaxResults(size);
            userLogDetails = q.list();
            if (userLogDetails != null) return userLogDetails; else return null;
        } catch (DataAccessException e) {
            logger.error("DataAccessException : " + e);
            return null;
        } catch (Exception e) {
            logger.error("Exception :" + e);
            return null;
        }
    }

    @Override
    public int recordSize() {
        try {
            List<TblUserLog> userLogDetails = findAll();
            int recordSize = userLogDetails.size();
            return recordSize;
        } catch (DataAccessException e) {
            logger.error("DataAccessException : " + e);
            return 0;
        } catch (Exception e) {
            logger.error("Exception :" + e);
            return 0;
        }
    }
}
