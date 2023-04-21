package webservicesapi.data.db.model.address;

import webservicesapi.data.db.model.ModelObject;

/**
 * Represents an address book entry
 */
public class Contact implements ModelObject {

    private String name;

    private String email;

    private String phone;

    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
