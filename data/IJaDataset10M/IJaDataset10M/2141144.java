package easyaccept.app.persistence;

import java.sql.SQLException;
import javax.persistence.PersistenceException;
import easyaccept.util.bean.BeanHelperUtil;
import easyaccept.util.reflection.Clazz;

/**
 * Handle jdbc and hibernate errors and convert to application messages
 * @author Fabrício Silva
 *
 */
public class PersistenceExceptionHandler implements easyaccept.util.persistence.PersistenceExceptionHandler {

    public void handle(Exception e) {
        e.printStackTrace();
    }

    public void tryHandle(Exception e) throws PersistenceException {
        Throwable cause = getCause(e);
        if (Clazz.isTypeOf(cause, SQLException.class)) {
            throw catchSQL((SQLException) cause);
        }
        throw unkown(cause);
    }

    /**
		 * Returns a exception with SQL error code translated
		 * @param e
		 * @return
		 */
    public PersistenceException catchSQL(SQLException e) {
        return new PersistenceException(BeanHelperUtil.getMessageResources().getMessage(SqlErrorsMap.ERRORMAP.get(e.getErrorCode())), e);
    }

    public PersistenceException unkown(Throwable e) {
        return new PersistenceException(e);
    }

    /**
		 * Returns the cause of exception if exists
		 * 
		 * @param exception
		 * @return
		 */
    private Throwable getCause(Exception exception) {
        Throwable e = exception;
        if (exception.getCause() != null) {
            e = exception.getCause();
        }
        return e;
    }
}
