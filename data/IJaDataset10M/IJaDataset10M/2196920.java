package nz.org.venice.parser.errcorrect;

import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.util.StringTokenizer;
import nz.org.venice.parser.Token;
import nz.org.venice.parser.ParserException;
import nz.org.venice.util.VeniceLog;

public class PTEntry implements Cloneable {

    final Vector data;

    Vector corrections;

    int numCorrections;

    boolean correctionsKnown;

    Rule rulePart;

    VeniceLog veniceLog;

    /**
     * Construct a new empty cell in a partition table.
     */
    public PTEntry() {
        data = new Vector();
        corrections = new Vector();
        correctionsKnown = false;
        rulePart = null;
        veniceLog = VeniceLog.getInstance();
    }

    public PTEntry(Vector input) {
        data = (Vector) input.clone();
        corrections = new Vector();
        correctionsKnown = false;
        rulePart = null;
        veniceLog = VeniceLog.getInstance();
    }

    /**
     * Remove the token at the head of the input and return in. 
     * 
     * @return The first token of the input
     */
    public Token pop() {
        Token rv;
        rv = (Token) data.get(0);
        data.remove(0);
        return rv;
    }

    /**
     * @return the number of tokens in the input.
     */
    public int size() {
        return data.size();
    }

    /**
     * @return The list of input tokens
     */
    public List getData() {
        return data;
    }

    /**
     * Remove the last token in the input.
     */
    public void chop() {
        data.remove(data.size() - 1);
    }

    /**
     * Place a token onto the end of the data list
     * @param token The token to add
     */
    public void push(Token token) {
        data.add(token);
    }

    /**
     * Place token on the head the data list
     */
    public void reput(Token token) {
        data.insertElementAt(token, 0);
    }

    /**
     * @return a string representing the input tokens
     */
    public String toString() {
        String rv = "";
        Iterator it = data.iterator();
        while (it.hasNext()) {
            Token t = (Token) it.next();
            rv += getWord(t);
        }
        if (rv == "") {
            rv = "e";
        }
        return rv;
    }

    /** 
     * @return an iterator for the input tokens
     *
     */
    public Iterator iterator() {
        Iterator rv = data.iterator();
        return rv;
    }

    public int compareTo(PTEntry e) {
        Iterator it1, it2;
        Token t1, t2;
        if (e.size() != data.size()) {
            return -1;
        }
        it1 = data.iterator();
        it2 = e.iterator();
        while (it1.hasNext()) {
            t1 = (Token) it1.next();
            t2 = (Token) it2.next();
            if (t1.getType() != t2.getType()) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Return true if the input at e is at the same semantic level
     * false otherwise. (e.g. x+y === x-y, x*y =/= x+y)
     * @param e The entry to compare inputs with
     * @return true if the input at e is semantically the same as this entry
     */
    public boolean compareInput(PTEntry e) {
        Iterator it1, it2;
        Token t1, t2;
        boolean rv = false;
        if (e.size() != data.size()) {
            return false;
        }
        it1 = data.iterator();
        it2 = e.iterator();
        while (it1.hasNext()) {
            t1 = (Token) it1.next();
            t2 = (Token) it2.next();
            boolean equivalent = Grammar.getInstance().equivalent(t1, t2);
            if (!equivalent) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return a new object which is a copy of this object
     */
    public Object clone() {
        Vector data2 = (Vector) data.clone();
        Vector corrections2 = (Vector) corrections.clone();
        PTEntry ne = new PTEntry(data2);
        ne.setNumCorrections(numCorrections);
        ne.setCorrectionsKnown(correctionsKnown);
        ne.setCorrections(corrections2);
        ne.setRule(rulePart);
        return ne;
    }

    /**
     * @return a string which is the name of a token. Where the token has a
     * variable value like a string or number, return the relevant word.
     */
    public String getWord(Token t) {
        int type;
        String rv;
        String[] words = Token.wordsOfGondola();
        type = t.getType();
        if (type == Token.NUMBER_TOKEN) {
            rv = "num";
        } else if (type == Token.STRING_TOKEN) {
            rv = "string";
        } else if (type == Token.VARIABLE_TOKEN) {
            rv = "varname";
        } else {
            assert type <= words.length;
            rv = words[type];
        }
        return rv;
    }

    /**
     * Set the number of corrections and also set correctionsKnown to true.
     * @param num The number of corrections.
     */
    public void setNumCorrections(int num) {
        correctionsKnown = true;
        numCorrections = num;
    }

    /**
     * Return the number of corrections.
     * Precondition: correctionsKnown must be true. 
     */
    public int getNumCorrections() {
        assert correctionsKnown == true;
        return numCorrections;
    }

    /**
     * Flag that the number of corrections is known. 
     * 
     * @param correctionsKnown a boolean flag indicating the corrections are known.
     */
    public void setCorrectionsKnown(boolean correctionsKnown) {
        this.correctionsKnown = correctionsKnown;
    }

    /**
     * @return the flag indicating the number of corrections is known.
     */
    public boolean getCorrectionsKnown() {
        return correctionsKnown;
    }

    /**
     * set the rule part of this entry to a given ruole.
     * @param rule The rule to set.
     *
     * Precondition: rulePart has not already been set. 
     */
    public void setRule(Rule rule) {
        assert rulePart == null;
        rulePart = rule;
    }

    /**
     * @return the rule part of this entry.
     */
    public Rule getRule() {
        return rulePart;
    }

    /**
     * @return true if the rule part of the entry is terminal.
     * 
     * Precondition: rulePart has to be set.
     */
    public boolean isTerminal() {
        assert rulePart != null;
        return rulePart.isTerminal();
    }

    /**
     * @return a list of the corrections.
     */
    public List getCorrections() {
        return corrections;
    }

    public void setCorrections(List corrections) {
        this.corrections = (Vector) corrections;
    }

    public static Vector stringToTokenList(String s) {
        Vector rv = new Vector();
        try {
            for (int i = 0; i < s.length(); i++) {
                String sub = s.substring(i, i + 1);
                Token t = new Token();
                Token.stringToToken(null, t, sub);
                rv.add(t);
            }
        } catch (ParserException e) {
            System.out.println("WTF? : " + e);
            assert false;
        }
        return rv;
    }

    /**
     * Print out a human friendly list of tokens to the standard input.
     * 
     * @param tokens A list of tokens
     */
    public static void dumpTokens(List tokens) {
        String[] words = Token.wordsOfGondola();
        System.out.println("Dumping: ");
        String rv = "";
        Iterator iterator = tokens.iterator();
        while (iterator.hasNext()) {
            Token t = (Token) iterator.next();
            rv += words[t.getType()];
        }
        System.out.println(rv);
    }

    /**
     * Return a human friendly string representation of a token
     * 
     * @param t A token
     */
    public static String printToken(Token t) {
        String[] words = Token.wordsOfGondola();
        String word = "";
        switch(t.getType()) {
            case Token.NUMBER_TOKEN:
                word = "num_const";
                break;
            case Token.VARIABLE_TOKEN:
                word = "var";
                break;
            case Token.STRING_TOKEN:
                word = "string";
                break;
            default:
                if (t.getType() <= 63) {
                    word = words[t.getType()];
                } else {
                    assert false;
                }
        }
        return word;
    }

    /**
     * Return a human friendly representation of a list of tokens
     * 
     * @param tokens a List of tokens
     * @return a string representing the words of the tokens
     */
    public static String printTokens(List tokens) {
        String rv = "";
        Iterator iterator = tokens.iterator();
        while (iterator.hasNext()) {
            Token t = (Token) iterator.next();
            String word = printToken(t);
            rv += word;
        }
        return rv;
    }

    /**
     * Calculate the score of the set of corrections and return.
     * 
     * @return the cumulative score of the corrections in the entry.
     */
    public int getScore() {
        assert correctionsKnown == true;
        int total = 0;
        Iterator iterator = corrections.iterator();
        while (iterator.hasNext()) {
            Correction c = (Correction) iterator.next();
            total += c.score();
        }
        return total;
    }

    /**
     * Return a list of the input with the corrections applied.
     *
     * @return a list of tokens after corrections have been applied.
     *
     * Precondition: Corrections is empty iff no corrections are required.
     */
    public List applyCorrections() {
        assert correctionsKnown == true;
        List correctedSentence = (List) data.clone();
        Iterator iterator = corrections.iterator();
        int index;
        int delIndex = 0;
        int i = 0;
        while (iterator.hasNext()) {
            Correction c = (Correction) iterator.next();
            Token t = (data.size() > 0) ? ((Token) data.get(c.getPosition())) : null;
            String word = (t != null) ? printToken(t) : "e";
            assert c.getPosition() >= 0;
            switch(c.getType()) {
                case Correction.NOOP:
                    break;
                case Correction.DELETE:
                    index = c.getPosition() - delIndex;
                    assert index >= 0;
                    correctedSentence.remove(c.getPosition() - delIndex);
                    delIndex++;
                    break;
                case Correction.ADD:
                    correctedSentence.add(c.getToken());
                    delIndex--;
                    break;
                case Correction.SUBSTITUTE:
                    index = c.getPosition() - delIndex;
                    assert index >= 0;
                    correctedSentence.set(index, c.getToken());
                    break;
                default:
                    assert false;
            }
            i++;
        }
        return correctedSentence;
    }
}
