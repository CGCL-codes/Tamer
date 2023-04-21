package jvc.util.db;

import java.sql.*;

/**
 * <p>Title :WEB����ƽ̨</p>
 * <p>Description:ͨ����ݿ�ӿ�</p>
 * <p>Created on 2004-3-3</p>
 * <p>Company :</p>
 *  @author : Ru_fj
 *  @version : 1.0
 */
public abstract class BaseDBPool {

    public abstract void init();

    public abstract Connection getConn(String dataBaseName);

    public abstract void close(Connection conn);
}
