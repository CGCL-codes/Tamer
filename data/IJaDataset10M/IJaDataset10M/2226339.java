package org.dmp.chillout.cpd.webwork.vo;

import java.util.ArrayList;
import java.util.List;

public class VLoginuser {

    private Integer type;

    private String nickname;

    private String errmessage;

    private List messages = new ArrayList();

    private String emailaddress;

    private String password;

    private String password2;

    private Integer userID;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * 
	 * @return
	 *
	 * @author Yuqiang
	 * @date 2006-12-25
	 */
    public String getPassword2() {
        return password;
    }

    /**
	 * 
	 * @param password2 the confirmed password
	 *
	 * @author Yuqiang
	 * @date 2006-12-25
	 */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public void setMessages(ArrayList messages) {
        this.messages = messages;
    }

    public List getMessages() {
        return messages;
    }

    public String getErrmessage() {
        return errmessage;
    }

    public void setErrmessage(String errmessage) {
        this.errmessage = errmessage;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
