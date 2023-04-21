package org.apache.oro.text;

import java.util.*;
import java.io.*;
import org.apache.oro.text.regex.*;

/**
 * This class is used to provide information regarding a match found by
 * MatchActionProcessor to a MatchAction callback implementation.
 *
 * @version @version@
 * @since 1.0
 * @see MatchAction
 * @see MatchActionProcessor
 */
public final class MatchActionInfo {

    /** The line number of the matching line */
    public int lineNumber;

    /** 
   * The String representation of the matching line with the trailing
   * newline truncated.
   */
    public String line;

    /** 
   * The char[] representation of the matching line with the trailing
   * newline truncated.
   */
    public char[] charLine;

    /**
   * The field separator used by the MatchActionProcessor.  This will be
   * set to null by a MatchActionProcessor instance if no field separator
   * was specified before match processing began.
   */
    public Pattern fieldSeparator;

    /**
   * A List of Strings containing the fields of the line that were
   * separated out by the fieldSeparator.  If no field separator was
   * specified, this variable will be set to null.
   */
    public List fields;

    /** The PatternMatcher used to find the match. */
    public PatternMatcher matcher;

    /**
   * The pattern found in the line of input.  If a MatchAction callback
   * is registered with a null pattern (meaning the callback should be
   * applied to every line of input), this value will be null.
   */
    public Pattern pattern;

    /** 
   * The first match found in the line of input.    If a MatchAction callback
   * is registered with a null pattern (meaning the callback should be
   * applied to every line of input), this value will be null.
   */
    public MatchResult match;

    /** The output stream passed to the MatchActionProcessor.  */
    public PrintWriter output;

    /**
   * The input stream passed to the MatchActionProcessor from which the
   * matching line was read.
   */
    public BufferedReader input;
}
