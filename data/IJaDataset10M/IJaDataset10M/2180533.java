package issrg.pba;

import java.util.Vector;

/**
 * This interface represents a implementation-independent Delegatable 
 * Authorisation Token.
 * In default PERMIS RBAC the implementation-specific AuthZ Tokens are X.509
 * Attribute Certificates. 
 */
public interface DelegatableToken extends ParsedToken {

    /**
   * This method extracts the delegatable Credentials from the object
   * representing the Authorisation Token.
   *
   * <p>The result is never null.
   *
   * @return Credentials is the delegatable Credentials that the Authorisation Token contains
   */
    public Credentials getDelegateableCredentials();

    /**
   * This method returns a domain of subjects to whom the holder of the token
   * can delegate the Delegateable Credentials to.
   */
    public issrg.pba.rbac.policies.Subtree getSubjectDomain();

    /**
   * This method returns the depth of delegation chain allowed for the holder
   * of this token. "0" means one level down, "1" means two leves down... "-1" means infinity.
   */
    public int getDepth();
}
