package hotel.model;

/**
 * 
 * 类说明
 * 
 * @author lipeiying
 * @version 创建时间：2011-12-31 下午03:09:13
 */
public class User {

    public static final int ADMINISTRATOR = 1;

    public static final int CUSTOMER = 0;

    private String username;

    private String password;

    private String telephone;

    private int type;

    private boolean placeOrder = false;

    public User() {
    }

    public User(String username, String password, String telephone, int type) {
        this.username = username;
        this.password = password;
        this.telephone = telephone;
        this.type = type;
    }

    public boolean isPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(boolean placeOrder) {
        this.placeOrder = placeOrder;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
