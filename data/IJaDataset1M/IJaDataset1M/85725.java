package org.renjin.primitives.text.regex;

import java.io.Serializable;
import java.util.Vector;

/**
 * An "extended" regular expression.
 *
 * <h2>Implementation</h2>
 *
 * <p>The implementation is imported from
 * http://jakarta.apache.org/regexp/index.html with minor modifications
 * for R syntax.
 *
 * <p>
 * RE is an efficient, lightweight regular expression evaluator/matcher
 * class. Regular expressions are pattern descriptions which enable
 * sophisticated matching of strings.  In addition to being able to
 * match a string against a pattern, you can also extract parts of the
 * match.  This is especially useful in text parsing! Details on the
 * syntax of regular expression patterns are given below.
 *
 * <p>
 * To compile a regular expression (RE), you can simply construct an RE
 * matcher object from the string specification of the pattern, like this:
 *
 * <pre>
 *  RE r = new RE("a*b");
 * </pre>
 *
 * <p>
 * Once you have done this, you can call either of the RE.match methods to
 * perform matching on a String.  For example:
 *
 * <pre>
 *  boolean matched = r.match("aaaab");
 * </pre>
 *
 * will cause the boolean matched to be set to true because the
 * pattern "a*b" matches the string "aaaab".
 *
 * <p>
 * If you were interested in the <i>number</i> of a's which matched the
 * first part of our example expression, you could change the expression to
 * "(a*)b".  Then when you compiled the expression and matched it against
 * something like "xaaaab", you would get results like this:
 *
 * <pre>
 *  RE r = new RE("(a*)b");                  // Compile expression
 *  boolean matched = r.match("xaaaab");     // Match against "xaaaab"
 *
 *  String wholeExpr = r.getParen(0);        // wholeExpr will be 'aaaab'
 *  String insideParens = r.getParen(1);     // insideParens will be 'aaaa'
 *
 *  int startWholeExpr = r.getParenStart(0); // startWholeExpr will be index 1
 *  int endWholeExpr = r.getParenEnd(0);     // endWholeExpr will be index 6
 *  int lenWholeExpr = r.getParenLength(0);  // lenWholeExpr will be 5
 *
 *  int startInside = r.getParenStart(1);    // startInside will be index 1
 *  int endInside = r.getParenEnd(1);        // endInside will be index 5
 *  int lenInside = r.getParenLength(1);     // lenInside will be 4
 * </pre>
 *
 * You can also refer to the contents of a parenthesized expression
 * within a regular expression itself.  This is called a
 * 'backreference'.  The first backreference in a regular expression is
 * denoted by \1, the second by \2 and so on.  So the expression:
 *
 * <pre>
 *  ([0-9]+)=\1
 * </pre>
 *
 * will match any string of the form n=n (like 0=0 or 2=2).
 *
 * <p>
 * The full regular expression syntax accepted by RE is described here:
 *
 * <pre>
 *
 *  <b><font face=times roman>Characters</font></b>
 *
 *    <i>unicodeChar</i>   Matches any identical unicode character
 *    \                    Used to quote a meta-character (like '*')
 *    \\                   Matches a single '\' character
 *    \0nnn                Matches a given octal character
 *    \xhh                 Matches a given 8-bit hexadecimal character
 *    \\uhhhh              Matches a given 16-bit hexadecimal character
 *    \t                   Matches an ASCII tab character
 *    \n                   Matches an ASCII newline character
 *    \r                   Matches an ASCII return character
 *    \f                   Matches an ASCII form feed character
 *
 *
 *  <b><font face=times roman>Character Classes</font></b>
 *
 *    [abc]                Simple character class
 *    [a-zA-Z]             Character class with ranges
 *    [^abc]               Negated character class
 * </pre>
 *
 * <b>NOTE:</b> Incomplete ranges will be interpreted as &quot;starts
 * from zero&quot; or &quot;ends with last character&quot;.
 * <br>
 * I.e. [-a] is the same as [\\u0000-a], and [a-] is the same as [a-\\uFFFF],
 * [-] means &quot;all characters&quot;.
 *
 * <pre>
 *
 *  <b><font face=times roman>Standard POSIX Character Classes</font></b>
 *
 *    [:alnum:]            Alphanumeric characters.
 *    [:alpha:]            Alphabetic characters.
 *    [:blank:]            Space and tab characters.
 *    [:cntrl:]            Control characters.
 *    [:digit:]            Numeric characters.
 *    [:graph:]            Characters that are printable and are also visible.
 *                         (A space is printable, but not visible, while an
 *                         `a' is both.)
 *    [:lower:]            Lower-case alphabetic characters.
 *    [:print:]            Printable characters (characters that are not
 *                         control characters.)
 *    [:punct:]            Punctuation characters (characters that are not letter,
 *                         digits, control characters, or space characters).
 *    [:space:]            Space characters (such as space, tab, and formfeed,
 *                         to name a few).
 *    [:upper:]            Upper-case alphabetic characters.
 *    [:xdigit:]           Characters that are hexadecimal digits.
 *
 *
 *  <b><font face=times roman>Non-standard POSIX-style Character Classes</font></b>
 *
 *    [:javastart:]        Start of a Java identifier
 *    [:javapart:]         Part of a Java identifier
 *
 *
 *  <b><font face=times roman>Predefined Classes</font></b>
 *
 *    .         Matches any character other than newline
 *    \w        Matches a "word" character (alphanumeric plus "_")
 *    \W        Matches a non-word character
 *    \s        Matches a whitespace character
 *    \S        Matches a non-whitespace character
 *    \d        Matches a digit character
 *    \D        Matches a non-digit character
 *
 *
 *  <b><font face=times roman>Boundary Matchers</font></b>
 *
 *    ^         Matches only at the beginning of a line
 *    $         Matches only at the end of a line
 *    \b        Matches only at a word boundary
 *    \B        Matches only at a non-word boundary
 *
 *
 *  <b><font face=times roman>Greedy Closures</font></b>
 *
 *    A*        Matches A 0 or more times (greedy)
 *    A+        Matches A 1 or more times (greedy)
 *    A?        Matches A 1 or 0 times (greedy)
 *    A{n}      Matches A exactly n times (greedy)
 *    A{n,}     Matches A at least n times (greedy)
 *    A{n,m}    Matches A at least n but not more than m times (greedy)
 *
 *
 *  <b><font face=times roman>Reluctant Closures</font></b>
 *
 *    A*?       Matches A 0 or more times (reluctant)
 *    A+?       Matches A 1 or more times (reluctant)
 *    A??       Matches A 0 or 1 times (reluctant)
 *
 *
 *  <b><font face=times roman>Logical Operators</font></b>
 *
 *    AB        Matches A followed by B
 *    A|B       Matches either A or B
 *    (A)       Used for subexpression grouping
 *   (?:A)      Used for subexpression clustering (just like grouping but
 *              no backrefs)
 *
 *
 *  <b><font face=times roman>Backreferences</font></b>
 *
 *    \1    Backreference to 1st parenthesized subexpression
 *    \2    Backreference to 2nd parenthesized subexpression
 *    \3    Backreference to 3rd parenthesized subexpression
 *    \4    Backreference to 4th parenthesized subexpression
 *    \5    Backreference to 5th parenthesized subexpression
 *    \6    Backreference to 6th parenthesized subexpression
 *    \7    Backreference to 7th parenthesized subexpression
 *    \8    Backreference to 8th parenthesized subexpression
 *    \9    Backreference to 9th parenthesized subexpression
 * </pre>
 *
 * <p>
 * All closure operators (+, *, ?, {m,n}) are greedy by default, meaning
 * that they match as many elements of the string as possible without
 * causing the overall match to fail.  If you want a closure to be
 * reluctant (non-greedy), you can simply follow it with a '?'.  A
 * reluctant closure will match as few elements of the string as
 * possible when finding matches.  {m,n} closures don't currently
 * support reluctancy.
 *
 * <p>
 * <b><font face="times roman">Line terminators</font></b>
 * <br>
 * A line terminator is a one- or two-character sequence that marks
 * the end of a line of the input character sequence. The following
 * are recognized as line terminators:
 * <ul>
 * <li>A newline (line feed) character ('\n'),</li>
 * <li>A carriage-return character followed immediately by a newline character ("\r\n"),</li>
 * <li>A standalone carriage-return character ('\r'),</li>
 * <li>A next-line character (''),</li>
 * <li>A line-separator character (' '), or</li>
 * <li>A paragraph-separator character (' ).</li>
 * </ul>
 *
 * <p>
 * RE runs programs compiled by the RECompiler class.  But the RE
 * matcher class does not include the actual regular expression compiler
 * for reasons of efficiency.  In fact, if you want to pre-compile one
 * or more regular expressions, the 'recompile' class can be invoked
 * from the command line to produce compiled output like this:
 *
 * <pre>
 *    // Pre-compiled regular expression "a*b"
 *    char[] re1Instructions =
 *    {
 *        0x007c, 0x0000, 0x001a, 0x007c, 0x0000, 0x000d, 0x0041,
 *        0x0001, 0x0004, 0x0061, 0x007c, 0x0000, 0x0003, 0x0047,
 *        0x0000, 0xfff6, 0x007c, 0x0000, 0x0003, 0x004e, 0x0000,
 *        0x0003, 0x0041, 0x0001, 0x0004, 0x0062, 0x0045, 0x0000,
 *        0x0000,
 *    };
 *
 *
 *    REProgram re1 = new REProgram(re1Instructions);
 * </pre>
 *
 * You can then construct a regular expression matcher (RE) object from
 * the pre-compiled expression re1 and thus avoid the overhead of
 * compiling the expression at runtime. If you require more dynamic
 * regular expressions, you can construct a single RECompiler object and
 * re-use it to compile each expression. Similarly, you can change the
 * program run by a given matcher object at any time. However, RE and
 * RECompiler are not threadsafe (for efficiency reasons, and because
 * requiring thread safety in this class is deemed to be a rare
 * requirement), so you will need to construct a separate compiler or
 * matcher object for each thread (unless you do thread synchronization
 * yourself). Once expression compiled into the REProgram object, REProgram
 * can be safely shared across multiple threads and RE objects.
 *
 * <br><p><br>
 *
 * <font color="red">
 * <i>ISSUES:</i>
 *
 * <ul>
 *  <li>com.weusours.util.re is not currently compatible with all
 *      standard POSIX regcomp flags</li>
 *  <li>com.weusours.util.re does not support POSIX equivalence classes
 *      ([=foo=] syntax) (I18N/locale issue)</li>
 *  <li>com.weusours.util.re does not support nested POSIX character
 *      classes (definitely should, but not completely trivial)</li>
 *  <li>com.weusours.util.re Does not support POSIX character collation
 *      concepts ([.foo.] syntax) (I18N/locale issue)</li>
 *  <li>Should there be different matching styles (simple, POSIX, Perl etc?)</li>
 *  <li>Should RE support character iterators (for backwards RE matching!)?</li>
 *  <li>Should RE support reluctant {m,n} closures (does anyone care)?</li>
 *  <li>Not *all* possibilities are considered for greediness when backreferences
 *      are involved (as POSIX suggests should be the case).  The POSIX RE
 *      "(ac*)c*d[ac]*\1", when matched against "acdacaa" should yield a match
 *      of acdacaa where \1 is "a".  This is not the case in this RE package,
 *      and actually Perl doesn't go to this extent either!  Until someone
 *      actually complains about this, I'm not sure it's worth "fixing".
 *      If it ever is fixed, test #137 in RETest.txt should be updated.</li>
 * </ul>
 *
 * </font>
 *
 * @see RECompiler
 *
 * @author <a href="mailto:jonl@muppetlabs.com">Jonathan Locke</a>
 * @author <a href="mailto:ts@sch-fer.de">Tobias Sch&auml;fer</a>
 * @version $Id$
 */
public class ExtendedRE implements Serializable, RE {

    /**
     * Specifies normal, case-sensitive matching behaviour.
     */
    public static final int MATCH_NORMAL = 0x0000;

    /**
     * Flag to indicate that matching should be case-independent (folded)
     */
    public static final int MATCH_CASEINDEPENDENT = 0x0001;

    /**
     * Newlines should match as BOL/EOL (^ and $)
     */
    public static final int MATCH_MULTILINE = 0x0002;

    /**
     * Consider all input a single body of text - newlines are matched by .
     */
    public static final int MATCH_SINGLELINE = 0x0004;

    static final char OP_END = 'E';

    static final char OP_BOL = '^';

    static final char OP_EOL = '$';

    static final char OP_ANY = '.';

    static final char OP_ANYOF = '[';

    static final char OP_BRANCH = '|';

    static final char OP_ATOM = 'A';

    static final char OP_STAR = '*';

    static final char OP_PLUS = '+';

    static final char OP_MAYBE = '?';

    static final char OP_ESCAPE = '\\';

    static final char OP_OPEN = '(';

    static final char OP_OPEN_CLUSTER = '<';

    static final char OP_CLOSE = ')';

    static final char OP_CLOSE_CLUSTER = '>';

    static final char OP_BACKREF = '#';

    static final char OP_GOTO = 'G';

    static final char OP_NOTHING = 'N';

    static final char OP_CONTINUE = 'C';

    static final char OP_RELUCTANTSTAR = '8';

    static final char OP_RELUCTANTPLUS = '=';

    static final char OP_RELUCTANTMAYBE = '/';

    static final char OP_POSIXCLASS = 'P';

    static final char E_ALNUM = 'w';

    static final char E_NALNUM = 'W';

    static final char E_BOUND = 'b';

    static final char E_NBOUND = 'B';

    static final char E_SPACE = 's';

    static final char E_NSPACE = 'S';

    static final char E_DIGIT = 'd';

    static final char E_NDIGIT = 'D';

    static final char POSIX_CLASS_ALNUM = 'w';

    static final char POSIX_CLASS_ALPHA = 'a';

    static final char POSIX_CLASS_BLANK = 'b';

    static final char POSIX_CLASS_CNTRL = 'c';

    static final char POSIX_CLASS_DIGIT = 'd';

    static final char POSIX_CLASS_GRAPH = 'g';

    static final char POSIX_CLASS_LOWER = 'l';

    static final char POSIX_CLASS_PRINT = 'p';

    static final char POSIX_CLASS_PUNCT = '!';

    static final char POSIX_CLASS_SPACE = 's';

    static final char POSIX_CLASS_UPPER = 'u';

    static final char POSIX_CLASS_XDIGIT = 'x';

    static final char POSIX_CLASS_JSTART = 'j';

    static final char POSIX_CLASS_JPART = 'k';

    static final int maxNode = 65536;

    static final int MAX_PAREN = 16;

    static final int offsetOpcode = 0;

    static final int offsetOpdata = 1;

    static final int offsetNext = 2;

    static final int nodeSize = 3;

    REProgram program;

    transient CharacterIterator search;

    int matchFlags;

    int maxParen = MAX_PAREN;

    transient int parenCount;

    transient int start0;

    transient int end0;

    transient int start1;

    transient int end1;

    transient int start2;

    transient int end2;

    transient int[] startn;

    transient int[] endn;

    transient int[] startBackref;

    transient int[] endBackref;

    /**
     * Constructs a regular expression matcher from a String by compiling it
     * using a new instance of RECompiler.  If you will be compiling many
     * expressions, you may prefer to use a single RECompiler object instead.
     *
     * @param pattern The regular expression pattern to compile.
     * @exception RESyntaxException Thrown if the regular expression has invalid syntax.
     * @see RECompiler
     */
    public ExtendedRE(String pattern) throws RESyntaxException {
        this(pattern, MATCH_NORMAL);
    }

    /**
   * Constructs a regular expression matcher from a String, with options passed
   * in from the R function
   * @param pattern
   * @param ignoreCase
   */
    public ExtendedRE(String pattern, boolean ignoreCase) {
        this(pattern);
        if (ignoreCase) {
            setMatchFlags(MATCH_CASEINDEPENDENT);
        }
    }

    public RE ignoreCase(boolean ignore) {
        if (ignore) {
            setMatchFlags(MATCH_CASEINDEPENDENT);
        }
        return this;
    }

    /**
     * Constructs a regular expression matcher from a String by compiling it
     * using a new instance of RECompiler.  If you will be compiling many
     * expressions, you may prefer to use a single RECompiler object instead.
     *
     * @param pattern The regular expression pattern to compile.
     * @param matchFlags The matching style
     * @exception RESyntaxException Thrown if the regular expression has invalid syntax.
     * @see RECompiler
     */
    public ExtendedRE(String pattern, int matchFlags) throws RESyntaxException {
        this(new RECompiler().compile(pattern), matchFlags);
    }

    /**
     * Construct a matcher for a pre-compiled regular expression from program
     * (bytecode) data.  Permits special flags to be passed in to modify matching
     * behaviour.
     *
     * @param program Compiled regular expression program (see RECompiler and/or recompile)
     * @param matchFlags One or more of the RE match behaviour flags (RE.MATCH_*):
     *
     * <pre>
     *   MATCH_NORMAL              // Normal (case-sensitive) matching
     *   MATCH_CASEINDEPENDENT     // Case folded comparisons
     *   MATCH_MULTILINE           // Newline matches as BOL/EOL
     * </pre>
     *
     * @see RECompiler
     * @see REProgram
     */
    public ExtendedRE(REProgram program, int matchFlags) {
        setProgram(program);
        setMatchFlags(matchFlags);
    }

    /**
     * Construct a matcher for a pre-compiled regular expression from program
     * (bytecode) data.
     *
     * @param program Compiled regular expression program
     */
    public ExtendedRE(REProgram program) {
        this(program, MATCH_NORMAL);
    }

    /**
     * Constructs a regular expression matcher with no initial program.
     * This is likely to be an uncommon practice, but is still supported.
     */
    public ExtendedRE() {
        this((REProgram) null, MATCH_NORMAL);
    }

    /**
     * Converts a 'simplified' regular expression to a full regular expression
     *
     * @param pattern The pattern to convert
     * @return The full regular expression
     */
    public static String simplePatternToFullRegularExpression(String pattern) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            switch(c) {
                case '*':
                    buf.append(".*");
                    break;
                case '.':
                case '[':
                case ']':
                case '\\':
                case '+':
                case '?':
                case '{':
                case '}':
                case '$':
                case '^':
                case '|':
                case '(':
                case ')':
                    buf.append('\\');
                default:
                    buf.append(c);
                    break;
            }
        }
        return buf.toString();
    }

    /**
     * Sets match behaviour flags which alter the way RE does matching.
     * @param matchFlags One or more of the RE match behaviour flags (RE.MATCH_*):
     *
     * <pre>
     *   MATCH_NORMAL              // Normal (case-sensitive) matching
     *   MATCH_CASEINDEPENDENT     // Case folded comparisons
     *   MATCH_MULTILINE           // Newline matches as BOL/EOL
     * </pre>
     */
    public void setMatchFlags(int matchFlags) {
        this.matchFlags = matchFlags;
    }

    /**
     * Returns the current match behaviour flags.
     * @return Current match behaviour flags (RE.MATCH_*).
     *
     * <pre>
     *   MATCH_NORMAL              // Normal (case-sensitive) matching
     *   MATCH_CASEINDEPENDENT     // Case folded comparisons
     *   MATCH_MULTILINE           // Newline matches as BOL/EOL
     * </pre>
     *
     * @see #setMatchFlags
     */
    public int getMatchFlags() {
        return matchFlags;
    }

    /**
     * Sets the current regular expression program used by this matcher object.
     *
     * @param program Regular expression program compiled by RECompiler.
     * @see RECompiler
     * @see REProgram
     */
    public void setProgram(REProgram program) {
        this.program = program;
        if (program != null && program.maxParens != -1) {
            this.maxParen = program.maxParens;
        } else {
            this.maxParen = MAX_PAREN;
        }
    }

    /**
     * Returns the current regular expression program in use by this matcher object.
     *
     * @return Regular expression program
     * @see #setProgram
     */
    public REProgram getProgram() {
        return program;
    }

    /**
     * Returns the number of parenthesized subexpressions available after a successful match.
     *
     * @return Number of available parenthesized subexpressions
     */
    public int getParenCount() {
        return parenCount;
    }

    /**
     * Gets the contents of a parenthesized subexpression after a successful match.
     *
     * @param which Nesting level of subexpression
     * @return String
     */
    public String getParen(int which) {
        int start;
        if (which < parenCount && (start = getParenStart(which)) >= 0) {
            return search.substring(start, getParenEnd(which));
        }
        return null;
    }

    /**
     * Returns the start index of a given paren level.
     *
     * @param which Nesting level of subexpression
     * @return String index
     */
    public final int getParenStart(int which) {
        if (which < parenCount) {
            switch(which) {
                case 0:
                    return start0;
                case 1:
                    return start1;
                case 2:
                    return start2;
                default:
                    if (startn == null) {
                        allocParens();
                    }
                    return startn[which];
            }
        }
        return -1;
    }

    /**
     * Returns the end index of a given paren level.
     *
     * @param which Nesting level of subexpression
     * @return String index
     */
    public final int getParenEnd(int which) {
        if (which < parenCount) {
            switch(which) {
                case 0:
                    return end0;
                case 1:
                    return end1;
                case 2:
                    return end2;
                default:
                    if (endn == null) {
                        allocParens();
                    }
                    return endn[which];
            }
        }
        return -1;
    }

    /**
     * Returns the length of a given paren level.
     *
     * @param which Nesting level of subexpression
     * @return Number of characters in the parenthesized subexpression
     */
    public final int getParenLength(int which) {
        if (which < parenCount) {
            return getParenEnd(which) - getParenStart(which);
        }
        return -1;
    }

    /**
     * Sets the start of a paren level
     *
     * @param which Which paren level
     * @param i Index in input array
     */
    protected final void setParenStart(int which, int i) {
        if (which < parenCount) {
            switch(which) {
                case 0:
                    start0 = i;
                    break;
                case 1:
                    start1 = i;
                    break;
                case 2:
                    start2 = i;
                    break;
                default:
                    if (startn == null) {
                        allocParens();
                    }
                    startn[which] = i;
                    break;
            }
        }
    }

    /**
     * Sets the end of a paren level
     *
     * @param which Which paren level
     * @param i Index in input array
     */
    protected final void setParenEnd(int which, int i) {
        if (which < parenCount) {
            switch(which) {
                case 0:
                    end0 = i;
                    break;
                case 1:
                    end1 = i;
                    break;
                case 2:
                    end2 = i;
                    break;
                default:
                    if (endn == null) {
                        allocParens();
                    }
                    endn[which] = i;
                    break;
            }
        }
    }

    /**
     * Throws an Error representing an internal error condition probably resulting
     * from a bug in the regular expression compiler (or possibly data corruption).
     * In practice, this should be very rare.
     *
     * @param s Error description
     */
    protected void internalError(String s) throws Error {
        throw new Error("RE internal error: " + s);
    }

    /**
     * Performs lazy allocation of subexpression arrays
     */
    private void allocParens() {
        startn = new int[maxParen];
        endn = new int[maxParen];
        for (int i = 0; i < maxParen; i++) {
            startn[i] = -1;
            endn[i] = -1;
        }
    }

    /**
     * Try to match a string against a subset of nodes in the program
     *
     * @param firstNode Node to start at in program
     * @param lastNode  Last valid node (used for matching a subexpression without
     *                  matching the rest of the program as well).
     * @param idxStart  Starting position in character array
     * @return Final input array index if match succeeded.  -1 if not.
     */
    protected int matchNodes(int firstNode, int lastNode, int idxStart) {
        int idx = idxStart;
        int next, opcode, opdata;
        int idxNew;
        char[] instruction = program.instruction;
        for (int node = firstNode; node < lastNode; ) {
            opcode = instruction[node];
            next = node + (short) instruction[node + offsetNext];
            opdata = instruction[node + offsetOpdata];
            switch(opcode) {
                case OP_MAYBE:
                case OP_STAR:
                    {
                        if ((idxNew = matchNodes(node + nodeSize, maxNode, idx)) != -1) {
                            return idxNew;
                        }
                        break;
                    }
                case OP_PLUS:
                    {
                        if ((idxNew = matchNodes(next, maxNode, idx)) != -1) {
                            return idxNew;
                        }
                        node = next + (short) instruction[next + offsetNext];
                        continue;
                    }
                case OP_RELUCTANTMAYBE:
                case OP_RELUCTANTSTAR:
                    {
                        if ((idxNew = matchNodes(next, maxNode, idx)) != -1) {
                            return idxNew;
                        }
                        return matchNodes(node + nodeSize, next, idx);
                    }
                case OP_RELUCTANTPLUS:
                    {
                        if ((idxNew = matchNodes(next + (short) instruction[next + offsetNext], maxNode, idx)) != -1) {
                            return idxNew;
                        }
                        break;
                    }
                case OP_OPEN:
                    if ((program.flags & REProgram.OPT_HASBACKREFS) != 0) {
                        startBackref[opdata] = idx;
                    }
                    if ((idxNew = matchNodes(next, maxNode, idx)) != -1) {
                        if (opdata >= parenCount) {
                            parenCount = opdata + 1;
                        }
                        if (getParenStart(opdata) == -1) {
                            setParenStart(opdata, idx);
                        }
                    }
                    return idxNew;
                case OP_CLOSE:
                    if ((program.flags & REProgram.OPT_HASBACKREFS) != 0) {
                        endBackref[opdata] = idx;
                    }
                    if ((idxNew = matchNodes(next, maxNode, idx)) != -1) {
                        if (opdata >= parenCount) {
                            parenCount = opdata + 1;
                        }
                        if (getParenEnd(opdata) == -1) {
                            setParenEnd(opdata, idx);
                        }
                    }
                    return idxNew;
                case OP_BACKREF:
                    {
                        int s = startBackref[opdata];
                        int e = endBackref[opdata];
                        if (s == -1 || e == -1) {
                            return -1;
                        }
                        if (s == e) {
                            break;
                        }
                        int l = e - s;
                        if (search.isEnd(idx + l - 1)) {
                            return -1;
                        }
                        final boolean caseFold = ((matchFlags & MATCH_CASEINDEPENDENT) != 0);
                        for (int i = 0; i < l; i++) {
                            if (compareChars(search.charAt(idx++), search.charAt(s + i), caseFold) != 0) {
                                return -1;
                            }
                        }
                    }
                    break;
                case OP_BOL:
                    if (idx != 0) {
                        if ((matchFlags & MATCH_MULTILINE) == MATCH_MULTILINE) {
                            if (isNewline(idx - 1)) {
                                break;
                            }
                        }
                        return -1;
                    }
                    break;
                case OP_EOL:
                    if (!search.isEnd(0) && !search.isEnd(idx)) {
                        if ((matchFlags & MATCH_MULTILINE) == MATCH_MULTILINE) {
                            if (isNewline(idx)) {
                                break;
                            }
                        }
                        return -1;
                    }
                    break;
                case OP_ESCAPE:
                    switch(opdata) {
                        case E_NBOUND:
                        case E_BOUND:
                            {
                                char cLast = ((idx == 0) ? '\n' : search.charAt(idx - 1));
                                char cNext = ((search.isEnd(idx)) ? '\n' : search.charAt(idx));
                                if ((Character.isLetterOrDigit(cLast) == Character.isLetterOrDigit(cNext)) == (opdata == E_BOUND)) {
                                    return -1;
                                }
                            }
                            break;
                        case E_ALNUM:
                        case E_NALNUM:
                        case E_DIGIT:
                        case E_NDIGIT:
                        case E_SPACE:
                        case E_NSPACE:
                            if (search.isEnd(idx)) {
                                return -1;
                            }
                            char c = search.charAt(idx);
                            switch(opdata) {
                                case E_ALNUM:
                                case E_NALNUM:
                                    if (!((Character.isLetterOrDigit(c) || c == '_') == (opdata == E_ALNUM))) {
                                        return -1;
                                    }
                                    break;
                                case E_DIGIT:
                                case E_NDIGIT:
                                    if (!(Character.isDigit(c) == (opdata == E_DIGIT))) {
                                        return -1;
                                    }
                                    break;
                                case E_SPACE:
                                case E_NSPACE:
                                    if (!(Character.isWhitespace(c) == (opdata == E_SPACE))) {
                                        return -1;
                                    }
                                    break;
                            }
                            idx++;
                            break;
                        default:
                            internalError("Unrecognized escape '" + opdata + "'");
                    }
                    break;
                case OP_ANY:
                    if ((matchFlags & MATCH_SINGLELINE) == MATCH_SINGLELINE) {
                        if (search.isEnd(idx)) {
                            return -1;
                        }
                    } else {
                        if (search.isEnd(idx) || isNewline(idx)) {
                            return -1;
                        }
                    }
                    idx++;
                    break;
                case OP_ATOM:
                    {
                        if (search.isEnd(idx)) {
                            return -1;
                        }
                        int startAtom = node + nodeSize;
                        if (search.isEnd(opdata + idx - 1)) {
                            return -1;
                        }
                        final boolean caseFold = ((matchFlags & MATCH_CASEINDEPENDENT) != 0);
                        for (int i = 0; i < opdata; i++) {
                            if (compareChars(search.charAt(idx++), instruction[startAtom + i], caseFold) != 0) {
                                return -1;
                            }
                        }
                    }
                    break;
                case OP_POSIXCLASS:
                    {
                        if (search.isEnd(idx)) {
                            return -1;
                        }
                        switch(opdata) {
                            case POSIX_CLASS_ALNUM:
                                if (!Character.isLetterOrDigit(search.charAt(idx))) {
                                    return -1;
                                }
                                break;
                            case POSIX_CLASS_ALPHA:
                                if (!Character.isLetter(search.charAt(idx))) {
                                    return -1;
                                }
                                break;
                            case POSIX_CLASS_DIGIT:
                                if (!Character.isDigit(search.charAt(idx))) {
                                    return -1;
                                }
                                break;
                            case POSIX_CLASS_BLANK:
                                if (!Character.isSpaceChar(search.charAt(idx))) {
                                    return -1;
                                }
                                break;
                            case POSIX_CLASS_SPACE:
                                if (!Character.isWhitespace(search.charAt(idx))) {
                                    return -1;
                                }
                                break;
                            case POSIX_CLASS_CNTRL:
                                if (Character.getType(search.charAt(idx)) != Character.CONTROL) {
                                    return -1;
                                }
                                break;
                            case POSIX_CLASS_GRAPH:
                                switch(Character.getType(search.charAt(idx))) {
                                    case Character.MATH_SYMBOL:
                                    case Character.CURRENCY_SYMBOL:
                                    case Character.MODIFIER_SYMBOL:
                                    case Character.OTHER_SYMBOL:
                                        break;
                                    default:
                                        return -1;
                                }
                                break;
                            case POSIX_CLASS_LOWER:
                                if (Character.getType(search.charAt(idx)) != Character.LOWERCASE_LETTER) {
                                    return -1;
                                }
                                break;
                            case POSIX_CLASS_UPPER:
                                if (Character.getType(search.charAt(idx)) != Character.UPPERCASE_LETTER) {
                                    return -1;
                                }
                                break;
                            case POSIX_CLASS_PRINT:
                                if (Character.getType(search.charAt(idx)) == Character.CONTROL) {
                                    return -1;
                                }
                                break;
                            case POSIX_CLASS_PUNCT:
                                {
                                    int type = Character.getType(search.charAt(idx));
                                    switch(type) {
                                        case Character.DASH_PUNCTUATION:
                                        case Character.START_PUNCTUATION:
                                        case Character.END_PUNCTUATION:
                                        case Character.CONNECTOR_PUNCTUATION:
                                        case Character.OTHER_PUNCTUATION:
                                            break;
                                        default:
                                            return -1;
                                    }
                                }
                                break;
                            case POSIX_CLASS_XDIGIT:
                                {
                                    boolean isXDigit = ((search.charAt(idx) >= '0' && search.charAt(idx) <= '9') || (search.charAt(idx) >= 'a' && search.charAt(idx) <= 'f') || (search.charAt(idx) >= 'A' && search.charAt(idx) <= 'F'));
                                    if (!isXDigit) {
                                        return -1;
                                    }
                                }
                                break;
                            case POSIX_CLASS_JSTART:
                                if (!Character.isJavaIdentifierStart(search.charAt(idx))) {
                                    return -1;
                                }
                                break;
                            case POSIX_CLASS_JPART:
                                if (!Character.isJavaIdentifierPart(search.charAt(idx))) {
                                    return -1;
                                }
                                break;
                            default:
                                internalError("Bad posix class");
                                break;
                        }
                        idx++;
                    }
                    break;
                case OP_ANYOF:
                    {
                        if (search.isEnd(idx)) {
                            return -1;
                        }
                        char c = search.charAt(idx);
                        boolean caseFold = (matchFlags & MATCH_CASEINDEPENDENT) != 0;
                        int idxRange = node + nodeSize;
                        int idxEnd = idxRange + (opdata * 2);
                        boolean match = false;
                        for (int i = idxRange; !match && i < idxEnd; ) {
                            char s = instruction[i++];
                            char e = instruction[i++];
                            match = ((compareChars(c, s, caseFold) >= 0) && (compareChars(c, e, caseFold) <= 0));
                        }
                        if (!match) {
                            return -1;
                        }
                        idx++;
                    }
                    break;
                case OP_BRANCH:
                    {
                        if (instruction[next] != OP_BRANCH) {
                            node += nodeSize;
                            continue;
                        }
                        int nextBranch;
                        do {
                            if ((idxNew = matchNodes(node + nodeSize, maxNode, idx)) != -1) {
                                return idxNew;
                            }
                            nextBranch = (short) instruction[node + offsetNext];
                            node += nextBranch;
                        } while (nextBranch != 0 && (instruction[node] == OP_BRANCH));
                        return -1;
                    }
                case OP_OPEN_CLUSTER:
                case OP_CLOSE_CLUSTER:
                case OP_NOTHING:
                case OP_GOTO:
                    break;
                case OP_CONTINUE:
                    node += nodeSize;
                    continue;
                case OP_END:
                    setParenEnd(0, idx);
                    return idx;
                default:
                    internalError("Invalid opcode '" + opcode + "'");
            }
            node = next;
        }
        internalError("Corrupt program");
        return -1;
    }

    /**
     * Match the current regular expression program against the current
     * input string, starting at index i of the input string.  This method
     * is only meant for internal use.
     *
     * @param i The input string index to start matching at
     * @return True if the input matched the expression
     */
    protected boolean matchAt(int i) {
        start0 = -1;
        end0 = -1;
        start1 = -1;
        end1 = -1;
        start2 = -1;
        end2 = -1;
        startn = null;
        endn = null;
        parenCount = 1;
        setParenStart(0, i);
        if ((program.flags & REProgram.OPT_HASBACKREFS) != 0) {
            startBackref = new int[maxParen];
            endBackref = new int[maxParen];
        }
        int idx;
        if ((idx = matchNodes(0, maxNode, i)) != -1) {
            setParenEnd(0, idx);
            return true;
        }
        parenCount = 0;
        return false;
    }

    public boolean match(String search, int i) {
        return match(new StringCharacterIterator(search), i);
    }

    /**
     * Matches the current regular expression program against a character array,
     * starting at a given index.
     *
     * @param search String to match against
     * @param i Index to start searching at
     * @return True if string matched
     */
    public boolean match(CharacterIterator search, int i) {
        if (program == null) {
            internalError("No RE program to run!");
        }
        this.search = search;
        if ((program.flags & REProgram.OPT_HASBOL) == REProgram.OPT_HASBOL) {
            if ((matchFlags & MATCH_MULTILINE) == 0) {
                return i == 0 && matchAt(i);
            }
            for (; !search.isEnd(i); i++) {
                if (isNewline(i)) {
                    continue;
                }
                if (matchAt(i)) {
                    return true;
                }
                for (; !search.isEnd(i); i++) {
                    if (isNewline(i)) {
                        break;
                    }
                }
            }
            return false;
        }
        if (program.prefix == null) {
            for (; !search.isEnd(i - 1); i++) {
                if (matchAt(i)) {
                    return true;
                }
            }
            return false;
        } else {
            boolean caseIndependent = (matchFlags & MATCH_CASEINDEPENDENT) != 0;
            char[] prefix = program.prefix;
            for (; !search.isEnd(i + prefix.length - 1); i++) {
                int j = i;
                int k = 0;
                boolean match;
                do {
                    match = (compareChars(search.charAt(j++), prefix[k++], caseIndependent) == 0);
                } while (match && k < prefix.length);
                if (k == prefix.length) {
                    if (matchAt(i)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * Matches the current regular expression program against a String.
     *
     * @param search String to match against
     * @return True if string matched
     */
    @Override
    public boolean match(String search) {
        return match(search, 0);
    }

    /**
     * Splits a string into an array of strings on regular expression boundaries.
     * This function works the same way as the Perl function of the same name.
     * Given a regular expression of "[ab]+" and a string to split of
     * "xyzzyababbayyzabbbab123", the result would be the array of Strings
     * "[xyzzy, yyz, 123]".
     *
     * <p>Please note that the first string in the resulting array may be an empty
     * string. This happens when the very first character of input string is
     * matched by the pattern.
     *
     * @param s String to split on this regular exression
     * @return Array of strings
     */
    @Override
    public String[] split(String s) {
        Vector v = new Vector();
        int pos = 0;
        int len = s.length();
        while (pos < len && match(s, pos)) {
            int start = getParenStart(0);
            int newpos = getParenEnd(0);
            if (newpos == pos) {
                v.addElement(s.substring(pos, start + 1));
                newpos++;
            } else {
                v.addElement(s.substring(pos, start));
            }
            pos = newpos;
        }
        String remainder = s.substring(pos);
        if (remainder.length() != 0) {
            v.addElement(remainder);
        }
        String[] ret = new String[v.size()];
        v.copyInto(ret);
        return ret;
    }

    @Override
    public String subst(String substituteIn, String substitution) {
        return subst(substituteIn, substitution, REPLACE_ALL);
    }

    @Override
    public String subst(String substituteIn, String substitution, int flags) {
        StringBuffer ret = new StringBuffer();
        int pos = 0;
        int len = substituteIn.length();
        while (pos < len && match(substituteIn, pos)) {
            ret.append(substituteIn.substring(pos, getParenStart(0)));
            if ((flags & REPLACE_BACKREFERENCES) != 0) {
                int lCurrentPosition = 0;
                int lLastPosition = -2;
                int lLength = substitution.length();
                while ((lCurrentPosition = substitution.indexOf("\\", lCurrentPosition)) >= 0) {
                    if ((lCurrentPosition == 0 || substitution.charAt(lCurrentPosition - 1) != '\\') && lCurrentPosition + 1 < lLength) {
                        char c = substitution.charAt(lCurrentPosition + 1);
                        if (c >= '0' && c <= '9') {
                            ret.append(substitution.substring(lLastPosition + 2, lCurrentPosition));
                            String val = getParen(c - '0');
                            if (val != null) {
                                ret.append(val);
                            }
                            lLastPosition = lCurrentPosition;
                        }
                    }
                    lCurrentPosition++;
                }
                ret.append(substitution.substring(lLastPosition + 2, lLength));
            } else {
                ret.append(substitution);
            }
            int newpos = getParenEnd(0);
            if (newpos == pos) {
                newpos++;
            }
            pos = newpos;
            if ((flags & REPLACE_FIRSTONLY) != 0) {
                break;
            }
        }
        if (pos < len) {
            ret.append(substituteIn.substring(pos));
        }
        return ret.toString();
    }

    /**
     * Returns an array of Strings, whose toString representation matches a regular
     * expression. This method works like the Perl function of the same name.  Given
     * a regular expression of "a*b" and an array of String objects of [foo, aab, zzz,
     * aaaab], the array of Strings returned by grep would be [aab, aaaab].
     *
     * @param search Array of Objects to search
     * @return Array of Strings whose toString() value matches this regular expression.
     */
    public String[] grep(Object[] search) {
        Vector v = new Vector();
        for (int i = 0; i < search.length; i++) {
            String s = search[i].toString();
            if (match(s)) {
                v.addElement(s);
            }
        }
        String[] ret = new String[v.size()];
        v.copyInto(ret);
        return ret;
    }

    /**
     * @return true if character at i-th position in the <code>search</code> string is a newline
     */
    private boolean isNewline(int i) {
        char nextChar = search.charAt(i);
        return nextChar == '\n' || nextChar == '\r' || nextChar == '' || nextChar == ' ' || nextChar == ' ';
    }

    /**
     * Compares two characters.
     *
     * @param c1 first character to compare.
     * @param c2 second character to compare.
     * @param caseIndependent whether comparision is case insensitive or not.
     * @return negative, 0, or positive integer as the first character
     *         less than, equal to, or greater then the second.
     */
    private int compareChars(char c1, char c2, boolean caseIndependent) {
        if (caseIndependent) {
            c1 = Character.toLowerCase(c1);
            c2 = Character.toLowerCase(c2);
        }
        return ((int) c1 - (int) c2);
    }
}
