package cn.com.believer.songyuanframework.openapi.storage.xdrive.object.core;

import net.sf.json.JSONObject;

/**
 * @author Jimmy
 * 
 */
public class ContactObject implements IXDriveObject {

    /** email adress. */
    private String email;

    /** first name. */
    private String firstname;

    /** id. */
    private String id;

    /** last name. */
    private String lastname;

    /** first name. */
    private String nickname;

    /**
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return this.firstname;
    }

    /**
     * @param firstname
     *            the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return this.lastname;
    }

    /**
     * @param lastname
     *            the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * @param nickname
     *            the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 
     * @return JSON string.
     */
    public String toJSONString() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("type", "ContactObject");
        jsonObj.put("email", this.email);
        jsonObj.put("firstname", this.firstname);
        jsonObj.put("id", this.id);
        jsonObj.put("lastname", this.lastname);
        jsonObj.put("nickname", this.nickname);
        return jsonObj.toString();
    }
}
