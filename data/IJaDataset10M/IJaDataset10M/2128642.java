package org.notify4b.im.msn;

import org.notify4b.im.User;

public class MsnUser extends User {

    private String nickName;

    public MsnUser(String username, String password) {
        super(username, password);
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
