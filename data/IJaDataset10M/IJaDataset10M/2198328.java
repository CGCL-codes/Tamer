package issrg.pba.rbac.policies;

import issrg.pba.Credentials;
import issrg.pba.DelegatableToken;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import issrg.utils.repository.Entry;

/**
 * This class represents an assignment (and delegation) rule. It knows the
 * Subject Domain to which it is applied, the delegation depth and the
 * credentials (as obtained from the Policy) that can be assigned according to
 * this rule.
 *
 * @author A Otenko
 * @version 1.0
 */
public class AssignmentRule {

    /**
   * This is the Subject Domain that this SOA is allowed to assign to,
   * according to this rule.
   */
    private Subtree subjectDomain;

    /**
   * This is how deep the delegation path can be, according to this rule. "-1" 
   * means unlimited delegation. Other integers should be non-negative
   * and reflect the actual depth of delegation.
   */
    private int delegation;

    /**
   * This is the set of credentials it can assign, according to this rule.
   */
    private issrg.pba.Credentials creds;

    protected Logger log = Logger.getLogger("credentialAllocation");

    protected AssignmentRule() {
    }

    /**
   * This is the constructor the PolicyParser should use.
   *
   * @param subjectDomain is the domain of subjects that the given set of
   *    credentials can be assigned to
   * @param delegationPathLength is an integer number specifying how deep the
   *    delegation can be; set to a negative number for unlimited delegation
   * @param SOACreds is the credentials that can be assigned to a subject from
   *    the given domain (subjectDomain)
   */
    public AssignmentRule(Subtree subjectDomain, int delegationPathLength, issrg.pba.Credentials SOACreds) {
        this.subjectDomain = subjectDomain;
        delegation = delegationPathLength;
        creds = SOACreds;
    }

    /**
   * @return the Subtree of Subjects to which this rule applies
   */
    public Subtree getSubjectDomain() {
        return subjectDomain;
    }

    /**
   * @return delegation depth of the chain that starts at the Subjects
   *   (i.e.&nbsp;this rule allows some issuer to assign privileges to some
   *   Subjects; delegation depth tells how far those Subjects can delegate
   *   their privileges further: "0" - they can't; "1" - they can, but those to 
   *   whom
   *   they delegated can't; ... "-1" - no limit on delegation is placed by 
   *   this rule)
   */
    public int getDelegationDepth() {
        return delegation;
    }

    /**
   * @return the Credentials that can be assigned to the Subjects of this rule
   */
    public issrg.pba.Credentials getCredentials() {
        return creds;
    }

    /**
   * This method determines the credential set that can be delegated to the 
   * given
   * user, delegation depth distant from this SOA/AA out of the set of assumed
   * credentials.
   *
   * <p>If assignment is not allowed then it returns null, as if the 
   * intersection
   * of credentials is null.
   *
   * @param him is the user's Entry that must match one of the allowed Subject 
   *    Domains
   * @param assumedCreds is the set of assumed credentials - the set, extracted
   *    from an Authorisation Token
   *
   * @return the Credential that the holder may legitimately be assigned by the
   *    SOA; in fact, it is the intersection of the SOA's credentials and the
   *    assumed credentials; can be null, if no Credentials can be assigned
   */
    public issrg.pba.Credentials allocate(Entry him, issrg.pba.Credentials assumedCreds) {
        if (subjectDomain.contains(him)) {
            Credentials cr = creds.intersection(assumedCreds);
            if (log.isLoggable(Level.FINE)) log.fine("allocating " + cr + " - a subset of " + assumedCreds + " to " + him.getEntryName().getName());
            return cr;
        }
        if (log.isLoggable(Level.FINE)) log.fine("cannot assign " + assumedCreds + " to " + him.getEntryName().getName() + ": subject domain " + subjectDomain + " did not match");
        return null;
    }

    /**
   * This method validates what Credentials and Assignment rules can be assigned.
   * First it calls allocate on the Credentials from the token, to find out the 
   * set of assertable credentials. 
   * Then, if the Token is a DelegatableToken, it calls allocate on the 
   * Delegatable Credentials from the token, and computes the constraints on the
   * delegation depth and subject domain.
   * 
   * @param token - the token with the credentials to be assigned to the holder
   *
   * @return issrg.pba.rbac.SubjectCredsRules containing the set of assertable
   *    credentials and the assignment rules
   */
    public issrg.pba.rbac.SubjectCredsRules assign(issrg.pba.ParsedToken token) {
        issrg.pba.Credentials assertable = allocate(token.getHolder(), token.getCredentials());
        java.util.Vector rules = new java.util.Vector();
        if (token instanceof DelegatableToken && delegation != 0) {
            DelegatableToken delTok = (DelegatableToken) token;
            rules = allocate(delTok.getHolder(), new AssignmentRule(delTok.getSubjectDomain(), delTok.getDepth(), delTok.getDelegateableCredentials()));
        }
        return new issrg.pba.rbac.SubjectCredsRules(assertable, rules);
    }

    /**
   * This method determines the set of RARs that can be delegated to the 
   * given
   * user, delegation depth distant from this SOA/AA out of the set of assumed
   * RARs.
   *
   * <p>If assignment is not allowed then it returns an empty set of RARs.
   *
   * @param holder is the user's Entry that must match one of the allowed 
   *    Subject 
   *    Domains
   * @param ar is the assumed RAR - as built from an Authorisation Token
   *
   * @return the Vector of RARs that the holder may legitimately be assigned by 
   *    the
   *    SOA; in fact, it is the intersection of the SOA's RARs and the
   *    assumed RAR; each element in the Vector is an AssignmentRule
   */
    public Vector allocate(Entry holder, AssignmentRule ar) {
        Credentials assign = ar.getCredentials();
        Subtree subjDomain = ar.getSubjectDomain();
        int depth = ar.getDelegationDepth();
        Vector rules = new Vector();
        if ((delegation != 0) && (subjectDomain.contains(holder))) {
            if (log.isLoggable(Level.FINE)) log.fine("computing delegatable creds for " + holder.getEntryName().getName() + " from " + ar);
            if (delegation > 0 && (delegation <= depth || depth < 0)) depth = delegation - 1;
            issrg.pba.Credentials assignable = allocate(holder, assign);
            rules.add(new AssignmentRule(new IntersectionSubtree(subjectDomain, subjDomain), depth, assignable));
            if (log.isLoggable(Level.FINE)) log.fine("added a rule for " + holder.getEntryName().getName() + " from " + ar + ": " + rules);
        } else {
            if (log.isLoggable(Level.FINE)) log.fine("cannot assign " + ar + " to " + holder.getEntryName().getName() + ": " + (delegation == 0 ? "this RAR does not allow delegation" : ("subject domain mismatch " + subjectDomain)));
        }
        return rules;
    }

    public String toString() {
        return "RAR to allocate " + creds + " to " + subjectDomain + " with delegation depth " + delegation;
    }
}
