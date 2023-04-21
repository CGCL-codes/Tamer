package starlink.ApplicationAutomata;

/**
 *
 * @author gracep
 */
public class CallException {

    public String msgName;

    public String label;

    public String value;

    public CallException(String a, String b, String v) {
        this.msgName = a;
        this.label = b;
        this.value = v;
    }
}
