package com.sax.michael.beans;

public class User {

    /**
	 * 用戶名
	 */
    String userName;

    /**
	 * 密碼
	 * 用MD5 加密
	 */
    String password;

    /**
	 * email
	 */
    String email;

    /**
	 * 權限
	 */
    String rights;

    /**
	 * 
	 * @return String userName
	 */
    public String getUserName() {
        return userName;
    }

    /**
	 *  Set userName
	 * @param  String userName
	 */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
	 * 
	 * @return password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * 
	 * @param String password
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * 
	 * @return email
	 */
    public String getEmail() {
        return email;
    }

    /**
	 * 
	 * @param email
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * 
	 * @return String rights
	 */
    public String getRights() {
        return rights;
    }

    /**
	 * 
	 * @param String rights
	 */
    public void setRights(String rights) {
        this.rights = rights;
    }
}
