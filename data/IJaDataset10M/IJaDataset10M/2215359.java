package mailreader2;

/**
 * <p> Log user out of the current session. </p>
 */
public class Logout extends MailreaderSupport {

    public String execute() {
        setUser(null);
        return SUCCESS;
    }
}
