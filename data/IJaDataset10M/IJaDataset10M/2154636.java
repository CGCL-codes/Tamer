package iwallet.client.gui;

/**
 * ��¼���ڵ�������ϸ��Ϣ
 * @author ����
 */
public class ConnectionDetails {

    private String userId, server, password;

    public ConnectionDetails(String userId, String server, String password) {
        this.userId = userId;
        this.server = server;
        this.password = password;
    }

    /**
	 * ȡ���û���
	 * @return �û���
	 */
    public String getUserId() {
        return userId;
    }

    /**
	 * ȡ�÷�����
	 * @return ��������
	 */
    public String getServer() {
        return server;
    }

    /**
	 * ȡ������
	 * @return ����
	 */
    public String getPassword() {
        return password;
    }
}
