package model;

public class Admin {

    private String name;

    private String password;

    /**
     * initialize admin constructor
     * @param name the admin's name
     * @param password the admin's password
     */
    public Admin(String name, String password) {
        this.name = name;
        this.password = password;
    }

    /**
     * retrieve the name of the admin
     * @return the name of the admin
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the admin
     * @param name the name of the admin
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve the password of the admin
     * @return the password of the admin
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the admin
     * @param password the password of the admin
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
