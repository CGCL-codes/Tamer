package edu.rice.cs.drjava.model.definitions.indent;

import edu.rice.cs.drjava.model.AbstractDJDocument;

/** Determines if the given search character is found between the start of the current statement and the end character.
  * Accomplishes this by searching backwards from the end character, for the search character until one of the 
  * following characters is found: '}', '{', ';', 0.
  * <b>The given end character must exist on the current line and not be part of a quote or comment.</b> If there is 
  * more than end character on the given line, then the first end character is used.
  * <p>This question is useful for determining if, when a colon is found on a line, it is part of a ternary operator 
  * or not (construct this question with '?' for search character and ':' for end character).
  * <p>It can also be used to determine if a statement contains a particular character by constructing it with the 
  * desired character as a search character and the end character as ';'.
  * <p>Note that characters in comments and quotes are disregarded. 
  *
  * @version $Id: QuestionExistsCharInStmt.java 5175 2010-01-20 08:46:32Z mgricken $
  */
public class QuestionExistsCharInStmt extends IndentRuleQuestion {

    /** The character to search for
   */
    private char _findChar;

    /** The character which marks the end of the search
   * space. i.e. search from the start of the statment
   * to the end char.
   */
    private char _endChar;

    /** Constructs a rule to determine if findChar exists
   * between the start of the current statement and endChar.
   *
   * @param findChar Character to search for from the start of the
   * statement to endChar
   * @param endChar Character that marks the end of the search space. Must
   * exist on the current line and not be in quotes or comments.
   * @param yesRule Rule to use if this rule holds
   * @param noRule Rule to use if this rule does not hold
   */
    public QuestionExistsCharInStmt(char findChar, char endChar, IndentRule yesRule, IndentRule noRule) {
        super(yesRule, noRule);
        _findChar = findChar;
        _endChar = endChar;
    }

    /** Searches backwards from endChar to the start of the statement looking for findChar. Ignores characters in 
    * comments and quotes. Start of the statement is the point right after when one of the following characters 
    * is found: ';', '{', '}', 0.
    * @param doc AbstractDJDocument containing the line to be indented.
    * @return true if this node's rule holds.
    */
    boolean applyRule(AbstractDJDocument doc, Indenter.IndentReason reason) {
        int endCharPos = doc.findCharOnLine(doc.getCurrentLocation(), _endChar);
        return doc.findCharInStmtBeforePos(_findChar, endCharPos);
    }
}
