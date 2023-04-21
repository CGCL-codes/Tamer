package net.adrianromero.tpv.ticket;

/**
 *
 * @author adrian
 */
public class UserInfo {

    private String m_sId;

    private String m_sName;

    /** Creates a new instance of UserInfoBasic */
    public UserInfo(String id, String name) {
        m_sId = id;
        m_sName = name;
    }

    public String getId() {
        return m_sId;
    }

    public String getName() {
        return m_sName;
    }
}
