package j2blog.corepo;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;

public class User {

    private int id;

    private String username;

    private String password;

    private String dispName;

    private String email;

    private boolean isEmailHidden;

    private Date regTime;

    private String regIP;

    private int postNum;

    private int commNum;

    private Date lastVisitTime;

    private String lastVisitIp;

    private Set posts = new HashSet();

    public User() {
    }

    /**
	 * @return the id
	 */
    public int getId() {
        return id;
    }

    /**
	 * @param id the iD to set
	 */
    public void setId(int id) {
        this.id = id;
    }

    /**
	 * @return the username
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param username the username to set
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @param password the password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * @return the dispName
	 */
    public String getDispName() {
        return dispName;
    }

    /**
	 * @param dispName the dispName to set
	 */
    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    /**
	 * @return the email
	 */
    public String getEmail() {
        return email;
    }

    /**
	 * @param email the email to set
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * @return the isEmailHidden
	 */
    public boolean getIsEmailHidden() {
        return isEmailHidden;
    }

    /**
	 * @param isEmailHidden the isEmailHidden to set
	 */
    public void setIsEmailHidden(boolean isEmailHidden) {
        this.isEmailHidden = isEmailHidden;
    }

    /**
	 * @return the regTime
	 */
    public Date getRegTime() {
        return regTime;
    }

    /**
	 * @param regTime the regTime to set
	 */
    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    /**
	 * @return the regIP
	 */
    public String getRegIP() {
        return regIP;
    }

    /**
	 * @param regIP the regIP to set
	 */
    public void setRegIP(String regIP) {
        this.regIP = regIP;
    }

    /**
	 * @return the postNum
	 */
    public int getPostNum() {
        return postNum;
    }

    /**
	 * @param postNum the postNum to set
	 */
    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }

    /**
	 * @return the commNum
	 */
    public int getCommNum() {
        return commNum;
    }

    /**
	 * @param commNum the commNum to set
	 */
    public void setCommNum(int commNum) {
        this.commNum = commNum;
    }

    /**
	 * @return the lastVisitTime
	 */
    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    /**
	 * @param lastVisitTime the lastVisitTime to set
	 */
    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    /**
	 * @return the lastVisitIp
	 */
    public String getLastVisitIp() {
        return lastVisitIp;
    }

    /**
	 * @param lastVisitIp the lastVisitIp to set
	 */
    public void setLastVisitIp(String lastVisitIp) {
        this.lastVisitIp = lastVisitIp;
    }

    /**
	 * @return the posts
	 */
    public Set getPosts() {
        return posts;
    }

    /**
	 * @param posts the posts to set
	 */
    public void setPosts(Set posts) {
        this.posts = posts;
    }
}
