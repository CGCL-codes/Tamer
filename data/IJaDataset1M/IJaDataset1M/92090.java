package jimm.twice.ice.xml;

/**
 * A get-package request.
 *
 * @author Jim Menard, <a href="mailto:jimm@io.com">jimm@io.com</a>
 */
public class GetPackage extends Message {

    /**
 * Constructor. If <var>subscriptionId</var> is the string &quot;1&quot;
 * then this is a request for a list of offers (a catalog).
 *
 * @param subscriptionId subscription id as a string; if &quot;1&quot;
 * then this is a request for a list of offers (a catalog)
 */
    public GetPackage(String subscriptionId) {
        this(subscriptionId, null);
    }

    /**
 * Constructor. If <var>subscriptionId</var> is the string &quot;1&quot;
 * then this is a request for a catalog (a list of offers).
 *
 * @param subscriptionId subscription id as a string; if &quot;1&quot;
 * then this is a request for a list of offers (a catalog)
 * @param state current state; may be <code>null</code>
 */
    public GetPackage(String subscriptionId, String state) {
        super("icedel:get-package");
        setAttribute("subscription-id", subscriptionId);
        if (state != null) setCurrentState(state);
    }

    public void setCurrentState(String state) {
        setAttribute("current-state", state);
    }
}
