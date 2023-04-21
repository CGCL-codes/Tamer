package net.sourceforge.customercare.server.entities.user;

import net.sourceforge.customercare.server.entities.Entry;
import net.sourceforge.customercare.server.helpers.Md5;

/**
 * a user entry
 */
public class User extends Entry {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private Integer rolId;

    private Integer splId;

    private Integer perId;

    private Integer chgctr;

    public User() {
        clear();
    }

    public Integer getChgctr() {
        return chgctr;
    }

    public void setChgctr(Integer chgctr) {
        this.chgctr = chgctr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password, boolean encrypt) {
        if (encrypt) this.password = Md5.md5(password); else this.password = password;
    }

    public Integer getPerId() {
        return perId;
    }

    public void setPerId(Integer perId) {
        this.perId = perId;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public Integer getSplId() {
        return splId;
    }

    public void setSplId(Integer splId) {
        this.splId = splId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        int top = username.length();
        if (top > 49) top = 49;
        this.username = username.substring(0, top);
    }

    public void clear() {
        super.clear();
        username = "";
        password = "";
        rolId = 0;
        splId = 0;
        perId = 0;
        chgctr = 0;
    }

    public String toString() {
        return username + ", " + password;
    }
}
