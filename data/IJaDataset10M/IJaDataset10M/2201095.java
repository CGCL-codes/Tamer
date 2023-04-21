package it.hellonet.hellocommerce.dataobject;

import net.sourceforge.xpring.orm.Bean;

public class User extends Bean {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String name;

    private String email;

    private Number infoId;

    public void create() throws Exception {
        setId(getBeanManager().create(this));
    }

    public User read() throws Exception {
        return (User) getBeanManager().read(getId(), User.class);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Number getInfoId() {
        return infoId;
    }

    public void setInfoId(Number code) {
        this.infoId = code;
    }

    public UserInfo getInfo() throws Exception {
        UserInfo info = (UserInfo) getBeanManager().read(getInfoId(), UserInfo.class);
        if (info == null) info = new UserInfo();
        return info;
    }
}
