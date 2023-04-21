package hogs.net.messages;

import hogs.net.ServerHandler;

/**
 * Represents a remote client trying to authenticate as an admin.
 * @author dapachec
 */
public class AdminAuthenticate extends AdminMessage {

    private String m_name;

    private String m_password;

    public AdminAuthenticate(String name, String pw) {
        m_name = name;
        m_password = pw;
    }

    public static AdminAuthenticate deserialize(String str) {
        String[] parts = str.split("\t", 2);
        return new AdminAuthenticate(parts[0], parts[1]);
    }

    public String getName() {
        return m_name;
    }

    public String getPassword() {
        return m_password;
    }

    public String serialize() {
        return hogs.common.Util.tabDelimit("admin", "authenticate", m_name, m_password);
    }

    public void handleServer(ServerHandler handler) {
        handler.handleAdminAuthenticate(this);
    }
}
