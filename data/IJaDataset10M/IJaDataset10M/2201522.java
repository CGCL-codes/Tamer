package org.antlr.runtime3_2_0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A generic recognizer that can handle recognizers generated from
 *  lexer, parser, and tree grammars.  This is all the parsing
 *  support code essentially; most of it is error recovery stuff and
 *  backtracking.
 */
public abstract class BaseRecognizer {

    public static final int MEMO_RULE_FAILED = -2;

    public static final int MEMO_RULE_UNKNOWN = -1;

    public static final int INITIAL_FOLLOW_STACK_SIZE = 100;

    public static final int DEFAULT_TOKEN_CHANNEL = Token.DEFAULT_CHANNEL;

    public static final int HIDDEN = Token.HIDDEN_CHANNEL;

    public static final String NEXT_TOKEN_RULE_NAME = "nextToken";

    /** State of a lexer, parser, or tree parser are collected into a state
	 *  object so the state can be shared.  This sharing is needed to
	 *  have one grammar import others and share same error variables
	 *  and other state variables.  It's a kind of explicit multiple
	 *  inheritance via delegation of methods and shared state.
	 */
    protected RecognizerSharedState state;

    public BaseRecognizer() {
        state = new RecognizerSharedState();
    }

    public BaseRecognizer(RecognizerSharedState state) {
        if (state == null) {
            state = new RecognizerSharedState();
        }
        this.state = state;
    }

    /** reset the parser's state; subclasses must rewinds the input stream */
    public void reset() {
        if (state == null) {
            return;
        }
        state._fsp = -1;
        state.errorRecovery = false;
        state.lastErrorIndex = -1;
        state.failed = false;
        state.syntaxErrors = 0;
        state.backtracking = 0;
        for (int i = 0; state.ruleMemo != null && i < state.ruleMemo.length; i++) {
            state.ruleMemo[i] = null;
        }
    }

    /** Match current input symbol against ttype.  Attempt
	 *  single token insertion or deletion error recovery.  If
	 *  that fails, throw MismatchedTokenException.
	 *
	 *  To turn off single token insertion or deletion error
	 *  recovery, override recoverFromMismatchedToken() and have it
     *  throw an exception. See TreeParser.recoverFromMismatchedToken().
     *  This way any error in a rule will cause an exception and
     *  immediate exit from rule.  Rule would recover by resynchronizing
     *  to the set of symbols that can follow rule ref.
	 */
    public Object match(IntStream input, int ttype, BitSet follow) throws RecognitionException {
        Object matchedSymbol = getCurrentInputSymbol(input);
        if (input.LA(1) == ttype) {
            input.consume();
            state.errorRecovery = false;
            state.failed = false;
            return matchedSymbol;
        }
        if (state.backtracking > 0) {
            state.failed = true;
            return matchedSymbol;
        }
        matchedSymbol = recoverFromMismatchedToken(input, ttype, follow);
        return matchedSymbol;
    }

    /** Match the wildcard: in a symbol */
    public void matchAny(IntStream input) {
        state.errorRecovery = false;
        state.failed = false;
        input.consume();
    }

    public boolean mismatchIsUnwantedToken(IntStream input, int ttype) {
        return input.LA(2) == ttype;
    }

    public boolean mismatchIsMissingToken(IntStream input, BitSet follow) {
        if (follow == null) {
            return false;
        }
        if (follow.member(Token.EOR_TOKEN_TYPE)) {
            BitSet viableTokensFollowingThisRule = computeContextSensitiveRuleFOLLOW();
            follow = follow.or(viableTokensFollowingThisRule);
            if (state._fsp >= 0) {
                follow.remove(Token.EOR_TOKEN_TYPE);
            }
        }
        if (follow.member(input.LA(1)) || follow.member(Token.EOR_TOKEN_TYPE)) {
            return true;
        }
        return false;
    }

    /** Report a recognition problem.
	 *
	 *  This method sets errorRecovery to indicate the parser is recovering
	 *  not parsing.  Once in recovery mode, no errors are generated.
	 *  To get out of recovery mode, the parser must successfully match
	 *  a token (after a resync).  So it will go:
	 *
	 * 		1. error occurs
	 * 		2. enter recovery mode, report error
	 * 		3. consume until token found in resynch set
	 * 		4. try to resume parsing
	 * 		5. next match() will reset errorRecovery mode
	 *
	 *  If you override, make sure to update syntaxErrors if you care about that.
	 */
    public void reportError(RecognitionException e) {
        if (state.errorRecovery) {
            return;
        }
        state.syntaxErrors++;
        state.errorRecovery = true;
        displayRecognitionError(this.getTokenNames(), e);
    }

    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        emitErrorMessage(hdr + " " + msg);
    }

    /** What error message should be generated for the various
	 *  exception types?
	 *
	 *  Not very object-oriented code, but I like having all error message
	 *  generation within one method rather than spread among all of the
	 *  exception classes. This also makes it much easier for the exception
	 *  handling because the exception classes do not have to have pointers back
	 *  to this object to access utility routines and so on. Also, changing
	 *  the message for an exception type would be difficult because you
	 *  would have to subclassing exception, but then somehow get ANTLR
	 *  to make those kinds of exception objects instead of the default.
	 *  This looks weird, but trust me--it makes the most sense in terms
	 *  of flexibility.
	 *
	 *  For grammar debugging, you will want to override this to add
	 *  more information such as the stack frame with
	 *  getRuleInvocationStack(e, this.getClass().getName()) and,
	 *  for no viable alts, the decision description and state etc...
	 *
	 *  Override this to change the message generated for one or more
	 *  exception types.
	 */
    public String getErrorMessage(RecognitionException e, String[] tokenNames) {
        String msg = e.getMessage();
        if (e instanceof UnwantedTokenException) {
            UnwantedTokenException ute = (UnwantedTokenException) e;
            String tokenName = "<unknown>";
            if (ute.expecting == Token.EOF) {
                tokenName = "EOF";
            } else {
                tokenName = tokenNames[ute.expecting];
            }
            msg = "extraneous input " + getTokenErrorDisplay(ute.getUnexpectedToken()) + " expecting " + tokenName;
        } else if (e instanceof MissingTokenException) {
            MissingTokenException mte = (MissingTokenException) e;
            String tokenName = "<unknown>";
            if (mte.expecting == Token.EOF) {
                tokenName = "EOF";
            } else {
                tokenName = tokenNames[mte.expecting];
            }
            msg = "missing " + tokenName + " at " + getTokenErrorDisplay(e.token);
        } else if (e instanceof MismatchedTokenException) {
            MismatchedTokenException mte = (MismatchedTokenException) e;
            String tokenName = "<unknown>";
            if (mte.expecting == Token.EOF) {
                tokenName = "EOF";
            } else {
                tokenName = tokenNames[mte.expecting];
            }
            msg = "mismatched input " + getTokenErrorDisplay(e.token) + " expecting " + tokenName;
        } else if (e instanceof MismatchedTreeNodeException) {
            MismatchedTreeNodeException mtne = (MismatchedTreeNodeException) e;
            String tokenName = "<unknown>";
            if (mtne.expecting == Token.EOF) {
                tokenName = "EOF";
            } else {
                tokenName = tokenNames[mtne.expecting];
            }
            msg = "mismatched tree node: " + mtne.node + " expecting " + tokenName;
        } else if (e instanceof NoViableAltException) {
            msg = "no viable alternative at input " + getTokenErrorDisplay(e.token);
        } else if (e instanceof EarlyExitException) {
            msg = "required (...)+ loop did not match anything at input " + getTokenErrorDisplay(e.token);
        } else if (e instanceof MismatchedSetException) {
            MismatchedSetException mse = (MismatchedSetException) e;
            msg = "mismatched input " + getTokenErrorDisplay(e.token) + " expecting set " + mse.expecting;
        } else if (e instanceof MismatchedNotSetException) {
            MismatchedNotSetException mse = (MismatchedNotSetException) e;
            msg = "mismatched input " + getTokenErrorDisplay(e.token) + " expecting set " + mse.expecting;
        } else if (e instanceof FailedPredicateException) {
            FailedPredicateException fpe = (FailedPredicateException) e;
            msg = "rule " + fpe.ruleName + " failed predicate: {" + fpe.predicateText + "}?";
        }
        return msg;
    }

    /** Get number of recognition errors (lexer, parser, tree parser).  Each
	 *  recognizer tracks its own number.  So parser and lexer each have
	 *  separate count.  Does not count the spurious errors found between
	 *  an error and next valid token match
	 *
	 *  See also reportError()
	 */
    public int getNumberOfSyntaxErrors() {
        return state.syntaxErrors;
    }

    /** What is the error header, normally line/character position information? */
    public String getErrorHeader(RecognitionException e) {
        return "line " + e.line + ":" + e.charPositionInLine;
    }

    /** How should a token be displayed in an error message? The default
	 *  is to display just the text, but during development you might
	 *  want to have a lot of information spit out.  Override in that case
	 *  to use t.toString() (which, for CommonToken, dumps everything about
	 *  the token). This is better than forcing you to override a method in
	 *  your token objects because you don't have to go modify your lexer
	 *  so that it creates a new Java type.
	 */
    public String getTokenErrorDisplay(Token t) {
        String s = t.getText();
        if (s == null) {
            if (t.getType() == Token.EOF) {
                s = "<EOF>";
            } else {
                s = "<" + t.getType() + ">";
            }
        }
        s = s.replaceAll("\n", "\\\\n");
        s = s.replaceAll("\r", "\\\\r");
        s = s.replaceAll("\t", "\\\\t");
        return "'" + s + "'";
    }

    /** Override this method to change where error messages go */
    public void emitErrorMessage(String msg) {
        System.err.println(msg);
    }

    /** Recover from an error found on the input stream.  This is
	 *  for NoViableAlt and mismatched symbol exceptions.  If you enable
	 *  single token insertion and deletion, this will usually not
	 *  handle mismatched symbol exceptions but there could be a mismatched
	 *  token that the match() routine could not recover from.
	 */
    public void recover(IntStream input, RecognitionException re) {
        if (state.lastErrorIndex == input.index()) {
            input.consume();
        }
        state.lastErrorIndex = input.index();
        BitSet followSet = computeErrorRecoverySet();
        beginResync();
        consumeUntil(input, followSet);
        endResync();
    }

    /** A hook to listen in on the token consumption during error recovery.
	 *  The DebugParser subclasses this to fire events to the listenter.
	 */
    public void beginResync() {
    }

    public void endResync() {
    }

    protected BitSet computeErrorRecoverySet() {
        return combineFollows(false);
    }

    /** Compute the context-sensitive FOLLOW set for current rule.
	 *  This is set of token types that can follow a specific rule
	 *  reference given a specific call chain.  You get the set of
	 *  viable tokens that can possibly come next (lookahead depth 1)
	 *  given the current call chain.  Contrast this with the
	 *  definition of plain FOLLOW for rule r:
	 *
	 *   FOLLOW(r)={x | S=>*alpha r beta in G and x in FIRST(beta)}
	 *
	 *  where x in T* and alpha, beta in V*; T is set of terminals and
	 *  V is the set of terminals and nonterminals.  In other words,
	 *  FOLLOW(r) is the set of all tokens that can possibly follow
	 *  references to r in *any* sentential form (context).  At
	 *  runtime, however, we know precisely which context applies as
	 *  we have the call chain.  We may compute the exact (rather
	 *  than covering superset) set of following tokens.
	 *
	 *  For example, consider grammar:
	 *
	 *  stat : ID '=' expr ';'      // FOLLOW(stat)=={EOF}
	 *       | "return" expr '.'
	 *       ;
	 *  expr : atom ('+' atom)* ;   // FOLLOW(expr)=={';','.',')'}
	 *  atom : INT                  // FOLLOW(atom)=={'+',')',';','.'}
	 *       | '(' expr ')'
	 *       ;
	 *
	 *  The FOLLOW sets are all inclusive whereas context-sensitive
	 *  FOLLOW sets are precisely what could follow a rule reference.
	 *  For input input "i=(3);", here is the derivation:
	 *
	 *  stat => ID '=' expr ';'
	 *       => ID '=' atom ('+' atom)* ';'
	 *       => ID '=' '(' expr ')' ('+' atom)* ';'
	 *       => ID '=' '(' atom ')' ('+' atom)* ';'
	 *       => ID '=' '(' INT ')' ('+' atom)* ';'
	 *       => ID '=' '(' INT ')' ';'
	 *
	 *  At the "3" token, you'd have a call chain of
	 *
	 *    stat -> expr -> atom -> expr -> atom
	 *
	 *  What can follow that specific nested ref to atom?  Exactly ')'
	 *  as you can see by looking at the derivation of this specific
	 *  input.  Contrast this with the FOLLOW(atom)={'+',')',';','.'}.
	 *
	 *  You want the exact viable token set when recovering from a
	 *  token mismatch.  Upon token mismatch, if LA(1) is member of
	 *  the viable next token set, then you know there is most likely
	 *  a missing token in the input stream.  "Insert" one by just not
	 *  throwing an exception.
	 */
    protected BitSet computeContextSensitiveRuleFOLLOW() {
        return combineFollows(true);
    }

    protected BitSet combineFollows(boolean exact) {
        int top = state._fsp;
        BitSet followSet = new BitSet();
        for (int i = top; i >= 0; i--) {
            BitSet localFollowSet = (BitSet) state.following[i];
            followSet.orInPlace(localFollowSet);
            if (exact) {
                if (localFollowSet.member(Token.EOR_TOKEN_TYPE)) {
                    if (i > 0) {
                        followSet.remove(Token.EOR_TOKEN_TYPE);
                    }
                } else {
                    break;
                }
            }
        }
        return followSet;
    }

    /** Attempt to recover from a single missing or extra token.
	 *
	 *  EXTRA TOKEN
	 *
	 *  LA(1) is not what we are looking for.  If LA(2) has the right token,
	 *  however, then assume LA(1) is some extra spurious token.  Delete it
	 *  and LA(2) as if we were doing a normal match(), which advances the
	 *  input.
	 *
	 *  MISSING TOKEN
	 *
	 *  If current token is consistent with what could come after
	 *  ttype then it is ok to "insert" the missing token, else throw
	 *  exception For example, Input "i=(3;" is clearly missing the
	 *  ')'.  When the parser returns from the nested call to expr, it
	 *  will have call chain:
	 *
	 *    stat -> expr -> atom
	 *
	 *  and it will be trying to match the ')' at this point in the
	 *  derivation:
	 *
	 *       => ID '=' '(' INT ')' ('+' atom)* ';'
	 *                          ^
	 *  match() will see that ';' doesn't match ')' and report a
	 *  mismatched token error.  To recover, it sees that LA(1)==';'
	 *  is in the set of tokens that can follow the ')' token
	 *  reference in rule atom.  It can assume that you forgot the ')'.
	 */
    protected Object recoverFromMismatchedToken(IntStream input, int ttype, BitSet follow) throws RecognitionException {
        RecognitionException e = null;
        if (mismatchIsUnwantedToken(input, ttype)) {
            e = new UnwantedTokenException(ttype, input);
            beginResync();
            input.consume();
            endResync();
            reportError(e);
            Object matchedSymbol = getCurrentInputSymbol(input);
            input.consume();
            return matchedSymbol;
        }
        if (mismatchIsMissingToken(input, follow)) {
            Object inserted = getMissingSymbol(input, e, ttype, follow);
            e = new MissingTokenException(ttype, input, inserted);
            reportError(e);
            return inserted;
        }
        e = new MismatchedTokenException(ttype, input);
        throw e;
    }

    /** Not currently used */
    public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow) throws RecognitionException {
        if (mismatchIsMissingToken(input, follow)) {
            reportError(e);
            return getMissingSymbol(input, e, Token.INVALID_TOKEN_TYPE, follow);
        }
        throw e;
    }

    /** Match needs to return the current input symbol, which gets put
	 *  into the label for the associated token ref; e.g., x=ID.  Token
	 *  and tree parsers need to return different objects. Rather than test
	 *  for input stream type or change the IntStream interface, I use
	 *  a simple method to ask the recognizer to tell me what the current
	 *  input symbol is.
	 * 
	 *  This is ignored for lexers.
	 */
    protected Object getCurrentInputSymbol(IntStream input) {
        return null;
    }

    /** Conjure up a missing token during error recovery.
	 *
	 *  The recognizer attempts to recover from single missing
	 *  symbols. But, actions might refer to that missing symbol.
	 *  For example, x=ID {f($x);}. The action clearly assumes
	 *  that there has been an identifier matched previously and that
	 *  $x points at that token. If that token is missing, but
	 *  the next token in the stream is what we want we assume that
	 *  this token is missing and we keep going. Because we
	 *  have to return some token to replace the missing token,
	 *  we have to conjure one up. This method gives the user control
	 *  over the tokens returned for missing tokens. Mostly,
	 *  you will want to create something special for identifier
	 *  tokens. For literals such as '{' and ',', the default
	 *  action in the parser or tree parser works. It simply creates
	 *  a CommonToken of the appropriate type. The text will be the token.
	 *  If you change what tokens must be created by the lexer,
	 *  override this method to create the appropriate tokens.
	 */
    protected Object getMissingSymbol(IntStream input, RecognitionException e, int expectedTokenType, BitSet follow) {
        return null;
    }

    public void consumeUntil(IntStream input, int tokenType) {
        int ttype = input.LA(1);
        while (ttype != Token.EOF && ttype != tokenType) {
            input.consume();
            ttype = input.LA(1);
        }
    }

    /** Consume tokens until one matches the given token set */
    public void consumeUntil(IntStream input, BitSet set) {
        int ttype = input.LA(1);
        while (ttype != Token.EOF && !set.member(ttype)) {
            input.consume();
            ttype = input.LA(1);
        }
    }

    /** Push a rule's follow set using our own hardcoded stack */
    protected void pushFollow(BitSet fset) {
        if ((state._fsp + 1) >= state.following.length) {
            BitSet[] f = new BitSet[state.following.length * 2];
            System.arraycopy(state.following, 0, f, 0, state.following.length);
            state.following = f;
        }
        state.following[++state._fsp] = fset;
    }

    /** Return List<String> of the rules in your parser instance
	 *  leading up to a call to this method.  You could override if
	 *  you want more details such as the file/line info of where
	 *  in the parser java code a rule is invoked.
	 *
	 *  This is very useful for error messages and for context-sensitive
	 *  error recovery.
	 */
    public List getRuleInvocationStack() {
        String parserClassName = getClass().getName();
        return getRuleInvocationStack(new Throwable(), parserClassName);
    }

    /** A more general version of getRuleInvocationStack where you can
	 *  pass in, for example, a RecognitionException to get it's rule
	 *  stack trace.  This routine is shared with all recognizers, hence,
	 *  static.
	 *
	 *  TODO: move to a utility class or something; weird having lexer call this
	 */
    public static List getRuleInvocationStack(Throwable e, String recognizerClassName) {
        List rules = new ArrayList();
        StackTraceElement[] stack = e.getStackTrace();
        int i = 0;
        for (i = stack.length - 1; i >= 0; i--) {
            StackTraceElement t = stack[i];
            if (t.getClassName().startsWith("org.antlr.runtime.")) {
                continue;
            }
            if (t.getMethodName().equals(NEXT_TOKEN_RULE_NAME)) {
                continue;
            }
            if (!t.getClassName().equals(recognizerClassName)) {
                continue;
            }
            rules.add(t.getMethodName());
        }
        return rules;
    }

    public int getBacktrackingLevel() {
        return state.backtracking;
    }

    public void setBacktrackingLevel(int n) {
        state.backtracking = n;
    }

    /** Return whether or not a backtracking attempt failed. */
    public boolean failed() {
        return state.failed;
    }

    /** Used to print out token names like ID during debugging and
	 *  error reporting.  The generated parsers implement a method
	 *  that overrides this to point to their String[] tokenNames.
	 */
    public String[] getTokenNames() {
        return null;
    }

    /** For debugging and other purposes, might want the grammar name.
	 *  Have ANTLR generate an implementation for this method.
	 */
    public String getGrammarFileName() {
        return null;
    }

    public abstract String getSourceName();

    /** A convenience method for use most often with template rewrites.
	 *  Convert a List<Token> to List<String>
	 */
    public List toStrings(List tokens) {
        if (tokens == null) return null;
        List strings = new ArrayList(tokens.size());
        for (int i = 0; i < tokens.size(); i++) {
            strings.add(((Token) tokens.get(i)).getText());
        }
        return strings;
    }

    /** Given a rule number and a start token index number, return
	 *  MEMO_RULE_UNKNOWN if the rule has not parsed input starting from
	 *  start index.  If this rule has parsed input starting from the
	 *  start index before, then return where the rule stopped parsing.
	 *  It returns the index of the last token matched by the rule.
	 *
	 *  For now we use a hashtable and just the slow Object-based one.
	 *  Later, we can make a special one for ints and also one that
	 *  tosses out data after we commit past input position i.
	 */
    public int getRuleMemoization(int ruleIndex, int ruleStartIndex) {
        if (state.ruleMemo[ruleIndex] == null) {
            state.ruleMemo[ruleIndex] = new HashMap();
        }
        Integer stopIndexI = (Integer) state.ruleMemo[ruleIndex].get(new Integer(ruleStartIndex));
        if (stopIndexI == null) {
            return MEMO_RULE_UNKNOWN;
        }
        return stopIndexI.intValue();
    }

    /** Has this rule already parsed input at the current index in the
	 *  input stream?  Return the stop token index or MEMO_RULE_UNKNOWN.
	 *  If we attempted but failed to parse properly before, return
	 *  MEMO_RULE_FAILED.
	 *
	 *  This method has a side-effect: if we have seen this input for
	 *  this rule and successfully parsed before, then seek ahead to
	 *  1 past the stop token matched for this rule last time.
	 */
    public boolean alreadyParsedRule(IntStream input, int ruleIndex) {
        int stopIndex = getRuleMemoization(ruleIndex, input.index());
        if (stopIndex == MEMO_RULE_UNKNOWN) {
            return false;
        }
        if (stopIndex == MEMO_RULE_FAILED) {
            state.failed = true;
        } else {
            input.seek(stopIndex + 1);
        }
        return true;
    }

    /** Record whether or not this rule parsed the input at this position
	 *  successfully.  Use a standard java hashtable for now.
	 */
    public void memoize(IntStream input, int ruleIndex, int ruleStartIndex) {
        int stopTokenIndex = state.failed ? MEMO_RULE_FAILED : input.index() - 1;
        if (state.ruleMemo == null) {
            System.err.println("!!!!!!!!! memo array is null for " + getGrammarFileName());
        }
        if (ruleIndex >= state.ruleMemo.length) {
            System.err.println("!!!!!!!!! memo size is " + state.ruleMemo.length + ", but rule index is " + ruleIndex);
        }
        if (state.ruleMemo[ruleIndex] != null) {
            state.ruleMemo[ruleIndex].put(new Integer(ruleStartIndex), new Integer(stopTokenIndex));
        }
    }

    /** return how many rule/input-index pairs there are in total.
	 *  TODO: this includes synpreds. :(
	 */
    public int getRuleMemoizationCacheSize() {
        int n = 0;
        for (int i = 0; state.ruleMemo != null && i < state.ruleMemo.length; i++) {
            Map ruleMap = state.ruleMemo[i];
            if (ruleMap != null) {
                n += ruleMap.size();
            }
        }
        return n;
    }

    public void traceIn(String ruleName, int ruleIndex, Object inputSymbol) {
        System.out.print("enter " + ruleName + " " + inputSymbol);
        if (state.backtracking > 0) {
            System.out.print(" backtracking=" + state.backtracking);
        }
        System.out.println();
    }

    public void traceOut(String ruleName, int ruleIndex, Object inputSymbol) {
        System.out.print("exit " + ruleName + " " + inputSymbol);
        if (state.backtracking > 0) {
            System.out.print(" backtracking=" + state.backtracking);
            if (state.failed) System.out.print(" failed"); else System.out.print(" succeeded");
        }
        System.out.println();
    }
}
