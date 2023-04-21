package csjavacc.struct;

import csjavacc.parser.Token;

/**
 * Describes regular expressions.
 */
public abstract class RegularExpression extends Expansion {

    /**
   * The label of the regular expression (if any).  If no label is
   * present, this is set to "".
   */
    public String label = "";

    /**
   * The ordinal value assigned to the regular expression.  It is
   * used for internal processing and passing information between
   * the parser and the lexical analyzer.
   */
    public int ordinal;

    /**
   * The LHS to which the token value of the regular expression
   * is assigned.  In case there is no LHS, then the vector
   * remains empty.
   */
    public java.util.Vector lhsTokens = new java.util.Vector();

    /**
	* We now allow qualified access to token members. Store it here.
   */
    public Token rhsToken;

    /**
   * This flag is set if the regular expression has a label prefixed
   * with the # symbol - this indicates that the purpose of the regular
   * expression is solely for defining other regular expressions.
   */
    public boolean private_rexp = false;

    /**
   * If this is a top-level regular expression (nested directly
   * within a TokenProduction), then this field point to that
   * TokenProduction object.
   */
    public TokenProduction tpContext = null;

    public abstract Nfa GenerateNfa(boolean ignoreCase);

    public boolean CanMatchAnyChar() {
        return false;
    }

    /**
   * The following variable is used to maintain state information for the
   * loop determination algorithm:  It is initialized to 0, and
   * set to -1 if this node has been visited in a pre-order walk, and then
   * it is set to 1 if the pre-order walk of the whole graph from this
   * node has been traversed.  i.e., -1 indicates partially processed,
   * and 1 indicates fully processed.
   */
    public int walkStatus = 0;
}
