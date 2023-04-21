package org.j3d.loaders.ac3d.parser;

/**
 * <p><code>LineTokenizer</code> is a simple tokenizer that breaks down 
 * a single line of text into individual word tokens, with the exception 
 * of text surrounded by quotes. This is actually something that could (and 
 * maybe should) be implemented using a <code>StreamTokenizer</code>. 
 * This is actually a suboptimal process, which should be revisited 
 * when everything is functionally complete.</p>
 *
 * <p><strong>TODO:</strong><ul>
 * <li> Fix transient inclusion of quotes
 * <li> Cleanup, commentary, and optimization.
 * </ul></p>
 *
 * @author  Ryan Wilhm (ryan@entrophica.com)
 * @version $Revision: 1.1 $
 */
public class LineTokenizer {

    /**
     * <p>Returns an array of <code>String</code> objects containing the 
     * individual tokens from the parameter. These tokens were delimited 
     * by whitespace, except for when enclosed in quotes.</p>
     *
     * @param line The input <code>String</code> to decompose.
     * @return The output array of tokens.
     */
    public static final String[] enumerateTokens(String line) {
        String[] rVal = new String[0];
        int startPos = 0, currentPos;
        boolean ignoreSpace = false, usedQuotes = false;
        line.trim();
        for (currentPos = 0; currentPos < line.length(); currentPos++) {
            if (line.charAt(currentPos) == '\"') {
                ignoreSpace = !ignoreSpace;
                usedQuotes = true;
            }
            if (!ignoreSpace && (line.charAt(currentPos) == ' ')) {
                if (usedQuotes) {
                    rVal = addStringToArray(rVal, line.substring(startPos + 1, currentPos - 1));
                    usedQuotes = false;
                } else if ((currentPos - startPos) > 0) {
                    rVal = addStringToArray(rVal, line.substring(startPos, currentPos));
                }
                startPos = currentPos + 1;
            }
        }
        if (currentPos > startPos) {
            if (usedQuotes) {
                rVal = addStringToArray(rVal, line.substring(startPos + 1, currentPos - 1));
                usedQuotes = false;
            } else if ((currentPos - startPos) > 0) {
                rVal = addStringToArray(rVal, line.substring(startPos, currentPos));
            }
        }
        return rVal;
    }

    /**
     * <p>Helper method for adding additional array space.</p>
     * 
     * @param in The input array of strings.
     * @param val The value to append to the array in a new space.
     * @return The output array containing both the inputs.
     */
    private static final String[] addStringToArray(String[] in, String val) {
        String[] rVal;
        rVal = new String[in.length + 1];
        System.arraycopy(in, 0, rVal, 0, in.length);
        rVal[in.length] = val;
        return rVal;
    }
}
