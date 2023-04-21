package org.deveel.csharpcc.parser;

import java.util.Vector;

/**
 * Describes regular expressions which are choices from
 * from among included regular expressions.
 */
public class RChoice extends RegularExpression {

    /**
	 * The list of choices of this regular expression.  Each
	 * Vector component will narrow to RegularExpression.
	 */
    public java.util.Vector choices = new java.util.Vector();

    public Nfa GenerateNfa(boolean ignoreCase) {
        CompressCharLists();
        if (choices.size() == 1) return ((RegularExpression) choices.elementAt(0)).GenerateNfa(ignoreCase);
        Nfa retVal = new Nfa();
        NfaState startState = retVal.start;
        NfaState finalState = retVal.end;
        for (int i = 0; i < choices.size(); i++) {
            Nfa temp;
            RegularExpression curRE = (RegularExpression) choices.elementAt(i);
            temp = curRE.GenerateNfa(ignoreCase);
            startState.AddMove(temp.start);
            temp.end.AddMove(finalState);
        }
        return retVal;
    }

    void CompressCharLists() {
        CompressChoices();
        RegularExpression curRE;
        RCharacterList curCharList = null;
        for (int i = 0; i < choices.size(); i++) {
            curRE = (RegularExpression) choices.elementAt(i);
            ;
            while (curRE instanceof RJustName) curRE = ((RJustName) curRE).regexpr;
            if (curRE instanceof RStringLiteral && ((RStringLiteral) curRE).image.length() == 1) choices.setElementAt(curRE = new RCharacterList(((RStringLiteral) curRE).image.charAt(0)), i);
            if (curRE instanceof RCharacterList) {
                if (((RCharacterList) curRE).negated_list) ((RCharacterList) curRE).RemoveNegation();
                Vector tmp = ((RCharacterList) curRE).descriptors;
                if (curCharList == null) choices.setElementAt(curRE = curCharList = new RCharacterList(), i); else choices.removeElementAt(i--);
                for (int j = tmp.size(); j-- > 0; ) curCharList.descriptors.addElement(tmp.elementAt(j));
            }
        }
    }

    void CompressChoices() {
        RegularExpression curRE;
        for (int i = 0; i < choices.size(); i++) {
            curRE = (RegularExpression) choices.elementAt(i);
            while (curRE instanceof RJustName) curRE = ((RJustName) curRE).regexpr;
            if (curRE instanceof RChoice) {
                choices.removeElementAt(i--);
                for (int j = ((RChoice) curRE).choices.size(); j-- > 0; ) choices.addElement(((RChoice) curRE).choices.elementAt(j));
            }
        }
    }

    public void CheckUnmatchability() {
        RegularExpression curRE;
        int numStrings = 0;
        for (int i = 0; i < choices.size(); i++) {
            if (!(curRE = (RegularExpression) choices.elementAt(i)).private_rexp && curRE.ordinal > 0 && curRE.ordinal < ordinal && LexGen.lexStates[curRE.ordinal] == LexGen.lexStates[ordinal]) {
                if (label != null) CSharpCCErrors.warning(this, "Regular Expression choice : " + curRE.label + " can never be matched as : " + label); else CSharpCCErrors.warning(this, "Regular Expression choice : " + curRE.label + " can never be matched as token of kind : " + ordinal);
            }
            if (!curRE.private_rexp && curRE instanceof RStringLiteral) numStrings++;
        }
    }
}
