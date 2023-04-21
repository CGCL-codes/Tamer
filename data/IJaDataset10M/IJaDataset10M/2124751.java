package totalpos;

import java.io.Serializable;

/**
 *
 * @author Saúl Hidalgo
 */
public class Client implements Serializable {

    private String id;

    private String name;

    private String address;

    private String phone;

    protected Client(String id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    protected String getAddress() {
        return address;
    }

    protected String getId() {
        return id;
    }

    protected String getName() {
        return name;
    }

    protected String getPhone() {
        return phone;
    }

    protected void setId(String id) {
        this.id = id;
    }
}
