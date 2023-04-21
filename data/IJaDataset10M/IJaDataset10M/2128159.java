package cranix.common.util;

import java.sql.*;
import javax.naming.*;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import java.util.Hashtable;

/**
 * jndi �� �̿��ؼ� DataSource �� ������ Ŀ�ؼ��� ���´�.
 * 
 * @author cranix
 *
 */
public abstract class SuperConnectionManager {

    private static Logger logger = Logger.getLogger(SuperConnectionManager.class);

    private Hashtable<String, DataSource> table = null;

    private Context initContext = null;

    private int connCount = 0;

    public SuperConnectionManager() throws Exception {
        if (logger.isDebugEnabled()) logger.debug("SuperConnectionManager()");
        table = new Hashtable<String, DataSource>();
        initContext = (Context) new InitialContext().lookup("java:comp/env/");
    }

    public DataSource getDataSource(String name) throws Exception {
        if (logger.isDebugEnabled()) logger.debug("getDataSource()");
        DataSource ds = table.get(name);
        if (ds == null) {
            ds = (DataSource) initContext.lookup(name);
            table.put(name, ds);
        }
        return ds;
    }

    public Connection openConnection(String name) throws Exception {
        if (logger.isDebugEnabled()) logger.debug("openConnection()");
        DataSource ds = getDataSource(name);
        Connection conn = ds.getConnection();
        connCount++;
        return conn;
    }

    public void closeConnection(Connection conn) throws Exception {
        if (logger.isDebugEnabled()) logger.debug("closeConnection()");
        conn.close();
        conn = null;
        connCount--;
    }

    /**
	 * �׽�Ʈ ������ ���Ǵ� �޼ҵ��
	 * connCount ������ 0 �� �ƴ� ���°� �������� ���ӵǸ� conn ��ȯ�� ���� �ʾҴٴ� �Ҹ���.
	 *  
	 * @return
	 */
    public int getConnCount() {
        return connCount;
    }
}
