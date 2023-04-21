package gear.application.utils;

import java.util.Vector;
import javax.microedition.lcdui.Font;

/**
* Class containing advanced string handling utilities
* @author Stefano Driussi, Paolo Burelli
*/
public class StringHelper {

    /**
	 * String tokenize method
	 * @param tokenList string to be tokenized
	 * @param separator tokens separator
	 * @return a Vector containing the tokens
	 */
    public static Vector getTokenizedVector(String tokenList, String separator) {
        Vector tokens = new Vector();
        if (tokenList == null) return tokens;
        int commaPos = 0;
        String token = "";
        int cnt = 0;
        commaPos = tokenList.indexOf(separator);
        while (commaPos > 0) {
            commaPos = tokenList.indexOf(separator);
            if (commaPos > 0) {
                token = tokenList.substring(0, commaPos);
                tokenList = tokenList.substring(commaPos, tokenList.length());
            }
            if (!token.startsWith(separator)) tokens.addElement(token);
            while (tokenList.startsWith(separator)) {
                cnt++;
                if (cnt >= 2) tokens.addElement("");
                tokenList = tokenList.substring(1, tokenList.length());
                commaPos = tokenList.indexOf(separator);
            }
            cnt = 0;
        }
        if (commaPos < 0) {
            token = tokenList;
            tokens.addElement(token);
        }
        return tokens;
    }

    /**
	 * String tokenize method
	 * @param tokenList string to be tokenized
	 * @param separator tokens separator
	 * @return an Array containing the tokens
	 */
    public static String[] getTokenizedArray(String tokenList, String separator) {
        Vector tokens = getTokenizedVector(tokenList, separator);
        String[] st = new String[tokens.size()];
        for (int i = 0; i <= tokens.size() - 1; i++) st[i] = (String) tokens.elementAt(i);
        return st;
    }

    /**
     * String wrapping method
     * Returns an array of strings containing the lines of the wrapped text, the text is separated in different lines according to the maximum line width and the '\n' chars
     * @param text the text to be wrapped
     * @param font the font of the text (used to calculate the line width)
     * @param width max width of a the lines
     * @return array of strings containing the lines
     */
    public static String[] wrapText(String text, Font font, int width) {
        if (text == null || font == null) return null;
        if (text.startsWith("\n")) text = text.substring(1);
        Vector lines = getTokenizedVector(text, "\n");
        String currentLine, firstPart, secondPart;
        int i = 0, maxLength, splitIndex;
        while (i < lines.size()) {
            currentLine = (String) lines.elementAt(i);
            maxLength = getPrintableLength(currentLine, font, width);
            if (maxLength > 0) {
                if (currentLine.length() > 0) {
                    if (maxLength < currentLine.length() && currentLine.charAt(maxLength) != ' ') splitIndex = currentLine.lastIndexOf(' ', maxLength); else splitIndex = maxLength;
                    if (splitIndex < 0) splitIndex = maxLength;
                    if (splitIndex < currentLine.length()) {
                        firstPart = currentLine.substring(0, splitIndex);
                        if (currentLine.charAt(splitIndex) == ' ') secondPart = currentLine.substring(splitIndex + 1); else secondPart = currentLine.substring(splitIndex);
                        lines.setElementAt(firstPart, i);
                        lines.insertElementAt(secondPart, i + 1);
                    }
                }
            }
            i++;
        }
        String[] returnValue = new String[lines.size()];
        lines.copyInto(returnValue);
        return returnValue;
    }

    /**
     * Text cropping method
     * Returns an array of strings containing the cropped text lines, the text is separated in different lines according to '\n' chars and each line is cropped to the visible space
     * @param text the text to be wrapped
     * @param font the font of the text (used to calculate the line width)
     * @param width max width of a the lines
     * @return array of strings containing the lines
     */
    public static String[] cropText(String text, Font font, int width) {
        Vector lines = getTokenizedVector(text, "\n");
        String[] returnValue = new String[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            String currentLine = (String) lines.elementAt(i);
            int maxLength = StringHelper.getPrintableLength(currentLine, font, width);
            returnValue[i] = currentLine.substring(0, maxLength);
        }
        return returnValue;
    }

    /**
     * String cropping method
     * Returns an array of strings containing the cropped text lines, the text is separated in different lines according to '\n' chars and each line is cropped to the visible space
     * @param string the text to be wrapped
     * @param font the font of the text (used to calculate the line width)
     * @param width max width of a the lines
     * @return array of strings containing the lines
     */
    public static String cropString(String string, Font font, int width) {
        int maxLength = StringHelper.getPrintableLength(string, font, width);
        return string.substring(0, maxLength);
    }

    /**
	 * Returns the number of chars that can be printed in the given space
	 * @param text the original string
	 * @param font the font used to calculate the string width
	 * @param width the space available for the string
	 * @return length of the printable string
	 */
    public static int getPrintableLength(String text, Font font, int width) {
        if (font.stringWidth(text) < width) return text.length();
        int i = 0;
        while (i <= text.length() && font.stringWidth(text.substring(0, i)) < width) i++;
        return i - 2;
    }

    /**
	 * Fills the beginning of the string with a number paddingString necessary to reach the desired length 
	 * @param source the source string
	 * @param paddingString the padding chars
	 * @param length the desired length
	 * @return the padded string
	 */
    public static String padLeft(String source, String paddingString, int length) {
        String returnValue = "";
        if (source != null) returnValue = source;
        while (returnValue.length() < length) {
            returnValue = paddingString.concat(returnValue);
        }
        return returnValue.substring(returnValue.length() - length, returnValue.length());
    }

    /**
	 * Fills the end of the string with a number paddingString necessary to reach the desired length 
	 * @param source the source string
	 * @param paddingString the padding chars
	 * @param length the desired length
	 * @return the padded string
	 */
    public static String padRight(String source, String paddingString, int length) {
        String returnValue = "";
        if (source != null) returnValue = source;
        while (returnValue.length() < length) {
            returnValue = returnValue.concat(paddingString);
        }
        return returnValue.substring(0, length);
    }

    /**
	 * Returns the sub string contained between prefix and postfix
	 * @param string the source string
	 * @param prefix the sub string prefix
	 * @param postfix the sub string postfix
	 * @return the sub string
	 */
    public static String getInnerString(String string, String prefix, String postfix) {
        int prefidx = string.indexOf(prefix);
        if (prefidx == -1) return "";
        prefidx += prefix.length();
        String sub = string.substring(prefidx);
        int postidx = sub.indexOf(postfix);
        if (postidx == -1) return sub;
        return sub.substring(0, postidx);
    }

    /**
	 * Returns the integer value of the integer contained in the string
	 * @param str the string to be parsed
	 * @return the parsed integer
	 */
    public static int stringToInt(String str) {
        return Integer.parseInt(str.trim());
    }

    /**
	 * Returns the integer value of the integer contained in the string
	 * @param str the string to be parsed
	 * @return the parsed integer
	 * @deprecated use stringToInt method instead
	 */
    public static int StringToInt(String str) {
        return Integer.parseInt(str.trim());
    }

    /**
	 * Returns the name of a class (without packages path)
	 * @param classObject the class object to be parsed
	 * @return the name of a class (without packages path)
	 */
    public static String getClassName(Class classObject) {
        String className = classObject.getName();
        int dotIndex = 0;
        if ((dotIndex = className.lastIndexOf('.')) != -1) className = className.substring(dotIndex + 1);
        return className;
    }
}
