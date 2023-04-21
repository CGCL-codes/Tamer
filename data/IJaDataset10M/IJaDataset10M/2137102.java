package edu.rice.cs.drjava.model.definitions.indent;

import javax.swing.text.*;
import edu.rice.cs.util.UnexpectedException;
import edu.rice.cs.drjava.model.AbstractDJDocument;

/** Question rule in the indentation decision tree.  Determines if the 
  * line previous to the current position starts with the specified character.
  * @version $Id: QuestionPrevLineStartsJavaDocWithText.java 5175 2010-01-20 08:46:32Z mgricken $
  */
public class QuestionPrevLineStartsJavaDocWithText extends IndentRuleQuestion {

    /** Constructs a rule that looks for "/**" as first non-whitespace followed by some additional non-whitespace text
    * @param yesRule Rule to use if this rule holds
    * @param noRule Rule to use if this rule does not hold
    */
    public QuestionPrevLineStartsJavaDocWithText(IndentRule yesRule, IndentRule noRule) {
        super(yesRule, noRule);
    }

    /** Determines if the previous line starts with "/**", ignoring whitespace, followed by more non-whitespace text
    * @param doc AbstractDJDocument containing the line to be indented.
    * @return true if this node's rule holds.
    */
    boolean applyRule(AbstractDJDocument doc, Indenter.IndentReason reason) {
        try {
            int here = doc.getCurrentLocation();
            int startLine = doc._getLineStartPos(here);
            if (startLine <= 0) return false;
            int endPrevLine = startLine - 1;
            int startPrevLine = doc._getLineStartPos(endPrevLine);
            int firstChar = doc._getLineFirstCharPos(startPrevLine);
            String actualPrefix = doc.getText(firstChar, 3);
            if (!actualPrefix.equals("/**")) return false;
            int nextNonWSChar = doc.getFirstNonWSCharPos(firstChar + 3, true);
            return nextNonWSChar != -1 && nextNonWSChar <= endPrevLine;
        } catch (BadLocationException e) {
            throw new UnexpectedException(e);
        }
    }
}
