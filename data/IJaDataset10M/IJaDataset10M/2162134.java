package cartago.security;

/**
 * Simple user credential: just a name.
 * 
 * @author aricci
 *
 */
public class AgentIdCredential extends AgentCredential {

    public AgentIdCredential(String userName) {
        super(userName, "");
    }

    public AgentIdCredential(String userName, String roleName) {
        super(userName, roleName);
    }
}
