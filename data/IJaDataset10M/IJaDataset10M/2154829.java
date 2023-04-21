package ces.coffice.addrslist.vo;

import ces.coffice.common.base.BaseVo;
import java.util.List;

/**
 * ͨѶ¼����
 */
public class AddrslistAuth extends BaseVo {

    private int folderId;

    private int userId;

    private String auth;

    private String userName;

    public AddrslistAuth(int userId) {
        this.userId = userId;
    }

    public int getFolderId() {
        return this.folderId;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getAuth() {
        return this.auth;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    /**
	 * @return
	 */
    public String getUserName() {
        return this.userName;
    }

    /**
	 * @param string
	 */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
