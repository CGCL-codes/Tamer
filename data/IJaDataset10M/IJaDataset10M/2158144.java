package org.mc.user;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.mc.app.RetailerViewer;
import org.mc.content.Retailer;

public class User {

    public static String ROLE_NOBODY = "NOBODY";

    public static String ROLE_CUSTOMER = "CUSTOMER";

    public static String ROLE_MANAGER = "MANAGER";

    private String name;

    private int id;

    private String role = ROLE_NOBODY;

    private int retailerId;

    private String login;

    private String pass;

    private String email;

    private Date addDate;

    private boolean locked;

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public static Map<Integer, String> dbRoleBinding = new HashMap<Integer, String>();

    static {
        dbRoleBinding.put(0, User.ROLE_NOBODY);
        dbRoleBinding.put(1, User.ROLE_CUSTOMER);
        dbRoleBinding.put(2, User.ROLE_MANAGER);
    }

    public static Map<String, Integer> dbRoleBindingReversed = new HashMap<String, Integer>();

    ;

    static {
        for (Map.Entry<Integer, String> e : dbRoleBinding.entrySet()) {
            dbRoleBindingReversed.put(e.getValue(), e.getKey());
        }
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isCustomer() {
        return role.equals(ROLE_CUSTOMER);
    }

    public boolean isManager() {
        return role.equals(ROLE_MANAGER);
    }

    public boolean isLoggedUser() {
        return role.equals(ROLE_CUSTOMER) || role.equals(ROLE_MANAGER);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setRetailerId(int retailerId) {
        this.retailerId = retailerId;
    }

    public int getRetailerId() {
        return retailerId;
    }

    public Retailer getRetailer() throws SQLException {
        RetailerViewer rv = RetailerViewer.getInstance();
        return rv.getRetailerMap().get(retailerId);
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User[id='" + id + "', login='" + login + "']";
    }
}
