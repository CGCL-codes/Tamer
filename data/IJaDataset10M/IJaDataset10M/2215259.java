package alphahr.types;

/**
 * @version     0.00 07 Mar 2011
 * @author      Andrey Pudov
 */
public class WebAddress {

    public static final int EMAIL = 0;

    public static final int WEB = 1;

    private final String address;

    private final int type;

    public WebAddress(String address, int type) {
        this.address = address;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public int getType() {
        return type;
    }
}
