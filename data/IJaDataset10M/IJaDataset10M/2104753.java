package com.bubblegumproject.keeper.protocol;

import com.bubblegumproject.ogo.action.Input;
import com.bubblegumproject.ogo.action.Output;
import com.bubblegumproject.keeper.domain.Member;

/**
 * @author Azubuko Obele (buko.obele@gmail.com)
 */
public class LoginAction extends KeeperAction {

    private String username;

    private String password;

    private Member authorizedMember;

    @Input
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Input
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Output
    public Member getAuthorizedMember() {
        return authorizedMember;
    }

    public void setAuthorizedMember(Member authorizedMember) {
        this.authorizedMember = authorizedMember;
    }
}
