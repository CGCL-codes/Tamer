package org.dcmn8.client;

public class User {

    private String nick;

    private String description;

    private String speed;

    private String email;

    private String shareSize;

    public User(String nick) {
        this.nick = nick;
    }

    /**
     * @return Returns the nick.
     */
    public String getNick() {
        return nick;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description to set.
     */
    public void setDescription(String tag) {
        this.description = tag;
    }

    /**
     * @return Returns the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return Returns the shareSize.
     */
    public String getShareSize() {
        return shareSize;
    }

    /**
     * @param shareSize The shareSize to set.
     */
    public void setShareSize(String shareSize) {
        this.shareSize = shareSize;
    }

    /**
     * @return Returns the speed.
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * @param speed The speed to set.
     */
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    public boolean equals(Object obj) {
        if (obj instanceof User) return getNick() != null ? getNick().equals(((User) obj).getNick()) : ((User) obj).getNick() == null;
        return false;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    public int hashCode() {
        return getNick() != null ? getNick().hashCode() : 0;
    }
}
