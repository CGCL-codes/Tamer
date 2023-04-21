package ces.platform.system.ldap;

import java.util.Vector;
import ces.platform.system.common.ConfigReader;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;

/**
 * @author  liusheng
 * @version 1.0.0
 * ��˾��  �Ϻ�����
 * 2005-3-22
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Pool {

    public static Pool instance;

    private String ldapHost = ConfigReader.getInstance().getAttribute("platform.system.ldap.ldappool.ldapHost");

    private int ldapPort = Integer.parseInt(ConfigReader.getInstance().getAttribute("platform.system.ldap.ldappool.ldapPort"));

    private int maxConn = Integer.parseInt(ConfigReader.getInstance().getAttribute("platform.system.ldap.ldappool.maxConn"));

    private int minConn = Integer.parseInt(ConfigReader.getInstance().getAttribute("platform.system.ldap.ldappool.minConn"));

    private String loginDN = ConfigReader.getInstance().getAttribute("platform.system.ldap.ldappool.loginDN");

    private String password = ConfigReader.getInstance().getAttribute("platform.system.ldap.ldappool.password");

    private int ldapVersion = Integer.parseInt(ConfigReader.getInstance().getAttribute("platform.system.ldap.ldappool.ldapVersion"));

    /**
     * ���ճ�
     */
    private Vector vPool;

    private Pool(int a) {
    }

    private Pool() {
        loginDN = "cn=Manager,ces=top";
        vPool = new Vector();
        for (int i = 0; i < minConn; i++) {
            try {
                LDAPConnection conn = new LDAPConnection();
                conn.connect(this.ldapHost, this.ldapPort);
                conn.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
                vPool.add(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized Pool getInstance() {
        if (instance == null) {
            instance = new Pool();
        }
        return instance;
    }

    public synchronized void release(LDAPConnection conn) throws LDAPException {
        if (vPool.size() < Integer.parseInt(ConfigReader.getInstance().getAttribute("platform.system.ldap.ldappool.maxConn"))) {
            vPool.add(conn);
        } else {
            conn.disconnect();
            conn = null;
        }
    }

    public synchronized LDAPConnection getConn() throws Exception, LDAPException {
        LDAPConnection conn = null;
        if (vPool.size() < minConn) {
            conn = new LDAPConnection();
            conn.connect(this.ldapHost, this.ldapPort);
            conn.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
        } else {
            conn = (LDAPConnection) vPool.firstElement();
            vPool.remove(0);
        }
        return conn;
    }
}
