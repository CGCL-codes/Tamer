package tudresden.ocl20.pivot.ocl2parser.testcasegenerator.util;

import java.util.ArrayList;
import java.util.List;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.abstractsyntax.IListElement;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.abstractsyntax.IModelExpression;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.abstractsyntax.IVariable;

public class BuildingCodeUtilClass {

    private static BuildingCodeUtilClass instance;

    private BuildingCodeUtilClass() {
    }

    public static BuildingCodeUtilClass getDefault() {
        if (instance == null) {
            instance = new BuildingCodeUtilClass();
            return instance;
        }
        return instance;
    }

    /**
	 * This method transforms a path name, where the elements are separted by '::',
	 * into a list of single elements without '::'. 
	 * @param pathName the string of a path name with the delmiter '::'
	 * @return a list of the elements without '::'
	 */
    public static List<String> transformPathName2List(String pathName) {
        java.util.List elementList = new ArrayList();
        String delimiter = "::";
        int index = 0;
        int length = pathName.length();
        int lastPosition = 0;
        while (index < length) {
            index = pathName.indexOf(delimiter, lastPosition);
            if (index == -1) {
                String subString = pathName.substring(lastPosition, length);
                elementList.add(subString);
                break;
            }
            String subString = pathName.substring(lastPosition, index);
            elementList.add(subString);
            lastPosition = index + delimiter.length();
        }
        return elementList;
    }

    /**
	 * Takes a list of strings and produce a string with commas in between.
	 * For example: Suppose we have the list [a,b,c] then a string "a,b,c" will
	 * be constructed. If <i>elements</i> is null or it has zero elements
	 * an empty string will be returned.
	 * @param elements the list from which to construct a string
	 * @return the string with commas
	 */
    public static String concatElements(List<String> elements) {
        String result = new String();
        if (elements == null) return result;
        if (elements.size() == 0) return result;
        result = elements.get(0);
        for (int i = 1; i < elements.size(); i++) {
            result = result + ", " + elements.get(i);
        }
        return result;
    }

    /**
	 * Takes a list of strings and produce a string with double colons in between.
	 * For example: Suppose we have the list [a,b,c] then a string "a::b::c" will
	 * be constructed. If <i>elements</i> is null or it has zero elements
	 * an empty string will be returned.
	 * @param elements the list from which to construct a string
	 * @return the string with commas
	 */
    public static String concatElementsWithDblColon(List<String> elements) {
        String result = new String();
        if (elements == null) return result;
        if (elements.size() == 0) return result;
        result = elements.get(0);
        for (int i = 1; i < elements.size(); i++) {
            result = result + ":: " + elements.get(i);
        }
        return result;
    }

    /**
	 * Takes a list of string buffers and produce a string with commas in between.
	 * For example: Suppose we have the list [a,b,c] then a string "a,b,c" will
	 * be constructed. If <i>elements</i> is null or it has zero elements
	 * an empty string will be returned.
	 * @param elements the list from which to construct a string
	 * @return the string with commas
	 */
    public static String concatElementsOfStringBufferList(List<StringBuffer> elements) {
        String result = new String();
        if (elements == null) return result;
        if (elements.size() == 0) return result;
        List<String> list = new ArrayList<String>();
        for (StringBuffer buf : elements) {
            list.add(buf.toString());
        }
        return concatElements(list);
    }

    /**
	 * This method returns the last element of a variable chain.
	 * @param a variable
	 * @return the last element of a variable chain
	 */
    public static IModelExpression transferLastElementOfVariableChainList(IVariable exp) {
        IModelExpression runVar = null;
        if (exp instanceof IVariable) {
            runVar = ((IVariable) exp).getReference();
        } else return exp;
        while (true) {
            if (runVar instanceof IVariable) {
                IModelExpression ref = ((IVariable) runVar).getReference();
                runVar = ref;
                continue;
            } else return runVar;
        }
    }

    /**
	 * This method returns the last variable of a variable chain.
	 * @param a variable
	 * @return the last variable of a variable chain
	 */
    public static IVariable transferLastVariableOfVariableChainList(IVariable exp) {
        IVariable runVar = null;
        if (!(exp.getReference() instanceof IVariable)) return exp;
        runVar = (IVariable) exp.getReference();
        while (true) {
            if (!(runVar.getReference() instanceof IVariable)) return runVar;
            runVar = (IVariable) runVar.getReference();
        }
    }

    /**
	 * This method returns always a list or null. A list is returned if
	 * <i>exp</i> is of type {@link IListElement} or of type {@link IVariable}
	 * if the last element is list. In the other cases the method will return null.
	 * @param a {@link IModelExpression}
	 * @return a list element if <i>exp</i> is of type {@link IListElement} or of type {@link IVariable}
	 * with its last element of type {@link IListElement}.
	 */
    public static IListElement transferList(IModelExpression exp) {
        IModelExpression runVar = null;
        if (exp instanceof IVariable) {
            runVar = ((IVariable) exp).getReference();
        } else if (exp instanceof IListElement) {
            return (IListElement) exp;
        } else return null;
        while (true) {
            if (runVar instanceof IVariable) {
                IModelExpression ref = ((IVariable) runVar).getReference();
                runVar = ref;
                continue;
            }
            if (runVar instanceof IListElement) return (IListElement) runVar;
            return null;
        }
    }

    /**
	 * Deletes all occurrences of <code>deleteChar</code> in <code>string</code>. For example:
	 * Suppose we have the string 'abcb' and the call of this method is deleteChar('abc','b') the
	 * result will be 'ac'.
	 * If <code>deleteChar</i> is not part of <code>string</code> the original string will be returned.
	 * If <code>string</code> is null, an empty string will be returned.
	 * @param string the string from which the character should deleted
	 * @param deleteChar the character that will be removed
	 * @return the string removed by <code>deleteChar</code>
	 */
    public static String deleteChar(String string, char deleteChar) {
        if (string == null) return new String();
        StringBuffer stringCopy = new StringBuffer(string);
        String deleteCharacter = Character.toString(deleteChar);
        int indexChar = stringCopy.indexOf(deleteCharacter);
        while (indexChar >= 0) {
            stringCopy.deleteCharAt(indexChar);
            indexChar = stringCopy.indexOf(deleteCharacter);
        }
        return stringCopy.toString();
    }

    /**
	 * Gets a string with implicit new lines and makes a string with explicit new lines.
	 * For example. We have the following string:
	 * 
	 * Hallo
	 * Test
	 * 
	 * The method transforms this string into:
	 * "Hallo"
	 * +"Test"
	 * 
	 * If <code>naturalString</code> is null, the method returns an empty string.
	 * 
	 * @param naturalString String to be transformed
	 * @return transformed string
	 */
    public static String transformStringToExplicitNewLines(String naturalString) {
        if (naturalString == null) return new String();
        StringBuffer buf = new StringBuffer();
        buf.append("\"");
        for (int i = 0; i < naturalString.length() - 1; i++) {
            char charAtPosition = naturalString.charAt(i);
            if (charAtPosition == '\n') {
                buf.append("\"\r\n+\"");
            } else {
                buf.append(charAtPosition);
            }
        }
        buf.append(naturalString.charAt(naturalString.length() - 1) + "\"");
        return buf.toString();
    }
}
