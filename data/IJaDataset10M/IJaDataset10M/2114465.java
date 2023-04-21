package jimm.twice.ice;

/**
 * ICE ice-package information is used often enough that it's nice to have
 * an objectified representation.
 *
 * @author Jim Menard, <a href="mailto:jimm@io.com">jimm@io.com</a>
 */
public class IcePackage {

    protected String identifier;

    protected String oldState;

    protected String newState;

    protected String subscriptionId;

    protected boolean confirmationRequested;

    protected boolean fullUpdate;

    /**
 * Constructor.
 *
 * @param identifier the package-id attribute value
 * @param oldState the old-state attribute value
 * @param newState the new-state attribute value
 * @param confirmationRequested <code>true</code> if the ICE package's
 * confirmation attribute is "true"
 * @param fullUpdate <code>true</code> if the ICE package's fullupdate
 * attribute is "true"
 */
    public IcePackage(String identifier, String oldState, String newState, String subscriptionId, boolean confirmationRequested, boolean fullUpdate) {
        this.identifier = identifier;
        this.oldState = oldState;
        this.newState = newState;
        this.subscriptionId = subscriptionId;
        this.confirmationRequested = confirmationRequested;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getOldState() {
        return oldState;
    }

    public String getNewState() {
        return newState;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    /**
 * Returns true if the confirmation attribute was seen within the ICE package
 * tag and if its value was "true".
 *
 * @return <code>true</code> if the ICE package's confirmation attribute
 * is "true"
 */
    public boolean isConfirmationRequested() {
        return confirmationRequested;
    }

    /**
 * Returns true if the fullupdate attribute was seen within the ICE package
 * tag and if its value was "true".
 *
 * @return <code>true</code> if the ICE package's fullupdate attribute
 * is "true"
 */
    public boolean isFullUpdate() {
        return fullUpdate;
    }
}
