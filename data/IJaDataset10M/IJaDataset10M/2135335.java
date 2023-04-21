package com.ibm.webdav;

import java.util.Vector;
import java.util.Enumeration;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.IOException;
import java.io.StreamTokenizer;

/** A Precondition represents some condition or collection of conditions
 * representing states of a resource. If the state of the resource does not
 * match any of the specified states in any of the conditions, then the
 * method must fail. Conditions in a Precondition are OR'd together while
 * ConditionFactors in a ConditionTerm are AND'ed.
 * @author Jim Amsden &lt;jamsden@us.ibm.com&gt;
 * @see com.ibm.webdav.Precondition
 * @see com.ibm.webdav.ConditionTerm
 * @see com.ibm.webdav.ConditionFactor
 * @see com.ibm.webdav.EntityTag
 * @see com.ibm.webdav.StateToken
 */
public class Precondition {

    private Vector conditions = new Vector();

    /** Construct an empty Precondition. The client must add Conditions.
	*/
    public Precondition() {
    }

    /** Construct a Precondition by parsing the given If header as defined by
	* section 8.4 in the WebDAV spec.
	* @param ifHeader the contents of a WebDAV If header
	*/
    public Precondition(String ifHeader) throws WebDAVException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(ifHeader));
        tokenizer.wordChars('!', '/');
        tokenizer.wordChars(':', '@');
        tokenizer.wordChars('[', '_');
        tokenizer.ordinaryChar('(');
        tokenizer.ordinaryChar(')');
        tokenizer.ordinaryChar('<');
        tokenizer.ordinaryChar('>');
        tokenizer.ordinaryChar('[');
        tokenizer.ordinaryChar(']');
        tokenizer.quoteChar('"');
        int token = 0;
        try {
            token = tokenizer.nextToken();
            switch(token) {
                case '<':
                    {
                        while (token == '<') {
                            addCondition(Condition.create(tokenizer));
                            token = tokenizer.ttype;
                        }
                        break;
                    }
                case '(':
                    {
                        while (token == '(') {
                            addCondition(Condition.create(tokenizer));
                            token = tokenizer.ttype;
                        }
                        break;
                    }
            }
            if (token != StreamTokenizer.TT_EOF) {
                throw new WebDAVException(WebDAVStatus.SC_BAD_REQUEST, "Error parsing If header: saw: " + (char) token + " expected: EOF");
            }
            if (!getConditions().hasMoreElements()) {
                throw new WebDAVException(WebDAVStatus.SC_BAD_REQUEST, "Syntax error in If header: list is empty: " + ifHeader);
            }
        } catch (IOException exc) {
        }
    }

    /** Add a Condition to this Precondition. Conditions are OR'd together to
 * check for a matching resource.
 * @param condition the Condition to add
 * @exception com.ibm.webdav.WebDAVException thrown if the precondition already contains this condition
*/
    public void addCondition(Condition condition) throws WebDAVException {
        Enumeration conditions = getConditions();
        if (condition.getResourceURI() != null) {
            while (conditions.hasMoreElements()) {
                Condition existingCondition = (Condition) conditions.nextElement();
                if (existingCondition.getResourceURI() != null && existingCondition.getResourceURI().equals(condition.getResourceURI())) {
                    throw new WebDAVException(WebDAVStatus.SC_BAD_REQUEST, condition.getResourceURI() + " cannot be specified more than once in an If header");
                }
            }
        }
        this.conditions.addElement(condition);
    }

    /** Add a condition created from the given URI and state token. This is a
 * convenience method used primarily to create preconditions for lock tokens
 * that must be provided in the resource context for methods that update
 * the resource.
 *
 * @param resourceURI the URI of the resource the state token applies to. Null
 *     implicitly specifies the resource processing the request
 * @param stateToken the state token to match
 */
    public void addStateTokenCondition(String resourceURI, String stateToken) throws WebDAVException {
        Condition condition = new Condition(resourceURI);
        ConditionTerm term = new ConditionTerm();
        term.addConditionFactor(new StateToken(stateToken));
        condition.addConditionTerm(term);
        addCondition(condition);
    }

    /** Construct a Precondition by parsing the given If header as defined by
 * section 9.4 in the WebDAV spec.
 * @param ifHeader the contents of a WebDAV If header
 * @return the parser If header
 * @exception com.ibm.webdav.WebDAVException thrown if there is a syntax error in the If header
 */
    public static Precondition create(String ifHeader) throws WebDAVException {
        return new Precondition(ifHeader);
    }

    /** Get the Conditions contained in this Precondition. At least one must match
 * in order for a valid match to occur.
 * @return an Enumeration of Conditions
 */
    public Enumeration getConditions() {
        return conditions.elements();
    }

    /** See if this Precondition contains a matching Condition.
 * @param condition the condition to match
 * @return true if this precondition contains atleast one condition matching the given condition
 */
    public boolean matches(Condition condition) {
        boolean match = false;
        Enumeration conditions = getConditions();
        while (!match && conditions.hasMoreElements()) {
            Condition existingCondition = (Condition) conditions.nextElement();
            match = existingCondition.matches(condition);
        }
        return match;
    }

    /** Return a String representation of this Precondition as defined by section 9.4
 * of the WebDAV Spec. The string is the value of an If header.
 * @return a string representation of this precondition
 */
    public String toString() {
        StringWriter os = new StringWriter();
        Enumeration conditions = getConditions();
        while (conditions.hasMoreElements()) {
            Condition condition = (Condition) conditions.nextElement();
            os.write(condition.toString());
            if (conditions.hasMoreElements()) {
                os.write(' ');
            }
        }
        try {
            os.close();
        } catch (Exception exc) {
        }
        return os.toString();
    }
}
