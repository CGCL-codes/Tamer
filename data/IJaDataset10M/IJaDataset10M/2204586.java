package net.sf.ikms.domain;

import java.io.Serializable;

/**
 * 用户信息表
 * 
 * @author <b>oxidy</b>, Copyright &#169; 2003-2011
 * @version 0.1,2011-3-2
 */
public class User implements Serializable {

    private static final long serialVersionUID = -9018967889074621762L;

    private String userCode;

    /**
	 * 用户编码
	 */
    private String userId;

    /**
	 * 用户名
	 */
    private String userName;

    /**
	 * 用户密码
	 */
    private String userPassword;

    /**
	 * 用户 昵称
	 */
    private String userNickName;

    /**
	 * 用户邮箱
	 */
    private String userEmail;

    /**
	 * 用户类型
	 */
    private String userType;

    /**
	 * 用户是否可用
	 */
    private String isEnable;

    public User() {
    }

    public User(String userCode, String userId, String userName, String userPassword, String userNickName, String userEmail, String userType, String isEnable) {
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User [userCode=");
        builder.append(userCode);
        builder.append(", userId=");
        builder.append(userId);
        builder.append(", userName=");
        builder.append(userName);
        builder.append(", userPassword=");
        builder.append(userPassword);
        builder.append(", userNickName=");
        builder.append(userNickName);
        builder.append(", userEmail=");
        builder.append(userEmail);
        builder.append(", userType=");
        builder.append(userType);
        builder.append(", isEnable=");
        builder.append(isEnable);
        builder.append("]");
        return builder.toString();
    }
}
