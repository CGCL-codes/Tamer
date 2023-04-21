package rubbish.db.core.handler;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ���^�f�[�^�����p�̃N���[�W��
 * 
 * @author $Author: winebarrel $
 * @version $Revision: 1.1 $
 */
public interface MetaDataProc {

    public ResultSet getResultSet(DatabaseMetaData dmd) throws SQLException;
}
