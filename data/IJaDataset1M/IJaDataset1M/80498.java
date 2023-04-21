package au.edu.qut.yawl.engine;

import au.edu.qut.yawl.elements.state.YIdentifier;
import java.util.Arrays;

/**
 * 
 * @author Lachlan Aldred
 * Date: 23/05/2003
 * Time: 14:32:32
 * 
 */
public class YWorkItemID {

    private static final char[] _uniqifier = UniqueIDGenerator.newAlphas();

    private char[] _uniqueID;

    private YIdentifier _caseID;

    private String _taskID;

    public YWorkItemID(YIdentifier caseID, String taskID) {
        _uniqueID = (char[]) _uniqifier.clone();
        UniqueIDGenerator.nextInOrder(_uniqifier);
        _caseID = caseID;
        _taskID = taskID;
    }

    public String toString() {
        return _caseID.toString() + ":" + _taskID;
    }

    public YIdentifier getCaseID() {
        return _caseID;
    }

    public String getTaskID() {
        return _taskID;
    }

    public String getUniqueID() {
        return new String(_uniqueID);
    }
}

class UniqueIDGenerator {

    static char[] newAlphas() {
        char[] alphas = new char[25];
        Arrays.fill(alphas, '0');
        return alphas;
    }

    /**
     *
     * @param chars
     * @return
     */
    static char[] nextInOrder(char[] chars) {
        for (int i = chars.length - 1; i >= 0; i--) {
            char aChar = chars[i];
            if (inRange(aChar)) {
                aChar = inc(aChar);
                chars[i] = aChar;
                break;
            } else {
                chars[i] = '0';
            }
        }
        return chars;
    }

    private static char inc(char aChar) {
        if (aChar == '9') {
            return 'A';
        }
        if (aChar == 'Z') {
            return 'a';
        }
        if (aChar == 'z') {
            throw new IllegalArgumentException("Shouldn't be called with 'z'.");
        }
        char incChar = (++aChar);
        return incChar;
    }

    private static boolean inRange(char aChar) {
        boolean inRange = ((int) aChar) < ((int) 'z');
        return inRange;
    }
}
